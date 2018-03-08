<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<html>
	<%@ taglib prefix="birt" uri="/birt.tld"%>
	<head>
		<title>月奖通知单</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="../../comm/scripts/Constants.js"></script>
	</head>
	<body>
		<birt:viewer id="monthRewardNotice" top="0" left="0" width="970"
			height="620" reportDesign="hr/monthRewardNotice.rptdesign"
			showTitle="false" showToolBar="false" pageOverflow="1"
			frameborder="no" format="pdf">
			<birt:param name="detailId"
				value="<%=request.getParameter("detailId")%>">
			</birt:param>
		</birt:viewer>
		<script language="javascript">
	 	document.getElementsByName("removePerson")[0].align='center';
</script>
	</body>

</html>