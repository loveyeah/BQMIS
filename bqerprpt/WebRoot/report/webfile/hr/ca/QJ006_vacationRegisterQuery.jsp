<%@ page language="java"  contentType="text/html; charset=UTF-8"%>

<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type">
<title>请假登记报表</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../../../comm/scripts/Constants.js"></script>
</head>
<body>
<birt:viewer id="vacationRegisterQuery"
	 top="0"
	 left="0"
	 width="1100"
	 height="650"
	 reportDesign="hr/ca/vacationRegisterQuery.rptdesign"
	 showTitle="false"
	 showToolBar = "false"
	 pageOverflow="1"
	 format="html"
	 >	
	 <birt:param name="fromDate" value="<%= request.getParameter("fromDate") %>" > </birt:param>
	 <birt:param name="toDate" value="<%= request.getParameter("toDate") %>" > </birt:param>
	 <birt:param name="enterpriseCode" value="<%= request.getParameter("enterpriseCode") %>" > </birt:param>
</birt:viewer>
<script language="javascript">
	 	document.getElementsByName("vacationRegisterQuery")[0].align='center';
</script>
</body>

</html>