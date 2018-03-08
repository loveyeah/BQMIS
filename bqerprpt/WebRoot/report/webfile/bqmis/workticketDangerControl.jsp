<%@ page language="java" contentType="text/html; charset=UTF-8"%>


<html>
	<%@ taglib prefix="birt" uri="/birt.tld"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
	</head>
	<body>
		<div>
			<%
			//String workticketNo = new String(request.getParameter(
			//			"workticketNo").getBytes("iso-8859-1"), ("utf-8"));
				String workticketNo =  request.getParameter("workticketNo") ;
			%>
			<birt:report id="dangerControl"
				reportDesign="bqmis/workticketDangerControl.rptdesign"
				position="absolute" width="1024" height="1500" left="0" top="0"
				format="pdf" reportContainer="div">
				<birt:param name="workticketNo" value="<%=workticketNo%>" />
			</birt:report>
		</div>
</html>