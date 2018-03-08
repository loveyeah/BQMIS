<%@ page language="java" pageEncoding="utf-8"%>
<html>
<head>
<title>部门维护</title>
	 	<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include> 
	</head>

	<body>
		<input type="hidden" id="userlogin" value=${sessionScope.userlogin } />
		<div id="dept-tree-div"></div>
		<script type="text/javascript" src="hr/maint/dept/dept.js"></script>
	</body>
</html>