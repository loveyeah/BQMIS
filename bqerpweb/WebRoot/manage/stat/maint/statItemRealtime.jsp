<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
<head> 
<title>经营指标与采集数据指标对应窗口</title>
<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include> 
  <link rel="stylesheet" type="text/css" href="comm/ext/rowcolor.css" />   
</head>
<body>
	<script type="text/javascript" src="manage/stat/maint/statItemRealtime.js"></script>
	<div id="tree"></div>
	<div id="center"></div>
</body>
</html>