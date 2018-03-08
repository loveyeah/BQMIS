package power.web.manage.plan.maintWeekPlan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.workbill.EquJWo;
import power.ejb.equ.workbill.EquJWoFacadeRemote;
import power.ejb.manage.contract.business.ConJContractInfo;
import power.ejb.manage.plan.BpJPlanRepairDep;
import power.ejb.manage.plan.maintWeekPlan.BpJPlanRepairGather;
import power.ejb.manage.plan.maintWeekPlan.BpJPlanRepairGatherDetail;
import power.ejb.manage.plan.maintWeekPlan.MaintWeekPlanGatherManager;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;
import power.web.comm.PostMessage;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

@SuppressWarnings("serial")
public class MaintWeekPlanGatherAction extends AbstractAction {

	private BpJPlanRepairGatherDetail repairDetail;
	private BpJPlanRepairGather repairMain;
	private MaintWeekPlanGatherManager remote;
	private EquJWoFacadeRemote equJWoRemote;

	public MaintWeekPlanGatherAction() {
		remote = (MaintWeekPlanGatherManager) factory
				.getFacadeRemote("MaintWeekPlanGatherManagerImpl");
		//add by mlian 20100928
		equJWoRemote = (EquJWoFacadeRemote) factory.getFacadeRemote("EquJWoFacade");
	}

	/**
	 * 新增修改
	 * 
	 * @throws ParseException
	 * @throws CodeRepeatException
	 */
	@SuppressWarnings("unchecked")
	public void addRepairGather() throws JSONException, ParseException,
			CodeRepeatException {
		String str = request.getParameter("isUpdate");
		String deleteId = request.getParameter("isDelete");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<BpJPlanRepairGatherDetail> addList = new ArrayList<BpJPlanRepairGatherDetail>();
		List<BpJPlanRepairGatherDetail> updateList = new ArrayList<BpJPlanRepairGatherDetail>();

		String planTime = request.getParameter("planDate");
		String planWeek = request.getParameter("planWeek");
		String gatherDept = request.getParameter("gatherDept");
		String weekStartTime = request.getParameter("weekStartTime");
		String weekEndTime = request.getParameter("weekEndTime");
		String totalMemo = request.getParameter("totalMemo");
		try {
			for (Map data : list) {
				String planDetailId = null;
				String content = null;
				String chargeDep = null;
				String assortDep = null;
				String beginTime = null;
				String endTime = null;
				String days = null;
				String memo = null;
				if (data.get("planDetailId") != null)
					planDetailId = data.get("planDetailId").toString();
				if (data.get("content") != null)
					content = data.get("content").toString();
				if (data.get("chargeDep") != null)
					chargeDep = data.get("chargeDep").toString();
				if (data.get("assortDep") != null)
					assortDep = data.get("assortDep").toString();
				if (data.get("beginTime") != null)
					beginTime = data.get("beginTime").toString();
				if (data.get("endTime") != null)
					endTime = data.get("endTime").toString();
				if (data.get("days") != null)
					days = data.get("days").toString();
				if (data.get("memo") != null)
					memo = data.get("memo").toString();

				BpJPlanRepairGatherDetail model = new BpJPlanRepairGatherDetail();
				SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				if (planDetailId == null) {
					model.setContent(content);
					if (chargeDep != null && !chargeDep.equals(""))
						model.setChargeDep(chargeDep);
					if (assortDep != null)
						model.setAssortDep(assortDep);
					else
						model.setAssortDep(null);
					if (beginTime != null && !beginTime.equals(""))
						model.setBeginTime(df.parse(beginTime));
					if (endTime != null && !beginTime.equals(""))
						model.setEndTime(df.parse(endTime));
					if (days != null && !days.equals(""))
						model.setDays(Long.parseLong(days));
					model.setMemo(memo);
					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);
				} else {
					model = remote.findByGatherDetailId(Long
							.parseLong(planDetailId));
					model.setPlanDetailId(Long.parseLong(planDetailId));
					model.setGatherId(model.getGatherId());
					model.setContent(content);
					if (chargeDep != null && !chargeDep.equals(""))
						model.setChargeDep(chargeDep);
					if (assortDep != null)
						model.setAssortDep(assortDep);
					else
						model.setAssortDep(null);
					if (beginTime != null && !beginTime.equals(""))
						model.setBeginTime(df.parse(beginTime));
					if (endTime != null && !beginTime.equals(""))
						model.setEndTime(df.parse(endTime));
					if (days != null && !days.equals(""))
						model.setDays(Long.parseLong(days));
					model.setMemo(memo);
					model.setEnterpriseCode(employee.getEnterpriseCode());
					updateList.add(model);
				}
			}
			if (addList.size() > 0 || updateList.size() > 0
					|| deleteId.length() > 0)
				remote.save(addList, updateList, deleteId, planTime, planWeek,
						gatherDept, weekStartTime, weekEndTime, totalMemo,
						employee.getWorkerCode(), employee.getEnterpriseCode());
			write("{success:true,msg:'操作成功！'}");
		} catch (CodeRepeatException e) {
			String out = "{success:true,msg :'" + e.getMessage() + "'}";
			write(out);
		}
	}

	/**
	 * 删除
	 */
	public void delRepairGatherDetail() {
		String planDetailId = request.getParameter("ids");
		if (planDetailId != null && !planDetailId.trim().equals("")) {
			if (remote.delRepairGatherDetail(planDetailId) != null)
				write("{success:true}");
			else
				write("{success:false}");
		}
	}

	public void getRepairGatherDetailList() throws Exception {

		String planTime = request.getParameter("planTime");
		String planWeek = request.getParameter("planWeek");
		String queryKey = request.getParameter("queryKey");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// 判断审批
		String approve = request.getParameter("approve");

		String entryIds = "";
		if (approve != null && approve.equals("Y")) {
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqMaintWeekPlanGather" }, employee
							.getWorkerCode());
		}

		PageObject pg = null;
		if (start != null && limit != null)
			pg = remote.findRepairGatherList(approve, entryIds, planTime,
					planWeek, queryKey, employee.getEnterpriseCode(), Integer
							.parseInt(start), Integer.parseInt(limit));
		else
			pg = remote.findRepairGatherList(approve, entryIds, planTime,
					planWeek, queryKey, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));

	}

	// 汇总上报
	public void RepairGatherReport() {
		String gatherId = request.getParameter("gatherId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String workflowType = request.getParameter("workflowType");
		String actionId = request.getParameter("actionId");
		remote.RepairGatherReport(Long.parseLong(gatherId), Long
				.parseLong(actionId), employee.getWorkerCode(), approveText,
				nextRoles, workflowType);

		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		BpJPlanRepairGather model = remote.findByGatherId(Long
				.parseLong(gatherId));
		if (nextRoles == null || nextRoles.equals("")) {
			nextRoles = postMsg.getFistStepRoles("bqMaintWeekPlanGather",
					actionId, null, null);
		}
		if (nextRoles != null && !nextRoles.equals("")) {
			String planTime = model.getPlanTime();
			String planTime1 = planTime.substring(0, planTime.length() - 1);
			String planTime2 = planTime.substring(planTime.length() - 1);
			String msg = "检修周计划汇总已上报，等待您的审批，请您及时处理。计划时间：" + planTime1 + "，第"
					+ planTime2 + "周。";
			postMsg.sendMsg(nextRoles, msg);
		}
		// -----------add end--------------------------------------

		write("{success:true,msg:'上报成功！'}");

	}

	// 汇总审批
	public void RepairGatherApprove() throws CodeRepeatException {
		String gatherId = request.getParameter("gatherId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");

		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		String thisRoleString = nextRoles;
		BpJPlanRepairGather model = remote.findByGatherId(Long
				.parseLong(gatherId));
		if (!actionId.equals("42") && !actionId.equals("122")
				&& !actionId.equals("132") && !actionId.equals("52")
				&& !actionId.equals("133") && !actionId.equals("53")) {
			if (nextRoles == null || "".equals(nextRoles)) {
				thisRoleString = postMsg.getNextSetpRoles(entryId, actionId);
			}
		}
		// -----------add end-------------------------------------
		//-----------add by mlian 20100928 生成工单----------------
		if (actionId.equals("133") || actionId.equals("53")){
			EquJWo equJWo = new EquJWo();
			BpJPlanRepairGather bpJPlanRepairGather = remote.findByGatherId(Long
					.parseLong(gatherId));
			List<BpJPlanRepairGatherDetail> detailList = remote.findDetailByGatherId(Long.parseLong(gatherId));
			for (BpJPlanRepairGatherDetail bpJPlanRepairGatherDetail : detailList) {
				equJWo.setWorkorderContent(bpJPlanRepairGatherDetail.getContent());
				equJWo.setWorkorderType("J");
				equJWo.setRepairDepartment(bpJPlanRepairGatherDetail.getChargeDep());
				equJWo.setRequireStarttime(bpJPlanRepairGatherDetail.getBeginTime());
				equJWo.setRequireEndtime(bpJPlanRepairGatherDetail.getEndTime());	
				equJWo.setEnterprisecode("hfdc");
				equJWo.setRequireManCode(employee.getWorkerCode());
				equJWo.setWorkorderStatus("0");
				equJWo.setWfState(0l);
				equJWoRemote.save(equJWo, null, null);
			}
		}
		// -----------add end-------------------------------------
		remote.RepairGatherApprove(Long.parseLong(gatherId), Long
				.parseLong(actionId), Long.parseLong(entryId), workerCode,
				approveText, nextRoles);

		// -----------add by ltong 20100609 短信通知----------------
		if (!actionId.equals("42") && !actionId.equals("122")
				&& !actionId.equals("132") && !actionId.equals("52")) {
			if (thisRoleString != null && !thisRoleString.equals("")) {
				String planTime = model.getPlanTime();
				String planTime1 = planTime.substring(0, planTime.length() - 1);
				String planTime2 = planTime.substring(planTime.length() - 1);
				String msg = "检修周计划汇总等待您的审批，请您及时处理。计划时间：" + planTime1 + "，第"
						+ planTime2 + "周。";
				postMsg.sendMsg(thisRoleString, msg);
			}
		}
		// -----------add end-------------------------------------

		write("{success:true,msg:'审批成功！'}");
	}

	public void getRepairApproveList() throws Exception {

		String planTime = request.getParameter("planTime");
		String planWeek = request.getParameter("planWeek");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// 判断审批
		String status = request.getParameter("status");

		String entryIds = "";

		PageObject pg = null;
		if (start != null && limit != null)
			pg = remote.getRepairApproveList(status, entryIds, planTime,
					planWeek, employee.getEnterpriseCode(), Integer
							.parseInt(start), Integer.parseInt(limit));
		else
			pg = remote.getRepairApproveList(status, entryIds, planTime,
					planWeek, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));

	}

	public void getRepairGatherInfo() {
		String str = remote.getRepairGatherApproveList(employee
				.getEnterpriseCode());
		write(str);
	}

	public void getRepairApproveGatherId() {
		String planTime = request.getParameter("planTime");
		String planWeek = request.getParameter("planWeek");
		String str = remote.getRepairApproveGatherId(planTime, planWeek,
				employee.getEnterpriseCode());
		write(str);
	}

	public BpJPlanRepairGatherDetail getRepairDetail() {
		return repairDetail;
	}

	public void setRepairDetail(BpJPlanRepairGatherDetail repairDetail) {
		this.repairDetail = repairDetail;
	}

	public BpJPlanRepairGather getRepairMain() {
		return repairMain;
	}

	public void setRepairMain(BpJPlanRepairGather repairMain) {
		this.repairMain = repairMain;
	}

}
