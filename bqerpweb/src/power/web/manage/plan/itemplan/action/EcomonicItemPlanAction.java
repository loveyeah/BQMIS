package power.web.manage.plan.itemplan.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.itemplan.BpCItemplanEcoItem;
import power.ejb.manage.plan.itemplan.BpCItemplanTopic;
import power.ejb.manage.plan.itemplan.BpJItemplanDepDetail;
import power.ejb.manage.plan.itemplan.BpJItemplanDepMain;
import power.ejb.manage.plan.itemplan.BpJItemplanPlantDetail;
import power.ejb.manage.plan.itemplan.BpJItemplanPlantMain;
import power.ejb.manage.plan.itemplan.EcomonicItemPlanManager;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class EcomonicItemPlanAction extends AbstractAction {
	private EcomonicItemPlanManager remote;

	public EcomonicItemPlanAction() {
		remote = (EcomonicItemPlanManager) factory
				.getFacadeRemote("EcomonicItemPlanManagerImpl");
	}

	/**
	 * 取得所有的维护主题
	 */
	public void getAllTopicRecord() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if (start != null && limit != null)
			pg = remote.getAllTopicRecord(employee.getEnterpriseCode(), Integer
					.parseInt(start), Integer.parseInt(limit));
		else
			pg = remote.getAllTopicRecord(employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}

	/**
	 * 批量修改维护主题数据
	 */
	public void saveModifiedTopic() throws JSONException {
		String addStr = request.getParameter("isAdd");
		String updateStr = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		List<Map> addMapList = (List<Map>) JSONUtil.deserialize(addStr);
		List<Map> updateMapList = (List<Map>) JSONUtil.deserialize(updateStr);
		List<BpCItemplanTopic> addList = new ArrayList<BpCItemplanTopic>();
		List<BpCItemplanTopic> updateList = new ArrayList<BpCItemplanTopic>();

		for (Map map : addMapList) {
			BpCItemplanTopic temp = this.parseItemPlanInstance(map);
			addList.add(temp);
		}
		for (Map map : updateMapList) {
			BpCItemplanTopic temp = this.parseItemPlanInstance(map);
			updateList.add(temp);
		}
		if (addList.size() > 0 || updateList.size() > 0 || ids != null) {
			remote.saveModifiedTopic(addList, updateList, ids);
		}
		write("{success:true,msg:'数据保存修改成功！'}");
	}

	/**
	 * 将一映射转化为一主题对象
	 * 
	 * @param map
	 * @return
	 */
	public BpCItemplanTopic parseItemPlanInstance(Map map) {
		BpCItemplanTopic temp = new BpCItemplanTopic();

		Long topicId = null;
		String topicName = null;
		String topicMemo = null;
		Long displayNo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();

		if (map.get("topicId") != null)
			topicId = Long.parseLong(map.get("topicId").toString());
		if (map.get("topicName") != null)
			topicName = map.get("topicName").toString();
		if (map.get("topicMemo") != null)
			topicMemo = map.get("topicMemo").toString();
		if (map.get("displayNo") != null)
			displayNo = Long.parseLong(map.get("displayNo").toString());

		temp.setTopicId(topicId);
		temp.setTopicName(topicName);
		temp.setTopicMemo(topicMemo);
		temp.setDisplayNo(displayNo);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		return temp;
	}

	/**
	 * 通过主题id取得该主题下的所有指标,只为维护用
	 * 
	 * @throws JSONException
	 */
	public void getItemByTopic() throws JSONException {
		String topic = request.getParameter("topicId");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = null;
		if (start != null && limit != null)
			pg = remote.getItemByTopic(Long.parseLong(topic), employee
					.getEnterpriseCode(), Integer.parseInt(start), Integer
					.parseInt(limit));
		else
			pg = remote.getItemByTopic(Long.parseLong(topic), employee
					.getEnterpriseCode());
		write(JSONUtil.serialize(pg));

	}

	/**
	 * 批量修改维护指标数据
	 * 
	 * @throws JSONException
	 */
	public void saveModifiedItem() throws JSONException {
		String addStr = request.getParameter("isAdd");
		String updateStr = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		List<Map> addMapList = (List<Map>) JSONUtil.deserialize(addStr);
		List<Map> updateMapList = (List<Map>) JSONUtil.deserialize(updateStr);
		List<BpCItemplanEcoItem> addList = new ArrayList<BpCItemplanEcoItem>();
		List<BpCItemplanEcoItem> updateList = new ArrayList<BpCItemplanEcoItem>();

		for (Map map : addMapList) {
			BpCItemplanEcoItem temp = this.parseEcoItemInstance(map);
			addList.add(temp);
		}
		for (Map map : updateMapList) {
			BpCItemplanEcoItem temp = this.parseEcoItemInstance(map);
			updateList.add(temp);
		}
		if (addList.size() > 0 || updateList.size() > 0 || ids != null) {
			remote.saveModifiedItem(addList, updateList, ids);
		}
		write("{success:true,msg:'数据保存修改成功！'}");

	}

	/**
	 * 将一映射转化为一经济指标
	 * 
	 * @param map
	 * @return
	 */
	public BpCItemplanEcoItem parseEcoItemInstance(Map map) {
		BpCItemplanEcoItem temp = new BpCItemplanEcoItem();
		Long economicItemId = null;
		Long topicId = null;
		String itemCode = null;
		String itemName = null;
		String alias = null;
		Long unitId = null;
		String itemType = null;
		Long displayNo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();

		if (map.get("economicItemId") != null)
			economicItemId = Long.parseLong(map.get("economicItemId")
					.toString());
		if (map.get("topicId") != null)
			topicId = Long.parseLong(map.get("topicId").toString());
		if (map.get("itemCode") != null)
			itemCode = map.get("itemCode").toString();
		if (map.get("itemName") != null)
			itemName = map.get("itemName").toString();
		if (map.get("alias") != null)
			alias = map.get("alias").toString();
		if (map.get("unitId") != null)
			unitId = Long.parseLong(map.get("unitId").toString());
		if (map.get("itemType") != null)
			itemType = map.get("itemType").toString();
		if (map.get("displayNo") != null)
			displayNo = Long.parseLong(map.get("displayNo").toString());

		temp.setEconomicItemId(economicItemId);
		temp.setTopicId(topicId);
		temp.setItemCode(itemCode);
		temp.setItemName(itemName);
		temp.setAlias(alias);
		temp.setUnitId(unitId);
		temp.setItemType(itemType);
		temp.setDisplayNo(displayNo);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		return temp;
	}

	/**
	 * 查询全厂，分部门，计划，实际值，共通方法 返回的数组的取值顺序
	 * 0：主题ID，1：主题名称，2：主题显示顺序，3：经济指标ID，4：指标名称，5：指标别名，
	 * 6：单位Id，7：单位名称，8：分类，9：指标显示顺序，10：指标计划主ID，11：月份，12：计划工作流序号，
	 * 13：计划工作流状态，14：完成情况工作流序号，15：完成情况工作流状态，16：指标计划明细ID，
	 * 17：#11#12计划值，18：#1#2计划值，19：#11#12完成情况，20：#1#2完成情况 topic month planStatus
	 * 计划状态 reported:已上报，approved:已审批 realStatus 实际值状态 reported:已上报，approved:已审批
	 * enterpriseCode rowStartIdxAndCount
	 * 
	 * @throws JSONException
	 */
	public void findItemByCondition() throws JSONException {
		String topicStr = request.getParameter("topic");
		String month = request.getParameter("month");
		String planStatus = request.getParameter("planStatus");
		String realStatus = request.getParameter("realStatus");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// add by liuyi 20100607  插入最近数据 Y:是
		String isInsertData = request.getParameter("isInsertData");
		
		Long topic = null;
		if (topicStr != null && !topicStr.equals(""))
			topic = Long.parseLong(topicStr);
		PageObject pg = null;
		// modified by liuyi 201006070 
//		if (start != null && limit != null)
//			pg = remote.findItemByCondition(topic, month, planStatus,
//					realStatus, employee.getEnterpriseCode(), Integer
//							.parseInt(start), Integer.parseInt(limit));
//		else
//			pg = remote.findItemByCondition(topic, month, planStatus,
//					realStatus, employee.getEnterpriseCode());
		if (start != null && limit != null)
			pg = remote.findItemByCondition(isInsertData,topic, month, planStatus,
					realStatus, employee.getEnterpriseCode(), Integer
							.parseInt(start), Integer.parseInt(limit));
		else
			pg = remote.findItemByCondition(isInsertData,topic, month, planStatus,
					realStatus, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}

	/**
	 * 批量保存全厂指标计划数据
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void saveWholeItemPlanAndActual() throws JSONException {
		String plantMainIdStr = request.getParameter("plantMainId");
		String month = request.getParameter("month");
		String addStr = request.getParameter("isAdd");
		String updateStr = request.getParameter("isUpdate");
		String planed = request.getParameter("planed");
		List<Map> addMapList = (List<Map>) JSONUtil.deserialize(addStr);
		List<Map> updateMapList = (List<Map>) JSONUtil.deserialize(updateStr);
		BpJItemplanPlantMain basic = new BpJItemplanPlantMain();
		if (plantMainIdStr != null && !plantMainIdStr.equals(""))
			// basic.setPlantMainId(Long.parseLong(plantMainIdStr));
			basic = remote.findWholeObject(Long.parseLong(plantMainIdStr));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if (month != null && !month.equals(""))
			try {
				basic.setMonth(sdf.parse(month));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (planed != null && planed.equals("Y")) {
			if (basic.getWorkflowStatusPlan() != null
					&& basic.getWorkflowStatusPlan() == 3l)
				;
			else
				basic.setWorkflowStatusPlan(0L);
		} else {
			if (basic.getWorkflowStatusFact() != null
					&& basic.getWorkflowStatusFact() == 3l)
				;
			else
				basic.setWorkflowStatusFact(0L);
		}
		basic.setIsUse("Y");
		basic.setEnterpriseCode(employee.getEnterpriseCode());
		List<BpJItemplanPlantDetail> addList = new ArrayList<BpJItemplanPlantDetail>();
		List<BpJItemplanPlantDetail> updateList = new ArrayList<BpJItemplanPlantDetail>();
		for (Map map : addMapList) {
			BpJItemplanPlantDetail temp = this.parsePlantDetailInstance(map);
			addList.add(temp);
		}
		for (Map map : updateMapList) {
			BpJItemplanPlantDetail temp = this.parsePlantDetailInstance(map);
			updateList.add(temp);
		}
		if (addList.size() > 0 || updateList.size() > 0)
			remote.saveWholeItemPlanAndActual(basic, addList, updateList);
		write("{success:true,msg:'数据保存成功！'}");
	}

	/**
	 * add by liuyi 091217 将以映射转化为一全厂指标计划（明细表）数据 BpJItemplanPlantDetail
	 * 
	 * @param map
	 * @return
	 */
	public BpJItemplanPlantDetail parsePlantDetailInstance(Map map) {
		BpJItemplanPlantDetail temp = new BpJItemplanPlantDetail();
		Long plantDetailId = null;
		Long plantMainId = null;
		Long economicItemId = null;
		String plantPlan1112 = null;
		String plantPlan12 = null;
		String plantFact1112 = null;
		String plantFact12 = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();

		if (map.get("plantDetailId") != null)
			plantDetailId = Long.parseLong(map.get("plantDetailId").toString());
		if (map.get("plantMainId") != null)
			plantMainId = Long.parseLong(map.get("plantMainId").toString());
		if (map.get("economicItemId") != null)
			economicItemId = Long.parseLong(map.get("economicItemId")
					.toString());
		if (map.get("plantPlan1112") != null)
			plantPlan1112 = map.get("plantPlan1112").toString();
		if (map.get("plantPlan12") != null)
			plantPlan12 = map.get("plantPlan12").toString();
		if (map.get("plantFact1112") != null)
			plantFact1112 = map.get("plantFact1112").toString();
		if (map.get("plantFact12") != null)
			plantFact12 = map.get("plantFact12").toString();

		temp.setPlantDetailId(plantDetailId);
		temp.setPlantMainId(plantMainId);
		temp.setEconomicItemId(economicItemId);
		temp.setPlantPlan1112(plantPlan1112);
		temp.setPlantPlan12(plantPlan12);
		temp.setPlantFact1112(plantFact1112);
		temp.setPlantFact12(plantFact12);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		return temp;
	}

	/**
	 * 批量保存各部门指标计划
	 * 
	 * @throws JSONException
	 */
	public void savePartItemPlanAndActual() throws JSONException {
		String depMainIdStr = request.getParameter("depMainId");
		String month = request.getParameter("month");
		String addStr = request.getParameter("isAdd");
		String updateStr = request.getParameter("isUpdate");
		String planed = request.getParameter("planed");
		List<Map> addMapList = (List<Map>) JSONUtil.deserialize(addStr);
		List<Map> updateMapList = (List<Map>) JSONUtil.deserialize(updateStr);
		BpJItemplanDepMain basic = new BpJItemplanDepMain();
		if (depMainIdStr != null && !depMainIdStr.equals(""))
			// basic.setDepMainId(Long.parseLong(depMainIdStr));
			basic = remote.findPartObject(Long.parseLong(depMainIdStr));
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		if (month != null && !month.equals(""))
			try {
				basic.setMonth(sdf.parse(month));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (planed != null && planed.equals("Y")) {
			if (basic.getWorkflowStatusPlan() != null
					&& basic.getWorkflowStatusPlan() == 3l)
				;
			else
				basic.setWorkflowStatusPlan(0L);
		} else {
			if (basic.getWorkflowStatusFact() != null
					&& basic.getWorkflowStatusFact() == 3l)
				;
			else
				basic.setWorkflowStatusFact(0L);
		}
		basic.setIsUse("Y");
		basic.setEnterpriseCode(employee.getEnterpriseCode());
		List<BpJItemplanDepDetail> addList = new ArrayList<BpJItemplanDepDetail>();
		List<BpJItemplanDepDetail> updateList = new ArrayList<BpJItemplanDepDetail>();
		for (Map map : addMapList) {
			BpJItemplanDepDetail temp = this.parsePartDepInstance(map);
			addList.add(temp);
		}
		for (Map map : updateMapList) {
			BpJItemplanDepDetail temp = this.parsePartDepInstance(map);
			updateList.add(temp);
		}
		if (addList.size() > 0 || updateList.size() > 0)
			remote.savePartItemPlanAndActual(basic, addList, updateList);
		write("{success:true,msg:'数据保存成功！'}");
	}

	/**
	 * add by liuyi 091217 将以映射转化为一分部门指标计划（明细表）数据 BpJItemplanDepDetail
	 * 
	 * @param map
	 * @return
	 */
	public BpJItemplanDepDetail parsePartDepInstance(Map map) {
		BpJItemplanDepDetail temp = new BpJItemplanDepDetail();
		Long depDetailId = null;
		Long depMainId = null;
		Long economicItemId = null;
		String depPlan1112 = null;
		String depPlan12 = null;
		String depFact1112 = null;
		String depFact12 = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();

		if (map.get("depDetailId") != null)
			depDetailId = Long.parseLong(map.get("depDetailId").toString());
		if (map.get("depMainId") != null)
			depMainId = Long.parseLong(map.get("depMainId").toString());
		if (map.get("economicItemId") != null)
			economicItemId = Long.parseLong(map.get("economicItemId")
					.toString());
		if (map.get("depPlan1112") != null)
			depPlan1112 = map.get("depPlan1112").toString();
		if (map.get("depPlan12") != null)
			depPlan12 = map.get("depPlan12").toString();
		if (map.get("depFact1112") != null)
			depFact1112 = map.get("depFact1112").toString();
		if (map.get("depFact12") != null)
			depFact12 = map.get("depFact12").toString();

		temp.setDepDetailId(depDetailId);
		temp.setDepMainId(depMainId);
		temp.setEconomicItemId(economicItemId);
		temp.setDepPlan1112(depPlan1112);
		temp.setDepPlan12(depPlan12);
		temp.setDepFact1112(depFact1112);
		temp.setDepFact12(depFact12);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		return temp;
	}

	/**
	 * 初始化各部门
	 * 
	 * @throws JSONException
	 */
	public void judgeApproveDept() throws JSONException {
		String planMainId = request.getParameter("planMainId");
		// 部门计划
		String entryIds1;
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		entryIds1 = workflowService.getAvailableWorkflow(
				new String[] { "bqPartItemPlan" }, employee.getWorkerCode());
		// 部门实际
		String entryIds2;
		WorkflowService workflowService1 = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		entryIds2 = workflowService1.getAvailableWorkflow(
				new String[] { "bqPartItemFact" }, employee.getWorkerCode());
		List list = remote.judgeApprovePlan(planMainId, entryIds1, entryIds2);
		write(JSONUtil.serialize(list));

	}

	/**
	 * 初始化各全厂
	 * 
	 * @throws JSONException
	 */
	public void judgeApprovePlant() throws JSONException {
		String planMainId = request.getParameter("factMainId");
		// 全厂计划
		String entryIds1;
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		entryIds1 = workflowService.getAvailableWorkflow(
				new String[] { "bqWholeItemPlan" }, employee.getWorkerCode());
		// 全厂实际
		String entryIds2;
		WorkflowService workflowService1 = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		entryIds2 = workflowService1.getAvailableWorkflow(
				new String[] { "bqWholeItemFact" }, employee.getWorkerCode());
		List list = remote.judgeApproveFact(planMainId, entryIds1, entryIds2);
		write(JSONUtil.serialize(list));

	}
}