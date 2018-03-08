<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>项目合同付款审批表预览</title>
</head>
<body>
<div >
<birt:report id="GCContractBalance" reportDesign="bqmis/GCContractBalance.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="balanceId" value="<%=request.getParameter("balanceId")%>" />
		</birt:report>
</div>
	</body>
<script  language='javascript'> 
document.getElementsByName('GCContractBalance')[0].align='center';

</script>


</html>