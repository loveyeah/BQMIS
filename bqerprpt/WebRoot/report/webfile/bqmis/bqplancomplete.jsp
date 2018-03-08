<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<div >
<birt:report id="bqplancomplete" reportDesign="bqmis/bqplancomplete.rptdesign" position="absolute"
			width="1024" height="800" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="yearmonth" value="<%=request.getParameter("yearmonth")%>" />
		</birt:report>
</div>		
<script  language='javascript'> 
document.getElementsByName('bqplancomplete')[0].align='center';
</script>

</body>

</html>