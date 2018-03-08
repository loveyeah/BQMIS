<%@ page language="java" import="java.util.*" contentType="text/html; charset=utf-8" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<base href="<%=basePath%>"> 
<title>重点事项维护</title>
         
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
        <script type="text/javascript" src="run/runlog/maint/mainitem/runlogmainitem.js"></script>
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