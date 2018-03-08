<%@ page language="java"  contentType="text/html; charset=UTF-8"%>

<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type">
<title>劳动合同台帐</title>
<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../../comm/scripts/Constants.js"></script>
</head>
<body>
<birt:viewer id="emploeeyContract"
	 top="0"
	 left="0"
	 width="1100"
	 height="620"
	 reportDesign="hr/dueContract.rptdesign"
	 showTitle="false"
	 showToolBar = "false"
	 pageOverflow="1"
	 format="html"
	 >	
	 <birt:param name="startDate" value="<%= request.getParameter("startDate") %>" > </birt:param>
	 <birt:param name="endDate" value="<%= request.getParameter("endDate") %>" > </birt:param>
	 <birt:param name="deptCode" value="<%= request.getParameter("deptCode") %>" > </birt:param>
	 <birt:param name="contractTerm" value="<%= request.getParameter("contractTerm") %>" > </birt:param>
	 <birt:param name="contractType" value="<%= request.getParameter("contractType") %>" > </birt:param>
	 <birt:param name="duetoTime" value="<%= request.getParameter("duetoTime") %>" > </birt:param>
	 <birt:param name="enterpriseCode" value="<%= request.getParameter("enterpriseCode") %>" > </birt:param>
	 <birt:param name="isDueOrNot" value="<%= request.getParameter("isDueOrNot") %>" > </birt:param>
</birt:viewer>
<script language="javascript">
	 	document.getElementsByName("emploeeyContract")[0].align='center';
</script>
</body>

</html>