<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<title></title>
</head>
<body>
<!--  
<birt:viewer id="elec2"
	 top="0"
	 left="0"
	 width="650"
	 height="1100"
	 reportDesign="ElectricTwo.rptdesign"
	 scrolling="auto"
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
<birt:report id="electricTwo" reportDesign="bqmis/ElectricTwo.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="no" value="<%=no%>" />
			<birt:param name="worktickStauts" value="<%= request.getParameter("status") %>" /> 
		</birt:report>
</div>		
<script  language='javascript'> 
document.getElementsByName('electricTwo')[0].align='center';
</script>
	</body>

</html>