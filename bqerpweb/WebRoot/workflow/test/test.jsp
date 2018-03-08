<%@ page contentType="text/html; charset=utf-8" %>
<%@ page import="  com.opensymphony.engineassistant.po.WorkflowEvent, 
                 com.opensymphony.engineassistant.po.WfJHistoryoperation" %> 
<%@ page import="com.opensymphony.workflow.service.WorkflowService" %>   
<%@page import="com.opensymphony.engineassistant.po.WorkflowActivity"%> 
<%@ page import="java.util.List" %>
<%@page import="java.util.Map"%> 
<html>
<head>
    <link rel="stylesheet" type="text/css" href="/css/style.css">
    <script type="text/javascript">
        function dosubmit(id,stepId,stepName) {  
            var action = Number(id); 
            document.getElementById("do").value = action; 
            document.form1.submit();
        }
    </script> 
</head> 
<body>
<% 
    String un = request.getParameter("un");
    String workflowType = request.getParameter("workflowType");    
    String opinion = request.getParameter("opinion");
    Long id = Long.parseLong(request.getParameter("id"));
    String doString = request.getParameter("do");  
    WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
    
    
%>
<p> 当前登录用户：<%=un%>，所在的组： 
</p> 
<p>工作流编号：<%=id%>
</p>
<hr/> 
<p>可用操作：  <a href="#" id="a_showApproveInfo">查看审批信息</a> 
</p>

 <script>
    	 
    		document.getElementById("a_showApproveInfo").onclick = function()
    		{
    			 window.open("../manager/show/show.jsp?entryId="+ <%=id %>);
    			 return false;
    		}
    	 
    </script>
<%
 	 Map m =  new java.util.HashMap(); 
     m.put("isOld",true);
   	 m.put("is12Failure",true);
   	 m.put("isYSZC",true);
   	 m.put("isDBP" ,true); 
   	 m.put("sumBelow1w",true);
    	
	if (doString != null && !doString.equals("")) {
        Long action = Long.parseLong(doString);  
      
    	String nrs = request.getParameter("nrs");
    	String nps = request.getParameter("nps"); 
        workflowService.doAction(id,un, action,opinion,m,nrs,nps);  
        /*
        List<WorkflowActivity> steps = workflowService.getNextSteps(id,action);
        out.println("<br/>***************************************<br/>");
        
        for(WorkflowActivity s:steps)
        {
        	out.println("<br/>"+s.getStepName()+"<br/>");
        }
        out.println("<br/>***************************************<br/>");
        */
        
    } 
    List<WorkflowActivity> steps = workflowService.findCurrentSteps(id,un,m);
    if(steps==null)
    {
    out.println("<br/>流程已经结束");
    return ;
    }
    for(WorkflowActivity step:steps)
    {
     out.println("<br/>"+step.getStepName()+"<br/>url:"+step.getUrl()+"<br/>");
	List<WorkflowEvent> actions  = step.getActions();// workflowService.getActions(id);  
    for (int i = 0; i < actions.size(); i++) {
    	String name = actions.get(i).getActionName() + actions.get(i).getActionId(); 
        Long action_id = actions.get(i).getActionId();
%>
<li><a href="#" onclick="dosubmit(<%=action_id%>,<%=step.getStepId()%>,'<%=step.getStepName()%>');"><%=name%>
</a> 
<%
	}
	}
%> 
<hr>  
<p></p>
 <form name="form1" method="post" action="test.jsp">
<table width="621" height="248" cellpadding="0" cellspacing="0" bordercolor="#CCCCCC" border="1"> 
        <input type="hidden" name="un" value="<%=un%>">
        <input type="hidden" name="workflowType" value="<%=workflowType%>">
        <input type="hidden" name="id" value="<%=id%>">
        <input type="hidden" name="do" value="" id="do">
        <tr>
            <th height="18" colspan="2">
                <div align="left">   <br/>
指定下一步角色:<input type="text" id="nrs" name="nrs"/> <br/>
指定下一步人员:<input type="text" id="nps" name="nps"/> 
                </div>
            </th>
        </tr>
        <tr>
            <td width="189">文档标题：</td>
            <td width="422" height="18">
              
                <input type="text" name="title" value="xxxx" maxlength="61"
                       size="61"> 
            </td>
        </tr>
        <tr>
            <td>文档内容：</td>
            <td height="35">
              xxx真的是xxx
            </td>
        </tr> 
        <tr>
            <td>审批意见：</td>
            <td height="35"><textarea name="opinion" cols="60" rows="8"></textarea></td>
        </tr>  
        <%
        	List opinions = workflowService.getDocumentationOpinions(id);
            if (opinions != null && !opinions.isEmpty()) {
                for (int i = 0; i < opinions.size(); i++) {
                    WfJHistoryoperation dovo = (WfJHistoryoperation) opinions.get(i);
                    out.println("活动：" + dovo.getStepName() + "&nbsp;&nbsp;&nbsp;" );
                    out.println("动作：" + dovo.getActionName()+ "&nbsp;&nbsp;&nbsp;" );
                    out.println("调用者：" + dovo.getCallerName()+ "&nbsp;&nbsp;&nbsp;" );
                    out.println("审批意见：" + dovo.getOpinion()+ "&nbsp;&nbsp;&nbsp;" );
                    out.println("审批时间:" + dovo.getOpinionTime());
                    out.println("<br/>");
                }
            }
        %> 
</table>
 </form>
<p></p> 
<a href="#" onclick="window.open('viewlivegraph.jsp?id=<%=id%>')">查看工作流图表</a>
<jsp:include page="nav.jsp" flush="true">
    <jsp:param name="un" value="<%=request.getParameter("un")%>"/>
    <jsp:param name="workflowType" value="<%=workflowType%>"/>
</jsp:include>
</body>
</html>