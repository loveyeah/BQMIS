package power.web.manage.plan.trainplan;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.manage.plan.maintWeekPlan.BpJPlanRepairGather;
import power.ejb.manage.plan.trainplan.BpJTrainingGather;
import power.ejb.manage.plan.trainplan.BpJTrainingMain;
import power.ejb.manage.plan.trainplan.TrainPlanManager;
import power.ejb.manage.plan.trainplan.TrainPlanSignManager;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

@SuppressWarnings("serial")
public class TrainPlanSignAction extends AbstractAction {

	private TrainPlanSignManager signRemote;
	protected TrainPlanManager planMain;

	public TrainPlanSignAction() {
		signRemote = (TrainPlanSignManager) factory
				.getFacadeRemote("TrainPlanSignManagerImpl");
		planMain = (TrainPlanManager) Ejb3Factory.getInstance()
				.getFacadeRemote("TrainPlanManagerImpl");
	}

	// 申请上报
	public void TrainPlanReport() {
		String mainId = request.getParameter("mainId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("workflowType");
		String actionId = request.getParameter("actionId");
		signRemote.TrainPlanReport(Long.parseLong(mainId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, workflowType);

		// -----------modify by fyyang 20100701 短信通知----------------
		PostMessage postMsg = new PostMessage();
		BpJTrainingMain model = planMain.findById(Long.parseLong(mainId));

		
		if (nextRoles != null && !nextRoles.equals("")) {
			String planTime = model.getTrainingMonth().toString();
			String planTime1 = planTime.substring(0, planTime.length() - 3);
			String msg = "部门培训计划已上报，等待您的审批，请您及时处理。计划时间：" + planTime1 + "。";
			postMsg.sendMsgByWorker(nextRoles, msg);
		}
		// -----------add end--------------------------------------

		write("{success:true,msg:'上报成功！'}");

	}

	// 汇总上报
	public void TrainPlanGatherReport() {
		String gatherId = request.getParameter("gatherId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("workflowType");
		String actionId = request.getParameter("actionId");
		signRemote.TrainPlanGatherReport(Long.parseLong(gatherId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, workflowType);

		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		BpJTrainingGather model = planMain.findByGatherId(Long
				.parseLong(gatherId));
		if (nextRoles == null || nextRoles.equals("")) {
			nextRoles = postMsg.getFistStepRoles("bpTrainPlanGather", actionId,
					null, null);
		}
		
	
		if (nextRoles != null && !nextRoles.equals("")) {
			String planTime = model.getTrainingMonth().toString();
			String planTime1 = planTime.substring(0, planTime.length() - 3);
			String msg = "部门培训计划汇总已上报，等待您的审批，请您及时处理。计划时间：" + planTime1 + "。";
			postMsg.sendMsg(nextRoles, msg);
		}
		// -----------add end--------------------------------------
		write("{success:true,msg:'上报成功！'}");

	}

	// 申请审批
	public void TrainPlanApprove() {
		String mainId = request.getParameter("mainId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");

		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		String thisRoleString = nextRoles;
		BpJTrainingMain model = planMain.findById(Long.parseLong(mainId));
		if (!actionId.equals("52") && !actionId.equals("45")
				&& !actionId.equals("43")) {
			if (nextRoles == null || "".equals(nextRoles)) {
				thisRoleString = postMsg.getNextSetpRoles(entryId, actionId);
			}
		}
		// -----------add end-------------------------------------

		signRemote.TrainPlanApprove(Long.parseLong(mainId), Long
				.parseLong(actionId), Long.parseLong(entryId), workerCode,
				approveText, nextRoles);
		
		// -----------add by ltong 20100609 短信通知----------------
		if (!actionId.equals("52") && !actionId.equals("45")) {
			if (thisRoleString != null && !thisRoleString.equals("")) {
				String planTime = model.getTrainingMonth().toString();
				String planTime1 = planTime.substring(0, planTime.length() - 3);
				String msg = "部门培训计划等待您的审批，请您及时处理。计划时间：" + planTime1 + "。";
				postMsg.sendMsg(thisRoleString, msg);
			}
		}
		// -----------add end-------------------------------------

		write("{success:true,msg:'审批成功！'}");
	}

	// 汇总审批
	public void TrainPlanGatherApprove() {
		String gatherId = request.getParameter("gatherId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");

		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		String thisRoleString = nextRoles;
		BpJTrainingGather model = planMain.findByGatherId(Long
				.parseLong(gatherId));
		if (!actionId.equals("52") && !actionId.equals("42")
				&& !actionId.equals("53")) {
			if (nextRoles == null || "".equals(nextRoles)) {
				thisRoleString = postMsg.getNextSetpRoles(entryId, actionId);
			}
		}
		// -----------add end-------------------------------------

		signRemote.TrainPlanGatherApprove(Long.parseLong(gatherId), Long
				.parseLong(actionId), Long.parseLong(entryId), workerCode,
				approveText, nextRoles);

		// -----------add by ltong 20100609 短信通知----------------
		if (!actionId.equals("52") && !actionId.equals("42")) {
			if (thisRoleString != null && !thisRoleString.equals("")) {
				String planTime = model.getTrainingMonth().toString();
				String planTime1 = planTime.substring(0, planTime.length() - 3);
				String msg = "部门培训计划汇总等待您的审批，请您及时处理。计划时间：" + planTime1 + "。";
				postMsg.sendMsg(thisRoleString, msg);
			}
		}
		// -----------add end-------------------------------------

		write("{success:true,msg:'审批成功！'}");
	}

	/**
	 * 培训计划回埴上报 add by drdu 091211
	 */
	public void TrainPlanBackReport() {
		
		/**
		 * planDate培训计划时间 100609 bjxu
		 */
		String planDate = request.getParameter("planDate");
		
		String mainId = request.getParameter("mainId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("flowCode");
		String actionId = request.getParameter("actionId");
		signRemote.TrainPlanBackReport(Long.parseLong(mainId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, workflowType);
		/**
		 * 短信通知 bjxu
		 */
		if(nextRoles!=null&&!nextRoles.equals(""))
		{
		PostMessage postMsg = new PostMessage();
		String msg = planDate + "部门培训计划回填已上报，请您及时处理。";
		postMsg.sendMsgByWorker(nextRoles, msg);
		}
		write("{success:true,msg:'上报成功！'}");

	}

	/**
	 * 培训计划回填审批 add by drdu 091211
	 */
	public void TrainPlanBackApprove() {
		/**
		 * planDate 计划时间 100609 bjxu
		 */
		String planDate = request.getParameter("planDate");
		
		String mainId = request.getParameter("mainId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		signRemote.TrainPlanBackApprove(Long.parseLong(mainId), Long
				.parseLong(actionId), Long.parseLong(entryId), workerCode,
				approveText, nextRoles);
		
		if("54".equals(actionId)){
			PostMessage postMsg = new PostMessage();
			String msg = planDate + "部门培训计划回填等待您的审批，请您及时处理。";
			postMsg.sendMsg(nextRoles, msg);
		}
		write("{success:true,msg:'审批成功！'}");
	}

	/**
	 * 培训计划回填汇总上报 add by drdu 091215
	 */
	public void TrainPlanBackGatherReport() {
		String approveId = request.getParameter("approveId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("flowCode");
		String actionId = request.getParameter("actionId");
		String planTime = request.getParameter("planTime");// add by ywliu 20100610
		signRemote.TrainPlanBackGatherReport(Long.parseLong(approveId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, workflowType);
		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		if (nextRoles == null || nextRoles.equals("")) {
			nextRoles = postMsg.getFistStepRoles(workflowType, actionId,
					null, null);
		}
		if (nextRoles != null && !nextRoles.equals("")) {
			String msg = "部门培训计划回填汇总已上报，等待您的审批，请您及时处理。计划时间：" + planTime + "。";
			postMsg.sendMsg(nextRoles, msg);
		}
		// -----------add end--------------------------------------
		write("{success:true,msg:'上报成功！'}");
	}

	/**
	 * 培训计划回填汇总审批 add by drdu 091215
	 */
	public void TrainPlanBackGatherApprove() {
		String approvalId = request.getParameter("approveId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String planTime = request.getParameter("planTime");// add by ywliu 20100610
		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		String thisRoleString = nextRoles;
		if ("45".equals(actionId)) {
			if (nextRoles == null || "".equals(nextRoles)) {
				thisRoleString = postMsg.getNextSetpRoles(entryId, actionId);
			}
		}
		// -----------add end-------------------------------------
		signRemote.trainPlanBackGatherApprove(Long.parseLong(approvalId), Long
				.parseLong(actionId), Long.parseLong(entryId), workerCode,
				approveText, nextRoles);
		// -----------add by ltong 20100609 短信通知----------------
		if ("45".equals(actionId)) {
			if (thisRoleString != null && !thisRoleString.equals("")) {
				String msg = "部门培训计划回填汇总等待您的审批，请您及时处理。计划时间：" + planTime + "。";
				postMsg.sendMsg(thisRoleString, msg);
			}
		}
		// -----------add end-------------------------------------
		write("{success:true,msg:'审批成功！'}");
	}
}
