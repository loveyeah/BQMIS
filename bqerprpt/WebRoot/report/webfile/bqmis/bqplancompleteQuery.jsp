
<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<html>
	<%@ taglib prefix="birt" uri="/birt.tld"%>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title></title>
	</head>
	<body>
		<div> 
			<birt:viewer id="bqplancompleteQuery" reportDesign="bqmis/bqplancompleteQuery.rptdesign"
				position="absolute" width="900" height="800" left="0" top="0" showTitle="false" 
				 showToolBar = "false" pageOverflow="2"
				 format="html" pattern="run"
				 >
				<birt:param name="yearmonth" value="<%=request.getParameter("yearmonth")%>" />
			</birt:viewer>
		</div>
	</body>
	<script language='javascript'> 
		document.getElementsByName('bqplancompleteQuery')[0].align='center';
</script>


</html>