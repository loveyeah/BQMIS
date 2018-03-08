<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<div >
<birt:report id="receiveIssuesDetailsWh" reportDesign="receiveIssuesDetailsWh.rptdesign" position="absolute"
			width="1524" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="0">
			<birt:param name="whsName" value="<%= request.getParameter("whsName") %>" > </birt:param>
			<birt:param name="whsNo" value="<%= request.getParameter("whsNo") %>" > </birt:param>
	        <birt:param name="month" value="<%= request.getParameter("month") %>" > </birt:param>    
</birt:report>
</div>		
<script  language='javascript'> 
document.getElementsByName('receiveIssuesDetailsWh')[0].align='center';
</script>
</body>
</html>