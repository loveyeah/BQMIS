<%@ page contentType="text/html; charset=utf-8" %>  
<%@ page import="com.opensymphony.workflow.service.WorkflowService" %>  
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head> 
<body>
<% 
    String un = request.getParameter("un"); 
    String workflowType = request.getParameter("workflowType"); 
    String actionId =  request.getParameter("actionId"); 
 	WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();  
    long id = 0;
    try {
        id = workflowService.doInitialize(workflowType,un,"S4200811Q0004");   
        workflowService.doAction(id,un,Long.parseLong(actionId),"上报意见",null);
    }
    catch (Exception e) {
        e.printStackTrace();
    } 
%>
新工作流创建完毕，编号为： <a href="test.jsp?un=<%=un%>&id=<%=id%>">#<%=id%>
</a>
 
<jsp:include page="nav.jsp" flush="true">
     <jsp:param name="un" value="<%=un%>"/>
     <jsp:param name="workflowType" value="<%=workflowType%>"/>
</jsp:include>
</body>
</html>