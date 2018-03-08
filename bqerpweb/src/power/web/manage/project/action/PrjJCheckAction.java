package power.web.manage.project.action;

import java.util.Date;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.project.PrjJCheck;
import power.ejb.manage.project.PrjJCheckFacadeRemote;
import power.web.comm.AbstractAction;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

public class PrjJCheckAction extends AbstractAction {

	private PrjJCheckFacadeRemote checkRemote;
	private PrjJCheck check;
	private int start;
	private int limit;
	
	public PrjJCheckAction() {
		checkRemote = (PrjJCheckFacadeRemote)factory.getFacadeRemote("PrjJCheckFacade");
	}
	
	public void saveCheck(){
		check.setEntryBy(employee.getWorkerCode());
		check.setEntryDate(new Date());
		check.setStatusId(0l);
		check.setEnterpriseCode(employee.getEnterpriseCode());
		check = checkRemote.save(check,employee.getDeptId().toString());
		write("{success:true,msg:'保存成功！',checkId:'"+check.getCheckId()+"',reportCode:'"+check.getReportCode()+"'}");
	}
	
	public void backEtryCheck(){
		String checkId = request.getParameter("check_id");
		PrjJCheck model = checkRemote.findById(Long.parseLong(checkId));
		model.setCheckApply(check.getCheckApply());
		model.setApplyBy(check.getApplyBy());
		model.setApplyDate(check.getApplyDate());
		model.setApproveText(check.getApproveText());
		model.setApproveBy(check.getApproveBy());
		model.setApproveDate(check.getApproveDate());
		model.setCheckText(check.getCheckText());
		model.setCheckBy(check.getCheckBy());
		model.setCheckDate(check.getCheckDate());
		checkRemote.update(model);
		this.write("{success:true,status:'回录成功！'}");
	}
	
	public void queryReport() throws Exception{
		String ContractName=request.getParameter("projectName");
		String status=request.getParameter("statusId");
		String startl=request.getParameter("start");
		String limitl=request.getParameter("limit");
		PageObject page=new PageObject();
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"prjEndChek"}, employee.getWorkerCode());
		if (startl!=null&&!"".equals(startl)&&limitl!=null&&!"".equals(limitl)) {
			int start=Integer.parseInt(startl);
			int limit=Integer.parseInt(limitl);
			page=checkRemote.checkfindAll(ContractName, employee.getEnterpriseCode(), entryIds,employee.getDeptId(), start,limit);
		}else{
			page=checkRemote.checkfindAll(ContractName, employee.getEnterpriseCode(), entryIds,employee.getDeptId());
		}
		try {
			write(JSONUtil.serialize(page));
		} catch (JSONException e) {
			e.printStackTrace();
		}
}
	
	public void updateCheck(){
		String checkId = request.getParameter("checkId");
		check.setCheckId(Long.parseLong(checkId));
		check.setEntryBy(employee.getWorkerCode());
		check.setEntryDate(new Date());
		check.setEnterpriseCode(employee.getEnterpriseCode());
		check.setStatusId(0l);
		check.setIsUse("Y");
		checkRemote.update(check);
		write("{success:true,msg:'修改成功！'}");
	}
	
	public void deleteCheck() {
		String id = request.getParameter("ids");
		checkRemote.delete(id);
		write("{success:true,status:'删除成功！'}");
	}
	
	public void queryNoReportCheck() {
		String status = request.getParameter("status");
		String projectName = request.getParameter("projectName");
		String flag = request.getParameter("flag");
		
		PageObject obj = checkRemote.findAll(status,employee.getWorkerCode(), projectName,flag, "", "", start,limit);
		try {
			write(JSONUtil.serialize(obj));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
	/**
	 * 审批
	 */
	public void projectApply() {
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String identify = request.getParameter("eventIdentify");
		String applyId=request.getParameter("applyId");
		if (entryId != null && actionId != null) {
			try {
				boolean flag=checkRemote.endCheckApprove(entryId, workerCode, actionId, approveText, nextRoles, identify, applyId, employee.getEnterpriseCode());
				if (flag) {
					write("{success:true,data:'审批成功!'}");
				} else {
					write("{success:true,data:'审批失败!'}");
				}
			} catch (Exception e) {
				write("{success:false,errorMsg:'" + e.getMessage() + "'}");
			}
		}
	}
	/**
	 * 上报
	 */
	public void projectCheckReport()
	{
		  String actionId=request.getParameter("actionId");
		  String busitNo=request.getParameter("busitNo");
		  String flowCode=request.getParameter("flowCode");
		  String workerCode=request.getParameter("workerCode");
		  String approveText=request.getParameter("approveText");
		  checkRemote.endCheckReportTo(busitNo, workerCode,actionId, approveText,flowCode);
		  write("{success:true,msg:'&nbsp&nbsp&nbsp上报成功&nbsp&nbsp&nbsp'}");
	}
	public void projectEndCheckSave(){
		String chargeBy=request.getParameter("chargeBy");
		String ContractName=request.getParameter("contractName");
		PrjJCheck pjc=new PrjJCheck();
		pjc=checkRemote.findById(check.getCheckId());
		if (ContractName!=null&&!ContractName.equals("")) {
			pjc.setContractName(ContractName);
		};
		if (check.getConId()!=null&&!check.getConId().equals("")) {
			pjc.setConId(check.getConId());
		};
		if (check.getStartDate()!=null&&!check.getStartDate().equals("")) {
			pjc.setStartDate(check.getStartDate());
		};
		if (check.getEndDate()!=null&&!check.getEndDate().equals("")) {
			pjc.setEndDate(check.getEndDate());
		};
		if (check.getContractorName()!=null&&!check.getContractorName().equals("")) {
			pjc.setContractorName(check.getContractorName());
		};
		if (check.getContractorId()!=null&&!check.getContractorId().equals("")) {
			pjc.setContractorId(check.getContractorId());
		};
		if (check.getDeptChargeBy()!=null&&!check.getDeptChargeBy().equals("")) {
			pjc.setDeptChargeBy(check.getDeptChargeBy());
		};
		if (chargeBy!=null&&!chargeBy.equals("")) {
			pjc.setChargeBy(chargeBy);
		};
		if (check.getCheckAppraise()!=null&&!check.getCheckAppraise().equals("")) {
			pjc.setCheckAppraise(check.getCheckAppraise());
		};
		checkRemote.update(pjc);
		write("{success:true,msg:'&nbsp&nbsp&nbsp保存成功！！&nbsp&nbsp&nbsp'}");
	}
	
	public void queryAllCheck(){
		
	}
	
	public void queryCheckForApprove() {
		
	}
	
	public void endCheckReportTo() {
		
	}
	
	public void endCheckApprove() {
		
	}
	
	public void backEntryCheck() {
		
	}

	public PrjJCheck getCheck() {
		return check;
	}

	public void setCheck(PrjJCheck check) {
		this.check = check;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

}
