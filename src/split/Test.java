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
 * @Description 处理输入内容，分词、排序、搜索
 * @author 吴扬颉
 * @date 2018年6月8日
 *
 */
public class Test {
	
	private DbOperation db = new DbOperation();
	News news;
	
	public static String  regex = "^[wu]|null";
	/**
	 * 
	 * @Title splitWord
	 * @Description 搜索内容分词
	 * @param str 输入的文本
	 * @param sql SQL语句
	 * @return List<List<String>> 关键词列表和文档列表
	 */
	public List<List<String>> splitWord(String str,String sql){
		
		//System.out.println(str);
		Result result = ToAnalysis.parse(str);
		List<Term> terms = result.getTerms();
		//关键词列表
		List<String> words = new ArrayList<String>();
		//文档索引列表
		List<String> docs = new ArrayList<String>();
		
		List<List<String>> list = new ArrayList<List<String>>();

		db.createPStatement(sql);
		String keyWord = "";
		ResultSet rs = null;
		Pattern p = Pattern.compile(regex);
		try{
			for(int i=0;i<terms.size();i++){
				//过滤标点符号和助词
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
	 * @Description 文档排序
	 * @param docs 匹配的原始文档列表
	 * @return List<String> 按出现次数排序后的文档列表
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
		

		//对文档出现次数排序
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
		//生成查询URL列表（已按文档频率排序）
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
	 * @Description 搜索文档
	 * @param showList 待检索的文档编号列表
	 * @param keyList 关键字列
	 * @return List<News> 检索到的文档消息列表
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
				//网页搜索
				if(type.equals("1")){

					news.setTitle(title);
					news.setUrl(rs.getString("url"));
					news.setDescription(rs.getString("description"));
					news.setTime(rs.getString("time"));
				}
				//问答搜索
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
	 * @Description 提取关键字
	 * @param title 问题标题
	 * @param detail 问题内容
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
	 * @Description 关闭连接
	 * @param 
	 * @return void
	 */
	public void close(){
		db.close();
	}
	//测试
	public static void main(String[] args){

	}
}
