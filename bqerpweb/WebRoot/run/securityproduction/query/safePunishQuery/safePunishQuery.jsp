<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@page import="power.ear.comm.Employee"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>

		<title>违章处罚查询</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js"
			defer="defer"></script>
		<script type="text/javascript"
			src="run/securityproduction/query/safePunishQuery/safePunishQuery.js"></script>
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