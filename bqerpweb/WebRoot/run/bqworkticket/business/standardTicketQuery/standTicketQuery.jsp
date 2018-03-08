<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<html>
	<head>
		<title>工作票查询</title>
		<jsp:include page="../../../../comm/jsp/commjsp.jsp"></jsp:include>
	</head>
	<body>
		<script type="text/javascript" src="comm/scripts/Constants.js"></script>
<script type="text/javascript" src="run/bqworkticket/workticketComm.js"></script>  
 <script type="text/javascript" src="comm/ext/RowExpander.js"></script>     
		<script type="text/javascript"
			src="run/bqworkticket/business/standardTicketQuery/standTicketQuery.js"></script>
								<script type="text/javascript">
	var currentUser = "<%=((Employee) (session.getAttribute("employee"))).getWorkerCode()%>"
	</script>
	  <div id='mygrid' ></div>
	</body>
</html>