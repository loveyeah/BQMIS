<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<head>
<title>运行日志值班人员</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="comm/ext/urlparams.js"></script>
</head>
<body>
	<script type="text/javascript" src="run/runlog/business/worker/worker.js"></script>
	<div align="center">
		<table width="100%" height="100%" border="0">
			<tr>
				<td width="45%">
				<div id="left-div" style="overflow:auto;width:100%;height:99%"></div>
				</td>
				<td width="50px" id="button-file-div">
					<div id="add-div"></div>
					<br/><br/>
					<div id="delete-div"></div> 
				</td>
				<td width="50%">
				<div id="right-div" style="overflow:auto;width:100%;height:99%"></div>
				</td>
			</tr>
			<tr style="height:20"><td colspan="3"><div style='font-size:10pt' style="overflow:auto;width:100%;height:100%">值班人员说明:
                        <font color="green">■</font> 值班人员
                        <font color="navy">■</font> 日志使用人员
                        <font color="gray">■</font> 代班人员
                        <font color="red">■</font> 缺勤人员
                        <font color="blue">■</font>紧急换班人员</div></td></tr>
		</table>
		</div> 
</body>
</html>