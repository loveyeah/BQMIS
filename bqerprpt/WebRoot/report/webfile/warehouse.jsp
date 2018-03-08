<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<%@page import="power.ear.comm.Employee"%>

<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>库存再订货报表</title>
<jsp:include page="../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="comm/scripts/Constants.js"></script>
</head>
<body>
<birt:viewer id="warehouse"
	 top="0"
	 left="0"
	 width="1100"
	 height="800"
	 reportDesign="wareHouse.rptdesign"
	 position="absolute"
	 showTitle="false"
	 showToolBar = "false"
	 pageOverflow="0"
	 frameborder="no"
	 format="html"
	 style="zoom:0.8"
	 >

	 <birt:param name="enterpriseCode" value="hfdc" > </birt:param>
	 <birt:param name="createMan" value="zhujie" > </birt:param>
</birt:viewer>
	</body>

</html>