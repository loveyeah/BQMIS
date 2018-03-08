<%@ page language="java" contentType="text/html; charset=UTF-8"%>


<html>
	<%@ taglib prefix="birt" uri="/birt.tld"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
	</head>
	<body>
		<div>
			
			<birt:report id="opreaterCount"
				reportDesign="bqmis/opticketCount.rptdesign"
				position="absolute" width="1024" height="1500" left="0" top="0"
				format="pdf" reportContainer="div">
				<birt:param name="statBy" value="<%=request.getParameter("statBy")%>" />
				<birt:param name="title" value="<%=request.getParameter("title")%>" />
				<birt:param name="yearMonth" value="<%=request.getParameter("yearMonth")%>" />
				<birt:param name="specialCode" value="<%=request.getParameter("specialCode")%>" />
			</birt:report>
		</div>
</html>