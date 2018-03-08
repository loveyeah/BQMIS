<%@ page language="java"  contentType="text/html; charset=UTF-8"%>

<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<title>新进/离职员工统计</title>
<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../../comm/scripts/Constants.js"></script>
</head>
<body>
<birt:viewer id="gotAndLoseStaffInfo"
	 top="0"
	 left="0"
	 width="1080"
	 height="630"
	 reportDesign="hr/GotAndLoseStaffInfo.rptdesign"
	 showTitle="false"
	 showToolBar = "false"
	 pageOverflow="1"
	 format="html"
	 >	
	 <birt:param name="year" value="<%= request.getParameter("year") %>" > </birt:param>
	 <birt:param name="dept" value="<%= request.getParameter("dept") %>" > </birt:param>
 	 <birt:param name="EnCode" value="<%= request.getParameter("enterpriseCode") %>" > </birt:param>
</birt:viewer>
	</body>

</html>