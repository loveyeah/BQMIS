<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<%@page import="power.ear.comm.Employee"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GB2312">
<title>物料盘点对照表</title>
<jsp:include page="../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../comm/scripts/Constants.js"></script>
</head>

<body>
<% Employee employee = (Employee) session.getAttribute("employee"); %>
<form id="frm1" method="post" action="/powerrpt/report/webfile/materialCheckPrint.jsp">
<input type="hidden" name="workername" value="<%= employee.getWorkerName() %>" />
<input type="hidden" name="enterprisecode" value="<%= employee.getEnterpriseCode()%>" />
<input type="hidden" name="delayStore" value="<%=request.getParameter("delayStore") %>" />
<input type="hidden" name="delayLocation" value="<%=request.getParameter("delayLocation") %>" />
<input type="hidden" name="delayMaterial" value="<%=request.getParameter("delayMaterial") %>" />
<input type="hidden" name="planer" value="<%=request.getParameter("planer") %>" />
<input type="hidden" name="materialSortId" value="<%=request.getParameter("materialSortId") %>" />
<input type="hidden" name="bookNo" value="<%=request.getParameter("bookNo") %>" />
</form>
<script language="javascript">
frm1.submit();
</script>
</body>

</html>