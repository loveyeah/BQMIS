<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
	<head>
		<title>文档类型维护</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
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
			src="message/maint/wordtype/wordtype.js"></script>
		<div id="window-win">
		</div>
	</body>
</html>