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
    <title>PMS</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="pms, a user privilege authentication admin console.">
	<meta name="author" content="ilbcj">
	<meta http-equiv="content-language" content="zh_CN" />
	<meta http-equiv="cache-control" content="no-cache" />
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />

    <!-- The styles -->
    <link rel="stylesheet" type="text/css" href="<%=path %>/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=path %>/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=path %>/css/main.css">
	<script type="text/javascript" src="<%=path %>/js/jquery.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=path %>/js/main.js"></script>
    
</head>

<body class="easyui-layout" style="background:#E6EEF8;">
<div data-options="region:'north',split:false,noheader:true,border:false" style="height:73px;background: url(../images/header_01.gif) repeat-x;">
	<img src="../images/header_02.gif" />
</div><!-- end of north -->
<!--     <div data-options="region:'south',title:'South Title',split:true" style="height:100px;"></div> -->
<!--     <div data-options="region:'east',title:'East',split:true" style="width:100px;"></div> -->
<div data-options="region:'west',split:true,noheader:true,border:false" style="width:200px;">
	<div id="mm" class="easyui-accordion"  fit="true">
	</div>
</div><!-- end of west -->
    <div data-options="region:'center',noheader:true, border:true" style="padding:5px;background:#E6EEF8;"></div>

<script>
$(document).ready(function () {
	$('#mm').accordion('add', {
		title: '用户管理',
		href:'menu/user.html',
		selected: false
	});

	$('#mm').accordion('add', {
		title: '机构管理',
		href:'menu/organization.html',
		selected: false
	});

	$('#mm').accordion('add', {
		title: '账号管理',
		href:'menu/account.html',
		selected: false
	});

	$('#mm').accordion('add', {
		title: '授权管理',
		href:'menu/privilege.html',
		selected: false
	});

	$('#mm').accordion('add', {
		title: '应用管理',
		href:'menu/application.html',
		selected: false
	});

	$('#mm').accordion('add', {
		title: '系统管理',
		href:'menu/system.html',
		selected: false
	});
	
	$('#mm').accordion('select', 0);
});

</script>
</body>
</html>
