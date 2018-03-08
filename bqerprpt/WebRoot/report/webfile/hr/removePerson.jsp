<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<html>
	<%@ taglib prefix="birt" uri="/birt.tld"%>
	<head>
		<title>职工内部调动通知单</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="../../comm/scripts/Constants.js"></script>
	</head>
	<body>
		<birt:viewer id="removePerson" top="0" left="0" width="970"
			height="1150" reportDesign="hr\removePerson.rptdesign"
			showTitle="false" showToolBar="false" pageOverflow="1"
			frameborder="no" format="pdf">
			<birt:param name="requisitionNo"
				value="<%=request.getParameter("requisitionNo")%>">
			</birt:param>
			<birt:param name="removeType"
				value="<%=request.getParameter("removeType")%>">
			</birt:param>
			<birt:param name="printDate"
				value="<%=request.getParameter("printDate")%>">
			</birt:param>
			<birt:param name="stationRemoveIds"
				value="<%=request.getParameter("stationRemoveIds")%>">
			</birt:param>
		</birt:viewer>
		<script language="javascript">
	 	document.getElementsByName("removePerson")[0].align='center';
</script>
	</body>

</html>