<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
	<head>

		<title>部门请假登记</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/scripts/Powererp-extend.js"></script>
		<script type="text/javascript" src="comm/scripts/Constants.js"></script>
		<script type="text/javascript" src="comm/scripts/ca/ca-common.js"></script>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js"
			defer="defer"></script>
	</head>
	<body>
		<% Employee employee = (Employee) session.getAttribute("employee");
Long deptId = employee.getDeptId();%>
<script type="text/javascript">
var deptId = <%= deptId%>;
</script>
		<script type="text/javascript"
			src="hr/ca/leave/deptvacationregister/QJ002.js"> </script>
	</body>
</html>