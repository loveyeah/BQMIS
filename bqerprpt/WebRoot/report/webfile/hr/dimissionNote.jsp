<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<html>
	<%@ taglib prefix="birt" uri="/birt.tld"%>
	<head>
		<title>离厂通知单</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="../../comm/scripts/Constants.js"></script>
	</head>
	<body>
		<birt:viewer id="dimissionNote" top="0" left="0" width="970"
			height="1150" reportDesign="hr\dimissionNote.rptdesign"
			showTitle="false" showToolBar="false" pageOverflow="1"
			frameborder="no" format="pdf">
			<birt:param name="empId"
				value="<%=request.getParameter("empId")%>">
			</birt:param>
		</birt:viewer>
		<script language="javascript">
	 	document.getElementsByName("dimissionNote")[0].align='center';
</script>
	</body>

</html>