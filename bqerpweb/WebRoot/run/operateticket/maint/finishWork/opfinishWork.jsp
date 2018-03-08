<%@ page language="java" contentType="text/html; charset=utf-8" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<title>电气倒闸操作后所完成工作维护页面</title>
    <jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="comm/scripts/Constants.js"></script>
    <script type="text/javascript" src="run/operateticket/maint/finishWork/opfinishWork.js"></script>
</head>
<body>
    <div id="treePanel"></div>
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