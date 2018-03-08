<%@ page contentType="text/html; charset=utf-8" %>
<%@page import="com.opensymphony.engineassistant.po.WorkflowActivity,com.opensymphony.engineassistant.po.WorkflowEvent"%> 
<%@ page import="java.util.List" %>
<%@page import="java.util.Map"%> 
<%
    String un = request.getParameter("un");
    String workflowType = request.getParameter("workflowType"); 
%>
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/style.css"> 
    <script>
    	function newInstance()
    	{ 
	    	var actionId = getActionId(); 
	   		var url = "newworkflow.jsp?un=<%=request.getParameter("un") %>&workflowType=<%=request.getParameter("workflowType") %>&actionId="+actionId;
	   		form1.action=url;
	   		
    	}
    	
    	function getActionId()
    	{
    		var actions = document.getElementsByName("actions");
    		for(i=0;i<actions.length;i++)
    		{
    			if(actions[i].checked)
    			{
    				return actions[i].value;
    			}
    		}
    		
    	}
    	
    </script>
</head>

<body>
<%
	 com.opensymphony.workflow.service.WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
 	
 	 WorkflowActivity step = workflowService.findFirstStep(workflowType,null);
 
 %>
<form name="form1" method="post" onSubmit="newInstance()">
<table width="621" height="248" cellpadding="0" cellspacing="0" bordercolor="#CCCCCC" border="1"> <input type="hidden" name="un" value="<%=un%>">
        <tr>
            <th height="18" colspan="2">
                <div align="left">创建流程</div> 
            </th>
        </tr>
        <tr>
            <td width="189">请选择操作：</td>
            <td width="422" height="18">
            	<%
            		List<WorkflowEvent> actions = step.getActions();
            		for(WorkflowEvent action : actions)
            		{
            			String name = action.getActionName() + action.getActionId() + action.getUrl()+"|||"+action.getChangeBusiStateTo()+"|||"+action.getExeBusiComponent(); 
        				Long action_id = action.getActionId();
            			%>
		   <li> <input  name="actions" type="radio" value="<%= action_id%>"/><%=name%> 
				 <%
            		}
            	 %>
            	
            </td>
        </tr>
        <tr>
            <td>文档内容：</td>
            <td height="35"> 
            </td>
        </tr>
        <tr>
            <td height="18" colspan="2">
                <div align="center"><input type="submit" value="提交"></div>
            </td>
        </tr> 
</table>
  </form> 
<jsp:include page="nav.jsp" flush="true">
    <jsp:param name="un" value="<%=un%>"/>
    <jsp:param name="workflowType" value="<%=workflowType%>"/>
</jsp:include>
</body>
</html>