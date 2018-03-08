package power.web.manage.budget.action;

import java.lang.reflect.Array;
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
import power.ejb.manage.budget.CbmJBudgetItem;
import power.ejb.manage.budget.CbmJBudgetItemFacadeRemote;
import power.ejb.manage.budget.CbmJBudgetMake;
import power.ejb.manage.budget.CbmJBudgetMakeFacadeRemote;
import power.ejb.manage.budget.CbmJMasterItem;
import power.ejb.manage.budget.CbmJMasterItemFacadeRemote;
import power.web.comm.AbstractAction;

public class ExeControlFeeAction extends AbstractAction {
	private CbmJMasterItemFacadeRemote remote;
	private CbmJBudgetItemFacadeRemote detaRemote;
	private CbmJBudgetMakeFacadeRemote makeRemote;

	public ExeControlFeeAction() {
		remote = (CbmJMasterItemFacadeRemote) factory
				.getFacadeRemote("CbmJMasterItemFacade");
		detaRemote = (CbmJBudgetItemFacadeRemote) factory
				.getFacadeRemote("CbmJBudgetItemFacade");
		makeRemote = (CbmJBudgetMakeFacadeRemote) factory
				.getFacadeRemote("CbmJBudgetMakeFacade");
	}

	public void getDeptBudetDetails() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String deptCode = request.getParameter("deptCode");
		String dataType = request.getParameter("dataType");
		String budgetTime = request.getParameter("budgetTime");
		String budgetItemId = request.getParameter("budgetItemId");
		if (start == null || limit == null) {
			PageObject pg = detaRemote.findToCheck(deptCode, budgetTime,
					dataType, employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		} else {
			PageObject pg = detaRemote.findToCheck(deptCode, budgetTime,
					dataType, employee.getEnterpriseCode(), Integer
							.parseInt(start), Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
	}

	public void getControlFeeList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String deptCode = request.getParameter("deptCode");
		String dataType = request.getParameter("dataType");
		String budgetTime = request.getParameter("budgetTime");
		String budgetItemId = request.getParameter("budgetItemId");
		String isFeeRegister = request.getParameter("isFeeRegister");
		if (start == null || limit == null) {
			PageObject pg = remote.getAllList(isFeeRegister, budgetItemId,
					deptCode, dataType, budgetTime, employee
							.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		} else {
			PageObject pg = remote.getAllList(isFeeRegister, budgetItemId,
					deptCode, dataType, budgetTime, employee
							.getEnterpriseCode(), Integer.parseInt(start),
					Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
	}

	@SuppressWarnings("unchecked")
	public void saveControlFeeModified() throws JSONException {
		String addString = request.getParameter("isAdd");
		String updateString = request.getParameter("isUpdate");
		String deleteIds = request.getParameter("isDelete");
		String isFeeRegister = request.getParameter("isFeeRegister");
		String isHave = request.getParameter("isHave");
		Object addObject = JSONUtil.deserialize(addString);
		Object updateObject = JSONUtil.deserialize(updateString);
		List<Map> alist = (List<Map>) addObject;
		List<Map> ulist = (List<Map>) updateObject;
		List<CbmJMasterItem> addList = new ArrayList<CbmJMasterItem>();
		List<CbmJMasterItem> updateList = new ArrayList<CbmJMasterItem>();
		SimpleDateFormat sbf = new SimpleDateFormat("yyyy-MM-dd");
		String bmIdArray = request.getParameter("bmIdArray");

		String bmidString = "";

		List<CbmJBudgetItem> newItemList = new ArrayList<CbmJBudgetItem>();
		// -------------------当前日期没有数据时为0-----------------
		if ("0".equals(isHave)) {
			if (alist != null && alist.size() > 0) {
				Map myData = alist.get(0);
				String thisTime = "";
				if (myData.get("cjm.budgetTime") != null) {
					thisTime = myData.get("cjm.budgetTime").toString();
				}
				if (bmIdArray != null && !"".equals(bmIdArray)) {
					List bmIdObject = (List) JSONUtil.deserialize(bmIdArray);
					for (int i = 0; i < bmIdObject.size(); i++) {
						bmidString = bmIdObject.get(i).toString();
						CbmJBudgetMake makeModel = makeRemote.findById(Long
								.parseLong(bmidString));
						makeModel.setBudgetMakeId(null);
						makeModel.setBudgetTime(thisTime);
						makeModel.setMakeBy(employee.getWorkerCode());
						makeModel.setMakeDate(new Date());
						makeModel = makeRemote.save(makeModel);
						List<CbmJBudgetItem> itemList = detaRemote
								.findBudgetByMakeId(Long.parseLong(bmidString));
						for (CbmJBudgetItem model : itemList) {
							model.setBudgetMakeId(makeModel.getBudgetMakeId());
							model.setModifyBy(employee.getWorkerCode());
							model.setModifyDate(new Date());
							CbmJBudgetItem detailModel = detaRemote.save(model);
							newItemList.add(detailModel);
						}
					}
				}
			}

		}
		// ------------------------------------

		for (Map data : alist) {
			CbmJMasterItem temp = new CbmJMasterItem();
			Long happenId = null;
			Long budgetItemId = null;
			Long centerId = null;
			String budgetTime = null;
			Long itemId = null;
			Long happenSerial = null;
			Double happenValue = null;
			String happenExplain = null;
			String fillBy = null;
			Date fillTime = null;
			Long workFlowNo = null;
			String happenStatus = "0";
			String isUse = "Y";
			String enterpriseCode = employee.getEnterpriseCode();

			if (data.get("cjm.happenId") != null)
				happenId = Long.parseLong(data.get("cjm.happenId").toString());
			if (data.get("cjm.budgetItemId") != null)
				budgetItemId = Long.parseLong(data.get("cjm.budgetItemId")
						.toString());
			if (data.get("cjm.centerId") != null)
				centerId = Long.parseLong(data.get("cjm.centerId").toString());
			if (data.get("cjm.budgetTime") != null)
				budgetTime = data.get("cjm.budgetTime").toString();
			if (data.get("cjm.itemId") != null)
				itemId = Long.parseLong(data.get("cjm.itemId").toString());
			if (data.get("cjm.happenSerial") != null)
				happenSerial = Long.parseLong(data.get("cjm.happenSerial")
						.toString());
			if (data.get("cjm.happenValue") != null)
				if (isFeeRegister != null && !(isFeeRegister.equals("N"))) {
					happenValue = Double.parseDouble(data
							.get("cjm.happenValue").toString()) / 10000;
				} else {
					happenValue = Double.parseDouble(data
							.get("cjm.happenValue").toString());
				}

			if (data.get("cjm.happenExplain") != null)
				happenExplain = data.get("cjm.happenExplain").toString();
			if (data.get("cjm.fillBy") != null)
				fillBy = data.get("cjm.fillBy").toString();
			if (data.get("cjm.fillTime") != null)
				try {
					fillTime = sbf.parse(data.get("cjm.fillTime").toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			if (data.get("cjm.workFlowNo") != null)
				workFlowNo = Long.parseLong(data.get("cjm.workFlowNo")
						.toString());
			if (data.get("cjm.happenStatus") != null)
				happenStatus = data.get("cjm.happenStatus").toString();

			// --------------------------
			for (CbmJBudgetItem detailModel : newItemList) {
//				if ((detailModel.getItemId() + "").equals(itemId + "")) {
//					budgetItemId = detailModel.getBudgetItemId();
//				}

			}
			// --------------------------
			temp.setHappenId(happenId);
			temp.setBudgetItemId(budgetItemId);
			temp.setCenterId(centerId);
			temp.setBudgetTime(budgetTime);
			temp.setItemId(itemId);
			temp.setHappenSerial(happenSerial);
			temp.setHappenValue(happenValue);
			temp.setHappenExplain(happenExplain);
			temp.setFillBy(fillBy);
			temp.setFillTime(fillTime);
			temp.setWorkFlowNo(workFlowNo);
			if (happenStatus.equals("3"))
				happenStatus = "0";
			temp.setHappenStatus(happenStatus);
			temp.setIsUse(isUse);
			temp.setEnterpriseCode(enterpriseCode);

			addList.add(temp);
		}
		for (Map data : ulist) {
			CbmJMasterItem temp = new CbmJMasterItem();
			Long happenId = null;
			Long budgetItemId = null;
			Long centerId = null;
			String budgetTime = null;
			Long itemId = null;
			Long happenSerial = null;
			Double happenValue = null;
			String happenExplain = null;
			String fillBy = null;
			Date fillTime = null;
			Long workFlowNo = null;
			String happenStatus = "0";
			String isUse = "Y";
			String enterpriseCode = employee.getEnterpriseCode();

			if (data.get("cjm.happenId") != null)
				happenId = Long.parseLong(data.get("cjm.happenId").toString());
			if (data.get("cjm.budgetItemId") != null)
				budgetItemId = Long.parseLong(data.get("cjm.budgetItemId")
						.toString());
			if (data.get("cjm.centerId") != null)
				centerId = Long.parseLong(data.get("cjm.centerId").toString());
			if (data.get("cjm.budgetTime") != null)
				budgetTime = data.get("cjm.budgetTime").toString();
			if (data.get("cjm.itemId") != null)
				itemId = Long.parseLong(data.get("cjm.itemId").toString());
			if (data.get("cjm.happenSerial") != null)
				happenSerial = Long.parseLong(data.get("cjm.happenSerial")
						.toString());
			if (data.get("cjm.happenValue") != null)
				if (isFeeRegister != null && !(isFeeRegister.equals("N"))) {
					happenValue = Double.parseDouble(data
							.get("cjm.happenValue").toString()) / 10000;
				} else {
					happenValue = Double.parseDouble(data
							.get("cjm.happenValue").toString());
				}
			if (data.get("cjm.happenExplain") != null)
				happenExplain = data.get("cjm.happenExplain").toString();
			if (data.get("cjm.fillBy") != null)
				fillBy = data.get("cjm.fillBy").toString();
			if (data.get("cjm.fillTime") != null)
				try {
					fillTime = sbf.parse(data.get("cjm.fillTime").toString());
				} catch (ParseException e) {
					e.printStackTrace();
				}
			if (data.get("cjm.workFlowNo") != null)
				workFlowNo = Long.parseLong(data.get("cjm.workFlowNo")
						.toString());
			if (data.get("cjm.happenStatus") != null)
				happenStatus = data.get("cjm.happenStatus").toString();

			temp.setHappenId(happenId);
			temp.setBudgetItemId(budgetItemId);
			temp.setCenterId(centerId);
			temp.setBudgetTime(budgetTime);
			temp.setItemId(itemId);
			temp.setHappenSerial(happenSerial);
			temp.setHappenValue(happenValue);
			temp.setHappenExplain(happenExplain);
			temp.setFillBy(fillBy);
			temp.setFillTime(fillTime);
			temp.setWorkFlowNo(workFlowNo);
			if (happenStatus.equals("3"))
				happenStatus = "0";
			temp.setHappenStatus(happenStatus);
			temp.setIsUse(isUse);
			temp.setEnterpriseCode(enterpriseCode);

			updateList.add(temp);
		}

		if (addList != null || updateList != null || deleteIds != null) {
			if (addList.size() > 0 || updateList.size() > 0
					|| deleteIds.length() > 0) {
				remote.saveModified(isFeeRegister, addList, updateList,
						deleteIds);
				write("{success: true,msg:'数据保存修改成功！'}");
			}
		}

	}

	// 上报
	public void exeControlReportTo() {
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String workflowType = request.getParameter("flowCode");
		String nextRoles = request.getParameter("nextRoles");
		String eventIdentify = request.getParameter("eventIdentify");
		String happenId = request.getParameter("happenId");
		String workerCode = request.getParameter("workerCode");
		remote
				.reportTo(Long.parseLong(happenId), workflowType, workerCode,
						Long.parseLong(actionId), approveText, nextRoles,
						eventIdentify);
		write("{success:true,msg:'上报成功！'}");
	}

	// 审批
	public void masterItemCommSign() {
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String eventIdentify = request.getParameter("eventIdentify");
		String happenId = request.getParameter("happenId");
		String responseDate = request.getParameter("responseDate");
		String workerCode = request.getParameter("workerCode");
		remote.masterItemCommSign(Long.parseLong(happenId), approveText,
				workerCode, Long.parseLong(actionId), responseDate, nextRoles,
				eventIdentify);
		write("{success:true,msg:'审批成功！'}");
	}

	public void masterItemApproveQuery() throws JSONException {
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String budgetTime = request.getParameter("budgetTime");
		String centerId = request.getParameter("centerId");
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(
				new String[] { "budgetExeControlApprove" }, employee
						.getWorkerCode());
		PageObject obj = new PageObject();

		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.masterItemApproveQuery(centerId, budgetTime, entryIds,
					start, limit);
		} else {
			obj = remote.masterItemApproveQuery(centerId, centerId, entryIds);
		}
		String str = JSONUtil.serialize(obj);
		if (str.equals("null")) {
			str = "{\"list\":[],\"totalCount\":null}";
		}

		write(str);
	}
}