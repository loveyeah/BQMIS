<%@ page language="java" pageEncoding="utf-8"%> 
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html>
	<head> 
	<base href="<%=basePath%>"> 
		<title>运行单元专业维护</title>
		<link rel="stylesheet" type="text/css" href="comm/ext/resources/css/ext-all.css" />
		<link rel="stylesheet" type="text/css" href="comm/ext/button.css" />
		<script type="text/javascript" src="ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="comm/ext/adapter/ext/ext-base.js"></script>
		<script type="text/javascript" src="comm/ext/ext-all.js"></script>
		<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
		<script type="text/javascript" src="comm/ext/ComboBoxTree.js"></script>
		<link href="comm/ext/resources/css/xtheme-gray.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="comm/ext/ComboBoxCheckTree.js"></script>

	</head>
	<body>	
			<table width="100%" height="100%" border="0">
				<tr>
					<td width="30%">
						<div id="mytree-div" style="overflow: auto; width: 100%;height: 100%"></div> 
					</td>
					<td width="50px" id="button-div" align="center">
						<div id="addbar-div"></div>
						<br />
						<br />
						<div id="updatebar-div"></div>
						<br />
						<br />
						<div id="deletebar-div"></div>
					</td>
					<td width="65%" align="left">							    
						<div id="rightGrid-div" style="overflow: auto; width: 100%; height: 100%"></div>
					</td>
				</tr>
			</table>
		<script type='text/javascript' src='run/runlog/maint/specials/unitprofession.js'></script>
						
	</body>
</html>
