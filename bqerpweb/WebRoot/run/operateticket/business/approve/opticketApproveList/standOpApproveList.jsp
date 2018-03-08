<%@ page language="java" pageEncoding="utf-8"%>
<html>
	<head>
		<title>标准操作票审批列表</title>
		<jsp:include page="../../../../../comm/jsp/commjsp.jsp"></jsp:include>
	</head>
	<body>
		<script type="text/javascript" src="comm/scripts/Constants.js"></script>
		<script type="text/javascript" src="comm/ext/ComboBoxTree.js"></script>
		<script type="text/javascript" src="run/operateticket/opticket_comm.js"></script>
		<script type="text/javascript"
			src="run/operateticket/business/approve/opticketApproveList/standOpApproveList.js"></script>
			<div id="loading-mask" style=""></div>
		<div id="loading">
			<div class="loading-indicator">
				<img src="comm/images/extanim32.gif" width="32" height="32"
					style="margin-right: 8px;" align="absmiddle" />
				Loading...
			</div>
		</div>	
	</body>
</html>