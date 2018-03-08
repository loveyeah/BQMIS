<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<head>
<title>值班人员查询</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="comm/ext/urlparams.js"></script>
</head>
<body>
	<script type="text/javascript" src="run/runlog/query/workerquery/workerquery.js"></script>
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