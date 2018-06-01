$(document).ready(function(){
	
	var input = $("#input");
	var lastTime;
	input.keyup(function(event){
		var text= input.val();
		if(text!=""){
			lastTime = event.timeStamp;
			setTimeout(function(){
				if(lastTime-event.timeStamp==0){
					var ul = $("#list");
					ul.empty();
					$.get("AjaxServlet",
							{keyword:text,"method":"search","type":$("input[name='type']:checked").val()},
							function(data,status){
								var items = data.split("-");
								if(items.length>1){
									for(var i=0;i<items.length-1;i++){
										var li = "<li class='line'>"+items[i]+"</li>";
										ul.append(li); 
									}
									$("#items").show();
									$(".line").click(function(){
										input.val($(this).text());
									});
									
/*								    $(".line").hover(function() {
								        $(this).addClass('hover');
								    }, function() {
								        $(this).removeClass('hover');
								    });*/
									
								}
								else
									$("#items").hide();

							},
						"text");

				}
			},1000);
		}

	});
	
});