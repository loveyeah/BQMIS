<%@ page language="java" pageEncoding="utf-8"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
	 
		<title>角色管理</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="hr/maint/empinfo/ComboxTree.js"></script>
		<script type="text/javascript" src="comm/ext/ComboBoxCheckTree.js"></script>
		<script type="text/javascript" src="system/maint/role/roleMaint.js"></script> 
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
		 
		<div id="tree-div" style="overflow:auto;width:100%;height:100%">
		<div id="tree-button-div" style="overflow:auto;width:100%;"></div>
		</div> 
		<div id="wait-tree-div"  style="overflow:auto;width:100%;"></div>>
		<div id="form-div"></div>
	</body>
</html>
