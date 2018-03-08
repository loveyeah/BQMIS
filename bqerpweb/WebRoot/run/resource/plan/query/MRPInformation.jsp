<%@ page language="java" contentType="text/html; charset=utf-8" %>
<%@page import="power.ear.comm.Employee"%>
<html>
<head>
<title>物料需求计划异动明细查询信息</title>
	<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="comm/scripts/Constants.js"></script>
	<script type="text/javascript" src="comm/scripts/Powererp-extend.js"></script>
	<script type="text/javascript" src="comm/datepicker/WdatePicker.js"
			defer="defer"></script>
	<script type="text/javascript" src="comm/ext/ComboBoxTree.js"></script>
	<script type="text/javascript" src="comm/ext/RowExpander.js"></script>
	<script type="text/javascript" src="run/resource/plan/register/GroupSummary.js"></script>
    <script type="text/javascript" src="run/resource/plan/query/MRPInformation.js"></script>
    <script type="text/javascript" src="run/resource/plan/planComm.js"></script> 
     <script type="text/javascript"  src="comm/jsp/item/budget/comBudget.js"></script>
     <script type="text/javascript" src="run/resource/plan/PlanStatusQuery.js"></script>
    	<script type="text/javascript">
    	
	var currentUser = "<%=((Employee) (session.getAttribute("employee"))).getWorkerCode()%>"
	</script>
</head>
<body>
<div id="myGrid"></div>
</body>
</html>