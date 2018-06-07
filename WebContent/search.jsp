<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import=" java.util.*, split.News" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="index.jsp"></jsp:include>

<style type="text/css">
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
  ul#page{
   	list-style-type: none;
  	margin: 50px;
  	width:200px;
  	float: left;
   	display: inline;
   	clear: both;
  }
  ul#page li{
   float: left;
   display: inline;
   width:20px;
   height: 20px;
   margin: 2px;
  }
  ul#page li a {
   text-decoration: none;
   display: block;
   width:20px;
   height:20px;
   border:1px red solid;
   background-color: White;
   line-height: 20px;
   font-size: 12px;
   text-align: center;
  }
  ul#page li a:hover{
  
  width:40px;
  height: 40px;
  line-height: 40px;
  font-size: 32px;
  z-index:100;
  margin: -10px 0 0 -10px;
  }
 </style>

<% request.setCharacterEncoding("utf-8"); %>
<title><%= request.getParameter("keyword") %>——查询结果</title>
	<script>
		$(document).ready(function(){
			$(".searching").css("margin","auto auto auto 2%");
			$("#input").val($("#keyworddd").val());
			
			$("input[name='type'][value="+$("#type").val()+"]").prop("checked",true);

			
		});
		
	</script>
	
</head>

<body>
	<input type="hidden" id="keyworddd" value=<%= request.getParameter("keyword") %>>
	<input type="hidden" id="type" value=<%= request.getParameter("type") %>>

	<jsp:useBean id="serialize" class="split.Serialize"></jsp:useBean>
	<%
		@SuppressWarnings("unchecked")
		List<News> newList = (List<News>)request.getAttribute("news");
		String result = (String)request.getAttribute("result");
		out.println(result);
		
		String text = request.getParameter("keyword");
		if(newList!=null){

			String dlist = serialize.write((List<String>)request.getAttribute("dlist"));
			String klist = serialize.write((List<String>)request.getAttribute("klist"));
			String type = request.getParameter("type");

			int totalPage = (int)request.getAttribute("totalPage");
			Iterator<News> it = newList.iterator();		
			//生成信息块
			while(it.hasNext()){
				News news = (News)it.next();
			%>
			<div class="items">
			<a href=<%= news.getUrl() %> target="_blank"> <%= news.getTitle() %> </a>
			<p><span><%= news.getDescription() %></span><br>
			<span><%= news.getTime() %></span>
			</p>
			</div><br>
			
			<%
				it.remove();
			}
			%>
			<div style="margin:auto 5% auto 5%">
			<ul id="page">
			<%
			//生成页码
			if(totalPage>1){
				for(int i=1;i<=totalPage;i++){
			%>
				<li><a href="SearchServlet2?keyword=<%=text %>&pc=<%= i %>&type=<%= type %>&dlist=<%= dlist %>&klist=<%= klist %>"><span><%= i %></span></a></li>
			<% 
				}
			}

		} 
		%>
		</ul>
		</div>
</body>
</html>