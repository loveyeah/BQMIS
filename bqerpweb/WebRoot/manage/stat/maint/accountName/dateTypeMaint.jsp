<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
<head>
   <base href="<%=basePath%>"> 
<title>指标数据时点设置维护</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/ext/urlparams.js"></script>
</head>
<body>
	<script type="text/javascript" src="manage/stat/maint/accountName/dateTypeMaint.js"></script>
	<div id="tree"></div>
	<div id="center"></div>
</body>
</html>