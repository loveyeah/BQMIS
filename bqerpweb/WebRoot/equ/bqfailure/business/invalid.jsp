<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title></title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="-1">
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/ext/urlparams.js"></script>
		<script type='text/javascript' src='equ/bqfailure/business/invalid.js'
			charset="utf-8"></script>
	</head>
		<body align="center">
		<div id="confirm-div"></div>
		<div id="invalid-div"></div>
		<div id="sendback-div"></div>
		<input id='workCode' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getWorkerCode()%>" />
		<input id='workName' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getWorkerName()%>" />
	</body>
</html>
