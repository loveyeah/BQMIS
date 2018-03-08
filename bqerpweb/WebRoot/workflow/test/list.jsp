<%@ page import="com.opensymphony.workflow.service.WorkflowService" %> 
<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="com.opensymphony.engineassistant.po.MyJobMessage"%>
<%@ page import="java.util.List" %>
<%
    String un = request.getParameter("un"); 
    String workflowType = request.getParameter("workflowType"); 
    WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
    //String str =  workflowService.getHasRightEntryIds(workflowType,un);
    String str =  workflowService.getAvailableWorkflow(new String[]{workflowType} ,un);
    String[] instances = null;
    if(str!=null)
    {
  		instances = str.split(",");//.getAvailableWorkflowInstances(workflowType);
    }
    List<MyJobMessage> messages =  workflowService.getMyJobMessage(un);
    if(messages!=null && messages.size()>0)
    {
    	out.println( "<hr/><"+un+">您当前有:<br/>");
    	for(MyJobMessage o: messages)
    	{
    		out.println("<a href=\""+o.getFlowListUrl()+"\">"+o.getCount()+"张"+o.getFlowName()+"("+o.getFlowType()+")等待审批</a><br/>");
    	}
    	out.println( "<hr/>");
    }
%>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
</head> 
<body>
<table width="100" cellpadding="0" cellspacing="0" bordercolor="#0000FF" border="0"> 
    <tr>
        <th>流程实例编号</th>
    </tr>
    <%
        if (instances != null && instances.length > 0) {
            for (int i = 0; i < instances.length; i++) {
    %>
    <tr>
        <td align="center"><a href="test.jsp?un=<%=un%>&id=<%=instances[i]%>" target="_blank"><%=instances[i]%>
        </a></td>
    </tr>
    <%
            }
        }
    %>

</table>
<p></p>
<jsp:include page="nav.jsp" flush="true">
    <jsp:param name="un" value="<%=un%>"/>
    <jsp:param name="workflowType" value="<%=workflowType%>"/>
</jsp:include>
</body>
</html>