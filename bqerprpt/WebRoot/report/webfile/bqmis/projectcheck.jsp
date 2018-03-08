<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>工程项目验收审批表预览</title>
</head>
<body>
<div >
<birt:report id="projectcheck" reportDesign="bqmis/projectcheck.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="projectId" value="<%=request.getParameter("projectId")%>" />
		</birt:report>
</div>
	</body>
<script  language='javascript'> 
document.getElementsByName('projectcheck')[0].align='center';
</script>
</html>