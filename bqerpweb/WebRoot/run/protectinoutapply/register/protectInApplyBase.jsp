<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
	<head>
		<title>保护投入申请</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js" defer="defer"></script>
	<script type="text/javascript" src="comm/ext/urlparams.js"></script>
	</head>
	<body>
	<%
			Employee employee = (Employee) session.getAttribute("employee");
		%>
		<input type='hidden' id='workerName'
			value='<%=employee.getWorkerName()%>' />
		<input type='hidden' id='workerCode'
			value='<%=employee.getWorkerCode()%>' />
		<input type='hidden' id='dept' value='<%=employee.getDeptCode()%>' />
		<input type='hidden' id='deptName'
			value='<%=employee.getDeptName()%>' />
		<script type="text/javascript" src="run/protectinoutapply/register/protectInApplyBase.js"></script>
	</body>
</html>