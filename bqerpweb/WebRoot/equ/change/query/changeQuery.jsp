<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head> 
		<title>异动登记</title> 
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include> 
		<script type="text/javascript" src="equ/change/query/changeQuery.js"></script> 
	</head>
	<body>
	<input id='workCode' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getWorkerCode()%>" />
		<input id='workName' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getWorkerName()%>" />
	</body>
</html>