<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<div >
<birt:report id="castBackProtect" reportDesign="bqmis/castBackProtect.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="applyId" value="<%=request.getParameter("applyId")%>" />
			<birt:param name="applyCode" value="<%=request.getParameter("applyCode")%>" />
			<birt:param name="enterpriseCode" value="<%=request.getParameter("enterpriseCode")%>" />
		</birt:report>
</div>
	</body>
<script  language='javascript'> 
document.getElementsByName('castBackProtect')[0].align='center';

</script>


</html>