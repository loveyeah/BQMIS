<%@ page language="java"  contentType="text/html; charset=UTF-8"%>

<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type">
<title>部门出勤统计</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../../../comm/scripts/Constants.js"></script>
</head>
<body>
<birt:viewer id="deptOndutyStatistics"
	 top="0"
	 left="0"
	 width="1100"
	 height="630"
	 reportDesign="hr/ca/deptOndutyStatistics.rptdesign"
	 showTitle="false"
	 showToolBar = "false"
	 pageOverflow="1"
	 format="html"
	 >	
	 <birt:param name="deptId" value="<%= request.getParameter("deptId") %>" > </birt:param>
	 <birt:param name="year" value="<%= request.getParameter("year") %>" > </birt:param>
	 <birt:param name="month" value="<%= request.getParameter("month") %>" > </birt:param>
	 <birt:param name="enterpriseCode" value="<%= request.getParameter("enterpriseCode") %>" > </birt:param>
</birt:viewer>
<script language="javascript">
	 	document.getElementsByName("deptOndutyStatistics")[0].align='center';
</script>
</body>

</html>