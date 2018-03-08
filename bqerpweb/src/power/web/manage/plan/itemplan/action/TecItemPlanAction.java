package power.web.manage.plan.itemplan.action;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.itemplan.BpCItemplanTecDep;
import power.ejb.manage.plan.itemplan.BpCItemplanTecItem;
import power.ejb.manage.plan.itemplan.BpJItemplanTecDetail;
import power.ejb.manage.plan.itemplan.TecItemPlanManager;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class TecItemPlanAction extends AbstractAction {
	private TecItemPlanManager remote;

	public TecItemPlanAction() {
		remote = (TecItemPlanManager) factory
				.getFacadeRemote("TecItemPlanManagerImpl");

	}

	//技术指标
	@SuppressWarnings("unchecked")
	public void saveOrUpdateTecItem() throws JSONException {
		String isAdd = request.getParameter("isAdd");
		String isUpdate = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		Object addObj = JSONUtil.deserialize(isAdd);
		Object updateObj = JSONUtil.deserialize(isUpdate);
		List<Map> list1 = (List<Map>) addObj;
		List<Map> list2 = (List<Map>) updateObj;
		List<BpCItemplanTecItem> addList = new ArrayList<BpCItemplanTecItem>();
		List<BpCItemplanTecItem> updateList = new ArrayList<BpCItemplanTecItem>();

		for (Map map : list1) {
			BpCItemplanTecItem entity = this.parseTecItem(map);
			addList.add(entity);
		}
		for (Map map : list2) {
			BpCItemplanTecItem entity = this.parseTecItem(map);
			updateList.add(entity);
		}
		if (addList != null || updateList != null || ids.equals("")) {
			remote.saveTecItem(addList, updateList, ids);
			write("{success : true,msg :'保存成功!'}");
		}
	}

	@SuppressWarnings("unchecked")
	private BpCItemplanTecItem parseTecItem(Map map) {
		Long technologyItemId = null;
		String itemCode = null;
		String itemName = null;
		String alias = null;
		Long unitId = null;
		Long displayNo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		if (map.get("technologyItemId") != null) {
			technologyItemId = Long.parseLong(map.get("technologyItemId")
					.toString());
		}
		if (map.get("itemCode") != null) {
			itemCode = map.get("itemCode").toString();
		}
		if (map.get("itemName") != null) {
			itemName = map.get("itemName").toString();
		}
		if (map.get("alias") != null) {
			alias = map.get("alias").toString();
		}
		if (map.get("displayNo") != null && !map.get("displayNo").toString().equals("")) {
			displayNo = Long.parseLong(map.get("displayNo").toString());
		}
		if (map.get("unitId") != null) {
			unitId = Long.parseLong(map.get("unitId").toString());
		}

		BpCItemplanTecItem model = new BpCItemplanTecItem();
		model.setTechnologyItemId(technologyItemId);
		model.setItemCode(itemCode);
		model.setItemName(itemName);
		model.setDisplayNo(displayNo);
		model.setUnitId(unitId);
		model.setAlias(alias);
		model.setIsUse(isUse);
		model.setEnterpriseCode(enterpriseCode);

		return model;

	}
	//技术部门
	@SuppressWarnings("unchecked")
	public void saveOrUpdateTecDept() throws JSONException {
		String isAdd = request.getParameter("isAdd");
		String isUpdate = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		Object addObj = JSONUtil.deserialize(isAdd);
		Object updateObj = JSONUtil.deserialize(isUpdate);
		List<Map> list1 = (List<Map>) addObj;
		List<Map> list2 = (List<Map>) updateObj;
		List<BpCItemplanTecDep> addList = new ArrayList<BpCItemplanTecDep>();
		List<BpCItemplanTecDep> updateList = new ArrayList<BpCItemplanTecDep>();
		for (Map map : list1) {
			BpCItemplanTecDep entity = this.parseTecDept(map);
			addList.add(entity);
		}
		for (Map map : list2) {
			BpCItemplanTecDep entity = this.parseTecDept(map);
			updateList.add(entity);
		}
		if (addList != null || updateList != null || ids != null) {
			remote.saveTecDept(addList, updateList, ids);
			write("{success : true,msg :'保存成功!'}");
		}

	}

	@SuppressWarnings("unchecked")
	private BpCItemplanTecDep parseTecDept(Map map) {
		Long depId = null;
		String depCode = null;
		String depName = null;
		Long displayNo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		if (map.get("depId") != null) {
			depId = Long.parseLong(map.get("depId").toString());
		}
		if (map.get("depCode") != null) {
			depCode = map.get("depCode").toString();
		}
		if (map.get("depName") != null) {
			depName = map.get("depName").toString();
		}
		if (map.get("displayNo") != null && !map.get("displayNo").toString().equals("")) {
			displayNo = Long.parseLong(map.get("displayNo").toString());
		}
		if (map.get("isUse") != null) {
			isUse = map.get("isUse").toString();
		}
		if (map.get("enterpriseCode") != null) {
			enterpriseCode = map.get("enterpriseCode").toString();
		}
		BpCItemplanTecDep model = new BpCItemplanTecDep();
		model.setDepId(depId);
		model.setDepCode(depCode);
		model.setDepName(depName);
		model.setDisplayNo(displayNo);
		model.setIsUse(isUse);
		model.setEnterpriseCode(enterpriseCode);
		return model;
	}

	public void getTecItemList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if (start != null && limit != null)
			pg = remote.getTecItemList(Integer.parseInt(start), Integer
					.parseInt(limit));
		else
			pg = remote.getTecItemList();
		write(JSONUtil.serialize(pg));
	}

	public void getTecDeptList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if (start != null && limit != null)
			pg = remote.getTecDeptList(Integer.parseInt(start), Integer
					.parseInt(limit));
		else
			pg = remote.getTecDeptList();
		write(JSONUtil.serialize(pg));
	}

	//技术明细
	@SuppressWarnings("unchecked")
	public void saveOrUpdateTecDetail() throws JSONException, ParseException {
		String month = request.getParameter("month");
		String isAdd = request.getParameter("isAdd");
		String isUpdate = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		Object addObj = JSONUtil.deserialize(isAdd);
		Object updateObj = JSONUtil.deserialize(isUpdate);
		List<Map> list1 = (List<Map>) addObj;
		List<Map> list2 = (List<Map>) updateObj;
		List<BpJItemplanTecDetail> addList = new ArrayList<BpJItemplanTecDetail>();
		List<BpJItemplanTecDetail> updateList = new ArrayList<BpJItemplanTecDetail>();
		for (Map map : list1) {
			BpJItemplanTecDetail entity = this.parseTecDetail(map);
			addList.add(entity);
		}
		for (Map map : list2) {
			BpJItemplanTecDetail entity = this.parseTecDetail(map);
			updateList.add(entity);
		}
		if (addList != null || updateList != null || ids != null) {
			remote.saveTecDetail(addList, updateList, month, ids,employee.getEnterpriseCode());
			write("{success : true,msg :'保存成功!'}");
		}
		
	}

	@SuppressWarnings("unchecked")
	private BpJItemplanTecDetail parseTecDetail(Map map){
		 Long tecDetailId =null;
		 Long tecMainId=null;
		 Long technologyItemId=null;
		 Long depId=null;
		 String tecPlan=null;
		 String tecFact=null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		if (map.get("tecDetailId") != null && !map.get("tecDetailId").toString().equals("")) {
			tecDetailId = Long.parseLong(map.get("tecDetailId").toString());
		}
		if (map.get("tecMainId") != null && !map.get("tecMainId").toString().equals("") ) {
			tecMainId = Long.parseLong(map.get("tecMainId").toString());
		}
		if (map.get("technologyItemId") != null && !map.get("technologyItemId").toString().equals("")) {
			technologyItemId = Long.parseLong(map.get("technologyItemId").toString());
		}
		if (map.get("depId") != null && !map.get("depId").toString().equals("") ) {
			depId = Long.parseLong(map.get("depId").toString());
		}
		if (map.get("tecPlan") != null) {
			tecPlan = map.get("tecPlan").toString();
		}
		if (map.get("tecFact") != null) {
			tecFact = map.get("tecFact").toString();
		}
		
		BpJItemplanTecDetail model = new BpJItemplanTecDetail();
		model.setTecDetailId(tecDetailId);
		model.setTecMainId(tecMainId);
		model.setDepId(depId);
		model.setTechnologyItemId(technologyItemId);
		model.setTecPlan(tecPlan);
		model.setTecFact(tecFact);
		model.setIsUse(isUse);
		model.setEnterpriseCode(enterpriseCode);
		return model;
	
		
	}
	public void getTecDeptItemList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String month = request.getParameter("month");
		String status = request.getParameter("status");
		String init = request.getParameter("init");
		String entryIds = "";
		
		//部门Id
		String deptId = request.getParameter("deptId");
		
		//技术指标填写审批权限
		if (status != null && status.equals("approve")) {
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bqFillTecItemPlan" }, employee
							.getWorkerCode());
		}
		
		//技术指标完成情况审批权限
		if(status != null && status.equals("Fapprove")){
			WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
			entryIds = workflowService.getAvailableWorkflow(
					new String[] { "bpTecItemFact" }, employee
							.getWorkerCode());
		}
		PageObject pg = null;
		if (start != null && limit != null)
			pg =remote.getTecDeptItemList(entryIds,month, status,deptId,Integer.parseInt(start), Integer
					.parseInt(limit));
		else
			pg = remote.getTecDeptItemList(entryIds,month, status,deptId);
		
		//初始化
		if(init!=null && init.equals("init")){
			if(pg.getTotalCount() > 0)
			write(JSONUtil.serialize(pg.getList().get(0)));
		}
		write(JSONUtil.serialize(pg));

	}
}
