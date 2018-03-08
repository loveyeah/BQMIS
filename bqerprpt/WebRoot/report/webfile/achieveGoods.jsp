<%@ page language="java"  contentType="text/html; charset=UTF-8"%>

<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>到货登记表</title>
<jsp:include page="../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="../../comm/scripts/Constants.js"></script>
</head>
<body>
<birt:viewer id="achieveGoods"
	 top="0"
	 left="0"
	 width="1200"
	 height="800"
	 reportDesign="achieveGoods.rptdesign"
	 showTitle="false"
	 showToolBar = "false"
	 pageOverflow="1"
	 format="html"
	 >	
	 <birt:param name="enterpriseCode" value="<%= request.getParameter("enterprisecode") %>" > </birt:param>
	 <birt:param name="createMan" value="<%= new java.lang.String(request.getParameter("workername").getBytes("ISO-8859-1"), "utf-8") %>" > </birt:param>
	 <birt:param name="mifNo" value="<%= request.getParameter("mifno") %>" > </birt:param>
</birt:viewer>
	</body>

</html>