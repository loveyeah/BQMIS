package power.web.manage.contract.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.contract.business.ConJBalance;
import power.ejb.manage.contract.business.ConJBalanceFacadeRemote;
import power.ejb.manage.contract.business.ConJContractInfo;
import power.ejb.manage.contract.business.ConJContractInfoFacadeRemote;
import power.ejb.manage.contract.business.ContractApprove;
import power.web.comm.AbstractAction;
import power.web.comm.PostMessage;

@SuppressWarnings("serial")
public class BqConBalaceAction extends AbstractAction {

	private ConJBalanceFacadeRemote balremote;
	private ContractApprove appremote;;

	public BqConBalaceAction() {
		balremote = (ConJBalanceFacadeRemote) factory
				.getFacadeRemote("ConJBalanceFacade");
		appremote = (ContractApprove) factory
				.getFacadeRemote("ContractApproveImp");
	}

	/**
	 * add by liuyi 091120 采购合同结算保存 灞桥用
	 */
	public void bqAddorUpdateBalance() throws JSONException {
		String appId = request.getParameter("appId");
		Long appIdFlag = null;
		if (appId != null && !appId.equals("")) {
			appIdFlag = Long.parseLong(appId);
		}
		String entryBy = request.getParameter("entryBy");
		String operateDepCode = request.getParameter("operateDepCode");
		String entryDateString = request.getParameter("entryDateString");
		String applicatDateString = request.getParameter("applicatDateString");
		String moneyUnitStr = request.getParameter("moneyUnit");
		Long moneyUnit = 1L;
		if (moneyUnitStr != null && !moneyUnitStr.equals("")) {
			moneyUnit = Long.parseLong(moneyUnitStr);
		}
		String add = request.getParameter("add");
		String update = request.getParameter("update");
		String ids = request.getParameter("ids");

		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM");
		Date entryDate = new Date();
		if (entryDateString != null && !entryDateString.equals("")) {
			try {
				entryDate = sdf1.parse(entryDateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		Date applicatDate = new Date();
		if (applicatDateString != null && !applicatDateString.equals("")) {
			try {
				applicatDate = sdf2.parse(applicatDateString);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Object addObj = JSONUtil.deserialize(add);
		Object updateObj = JSONUtil.deserialize(update);
		List<Map> list1 = (List<Map>) addObj;
		List<Map> list2 = (List<Map>) updateObj;

		List<ConJBalance> addList = new ArrayList<ConJBalance>();
		List<ConJBalance> updateList = new ArrayList<ConJBalance>();

		for (Map map : list1) {
			ConJBalance temp = this.parseConJBalance(map);
			temp.setAppId(appIdFlag);
			temp.setEntryBy(entryBy);
			temp.setEntryDate(entryDate);
			temp.setOperateDepCode(operateDepCode);
			temp.setApplicatDate(applicatDate);
			temp.setMoneyUnit(moneyUnit);
			addList.add(temp);
		}
		for (Map map : list2) {
			ConJBalance temp = this.parseConJBalance(map);
			temp.setAppId(appIdFlag);
			temp.setEntryBy(entryBy);
			temp.setEntryDate(entryDate);
			temp.setOperateDepCode(operateDepCode);
			temp.setApplicatDate(applicatDate);
			temp.setMoneyUnit(moneyUnit);
			updateList.add(temp);
		}
		if (addList != null || updateList != null || ids != null) {
			Long val = balremote.saveModiRec(appIdFlag, addList, updateList,
					ids);
			write("{success : true,value :" + val + "}");
		} else {
			write("{success : true,value : null }");
		}
	}

	/**
	 * add by liuyi 091120 将已映射转为ConJBalance 对象
	 */
	public ConJBalance parseConJBalance(Map map) {
		Long balanceId = null;
		Long conId = null;
		String operateDepCode = null;
		String balaFlag = "0";
		String memo = null;
		Double applicatPrice = 0.0;
		Double passPrice = 0.0;
		String entryBy = employee.getWorkerCode();
		Date entryDate = new Date();
		String enterpriseCode = employee.getEnterpriseCode();
		String isUse = "Y";
		Long appId = null;

		if (map.get("balanceId") != null)
			balanceId = Long.parseLong(map.get("balanceId").toString());
		if (map.get("appId") != null)
			appId = Long.parseLong(map.get("appId").toString());
		if (map.get("conId") != null)
			conId = Long.parseLong(map.get("conId").toString());
		if (map.get("balaFlag") != null)
			balaFlag = map.get("balaFlag").toString();
		if (map.get("operateDepCode") != null)
			operateDepCode = map.get("operateDepCode").toString();
		if (map.get("memo") != null)
			memo = map.get("memo").toString();
		if (map.get("applicatPrice") != null
				&& !map.get("applicatPrice").toString().equals(""))
			applicatPrice = Double.parseDouble(map.get("applicatPrice")
					.toString());
		if (map.get("passPrice") != null
				&& !map.get("passPrice").toString().equals(""))
			passPrice = Double.parseDouble(map.get("passPrice").toString());

		ConJBalance temp = new ConJBalance();
		temp.setBalanceId(balanceId);
		temp.setConId(conId);
		temp.setOperateDepCode(operateDepCode);
		temp.setBalaFlag(balaFlag);
		temp.setMemo(memo);
		temp.setApplicatPrice(applicatPrice);
		temp.setPassPrice(passPrice);
		temp.setEntryBy(entryBy);
		temp.setEntryDate(entryDate);
		temp.setEnterpriseCode(enterpriseCode);
		temp.setIsUse(isUse);
		temp.setAppId(appId);

		return temp;

	}

	public void bqfindAppConList() throws JSONException {
		String appId = request.getParameter("appId");
		String start1 = request.getParameter("start");
		String limit1 = request.getParameter("limit");
		String approved = request.getParameter("approved");
		String status = request.getParameter("status");
		int start = (start1 == null) ? 0 : Integer.parseInt(start1);
		int limit = (limit1 == null) ? 999999 : Integer.parseInt(limit1);

		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(
				new String[] { "bqConBalance" }, employee.getWorkerCode());

		PageObject obj = balremote.bqfindAppConList(status, approved, entryIds,
				appId, employee.getWorkerCode(), employee.getEnterpriseCode(),
				start, limit);
		if (appId == null || appId.equals("")) {
			if (obj != null) {
				write(JSONUtil.serialize(obj));
			} else {
				write("{totalCount : 0,list :[]}");
			}
		} else {
			if (obj != null) {
				write(JSONUtil.serialize(obj.getList().get(0)));
			} else {
				write("{totalCount : 0,list :[]}");
			}
		}
	}

	public void bqBalanceQueryList() throws JSONException {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		String start1 = request.getParameter("start");
		String limit1 = request.getParameter("limit");
		int start = (start1 == null) ? 0 : Integer.parseInt(start1);
		int limit = (limit1 == null) ? 999999 : Integer.parseInt(limit1);
		PageObject object = balremote.balanceQueryList(startDate, endDate,
				start, limit);
		if (object != null) {
			write(JSONUtil.serialize(object));
		} else {
			write("{totalCount : 0,list :[]}");
		}

	}

	public void bqfindBalanceList() throws JSONException {
		String appId = request.getParameter("appId");
		String start1 = request.getParameter("start");
		String limit1 = request.getParameter("limit");
		int start = (start1 == null) ? 0 : Integer.parseInt(start1);
		int limit = (limit1 == null) ? 999999 : Integer.parseInt(limit1);
		PageObject obj = balremote.bqfindBalanceListByAppId(appId, employee
				.getEnterpriseCode(), start, limit);
		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{[]}");
		}
	}

	/**
	 * 删除采购合同付款申请
	 */
	public void bqDelRecByAppId() {
		String appId = request.getParameter("appId");
		balremote.deleteRecByAppId(appId);
	}

	/**
	 * 采购合同上报 bq
	 * 
	 */
	public void bqReportByAppId() {
		String appId = request.getParameter("appId");
		String nextRoles = request.getParameter("nextRoles");
		appremote.bqConBalanceReport(appId, employee.getEnterpriseCode(),
				employee.getWorkerCode());
		// -----------add by ltong 20100608 短信通知----------------
		PostMessage postMsg = new PostMessage();
		if (nextRoles == null || nextRoles.equals("")) {
			nextRoles = postMsg.getFistStepRoles("bqConBalance", "24", null,
					null);
		}
		if (nextRoles != null && !nextRoles.equals("")) {
			String msg = "采购合同付款申请等待您的审批，请您及时处理。";
			postMsg.sendMsg(nextRoles, msg);
		}
		// -----------add end--------------------------------------
	}

	/**
	 * 采购合同审批 bq
	 * 
	 */
	public void bqReportApproveByAppId() {
		String appId = request.getParameter("appId");
		String entryId = request.getParameter("entryId");
		String workerCode = request.getParameter("workerCode");
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String approveDepts = request.getParameter("approveDepts");
		try {
			// -----------add by ltong 20100608 短信通知----------------
			PostMessage postMsg = new PostMessage();
			String thisRoleString = nextRoles;
			if (!actionId.equals("72") && !actionId.equals("42")
					&& !actionId.equals("73")) {
				if (nextRoles == null || "".equals(nextRoles)) {
					thisRoleString = postMsg
							.getNextSetpRoles(entryId, actionId);
				}
			}
			// -----------add end-------------------------------------
			appremote.bqConBalanceApprove(appId, Long.parseLong(entryId), Long
					.parseLong(actionId), approveText, nextRoles, approveDepts,
					employee.getEnterpriseCode(), workerCode);
			// -----------add by ltong 20100608 短信通知----------------
			if (!actionId.equals("72") && !actionId.equals("42")) {
				if (thisRoleString != null && !thisRoleString.equals("")) {
					String msg = "采购合同付款申请等待您的审批，请您及时处理。";
					postMsg.sendMsg(thisRoleString, msg);
				}
			}
			// -----------add end-------------------------------------
			write("{success:true,data:'审批成功!'}");
		} catch (Exception e) {
			write("{success:false,errorMsg:'" + e.getMessage() + "'}");
		}
	}

	/**
	 * 采购合同增加选择列表 bq
	 * 
	 * @throws JSONException
	 * 
	 */
	public void bqContractSelect() throws JSONException {
		String fuzzy = request.getParameter("fuzzy");
		String start1 = request.getParameter("start");
		String limit1 = request.getParameter("limit");
		int start = (start1 == null) ? 0 : Integer.parseInt(start1);
		int limit = (limit1 == null) ? 999999 : Integer.parseInt(limit1);
		PageObject obj = balremote.bqContractSelect(fuzzy, start, limit);
		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{[]}");
		}
	}
}
