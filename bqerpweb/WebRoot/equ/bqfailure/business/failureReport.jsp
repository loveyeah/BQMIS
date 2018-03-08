<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>缺陷上报</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="-1">
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js" defer="defer"></script>
		<script type="text/javascript" src="comm/ext/urlparams.js"></script> 
		<script type='text/javascript' src='equ/bqfailure/business/failureReport.js' charset="utf-8"></script>
	</head>
	<body align="center">
		<input id='workCode' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getWorkerCode()%>" />
		<input id='workName' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getWorkerName()%>" />
	</body>
</html>