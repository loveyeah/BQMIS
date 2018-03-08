<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>经济责任技术指标部门考核兑现表</title>
</head>
<body>
<div >
<birt:report id="deptcash1" reportDesign="bqmis/deptcash1.rptdesign" position="absolute"
			width="1024" height="900" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="dateTime" value="<%=request.getParameter("dateTime")%>" />
		</birt:report>
</div>
	</body>
<script  language='javascript'> 
document.getElementsByName('deptcash1')[0].align='center';

</script>


</html>