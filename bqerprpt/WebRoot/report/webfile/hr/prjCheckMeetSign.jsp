<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<html>
	<%@ taglib prefix="birt" uri="/birt.tld"%>
	<head>
		<title>工程项目竣工验收会签单</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="../../comm/scripts/Constants.js"></script>
	</head>
	<body>
		<birt:viewer id="prjCheckMeetSign" top="0" left="0" width="970"
			height="1150" reportDesign="hr\prjCheckMeetSign.rptdesign"
			showTitle="false" showToolBar="false" pageOverflow="1"
			frameborder="no" format="pdf">
			<birt:param name="checkSignId"
				value="<%=request.getParameter("checkSingId")%>">
				alert(value)
			</birt:param>
			
		</birt:viewer>
		<script language="javascript">
	 	document.getElementsByName("prjCheckMeetSign")[0].align='center';
</script>
	</body>

</html>