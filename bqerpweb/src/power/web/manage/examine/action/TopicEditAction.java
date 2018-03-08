package power.web.manage.examine.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;
import power.ejb.manage.exam.BpCCbmAffiliated;
import power.ejb.manage.exam.BpCCbmAffiliatedFacadeRemote;
import power.ejb.manage.exam.BpCCbmItem;
import power.ejb.manage.exam.BpCCbmItemFacadeRemote;
import power.ejb.manage.exam.BpCCbmOverheadItem;
import power.ejb.manage.exam.BpJCbmDepSeasonFacadeRemote;
import power.ejb.manage.exam.BpJCbmExecutionFacadeRemote;
import power.ejb.manage.exam.form.BpJCbmExecutionForm;
import power.ejb.manage.system.BpCMeasureUnit;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class TopicEditAction extends AbstractAction {

	// private BpCCbmTopicFacadeRemote remoteT;
	private BpCCbmItemFacadeRemote remoteI;
	// private BpCCbmRelationFacadeRemote remoteR;
	private BpCCbmAffiliatedFacadeRemote remoteS;
	private BpJCbmExecutionFacadeRemote remoteE;
	// private BpCCbmDepFacadeRemote remoteD;
	// private BpCCbmOverheadItemFacadeRemote remoteO;
	private BpJCbmDepSeasonFacadeRemote remoteDS;
	private int start;
	private int limit;
	private String topicId;
	private String itemCode;
	private String method;
	private String itemId;
	private BpCCbmItem bpItem;
	private BpCCbmAffiliated afInfo;
	private String searchKey;
	private String type;
	private String datetime;

	public TopicEditAction() {
		remoteI = (BpCCbmItemFacadeRemote) factory
				.getFacadeRemote("BpCCbmItemFacade");
		remoteS = (BpCCbmAffiliatedFacadeRemote) factory
				.getFacadeRemote("BpCCbmAffiliatedFacade");
		remoteE = (BpJCbmExecutionFacadeRemote) factory
				.getFacadeRemote("BpJCbmExecutionFacade");
		remoteDS = (BpJCbmDepSeasonFacadeRemote) factory
				.getFacadeRemote("BpJCbmDepSeasonFacade");
	}

	/**
	 * 取TOPIC下指标
	 * 
	 * @throws JSONException
	 */
	public void getExamItemList() throws JSONException {
		PageObject obj = remoteI.findAllForTopic(employee.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 根据id获取指标详细信息
	 * 
	 * @throws JSONException
	 */
	public void findItemTree() {
		String pid = request.getParameter("pid");
		String searchKey = request.getParameter("searchKey");
		try {
			List<TreeNode> list = remoteI.findStatTreeList(pid, employee
					.getEnterpriseCode(), searchKey);
			write(JSONUtil.serialize(list));
		} catch (Exception exc) {
			exc.printStackTrace();
			write("[]");
		}
	}

	public void findItemInfo() throws JSONException {
		Object o = remoteI.findItemInfo(itemCode);
		write(JSONUtil.serialize(o));
	}

	/**
	 * 根据录入报表编码查找列表信息
	 * 
	 * @throws Exception
	 */
	public void findItemList() throws Exception {
		PageObject obj = remoteI.findAllItemToSelect(searchKey, employee
				.getEnterpriseCode(), start, limit);
		if (obj != null) {
			write(JSONUtil.serialize(obj));
		} else {
			write("{totalCount : 0,list : []}");
		}
	}

	public void saveExamItem() throws JSONException {
		String isItem = request.getParameter("isItem");
		String itemName = request.getParameter("itemName");
		String displayNo = request.getParameter("displayNo");
		String itemId = request.getParameter("id");
		if (isItem.equals("N")) {
			if (method.equals("add")) {
				BpCCbmItem model = new BpCCbmItem();
				model.setItemName(itemName);
				if (displayNo != null && !displayNo.trim().equals(""))
					model.setDisplayNo(Long.parseLong(displayNo));
				else
					model.setDisplayNo(null);
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setFItemId(Long.parseLong(request
						.getParameter("bpItem.FItemId")));
				model.setIsItem("N");
				model.setIsUse("Y");
				remoteI.save(model);

			} else {
				BpCCbmItem model = new BpCCbmItem();
				model = remoteI.findById(Long.parseLong(itemId));
				model.setItemName(itemName);
				if (displayNo != null && !displayNo.trim().equals(""))
					model.setDisplayNo(Long.parseLong(displayNo));
				else
					model.setDisplayNo(null);
				remoteI.update(model);
			}
		} else {
			String unitId = request.getParameter("bpItem.unitCode");
			if (method.equals("update")) {
				if (remoteI.namePermitted(bpItem.getItemName(), bpItem
						.getItemId())) {
					write("{success:false,msg:'指标名称不能重复！'}");
					return;
				}
				BpCCbmItem model = new BpCCbmItem();
				model = remoteI.findById(bpItem.getItemId());
				if (bpItem.getAccountOrder() != null)
					model.setAccountOrder(bpItem.getAccountOrder());
				model.setAlias(bpItem.getAlias());
//				model.setIfBranchItem(bpItem.getIfBranchItem());//modify by drdu 091201
				model.setDataType(request.getParameter("bpItem.dataType"));
				if (bpItem.getDisplayNo() != null)
					model.setDisplayNo(bpItem.getDisplayNo());
				else
					model.setDisplayNo(null);
				model.setEnterpriseCode(employee.getEnterpriseCode());
				// if (bpItem.getFItemId() != null)
				// model.setFItemId(bpItem.getFItemId());
				model.setIsItem(bpItem.getIsItem());
				// add by liuyi 091207 增加考核主题
				model.setTopicId(bpItem.getTopicId());
				model.setIsUse("Y");
				model.setItemCode(bpItem.getItemCode());
				model.setItemName(bpItem.getItemName());
				if (unitId != null)
					model.setUnitId(Long.parseLong(unitId));
				else
					model.setUnitId(null);
				if (remoteI.update(model))
					write("{success:true,msg:'更 新 成 功 ！'}");
				else
					write("{success:false,msg:'更 新 失 败 ！'}");
			} else if (method.equals("add")) {

				if (remoteI.namePermitted(bpItem.getItemName())) {
					write("{success:false,msg:'指标名称不能重复！'}");
					return;
				}
				bpItem.setEnterpriseCode(employee.getEnterpriseCode());
				bpItem.setUnitId(Long.parseLong(unitId));
				bpItem.setDataType(request.getParameter("bpItem.dataType"));
				bpItem.setAccountOrder(Long.parseLong("0"));
				if (bpItem.getDisplayNo() != null)
					bpItem.setDisplayNo(bpItem.getDisplayNo());
				else
					bpItem.setDisplayNo(null);
				if (remoteI.save(bpItem))
					write("{success:true,msg:'添 加 成 功 ！'}");
				else
					write("{success:false,msg:'添 加 失 败 ！'}");
			} else
				write("{success:false,msg:'未指定的操作！'}");
		}
	}

	public void deleteExamItem() {
		String id = request.getParameter("id");
		try {
			if (id != null)
				if (remoteI.delete(id))
					write("{success:true,msg:'操 作 成 功 ！'}");
				else
					write("{success:false,msg:'操 作 失 败 ！'}");
		} catch (RuntimeException e) {
			write("{success:false,msg:'操 作 失 败 ！'}");
		}
	}

	/**
	 * 挂靠标准有关的操作
	 * 
	 * @return
	 * @throws Exception
	 */
	public void getStandardList() throws Exception {
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		int start = 0, limit = 10000;
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(objstart.toString());
			limit = Integer.parseInt(objlimit.toString());
		}
		obj = remoteS.findStandardList(employee.getEnterpriseCode(), start,
				limit);
		System.out.print(JSONUtil.serialize(obj));
		write(JSONUtil.serialize(obj));
	}

	public void getStandardInfo() throws JSONException {
		String aID = request.getParameter("aid");
		Object o = remoteS.findStandardInfo(aID);
		write(JSONUtil.serialize(o));
	}

	public void saveStandard() {
		try {
			String affiliatedId = request.getParameter("id");
			String itemId = request.getParameter("itemId");
			String depCode = request.getParameter("deptcode");
			if (method.equals("add")) {
				afInfo.setItemId(Long.parseLong(itemId));
				afInfo.setDepCode(depCode);
				afInfo.setEnterpriseCode(employee.getEnterpriseCode());
				if (remoteS.save(afInfo))
					write("{success:true}");
				else
					write("{success:false,msg : '该挂靠级别已经存在'}");
			} else {
				BpCCbmAffiliated model = new BpCCbmAffiliated();
				model = remoteS.findById(Long.parseLong(affiliatedId));
				if (itemId != null)
					model.setItemId(Long.parseLong(itemId));
				else
					model.setItemId(null);
				if (depCode != null && !depCode.trim().equals(""))
					model.setDepCode(depCode);
				else
					model.setDepCode(null);
				model.setAffiliatedLevel(afInfo.getAffiliatedLevel());
				model.setAffiliatedValue(afInfo.getAffiliatedValue());
				model.setViewNo(afInfo.getViewNo());
				model.setEnterpriseCode(employee.getEnterpriseCode());
				model.setIsUse("Y");
				if (remoteS.update(model))
					write("{success:true}");
				else
					write("{success:false,msg :'该挂靠级别已经存在'}");
			}
		} catch (RuntimeException e) {
			write("{success:false}");
		}
	}

	public void deleteStandard() {
		String affiliatedId = request.getParameter("affiliatedId");
		if (affiliatedId != null && !affiliatedId.trim().equals("")) {
			if (remoteS.delete(affiliatedId))
				write("{success:true}");
			else
				write("{success:false}");
		}
	}

	/**
	 * 数据录入
	 * 
	 * @return
	 * @throws JSONException
	 */
	public void getExecutionTable() throws JSONException {
		PageObject obj = new PageObject();
		obj = remoteE.getExecutionValueList(type, datetime, employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 载入年度数据
	 * 
	 * @author ywliu 20091119
	 * @return
	 * @throws JSONException
	 */
	public void getYearExecutionTableByItemId() throws JSONException {
		PageObject obj = new PageObject();
		String itemId = request.getParameter("itemId");
		obj = remoteE.getYearExecutionValueListByItemId(itemId, datetime,
				employee.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 年度计划执行情况查询
	 * 
	 * @author ywliu 20091120
	 * @return
	 * @throws JSONException
	 */
	public void getYearExecutionTable() throws JSONException {
		PageObject obj = new PageObject();
		obj = remoteE.getYearExecutionValueList(datetime, employee
				.getEnterpriseCode());
		write(JSONUtil.serialize(obj));
	}

	@SuppressWarnings("unchecked")
	public void saveExecutionTable() throws JSONException {
		if ("plan".equals(type)) {
			if (!remoteE.ifAllowSaveExecutionTable(
					employee.getEnterpriseCode(), datetime)) {
				write("{success:false,msg:'数据已发布，不允许更改 ！'}");
				return;
			}
		}
		try {
			String str = request.getParameter("addOrUpdateRecords");
			Object obj = JSONUtil.deserialize(str);
			List<Map> list = (List<Map>) obj;
			List<BpJCbmExecutionForm> postlist = new ArrayList<BpJCbmExecutionForm>();
			for (Map data : list) {
				BpJCbmExecutionForm model = new BpJCbmExecutionForm();
				if (data.get("executionid1") != null)
					if (!("").equals(data.get("executionid1").toString()))
						model.setExecutionid1(data.get("executionid1")
								.toString());
				if (data.get("executionid2") != null)
					if (!("").equals(data.get("executionid2").toString()))
						model.setExecutionid2(data.get("executionid2")
								.toString());
				if (data.get("executionid3") != null)
					if (!("").equals(data.get("executionid3").toString()))
						model.setExecutionid3(data.get("executionid3")
								.toString());
				if (data.get("itemid") != null)
					model.setItemid(data.get("itemid").toString());
				if (data.get("itemcode") != null)
					model.setItemcode(data.get("itemcode").toString());
				if (data.get("itemname") != null)
					model.setItemname(data.get("itemname").toString());
				if (data.get("unitname") != null)
					model.setUnitname(data.get("unitname").toString());
				if (data.get("value1") != null)
					model.setValue1(data.get("value1").toString());
				if (data.get("value2") != null)
					model.setValue2(data.get("value2").toString());
				if (data.get("value3") != null)
					model.setValue3(data.get("value3").toString());
				postlist.add(model);
			}
			remoteE.saveExecutionValueList(type, datetime, postlist, "hfdc");
			write("{success:true,msg:'操 作 成 功 ！'}");
		} catch (RuntimeException e) {
			write("{success:false,msg:'操 作 失 败 ！'}");
		}
	}

	public void issueExecutionTable() {
		try {
			remoteE.issueExecutionTable("hfdc", datetime);
			write("{success:true,msg:'操 作 成 功 ！'}");
		} catch (Exception e) {
			write("{success:false,msg:'操 作 失 败 ！'}");
		}
	}

	/**
	 * @author ywliu 20091119
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void saveYearExecutionTable() throws JSONException {
		try {
			String str = request.getParameter("addOrUpdateRecords");
			Object obj = JSONUtil.deserialize(str);
			List<Map> list = (List<Map>) obj;
			List<BpJCbmExecutionForm> postlist = new ArrayList<BpJCbmExecutionForm>();
			for (Map data : list) {
				BpJCbmExecutionForm model = new BpJCbmExecutionForm();
				if (data.get("executionid1") != null)
					if (!("").equals(data.get("executionid1").toString()))
						model.setExecutionid1(data.get("executionid1")
								.toString());
				if (data.get("executionid2") != null)
					if (!("").equals(data.get("executionid2").toString()))
						model.setExecutionid2(data.get("executionid2")
								.toString());
				if (data.get("executionid3") != null)
					if (!("").equals(data.get("executionid3").toString()))
						model.setExecutionid3(data.get("executionid3")
								.toString());
				if (data.get("itemid") != null)
					model.setItemid(data.get("itemid").toString());
				if (data.get("itemcode") != null)
					model.setItemcode(data.get("itemcode").toString());
				if (data.get("itemname") != null)
					model.setItemname(data.get("itemname").toString());
				if (data.get("unitname") != null)
					model.setUnitname(data.get("unitname").toString());
				if (data.get("value1") != null)
					model.setValue1(data.get("value1").toString());
				if (data.get("value2") != null)
					model.setValue2(data.get("value2").toString());
				if (data.get("value3") != null)
					model.setValue3(data.get("value3").toString());
				if (data.get("dateTime") != null)
					model.setDateTime(data.get("dateTime").toString());
				postlist.add(model);
			}
			remoteE.saveYearExecutionValueList(type, datetime, postlist,
					employee.getEnterpriseCode());
			write("{success:true,msg:'操 作 成 功 ！'}");
		} catch (RuntimeException e) {
			write("{success:false,msg:'操 作 失 败 ！'}");
		}
	}

	private List<BpCCbmOverheadItem> getUpdateList(String jsonStr) {
		List<BpCCbmOverheadItem> list = new ArrayList<BpCCbmOverheadItem>();
		try {
			Object object = JSONUtil.deserialize(jsonStr);
			List<Map> lm = (List<Map>) object;
			for (Map m : lm) {
				BpCCbmOverheadItem b = new BpCCbmOverheadItem();
				b.setEnterpriseCode(employee.getEnterpriseCode());
				b.setIsUse("Y");
				if (m.get("itemCode") != null)
					b.setItemCode(m.get("itemCode").toString());
				if (m.get("itemName") != null)
					b.setItemName(m.get("itemName").toString());
				if (m.get("unitId") != null
						&& !m.get("unitId").toString().trim().equals("")) {
					BpCMeasureUnit unit = new BpCMeasureUnit();
					Long.parseLong(m.get("unitId").toString());
					b.setUnit(unit);
				}
				list.add(b);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return list;
	}

	// get&set
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

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public BpCCbmItem getBpItem() {
		return bpItem;
	}

	public void setBpItem(BpCCbmItem bpItem) {
		this.bpItem = bpItem;
	}

	public String getSearchKey() {
		return searchKey;
	}

	public void setSearchKey(String searchKey) {
		this.searchKey = searchKey;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}

	public BpCCbmAffiliated getAfInfo() {
		return afInfo;
	}

	public void setAfInfo(BpCCbmAffiliated afInfo) {
		this.afInfo = afInfo;
	}
}
