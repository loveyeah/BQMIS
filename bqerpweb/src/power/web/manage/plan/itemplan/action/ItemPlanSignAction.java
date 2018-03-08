package power.web.manage.plan.itemplan.action;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ejb.manage.plan.itemplan.BpJItemplanDepMain;
import power.ejb.manage.plan.itemplan.EcomonicItemPlanManager;
import power.ejb.manage.plan.itemplan.ItemPlanSignManager;
import power.ejb.manage.plan.maintWeekPlan.BpJPlanRepairGather;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

@SuppressWarnings("serial")
public class ItemPlanSignAction extends AbstractAction {

	private ItemPlanSignManager remote;
	private EcomonicItemPlanManager remoteA;

	public ItemPlanSignAction() {
		remote = (ItemPlanSignManager) factory
				.getFacadeRemote("ItemPlanSignManagerImpl");

		remoteA = (EcomonicItemPlanManager) factory
				.getFacadeRemote("EcomonicItemPlanManagerImpl");

	}

	/**
	 * 全厂指标计划上报
	 */
	public void wholeItemPlanReport() {
		/**
		 * planDate 计划时间 100609 bjxu
		 */
		String planDate = request.getParameter("planDate");
		
		String mainId = request.getParameter("planmainid");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("flowCode");
		String actionId = request.getParameter("actionId");
		remote.wholeItemPlanReport(Long.parseLong(mainId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, workflowType);
		/**
		 * 短信通知 bjxu
		 */
			
		PostMessage postMsg = new PostMessage();
		String thisRoleString = (nextRoles == null || "".equals(nextRoles)) ? postMsg
				.getFistStepRoles(workflowType, actionId, null, null)
				: nextRoles;
		String msg = planDate + "全厂指标计划已上报，请您及时处理。";
		postMsg.sendMsg(thisRoleString, msg);
		write("{success:true,msg:'上报成功！'}");

	}

	/**
	 * 全厂指标计划审批
	 */
	public void wholeItemPlanApprove() {
		String mainId = request.getParameter("planmainid");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		remote.wholeItemPlanApprove(Long.parseLong(mainId), Long
				.parseLong(actionId), Long.parseLong(entryId), workerCode,
				approveText, nextRoles);
		write("{success:true,msg:'审批成功！'}");
	}

	/**
	 * 各部门指标计划上报
	 */
	public void partItemPlanReport() {
		String mainId = request.getParameter("depmainid");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("flowCode");
		String actionId = request.getParameter("actionId");
		remote.partItemPlanReport(Long.parseLong(mainId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, workflowType);

		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		BpJItemplanDepMain model = remoteA.findPartObject(Long
				.parseLong(mainId));
		if (nextRoles == null || nextRoles.equals("")) {
			nextRoles = postMsg.getFistStepRoles("bqPartItemPlan", actionId,
					null, null);
		}
		if (nextRoles != null && !nextRoles.equals("")) {
			String monthString = model.getMonth().toString();
			String monthStringA = monthString.substring(0,
					monthString.length() - 3);
			String msg = "专业经济指标已上报，等待您的审批，请您及时处理。计划时间：" + monthStringA + "。";
			postMsg.sendMsg(nextRoles, msg);
		}
		// -----------add end--------------------------------------

		write("{success:true,msg:'上报成功！'}");
	}

	/**
	 * 各部门指标计划审批
	 */
	public void partItemPlanApprove() {
		String mainId = request.getParameter("depmainid");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");

		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		String thisRoleString = nextRoles;
		BpJItemplanDepMain model = remoteA.findPartObject(Long
				.parseLong(mainId));
		if (!actionId.equals("42") && !actionId.equals("62")
				&& !actionId.equals("63")) {
			if (nextRoles == null || "".equals(nextRoles)) {
				thisRoleString = postMsg.getNextSetpRoles(entryId, actionId);
			}
		}
		// -----------add end-------------------------------------

		remote.partItemPlanApprove(Long.parseLong(mainId), Long
				.parseLong(actionId), Long.parseLong(entryId), workerCode,
				approveText, nextRoles);

		// -----------add by ltong 20100609 短信通知----------------
		if (!actionId.equals("42") && !actionId.equals("62")) {
			if (thisRoleString != null && !thisRoleString.equals("")) {
				String monthString = model.getMonth().toString();
				String monthStringA = monthString.substring(0, monthString
						.length() - 3);
				String msg = "专业经济指标等待您的审批，请您及时处理。计划时间：" + monthStringA + "。";
				postMsg.sendMsg(thisRoleString, msg);
			}
		}
		// -----------add end-------------------------------------

		write("{success:true,msg:'审批成功！'}");
	}

	/**
	 * 全厂指标计划完成情况上报
	 */
	public void wholeItemFactReport() {
		/**
		 * planDate 计划完成时间 100609 bjxu
		 */
		String planDate = request.getParameter("planDate");
		
		String mainId = request.getParameter("planmainid");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("flowCode");
		String actionId = request.getParameter("actionId");
		remote.wholeItemFactReport(Long.parseLong(mainId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, workflowType);
		/**
		 * 短信通知 bjxu
		 */
			
		PostMessage postMsg = new PostMessage();
		String thisRoleString = (nextRoles == null || "".equals(nextRoles)) ? postMsg
				.getFistStepRoles(workflowType, actionId, null, null)
				: nextRoles;
		String msg = planDate + "全厂指标完成情况已上报，请您及时处理。";
		postMsg.sendMsg(thisRoleString, msg);
		write("{success:true,msg:'上报成功！'}");
	}

	/**
	 * 全厂指标计划完成情况审批
	 */
	public void wholeItemFactApprove() {
		String mainId = request.getParameter("planmainid");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		remote.wholeItemFactApprove(Long.parseLong(mainId), Long
				.parseLong(actionId), Long.parseLong(entryId), workerCode,
				approveText, nextRoles);
		write("{success:true,msg:'审批成功！'}");
	}

	/**
	 * 各部门指标计划完成情况上报
	 */
	public void partItemFactReport() {
		String mainId = request.getParameter("depmainid");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("flowCode");
		String actionId = request.getParameter("actionId");
		remote.partItemFactReport(Long.parseLong(mainId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, workflowType);

		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		BpJItemplanDepMain model = remoteA.findPartObject(Long
				.parseLong(mainId));
		if (nextRoles == null || nextRoles.equals("")) {
			nextRoles = postMsg.getFistStepRoles("bqPartItemFact", actionId,
					null, null);
		}
		if (nextRoles != null && !nextRoles.equals("")) {
			String monthString = model.getMonth().toString();
			String monthStringA = monthString.substring(0,
					monthString.length() - 3);
			String msg = "专业经济指标完成情况已上报，等待您的审批，请您及时处理。计划时间：" + monthStringA
					+ "。";
			postMsg.sendMsg(nextRoles, msg);
		}
		// -----------add end--------------------------------------

		write("{success:true,msg:'上报成功！'}");
	}

	/**
	 * 各部门指标计划完成情况审批
	 */
	public void partItemFactApprove() {
		String mainId = request.getParameter("depmainid");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");

		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		String thisRoleString = nextRoles;
		BpJItemplanDepMain model = remoteA.findPartObject(Long
				.parseLong(mainId));
		if (!actionId.equals("42") && !actionId.equals("52")
				&& !actionId.equals("53")) {
			if (nextRoles == null || "".equals(nextRoles)) {
				thisRoleString = postMsg.getNextSetpRoles(entryId, actionId);
			}
		}
		// -----------add end-------------------------------------

		remote.partItemFactApprove(Long.parseLong(mainId), Long
				.parseLong(actionId), Long.parseLong(entryId), workerCode,
				approveText, nextRoles);

		// -----------add by ltong 20100609 短信通知----------------
		if (!actionId.equals("42") && !actionId.equals("52")) {
			if (thisRoleString != null && !thisRoleString.equals("")) {
				String monthString = model.getMonth().toString();
				String monthStringA = monthString.substring(0, monthString
						.length() - 3);
				String msg = "专业经济指标完成情况等待您的审批，请您及时处理。计划时间：" + monthStringA
						+ "。";
				postMsg.sendMsg(thisRoleString, msg);
			}
		}
		// -----------add end-------------------------------------

		write("{success:true,msg:'审批成功！'}");
	}

	/**
	 * 月度技术指标上报
	 */
	public void fillTecItemReport() {
		String tecMainId = request.getParameter("tecMainId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("workflowType");
		String actionId = request.getParameter("actionId");
		remote.fillTecItemReport(Long.parseLong(tecMainId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, workflowType);
		write("{success:true,msg:'上报成功！'}");

	}

	/**
	 * 月度技术指标审批
	 */
	public void fillTecItemApprove() {
		String tecMainId = request.getParameter("tecMainId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		remote.fillTecItemApprove(Long.parseLong(tecMainId), Long
				.parseLong(actionId), Long.parseLong(entryId), workerCode,
				approveText, nextRoles);
		write("{success:true,msg:'审批成功！'}");
	}

	/**
	 * 月度技术指标完成情况上报
	 */
	public void TecItemFinishReport() {
		String tecMainId = request.getParameter("tecMainId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("workflowType");
		String actionId = request.getParameter("actionId");
		remote.TecItemFinishReport(Long.parseLong(tecMainId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, workflowType);
		write("{success:true,msg:'上报成功！'}");
	}

	/**
	 * 月度技术指标完成情况审批
	 */
	public void TecItemFinishApprove() {
		String tecMainId = request.getParameter("tecMainId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		remote.TecItemFinishApprove(Long.parseLong(tecMainId), Long
				.parseLong(actionId), Long.parseLong(entryId), workerCode,
				approveText, nextRoles);
		write("{success:true,msg:'审批成功！'}");
	}
}
