package power.web.equ.workbill.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;

import power.ejb.equ.workbill.EquJWo;
import power.ejb.equ.workbill.EquJWoFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EquJWoAction extends AbstractAction {
	EquJWoFacadeRemote remote;
	private int start;
	private int limit;

	public EquJWoAction() {
		remote = (EquJWoFacadeRemote) factory.getFacadeRemote("EquJWoFacade");
	}

	public void getAllWorkbillList() throws JSONException {
		String reqBeginTime = request.getParameter("startdate");
		String reqEndTime = request.getParameter("enddate");
		String workorderStatus = request.getParameter("workorderStatus");
		String workorderType = request.getParameter("workorderType");
		String professionCode = request.getParameter("professionCode");
		String repairDepartment = request.getParameter("repairDepartment");
		String ifWorkticket = request.getParameter("ifWorkticket");
		String ifMaterials = request.getParameter("ifMaterials");
		String argFuzzy = request.getParameter("argFuzzy");
		String objstart = request.getParameter("start");
		String objlimit = request.getParameter("limit");
		String flag = request.getParameter("flag");
		int start = 0;
		int limit = 18; 
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit")); 
		}  
		PageObject list = null;
		String entryIds = "";
		if("approve".equals(flag)){
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			entryIds = workflowService.getAvailableWorkflow(
						new String[] { "GDSP" }, employee.getWorkerCode());
		}
		list = remote.FindByMoreCondition(reqBeginTime, reqEndTime,
				workorderStatus, workorderType, professionCode,
				repairDepartment, ifWorkticket, ifMaterials, employee
						.getEnterpriseCode(), argFuzzy,flag,entryIds, start, limit);
		if (list != null) {
			String reStr = JSONUtil.serialize(list.getList());
			write("{data:{totalCount:" + list.getTotalCount() + ",list:"
					+ reStr + "}}");
		} else {
			write("{data:{totalCount:0,list:[]}}");
		}
	}

	public void checkIfDelete() {
		String woCode = request.getParameter("woCode");
		//String enterprisecode = request.getParameter("");
		if (remote.isDeleteWorkbillCheck(woCode, employee.getEnterpriseCode()))
			write("0");
		else
			write("1");
	}
	
	
	
	public void reportWorkBill(){
		 String actionId=request.getParameter("actionId");
		  String busitNo=request.getParameter("busitNo");
		  String flowCode=request.getParameter("flowCode");
		  String workerCode=request.getParameter("workerCode");
		  String approveText=request.getParameter("approveText");
		  remote.reportWorkBill(busitNo, flowCode, workerCode,approveText,Long.parseLong(actionId));
		  write("{success:true,msg:'&nbsp&nbsp&nbsp上报成功&nbsp&nbsp&nbsp'}");
		
	}
	
	public void approveWorkBill(){
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String identify = request.getParameter("eventIdentify");
		String woId=request.getParameter("woId");
		String nextRolePerson=request.getParameter("nextRolePerson");
		if (entryId != null && actionId != null) {
			try {
			boolean flag=remote.approveWorkBill(entryId, workerCode, actionId, approveText, nextRoles, identify, woId, employee.getEnterpriseCode(),nextRolePerson);
			if (flag) {
				write("{success:true,data:'审批成功!'}");
			}else{
				write("{success:true,data:'审批失败!'}");
			}
			} catch (Exception e) {
				write("{success:false,errorMsg:'" + e.getMessage() + "'}");
			}
		}
	}
	public void deleteWorkbill() {
		String woCode = request.getParameter("woCode");
		EquJWo model = remote.findOnebill(woCode);
		if (model != null) {
			remote.delete(model);
		}
	}

	
}
