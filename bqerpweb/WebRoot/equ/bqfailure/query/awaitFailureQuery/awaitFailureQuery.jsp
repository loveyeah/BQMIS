<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>待处理缺陷统计</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="comm/ext/urlparams.js"></script>
	<script type="text/javascript"
			src="comm/datepicker/WdatePicker.js" defer="defer"></script>
			
</head>
<body>
    <script type="text/javascript" src="comm/scripts/Constants.js"></script>
	<script type="text/javascript" src="equ/bqfailure/query/awaitFailureQuery/awaitFailureQuery.js"></script>
	<div id="orgGrid" style="width: 100%; height: 100%"></div>
	<div id="div_lay"></div>
</html>