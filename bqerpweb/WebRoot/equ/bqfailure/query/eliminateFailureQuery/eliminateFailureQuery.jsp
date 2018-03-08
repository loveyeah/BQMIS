<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="power.ear.comm.Employee"%>
<html>
<head>
<title>消缺查询</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="comm/ext/urlparams.js"></script>
	<script type="text/javascript"
			src="comm/datepicker/WdatePicker.js" defer="defer"></script>
			
</head>
<body>
    <script type="text/javascript" src="comm/scripts/Constants.js"></script>
	<script type="text/javascript" src="equ/bqfailure/query/eliminateFailureQuery/eliminateFailureQuery.js"></script>
	<div id="div_lay"></div>
	<input id='workCode' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getWorkerCode()%>" />
		<input id='workName' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getWorkerName()%>" />
</html>