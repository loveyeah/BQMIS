package power.web.manage.plan.trainplan;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJBalance;
import power.ejb.manage.contract.business.ConJContractInfo;
import power.ejb.manage.plan.BpJPlanRepairDep;
import power.ejb.manage.plan.BpJPlanRepairDepFacadeRemote;
import power.ejb.manage.plan.BpJPlanRepairDetail;
import power.ejb.manage.plan.BpJPlanRepairDetailFacadeRemote;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

@SuppressWarnings("serial")
public class registerAction extends AbstractAction {
	private BpJPlanRepairDetailFacadeRemote remoteA;
	private BpJPlanRepairDepFacadeRemote remoteB;

	public registerAction() {
		remoteA = (BpJPlanRepairDetailFacadeRemote) factory
				.getFacadeRemote("BpJPlanRepairDetailFacade");
		remoteB = (BpJPlanRepairDepFacadeRemote) factory
				.getFacadeRemote("BpJPlanRepairDepFacade");
	}

	public void repairReport() {
		String detailId = request.getParameter("depmainid");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		// System.out.println("the next role is"+nextRoles);
		String flowCode = request.getParameter("flowCode");
		String actionId = request.getParameter("actionId");
		remoteA.repairReport(Long.parseLong(detailId),
				Long.parseLong(actionId), employee.getWorkerCode(),
				approveText, nextRoles, flowCode);

		// -----------add by ltong 20100609 短信通知----------------
		PostMessage postMsg = new PostMessage();
		BpJPlanRepairDep model = remoteB.findById(Long.parseLong(detailId));
		if (nextRoles == null || nextRoles.equals("")) {
			nextRoles = postMsg.getFistStepRoles("bqRepair", actionId, null,
					null);
		}
		if (nextRoles != null && !nextRoles.equals("")) {
			String planTime = model.getPlanTime();
			String planTime1 = planTime.substring(0, planTime.length() - 1);
			String planTime2 = planTime.substring(planTime.length() - 1);
			String msg = "检修周计划已上报，等待您的审批，请您及时处理。计划时间：" + planTime1 + "，第"
					+ planTime2 + "周。";
			postMsg.sendMsg(nextRoles, msg);
		}
		// -----------add end--------------------------------------

		write("{success:true,msg:'上报成功！'}");

	}

	public void repairApprove() {

		String mainId = request.getParameter("deptID");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		System.out.println("the mainId" + mainId);
		String[] deptIds = mainId.split(",");
		String[] workNO = entryId.split(",");

		for (int i = 0; i < deptIds.length; i++) {
			remoteA.repairApprove(Long.parseLong(deptIds[i]), Long
					.parseLong(actionId), Long.parseLong(workNO[i]),
					workerCode, approveText, nextRoles);

		}
		write("{success:true,msg:'审批成功！'}");
	}

	public void saveRepair() throws JSONException, ParseException,
			java.text.ParseException {

		String approve = request.getParameter("approve");
		String planDate = request.getParameter("planDate");
		String planDeptCode = request.getParameter("planDeptCode");
		String weekStart = request.getParameter("weekStart");
		String weekEnd = request.getParameter("weekEnd");
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<BpJPlanRepairDetail> addList = null;
		List<BpJPlanRepairDetail> updateList = null;
		if (list != null && list.size() > 0) {
			addList = new ArrayList<BpJPlanRepairDetail>();
			updateList = new ArrayList<BpJPlanRepairDetail>();
			for (Map data : list) {
				String planDetailId = null;
				String planDepId = null;
				String content = null;
				String chargeDep = null;
				String assortDep = null;
				String beginTime = null;
				String editBy = null;
				String editDate = null;
				String endTime = null;
				long days = 0;
				String memo = null;

				if (data.get("planDetailId") != null)
					planDetailId = data.get("planDetailId").toString();
				if (data.get("planDepId") != null)
					planDepId = data.get("planDepId").toString();

				if (data.get("content") != null)
					content = data.get("content").toString();
				if (data.get("chargeDep") != null)
					chargeDep = data.get("chargeDep").toString();
				if (data.get("assortDep") != null)
					assortDep = data.get("assortDep").toString();
				if (data.get("editBy") != null)
					editBy = data.get("editBy").toString();
				if (data.get("editDate") != null)
					editDate = data.get("editDate").toString();
				if (data.get("beginTime") != null)
					beginTime = data.get("beginTime").toString();
				if (data.get("endTime") != null)
					endTime = data.get("endTime").toString();

				if (data.get("memo") != null)
					memo = data.get("memo").toString();

				BpJPlanRepairDetail model = new BpJPlanRepairDetail();
				if (planDetailId == null) {
					model.setContent(content);
					if (chargeDep != null && !chargeDep.equals(""))
						model.setChargeDep(chargeDep);
					if (assortDep != null)
						model.setAssortDep(assortDep);

					if (memo != null)
						model.setMemo(memo);
					if (beginTime != null && !beginTime.equals("")) {
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						model.setBeginTime(sdf.parse(beginTime));

					}
					if (endTime != null && !endTime.equals("")) {
						DateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						model.setEndTime(sf.parse(endTime));

					}

					if (beginTime != null && endTime != null
							&& !("").equals(beginTime) && !("").equals(endTime)) {

						SimpleDateFormat dsf = new SimpleDateFormat(
								"yyyy-MM-dd");
						long day = 0;
						try {
							java.util.Date date = dsf.parse(beginTime);
							java.util.Date mydate = dsf.parse(endTime);
							day = (mydate.getTime() - date.getTime())
									/ (24 * 60 * 60 * 1000);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("相差的天数:" + day);
						days = day + 1;
						model.setDays(days);
					}

					model.setEnterpriseCode(employee.getEnterpriseCode());
					addList.add(model);
				} else {
					model = remoteA.findById(Long.parseLong(planDetailId));
					if (content != null && !content.equals(""))
						model.setContent(content);
					if (chargeDep != null && !chargeDep.equals(""))
						model.setChargeDep(chargeDep);
					if (assortDep != null)
						model.setAssortDep(assortDep);
					if (memo != null)
						model.setMemo(memo);
					if (beginTime != null && !beginTime.equals("")) {
						DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
						model.setBeginTime(sdf.parse(beginTime));

					}
					if (endTime != null && !endTime.equals("")) {
						DateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
						model.setEndTime(sf.parse(endTime));

					}
					if (beginTime != null && endTime != null
							&& !("").equals(beginTime) && !("").equals(endTime)) {
						SimpleDateFormat dsf = new SimpleDateFormat(
								"yyyy-MM-dd");
						long day = 0;
						try {
							java.util.Date date = dsf.parse(beginTime);
							java.util.Date mydate = dsf.parse(endTime);
							day = (mydate.getTime() - date.getTime())
									/ (24 * 60 * 60 * 1000);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						// System.out.println("相差的天数:" + day);
						days = day + 1;
						model.setDays(days);
					}
					model.setEnterpriseCode(employee.getEnterpriseCode());
					updateList.add(model);
				}
			}
		}
		if (approve != null && approve.equals("approve")) {
			remoteA.saveRepairList("approve", addList, updateList, planDate,
					planDeptCode, weekStart, weekEnd, employee.getWorkerCode(),
					employee.getEnterpriseCode());
			write("{success:true,msg:'操作成功！'}");
		} else {
			remoteA.saveRepairList(null, addList, updateList, planDate,
					planDeptCode, weekStart, weekEnd, employee.getWorkerCode(),
					employee.getEnterpriseCode());
			write("{success:true,msg:'操作成功！'}");

		}
	}

	public void deleteRepair() {
		String ids = request.getParameter("ids");

		remoteA.delRepairDetail(ids);

	}

	public void getAllRepair() throws JSONException, java.text.ParseException {
		String planTime = request.getParameter("planTime");
		String status = request.getParameter("status");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String approve = request.getParameter("approve");

		String entryIds = "";
		if (approve != null && approve.equals("approve")) {
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqRepair" }, employee.getWorkerCode());
		}

		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = remoteA.findRepair(approve, entryIds, planTime, employee
				.getWorkerCode(), employee.getEnterpriseCode(), status);
		if (obj != null) {

			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list :[]}");
		}

	}

	public void getRepairDeptObj() throws Exception {

		String planTime = request.getParameter("planTime");
		String deptCode = employee.getDeptCode();
		PageObject obj = new PageObject();
		obj = remoteA.getRepairDept(planTime, deptCode);

		List lists = null;

		write(JSONUtil.serialize(obj));
	}

	public void findAlldept() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String Time = request.getParameter("planTime");
		if (start != null && limit != null) {
			PageObject result = remoteB.findAlldept(Time, Integer
					.parseInt(start), Integer.parseInt(limit));
			write(JSONUtil.serialize(result));
		} else {
			PageObject result = remoteB.findAlldept(Time);
			write(JSONUtil.serialize(result));
		}

	}

	public void getDetailByDept() throws JSONException {
		String deptId = request.getParameter("deptId");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if (start != null && limit != null) {
			PageObject result = remoteA.getAllDetail(deptId, Integer
					.parseInt(start), Integer.parseInt(limit));
			write(JSONUtil.serialize(result));
		} else {
			PageObject result = remoteA.getAllDetail(deptId);
			write(JSONUtil.serialize(result));

		}

	}

	/**
	 * 查询审批中最大月份下最大周的数据 add by sychen 20100319
	 */
	public void getRepairApproveInfo() {
		String str = remoteB.getRepairApproveList(employee.getEnterpriseCode());
		write(str);
	}

	/**
	 * 判断审批页面时间下主表Id是否存在 add by sychen 20100320
	 */
	public void getRepairApproveDeptId() {

		String planTime = request.getParameter("planTime");
		String planWeek = request.getParameter("planWeek");
		String str = remoteB.getRepairApproveDeptId(planTime, planWeek,
				employee.getEnterpriseCode(), employee.getWorkerCode());
		write(str);
	}

}
