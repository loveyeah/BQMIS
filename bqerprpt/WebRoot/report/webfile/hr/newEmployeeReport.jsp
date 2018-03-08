<%@ page language="java" contentType="text/html; charset=UTF-8"%>

<html>
	<%@ taglib prefix="birt" uri="/birt.tld"%>
	<head>
		<title>新进员工通知单</title>
		<jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="../../comm/scripts/Constants.js"></script>
	</head>
	<body>
		<birt:viewer id="newEmployeeReport" top="0" left="0" width="970"
			height="1150" reportDesign="hr\newEmployeeReport.rptdesign"
			showTitle="false" showToolBar="false" pageOverflow="1"
			frameborder="no" format="pdf">
			<birt:param name="empId"
				value="<%=request.getParameter("empId")%>">
			</birt:param>
			<birt:param name="advicenoteNo"
				value="<%=request.getParameter("advicenoteNo")%>">
			</birt:param>
			<birt:param name="printDate"
				value="<%=request.getParameter("printDate")%>">
			</birt:param>
			<birt:param name="newEmpids"
				value="<%=request.getParameter("newEmpids")%>">
			</birt:param>
		</birt:viewer>
		<script language="javascript">
	 	document.getElementsByName("newEmployeeReport")[0].align='center';
</script>
	</body>

</html>