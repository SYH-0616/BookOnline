$(function() {
	/*
	 * 1. 得到所有的错误信息，循环遍历之。调用一个方法来确定是否显示错误信息！
	 */
	$(".errorClass").each(function() {
		showError($(this));//遍历每个元素，使用每个元素来调用showError方法
	});
	
	/*
	 * 2. 切换注册按钮的图片
	 */
	$("#submitBtn").hover(
		function() {
			$("#submitBtn").attr("src", "/BookOnline/images/regist2.jpg");
		},
		function() {
			$("#submitBtn").attr("src", "/BookOnline/images/regist1.jpg");
		}
	);
	/*
	 * 3. 输入框得到焦点隐藏
	 */
	$(".inputClass").focus(function(){
		var lableId = $(this).attr("id")+"Error";//得到error label
		$("#"+lableId).text("");//把label清空
		showError($("#"+lableId));//隐藏没有信息的lable
	});
	/*
	 * 4.输入框失去焦点，显示
	 */
	$(".inputClass").blur(function(){
		var id = $(this).attr("id");//得到当前输入框id
		var funName="validate"+id.substring(0,1).toUpperCase()+id.substring(1)+"()";//得到检验方法名。
		eval(funName);//eval里面当成相应代码来执行,相当于调用了对应的
	});
	/*
	 *5.表单提交验证
	 *
	 */
	$("#tableForm").submit(function(){
		var bool = true;//表示校验通过
		if(!validateLoginname()){
			bool = false;
		}
		if(!validateLoginpass()){
			bool = false;
		}
		if(!validateReloginpass()){
			bool = false;
		}
		if(!validateEmail()){
			bool = false;
		}
		if(!validateVerifyCode()){
			bool = false;
		}
		return bool;
		
	});
});

/*
 * 登录名校验方法
 */
function validateLoginname(){
	
	var id = "loginname";
	var value = $("#"+id).val();//获取输入框内容
	//1.非空校验
	if(!value){
		//获取对应label
		//添加错误信息
		//显示错误信息
		$("#"+id+"Error").text("用户名不能为空");
		showError($("#"+id+"Error"));
		return false;
	}
	//2.长度校验
	if(value.length < 6||value.length > 20){
		//获取对应label
		//添加错误信息
		//显示错误信息
		$("#"+id+"Error").text("用户名必须在6~20字符之间");
		showError($("#"+id+"Error"));
		return false;
	}
	//3.是否注册校验
	//服务器校验
	$.ajax({
		url:"/BookOnline/UserServlet",
		data:{method:"ajaxValidateLoginname", loginname:value},
		type:"POST",
		dataType:"json",
		aysnc:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#"+id+"Error").text("用户名已经被注册！");
				showError($("#"+id+"Error"));
				return false;
			}
		}
		
	});
	
	return true;
} 
/*
 * 登录密码校验
 */
function validateLoginpass(){
	var id = "loginpass";
	var value = $("#"+id).val();//获取输入框内容
	//1.非空校验
	if(!value){
		//获取对应label
		//添加错误信息
		//显示错误信息
		$("#"+id+"Error").text("密码不能为空");
		showError($("#"+id+"Error"));
		return false;
	}
	//2.长度校验
	if(value.length<6||value.length>20){
		//获取对应label
		//添加错误信息
		//显示错误信息
		$("#"+id+"Error").text("密码长度必须在6~20个字符 ");
		showError($("#"+id+"Error"));
		return false;
	}
	
	return true;
}
/*
 * 再次输入密码校验
 */
function validateReloginpass(){
	var id = "reloginpass";
	var value = $("#"+id).val();//获取输入框内容
	//1.非空校验
	if(!value){
		//获取对应label
		//添加错误信息
		//显示错误信息
		$("#"+id+"Error").text("请再次输入密码");
		showError($("#"+id+"Error"));
		return false;
	}
	//2.两次输入是否一致校验
	if(value !=$("#loginpass").val()){
		$("#"+id+"Error").text("两次密码输入不一致");
		showError($("#" + id + "Error"));
		return false;
	}
	
	return true;
}
/*
 * Email校验
 */

function validateEmail(){
	var id = "email";
	var value = $("#"+id).val();//获取输入框内容
	//1.非空校验
	if(!value){
		//获取对应label
		//添加错误信息
		//显示错误信息
		$("#"+id+"Error").text("Email不能为空");
		showError($("#"+id+"Error"));
		return false;
	}
	//2.email格式是否正确
	if(!/^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\.[a-zA-Z0-9_-]{2,3}){1,2})$/.test(value)){
		$("#"+id+"Error").text("错误的Email格式");
		showError($("#" + id + "Error"));
		return false;
	}
	//3.是否注册校验
	//服务器校验
	$.ajax({
		url:"/BookOnline/UserServlet",
		data:{method:"ajaxValidateEmail", email:value},
		type:"POST",
		dataType:"json",
		aysnc:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#"+id+"Error").text("邮箱已经被注册！");
				showError($("#"+id+"Error"));
				return false;
			}
		}
	});
	return true;
}
/*
 * 验证码校验
 */
function validateVerifyCode(){
	var id = "verifyCode";
	var value = $("#"+id).val();//获取输入框内容
	//1.非空校验
	if(!value){
		//获取对应label
		//添加错误信息
		//显示错误信息
		$("#"+id+"Error").text("验证码不能为空");
		showError($("#"+id+"Error"));
		return false;
	}
	//2.长度校验
	if(value.length!=4){
		$("#"+id+"Error").text("错误的验证码");
		showError($("#" + id + "Error"));
		return false;
	}
	//服务器校验
	$.ajax({
		url:"/BookOnline/UserServlet",
		data:{method:"ajaxValidateVerifyCode", verifyCode:value},
		type:"POST",
		dataType:"json",
		aysnc:false,
		cache:false,
		success:function(result){
			if(!result){
				$("#"+id+"Error").text("验证码错误!");
				showError($("#"+id+"Error"));
				return false;
			}
		}
		
	});
	return true;
}

/*
 * 判断当前元素是否存在内容，如果存在显示，不页面不显示！
 */
function showError(ele) {
	var text = ele.text();//获取元素的内容
	if(!text) {//如果没有内容
		ele.css("display", "none");//隐藏元素
	} else {//如果有内容
		ele.css("display", "");//显示元素
	}
}

/*
 * 换一张验证码
 */
function _hyz() {
	/*
	 * 1. 获取<img>元素
	 * 2. 重新设置它的src
	 * 3. 使用毫秒来添加参数
	 */
	$("#imgVerifyCode").attr("src", "/BookOnline/VerifyCodeServlet?a=" + new Date().getTime());
}
