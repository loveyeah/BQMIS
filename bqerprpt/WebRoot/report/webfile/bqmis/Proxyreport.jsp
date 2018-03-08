<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目合同委托书</title>
</head>
<body>
<div >
<birt:report id="Proxyreport" reportDesign="bqmis/Proxyreport.rptdesign" position="absolute"
			width="700" height="900" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="conId" value="<%=request.getParameter("conId")%>" />
		</birt:report>
</div>
	</body>
<script  language='javascript'> 
document.getElementsByName('Proxyreport')[0].align='center';

</script>


</html>