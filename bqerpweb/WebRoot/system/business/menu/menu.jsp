<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee;"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head> 
		<title>电厂管理系统</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/ext/myext/tabCloseMenu.js"></script>
		<script type="text/javascript" src="system/business/menu/menu.js"></script> 
		<style type="text/css">
.x-tab-strip-top .x-tab-right {
	background-image: url(system/business/menu/tabs-sprite-prior.gif);
}

.x-tab-strip-top .x-tab-left {
	background-image: url(system/business/menu/tabs-sprite-prior.gif);
}

.x-tab-strip-top .x-tab-strip-inner {
	background-image: url(system/business/menu/tabs-sprite-prior.gif);
}
.goto {
	background-image: url(comm/images/btn_goto.gif) !important;
	color:white !important;
}

.signout {
	background-image: url(comm/images/btn_signout.gif) !important;
	color:white !important;
}

.help {
	background-image: url(comm/images/btn_help.gif) !important;
	color:white !important;
}
.bgnb {
	background-color: #396799;
	color: white;
	font-family: Arial, Helvetica, sans-serif;
	font-size: 11px;
	font-style: normal;
	font-weight: bold;
	background-image: url(comm/images/bg_navbar.jpg);
	background-repeat: no-repeat;
	background-position: right top;
	vertical-align: top;
	font-weight: bold;
}
.transbg {
	background: transparent;
	border: 0, 0, 0, 0
}
/*
.x-btn-text {
	font-size: 9px;
	background-position: 0 2px;
	background-repeat: no-repeat;
	padding-left: 18px;
	padding-top: 3px;
	padding-bottom: 2px;
	padding-right: 0;
}
*/
.first-tab {
	background-image: url(comm/images/home.gif) !important;
}

.menu-item-folder {
	background-image: url(comm/images/folder.gif) !important;
}

.menu-item-page {
	background-image: url(comm/images/page.gif) !important;
}

.tab-item-lock {
	background-image: url(comm/images/hmenu-lock.gif) !important;
}

.tab-item-unlock {
	background-image: url(comm/images/hmenu-unlock.gif) !important;
}

.modify-pwd {
	background-image: url(comm/images/menu_icon_passwordinfo.gif) !important;
		color:white !important;
}

.re-login {
	background-image: url(comm/images/nav_icon_restoreGlobalDefau.gif)
		!important;
		color:white !important;
}
</style>  
	</head>
	<body>
 
		  <input id="userName" type="hidden" value="999999"/>
	</body>
</html>

