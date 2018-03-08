<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" >
<html>
<head>
<title>运行日志查询</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="comm/ext/urlparams.js"></script>
</head>
<body>
	<script type="text/javascript" src="run/runlog/query/basicinfo/basicinfo.js"></script>
	<div id="orgGrid" style="width: 100%; height: 100%"></div>
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