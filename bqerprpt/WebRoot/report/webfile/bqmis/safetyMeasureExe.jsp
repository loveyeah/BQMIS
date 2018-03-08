<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>

<!--  
<birt:viewer id="elec1"
    pattern="frameset"
    pageOverflow="1"
    reportDesign="ElectricOne.rptdesign"
    position="absolute"
    width="1400"
    height="800"
    left="0"
    top="0"
    format="html"
    showTitle="false"
    scrolling="no"
    frameborder="no"
    showNavigationBar="false">
     <birt:param name="no" value="<%= request.getParameter("no") %>" > </birt:param>
	 <birt:param name="worktickStauts" value="<%= request.getParameter("status") %>" > </birt:param>
</birt:viewer>
-->
<div >
<birt:report id="safetyMeasureExe" reportDesign="bqmis/safetyMeasureExe.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="no" value="<%=request.getParameter("no")%>" />
			<birt:param name="worktickStauts" value="<%= request.getParameter("status") %>" /> 
		</birt:report>
</div>		
<script  language='javascript'> 
document.getElementsByName('safetyMeasureExe')[0].align='center';
</script>

</body>

</html>