<%@ page language="java" contentType="text/html; charset=UTF-8"%>


<html>
	<%@ taglib prefix="birt" uri="/birt.tld"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
	</head>
	<body>
		<div>
			<birt:viewer id="fillTecItemFinish"
				reportDesign="bqmis/fillTecItemFinish.rptdesign" position="absolute"
				width="1024" height="800" left="0" top="0" showTitle="false"
				pattern="run" pageOverflow="2">
				<birt:param name="month" value="<%=request.getParameter("month")%>" />
			</birt:viewer>
		</div>
	</body>
	<script language='javascript'> 
		document.getElementsByName('fillTecItemFinish')[0].align='center';
</script>


</html>