<%@ page language="java"  contentType="text/html; charset=UTF-8"%>

<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type">
<title>职工考勤记录表</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../../../comm/scripts/Constants.js"></script>
</head>
<body>
<birt:viewer id="allAttendanceQuery"
	 top="0"
	 left="0"
	 width="1800"
	 height="880"
	 reportDesign="hr/ca/allAttendanceQuery.rptdesign"
	 showTitle="false"
	 showToolBar = "false"
	 pageOverflow="1"
	 format="html"
	 >	
	 <birt:param name="checkDate" value="<%= request.getParameter("checkDate") %>" > </birt:param>
	 <birt:param name="checkDeptId" value="<%= request.getParameter("checkDeptId") %>" > </birt:param>
	 <birt:param name="enterpriseCode" value="<%= request.getParameter("enterpriseCode") %>" > </birt:param>
	 <birt:param name="empDeptId" value="<%= request.getParameter("empDeptId") %>" > </birt:param>
</birt:viewer>
<script language="javascript">
	 	document.getElementsByName("allAttendanceQuery")[0].align='center';
</script>
</body>

</html>