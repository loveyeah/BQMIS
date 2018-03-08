<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<%@page import="power.ear.comm.Employee"%>

<html>
<head>
<meta http-equiv="Content-Type">
<title>离职员工花名册</title>
<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../../comm/scripts/Constants.js"></script>
</head>

<body>
<% Employee employee = (Employee) session.getAttribute("employee"); %>
<form id="frm1" method="post" action="/powerrpt/report/webfile/hr/PD007_dismissionStaffInfo.jsp">
<input type="hidden" name="enterpriseCode" value="<%= employee.getEnterpriseCode()%>" />
<input type="hidden" name="year" value="<%=request.getParameter("year") %>" />
<input type="hidden" name="dept" value="<%=request.getParameter("dept") %>" />
<input type="hidden" name="typeId" value="<%=request.getParameter("typeId") %>" />
<input type="hidden" name="advicenoteNo" value="<%=request.getParameter("advicenoteNo") %>" />
</form>
<script language="javascript">
frm1.submit();
</script>
</body>

</html>