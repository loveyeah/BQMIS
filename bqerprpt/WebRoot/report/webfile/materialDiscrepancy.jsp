<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<html>
	<%@ taglib prefix="birt" uri="/birt.tld"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
		<jsp:include page="../../comm/jsp/commjsp.jsp"></jsp:include>
	</head>
	<body>
		<div>
			<birt:report id="materialDiscrepancy"
				reportDesign="materialDiscrepancy.rptdesign"
				position="absolute" width="1024" height="1500" left="0" top="0"
				format="pdf" reportContainer="div" pageOverflow="1">
				<birt:param name="dateMonth"
					value="<%=request.getParameter("dateMonth")%>">
				</birt:param>
			</birt:report>
		</div>
	</body>

</html>