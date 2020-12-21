layui.use(['element','carousel','laypage'], function(){
  var element = layui.element;
  var carousel = layui.carousel;
	//建造实例
	carousel.render({
	  elem: '#test1',
	  width: '100%' ,//设置容器宽度
	  height: '459px',
	  arrow: 'always' //始终显示箭头
	  //,anim: 'updown' //切换动画方式
	});

});

$(function() {// 初始化内容
	var htmlLan = "zh_CN";
	// console.log("adsd:",htmlLan)
	var urlLan = getUrlParam("lang");
	if(urlLan!=null&&urlLan!=''){
		htmlLan = urlLan
		console.log(htmlLan)
	}
	lanChange(htmlLan);
	var aList = $(".lang-change");
	for(var i=0;i<aList.length;i++){
		var href = aList[i].href;
		aList[i].href=href+"?lang="+htmlLan;
	}

	$("#enLan").click(function (){
		htmlLan = "en_US";
	})

	$("#cnLan").click(function (){
		htmlLan = "zh_CN";
	})



});

function getUrlParam(name) {
	var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
	var r = window.location.search.substr(1).match(reg);  //匹配目标参数
	if (r != null) return unescape(r[2]); return null; //返回参数值
}

function lanChange(htmlLan){
	if(htmlLan=="zh_CN"){
		$(".categoryCn").show();
		$(".categoryEn").hide();
		$("#logoName").removeAttr("style");
	}else if(htmlLan=="en_US"){
		$(".categoryCn").hide();
		$(".categoryEn").show();
		$("#logoName").css("width","200px")
	}
}





