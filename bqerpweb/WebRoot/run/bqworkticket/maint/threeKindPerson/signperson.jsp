<%@ page language="java" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
	<head>
		<title>工作票签发人维护</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
		<script type="text/javascript" src="comm/scripts/Constants.js"></script>
		<script type="text/javascript" src="hr/maint/empinfo/ComboxTree.js"></script>
	</head>
	<body  bgcolor="#EDEDED">
		<div id="header" style="width:100%; height:5%">
		</div>
		<div align="center" >
		<table width="80%" height="93%" border="0">
			<tr height="100%">
				<td width="45%">
					<div id="already-role-div" style="overflow:auto;width:100%;height:100%"></div>
				</td>
				<td width="5%" id="button-role-div">
					<div id="revoke-div" style="width:50%"></div>
					<br/><br/>
					<div id="grant-div" style="width:50%"></div>
				</td>
				<td width="45%">
				<div id="wait-role-div" style="overflow:auto;width:100%;height:100%"></div>
				</td>
			</tr>
		</table>
		</div>
		<script type="text/javascript" src="run/bqworkticket/maint/threeKindPerson/signperson.js"></script>
	</body>
</html>
