<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>"> 
    <title>运行设备状态维护</title>
    <jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
    <script type="text/javascript" src="comm/ext/urlparams.js"></script>
    <script type="text/javascript" src="run/runlog/maint/shiftequ/runstatusmaint.js"></script> 
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">   
	
  </head> 
  <body> 
  <!--  
  <div align="center">
	<table>
		<tr>
		<td width="200" id="leftDiv"></td>
		<td width="7" id="tbarDiv" valign="middle">
					<div id="revoke-div"></div> 
					<br/><br/><br/><br/>
					<div id="grant-div"></div> 
		</td>
		<td width="400" id="rightDiv"><div></div></td>
		</tr>
	</table>
 </div>
 -->
 <div align="center">
		<table width="100%" height="100%" border="0">
			<tr>
				<td width="45%">
				<div id=leftDiv style="overflow:auto;width:100%;height:100%"></div>
				</td>
				<td width="50px" id="tbarDiv">
					<div id="revoke-div"></div>
					<br/><br/>
					<div id="grant-div"></div> 
				</td>
				<td width="50%">
				<div id="rightDiv" style="overflow:auto;width:100%;height:100%"></div>
				</td>
			</tr>
			</table>
			</div>
  </body>
</html>
