package power.web.manage.budget.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmCBasis;
import power.ejb.manage.budget.CbmCBasisFacadeRemote;
import power.ejb.manage.budget.CbmJBudgetItem;
import power.ejb.manage.budget.CbmJBudgetItemFacadeRemote;
import power.ejb.manage.budget.CbmJBudgetMake;
import power.ejb.manage.budget.CbmJBudgetMakeFacadeRemote;
import power.web.comm.AbstractAction;

import power.ejb.manage.budget.form.CbmJBudgetItemForm;

/**
 * 
 * @author fyyang 090819
 * 
 */
@SuppressWarnings("serial")
public class BudgetMakeAction extends AbstractAction {
	CbmJBudgetMakeFacadeRemote remote;
	CbmJBudgetItemFacadeRemote detailRemote;
	CbmCBasisFacadeRemote basisRemote;
	/**
	 * 构造函数
	 */
	public BudgetMakeAction() {
		remote = (CbmJBudgetMakeFacadeRemote) factory
				.getFacadeRemote("CbmJBudgetMakeFacade");
		detailRemote = (CbmJBudgetItemFacadeRemote) factory
				.getFacadeRemote("CbmJBudgetItemFacade");
		basisRemote = (CbmCBasisFacadeRemote)factory.getFacadeRemote("CbmCBasisFacade");
	}

	/**
	 * 按照预算时间，预算部门和预算主题 查找编制单信息
	 */
	public void findBudgetMakeList() throws JSONException {
		String time = request.getParameter("budgetTime");
		String deptCode = request.getParameter("deptCode");
		String topicId = request.getParameter("topicId");
		PageObject rec = new PageObject();
		List<CbmJBudgetItemForm> list = remote.findByTopicAndDept(time,
				deptCode, Long.parseLong(topicId));
		if (list != null && list.size() > 0) {
			rec.setList(list);
			rec.setTotalCount(Long.parseLong(list.size() + ""));
		}
		write(JSONUtil.serialize(rec));
	}

	/**
	 * 保存(增加或修改)预算编制单信息
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void saveBudgetMake() throws JSONException {
		String addOrUpdateRecords = request.getParameter("addOrUpdateRecords");
		String isHappen = request.getParameter("isHappen");// add by ltong
		if (addOrUpdateRecords != null) {
			List<CbmJBudgetItem> list = this
					.parseRecordList(addOrUpdateRecords);
			detailRemote.addOrUpdateRecords(list, isHappen);
			if (list != null && list.size() > 0) {
				detailRemote.calculateItemValue(list.get(0).getBudgetMakeId());
			}
			write("{success:true,msg:'保存成功！'}");
		}
	}

	@SuppressWarnings("unchecked")
	private List<CbmJBudgetItem> parseRecordList(String records)
			throws JSONException {
		CbmJBudgetMake makeModel = new CbmJBudgetMake();
		makeModel.setMakeBy(employee.getWorkerCode());
		makeModel.setEnterpriseCode(employee.getEnterpriseCode());
		makeModel.setIsUse("Y");
		List<CbmJBudgetItem> result = new ArrayList<CbmJBudgetItem>();
		Object object = JSONUtil.deserialize(records);
		List list = (List) object;
		int intLen = list.size();
		for (int i = 0; i < intLen; i++) {
			Map map = (Map) list.get(i);
			CbmJBudgetItem model = this.parseRecordModel(map, makeModel);
			result.add(model);
		}

		if (makeModel.getBudgetMakeId() == null) {
			List<CbmJBudgetItem> resultAdd = new ArrayList<CbmJBudgetItem>();
			makeModel = remote.save(makeModel);
			for (int j = 0; j < result.size(); j++) {
				CbmJBudgetItem itemModel = new CbmJBudgetItem();
				itemModel = result.get(j);
				itemModel.setBudgetMakeId(makeModel.getBudgetMakeId());
				resultAdd.add(itemModel);
			}
			return resultAdd;
		} else {
			CbmJBudgetMake makeEntity = remote.findById(makeModel
					.getBudgetMakeId());
			makeEntity.setMakeBy(employee.getWorkerCode());
			remote.update(makeEntity);
			return result;
		}

	}

	@SuppressWarnings("unchecked")
	private CbmJBudgetItem parseRecordModel(Map map, CbmJBudgetMake makeModel) {
		CbmJBudgetItem model = new CbmJBudgetItem();
		model.setEnterpriseCode(employee.getEnterpriseCode());
		model.setIsUse("Y");
		
		Object budgetMakeId = map.get("budgetMakeId");
		Object centerId = map.get("centerId");
		Object topicId = map.get("topicId");
		Object budgetTime = map.get("budgetTime");

		Object budgetItemId = map.get("budgetItemId");

		Object adviceBudget = map.get("adviceBudget");
		Object budgetBasis = map.get("budgetBasis");
		Object itemId = map.get("itemId");
		Object financeHappen = map.get("financeHappen");

		if (budgetMakeId != null && !budgetMakeId.equals("")) {
			model.setBudgetMakeId(Long.parseLong(budgetMakeId.toString()));
			makeModel.setBudgetMakeId(Long.parseLong(budgetMakeId.toString()));
		}
		if (centerId != null && !centerId.equals("")) {
			makeModel.setCenterId(Long.parseLong(centerId.toString()));
		}
		if (topicId != null && !topicId.equals("")) {
			makeModel.setTopicId(Long.parseLong(topicId.toString()));
		}
		if (budgetTime != null && !budgetTime.equals("")) {
			makeModel.setBudgetTime(budgetTime.toString());
		}

		if (budgetItemId != null && !budgetItemId.equals("")) {
			model.setBudgetItemId(Long.parseLong(budgetItemId.toString()));
		}

		if (adviceBudget != null && !adviceBudget.equals("")) {
			model.setAdviceBudget(Double.parseDouble(adviceBudget.toString()));
		}
		if (budgetBasis != null && !budgetBasis.equals("")) {
			model.setBudgetBasis(budgetBasis.toString());
		}
		if (itemId != null && !itemId.equals("")) {
			model.setCenterItemId(Long.parseLong(itemId.toString()));
		}
		if (financeHappen != null && !financeHappen.equals("")) {
			model
					.setFinanceHappen(Double.parseDouble(financeHappen
							.toString()));
		}
		return model;

	}

	/**
	 * 上报预算编制单
	 */
	public void upcommitBudgetMake() throws JSONException {

		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String workflowType = request.getParameter("flowCode");
		String nextRoles = request.getParameter("nextRoles");
		String eventIdentify = request.getParameter("eventIdentify");
		String budgetMakeId = request.getParameter("budgetMakeId");
		// String responseDate=request.getParameter("responseDate");
		String workerCode = request.getParameter("workerCode");
		remote
				.reportTo(Long.parseLong(budgetMakeId), workflowType,
						workerCode, Long.parseLong(actionId), approveText,
						nextRoles, eventIdentify);
		write("{success:true,msg:'上报成功！'}");
	}

	/**
	 * 发送预算编制单 add by ltong 20100528
	 */
	public void sendBudgetMake() throws JSONException {

		String budgetMakeId = request.getParameter("budgetMakeId");
		CbmJBudgetMake model = new CbmJBudgetMake();
		model = remote.findById(Long.parseLong(budgetMakeId));
		model.setSendBy(employee.getWorkerCode());
		model.setSendDate(new Date());
		model.setMakeStatus("2");
		remote.update(model);
		write("{success:true,msg:'发送成功！'}");
	}

	/**
	 * 获得编制单审批列表
	 * 
	 * @throws JSONException
	 */
	public void findMakeApproveList() throws JSONException {
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String topicId = request.getParameter("topicId");
		String centerId = request.getParameter("centerId");
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService
				.getAvailableWorkflow(new String[] { "budgetMakeApporval" },
						employee.getWorkerCode());
		PageObject obj = new PageObject();

		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findMakeApproveList(topicId, centerId, entryIds,
					start, limit);
		} else {
			obj = remote.findMakeApproveList(topicId, centerId, entryIds);
		}
		String str = JSONUtil.serialize(obj);
		if (str.equals("null")) {
			str = "{\"list\":[],\"totalCount\":null}";
		}

		write(str);
	}

	/**
	 * 根据编制单id查询对应的物资
	 * 
	 * @throws JSONException
	 */
	public void findMakeItemListByMakeId() throws JSONException {
		String budgetMakeId = request.getParameter("budgetMakeId");

		PageObject rec = new PageObject();
		List<CbmJBudgetItemForm> list = remote.findMakeItemByMakeId(Long
				.parseLong(budgetMakeId));
		if (list != null && list.size() > 0) {
			rec.setList(list);
			rec.setTotalCount(Long.parseLong(list.size() + ""));
		}
		write(JSONUtil.serialize(rec));
	}

	/**
	 * 审批签字
	 */
	public void budgetMakeCommSign() {
		String actionId = request.getParameter("actionId");
		String approveText = request.getParameter("approveText");
		String nextRoles = request.getParameter("nextRoles");
		String eventIdentify = request.getParameter("eventIdentify");
		String budgetMakeId = request.getParameter("budgetMakeId");
		String responseDate = request.getParameter("responseDate");
		String workerCode = request.getParameter("workerCode");
		remote.MakeCommSign(Long.parseLong(budgetMakeId), approveText,
				workerCode, Long.parseLong(actionId), responseDate, nextRoles,
				eventIdentify);
		write("{success:true,msg:'审批成功！'}");
	}

	/**
	 * 部门预算指标查询 查询 add by ltong
	 */
	public void findDeptTopicBusinessList() throws JSONException {
		// String start = request.getParameter("start");
		// String limit = request.getParameter("limit");
		String centerTopicId = request.getParameter("centerTopicId");
		String centerId = request.getParameter("centerId");
		String budgetTime = request.getParameter("findmonth");
		PageObject rec = new PageObject();
		// String enterpriseCode = employee.getEnterpriseCode();
		// if (start == null || limit == null) {
		List<CbmJBudgetItemForm> list = remote.findDeptTopicItemList(
				centerTopicId, centerId, budgetTime, employee
						.getEnterpriseCode());
		if (list != null && list.size() > 0) {
			rec.setList(list);
			rec.setTotalCount(Long.parseLong(list.size() + ""));
		}
		write(JSONUtil.serialize(rec));
		// } else {
		// PageObject pg = mremote.findDeptTopicItemList(centerTopicId,
		// employee.getEnterpriseCode(), Integer.parseInt(start),
		// Integer.parseInt(limit));
		// write(JSONUtil.serialize(pg));
		// }
	}
	
	/**
	 * 现金预算编制依据明细查询
	 * add by drdu 20100702
	 * @throws JSONException
	 */
	public void findBudgetBasisList() throws JSONException
	{
		String time = request.getParameter("budgetTime");
		String deptCode = request.getParameter("deptCode");
		String topicId = request.getParameter("topicId");
		PageObject object = null;
		object = remote.findBudgetBasisList(time, deptCode, Long.parseLong(topicId));
	
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
	
	//=======================编制明细维护 add by drdu 20100701  start=========================
	
	public void findBasisListById() throws JSONException
	{
		String budgetItemId = request.getParameter("budgetItemId");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = basisRemote.findBasisList(employee.getEnterpriseCode(), budgetItemId, start,limit);
		} else {
			object = basisRemote.findBasisList(employee.getEnterpriseCode(), budgetItemId);
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
//	
//	public void deleteBasisRecord()
//	{
////		String ids = request.getParameter("ids");
////		basisRemote.deleMutl(ids);
////		write("{success:'true',msg:'删除成功！'}");
//	}
	
	@SuppressWarnings("unchecked")
	public void editBasisRecord() throws ParseException, JSONException {
		String str = request.getParameter("isUpdate");
		String deleteIds = request.getParameter("isDelete");
		String amountSum = request.getParameter("amountSum");
		String budgetItem = request.getParameter("budgetItemId");

		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<CbmCBasis> addList = null;
		List<CbmCBasis> updateList = null;
		if (list != null && list.size() > 0) {
			addList = new ArrayList<CbmCBasis>();
			updateList = new ArrayList<CbmCBasis>();
			for (Map data : list) {
				if (data != null) {
					String basisId = null;
					String budgetItemId = null;
					String budgetBasis = null;
					String budgetAmount = null;
					String lastModifyBy = null;
					String lastModifyDate = null;

					if (data.get("basisId") != null
							&& !data.get("basisId").equals(""))
						basisId = data.get("basisId").toString();
					if (data.get("budgetItemId") != null
							&& !data.get("budgetItemId").equals(""))
						budgetItemId = data.get("budgetItemId").toString();
					if (data.get("budgetBasis") != null
							&& !data.get("budgetBasis").equals(""))
						budgetBasis = data.get("budgetBasis").toString();
					if (data.get("budgetAmount") != null
							&& !data.get("budgetAmount").equals(""))
						budgetAmount = data.get("budgetAmount").toString();
					if (data.get("lastModifyBy") != null
							&& !data.get("lastModifyBy").equals(""))
						lastModifyBy = data.get("lastModifyBy").toString();
					if (data.get("lastModifyDate") != null
							&& !data.get("lastModifyDate").equals(""))
						lastModifyDate = data.get("lastModifyDate").toString();

					CbmCBasis model = new CbmCBasis();
					if (basisId == null) {
						if (budgetItemId != null) {
							model.setBudgetItemId(Long.parseLong(budgetItemId.toString()));
						}
						if (budgetBasis != null) {
							model.setBudgetBasis((budgetBasis));
						}
						if (budgetAmount != null) {
							model.setBudgetAmount(Double.parseDouble(budgetAmount));
						}
						model.setLastModifyBy(employee.getWorkerCode());
						model.setLastModifyDate(new Date());
						model.setEnterpriseCode(employee.getEnterpriseCode());
						addList.add(model);
					} else {
						model = basisRemote.findById(Long.parseLong(basisId));
						if (budgetItemId != null) {
							model.setBudgetItemId(Long.parseLong(budgetItemId));
						}
						if (budgetBasis != null) {
							model.setBudgetBasis((budgetBasis));
						}
						if (budgetAmount != null) {
							model.setBudgetAmount(Double.parseDouble(budgetAmount));
						}
						model.setLastModifyBy(employee.getWorkerCode());
						model.setLastModifyDate(new Date());
						updateList.add(model);
					}
				}
			}
		}
		basisRemote.saveBasis(addList, updateList);
		
		if (deleteIds != null && !deleteIds.trim().equals("")) {
			basisRemote.deleMutl(deleteIds);
		}
		
		if (budgetItem != null && !budgetItem.trim().equals("")) {
			CbmJBudgetItem itemModel = detailRemote.findById(Long.parseLong(budgetItem));
			itemModel.setAdviceBudget(Double.parseDouble(amountSum));
			detailRemote.update(itemModel);
		}
		
		write("{success:true,msg:'操作成功！'}");
	}
	
	//=======================add by drdu end=====================================
	
}
