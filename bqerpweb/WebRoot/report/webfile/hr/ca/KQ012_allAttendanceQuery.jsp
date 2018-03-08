<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
<head>
<meta http-equiv="Content-Type">
<title>职工考勤记录表</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../../../comm/scripts/Constants.js"></script>
</head>

<body>
<% Employee employee = (Employee) session.getAttribute("employee"); %>
<form id="frm1" method="post" action="/powerrpt/report/webfile/hr/ca/KQ012_allAttendanceQuery.jsp">
<input type="hidden" name="checkDate" value="<%=request.getParameter("checkDate") %>" />
<input type="hidden" name="enterpriseCode" value="<%= employee.getEnterpriseCode()%>" />
<input type="hidden" name="checkDeptId" value="<%=request.getParameter("checkDeptId") %>" />
<input type="hidden" name="empDeptId" value="<%= employee.getDeptId()%>" />
</form>
<script language="javascript">
frm1.submit();
</script>
</body>

</html>