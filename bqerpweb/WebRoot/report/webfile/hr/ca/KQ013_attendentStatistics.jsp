<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<%@page import="power.ear.comm.Employee"%>

<html>
<head>
<meta http-equiv="Content-Type">
<title>请假统计查询报表</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../../../comm/scripts/Constants.js"></script>
</head>

<body>
<% Employee employee = (Employee) session.getAttribute("employee"); %>
<form id="frm1" method="post" action="/powerrpt/report/webfile/hr/ca/KQ013_attendentStatistics.jsp">
<input type="hidden" name="enterpriseCode" value="<%= employee.getEnterpriseCode()%>" />
<input type="hidden" name="type" value="<%=request.getParameter("type") %>" />
<input type="hidden" name="year" value="<%=request.getParameter("year") %>" />
<input type="hidden" name="month" value="<%=request.getParameter("month") %>" />
</form>
<script language="javascript">
frm1.submit();
</script>
</body>

</html>