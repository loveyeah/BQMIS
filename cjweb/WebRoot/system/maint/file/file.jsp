<%@ page language="java" pageEncoding="utf-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		
		<title>功能模块管理</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="system/maint/file/file.js"></script>
	</head>
	<body>
		<div id="loading-mask"></div>
		<div id="loading">
			<div class="loading-indicator">
				<img src="comm/images/extanim32.gif" width="32" height="32"
					style="margin-right: 8px;" />
				Loading...
			</div>
		</div> 
		<div id="tree-div">
			<div id="tree-button-div"></div>
		</div>
<div id="west-div">
<div id="tree-button-div" ></div>
<div id="file-tree-div"></div>
</div>
	</body>
</html>
