<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>

<div>
<birt:report id="businessItem" reportDesign="bqmis/businessItem.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="0">
			<birt:param name="dateTime" value="<%=request.getParameter("dateTime")%>" />
		</birt:report>
</div>		
<script  language='javascript'> 
document.getElementsByName('failureBugReport')[0].align='center';
</script>

</body>

</html>