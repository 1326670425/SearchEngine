package split;

import java.util.*;

import org.ansj.app.keyword.KeyWordComputer;
import org.ansj.app.keyword.Keyword;
import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.*;
import database.DbOperation;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.regex.*;
/**
 * 
 * @ClassName Test
 * @Description �����������ݣ��ִʡ���������
 * @author �����
 * @date 2018��6��8��
 *
 */
public class Test {
	
	private DbOperation db = new DbOperation();
	News news;
	
	public static String  regex = "^[wu]|null";
	/**
	 * 
	 * @Title splitWord
	 * @Description �������ݷִ�
	 * @param str ������ı�
	 * @param sql SQL���
	 * @return List<List<String>> �ؼ����б���ĵ��б�
	 */
	public List<List<String>> splitWord(String str,String sql){
		
		//System.out.println(str);
		Result result = ToAnalysis.parse(str);
		List<Term> terms = result.getTerms();
		//�ؼ����б�
		List<String> words = new ArrayList<String>();
		//�ĵ������б�
		List<String> docs = new ArrayList<String>();
		
		List<List<String>> list = new ArrayList<List<String>>();

		db.createPStatement(sql);
		String keyWord = "";
		ResultSet rs = null;
		Pattern p = Pattern.compile(regex);
		try{
			for(int i=0;i<terms.size();i++){
				//���˱����ź�����
				if(!p.matcher(terms.get(i).getNatureStr()).matches()){
					keyWord = terms.get(i).getName();

					//System.out.println(keyWord);
					words.add(keyWord);
					//System.out.println(String.format(sql, keyWord));
					rs = db.executeQuery(new String[]{"%"+keyWord+"%"});
					if(!rs.next())
						continue;
					docs.addAll(Arrays.asList(rs.getString("IDlist").split(" ")));
				}
			}
			rs.close();
			
		}catch(SQLException e){
			e.printStackTrace();
		}
/*		System.out.println(words);
		
		for(int j=0;j<words.size();j++)
			System.out.println(words.get(j));*/
		list.add(words);
		list.add(docs);
		return list;
	}
	/**
	 * 
	 * @Title sord
	 * @Description �ĵ�����
	 * @param docs ƥ���ԭʼ�ĵ��б�
	 * @return List<String> �����ִ����������ĵ��б�
	 */
	public List<String> sord(List<String> docs){
		Map<String,Integer> docList = new LinkedHashMap<String,Integer>();
		String temp = "";

		for(int i=0;i<docs.size();i++){
			temp = docs.get(i);

			if(!docList.containsKey(temp))
				docList.put(temp, 1);
			else{
				docList.put(temp, docList.get(temp).intValue()+1);
			}
		}
		

		//���ĵ����ִ�������
		List<Map.Entry<String, Integer>> list = new ArrayList<Map.Entry<String,Integer>>(docList.entrySet());
		Collections.sort(list,new Comparator<Map.Entry<String, Integer>>(){

			@Override
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				// TODO Auto-generated method stub
				int i1 = o1.getValue();
				int i2 = o2.getValue();
				if(i2>i1)
					return 1;
				else if(i2==i1)
					return 0;
				else
					return -1;
			}
			
		});
		//���ɲ�ѯURL�б��Ѱ��ĵ�Ƶ������
		List<String> showList = new ArrayList<String>();
		for(Map.Entry<String, Integer> mapping : list){
			showList.add(mapping.getKey());
		}
		//System.out.println(showList);
		
		return showList;
	}
	/**
	 * 
	 * @Title search
	 * @Description �����ĵ�
	 * @param showList ���������ĵ�����б�
	 * @param keyList �ؼ�����
	 * @return List<News> ���������ĵ���Ϣ�б�
	 */
	public List<News> search(List<String>showList,List<String>keyList,String type){
		String sql = "";
		if(type.equals("1"))
			sql = "select * from news where docID = ?";
		else
			sql = "select * from question where id = ?";
		db.createPStatement(sql);
		
		String title = "";
		String description = "";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		ResultSet rs = null;
		
		List<News> newList = new ArrayList<News>();
		try{
			for(int i=0;i<showList.size();i++){
				rs = db.executeQuery(new String[]{showList.get(i)});
				rs.next();
				news = new News();
				title = rs.getString("title");
				for(int j=0;j<keyList.size();j++){
					String keyword = keyList.get(j);
					title = title.replaceAll(keyword, "<font color=red>"+keyword+"</font>");
				}
				//��ҳ����
				if(type.equals("1")){

					news.setTitle(title);
					news.setUrl(rs.getString("url"));
					news.setDescription(rs.getString("description"));
					news.setTime(rs.getString("time"));
				}
				//�ʴ�����
				else{
					news.setTitle(title);
					news.setUser(rs.getString("user"));
					description = rs.getString("detail");
					news.setDescription(description.length()>50?description.substring(0,50):description);
					news.setUrl("QandAServlet?id="+rs.getString("id"));
					news.setTime(sdf.format(rs.getTimestamp("date")));
				}
				newList.add(news);
			}
			rs.close();
		}catch(SQLException e){
			e.printStackTrace();
		}
		return newList;
	}
	/**
	 * 
	 * @Title extract
	 * @Description ��ȡ�ؼ���
	 * @param title �������
	 * @param detail ��������
	 * @return String[]

	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public String[] extract(String title,String detail){
		KeyWordComputer kwc = new KeyWordComputer(3);
		Collection<Keyword> result = kwc.computeArticleTfidf(title, detail);
		String str[] = new String[result.size()];
		Iterator<Keyword> it = result.iterator();
		for(int i=0;i<str.length;i++){
			str[i] = it.next().getName();
		}
		return str;
	}
	/**
	 * 
	 * @Title close
	 * @Description �ر�����
	 * @param 
	 * @return void
	 */
	public void close(){
		db.close();
	}
	//����
	public static void main(String[] args){

	}
}
