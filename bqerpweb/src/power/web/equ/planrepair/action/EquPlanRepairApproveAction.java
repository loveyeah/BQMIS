package power.web.equ.planrepair.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.planrepair.EquJPlanRepairMainFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquPlanRepairApproveAction extends AbstractAction{
	private EquJPlanRepairMainFacadeRemote remote;
	public EquPlanRepairApproveAction()
	{
		remote = (EquJPlanRepairMainFacadeRemote) factory
		.getFacadeRemote("EquJPlanRepairMainFacade");
	}
	
	public void findPlanRepairApproveList() throws JSONException
	{
		 int start=Integer.parseInt((request.getParameter("start")));
		 int limit=Integer.parseInt((request.getParameter("limit")));
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"bpPlanRepair"}, employee.getWorkerCode());
		PageObject object = remote.findRepairApproveList(startDate, endDate,entryIds, start,
				limit);
		if (object != null) {
			write(JSONUtil.serialize(object));
		} else {
			write("[]");
		}
	}
	
	public void planRepairApproveSign()
	{
		String actionId=request.getParameter("actionId");
		String  approveText=request.getParameter("approveText");
		String nextRoles=request.getParameter("nextRoles");
		String eventIdentify=request.getParameter("eventIdentify");
		String repairId=request.getParameter("repairId");
		String responseDate=request.getParameter("responseDate");
		String workerCode=request.getParameter("workerCode");
		remote.planRepairSign(Long.parseLong(repairId), approveText, workerCode,Long.parseLong(actionId), responseDate, nextRoles, eventIdentify);
		write("{success:true,msg:'审批成功！'}");
	}
}
