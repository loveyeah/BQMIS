<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<%@page import="power.ear.comm.Employee"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<title>到货登记表</title>
<jsp:include page="../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../comm/scripts/Constants.js"></script>
</head>

<body>
<% Employee employee = (Employee) session.getAttribute("employee"); %>
<form id="frm1" method="post" action="/powerrpt/report/webfile/achieveGoods.jsp">
<input type="hidden" name="workername" value="<%= employee.getWorkerName() %>" />
<input type="hidden" name="enterprisecode" value="<%= employee.getEnterpriseCode()%>" />
<input type="hidden" name="mifno" value="<%=request.getParameter("mifNo") %>" />
</form>
<script language="javascript">
frm1.submit();
</script>
</body>

</html>