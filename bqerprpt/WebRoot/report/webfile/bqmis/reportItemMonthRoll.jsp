<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>

<div>
<%
String title = new String(request.getParameter("title").getBytes("iso-8859-1"),("utf-8"));
%>
<birt:report id="reportItemMonthRoll" reportDesign="bqmis/reportItemMonthRoll.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="0">
			<birt:param name="dateTime" value="<%=request.getParameter("dateTime")%>" />
			<birt:param name="reportType" value="<%=request.getParameter("reportType")%>" />
			<birt:param name="title" value="<%=title%>" />
		</birt:report>
</div>		
<script  language='javascript'> 
document.getElementsByName('reportItemMonthRoll')[0].align='center';
</script>

</body>

</html>