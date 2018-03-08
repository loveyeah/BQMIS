<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<div >
<birt:report id="protect" reportDesign="bqmis/protectInOutApply.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="no" value="<%=request.getParameter("no")%>" />
		</birt:report>
</div>		
<script  language='javascript'> 
document.getElementsByName('protect')[0].align='center';
</script>

</body>

</html>