<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
  <head> 
    	<base href="<%=basePath%>"> 
		<title>合作伙伴类型维护</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="manage/contract/maint/cooperator/conclienttype.js"></script>
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
