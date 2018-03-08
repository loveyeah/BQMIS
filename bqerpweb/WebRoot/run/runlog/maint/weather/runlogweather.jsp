<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head> 
  <base href="<%=basePath%>">
		<title>天气状态维护</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">  
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="run/runlog/maint/weather/runlogweather.js"></script>
	</head>
	<body>		  
	<div id="loading-mask" style=""></div>
		 <div id="loading">
			<div class="loading-indicator">
				<img src="comm/images/extanim32.gif" width="32" height="32"
					style="margin-right: 8px;" align="absmiddle" />
				Loading...
			</div>
		</div> 
		<div id="form-div"></div>
	</body>
</html>