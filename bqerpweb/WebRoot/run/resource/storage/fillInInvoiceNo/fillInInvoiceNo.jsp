<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
	<head>
		<title>补填发票管理</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		
	</head>
	<body>
		<script type="text/javascript" src="comm/scripts/Constants.js"></script>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js" defer="defer"></script>
		<script type="text/javascript" src="run/resource/storage/fillInInvoiceNo/fillInInvoiceNo.js"></script>
	   <script type="text/javascript">
	var currentCode = "<%=((Employee) (session.getAttribute("employee"))).getWorkerCode()%>"
	var currentName = "<%=((Employee) (session.getAttribute("employee"))).getWorkerName() %>"
	</script>
	</body>
</html>