<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	byte[] byteRand = new byte[32];
	new java.util.Random().nextBytes(byteRand);
	String random = new sun.misc.BASE64Encoder().encode(byteRand);
	
	HttpSession sess = request.getSession();
	if (sess == null){
		sess = request.getSession(true);
	}	
	sess.setAttribute("auth.random",random);
	System.out.println("login flag:" + random);
%>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta charset="utf-8">
    <title>PMS | Sign In</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="pms, a user privilege authentication admin console.">
	<meta name="author" content="ilbcj">

    <!-- The styles -->
    <link rel="stylesheet" type="text/css" href="<%=path %>/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=path %>/themes/icon.css">
	<script type="text/javascript" src="<%=path %>/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jquery.easyui.min.js"></script>
    
</head>

<body>
<div class="easyui-panel" fit="true" style="padding:5px;background:#eee;">
	<div id="win" style="width:300px;height:180px;">
	<form id="login" name="login" action="<%=path %>/login/loginpwd.action" method="post" style="padding:10px 20px 10px 40px;">
		<p>账号: <input type="text" name="loginid"/></p>
		<p>口令: <input id="pwd" type="password" name="pwd"/></p>
		<div style="padding:5px;text-align:center;">
			<a href="#" class="easyui-linkbutton" icon="icon-ok" onclick="login_ok()">登录</a>
			<a href="#" class="easyui-linkbutton" icon="icon-cancel" onclick="login_cancel()">取消</a>
		</div>
		<input type="hidden" name="random" value="<%= random%>" />
	</form>
	</div>
</div>
<script>
$('#win').window({
	title:"登录",
	collapsible:false,
	minimizable:false,
	maximizable:false,
	closable:false,
	draggable:false,
	resizable:false,
    width:300,
    height:180,
    modal:true
});
// $('#login').form({
// 	url:'<%=path %>/login/loginpwd.action'
// });
function login_ok(){
	
	// submit the form
	//$('#login').submit();
	document.getElementById('login').submit();
}
function login_cancel(){
	$("input").val("");
}
</script>
</body>
</html>
