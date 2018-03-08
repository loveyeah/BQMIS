/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.issue.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.resource.InvCLocation;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.InvCTransaction;
import power.ejb.resource.InvCWarehouse;
import power.ejb.resource.InvCWarehouseFacadeRemote;
import power.ejb.resource.InvJIssueDetails;
import power.ejb.resource.InvJIssueDetailsFacadeRemote;
import power.ejb.resource.InvJIssueHead;
import power.ejb.resource.InvJIssueHeadFacadeRemote;
import power.ejb.resource.InvJLocation;
import power.ejb.resource.InvJLocationFacadeRemote;
import power.ejb.resource.InvJLot;
import power.ejb.resource.InvJLotFacadeRemote;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.InvJWarehouseFacadeRemote;
import power.ejb.resource.MrpJPlanRequirementDetail;
import power.ejb.resource.MrpJPlanRequirementDetailFacadeRemote;
import power.ejb.resource.business.IssueManage;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 出库管理
 * 
 * @author zhangqi
 */
public class StorageIssueManageAction extends AbstractAction {
	/** serial id */
	private static final long serialVersionUID = 1L;
	/** 出库服务 */
	private IssueManage issueRemote;
	/** 领料单表头服务 */
	private InvJIssueHeadFacadeRemote issueHeadRemote;
	/** 领料单明细服务 */
	private InvJIssueDetailsFacadeRemote issueDetailRemote;
	/** 物资需求计划明细 */
	private MrpJPlanRequirementDetailFacadeRemote planDetailRemote;
	/** 批号记录 */
	private InvJLotFacadeRemote lotRemote;
	/** 库存物料记录 */
	private InvJWarehouseFacadeRemote wareRemote;
	/** 库位物料记录 */
	private InvJLocationFacadeRemote locationRemote;
	/** 共通 */
	private BaseDataManager baseRemote;

	/** 仓库 */
	private InvCWarehouseFacadeRemote warehouseRemote;
	private static final String MESSAGE_ROLLBACK = "'操作数据库过程中异常终了。'";
	private static final String MESSAGE_LOCK = "'他人使用中。'";
	private static final String ISSUE_TYPE_PLAN = "1";
	/** 最后出库的批号记录的出库数 */
	private double lastNum = 0;
	/** 事务作用 */
	private InvCTransaction trans = null;
	/** 备注 */
	private String strMemo;

	/** add by fyyang 090708 */
	private InvCMaterialFacadeRemote materialRemote;
	/**
	 * 构造函数
	 */
	public StorageIssueManageAction() {
		// 初始化
		issueRemote = (IssueManage) factory.getFacadeRemote("IssueManageImpl");
		// 表头
		issueHeadRemote = (InvJIssueHeadFacadeRemote) factory
				.getFacadeRemote("InvJIssueHeadFacade");
		// 明细
		issueDetailRemote = (InvJIssueDetailsFacadeRemote) factory
				.getFacadeRemote("InvJIssueDetailsFacade");
		// 需求计划明细
		planDetailRemote = (MrpJPlanRequirementDetailFacadeRemote) factory
				.getFacadeRemote("MrpJPlanRequirementDetailFacade");
		// 批号记录
		lotRemote = (InvJLotFacadeRemote) factory
				.getFacadeRemote("InvJLotFacade");
		// 库存物料记录
		wareRemote = (InvJWarehouseFacadeRemote) factory
				.getFacadeRemote("InvJWarehouseFacade");
		// 库位物料记录
		locationRemote = (InvJLocationFacadeRemote) factory
				.getFacadeRemote("InvJLocationFacade");
		// 仓库主文件
		warehouseRemote = (InvCWarehouseFacadeRemote) factory
				.getFacadeRemote("InvCWarehouseFacade");
		baseRemote = (BaseDataManager) factory
				.getFacadeRemote("BaseDataManagerImpl");
		
		materialRemote=(InvCMaterialFacadeRemote)factory.getFacadeRemote("InvCMaterialFacade");

	}

	/**
	 * 系统配置是否有计划
	 */
	public void isIssueTypePlan() {
		// 出库类型
		String issueType = baseRemote.getIssueType();
		if (ISSUE_TYPE_PLAN.equals(issueType)) {
			write("true");
		} else {
			write("false");
		}
	}

	/**
	 * 查询领料单数据
	 * 
	 * @throws JSONException
	 */
	public void getIssueHeadDatas() throws JSONException {
		String applyName=request.getParameter("applyName");
		// 领料单编号
		String issueNo = request.getParameter("issueNo");
		// 开始行号
		String materailName = request.getParameter("materail_name");
		// 物资编码
		String strStart = request.getParameter("start");
		// 行数
		String strlimit = request.getParameter("limit");
		int start = 0;
		int limit = 0;
		if (strStart != null && strStart.length() > 0) {
			start = Integer.parseInt(strStart);
		}
		if (strlimit != null && strlimit.length() > 0) {
			limit = Integer.parseInt(strlimit);
		}
		PageObject obj = new PageObject();
		//modify by fyyang 091204
//		// 没有输入领料单编号，查询所有
//		if (issueNo == null || issueNo.length() < 1) {
//			obj = issueRemote.getAllIssueHeadDatas(
//					employee.getEnterpriseCode(), start, limit);
//		} else {
			// 查询相应领料单编号的数据
			obj = issueRemote.getIssueHeadDatasByNo(employee
					.getEnterpriseCode(), issueNo, applyName,materailName,start, limit);
//		}
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 获取登录用户
	 */
	public void getLogonWorkerCode() {
		// 为空则返回
		if (employee == null) {
			write(Constants.BLANK_STRING);
			return;
		}
		// 返回登录用户的名字
		write(employee.getWorkerName());
	}

	/**
	 * 获取保管员信息
	 */
	public void getIssueSaveMan() {
		// 仓库编码
		String whsNo = request.getParameter("whsNo");
		// 查询仓库信息
		InvCWarehouse entity = warehouseRemote.findByWhsNo(employee
				.getEnterpriseCode(), whsNo);
		// 联系人存在
		if (entity != null && entity.getContactMan() != null) {
			Employee person = baseRemote
					.getEmployeeInfo(entity.getContactMan());
			// 返回联系人
			if (person != null) {
				write(person.getWorkerName());
			} else {
				write("");
			}
		}
		write("");
	}

	/**
	 * 获取物料详细信息
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getMaterialDetailDatas() throws JSONException {
		// 领料单编号
		String strIssueHeadId = request.getParameter("issueHeadId");
		// 开始行号
		String strStart = request.getParameter("start");
		// 行数
		String strlimit = request.getParameter("limit");
		int start = 0;
		int limit = 0;
		if (strStart != null && strStart.length() > 0) {
			start = Integer.parseInt(strStart);
		}
		if (strlimit != null && strlimit.length() > 0) {
			limit = Integer.parseInt(strlimit);
		}
		PageObject obj = new PageObject();
		// 查询物料详细信息的数据
		// obj =
		// issueRemote.getMaterialDetailDatas(employee.getEnterpriseCode(),
		// Long.parseLong(strIssueHeadId), start, limit);
		// 5/6/09 不分页，不设置限制条件 yiliu
		obj = issueRemote.getMaterialDetailDatas(employee.getEnterpriseCode(),
				Long.parseLong(strIssueHeadId));

		write(JSONUtil.serialize(obj));
	}

	/**
	 * 获取批号数据
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getLotsByMaterialid() throws JSONException {
		// 物料id
		String strMaterialId = request.getParameter("materialId");
		// 获取检索值
		PageObject obj = issueRemote.getLots(employee.getEnterpriseCode(), Long
				.parseLong(strMaterialId));
		List<String> lst = obj.getList();
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		int i = 0;
		// 如果批号不为‘0’，添加一个空项，允许前台不做选择
		if (lst != null && lst.size() > 0 && !"0".equals(lst.get(0))) {
			sb.append("{\"lotNo\":\"\"},");
		}
		for (String lotNo : lst) {
			i++;
			sb.append("{\"").append("lotNo").append("\":\"").append(lotNo)
					.append("\"}");
			if (i < lst.size()) {
				sb.append(",");
			}
		}
		sb.append("]");
		write(sb.toString());
	}

	/**
	 * 通过批号获得仓库数据
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getWhsByLots() throws JSONException {
		// 物料id
		String strMaterialId = request.getParameter("materialId");
		// 批号
		String lotNo = request.getParameter("lotNo");
		// 检索
		PageObject obj = issueRemote.getWareHouseByLotNo(employee
				.getEnterpriseCode(), lotNo, Long.parseLong(strMaterialId));
		if (obj.getList().size() > 0) {
			// 实例
			InvCWarehouse entity = new InvCWarehouse();
			entity.setWhsNo("");
			entity.setWhsName("");
			obj.getList().add(0, entity);
		}
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 通过批号和仓库获得库位数据
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getLocationByWhsAndLots() throws JSONException {
		// 物料id
		String strMaterialId = request.getParameter("materialId");
		// 批号
		String lotNo = request.getParameter("lotNo");
		// 仓库号
		String whsNo = request.getParameter("whsNo");
		// 检索
		PageObject obj = issueRemote.getLocationByLotNoAndWhsNo(employee
				.getEnterpriseCode(), lotNo, whsNo, Long
				.parseLong(strMaterialId));
		if (obj.getList().size() > 0) {
			// 实例
			InvCLocation entity = new InvCLocation();
			entity.setLocationNo("");
			entity.setLocationName("");
			obj.getList().add(0, entity);
		}
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 判断是否正在结帐
	 */
	public void isIssueBalanceNow() {
		// 事务编码
		String transCode = request.getParameter("transCode");
		// 判断是否正在结帐
		boolean result = issueRemote.isBalanceNow(transCode);
		if (result) {
			write("true");
		} else {
			write("false");
		}
	}

	/**
	 * 获取库存物料数量
	 */
	public void getStorageMaterialCounts() {
		// 批号
		String lotNo = request.getParameter("lotNo");
		// 仓库号
		String whsNo = request.getParameter("whsNo");
		// 库位号
		String locationNo = request.getParameter("locationNo");
		// 物料id
		String strMaterialId = request.getParameter("materialId");
		// 检索
		double count = issueRemote.getMaterialNums(
				employee.getEnterpriseCode(), lotNo, locationNo, whsNo, Long
						.parseLong(strMaterialId));
		write(String.valueOf(count));
	}

	/**
	 * 获取紧急领用领料单所需特定物资的数量
	 */
	public void getEmergencyDemandCounts() {
		// 物料id
		String strMaterialId = request.getParameter("materialId");
		// 检索
		double count = issueRemote.getEmergencyNums(employee
				.getEnterpriseCode(), Long.parseLong(strMaterialId));
		write(String.valueOf(count));
	}

	/**
	 * 获取计划关联时，高于当前领料单优先级的领料单所需物资数量
	 */
	public void getPlanRelateCounts() {
		// 领料单编号
		String issueHeadId = request.getParameter("issueHeadId");
		// 物料id
		String strMaterialId = request.getParameter("materialId");
		// 需求单编号
		String mrNo = request.getParameter("mrNo");
		// 数量
		double count;
		// 计划等级
		Long planGrade;
		// 需求单不存在时，计划等级最低(0)
		if (mrNo == null) {
			planGrade = 0L;
		} else {
			// 检索领料单的计划等级
			planGrade = issueRemote.getIssuePlanGrade(employee
					.getEnterpriseCode(), issueHeadId);
		}
		// 检索
		count = issueRemote.getPlanRelateMaterialCount(employee
				.getEnterpriseCode(), Long.parseLong(strMaterialId), planGrade);
		write(String.valueOf(count));
	}

	/**
	 * db更新操作
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void updateIssueDbTables() throws JSONException {
		// 领料单数据
		String strForm = request.getParameter("form");
		// 领料单实际领用人
		String getRealPerson = request.getParameter("getRealPerson");

		// 物料详细grid数据
		Object objRecords = request.getParameter("records");
		try {
			// json反序列化grid记录
			List<Map> records = (List<Map>) JSONUtil.deserialize(objRecords
					.toString());
			// json反序列化领料单数据
			Map form = (Map) JSONUtil.deserialize(strForm);
			// 要更新的领料单
			InvJIssueHead head = updateIssueHeadRecord(getLong(form
					.get("issueHeadId")), strMemo, getString(form
					.get("lastModifiedDate")));
			if(getRealPerson != null && !"".equals(getRealPerson) ) {
				head.setGetRealPerson(getRealPerson);
			}
			// 要更新的领料单明细表
			List<InvJIssueDetails> details = updateIssueDetails(records);
			// 要更新的物料需求计划明细表
			List<MrpJPlanRequirementDetail> planDetails = updatePlanDetails(records);
			// TODO 工单明细表

			// 更新库存物料记录表、库位物料记录表、批号记录表，插入事务历史表
			// 批号记录表
			List<InvJLot> lots = new ArrayList<InvJLot>();
			List<InvJLot> oneLots = null;
			// 事务历史表
			List<InvJTransactionHis> transHises = new ArrayList<InvJTransactionHis>();
			List<InvJTransactionHis> oneTransHises = null;
			// 库存物料记录
			List<InvJWarehouse> wareHouses = new ArrayList<InvJWarehouse>();
			List<InvJWarehouse> oneWareHouses = null;
			// 库位物料记录
			List<InvJLocation> locations = new ArrayList<InvJLocation>();
			List<InvJLocation> oneLocations = null;
			for (int i = 0; i < records.size(); i++) {
				// 批号记录表
				oneLots = new ArrayList<InvJLot>();
				// 事务历史表
				oneTransHises = new ArrayList<InvJTransactionHis>();
				// 库存物料记录
				oneWareHouses = new ArrayList<InvJWarehouse>();
				// 库位物料记录
				oneLocations = new ArrayList<InvJLocation>();
				updateIssueRelatingTables(records.get(i), form, oneLots,
						oneTransHises, oneWareHouses, oneLocations);
				// 批号记录表
				lots.addAll(oneLots);
				// 事务历史表
				transHises.addAll(oneTransHises);
				// 库存物料记录
				wareHouses.addAll(oneWareHouses);
				// 库位物料记录
				locations.addAll(oneLocations);
			}
			// 去除重复的库位和仓库
			getUpdateLocations(locations);
			getUpdateWareHouses(wareHouses);
			// 更新
			issueRemote.updateIssueDbTables(head, details, planDetails, lots,
					transHises, wareHouses, locations);
			// 以下是出库成功后返回页面出库物资仓库来源信息。 add by ywliu 2009/6/25
			List whsNoList = new ArrayList();
			HashSet whsNoSet = new HashSet();// modify by ywliu 2009/11/05
			if(transHises != null ) {
				Iterator it = transHises.iterator();
				while(it.hasNext()) {
					InvJTransactionHis transHis = (InvJTransactionHis)it.next();
					whsNoSet.add(transHis.getFromWhsNo());// modify by ywliu 2009/11/05
				}
			}
			Iterator whsNoIt = whsNoSet.iterator();
			while(whsNoIt.hasNext()) {
				Object data = (Object)whsNoIt.next();
				whsNoList.add(data.toString());// modify by ywliu 2009/11/05
			}
//			whsNoList.add(whsNoSet);
			
			// modify by ywliu 2009/11/05
//			write("{success:true,msg:'"+JSONUtil.serialize(whsNoList)+"'}");
			write(JSONUtil.serialize(whsNoList));
		} catch (RuntimeException e) {
			write("{success:false,msg:" + MESSAGE_ROLLBACK + "}");
		} catch (Exception e) {
			write("{success:false,msg:" + MESSAGE_LOCK + "}");
		}
	}

	/**
	 * 计算要更新的库存物料记录
	 * 
	 * @param wareHouses
	 *            库存物料记录集
	 */
	private void getUpdateWareHouses(List<InvJWarehouse> wareHouses) {
		InvJWarehouse now = null;
		InvJWarehouse next = null;
		for (int i = 0; i < wareHouses.size(); i++) {
			now = wareHouses.get(i);
			for (int j = i + 1; j < wareHouses.size();) {
				next = wareHouses.get(j);
				if (now.getWarehouseInvId().equals(next.getWarehouseInvId())) {
					now.setIssue(getDoubleQty(now.getIssue())
							+ getDoubleQty(next.getIssue()));
					now.setOpenBalance(getDoubleQty(now.getOpenBalance())
							+ getDoubleQty(next.getOpenBalance()));
					now.setAdjust(getDoubleQty(now.getAdjust())
							+ getDoubleQty(next.getAdjust()));
					now.setReceipt(getDoubleQty(now.getReceipt())
							+ getDoubleQty(next.getReceipt()));
					wareHouses.remove(j);
				} else {
					j++;
				}
			}
		}
		for (InvJWarehouse ware : wareHouses) {
			// 流水号
			now = wareRemote.findById(ware.getWarehouseInvId());
			ware.setIssue(getDoubleQty(now.getIssue())
					+ getDoubleQty(ware.getIssue()));
			ware.setOpenBalance(getDoubleQty(now.getOpenBalance())
					+ getDoubleQty(ware.getOpenBalance()));
			ware.setAdjust(getDoubleQty(now.getAdjust())
					+ getDoubleQty(ware.getAdjust()));
			ware.setReceipt(getDoubleQty(now.getReceipt())
					+ getDoubleQty(ware.getReceipt()));
		}
	}

	/**
	 * 计算要更新的库位物料记录
	 * 
	 * @param locations
	 *            库位物料记录集
	 */
	private void getUpdateLocations(List<InvJLocation> locations) {
		InvJLocation now = null;
		InvJLocation next = null;
		for (int i = 0; i < locations.size(); i++) {
			now = locations.get(i);
			for (int j = i + 1; j < locations.size();) {
				next = locations.get(j);
				if (now.getLocationInvId().equals(next.getLocationInvId())) {
					now.setIssue(getDoubleQty(now.getIssue())
							+ getDoubleQty(next.getIssue()));
					now.setOpenBalance(getDoubleQty(now.getOpenBalance())
							+ getDoubleQty(next.getOpenBalance()));
					now.setAdjust(getDoubleQty(now.getAdjust())
							+ getDoubleQty(next.getAdjust()));
					now.setReceipt(getDoubleQty(now.getReceipt())
							+ getDoubleQty(next.getReceipt()));
					locations.remove(j);
				} else {
					j++;
				}
			}
		}
		for (InvJLocation location : locations) {
			// 流水号
			now = locationRemote.findById(location.getLocationInvId());
			location.setIssue(getDoubleQty(now.getIssue())
					+ getDoubleQty(location.getIssue()));
			location.setOpenBalance(getDoubleQty(now.getOpenBalance())
					+ getDoubleQty(location.getOpenBalance()));
			location.setAdjust(getDoubleQty(now.getAdjust())
					+ getDoubleQty(location.getAdjust()));
			location.setReceipt(getDoubleQty(now.getReceipt())
					+ getDoubleQty(location.getReceipt()));
		}
	}

	/**
	 * 更新领料单表
	 */
	public void updateIssueHead() {
		// 领料单表头流水号
		Long issueHeadId = Long.parseLong(request.getParameter("issueHeadId"));
		// 上次修改时间
		String lastModifiedDate = request.getParameter("lastModifiedDate");
		// 如果时间里有T，去除
		if(lastModifiedDate != null && !"".equals(lastModifiedDate)) {
			lastModifiedDate = lastModifiedDate.replace("T", " ");
		}
		try {
			// entity
			InvJIssueHead entity = updateIssueHeadRecord(issueHeadId, strMemo,
					lastModifiedDate);
			// 更新
			issueHeadRemote.update(entity);
			write("{success:true,msg:''}");
		} catch (RuntimeException e) {
			write("{success:false,msg:" + MESSAGE_ROLLBACK + "}");
		} catch (Exception e) {
			write("{success:false,msg:" + MESSAGE_LOCK + "}");
		}
	}

	/**
	 * 更新领料单表头
	 * 
	 * @param issueHeadId
	 *            流水号
	 * @param memo
	 *            备注
	 * @param lastModifiedDate
	 *            上次修改时间
	 * @throws Exception
	 */
	private InvJIssueHead updateIssueHeadRecord(Long issueHeadId, String memo,
			String lastModifiedDate) throws Exception {
		// entity
		InvJIssueHead head = issueHeadRemote.findById(issueHeadId);
		if (head == null) {
			throw new Exception();
		}
		String dbDate = DateToString(head.getLastModifiedDate());
		// 时间不相同
		if (!dbDate.equals(lastModifiedDate)) {
			throw new Exception();
		}
		// 更新领料单表
		InvJIssueHead entity = issueHeadRemote.findById(issueHeadId);
		// 备注
		entity.setMemo(memo);
		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		return entity;
	}

	/**
	 * 更新库存物料记录表、库位物料记录表、批号记录表，插入事务历史表
	 * 
	 * @param record
	 *            明细记录信息
	 * @param form
	 *            领料单表头信息
	 * @param lots
	 *            批号记录表
	 * @param transHises
	 *            事务历史表
	 * @param wareHouses
	 *            库存物料记录
	 * @param locations
	 *            库位物料记录
	 */
	@SuppressWarnings("unchecked")
	private void updateIssueRelatingTables(Map record, Map form,
			List<InvJLot> lots, List<InvJTransactionHis> transHises,
			List<InvJWarehouse> wareHouses, List<InvJLocation> locations) {
		// 本次出库数
		double thisNum = getDouble(record.get("thisNum")) == null ? 0
				: getDouble(record.get("thisNum"));
		// 没有出库，不需要更新
		if (thisNum == 0) {
			return;
		}
		// 批号
		String lotNo = getString(record.get("lotNo"));
		// 仓库号
		String whsNo = getString(record.get("whsNo"));
		// 库位号
		String locationNo = getString(record.get("locationNo"));
		// 物料id
		Long materialId = getLong(record.get("materialId"));

		// 获得要更新的记录的id
		List<Long> lotInvIds = getLotInvIds(lotNo, whsNo, locationNo,
				materialId, thisNum);
		// 循环更新
		for (int i = 0; i < lotInvIds.size(); i++) {
			// 流水号
			Long lotInvId = lotInvIds.get(i);
			// 对应的记录
			InvJLot entity = lotRemote.findById(lotInvId);
			// 出库数:本期初始+本期接收+本期调整-本期出库
			double issueQty = entity.getOpenBalance() + entity.getReceipt()
					+ entity.getAdjust() - entity.getIssue();
			// 如果是最后一条记录，则出库数从lastnum处获得
			if (i == lotInvIds.size() - 1) {
				issueQty = lastNum;
			}
			// 更新批号记录表
			lots.add(updateInvJLot(entity, issueQty));
			// 插入事务历史表
			transHises.add(addTransHis(entity, form, record, issueQty));
			// 更新库存物料记录
			wareHouses.add(updateWarehouse(entity, issueQty));
			// 库位为空时，不更新库位物料记录
			if (entity.getLocationNo() != null
					&& entity.getLocationNo().length() > 0) {
				// 更新库位物料记录
				locations.add(updateLocatioon(entity, issueQty));
			}
		}
	}

	/**
	 * 更新批号记录表
	 * 
	 * @param entity
	 *            批号记录表中的record
	 * @param issueQty
	 *            出库数量
	 */
	private InvJLot updateInvJLot(InvJLot entity, double issueQty) {
		// 检索事务作用信息
		if (trans == null) {
			findTransInfo();
		}
		if (Constants.FLAG_Y.equals(trans.getIsOpenBalance())) {
			// 本期初始
			entity.setOpenBalance(getDoubleQty(entity.getOpenBalance())
					+ issueQty);
		}
		if (Constants.FLAG_Y.equals(trans.getIsReceive())) {
			// 本期接收
			entity.setReceipt(getDoubleQty(entity.getReceipt()) + issueQty);
		}
		if (Constants.FLAG_Y.equals(trans.getIsAdjust())) {
			// 本期调整
			entity.setAdjust(getDoubleQty(entity.getAdjust()) + issueQty);
		}
		if (Constants.FLAG_Y.equals(trans.getIsIssues())) {
			// 出库数量
			entity.setIssue(getDoubleQty(entity.getIssue()) + issueQty);
		}
		// 上次修改人
		entity.setLastModifiedBy(employee.getWorkerCode());
		// 上次修改时间
		entity.setLastModifiedDate(new Date());
		// lotRemote.update(entity);
		return entity;
	}

	/**
	 * 更新库存物料记录
	 * 
	 * @param entity
	 *            批号记录表中的record
	 * @param issueQty
	 *            出库数量
	 */
	private InvJWarehouse updateWarehouse(InvJLot entity, double issueQty) {
		// 库存物料记录
		List<InvJWarehouse> wares = wareRemote
				.findByWHandMaterial(employee.getEnterpriseCode(), entity
						.getWhsNo(), entity.getMaterialId());
		InvJWarehouse ware = null;
		if (wares.size() > 0) {
			ware = wares.get(0);
		} else {
			throw new RuntimeException();
		}
		// 检索事务作用信息
		if (trans == null) {
			findTransInfo();
		}

		if (Constants.FLAG_Y.equals(trans.getIsOpenBalance())) {
			// 本期初始
			ware.setOpenBalance(issueQty);
		} else {
			// 本期初始
			ware.setOpenBalance(0.0);
		}
		if (Constants.FLAG_Y.equals(trans.getIsReceive())) {
			// 本期接收
			ware.setReceipt(issueQty);
		} else {
			// 本期接收
			ware.setReceipt(0.0);
		}
		if (Constants.FLAG_Y.equals(trans.getIsAdjust())) {
			// 本期调整
			ware.setAdjust(issueQty);
		} else {
			// 本期调整
			ware.setAdjust(0.0);
		}
		if (Constants.FLAG_Y.equals(trans.getIsIssues())) {
			// 出库数量
			ware.setIssue(issueQty);
		} else {
			// 出库数量
			ware.setIssue(0.0);
		}
		// 上次修改者
		ware.setLastModifiedBy(employee.getWorkerCode());
		// 上次修改日期
		ware.setLastModifiedDate(new Date());
		// 更新
		// wareRemote.update(ware);
		return ware;

	}

	/**
	 * 更新库位物料记录
	 * 
	 * @param entity
	 *            批号记录表中的record
	 * @param issueQty
	 *            出库数量
	 */
	private InvJLocation updateLocatioon(InvJLot entity, double issueQty) {
		// 库位物料记录
		List<InvJLocation> locations = locationRemote.findByWHLM(employee
				.getEnterpriseCode(), entity.getWhsNo(),
				entity.getLocationNo(), entity.getMaterialId());
		// 库位
		InvJLocation location = null;
		if (locations.size() > 0) {
			location = locations.get(0);
		} else {
			throw new RuntimeException();
		}
		// 检索事务作用信息
		if (trans == null) {
			findTransInfo();
		}
		if (Constants.FLAG_Y.equals(trans.getIsOpenBalance())) {
			location.setOpenBalance(issueQty);
		} else {
			location.setOpenBalance(0.0);
		}
		if (Constants.FLAG_Y.equals(trans.getIsReceive())) {
			// 本期接收
			location.setReceipt(issueQty);
		} else {
			location.setReceipt(0.0);
		}
		if (Constants.FLAG_Y.equals(trans.getIsAdjust())) {
			// 本期调整
			location.setAdjust(issueQty);
		} else {
			location.setAdjust(0.0);
		}
		if (Constants.FLAG_Y.equals(trans.getIsIssues())) {
			// 出库数量
			location.setIssue(issueQty);
		} else {
			location.setIssue(0.0);
		}
		// 上次修改者
		location.setLastModifiedBy(employee.getWorkerCode());
		// 上次修改日期
		location.setLastModifiedDate(new Date());
		// 更新
		// locationRemote.update(location);
		return location;

	}

	/**
	 * 插入事务历史表
	 * 
	 * @param form
	 *            领料单表头信息
	 * @param lot
	 *            批号记录entity
	 * @param record
	 *            领料单明细记录
	 * @param issueQty
	 *            出库数
	 */
	@SuppressWarnings("unchecked")
	private InvJTransactionHis addTransHis(InvJLot lot, Map form, Map record,
			double issueQty) {
		// 检索事务历史表
		PageObject obj = null;
		// 检索结果
		List<Map> lstTrans = null;
		// 结果
		Map mapTrans = null;
		// 批号为0时不检索事务历史表
		if (!"0".equals(lot.getLotNo())) {
			obj = issueRemote.getTransHis(employee.getEnterpriseCode(), lot
					.getMaterialId(), lot.getLotNo(), lot.getWhsNo(), lot
					.getLocationNo());
			// 检索结果
			lstTrans = obj.getList();
			// 结果
			if (lstTrans.size() > 0) {
				mapTrans = lstTrans.get(0);
			}
		}
		// 检索物料主文件获取标准成本计算方式
		obj = issueRemote.getStdCostRelatingFields(
				employee.getEnterpriseCode(), lot.getMaterialId());
		List<Map> lstStdCost = obj.getList();
		// 结果
		Map mapStdCost = new HashMap();
		if (lstStdCost.size() > 0) {
			mapStdCost = lstStdCost.get(0);
		}

		// 事务历史
		InvJTransactionHis entity = new InvJTransactionHis();
		// 单号->领料单编号
		entity.setOrderNo(getString(form.get("issueNo")));
		// 序号->领料单明细id
		entity.setSequenceId(getLong(record.get("issueDetailsId")));
		// 检索事务作用信息
		if (trans == null) {
			findTransInfo();
		}
		// 事务作用类型id
		entity.setTransId(trans.getTransId());
		// 物料id
		entity.setMaterialId(lot.getMaterialId());
		// 批号
		entity.setLotNo(lot.getLotNo());
		if (mapTrans != null) {
			// 物料单价->事务历史检索结果.单价
			entity.setPrice(getDouble(mapTrans.get("price")));
			// 税率->事务历史检索结果.税率
			entity.setTaxRate(getDouble(mapTrans.get("taxRate")));
			// 供应商id->事务历史检索结果.供应商id
			entity.setSupplier(getLong(mapTrans.get("supplier")));
		}
		// 异动数量->出库数
		entity.setTransQty(issueQty);
		// 企业编码
		entity.setEnterpriseCode(employee.getEnterpriseCode());
		// 领用人->
		entity.setReceiveMan(getString(form.get("receiptBy")));
		// 领用部门
		entity.setReceiveDept(getString(form.get("receiptDep")));
		// 归口人
		entity.setCostMan(getString(form.get("receiptBy")));
		// 归口部门
		entity.setCostDept(getString(form.get("feeByDep")));
		// 计量单位
		entity.setTransUmId(getLong(record.get("stockUmId")));
		// 操作仓库
		entity.setFromWhsNo(lot.getWhsNo());
		// 操作库位
		entity.setFromLocationNo(lot.getLocationNo());
		// 标准成本
		String costMethod = getString(mapStdCost.get("costMethod"));
		Double stdCost = null;
		// 先进先出（批控制）
		if ("2".equals(costMethod)) {
			if (mapTrans != null) {
				stdCost = getDouble(mapTrans.get("stdCost"));
			}
		}// 加权平均
		else if ("1".equals(costMethod)) {
			stdCost = getDouble(mapStdCost.get("stdCost"));

		}// 计划价格
		else if ("3".equals(costMethod)) {
			stdCost = getDouble(mapStdCost.get("frozenCost"));
		} else {
			// 批控制
			if (!"0".equals(lot.getLotNo())) {
				if (mapTrans != null) {
					stdCost = getDouble(mapTrans.get("stdCost"));
				}
			} else {
				stdCost = getDouble(mapStdCost.get("stdCost"));
			}
		}
		entity.setStdCost(stdCost);
		// 备注
		entity.setMemo(getString(record.get("memo")));
		entity.setIsUse("Y");
		entity.setLastModifiedBy(employee.getWorkerCode());
		// add
		// transHisRemote.save(entity);
		return entity;
	}

	/**
	 * 检索批号记录表数据知道满足出库数量
	 * 
	 * @param lotNo
	 *            批号
	 * @param whsNo
	 *            仓库号
	 * @param locationNo
	 *            库位号
	 * @param materialId
	 *            物料id
	 * @param thisNum
	 *            本次出库数量
	 */
	@SuppressWarnings("unchecked")
	private List<Long> getLotInvIds(String lotNo, String whsNo,
			String locationNo, Long materialId, double thisNum) {
		// 流水号集合
		List<Long> lotInvIds = new ArrayList<Long>();
		// 批号未选择
		if (lotNo == null) {
			// 获得所有批号
			PageObject obj = issueRemote.getLots(employee.getEnterpriseCode(),
					materialId);
			List<String> lotNos = obj.getList();
			boolean blnGotIt = false;
			for (String no : lotNos) {
				// 检索该批次库存数
				List<Map> list = issueRemote.getLotIdsAndCounts(
						employee.getEnterpriseCode(), no, null, null,
						materialId).getList();
				// 循环判断是否足够出库
				for (int i = 0; i < list.size(); i++) {
					Map map = list.get(i);
					double issueQty = getDouble(map.get("storages")) == null ? 0
							: getDouble(map.get("storages"));
					if (issueQty <= 0) {
						continue;
					}
					// 判断是否足够出库
					double num = thisNum - issueQty;
					// 足够的情况下
					if (num <= 0) {
						// 出库数
						lastNum = thisNum;
						// 流水号
						lotInvIds.add(getLong(map.get("lotId")));
						blnGotIt = true;
						break;
					}// 不足时
					else {
						thisNum = num;
						// 流水号
						lotInvIds.add(getLong(map.get("lotId")));
					}
				}
				if (blnGotIt) {
					break;
				}
			}
		} else {
			// 检索该批次库存数
			List<Map> list = issueRemote.getLotIdsAndCounts(
					employee.getEnterpriseCode(), lotNo, locationNo, whsNo,
					materialId).getList();
			// 循环判断是否足够出库
			for (int i = 0; i < list.size(); i++) {
				Map map = list.get(i);
				double issueQty = getDouble(map.get("storages")) == null ? 0
						: getDouble(map.get("storages"));
				if (issueQty <= 0) {
					continue;
				}
				// 判断是否足够出库
				double num = thisNum - issueQty;
				// 足够的情况下
				if (num <= 0) {
					// 出库数
					lastNum = thisNum;
					// 流水号
					lotInvIds.add(getLong(map.get("lotId")));
					break;
				}// 不足时
				else {
					thisNum = num;
					// 流水号
					lotInvIds.add(getLong(map.get("lotId")));
				}
			}
		}
		return lotInvIds;
	}

	/**
	 * 更新领料单明细表和物资需求计划明细表
	 * 
	 * @param records
	 *            明细记录集
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private List<InvJIssueDetails> updateIssueDetails(List<Map> records)
			throws Exception {
		// 本次发货数量
		double thisNum;
		// 领料单明细流水号
		Long issueDetailsId;
		// 记录
		Map record;
		// 领料单明细表entity
		InvJIssueDetails entity;
		// 上次修改日期
		String lastModifiedDate;
		// 要更新的领料单明细
		List<InvJIssueDetails> details = new ArrayList<InvJIssueDetails>();
		for (int i = 0; i < records.size(); i++) {
			// 记录
			record = records.get(i);
			// 本次发货数量
			thisNum = getDouble(record.get("thisNum")) == null ? 0
					: getDouble(record.get("thisNum"));
			// 数据没有变更，不更新
			if (thisNum <= 0) {
				continue;
			}
			// 更新领料单明细表

			// 领料单明细流水号
			issueDetailsId = getLong(record.get("issueDetailsId"));
			// entity
			entity = issueDetailRemote.findById(issueDetailsId);
			// 排他
			if (entity == null) {
				throw new Exception();
			}
			lastModifiedDate = getString(record.get("lastModifiedDate"));
			String dbDate = DateToString(entity.getLastModifiedDate());
			// 时间不相同
			if (!dbDate.equals(lastModifiedDate)) {
				throw new Exception();
			}
			// 已发货数量
			if (entity.getActIssuedCount() == null) {
				entity.setActIssuedCount(thisNum);
			} else {
				entity.setActIssuedCount(entity.getActIssuedCount() + thisNum);
			}
			// 上次修改人
			entity.setLastModifiedBy(employee.getWorkerCode());
			// issueDetailRemote.update(entity);
			details.add(entity);
		}
		return details;
	}

	/**
	 * 查找要更新的物资需求明细记录
	 */
	@SuppressWarnings("unchecked")
	private List<MrpJPlanRequirementDetail> updatePlanDetails(List<Map> records) {
		// 本次发货数量
		double thisNum;
		// 记录
		Map record;
		// 物资需求计划明细表entity
		MrpJPlanRequirementDetail plan;
		// 要更新的物资需求明细
		List<MrpJPlanRequirementDetail> planDetails = new ArrayList<MrpJPlanRequirementDetail>();
		for (int i = 0; i < records.size(); i++) {
			// 记录
			record = records.get(i);
			// 本次发货数量
			thisNum = getDouble(record.get("thisNum")) == null ? 0
					: getDouble(record.get("thisNum"));
			// 数据没有变更，不更新
			if (thisNum <= 0) {
				continue;
			}

			// 物资需求计划明细表
			// 需求计划明细id
			Long requirementDetailId = getLong(record
					.get("requirementDetailId"));
			// bg 物资需求计划明细表可能没有该条数据
			if (requirementDetailId != null) {
				// entity
				plan = planDetailRemote.findById(requirementDetailId, employee
						.getEnterpriseCode());
				if (plan != null) {
					// 已领数量
					if (plan.getIssQty() == null) {
						plan.setIssQty(thisNum);
					} else {
						plan.setIssQty(plan.getIssQty() + thisNum);
					}
					// 上次修改人
					plan.setLastModifiedBy(employee.getWorkerCode());
					// planDetailRemote.update(plan);
					planDetails.add(plan);
				}
			}
		}
		return planDetails;
	}

	/**
	 * 出库物资对账查询 add by drdu 20090512
	 * 
	 * @throws JSONException
	 */
	public void findIssueList() throws JSONException {
		

		String getPerson=request.getParameter("getPerson");
		String sDate = request.getParameter("startdate");
		String freeFrom = request.getParameter("freeFrom");
		String eDate = request.getParameter("enddate");
		String whsName = request.getParameter("warehouseName");
		String materialCode = request.getParameter("materialNo");
		String materialName = request.getParameter("materialName");
		String specNo = request.getParameter("specNo");
		String issuNo = request.getParameter("issueNo");
		String issueBy = request.getParameter("sendPerson");//add by fyyang 091218
		// add by liuyi 091102 专业，仓库，物资类型，部门
//		String issueBy = request.getParameter("issueBy");
		String delayStore = request.getParameter("delayStore");
		String materialClass = request.getParameter("materialClass");
		String dept = request.getParameter("dept");

		String isRedOp=request.getParameter("isRedOp");
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject pg = issueRemote.findIssueList(delayStore,materialClass,dept,employee.getEnterpriseCode(),
				sDate, eDate, whsName, materialCode, materialName, specNo,
				issuNo, issueBy,isRedOp,getPerson,freeFrom, start, limit);
		String str = "{total:" + pg.getTotalCount() + ",root:"
				+ JSONUtil.serialize(pg.getList()) + "}";
		write(str);
	}

	/**
	 * 查询领料单审核列表 add by fyyang 090619
	 * 
	 * @throws JSONException
	 */
	public void getIssueHeadListForCheck() throws JSONException {

		//add by drdu 091103
		String checkStatus = request.getParameter("checkStatus");
		//add by drdu 091126
		String issueNo = request.getParameter("issueNoFuzzy");
		// 开始行号
		String strStart = request.getParameter("start");
		// 行数
		String strlimit = request.getParameter("limit");
		int start = 0;
		int limit = 0;
		if (strStart != null && strStart.length() > 0) {
			start = Integer.parseInt(strStart);
		}
		if (strlimit != null && strlimit.length() > 0) {
			limit = Integer.parseInt(strlimit);
		}
		PageObject obj = new PageObject();

		obj = issueRemote.getIssueHeadListForCheck(
				employee.getEnterpriseCode(),checkStatus,issueNo, start, limit);

		write(JSONUtil.serialize(obj));
	}

	/**
	 * 查询领料单审核的物资明细列表 add by fyyang 090619
	 * 某个时间出库的，或者某个时间段审核的
	 * 
	 * @throws JSONException
	 */
	public void getMaterialDetailListForCheck() throws JSONException {
		String strIssueNo = request.getParameter("issueNo");
		//出库时间
		String issueDate=request.getParameter("issueDate"); 
		
		String startCheckDate=request.getParameter("startCheckDate");
		String endCheckDate=request.getParameter("endCheckDate");
		// 开始行号
		String strStart = request.getParameter("start");
		// 行数
		String strlimit = request.getParameter("limit");
		int start = 0;
		int limit = 0;
		if (strStart != null && strStart.length() > 0) {
			start = Integer.parseInt(strStart);
		}
		if (strlimit != null && strlimit.length() > 0) {
			limit = Integer.parseInt(strlimit);
		}
		PageObject obj = new PageObject();
		obj = issueRemote.getMaterialDetailListForCheck(employee
				.getEnterpriseCode(),strIssueNo,issueDate,startCheckDate,endCheckDate);

		write(JSONUtil.serialize(obj));
	}

	/**
	 * add by fyyang 090619 审核领料单
	 * @throws JSONException 
	 */
	public void issueCheckOp() throws JSONException {
		String issueHeadIds = request.getParameter("ids");
		
		//add by drdu 20100408------
		String str = request.getParameter("updateData");
		Object object = JSONUtil.deserialize(str);
		//-------------------
		
		issueHeadRemote.issueCheckOp(issueHeadIds, employee.getWorkerCode(),(List<Map>)object);
		write("{success:true,msg:'审核成功！'}");
	}

	/**
	 * 取消领料单审核
	 * add by drdu 091103
	 */
	public void issueCheckCancel()
	{
		String issueHeadIds = request.getParameter("ids");
		issueHeadRemote.issueCheckCancel(issueHeadIds, employee.getWorkerCode());
		write("{success:true,msg:'取消审核成功！'}");
	}
	
	/**
	 * 检索事务作用信息
	 */
	private void findTransInfo() {
		if (trans == null) {
			// 检索出库事务作用信息
			PageObject obj = issueRemote.getTransIdByTransCode(employee
					.getEnterpriseCode(), "I");
			if (obj.getList().size() > 0) {
				trans = (InvCTransaction) obj.getList().get(0);
				return;
			}
			// 空对象
			trans = new InvCTransaction();
		}
	}
	
//-------------------add by fyyang 090708 出库红单页面处理 ---------------------------------------	
	
	/**
	 * 获得已审核过的领料单物资列表 (用于出库红单处理页面)
	 * add by fyyang 090708 
	 * @throws JSONException 
	 */
	public void  findIssueDetailCheckListForBack() throws JSONException
	{
		String sDate = request.getParameter("startdate");
		String eDate = request.getParameter("enddate");
		String materialName = request.getParameter("materialName");
		String issuNo = request.getParameter("issueNo");
		String specNo = request.getParameter("specNo");
		String materialNo = request.getParameter("materialNo");
		// 添加红单查询 add by ywliu 20100128
		String isRedBill = request.getParameter("redBill");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject pg=new PageObject();
		if (objstart != null && objlimit != null) {
		 int	start = Integer.parseInt(request.getParameter("start"));
		 int	limit = Integer.parseInt(request.getParameter("limit"));
		 pg =issueRemote.findIssueDetailCheckList(employee.getEnterpriseCode(), sDate, eDate, 
				 materialName, issuNo, isRedBill, materialNo, specNo, start,limit);
		}
		else
		{
		 pg = issueRemote.findIssueDetailCheckList(employee.getEnterpriseCode(), sDate, eDate, 
				 materialName, issuNo, isRedBill, materialNo, specNo);
		}
		String str = "{total:" + pg.getTotalCount() + ",root:"
				+ JSONUtil.serialize(pg.getList()) + "}";
		write(str);
	}
	
	/**
	 * 出库红单处理
	 */
	public void IssueMaterialRedBackOp()
	{
		 Double thisPrice=Double.parseDouble(request.getParameter("stdCost"));//add by fyyang 20100304
		 Long issueDetailId=Long.parseLong(request.getParameter("issueDetailId"));
		 String issueHeadNo=request.getParameter("issueHeadNo");
		 Long materialId=Long.parseLong(request.getParameter("materialId"));
		 //处理原因
		 String memo=request.getParameter("memo");
		 //物资数量
		 double backCount=Double.parseDouble(request.getParameter("backCount"));
		materialRemote.updateStdCost(materialId, employee.getEnterpriseCode(), backCount, thisPrice);
		 backCount=0-backCount;
		 InvJIssueHead headModel= issueHeadRemote.findByMatCode(issueHeadNo, employee.getEnterpriseCode());
		 //更新标准成本
		
		 InvCMaterial materialModel= materialRemote.findById(materialId);
		 
		 InvJIssueDetails detailModel= issueDetailRemote.findById(issueDetailId);
		 //增加一条领料单  事务表增加一条数据 改变库存
		 //获得批号记录表里对应的记录
		 List<InvJLot> lotList=lotRemote.findByLWHLM(employee.getEnterpriseCode(), "0", materialModel.getDefaultWhsNo(), materialModel.getDefaultLocationNo(), materialId);
		if(lotList!=null&&lotList.size()>0)
		{
			
			InvJLot lotModel=lotList.get(0);
			// 更新批号记录表
			InvJLot lotEntity=updateInvJLot(lotModel, backCount);
			// 插入事务历史表
			InvJTransactionHis tranHisModel=getTransHisModel(lotModel, headModel, detailModel, backCount, materialModel.getStockUmId(), memo);
			
			// 更新库存物料记录
			InvJWarehouse warehouseEntity= updateWarehouse(lotModel, backCount);
			// add by ywliu 20100207
			warehouseEntity.setOpenBalance(lotEntity.getOpenBalance());
			warehouseEntity.setReceipt(lotEntity.getReceipt());
			warehouseEntity.setAdjust(lotEntity.getAdjust());
			warehouseEntity.setIssue(lotEntity.getIssue());
			// 库位为空时，不更新库位物料记录
			InvJLocation locationEntity=new InvJLocation();
			if(lotModel.getLocationNo()!=null)
			{
				// 更新库位物料记录
				locationEntity=updateLocatioon(lotModel, backCount);
				// add by ywliu 20100207
				locationEntity.setOpenBalance(lotEntity.getOpenBalance());
				locationEntity.setReceipt(lotEntity.getReceipt());
				locationEntity.setAdjust(lotEntity.getAdjust());
				locationEntity.setIssue(lotEntity.getIssue());
			}
			
			//增加一条领料单及领料单明细
			InvJIssueHead headEntity=headModel;
			headEntity.setLastModifiedBy(employee.getEnterpriseCode());
			headEntity.setLastModifiedDate(new Date());
			headEntity.setRefIssueNo(headModel.getIssueNo());
			headEntity.setIssueHeadId(null);
			headEntity.setIssueNo(null);
			headEntity.setIssueStatus("B");
			// modify by ywliu 20100125
			headEntity.setCheckedBy("");
			headEntity.setCheckedDate(null);
			headEntity.setLastModifiedBy(employee.getWorkerCode());
			headEntity.setLastModifiedDate(new Date());
			 InvJIssueDetails detailEntity= detailModel;
			 detailEntity.setAppliedCount(backCount);
			 detailEntity.setActIssuedCount(backCount);
			 detailEntity.setApprovedCount(backCount);
			 detailEntity.setIssueDetailsId(null);
			 detailEntity.setIssueHeadId(null);
			 tranHisModel.setStdCost(thisPrice);//设置单价 add by fyyang 20100304
			 issueRemote.updateDbTablesForIssueBack(headEntity, detailEntity, lotEntity, tranHisModel, warehouseEntity, locationEntity);
			 
			 write("{success:true}");
		}
		
		
	}
	
	/**
	 * add by fyyang 获得要增加的历史事务表的model
	 */
	@SuppressWarnings("unchecked")
	private InvJTransactionHis getTransHisModel(InvJLot lot, InvJIssueHead headModel, InvJIssueDetails detailModel,
			double issueQty,Long unitId,String memo) {
		// 检索事务历史表
		PageObject obj = null;
		// 检索结果
		List<Map> lstTrans = null;
		// 结果
		Map mapTrans = null;
		// 批号为0时不检索事务历史表
		if (!"0".equals(lot.getLotNo())) {
			obj = issueRemote.getTransHis(employee.getEnterpriseCode(), lot
					.getMaterialId(), lot.getLotNo(), lot.getWhsNo(), lot
					.getLocationNo());
			// 检索结果
			lstTrans = obj.getList();
			// 结果
			if (lstTrans.size() > 0) {
				mapTrans = lstTrans.get(0);
			}
		}
		// 检索物料主文件获取标准成本计算方式
		obj = issueRemote.getStdCostRelatingFields(
				employee.getEnterpriseCode(), lot.getMaterialId());
		List<Map> lstStdCost = obj.getList();
		// 结果
		Map mapStdCost = new HashMap();
		if (lstStdCost.size() > 0) {
			mapStdCost = lstStdCost.get(0);
		}

		// 事务历史
		InvJTransactionHis entity = new InvJTransactionHis();
		// 单号->领料单编号
		entity.setOrderNo(headModel.getIssueNo());
		// 序号->领料单明细id
		entity.setSequenceId(detailModel.getIssueDetailsId());
		// 检索事务作用信息
		if (trans == null) {
			findTransInfo();
		}
		// 事务作用类型id
		entity.setTransId(trans.getTransId());
		// 物料id
		entity.setMaterialId(lot.getMaterialId());
		// 批号
		entity.setLotNo(lot.getLotNo());
		if (mapTrans != null) {
			// 物料单价->事务历史检索结果.单价
			entity.setPrice(getDouble(mapTrans.get("price")));
			// 税率->事务历史检索结果.税率
			entity.setTaxRate(getDouble(mapTrans.get("taxRate")));
			// 供应商id->事务历史检索结果.供应商id
			entity.setSupplier(getLong(mapTrans.get("supplier")));
		}
		// 异动数量->出库数
		entity.setTransQty(issueQty);
		// 企业编码
		entity.setEnterpriseCode(employee.getEnterpriseCode());
		// 领用人->
		entity.setReceiveMan(headModel.getReceiptBy());
		// 领用部门
		entity.setReceiveDept(headModel.getReceiptDep());
		// 归口人
		entity.setCostMan(headModel.getReceiptBy());
		// 归口部门
		entity.setCostDept(headModel.getFeeByDep());
		// 计量单位
		entity.setTransUmId(unitId);
		// 操作仓库
		entity.setFromWhsNo(lot.getWhsNo());
		// 操作库位
		entity.setFromLocationNo(lot.getLocationNo());
		// 标准成本
		String costMethod = getString(mapStdCost.get("costMethod"));
		Double stdCost = null;
		// 先进先出（批控制）
		if ("2".equals(costMethod)) {
			if (mapTrans != null) {
				stdCost = getDouble(mapTrans.get("stdCost"));
			}
		}// 加权平均
		else if ("1".equals(costMethod)) {
			stdCost = getDouble(mapStdCost.get("stdCost"));

		}// 计划价格
		else if ("3".equals(costMethod)) {
			stdCost = getDouble(mapStdCost.get("frozenCost"));
		} else {
			// 批控制
			if (!"0".equals(lot.getLotNo())) {
				if (mapTrans != null) {
					stdCost = getDouble(mapTrans.get("stdCost"));
				}
			} else {
				stdCost = getDouble(mapStdCost.get("stdCost"));
			}
		}
		entity.setStdCost(stdCost);
		// 备注
		entity.setMemo(memo);
		entity.setIsUse("Y");
		entity.setLastModifiedBy(employee.getWorkerCode());
		
		return entity;
	}

	//-----------------add by fyyang  end-------------------------------------------------
	/**
	 * 查询“取消出库”列表
	 * add by drdu 090708
	 * @throws JSONException
	 */
	public void findCancelIssueList() throws JSONException
	{
		String issueNo = request.getParameter("issueNo");
		String materialId = request.getParameter("materialId");
		// 开始行号
		String strStart = request.getParameter("start");
		// 行数
		String strlimit = request.getParameter("limit");
		int start = 0;
		int limit = 0;
		if (strStart != null && strStart.length() > 0) {
			start = Integer.parseInt(strStart);
		}
		if (strlimit != null && strlimit.length() > 0) {
			limit = Integer.parseInt(strlimit);
		}
		PageObject obj = new PageObject();
		obj = issueRemote.findCancelIssueList(employee.getEnterpriseCode(), issueNo, materialId, start,limit);
		if(obj==null||obj.getList()==null)
		{
		write("{\"list\":[],\"totalCount\":0}");
		}
		else
		{
		write(JSONUtil.serialize(obj));
		}
	}

	/**
	 * 出库回滚
	 * 修改库存物料记录表、领料单明细表、事务历史表、库位物料记录、批号记录表中的出库数量
	 * add by drdu 090708
	 */
	public void updateIssueQty()
	{
		Long tHisId = Long.parseLong(request.getParameter("tHisId"));
		// modified by liuyi 
//		Long qty = Long.parseLong(request.getParameter("qty"));
		Double qty = Double.parseDouble(request.getParameter("qty"));
		
		issueRemote.updateIssueQty(employee.getEnterpriseCode(), tHisId, qty,employee.getWorkerCode());
		
		write("{success:true,msg:'修改成功！'}");
	}
	
	/**
	 * 出库补打印列表
	 * add by drdu 091121
	 * @throws JSONException
	 */
	public void findAfterPrintIssueList() throws JSONException {
		String issueNo = request.getParameter("issueNo");
		String strStart = request.getParameter("start");
		String strlimit = request.getParameter("limit");
		int start = 0;
		int limit = 0;
		if (strStart != null && strStart.length() > 0) {
			start = Integer.parseInt(strStart);
		}
		if (strlimit != null && strlimit.length() > 0) {
			limit = Integer.parseInt(strlimit);
		}
		PageObject obj = new PageObject();
		obj = issueRemote.findAfterPrintIssueList(issueNo, start, limit,
				employee.getEnterpriseCode());
		if (obj == null || obj.getList() == null) {
			write("{\"list\":[],\"totalCount\":0}");
		} else {
			write(JSONUtil.serialize(obj));
		}
	}
	/**
	 * @throws JSONException 
	 * 
	 */
	public void queryCheckedIssueList() throws JSONException
	{
		String sdate=request.getParameter("sdate");
		String edate=request.getParameter("edate");
		String issueNo=request.getParameter("issueNo");
		String receiptBy=request.getParameter("receiptBy");
		String receiptDept=request.getParameter("receiptDept");
		String billType=request.getParameter("billType");
		
		PageObject obj=new PageObject();
		
		String strStart = request.getParameter("start");
		String strlimit = request.getParameter("limit");
		
		if (strStart != null &&strlimit != null) {
		    obj=issueRemote.queryCheckedIssueList(sdate, edate, issueNo, receiptBy, receiptDept, employee.getEnterpriseCode(),billType, Integer.parseInt(strStart),Integer.parseInt(strlimit));
		}
		else
		{
			obj=issueRemote.queryCheckedIssueList(sdate, edate, issueNo, receiptBy, receiptDept, employee.getEnterpriseCode(),billType);
		}
		write(JSONUtil.serialize(obj));
	}
	
	/**
	 *  审核单据查询(领料单审核单据明细查询)
	 * @throws JSONException
	 */
	public void queryCheckedIssueDetailList() throws JSONException
	{
		String sdate=request.getParameter("sdate");
		String edate=request.getParameter("edate");
		String issueNo=request.getParameter("issueNo");
		String receiptBy=request.getParameter("receiptBy");
		String receiptDept=request.getParameter("receiptDept");
		String billType=request.getParameter("billType");
		
		PageObject obj=new PageObject();
		
		String strStart = request.getParameter("start");
		String strlimit = request.getParameter("limit");
		
		if (strStart != null &&strlimit != null) {
		    obj=issueRemote.queryCheckedIssueDetailList(sdate, edate, issueNo, receiptBy, receiptDept, employee.getEnterpriseCode(),billType, Integer.parseInt(strStart),Integer.parseInt(strlimit));
		}
		else
		{
			obj=issueRemote.queryCheckedIssueDetailList(sdate, edate, issueNo, receiptBy, receiptDept, employee.getEnterpriseCode(),billType);
		}
		write(JSONUtil.serialize(obj));
	}
	
	/**
	 * 获取map里面的long型值
	 * 
	 * @return 结果
	 */
	private Long getLong(Object obj) {
		if (obj != null) {
			return Long.parseLong(obj.toString());
		}
		return null;
	}

	/**
	 * 获取map里面的double型值
	 * 
	 * @return 结果
	 */
	private Double getDouble(Object obj) {
		if (obj != null) {
			try {
				return Double.parseDouble(obj.toString());
			} catch (NumberFormatException e) {
				return null;
			}
		}
		return null;
	}

	/**
	 * 获取数量
	 */
	private double getDoubleQty(Double qty) {
		if (qty != null) {
			return qty.doubleValue();
		}
		return 0;
	}

	/**
	 * 获取map里面的long型值
	 * 
	 * @return 结果
	 */
	private String getString(Object obj) {
		if (obj != null) {
			return String.valueOf(obj);
		}
		return null;
	}

	/**
	 * 获取
	 * 
	 * @return strMemo
	 */
	public String getStrMemo() {
		return strMemo;
	}

	/**
	 * 设置
	 * 
	 * @param strMemo
	 */
	public void setStrMemo(String strMemo) {
		this.strMemo = strMemo;
	}

	/**
	 * 日期格式化为字符串
	 * 
	 * @param date
	 *            日期
	 * @return date对应的字符串
	 */
	private String DateToString(Date date) {
		SimpleDateFormat defaultFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String sysDate = defaultFormat.format(date);
		return sysDate;
	}
}
