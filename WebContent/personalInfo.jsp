<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="database.DbOperation,java.sql.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人信息</title>
<jsp:include page="head.html"></jsp:include>

</head>
<body>
	<jsp:useBean id="db" scope="page" class="database.DbOperation"></jsp:useBean>
	<%
		String username = request.getParameter("username");
		String sql = "select * from user where name = ?";
		db.createPStatement(sql);
		ResultSet rs = db.executeQuery(new String[]{username});
		rs.next();
	%>
	<div style="margin:15% 20% 30% 40%">
	<table border="1">
	<tr><td>用户名：</td><td><%= username %></td></tr>
	<tr><td>邮箱：</td><td><%= rs.getString("email") %></td></tr>
	<tr><td>联系电话：</td><td><%= rs.getString("tel") %></td></tr>
	</table>
	</div>
	<%
		rs.close();
		db.close();
	%>
</body>
</html>