<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>欢迎进入</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/ext/ComboBoxCheckTree.js"></script>
		<script type="text/javascript" src="system/business/roleFile/roleFileMaint.js"></script> 
	</head>
	<body  bgcolor="#EDEDED">
		<div id="loading-mask" style=""></div>
		<div id="loading">
			<div class="loading-indicator">
				<img src="comm/images/extanim32.gif" width="32" height="32"
					style="margin-right: 8px;" align="absmiddle" />
				Loading...
			</div>
		</div>
		<div id="role-div"></div>
		<div id="file-tree-div"></div>
		<div id="revoke-div"></div>
		<div id="grant-div"></div> 
		<div id="waitfile-tree-div"></div> 
		
		 
	</body>
</html>
