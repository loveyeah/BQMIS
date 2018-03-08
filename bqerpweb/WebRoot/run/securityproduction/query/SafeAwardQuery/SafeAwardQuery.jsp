<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
	<head>

		<title>安全奖励查询</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js"
			defer="defer"></script>
		<script type="text/javascript"
			src="run/securityproduction/query/SafeAwardQuery/SafeAwardQuery.js"></script>
		<script type="text/javascript">
			var currentUser = "<%=((Employee) (session.getAttribute("employee")))
							.getWorkerCode()%>";
			var currentUserName = "<%=((Employee) (session.getAttribute("employee")))
							.getWorkerName()%>";
		</script>
	</head>
	<body>
	</body>
</html>