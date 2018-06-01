<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<jsp:include page="head.html"></jsp:include>
<script type="text/javascript" src="js/ajax_search.js"></script>

<style>
.line:hover{background:#007ab8; color:#fff;cursor:pointer}
.line{font-size:20px}
.searching{
	margin:10% 25% 30% 35%
}

</style>

	<script type="text/javascript">
	function check(){
		if(document.getElementById('input').value=="")
			return false;
		else
			return true;
	}

	</script>


<title>首页</title>



</head>
<body style="padding-top:90px">


	<div class="searching">
	<form action="SearchServlet" method="get" onsubmit="return check()">
		<table>
		<tr>
		<td><input type="text" id="input" name="keyword" size=20 style="height:30px;font-size:20px" autocomplete="off" autofocus></td>
		<td><input type="submit" value="搜索" style="height:40px;width:80px;background:#6BC30D;color:#fff"></td>
		<td>&nbsp;&nbsp;<input type="radio" name="type" value="1" checked>网页&nbsp;&nbsp;<input type="radio" name="type" value="2">问答</td>
		</tr>
		<tr><td>
		
		<div id="items" style="display:none;border:1px solid #d3d5d4;border-top:0px">
		<ul style="margin:0;padding:0;list-style:none" id="list">

		</ul>
		</div>
		
		</td></tr>
		</table>
	</form>

	</div>
</body>
</html> 