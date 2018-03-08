<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>

<div>
<birt:report id="failureBugReport" reportDesign="bqmis/failureBugReport.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="1">
			<birt:param name="year" value="<%=request.getParameter("year")%>" />
		</birt:report>
</div>		
<script  language='javascript'> 
document.getElementsByName('failureBugReport')[0].align='center';
</script>

</body>

</html>