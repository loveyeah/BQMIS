<%@ page language="java"  contentType="text/html; charset=UTF-8"%>

<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<title>职工履历表</title>
<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../comm/scripts/Constants.js"></script>
</head>
<body>
<birt:viewer id="employeeRecord"
	 top="0"
	 left="0"
	 width="970"
	 height="1150"
	 reportDesign="hr\employeeRecord.rptdesign"
	 showTitle="false"
	 showToolBar = "false"
	 pageOverflow="1"
	 frameborder="no"
	 format="html"
	 >		 
	 <birt:param name="enterpriseCode" value="<%=request.getParameter("enterpriseCode")%>" > </birt:param>	
	 <birt:param name="empId" value="<%=request.getParameter("empId")%>" > </birt:param>
</birt:viewer>
<script language="javascript">
	 	document.getElementsByName("employeeRecord")[0].align='center';
</script>
</body>

</html>