<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>检修项目验收单</title>
</head>
<body>
<div >
<birt:report id="repairaccept" reportDesign="bqmis/repairAccept.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="acceptId" value="<%=request.getParameter("acceptId")%>" />
		</birt:report>
</div>
	</body>
<script  language='javascript'> 
document.getElementsByName('repairaccept')[0].align='center';
</script>
</html>