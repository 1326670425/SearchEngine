package split;

import java.util.*;


import org.ansj.domain.Result;
import org.ansj.domain.Term;
import org.ansj.splitWord.analysis.*;
import database.DbOperation;


import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.regex.*;

public class Test {
	private DbOperation db = new DbOperation();
	News news;
	public static String  regex = "^[wu]|null";
	//分词
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
	//排序
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
	//查找匹配的news
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
				if(type.equals("1")){
					news.setTitle(title);
					news.setUrl(rs.getString("url"));
					news.setDescription(rs.getString("content"));
					news.setTime(rs.getString("time"));
				}
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
	public void close(){
		db.close();
	}
	//测试
	public static void main(String[] args){
	
	}
}
