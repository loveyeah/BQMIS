<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
	<head>
		<title>缺陷审批</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="-1">

		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/ext/ComboBoxTree.js"></script>
		<script type="text/javascript" src="comm/datepicker/WdatePicker.js" defer="defer"></script>
		<script type='text/javascript' src='equ/bqfailure/business/chiefawaitFailure.js'
			charset="utf-8"></script>
			<script type="text/javascript" src=" comm/scripts/Datetime.js"></script>
	</head>
	<body align="center">
		<div id="confirm-div"></div>
		<div id="invalid-div"></div>
		<div id="sendback-div"></div>
		<input id='workCode' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getWorkerCode()%>" />
		<input id='workName' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getWorkerName()%>" />
		<input id='deptCode' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getDeptCode()%>" />
		<input id='deptName' type='hidden'
			value="<%=((Employee) (session.getAttribute("employee")))
							.getDeptName()%>" />
	</body>
</html>
