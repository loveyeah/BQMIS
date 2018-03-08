<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<%@page import="power.ear.comm.Employee"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<title>供应商档案</title>
<jsp:include page="../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../comm/scripts/Constants.js"></script>
</head>

<body>
<% Employee employee = (Employee) session.getAttribute("employee"); %>
<form id="frm1" method="post" action="/powerrpt/report/webfile/supplierBaseInfo.jsp">
<input type="hidden" name="enterpriseCode" value="<%= employee.getEnterpriseCode()%>" />
<input type="hidden" name="supplierCode" value="<%=request.getParameter("clientCode") %>" />
<input type="hidden" name="createMan" value="<%= employee.getWorkerName() %>" />

</form>
<script language="javascript">
frm1.submit();
</script>

</body>

</html>