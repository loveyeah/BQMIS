<%@ page language="java"  contentType="text/html; charset=UTF-8"%>
<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title></title>
</head>
<body>
<div >
<birt:report id="receiptBillAddOfGd" reportDesign="receiptBillAddOfGd.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="0">
			<birt:param name="receiveWHSName" value="<%= request.getParameter("whsName") %>" > </birt:param>
	        <birt:param name="purNo" value="<%= request.getParameter("purNo") %>" > </birt:param>  
	        <birt:param name="receiveDate" value="<%= request.getParameter("receiveDate") %>" > </birt:param>
	        <birt:param name="arrivalNo" value="<%= request.getParameter("arrivalNo") %>" > </birt:param>
	        <birt:param name="contractNo" value="<%= request.getParameter("contractNo") %>" > </birt:param>  
	         <birt:param name="operateDate" value="<%= request.getParameter("operateDate") %>" > </birt:param> 
			 <birt:param name="flag" value="<%= request.getParameter("flag") %>" > </birt:param> 
			  <birt:param name="materialId" value="<%= request.getParameter("materialId") %>" > </birt:param> 
			
		</birt:report>
</div>		
<script  language='javascript'> 
document.getElementsByName('receiptBillAddOfGd')[0].align='center';
</script>
</body>
</html>