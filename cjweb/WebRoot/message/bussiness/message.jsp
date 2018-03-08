<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>消息管理</title>
        <jsp:include page="../../comm/jsp/commjsp.jsp"></jsp:include>        
        <script type="text/javascript" src="message/bussiness/message.js"></script>          
    </head>
    <body>
     <%
		Employee employee=(Employee)session.getAttribute("employee");
		%>
		<input type='hidden' id='workerName' value='<%=employee.getWorkerName()%>'/>
		<input type='hidden'  id='workerCode' value='<%=employee.getWorkerName()%>'/>
    </body>
</html>
