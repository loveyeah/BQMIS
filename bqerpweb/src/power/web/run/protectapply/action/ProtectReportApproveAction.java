package power.web.run.protectapply.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.protectinoutapply.ProtectApplyApprove;
import power.ejb.run.protectinoutapply.RunJProtectinoutApprove;
import power.ejb.run.protectinoutapply.RunJProtectinoutapply;
import power.ejb.run.protectinoutapply.RunJProtectinoutapplyFacadeRemote;
import power.web.comm.AbstractAction;

public class ProtectReportApproveAction extends AbstractAction {

	protected ProtectApplyApprove approveRemote;
	protected RunJProtectinoutapplyFacadeRemote protectRemote;
	private RunJProtectinoutapply protectModel;
	private String actionId;
	private String eventIdentify;
	private String nextRoles;
	private String busitNo;
	private String workerCode;
	private String responseDate;
	private String approveText;

	public ProtectReportApproveAction() {
		approveRemote = (ProtectApplyApprove) factory
				.getFacadeRemote("ProtectApplyApproveImp");
		protectRemote = (RunJProtectinoutapplyFacadeRemote) factory
				.getFacadeRemote("RunJProtectinoutapplyFacade");
	}

	public void findProtectApproveList() throws JSONException {
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String applyDept = request.getParameter("applyDept");
		String status = request.getParameter("status");
		PageObject obj = new PageObject();
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(
				new String[] { "bqProtectApply" }, employee.getWorkerCode());

		if (objstart != null && objlimit != null) {
			obj = protectRemote.findApproveList(startDate, endDate, applyDept,
					status, entryIds, employee.getEnterpriseCode(), Integer
							.parseInt(objstart.toString()), Integer
							.parseInt(objlimit.toString()));
		} else {
			obj = protectRemote.findApproveList(startDate, endDate, applyDept,
					status, entryIds, employee.getEnterpriseCode());
		}
		if (obj == null || obj.getList() == null) {
			write("{\"list\":[],\"totalCount\":0}");
		} else {
			String str = JSONUtil.serialize(obj);
			write(str);
		}
	}

	public void protectReport() {
		String flowCode = request.getParameter("flowCode");

		approveRemote.reportTo(busitNo, flowCode, workerCode, Long
				.parseLong(actionId), approveText, nextRoles, eventIdentify,
				employee.getEnterpriseCode());
		write("{success:true,msg:'上报成功！'}");
	}

	/** 设备部专工签发 设备部主任签发 */
	public void protectCommSign() {

		RunJProtectinoutapply model = protectRemote.findByProtectNo(busitNo,
				employee.getEnterpriseCode());
		RunJProtectinoutApprove hisModel = new RunJProtectinoutApprove();
		hisModel.setProtectNo(busitNo);
		hisModel.setApproveBy(workerCode);
		hisModel.setApproveText(approveText);
		hisModel.setEnterpriseCode(employee.getEnterpriseCode());
		approveRemote.protectApplySign(model, hisModel, Long
				.parseLong(actionId), responseDate, nextRoles, eventIdentify);
		write("{success:true,msg:'审批成功！'}");
	}

	/** 厂领导批准 */
	public void protectFactoryLeadSign() {
		RunJProtectinoutapply model = protectRemote.findByProtectNo(busitNo,
				employee.getEnterpriseCode());
		// if(protectModel!=null)
		// {
		// model.setApproveStartDate(protectModel.getApproveStartDate());
		// model.setApproveEndDate(protectModel.getApproveEndDate());
		// }
		RunJProtectinoutApprove hisModel = new RunJProtectinoutApprove();
		hisModel.setProtectNo(busitNo);
		hisModel.setApproveBy(workerCode);
		hisModel.setApproveText(approveText);
		hisModel.setEnterpriseCode(employee.getEnterpriseCode());
		approveRemote.protectApplySign(model, hisModel, Long
				.parseLong(actionId), responseDate, nextRoles, eventIdentify);
		write("{success:true,msg:'审批成功！'}");
	}

	/**
	 * 当值班长审批
	 */

	public void currentChargerSign() {
		RunJProtectinoutapply model = protectRemote.findByProtectNo(busitNo,
				employee.getEnterpriseCode());
		if (protectModel != null) {
			model.setApproveStartDate(protectModel.getApproveStartDate());
			model.setApproveEndDate(protectModel.getApproveEndDate());
		}
		RunJProtectinoutApprove hisModel = new RunJProtectinoutApprove();
		hisModel.setProtectNo(busitNo);
		hisModel.setApproveBy(workerCode);
		hisModel.setApproveText(approveText);
		hisModel.setEnterpriseCode(employee.getEnterpriseCode());
		approveRemote.protectApplySign(model, hisModel, Long
				.parseLong(actionId), responseDate, nextRoles, eventIdentify);
		write("{success:true,msg:'审批成功！'}");
	}

	/**
	 * 运行人员回填信息
	 */
	public void protectRunManSign() {
		RunJProtectinoutapply model = protectRemote.findByProtectNo(busitNo,
				employee.getEnterpriseCode());
		if (protectModel != null) {
			model.setExecuteBy(protectModel.getExecuteBy());
			model.setKeeper(protectModel.getKeeper());
			// model.setPermitBy(protectModel.getPermitBy());
			// model.setCheckupBy(protectModel.getCheckupBy());
			if (model.getIsIn().equals("Y"))
				model.setProtectInDate(protectModel.getProtectInDate());
			else if (model.getIsIn().equals("N"))
				model.setProtectOutDate(protectModel.getProtectOutDate());
		}
		RunJProtectinoutApprove hisModel = new RunJProtectinoutApprove();
		hisModel.setProtectNo(busitNo);
		hisModel.setApproveBy(workerCode);
		hisModel.setApproveText(approveText);
		hisModel.setEnterpriseCode(employee.getEnterpriseCode());
		approveRemote.protectApplySign(model, hisModel, Long
				.parseLong(actionId), responseDate, nextRoles, eventIdentify);
		write("{success:true,msg:'审批成功！'}");
	}

	public RunJProtectinoutapply getProtectModel() {
		return protectModel;
	}

	public void setProtectModel(RunJProtectinoutapply protectModel) {
		this.protectModel = protectModel;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getEventIdentify() {
		return eventIdentify;
	}

	public void setEventIdentify(String eventIdentify) {
		this.eventIdentify = eventIdentify;
	}

	public String getNextRoles() {
		return nextRoles;
	}

	public void setNextRoles(String nextRoles) {
		this.nextRoles = nextRoles;
	}

	public String getBusitNo() {
		return busitNo;
	}

	public void setBusitNo(String busitNo) {
		this.busitNo = busitNo;
	}

	public String getWorkerCode() {
		return workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	public String getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(String responseDate) {
		this.responseDate = responseDate;
	}

	public String getApproveText() {
		return approveText;
	}

	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}

}
