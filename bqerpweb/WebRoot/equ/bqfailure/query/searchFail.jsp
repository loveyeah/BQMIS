<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>缺陷查询</title>
		<link rel="stylesheet" type="text/css" href="failure-grid.css" />
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/ext/urlparams.js"></script>
		<script type="text/javascript" src="comm/ext/ComboBoxTree.js"></script>
		<script type="text/javascript" src="equ/bqfailure/query/searchFail.js"></script>
		<script type="text/javascript">
		var currentUser = "<%=((Employee) (session.getAttribute("employee"))).getWorkerCode()%>"
		</script>
	</head>
	<body>
		<div id="treepanel"></div>
	</body>
</html>