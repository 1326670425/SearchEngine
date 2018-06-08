<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="database.DbOperation,java.sql.*,java.text.SimpleDateFormat"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人记录</title>
<jsp:include page="head.html"></jsp:include>
<style>
	div.items{
	border: 5px solid #ddd;
	padding: 5px;
	margin:auto 5% auto 5%"
	}
	.items a{
		font: bold 15px/35px arial, sans-serif;
		text-decoration:none;
		color:#00f;
		}
	}
</style>
</head>
<body>
	<jsp:useBean id="db" scope="page" class="database.DbOperation"></jsp:useBean>
	<%
		String username = request.getParameter("username");
		String type = request.getParameter("type");
		String sql = null;
		ResultSet rs = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		if(type.equals("A")){
			sql = "select detail,date,qid from answer where user = ?";
			db.createPStatement(sql);
			rs = db.executeQuery(new String[]{username});
			while(rs.next()){
	%>
			<div class="items">
			<p><span><%= rs.getString("detail") %></span><br>
			<span><a href="QandAServlet?id=<%= rs.getString("qid")%>">查看源问题</a></span>
			<span><%= sdf.format(rs.getTimestamp("date")) %></span>
			</p>
			</div><br>
	<%
			}
		}
		else{
			sql = "select * from question where user = ?";
			db.createPStatement(sql);
			rs = db.executeQuery(new String[]{username});
			while(rs.next()){
	%>
			<div class="items">
			<a href="QandAServlet?id=<%= rs.getString("id") %>" target="_blank"> <%= rs.getString("title") %> </a>
			<p><span><%= rs.getString("detail") %></span><br>
			<span><%= sdf.format(rs.getTimestamp("date")) %></span>
			</p>
			</div><br>
	<%
			}
		}
		rs.close();
		db.close();
	%>
</body>
</html>