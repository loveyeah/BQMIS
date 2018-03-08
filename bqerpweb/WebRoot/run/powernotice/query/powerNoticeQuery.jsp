<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
	<head>
		<title>停送电通知单查询</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript">
	var currentUser = "<%=((Employee) (session.getAttribute("employee"))).getWorkerCode()%>"
	</script>
	</head>
	<body>
	    <script type="text/javascript" src="comm/scripts/Constants.js"></script>
		<script type="text/javascript" src="run/workticket/workticket-comm.js"></script>
		<script type="text/javascript"
			src="run/powernotice/query/powerNoticeQuery.js"></script>
	</body>
</html>