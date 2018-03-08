package power.web.manage.budget.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmJDepreciationFacadeRemote;
import power.web.comm.AbstractAction;

public class DepreciationAction extends AbstractAction{

	private CbmJDepreciationFacadeRemote remote;
	public DepreciationAction()
	{
		remote=(CbmJDepreciationFacadeRemote)factory.getFacadeRemote("CbmJDepreciationFacade");
	}
	
	//上报
	public void depreciationReportTo()
	{
		String actionId=request.getParameter("actionId");
		String  approveText=request.getParameter("approveText");
		String workflowType=request.getParameter("flowCode");
		String nextRoles=request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		String depreciationId=request.getParameter("depreciationId");
		String workerCode=request.getParameter("workerCode");
		remote.reportTo(Long.parseLong(depreciationId), workflowType, workerCode, Long.parseLong(actionId), approveText, nextRoles, eventIdentify);
		write("{success:true,msg:'上报成功！'}");
	}
	
	//审批
	public void depreciationCommSign()
	{
		String actionId=request.getParameter("actionId");
		String  approveText=request.getParameter("approveText");
		String nextRoles=request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		String depreciationId=request.getParameter("depreciationId");
		String responseDate=request.getParameter("responseDate");
		String workerCode=request.getParameter("workerCode");
		remote.depreciationCommSign(Long.parseLong(depreciationId), approveText, workerCode, Long.parseLong(actionId), responseDate, nextRoles, eventIdentify);
		write("{success:true,msg:'审批成功！'}");
	}
	
	public void depreciationApproveQuery() throws JSONException
	{
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String budgetTime=request.getParameter("budgetTime");
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"budgetDetailApprove"}, employee.getWorkerCode());
		PageObject obj=new PageObject(); 
		
		if(objstart!=null&&objlimit!=null)
		{
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.depreciationApproveQuery(budgetTime, employee.getEnterpriseCode(), entryIds, start,limit);
		}
		else
		{
			obj=remote.depreciationApproveQuery(budgetTime, employee.getEnterpriseCode(), entryIds);
		}
		String strOutput = "";
		if (obj == null || obj.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(obj);
		}
		write(strOutput);
	}
}
