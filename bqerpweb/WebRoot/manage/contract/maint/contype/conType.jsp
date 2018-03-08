<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head> 
    	<base href="<%=basePath%>"> 
		<title>合同类别维护</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="manage/contract/maint/contype/conType.js"></script>
	</head>
	<body>		  
	<div id="treepanel"></div>
	</body>
</html>