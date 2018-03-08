<%@ page language="java"  contentType="text/html; charset=UTF-8"%>


<html>
<%@ taglib prefix="birt" uri="/birt.tld" %>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>采购合同变更会签审批表预览</title>
</head>
<body>
<div >
<birt:report id="GCContractModifyMeetSign" reportDesign="bqmis/CGContractModifyMeetSign.rptdesign" position="absolute"
			width="1024" height="1500" left="0" top="0" format="pdf"
			reportContainer="div"
			pageOverflow="2">
			<birt:param name="modifyId" value="<%=request.getParameter("modifyId")%>" />
		</birt:report>
</div>
	</body>
<script  language='javascript'> 
document.getElementsByName('GCContractModifyMeetSign')[0].align='center';
</script>
</html>