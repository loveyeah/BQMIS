<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<%@page import="power.ear.comm.Employee"%>

<html>
<head>
<meta http-equiv="Content-Type">
<title>部门请假单</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../../../comm/scripts/Constants.js"></script>
</head>

<body>
<% Employee employee = (Employee) session.getAttribute("employee"); %>
<form id="frm1" method="post" action="/powerrpt/report/webfile/hr/ca/KQ017_deptleaveStatistics.jsp">
<input type="hidden" name="enterpriseCode" value="<%= employee.getEnterpriseCode()%>" />
<input type="hidden" name="deptId" value="<%=request.getParameter("deptId") %>" />
<input type="hidden" name="yearMonth" value="<%=request.getParameter("yearMonth") %>" />
<input type="hidden" name="signState" value="<%=request.getParameter("signState") %>" />
</form>
<script language="javascript">
frm1.submit();
</script>
</body>

</html>