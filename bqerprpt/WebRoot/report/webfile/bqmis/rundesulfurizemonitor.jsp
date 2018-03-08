<%@ page language="java" contentType="text/html; charset=UTF-8" %>

<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title></title>
</head>
<%String dutyTime = request.getParameter("dutyTime");%>
<%String dutyShift = request.getParameter("dutyShift");%>
<%dutyShift = dutyShift.substring(0,2) + "班"; %>
<body>
	<div>
		<birt:report id="runboilermonitor" 
			reportDesign="bqmis/rundesulfurizemonitor.rptdesign" 
			position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf" 
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="dutytime" value="<%=dutyTime%>" />
			<birt:param name="dutyclass" value="<%=dutyShift%>" />
		</birt:report>
	</div>
</body>
</html>