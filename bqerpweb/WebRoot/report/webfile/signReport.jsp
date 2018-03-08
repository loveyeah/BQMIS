<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<%@page import="power.ear.comm.Employee"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<title></title>
<jsp:include page="../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../comm/scripts/Constants.js"></script>
</head>

<body>
<% Employee employee = (Employee) session.getAttribute("employee"); %>
<form id="frm1" method="post" action="/powerrpt/report/webfile/signReport.jsp">
<input type="hidden" name="status" value="<%=request.getParameter("status") %>" />
<input type="hidden" name="applyId" value="<%= request.getParameter("applyId")%>" />
<input type="hidden" name="readMan" value="<%= employee.getWorkerCode()%>" />
<input type="hidden" name="enterpriseCode" value="<%= employee.getEnterpriseCode()%>" />

</form>
<script language="javascript">
frm1.submit();
</script>
</body>

</html>