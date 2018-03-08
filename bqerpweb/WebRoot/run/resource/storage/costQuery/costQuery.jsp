<%@ page language="java" pageEncoding="utf-8"%>
<html>
	<head>
		<title>材料领用费用查询</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript"
			src="comm/datepicker/WdatePicker.js" defer="defer"></script>
	<script type="text/javascript" src="comm/ext/urlparams.js"></script>
	</head>
	<body>
		<script type="text/javascript" src="run/resource/storage/costQuery/costQuery.js"></script>
		<div id="loading-mask" style=""></div>
		 <div id="loading">
			<div class="loading-indicator">
				<img src="comm/images/extanim32.gif" width="32" height="32"
					style="margin-right: 8px;" align="absmiddle" />
				Loading...
			</div>
		</div> 
		<div id="form-div"></div>
	</body>
</html>

