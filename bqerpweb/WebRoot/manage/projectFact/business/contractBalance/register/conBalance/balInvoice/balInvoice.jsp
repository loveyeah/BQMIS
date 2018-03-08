<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
	<head>

		<title>本次相关票据</title>
		<jsp:include page="../../../../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="comm/ext/urlparams.js"></script>
	</head>
	<body>
<%
		Employee employee=(Employee)session.getAttribute("employee");
		%>
		<input type='hidden' id='workerName' value='<%=employee.getWorkerName()%>'/>
		<input type='hidden'  id='workerCode' value='<%=employee.getWorkerName()%>'/>
		<script type="text/javascript"
			src="manage/projectFact/business/contractBalance/register/conBalance/balInvoice/balInvoice.js"></script>
		<div id="win"></div>
	</body>
</html>