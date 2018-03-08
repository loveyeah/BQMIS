<%@ page language="java" pageEncoding="utf-8"%>
<%@page import="power.ear.comm.Employee"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
    <head>
        <title>保护投退申请单查询</title>
        <jsp:include page="../../../comm/jsp/commjsp.jsp"></jsp:include>
        <script type="text/javascript" src="comm/scripts/Constants.js"></script>
        <script type="text/javascript" src="comm/ext/ComboBoxTree.js"></script>   
         <script type="text/javascript" src="run/protectinoutapply/protect-comm.js"></script>   
       <script type="text/javascript">
	    var currentUser = "<%=((Employee) (session.getAttribute("employee"))).getWorkerCode()%>"
	   </script>
    </head>
    <body >        
        <script type="text/javascript" src="run/protectinoutapply/query/protectQuery.js"></script>
    </body>
</html>