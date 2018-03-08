<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
	<head>
		<title>物资领用综合查询</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/scripts/Constants.js"></script>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js" defer="defer"></script>
			<script type="text/javascript" src="comm/ext/ComboBoxTree.js"></script>
			<script type="text/javascript" src="run/resource/storage/issueheadapprove/issueApproveStatus.js"></script>
	<script type="text/javascript"  src="comm/jsp/item/budget/comBudget.js"></script>
	<script type="text/javascript"  src="run/resource/storage/issueheadregister/ComIssuekind.js"></script>
	</head>
	<body>
		<script type="text/javascript" src="run/resource/storage/issuecompquery/issueCompQuery.js"></script>
		<script type="text/javascript">
	var currentUser = "<%=((Employee) (session.getAttribute("employee"))).getWorkerCode()%>"
	var currentUserName = "<%=((Employee) (session.getAttribute("employee"))).getWorkerName()%>"
	var currentDeptName = "<%=((Employee) (session.getAttribute("employee"))).getDeptName()%>"
	
	</script>
	<div id="mygrid">
	<div id="detailGrid">
	</body>
</html>