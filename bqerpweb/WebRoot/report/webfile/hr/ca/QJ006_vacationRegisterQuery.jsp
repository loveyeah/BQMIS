<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<%@page import="power.ear.comm.Employee"%>

<html>
<head>
<meta http-equiv="Content-Type">
<title>请假登记表</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../../../comm/scripts/Constants.js"></script>
</head>

<body>
<% Employee employee = (Employee) session.getAttribute("employee"); %>
<form id="frm1" method="post" action="/powerrpt/report/webfile/hr/ca/QJ006_vacationRegisterQuery.jsp">
<input type="hidden" name="enterpriseCode" value="<%= employee.getEnterpriseCode()%>" />
<input type="hidden" name="fromDate" value="<%=request.getParameter("fromDate") %>" />
<input type="hidden" name="toDate" value="<%=request.getParameter("toDate") %>" />
</form>
<script language="javascript">
frm1.submit();
</script>
</body>

</html>