<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%> 
<%Employee employee =(Employee) session.getAttribute("employee");
	String workcode = employee.getWorkerCode().toString();
 %>
<html>
	<head>
		<title>基本信息</title>
		
		<script type="text/javascript"
			src="comm/datepicker/WdatePicker.js" defer="defer"></script>
	
	</head>
	<body>
		<center><OBJECT ID=PBRX WIDTH=1000 HEIGHT=777 CLASSID=CLSID:BBBB1304-BBBB-1000-8000-080009AC61A9>
                  <PARAM NAME=_Version VALUE=65536>
                  <PARAM NAME=_ExtentX VALUE=15843>
                  <PARAM NAME=_ExtentY VALUE=9172>
                  <PARAM NAME=_StockProps VALUE=0>
                  <PARAM NAME=DisplayRuntimeMessages VALUE=true>
                  <PARAM NAME=CommandParm VALUE="<%=workcode%>"> 
                  <PARAM NAME=PBWindow VALUE="w_j_sqjc_plan_cx">
                  <PARAM NAME=LibList VALUE="http://10.10.100.25:8080/power/pb_jsjc/sp_techlogy.pbd;">
                  </OBJECT></center>
		
	</body>
</html>