<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>超时缺陷查询</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">

		<link rel="stylesheet" type="text/css" href="comm/ext/resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css" href="comm/ext/button.css" />
		<script type="text/javascript" src="comm/ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="comm/ext/ext-all.js"></script>
		<script type="text/javascript" src="comm/ext/ComboBoxTree.js"></script> 
		<script type='text/javascript' src='equ/bqfailure/query/overtimeFail.js' charset="utf-8"></script>
	</head>
	<body align="center">  
	</body>
</html>
