<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		
		<title>客户公司对应联系人维护</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
	<link rel="stylesheet" type="text/css" href="ext/button.css"/>
	<script type="text/javascript" src="comm/ext/ComboBoxTree.js"></script>
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
		<div id="window-win"></div>
		<div id="role-div"></div>
		<div id="filetreediv"></div>
		<div id="revoke-div"></div>
		<div id="grant-div"></div> 
		<div id="wait-role-div"></div> 
		<div id="already-role-div"></div> 
		<script type="text/javascript" src="message/bussiness/customer_contact/customer_contact.js"></script> 
		 
	</body>
</html>
