<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<div >
<birt:report id="issueDetails" reportDesign="issueDetails.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="0">
			<birt:param name="whsName" value="<%= request.getParameter("whsName") %>" > </birt:param>
	        <birt:param name="issueNo" value="<%= request.getParameter("issueNo") %>" > </birt:param>  
	        <birt:param name="filledDate" value="<%= request.getParameter("filledDate") %>" > </birt:param>      
			 <birt:param name="operatedDate" value="<%= request.getParameter("operatedDate") %>" > </birt:param> 
			 <birt:param name="flag" value="<%= request.getParameter("flag") %>" > </birt:param>
			 <birt:param name="gdFlag" value="<%= request.getParameter("gdFlag") %>" > </birt:param>  
			 <birt:param name="materialId" value="<%= request.getParameter("materialId") %>" > </birt:param> 
		</birt:report>
</div>		
<script  language='javascript'> 
document.getElementsByName('issueDetails')[0].align='center';
</script>
</body>
</html>