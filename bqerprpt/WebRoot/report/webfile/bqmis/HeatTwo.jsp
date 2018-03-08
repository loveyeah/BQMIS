<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<!--  
<birt:viewer id="heat2"
	 top="0"
	 left="0"
	 width="900"
	 height="800"
	 reportDesign="HeatTwo.rptdesign"
	 showTitle="false"
	 showToolBar = "false"
	 pageOverflow="1"
	 frameborder="no"
	 format="html"
	 >
	 <birt:param name="no" value="<%= request.getParameter("no") %>" > </birt:param>
</birt:viewer>
-->
<div >
<%
String no = request.getParameter("no");
//String no = new String(request.getParameter("no").getBytes("iso-8859-1"),("utf-8"));
%>
<birt:report id="HeatTwo" reportDesign="bqmis/HeatTwo.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="no" value="<%=no%>" />
		</birt:report>
</div>

	</body>
<script  language='javascript'> 
document.getElementsByName('HeatTwo')[0].align='center';
</script>
</html>