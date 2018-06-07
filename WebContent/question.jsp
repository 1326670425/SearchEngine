<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import=" java.util.*, split.News" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<% request.setCharacterEncoding("utf-8"); %>
<title><%=request.getAttribute("title") %></title>
<style>
span{font-size:20px}
</style>
<jsp:include page="head.html"></jsp:include>
<script>
	$(document).ready(function(){
		$("#submit").click(function(){
			var username = $.cookie("username");
			var password = $.cookie("password");
			if(username=="null"&&password=="null"){
				alert("请先登录");
			}
			else{
				$.post("AjaxServlet",
						{username:username,password:password,method:"submit",detail:$("#content").val(),qid:$("h2").attr("docId")},
						function(data){
							alert("提交成功");	
							window.location.reload();
						}
				);
			}
		});
	});
</script>
</head>
<body>

	<div align="center">
	<%
		@SuppressWarnings("unchecked")
		List<News> newList = (List<News>)request.getAttribute("list");
		Iterator<News> it = newList.iterator();
		//提问块
		News news = (News)it.next();
	%>
		<div id="questioner">
		<h2 docId=<%= news.getId() %>><%= news.getTitle() %></h2>
		<p align="left"><span>提问者：<%= news.getUser() %></span></p>
		<p><span><%= news.getDescription() %></span><br></p>
		<p align="right"><span><%= news.getTime() %></span></p>

		</div>
		<hr>
		<div id="response">
		<label>回复</label>
		<form action="" method="post" onsubmit="return false">
			<textarea cols="100" rows="10" id="content" name="content"></textarea>
			<input type="submit" id="submit" value="提交" style="height:30px;width:60px;background:#6BC30D;color:#fff">
		</form>
		</div>
		<hr>

	<%
		//回答块
		while(it.hasNext()){
			news = (News)it.next();
	%>
		<div id="answer">
		<p align="left"><span>回答者：<%= news.getUser() %></span></p>
		<p><span><%= news.getDescription() %></span><br>
		<p align="right"><span><%= news.getTime() %></span></p>
		</div><hr>
		
	<%
			it.remove();
		}
	%>
	</div>
	
</body>
</html>