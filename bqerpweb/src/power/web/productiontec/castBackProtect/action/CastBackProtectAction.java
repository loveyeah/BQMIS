package power.web.productiontec.castBackProtect.action;

import java.text.ParseException;
import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.castBackProtect.PtJProtectApply;
import power.ejb.productiontec.castBackProtect.PtJProtectApplyFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

@SuppressWarnings("serial")
public class CastBackProtectAction extends AbstractAction {

	
	private PtJProtectApply protectApply;
	private int start;
	private int limit;
	private PtJProtectApplyFacadeRemote remote;

	public CastBackProtectAction()
	{
		remote  = (PtJProtectApplyFacadeRemote) factory
		.getFacadeRemote("PtJProtectApplyFacade");
	}


	public void getCastBackProtectApproveList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");

		String inOut = request.getParameter("inOut");
		String protectionType = request.getParameter("protectionType");
		String applyId = request.getParameter("applyId");
		String status = request.getParameter("status");
		
		String entryIds = "";
		if(status != null && status.equals("approve")){
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqCastBackProtectApprove" }, employee.getWorkerCode());
		}                           
		
		PageObject pg = null;
		if (start != null && limit != null)
			pg = remote.getCastBackProtectApproveList(inOut, protectionType,applyId, status,entryIds,
					employee.getEnterpriseCode(),Integer.parseInt(start), Integer.parseInt(limit));
		else
			pg = remote.getCastBackProtectApproveList(inOut, protectionType, applyId,status,entryIds,employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}

	public void saveCastBackProtect() {
//		PtJProtectApply entity = remote.findByCastBackProtectId(protectApply.getApplyId());
		String applyId=request.getParameter("applyId");
		String status=request.getParameter("status");
		String entryId = request.getParameter("entryId");
		PtJProtectApply entity= remote.findByCastBackProtectId(Long.parseLong(applyId));
		if (protectApply.getApplyCode() != null
				&& !("".equals(protectApply.getApplyCode()))) {
			entity.setApplyCode(protectApply.getApplyCode());
		}
		if (protectApply.getExecutor() != null
				&& !("".equals(protectApply.getExecutor()))) {
			entity.setExecutor(protectApply.getExecutor());
		}
		if (protectApply.getClassLeader() != null
				&& !("".equals(protectApply.getClassLeader()))) {
			entity.setClassLeader(protectApply.getClassLeader());
		}
		
		if (protectApply.getGuardian() != null
				&& !("".equals(protectApply.getGuardian()))) {
			entity.setGuardian(protectApply.getGuardian());
		}
		
		if (protectApply.getExecuteTime() != null
				&& !("".equals(protectApply.getExecuteTime()))) {
			entity.setExecuteTime(protectApply.getExecuteTime());
		}
		if (protectApply.getBlockId() != null
				&& !("".equals(protectApply.getBlockId()))) {
			entity.setBlockId(protectApply.getBlockId());
		}
		if (protectApply.getProtectionName() != null
				&& !("".equals(protectApply.getProtectionName()))) {
			entity.setProtectionName(protectApply.getProtectionName());
		}
		if (protectApply.getProtectionReason() != null
				&& !("".equals(protectApply.getProtectionReason()))) {
			entity.setProtectionReason(protectApply.getProtectionReason());
		}
		if (protectApply.getMeasures() != null
				&& !("".equals(protectApply.getMeasures()))) {
			entity.setMeasures(protectApply.getMeasures());
		}
		remote.saveCastBackProtect(entity,applyId,status,entryId);
		write("{success:true,msg:'操作成功！'}");
		
	}

	public void approveCastBackProtect() throws NumberFormatException,
			ParseException {
		String eventIdentify = request.getParameter("eventIdentify");
		String applyId = request.getParameter("applyId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String responseDate = request.getParameter("responseDate");
		String nextRoles = request.getParameter("nextRoles");
		String nextRolePs = request.getParameter("nextRolePs");
		String approveText = request.getParameter("approveText");
		remote.approveCastBackProtect(Long.parseLong(applyId), workerCode,entryId, approveText, Long.parseLong(actionId), responseDate, nextRoles,nextRolePs, eventIdentify);
		write("{success:true,msg:'审批成功！'}");
	}

	public PtJProtectApply getProtectApply() {
		return protectApply;
	}


	public void setProtectApply(PtJProtectApply protectApply) {
		this.protectApply = protectApply;
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