<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
</head>
<body>
	<div>
		<birt:report id="steamengenmonitor" 
			reportDesign="bqmis/steamengenmonitor.rptdesign" 
			position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf" 
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="dutytime" value="<%=request.getParameter("dutyTime") %>" />
			<birt:param name="dutyclass" value="<%=request.getParameter("dutyShift") %>" />
		</birt:report>
	</div>
</body>
</html>