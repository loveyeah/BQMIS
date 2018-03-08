<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
	<head>
		<title>社保数据查询</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/scripts/Constants.js"></script>    
		<script type="text/javascript"
			src="comm/datepicker/WdatePicker.js" defer="defer"></script>
	</head>
	<body>
		<script type="text/javascript" src="hr/labor/business/laborManage/laborManage.js"></script>
	<div id='mygrid' ></div>
	<script language="javascript">
	    var hiddenBtn = "2";
	</script>
	</body>
</html>