<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<div >
<birt:report id="projectEndReport" reportDesign="projectEndReport.rptdesign" position="absolute"
			width="1124" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="0">
	 <birt:param name="report_id" value="<%= request.getParameter("reportId") %>" > </birt:param>
	  <birt:param name="back_entry_by" value="<%= request.getParameter("backEntryBy") %>" > </birt:param>           
</birt:report>
</div>		
<script  language='javascript'> 
document.getElementsByName('projectEndReport')[0].align='center';
</script>
</body>
</html>