<%@ page language="java" pageEncoding="utf-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<base href="<%=basePath%>">
		<title>交接班方式</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<script type="text/javascript" src="comm/ext/urlparams.js"></script>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	</head>
	<body  bgcolor="#DFE8F6">
	<!--  
		<div align="center">
		<table width="100%" height="100%" border="0">
			<tr>	
				<td width="100%">
				<div id="wait-equ-div" style="width:100%;height:100%"></div>
				</td>
				<td width="100%">
				<div id="equ-div" style="width:100%;height:100%"></div>			
				</td>		
			</tr>
		</table>
		</div> 
		-->
		<script type="text/javascript" src="run/runlog/business/shiftequ/shift.js"></script> 
		 
	</body>
</html>
