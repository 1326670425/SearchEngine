<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import=" java.util.*, split.News" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<% request.setCharacterEncoding("utf-8"); %>
<title></title>

<jsp:include page="head.html"></jsp:include>
</head>
<body>
	<%
		@SuppressWarnings("unchecked")
		List<News> newList = (List<News>)request.getAttribute("list");
		Iterator<News> it = newList.iterator();
		//提问块
		News news = (News)it.next();
		
		//回答块
		while(it.hasNext()){
			news = (News)it.next();
		%>
		<div>
		<p><span><%= news.getUser() %></span></p>
		<p><span><%= news.getDescription() %></span><br>
		<span><%= news.getTime() %></span>
		</p>
		</div><br>
		
		<%
			it.remove();
		}
		%>
	
	
</body>
</html>