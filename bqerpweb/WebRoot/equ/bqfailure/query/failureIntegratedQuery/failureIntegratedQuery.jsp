<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<html>
<head>
<title>缺陷综合统计</title>
<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	<script type="text/javascript" src="comm/ext/ext-lang-zh_CN.js"></script>
	<script type="text/javascript" src="comm/ext/urlparams.js"></script>
	<script type="text/javascript"
			src="comm/datepicker/WdatePicker.js" defer="defer"></script>
</head>
<body>
    <script type="text/javascript" src="comm/scripts/Constants.js"></script>
	<script type="text/javascript" src="equ/bqfailure/query/failureIntegratedQuery/amchart/amline/swfobject.js"></script>
	<script type="text/javascript" src="equ/bqfailure/query/failureIntegratedQuery/failureIntegratedQuery.js"></script>
	<div id="div_lay"></div>
	<div id="flashcontent" style="overflow: auto; width: 100%; height: 100%"></div>
</html>