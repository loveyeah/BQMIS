package power.web.manage.project.action;

import power.ejb.manage.project.PrjJInfo;
import power.ejb.manage.project.PrjJInfoFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ProjectApplyImpAction extends AbstractAction {

	PrjJInfoFacadeRemote remote;
	private PrjJInfo prjjInfo;
	private String prjNo;
	private String workflowType;
	private String actionId;
	private String eventIdentify;
	private String approveText;
	private String nextRoles;
	private String stepId;

	public ProjectApplyImpAction() {
		remote = (PrjJInfoFacadeRemote) factory
				.getFacadeRemote("PrjJInfoFacade");
	}

	public void newProject() {
		try {
			String msg = remote.reportTo(prjNo, workflowType, employee
					.getWorkerCode(), actionId, approveText, nextRoles,
					employee.getEnterpriseCode());
			write("{success:true,msg:'" + msg + "'}");
		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}

	public void approveProject() {
		try {
			String msg = remote.approveStep(prjNo, workflowType, employee
					.getWorkerCode(), actionId, eventIdentify, approveText,
					nextRoles, stepId, employee.getEnterpriseCode());
			write("{success:true,msg:'" + msg + "'}");
		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}

	public void reportProject() {
		try {
			String msg = remote.checkReport(prjNo, workflowType, employee
					.getWorkerCode(), actionId, eventIdentify, approveText,
					nextRoles, stepId, employee.getEnterpriseCode());
			write("{success:true,msg:'" + msg + "'}");
		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}

	public void checkProject() {
		try {
			String msg = remote.checkStep(prjNo, workflowType, employee
					.getWorkerCode(), actionId, eventIdentify, approveText,
					nextRoles, stepId, employee.getEnterpriseCode());
			write("{success:true,msg:'" + msg + "'}");
		} catch (Exception e) {
			write("{failure:true,msg:'" + e.getMessage() + "'}");
		}
	}

	public PrjJInfo getPrjjInfo() {
		return prjjInfo;
	}

	public void setPrjjInfo(PrjJInfo prjjInfo) {
		this.prjjInfo = prjjInfo;
	}

	public String getActionId() {
		return actionId;
	}

	public void setActionId(String actionId) {
		this.actionId = actionId;
	}

	public String getApproveText() {
		return approveText;
	}

	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}

	public String getNextRoles() {
		return nextRoles;
	}

	public void setNextRoles(String nextRoles) {
		this.nextRoles = nextRoles;
	}

	public String getStepId() {
		return stepId;
	}

	public void setStepId(String stepId) {
		this.stepId = stepId;
	}

	public String getWorkflowType() {
		return workflowType;
	}

	public void setWorkflowType(String workflowType) {
		this.workflowType = workflowType;
	}

	public String getPrjNo() {
		return prjNo;
	}

	public void setPrjNo(String prjNo) {
		this.prjNo = prjNo;
	}

	public String getEventIdentify() {
		return eventIdentify;
	}

	public void setEventIdentify(String eventIdentify) {
		this.eventIdentify = eventIdentify;
	}
}
