function setCookie(username,password,bcdl){
	if(bcdl=="checked"){
		$.cookie("username",username,{expires:7});
		$.cookie("password",password,{expires:7});
	}
	else{
		$.cookie("username",username);
		$.cookie("password",password);
	}
}

function getCookie(){
	var username = $.cookie("username");
	var password = $.cookie("password");
	
	if(username!="null" && password!="null"){

		$(".login").css("display","none");
		$(".produter").css("display","block");
		$(".main").text(username);
	}
	else{
		$(".produter").css("display","none");
		$(".login").css("display","block");
	}
}
function deleteCookie(){
	if(confirm("确认退出登录?")){
		$.cookie("username",null);
		$.cookie("password",null);
		window.location.reload();
	}
}
$(document).ready(function(){
	getCookie();
});
