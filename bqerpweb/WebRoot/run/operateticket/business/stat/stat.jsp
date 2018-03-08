<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=utf-8"%>
<%@page import="power.ear.comm.Employee"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>电气专业操作票统计</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js"
			defer="defer"></script>

	</head>
	<body>
		<%
			Employee employee = (Employee) session.getAttribute("employee");
		%>
		<input type='hidden' id='workerName'
			value='<%=employee.getWorkerName()%>' />
		<input type='hidden' id='workerCode'
			value='<%=employee.getWorkerName()%>' />
		<script type="text/javascript"
			src="run/operateticket/business/stat/stat.js"></script>
	</body>
</html>
