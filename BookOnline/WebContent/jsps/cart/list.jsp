<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <title>cartlist.jsp</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script src="<c:url value='/jquery/jquery-1.5.1.js'/>"></script>
	<script src="<c:url value='/js/round.js'/>"></script>
	
	<link rel="stylesheet" type="text/css" href="<c:url value='/jsps/css/cart/list.css'/>">
<script type="text/javascript">

$(function(){
	showTotal();
	//给全选添加click事件
	$("#selectAll").click(function(){
		//获取全选状态
		var bool = $("#selectAll").attr("checked");
		setItemCheckBox(bool);
		//设置结算
		setJieSuan(bool);
		//重新计算总计
		showTotal();
	});
	
	//给所有条目的复选框添加click事件
	$(":checkbox[name=checkboxBtn]").click(function(){
		var all = $(":checkbox[name=checkboxBtn]").length;//所有条目的个数
		var select = $(":checkbox[name=checkboxBtn][checked=true]").length;
		if(all == select){
			$("#selectAll").attr("checked",true);//勾选复选框
			setJieSuan(true);//
		}else if(select == 0){
			$("#selectAll").attr("checked",false);
			setJieSuan(false);
		}else{
			$("#selectAll").attr("checked",false);
			setJieSuan(true);
		}
		showTotal();
	});
	//给减号添加click事件
	$(".jian").click(function(){
		//获取cartItemId
		var id = $(this).attr("id").substring(0,32);
		//获取输入框的数量
		var quantity = $("#"+id+"Quantity").val();
		//判断当前数量是不是为1,如果为1，则弹出提示
		if(quantity==1){
			if(confirm("您是否要删除该条目?")){
				location="/BookOnline/CartItemServlet?method=batchDelete&cartItemIds=" + id;
			}
		}else{
			sendUpdateQuantity(id,Number(quantity)-1);
		}
		
	});
	//给加号添加click事件
	$(".jia").click(function(){
		//获取cartItemId
		var id = $(this).attr("id").substring(0,32);
		//获取输入框的数量
		var quantity = $("#"+id+"Quantity").val();
		//判断当前数量是不是为1,如果为1，则弹出提示
	
		sendUpdateQuantity(id,Number(quantity)+1);
		
	});
	
	
});

//请求服务器，修改数量。
function sendUpdateQuantity(id, quantity) {
	$.ajax({
		async:false,
		cache:false,
		url:"/BookOnline/CartItemServlet",
		data:{method:"updateQuantity",cartItemId:id,quantity:quantity},
		type:"POST",
		dataType:"json",
		success:function(result) {
			//1. 修改数量
			$("#" + id + "Quantity").val(result.quantity);
			//2. 修改小计
			$("#" + id + "Subtotal").text(result.subtotal);
			//3. 重新计算总计
			showTotal();
		}
	});
}

//计算总计
function showTotal(){
	/*
	1.获取被勾选的条目,遍历
	2.
	*/
	var total = 0;
	$(":checkbox[name=checkboxBtn][checked=true]").each(function(){
		//获取复选框的值,即cartItemId，其他元素的前缀
		var id = $(this).val();
		//再通过cartItemId，获取其内容
		
		var text = $("#"+id+"Subtotal").text();
		//累加计算
		total = total + Number(text);
	
	});
	//把总计显示在总计元素内
	$("#total").text(round(total,2));//把total保留两位小数
	
}
//统一设置所有条目的复选按钮
function setItemCheckBox(bool){
	$(":checkbox[name=checkboxBtn]").attr("checked",bool);
}
//设置结算按钮样式
function setJieSuan(bool){
	if(bool){
		$("#jiesuan").removeClass("kill").addClass("jiesuan");
		$("#jiesuan").unbind("click");//撤销当前元素上的所有click事件
	}else{
		$("#jiesuan").removeClass("jiesuan").addClass("kill");
		$("#jiesuan").click(function(){
			return false;
		});
	}
	
}
//批量删除方法
function batchDelete(){
	var cartItemArray = new Array();
	$(":checkbox[name=checkboxBtn][checked=true]").each(function(){
		cartItemArray.push($(this).val());
	});
	location="/BookOnline/CartItemServlet?method=batchDelete&cartItemIds="+cartItemArray;
}
/*
 * 结算
 */
function jiesuan() {
	// 1. 获取所有被选择的条目的id，放到数组中
	var cartItemIdArray = new Array();
	$(":checkbox[name=checkboxBtn][checked=true]").each(function() {
		cartItemIdArray.push($(this).val());//把复选框的值添加到数组中
	});	
	
	// 2. 把数组的值toString()，然后赋给表单的cartItemIds这个hidden
	$("#cartItemIds").val(cartItemIdArray.toString());
	// 把总计的值，也保存到表单中
	$("#hiddenTotal").val($("#total").text());
	// 3. 提交这个表单
	$("#jieSuanForm").submit();
}

</script>
  </head>
  <body>
<c:choose>
	<c:when test="${empty cartItemList }">
		<table width="95%" align="center" cellpadding="0" cellspacing="0">
		<tr>
			<td align="right">
				<img align="top" src="<c:url value='/images/icon_empty.png'/>"/>
			</td>
			<td>
				<span class="spanEmpty">您的购物车中暂时没有商品</span>
			</td>
		</tr>
	</table>  


	</c:when>
	<c:otherwise>
		<table width="95%" align="center" cellpadding="0" cellspacing="0">
	<tr align="center" bgcolor="#efeae5">
		<td align="left" width="50px">
			<input type="checkbox" id="selectAll" checked="checked"/><label for="selectAll">全选</label>
		</td>
		<td colspan="2">商品名称</td>
		<td>单价</td>
		<td>数量</td>
		<td>小计</td>
		<td>操作</td>
	</tr>


<c:forEach items="${cartItemList}" var="cartItem">

	<tr align="center">
		<td align="left">
			<input value="${cartItem.cartItemId}" type="checkbox" name="checkboxBtn" checked="checked"/>
		</td>
		<td align="left" width="70px">
			<a class="linkImage" href="<c:url value='/jsps/book/desc.jsp'/>"><img border="0" width="54" align="top" src="<c:url value='/${cartItem.book.image_b }'/>"/></a>
		</td>
		<td align="left" width="400px">
		    <a href="<c:url value='/jsps/book/desc.jsp'/>"><span>${cartItem.book.bname }</span></a>
		</td>
		<td><span>&yen;<span class="currPrice" >${cartItem.book.currPrice }</span></span></td>
		<td>
			<a class="jian" id="${cartItem.cartItemId}Jian"></a><input class="quantity" readonly="readonly" id="${cartItem.cartItemId}Quantity" type="text" value="${cartItem.quantity }"/><a class="jia" id="${cartItem.cartItemId}Jia"></a>
		</td>
		<td width="100px">
			<span class="price_n">&yen;<span class="subTotal" id="${cartItem.cartItemId}Subtotal">${cartItem.subtotal }</span></span>
		</td>
		<td>
			<a href="<c:url value='/CartItemServlet?method=batchDelete&cartItemIds=${cartItem.cartItemId }'/>">删除</a>
		</td>
	</tr>

</c:forEach>






	<tr>
		<td colspan="4" class="tdBatchDelete">
			<a href="javascript:batchDelete();">批量删除</a>
		</td>
		<td colspan="3" align="right" class="tdTotal">
			<span>总计：</span><span class="price_t">&yen;<span id="total"></span></span>
		</td>
	</tr>
	<tr>
		<td colspan="7" align="right">
			<a href="javascript:jiesuan();"  id="jiesuan" class="jiesuan"></a>
		</td>
	</tr>
</table>
	


	<form id="jieSuanForm" action="<c:url value='/CartItemServlet'/>" method="post">
		<input type="hidden" name="cartItemIds" id="cartItemIds"/>
		<input type="hidden" name="total" id="hiddenTotal"/>
		<input type="hidden" name="method" value="loadCartItems"/>
	</form>
	</c:otherwise>
</c:choose>

  </body>
</html>
