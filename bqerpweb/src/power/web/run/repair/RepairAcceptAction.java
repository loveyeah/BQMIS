package power.web.run.repair;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.repair.RunJRepairProjectAccept;
import power.ejb.run.repair.RunJRepairProjectAcceptFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class RepairAcceptAction extends AbstractAction {

	protected RunJRepairProjectAcceptFacadeRemote remote;
	private RunJRepairProjectAccept main;
	private String actionId;
	private String eventIdentify;
	private String nextRoles;
	private String busitNo;
	private String workerCode;
	private String approveText;

	public RepairAcceptAction() {
		remote = (RunJRepairProjectAcceptFacadeRemote) factory
				.getFacadeRemote("RunJRepairProjectAcceptFacade");
	}

	public void getRepairItemList() throws JSONException {
		String workflowStatus = request.getParameter("workflowStatus");
		String year = request.getParameter("year");
		String repairType = request.getParameter("cbxRepairType");
		String edition = request.getParameter("edition");

		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");

		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.getRepairItemList(workflowStatus, employee
					.getEnterpriseCode(), year, repairType, edition, start,
					limit);
		} else {
			obj = remote.getRepairItemList(workflowStatus, employee
					.getEnterpriseCode(), year, repairType, edition);
		}
		write(JSONUtil.serialize(obj));

	};

	public void repairAcceptApprove() {
		String acceptId = request.getParameter("acceptId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String eventIdentify = request.getParameter("eventIdentify");
		remote.repairAcceptApprove(Long.parseLong(acceptId), Long
				.parseLong(actionId), Long.parseLong(entryId), eventIdentify,
				workerCode, approveText, nextRoles);
		write("{success:true,msg:'审批成功！'}");
	}

	public void getRepairAcceptList() throws JSONException {
		String workflowStatus = request.getParameter("workflowStatus");
		String projectName = request.getParameter("projectName");
		String year = request.getParameter("year");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String isFillBy = request.getParameter("isFillBy");
		String fillSdate = request.getParameter("sd");
		String fillEdate = request.getParameter("ed");
		
		String entryIds = null;
		if (workflowStatus != null && workflowStatus.equals("approve")) {
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();

			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqRepairAcceptApprove" }, employee
							.getWorkerCode());
		}
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.getRepairAcceptList(workflowStatus, employee.getEnterpriseCode(), employee.getWorkerCode(),
					year, projectName, startDate, endDate, isFillBy, entryIds, fillSdate, fillEdate, start,limit);
		} else {
			obj = remote.getRepairAcceptList(workflowStatus, employee.getEnterpriseCode(), employee.getWorkerCode(),
					year, projectName, startDate, endDate, isFillBy, entryIds, fillSdate, fillEdate);
		}
		write(JSONUtil.serialize(obj));

	};

	public void saveRepairAcceptList() throws JSONException {
		String method = request.getParameter("method");
		if (method != null && method.equals("add")) {
			RunJRepairProjectAccept model = new RunJRepairProjectAccept();
			main.setWorkflowStatus("0");
			main.setEnterpriseCode(employee.getEnterpriseCode());
			model = remote.save(main);
			write("{success:true,data:" + JSONUtil.serialize(model)
					+ ",msg:'保存成功！'}");
		} else {
			RunJRepairProjectAccept model = remote.findById(main.getAcceptId());
			model.setFillBy(main.getFillBy());
			model.setFillDate(main.getFillDate());
			model.setRepairProjectName(main.getRepairProjectName());
			model.setWorkingCharge(main.getWorkingCharge());
			model.setWorkingMenbers(main.getWorkingMenbers());
			model.setStartTime(main.getStartTime());
			model.setEndTime(main.getEndTime());
			model.setSpecialityId(main.getSpecialityId());
			model.setCompletion(main.getCompletion());
			model.setMemo(main.getMemo());
			model.setFillBy(employee.getWorkerCode());
			remote.update(model);
			write("{success:true,data:" + JSONUtil.serialize(model)
					+ ",msg:'保存成功！'}");
		}
	};

	public void deleteRepairAcceptList() {
		String ids = request.getParameter("ids");
		RunJRepairProjectAccept model = remote.findById(Long.parseLong(ids));
		remote.delete(model);
		write("{success:true,msg:'删除成功！'}");
	};

	public void reportRepairAcceptList() {
		String flowCode = request.getParameter("flowCode");
		String nextRolePs = request.getParameter("nextRolePs");//人员
		remote.reportTo(busitNo, flowCode, workerCode,
				Long.parseLong(actionId), approveText, nextRoles,nextRolePs,
				eventIdentify, employee.getEnterpriseCode());
		write("{success:true,msg:'上报成功！'}");

	};

	public RunJRepairProjectAccept getMain() {
		return main;
	}

	public void setMain(RunJRepairProjectAccept main) {
		this.main = main;
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

	public String getApproveText() {
		return approveText;
	}

	public void setApproveText(String approveText) {
		this.approveText = approveText;
	};
}
