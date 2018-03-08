<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>缺陷登记</title>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">

		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript"
			src="comm/datepicker/WdatePicker.js" defer="defer"></script>
		<script type="text/javascript"
			src="equ/bqfailure/business/failureAdd.js"></script>
	</head>
	<body>
		<div id="loading-mask" style=""></div>
		<div id="loading">
			<div class="loading-indicator">
				<img src="comm/images/extanim32.gif" width="32" height="32"
					style="margin-right: 8px;" align="absmiddle" />
				Loading...
			</div>
		</div>

		<div id="tree-div" style="overflow: auto; width: 100%; height: 100%">
			<div id="tree-button-div" style="overflow: auto; width: 100%;"></div>
		</div>
		<div id="failure-grid"></div>
		<div id="form-div"></div>
		
		<div id="tips-div"></div>
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
