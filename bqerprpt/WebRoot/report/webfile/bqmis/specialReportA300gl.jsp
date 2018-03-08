<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<html>
	<%@ taglib prefix="birt" uri="/birt.tld"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
	</head>
	<body>
		<div> 
			<birt:viewer id="specialReportA300gl" reportDesign="bqmis/specialReportA300gl.rptdesign"
				position="absolute" width="1024" height="700" left="0" top="0" showTitle="false" 
				 showToolBar = "false" pageOverflow="2"
				 format="html" pattern="run"
				 >
				<birt:param name="month"
					value="<%=request.getParameter("month")%>" />
				<birt:param name="reportCode"
					value="<%=request.getParameter("reportCode")%>" />
			</birt:viewer>
		</div>
	</body>
	<script language='javascript'> 
		document.getElementsByName('specialReportA300gl')[0].align='center';
</script>
</html>