/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.resource.purchasewarehouse.action;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.InvCLocation;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvCMaterialFacadeRemote;
import power.ejb.resource.InvCTransaction;
import power.ejb.resource.InvCTransactionFacadeRemote;
import power.ejb.resource.InvCWarehouse;
import power.ejb.resource.InvJLocation;
import power.ejb.resource.InvJLocationFacadeRemote;
import power.ejb.resource.InvJLot;
import power.ejb.resource.InvJLotFacadeRemote;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJTransactionHisFacadeRemote;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.InvJWarehouseFacadeRemote;
import power.ejb.resource.PurJArrival;
import power.ejb.resource.PurJArrivalDetails;
import power.ejb.resource.PurJArrivalDetailsFacadeRemote;
import power.ejb.resource.PurJArrivalFacadeRemote;
import power.ejb.resource.PurJOrderDetails;
import power.ejb.resource.PurJOrderDetailsFacadeRemote;
import power.ejb.resource.business.IssueManage;
import power.ejb.resource.business.PurchaseWarehouse;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 采购入库管理Action
 * 
 * @author fangjihu
 * 
 */
public class PurchaseWarehouseAction extends AbstractAction {

	private static final long serialVersionUID = 1L;
	/**  */
	protected PurchaseWarehouse remote;
	/** 到货登记/验收表远程对象 */
	protected PurJArrivalFacadeRemote purJArrivalRemote;
	/** 采购订单明细表远程对象 */
	protected PurJOrderDetailsFacadeRemote orderDetailRemote;
	/** 事务历史表远程对象 */
	protected InvJTransactionHisFacadeRemote transHisRemote;

	protected BaseDataManager managerRemote;

	private static final String EMPTY = "";

	private static final String ZERO = "0";

	private static final String ONE = "1";

	private static final String TWO = "2";

	private static final String THERE = "3";

	private static final String YES = "Y";

	private static final String TRANS_NAME = "P";

	private static final String MESSAGE1 = "{flag:'1',msg:'&nbsp&nbsp保存成功。&nbsp&nbsp'}";

	private static final String MESSAGE2 = "{flag:'0',msg:'正在结账，此业务无法进行。'}";

	private static final String MASSAGE3 = "{flag:'0',msg:'操作数据库过程中异常终了。'}";

	private static final String MASSAGE4 = "{flag:'0',msg:'他人使用中。'}";

	private static final String MESSAGE5 = "{flag:'2',msg:'保存成功。<br>没有物料入库。'}";

	public PurchaseWarehouseAction() {
		remote = (PurchaseWarehouse) factory
				.getFacadeRemote("PurchaseWarehouseImpl");
		// 到货登记/验收表远程对象
		purJArrivalRemote = (PurJArrivalFacadeRemote) factory
				.getFacadeRemote("PurJArrivalFacade");
		// 订单明细表远程对象
		orderDetailRemote = (PurJOrderDetailsFacadeRemote) factory
				.getFacadeRemote("PurJOrderDetailsFacade");
		// 事务历史表远程对象
		transHisRemote = (InvJTransactionHisFacadeRemote) factory
				.getFacadeRemote("InvJTransactionHisFacade");
		managerRemote = (BaseDataManager) factory
				.getFacadeRemote("BaseDataManagerImpl");
	}

	public void getRS001PurchaseList() throws JSONException {
		// 查询值
		String fuzzy = request.getParameter("fuzzy");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// 明细部的操作员
		String workCodeName = employee.getWorkerName();
		// 企业编码
		String enterpriseCode = employee.getEnterpriseCode();
		PageObject obj = remote.findPurchaseOrderList(fuzzy, Integer
				.parseInt(start), Integer.parseInt(limit), workCodeName,
				enterpriseCode);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 事务名称取得
	 */
	public void getTransName() {
		String transCode = request.getParameter("transCode");
		write(remote.findTransName(transCode, employee.getEnterpriseCode()));
	}

	/**
	 * 采购入库详细信息列表
	 * 
	 * @throws JSONException
	 */
	public void getRS001PurchaseDetailList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String id = request.getParameter("id");
		PageObject obj = remote.findPurchaseOrderDetailList(Long.parseLong(id),
				Integer.parseInt(start), Integer.parseInt(limit), employee
						.getEnterpriseCode());

		write(JSONUtil.serialize(obj));
	}

	/**
	 * 仓库列表
	 * 
	 * @throws JSONException
	 */
	public void getWarehouseList() throws JSONException {
		// flag = '1' 入库出库移库时
		// flag ！= '1' 其他调用时
		// 对应变更
		String flag = request.getParameter("flag");
		PageObject obj = remote.findAllWarehouse(flag, employee
				.getEnterpriseCode());
		List<InvCWarehouse> list = obj.getList();
		// 对应式样变更
		if (list.size() > 0) {
			InvCWarehouse inv = new InvCWarehouse();
			inv.setWhsNo(EMPTY);
			inv.setWhsName(EMPTY);
			list.add(0, inv);
			obj.setList(list);
		}
		String str = JSONUtil.serialize(obj);
		write(str);

	}

	/**
	 * 库位列表
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void getLocationList() throws JSONException {
		String whsNo = request.getParameter("whsNo");
		PageObject obj = remote.findLocation(whsNo, employee
				.getEnterpriseCode());
		List<InvCLocation> list = obj.getList();
		if (list.size() > 0) {
			InvCLocation inv = new InvCLocation();
			inv.setWhsNo(whsNo);
			inv.setLocationName(EMPTY);
			inv.setLocationNo(EMPTY);
			list.add(0, inv);
			obj.setList(list);
		}
		String str = JSONUtil.serialize(obj);
		write(str);
	}

	/**
	 * 仓库名称
	 */
	public void getWarehouseName() {
		String whsNo = request.getParameter("whsNo");
		if (!whsNo.equals(EMPTY)) {
			String whsName = remote.findWarehouseName(whsNo, employee
					.getEnterpriseCode());
			write(whsName);
		} else {
			write(EMPTY);
		}
	}

	/**
	 * 库位名称获取
	 * 
	 */
	public void getLocationName() {
		String whsNo = request.getParameter("whsNo");
		String locationNo = request.getParameter("locationNo");
		if (!locationNo.equals(EMPTY)) {
			String locationName = remote.findLocationName(whsNo, locationNo,
					employee.getEnterpriseCode());
			write(locationName);
		} else {
			write(EMPTY);
		}
	}

	/**
	 * 仓库保管人取得
	 */
	public void getWarehouseSaveName() {
		String whsNo = request.getParameter("whsNo");
		if (!whsNo.equals(EMPTY)) {
			String saveName = remote.findWarehouseSaveName(whsNo, employee
					.getEnterpriseCode());
			if (EMPTY.equals(saveName)) {
				write(EMPTY);
				return;
			}
			Employee bean = managerRemote.getEmployeeInfo(saveName);
			if (bean == null) {
				write(EMPTY);
			} else {
				write(bean.getWorkerName());
			}
			/*
			 * String chsName = remote.findPersonInfo(saveName); write(chsName);
			 */
		} else {
			write(EMPTY);
		}
	}

	/**
	 * 获取单位名称
	 */
	@SuppressWarnings("unchecked")
	public void getRS001UnitName() {

		BpCMeasureUnitFacadeRemote unitRemote = (BpCMeasureUnitFacadeRemote) factory
				.getFacadeRemote("BpCMeasureUnitFacade");
		Object unitCode = request.getParameter("unitCode");
		if (unitCode == null || (EMPTY.equals(unitCode))) {
			write(EMPTY);
			return;
		}
		BpCMeasureUnit obj = unitRemote.findById(Long.parseLong(unitCode
				.toString()));
		if (obj != null) {
			write(obj.getUnitName());
		} else {
			write(EMPTY);
		}

	}

	/**
	 * 获取验收员名称
	 */
	public void getRS001OperaterByName() {
		// 人员编码
		String operateBy = request.getParameter("operateBy");
		Employee bean = managerRemote.getEmployeeInfo(operateBy);
		if (bean == null) {
			write(EMPTY);
		} else {
			write(bean.getWorkerName());
		}
		/*
		 * String chsName = remote.findPersonInfo(operateBy); write(chsName);
		 */
	}

	/**
	 * 登陆入库操作
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void updatePurchaseWarehouse() throws JSONException {
		try {
			// 流水号
			String id = request.getParameter("id");
			// 右边store数据
			String rightData = request.getParameter("data");
			// 左边的store数据
			String leftData = request.getParameter("data1");
			// 到货登记/验收表备注
			String detailMemo = request.getParameter("detailMemo");
			// 发票号 add by drdu 090618
			String invoiceNo = request.getParameter("invoiceNo");
			// 备注是否修改 1：修改 0：未修改
			String memoFlag = request.getParameter("memoFlag");
			List<Map> rightDataList = (List<Map>) JSONUtil
					.deserialize(rightData);
			List<Map> leftDataList = (List<Map>) JSONUtil.deserialize(leftData);
			Map leftMap = null;
			if (leftDataList.size() == 1) {
				leftMap = leftDataList.get(0);
			}
			String operateDate = getString(leftMap.get("operateDate2"));

			if (!isBlance()) {
				PurJArrival purjArrivalBean = null;
				// 更新到货登记/验收表
				if (ONE.equals(memoFlag)) {

					purjArrivalBean = purJArrivalRemote.findById(Long
							.parseLong(id));
					if (YES.equals(purjArrivalBean.getIsUse())) {
						if (!operateDate.equals(DateToString(purjArrivalBean
								.getLastModifiedDate()))) {
							throw new Exception();
						}
						// 操作人
						purjArrivalBean.setLastModifiedBy(employee
								.getWorkerCode());
						// 操作时间
						purjArrivalBean.setLastModifiedDate(new Date());
						// 备注
						purjArrivalBean.setMemo(detailMemo);
						// 发票号 add by drdu 090618
						purjArrivalBean.setInvoiceNo(invoiceNo);

						// purJArrivalRemote.update(purjArrivalBean);

					}
				}
				Map map;
				// 到货登记明细表
				List<PurJArrivalDetails> updatePurJArrivalDetailList = new ArrayList<PurJArrivalDetails>();
				// 采购订单明细表
				List<PurJOrderDetails> updatePurJOrderDetailList = new ArrayList<PurJOrderDetails>();
				// 事务历史表
				List<InvJTransactionHis> saveInvJTransactionHisList = new ArrayList<InvJTransactionHis>();
				// 库存物料记录表
				List<InvJWarehouse> saveInvJWarehouseList = new ArrayList<InvJWarehouse>();
				List<InvJWarehouse> updateInvJWarehouseList = new ArrayList<InvJWarehouse>();

				// 库位物料记录表
				List<InvJLocation> saveInvJLocationList = new ArrayList<InvJLocation>();
				List<InvJLocation> updateInvJLocationList = new ArrayList<InvJLocation>();

				// 批号记录表
				List<InvJLot> saveInvJLotList = new ArrayList<InvJLot>();
				List<InvJLot> updateInvJLotList = new ArrayList<InvJLot>();

				// 物料主文件
				List<InvCMaterial> updateInvCMaterialList = new ArrayList<InvCMaterial>();
				// 对应回滚操作
				if (rightDataList.size() > 0) {
					for (int i = 0; i < rightDataList.size(); i++) {
						map = rightDataList.get(i);
						// 更新到货登记明细表
						updatePurJArrivalDetails(map,
								updatePurJArrivalDetailList);
						// 更新采购订单明细表
						updatePurJOrderDetails(map, updatePurJOrderDetailList);
						// 更新事务历史表
						saveInvJTransactionHisTable(map, leftMap,
								saveInvJTransactionHisList);
						// 更新库存物料记录表
						updateInvJWarehouse(map, saveInvJWarehouseList,
								updateInvJWarehouseList);
						// 更新库位物料记录表
						updateInvJLocation(map, saveInvJLocationList,
								updateInvJLocationList);
						// 更新批号记录表
						updateInvJLot(map, saveInvJLotList, updateInvJLotList);
						// 更新物料主文件
						updateInvCMaterial(map, updateInvCMaterialList);
						// write(MESSAGE1);

					}
				} else {
					// write(MESSAGE1);
				}
				remote.saveORUpdateAllDB(purjArrivalBean,
						updatePurJArrivalDetailList, updatePurJOrderDetailList,
						saveInvJTransactionHisList, saveInvJWarehouseList,
						updateInvJWarehouseList, saveInvJLocationList,
						updateInvJLocationList, saveInvJLotList,
						updateInvJLotList, updateInvCMaterialList);
				if (rightDataList.size() == 0 && purjArrivalBean != null) {
					write(MESSAGE5);
				} else {
					write(MESSAGE1);
				}
			} else {
				write(MESSAGE2);
			}
		} catch (RuntimeException e) {
			write(MASSAGE3);
		} catch (Exception e) {
			write(MASSAGE4);
		}
	}

	/**
	 * 更新到货登记明细表
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updatePurJArrivalDetails(Map map,
			List<PurJArrivalDetails> updatePurJArrivalDetailList)
			throws Exception {
		// 本次入库数
		Double thisQty = Double.parseDouble(map.get("thisQty").toString());
		// 更新到货登记明细表的流水号
		Long id = Long.parseLong(map.get("arrivalDetailID").toString());
		PurJArrivalDetailsFacadeRemote purJArrivalDetailsRemote = (PurJArrivalDetailsFacadeRemote) factory
				.getFacadeRemote("PurJArrivalDetailsFacade");
		PurJArrivalDetails entity = purJArrivalDetailsRemote.findById(id);
		String arrivalDetailModifiedDate = getString(map
				.get("arrivalDetailModifiedDate"));
		if (!arrivalDetailModifiedDate.equals(DateToString(entity
				.getLastModifiedDate()))) {
			throw new Exception();
		}
		Double recQty = entity.getRecQty();
		entity.setRecQty(recQty + thisQty);
		String gridMemo = getString(map.get("gridMemo"));
		entity.setMemo(gridMemo);
		entity.setLastModifiedBy(employee.getWorkerCode());
		entity.setLastModifiedDate(new Date());
		updatePurJArrivalDetailList.add(entity);
		// purJArrivalDetailsRemote.update(entity);
	}

	/**
	 * 判断是否结账
	 * 
	 * @return
	 */
	public boolean isBlance() {

		IssueManage issueRemote = (IssueManage) factory
				.getFacadeRemote("IssueManageImpl");

		return issueRemote.isBalanceNow(TRANS_NAME);
	}

	/**
	 * 更新物料主文件
	 * 
	 * @param map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void updateInvCMaterial(Map map,
			List<InvCMaterial> updateInvJLotList) throws Exception {

		// 物料ID
		Long materialId = getLong(map.get("materialId"));
		// 本次入库数
		Double thisQty = Double.parseDouble(map.get("thisQty").toString());
		String tempQty = remote.findWarehouseQty(materialId, employee
				.getEnterpriseCode());
		if (EMPTY.equals(tempQty)) {
			tempQty = "0";
		}
		Double warehouseQty = Double.parseDouble(tempQty);
		// 物料主文件.标准成本
		Double stdCost = Double.parseDouble(getStringToDouble(map
				.get("stdCost")));
		// 画面.单价
		Double unitPrice = Double.parseDouble(getStringToDouble(map
				.get("unitPrice")));

		// 标准成本ＴＥＭＰ
		// modify by fyyang 091222
		Double stdCostTemp = unitPrice;
		if (warehouseQty + thisQty != 0) {
			stdCostTemp = (stdCost * warehouseQty + unitPrice * thisQty)
					/ (warehouseQty + thisQty);
		}
		// 画面.计价方式
		String costMethod = getString(map.get("costMethod"));
		if (EMPTY.equals(costMethod) || ONE.equals(costMethod)) {
			InvCMaterialFacadeRemote invCMaterialRemote = (InvCMaterialFacadeRemote) factory
					.getFacadeRemote("InvCMaterialFacade");
			InvCMaterial entity = invCMaterialRemote.findById(materialId);
			String materialModifiedDate = getString(map
					.get("materialModifiedDate"));
			if (!materialModifiedDate.equals(DateToString(entity
					.getLastModifiedDate()))) {
				throw new Exception();
			}
			entity.setStdCost(stdCostTemp);
			entity.setLastModifiedBy(employee.getWorkerCode());
			entity.setLastModifiedDate(new Date());
			updateInvJLotList.add(entity);
			// invCMaterialRemote.update(entity);

		}

	}

	/**
	 * 更新采购订单明细表
	 * 
	 * @param map
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void updatePurJOrderDetails(Map map,
			List<PurJOrderDetails> updatePurJOrderDetailList) throws Exception {
		// 采购订单明细表.流水号
		Long purOrderDetailsId = getLong(map.get("purOrderDetailsId"));
		// 本次入库数
		Double thisQty = Double.parseDouble(map.get("thisQty").toString());
		// 仓库编码
		String whsNo = getString(map.get("whsNo"));
		// 仓位编码
		String locationNo = getString(map.get("locationNo"));
		// 缺省仓库编码
		String defaultWhsNo = getString(map.get("defaultWhsNo"));
		// 缺省库位编码
		String defaultLocationNo = getString(map.get("defaultLocationNo"));
		if (EMPTY.equals(whsNo)) {
			whsNo = defaultWhsNo;
		}
		if (EMPTY.equals(locationNo)) {
			locationNo = defaultLocationNo;
		}

		PurJOrderDetails orderDetailBean = orderDetailRemote
				.findById(purOrderDetailsId);
		String orderDetailModifiedDate = getString(map
				.get("orderDetailModifiedDate"));
		if (!orderDetailModifiedDate.equals(DateToString(orderDetailBean
				.getLastModifiedDate()))) {
			throw new Exception();
		}
		// 已收数量
		Double receiveQty = orderDetailBean.getRcvQty();
		// 暂收数量
		Double insQty = orderDetailBean.getInsQty();
		Double qty = new Double(thisQty);
		// 已收数量 = 已收数量+画面．入库数
		orderDetailBean.setRcvQty(receiveQty + qty);
		// 暂收数量 = 暂收数量-画面．入库数
		orderDetailBean.setInsQty(insQty - qty);
		/*
		 * // 接收仓库 orderDetailBean.setReceiptWhs(whsNo); // 接收库位
		 * orderDetailBean.setReceiptLocation(locationNo);
		 */
		// 对应变更
		// 上次修改人
		orderDetailBean.setLastModifiedBy(employee.getWorkerCode());
		// 上次修改日期
		orderDetailBean.setLastModifiedDate(new Date());
		updatePurJOrderDetailList.add(orderDetailBean);
		// orderDetailRemote.update(orderDetailBean);
	}

	/**
	 * 取得所有的库位记录个数
	 */
	public void getLocationAllListCount() {
		String count = remote.findLocationAllRecord(employee
				.getEnterpriseCode());
		if (!EMPTY.equals(count)) {
			write(count);
		} else {
			write(ZERO);
		}
	}

	/**
	 * 获得字符串值
	 */
	private String getString(Object obj) {
		if (obj != null) {
			return (String) obj;
		} else {
			return "";
		}

	}

	/**
	 * 获得字符串值
	 */
	private Long getLong(Object obj) {
		if (obj != null) {
			return (Long) obj;
		} else {
			return 0l;
		}

	}

	/**
	 * 更新事务历史表
	 * 
	 * @param map
	 * @param leftMap
	 */
	@SuppressWarnings("unchecked")
	private void saveInvJTransactionHisTable(Map map, Map leftMap,
			List<InvJTransactionHis> saveInvJTransactionHisList) {
		// 采购订单明细表.流水号
		Long purOrderDetailsId = (Long) map.get("purOrderDetailsId");
		// 本次入库数
		Double thisQty = Double.parseDouble(map.get("thisQty").toString());
		// 物料ID
		Long materialId = getLong(map.get("materialId"));
		// 批次ＴＥＭＰ
		String lotCodeTemp = "0";
		// 画面.是否批控制
		String isLot = getString(map.get("isLot"));
		if (YES.equals(isLot)) {
			// TODO 批次ＴＥＭＰ = 自动生成批号
			Long maxLotCode = Long.parseLong(remote.findMaxLotNo(materialId)) + 1;
			lotCodeTemp = maxLotCode.toString();
		}
		String tempQty = remote.findWarehouseQty(materialId, employee
				.getEnterpriseCode());
		if (EMPTY.equals(tempQty)) {
			tempQty = "0";
		}
		// 登陆事务历史表
		Double warehouseQty = Double.parseDouble(tempQty);
		// 物料主文件.标准成本
		Double stdCost = Double.parseDouble(getStringToDouble(map
				.get("stdCost")));
		// 画面.单价
		Double unitPrice = Double.parseDouble(getStringToDouble(map
				.get("unitPrice")));
		// 画面.计划价格
		Double frozenCost = Double.parseDouble(getStringToDouble(map
				.get("frozenCost")));
		// 标准成本ＴＥＭＰ

		// modify by fyyang 091223
		Double stdCostTemp = unitPrice;
		if (warehouseQty + thisQty != 0) {
			stdCostTemp = (stdCost * warehouseQty + unitPrice * thisQty)
					/ (warehouseQty + thisQty);
		}
		// 画面.计价方式
		String costMethod = getString(map.get("costMethod"));
		// 事务标准成本ＴＥＭＰ
		Double transStdCostTemp = 0d;
		if (TWO.equals(costMethod)) {
			transStdCostTemp = new Double(unitPrice);
		} else if (THERE.equals(costMethod)) {
			transStdCostTemp = new Double(frozenCost);
		} else {
			transStdCostTemp = stdCostTemp;
		}

		// 画面.税率
		String taxRate = getString(map.get("taxRate"));
		// 画面.币种
		String currencyType = getString(map.get("currencyType"));
		// 画面.存货计量单位
		Long stockUmId = Long
				.parseLong(getStringToDouble((map.get("stockUmId"))));

		// 仓库编码
		String whsNo = getString(map.get("whsNo"));
		// 仓位编码
		String locationNo = getString(map.get("locationNo"));
		// 缺省仓库编码
		String defaultWhsNo = getString(map.get("defaultWhsNo"));
		// 缺省库位编码
		String defaultLocationNo = getString(map.get("defaultLocationNo"));
		if (whsNo.equals(EMPTY)) {
			whsNo = defaultWhsNo;
		}
		if (locationNo.equals(EMPTY)) {
			locationNo = defaultLocationNo;
		}

		// 画面．供应商编码
		String supplier = "";
		// 画面．供应商全称
		String supplyName = "";
		if (leftMap != null) {
			supplier = getStringToDouble(leftMap.get("supplier"));
			supplyName = getStringToDouble(leftMap.get("supplyName"));
		}
		String gridMemo = getString(map.get("gridMemo"));

		InvJTransactionHis transHisEntity = new InvJTransactionHis();
		// 式样变更 单据编号--》订单编号
		String purNo = getString(leftMap.get("purNo"));
		// 单号 = 画面.单据编号
		transHisEntity.setOrderNo(purNo);
		// 序号 = 画面.流水号
		transHisEntity.setSequenceId(purOrderDetailsId);
		// 事务类型 =‘P’

		transHisEntity.setTransId(Long.parseLong(remote.findTransId("P",
				employee.getEnterpriseCode())));
		// 物料编码
		transHisEntity.setMaterialId(materialId);
		// 批号 ＝ 批次ＴＥＭＰ
		transHisEntity.setLotNo(lotCodeTemp);
		// 异动数量 ＝ 画面.入库数
		transHisEntity.setTransQty(new Double(thisQty));
		// 备注 = 画面.备注
		transHisEntity.setMemo(gridMemo);
		// 物料单价 ＝ 画面.单价
		transHisEntity.setPrice(new Double(unitPrice));
		if (!EMPTY.equals(taxRate)) {
			// 税率 ＝ 画面.税率
			transHisEntity.setTaxRate(new Double(taxRate));
		}
		// 事务标准成本 ＝ 事务标准成本ＴＥＭＰ
		transHisEntity.setStdCost(transStdCostTemp);
		// 本币 ＝ 系统配置文件中取得（共通的方法读取配置文件）
		Long currentId = Long.parseLong(managerRemote.getOriCurrency());
		// 
		transHisEntity.setOriCurrencyId(currentId);
		if (!EMPTY.equals(currencyType)) {
			// 交易币种 ＝ 画面.币种
			transHisEntity.setCurrencyId(Long.parseLong(currencyType));
		}
		// 汇率
		// 对应bug
		if (!EMPTY.equals(currencyType)) {
			String exchangeRate = remote.findExchangeRate(currentId, employee
					.getEnterpriseCode(), Long.parseLong(currencyType));
			if (!ZERO.equals(exchangeRate)) {
				transHisEntity
						.setExchangeRate(Double.parseDouble(exchangeRate));
			}
		}
		// 计量单位 ＝ 画面.存货计量单位
		transHisEntity.setTransUmId(stockUmId);
		// 操作仓库 ＝ 画面．仓库

		transHisEntity.setFromWhsNo(whsNo);
		// 操作库位 ＝ 画面．库位
		transHisEntity.setFromLocationNo(locationNo);
		// 供应商编码 ＝ 画面．供应商编码
		transHisEntity.setSupplier(Long.parseLong(supplier));
		// 供应商 ＝ 画面．供应商全称
		transHisEntity.setCustomerNo(supplyName);

		// 操作人 = 当前用户
		transHisEntity.setLastModifiedBy(employee.getWorkerCode());
		// 操作日期 = 系统时间
		transHisEntity.setLastModifiedDate(new Date());
		// 企业代码 = 画面.企业代码(session)
		transHisEntity.setEnterpriseCode(employee.getEnterpriseCode());
		// 是否使用 = 'Y'
		transHisEntity.setIsUse(YES);
		transHisEntity.setTransHisId(transHisRemote.getMaxId());

		transHisEntity.setArrivalNo(getString(leftMap.get("arrivalNo")));// add
		// by
		// fyyang
		// 090709
		// 到货单号
		saveInvJTransactionHisList.add(transHisEntity);
		// transHisRemote.save(transHisEntity);

	}

	/**
	 * 库存物料记录的更新或添加
	 * 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private void updateInvJWarehouse(Map map,
			List<InvJWarehouse> saveInvJWarehouseList,
			List<InvJWarehouse> updateInvJWarehouseList) {
		// 物料ID
		Long materialId = getLong(map.get("materialId"));
		// 仓库编码
		String whsNo = getString(map.get("whsNo"));
		// 缺省仓库编码
		String defaultWhsNo = getString(map.get("defaultWhsNo"));
		if (EMPTY.equals(whsNo)) {
			whsNo = defaultWhsNo;
		}

		// 本次入库数
		Double thisQty = Double.parseDouble(map.get("thisQty").toString());
		String warehouseInvId = remote.findInvJWarehouse(materialId, whsNo,
				employee.getEnterpriseCode());
		if (!EMPTY.equals(warehouseInvId)) {
			// 库存物料记录的远程对象
			InvJWarehouseFacadeRemote invJWarehouseRemote = (InvJWarehouseFacadeRemote) factory
					.getFacadeRemote("InvJWarehouseFacade");
			InvJWarehouse entity = invJWarehouseRemote.findById(Long
					.parseLong(warehouseInvId));
			InvJWarehouse modifiedEntity = warehouseLogicalProcess(entity,
					thisQty);
			updateInvJWarehouseList.add(modifiedEntity);
			// invJWarehouseRemote.update(modifiedEntity);
		} else {

			InvJWarehouseFacadeRemote invJWarehouseRemote = (InvJWarehouseFacadeRemote) factory
					.getFacadeRemote("InvJWarehouseFacade");
			InvJWarehouse entity = new InvJWarehouse();
			entity.setOpenBalance(0d);
			entity.setReceipt(0d);
			entity.setAdjust(0d);
			entity.setIssue(0d);
			InvJWarehouse modifiedEntity = warehouseLogicalProcess(entity,
					thisQty);
			// 物料ID
			modifiedEntity.setMaterialId(materialId);
			// 仓库编码
			modifiedEntity.setWhsNo(whsNo);
			// 本期预留
			modifiedEntity.setReserved(0d);
			// 是否使用
			modifiedEntity.setIsUse(YES);
			// 企业代码
			modifiedEntity.setEnterpriseCode(employee.getEnterpriseCode());
			saveInvJWarehouseList.add(modifiedEntity);
			// invJWarehouseRemote.save(modifiedEntity);

		}
	}

	/**
	 * 库位物料记录的更新或添加
	 * 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private void updateInvJLocation(Map map,
			List<InvJLocation> saveInvJLocationList,
			List<InvJLocation> updateInvJLocationList) {
		// 本次入库数
		Double thisQty = Double.parseDouble(map.get("thisQty").toString());
		// 物料ID
		Long materialId = getLong(map.get("materialId"));
		// 物料编码
		String materialNo = getString(map.get("materialNo"));
		// 仓库编码
		String whsNo = getString(map.get("whsNo"));
		// 仓位编码
		String locationNo = getString(map.get("locationNo"));
		// 缺省仓库编码
		String defaultWhsNo = getString(map.get("defaultWhsNo"));
		// 缺省库位编码
		String defaultLocationNo = getString(map.get("defaultLocationNo"));
		if (EMPTY.equals(whsNo)) {
			whsNo = defaultWhsNo;
		}
		if (EMPTY.equals(locationNo)) {
			locationNo = defaultLocationNo;
		}
		PageObject obj = remote.findLocation(whsNo, employee
				.getEnterpriseCode());
		List<InvCLocation> list = obj.getList();
		if (list != null && list.size() > 0) {
			String locationInvId = remote.findInvJLocation(materialId, whsNo,
					locationNo, employee.getEnterpriseCode());
			if (!"".equals(locationInvId)) {
				InvJLocationFacadeRemote invJLocationRemote = (InvJLocationFacadeRemote) factory
						.getFacadeRemote("InvJLocationFacade");
				InvJLocation entity = invJLocationRemote.findById(Long
						.parseLong(locationInvId));

				InvJLocation modifiedEntity = locationLogicalProcess(entity,
						thisQty);
				updateInvJLocationList.add(modifiedEntity);
				// invJLocationRemote.update(modifiedEntity);
			} else {
				InvJLocationFacadeRemote invJLocationRemote = (InvJLocationFacadeRemote) factory
						.getFacadeRemote("InvJLocationFacade");
				InvJLocation entity = new InvJLocation();
				entity.setOpenBalance(0d);
				entity.setReceipt(0d);
				entity.setAdjust(0d);
				entity.setIssue(0d);
				InvJLocation modifiedEntity = locationLogicalProcess(entity,
						thisQty);
				// 物料ID
				modifiedEntity.setMaterialId(materialId);
				// DB变更：库位物料记录 INV_J_LOCATION 物料编码 MATERIAL_NO 删除

				/*
				 * // 物料编码 modifiedEntity.setMaterialNo(materialNo);
				 */
				// 仓库编码
				modifiedEntity.setWhsNo(whsNo);
				// 库位编码
				modifiedEntity.setLocationNo(locationNo);
				// 批号
				modifiedEntity.setReserved(0d);
				modifiedEntity.setIsUse(YES);
				modifiedEntity.setEnterpriseCode(employee.getEnterpriseCode());
				saveInvJLocationList.add(modifiedEntity);
				// invJLocationRemote.save(modifiedEntity);
			}
		}

	}

	/**
	 * 批号记录表的更新或添加
	 * 
	 * @param map
	 */
	@SuppressWarnings("unchecked")
	private void updateInvJLot(Map map, List<InvJLot> saveInvJLotList,
			List<InvJLot> updateInvJLotList) {
		// 本次入库数
		Double thisQty = Double.parseDouble(map.get("thisQty").toString());
		// 物料ID
		Long materialId = getLong(map.get("materialId"));
		// 仓库编码
		String whsNo = getString(map.get("whsNo"));
		// 仓位编码
		String locationNo = getString(map.get("locationNo"));
		// 缺省仓库编码
		String defaultWhsNo = getString(map.get("defaultWhsNo"));
		// 缺省库位编码
		String defaultLocationNo = getString(map.get("defaultLocationNo"));
		if (EMPTY.equals(whsNo)) {
			whsNo = defaultWhsNo;
		}
		if (EMPTY.equals(locationNo)) {
			locationNo = defaultLocationNo;
		}
		String lotCodeTemp = "0";
		// 画面.是否批控制
		String isLot = getString(map.get("isLot"));
		if (YES.equals(isLot)) {
			// TODO 批次ＴＥＭＰ = 自动生成批号
			Long maxLotCode = Long.parseLong(remote.findMaxLotNo(materialId)) + 1;
			lotCodeTemp = maxLotCode.toString();
		}
		if (ZERO.equals(lotCodeTemp)) {
			String lotInvId = remote.findInvJLotId(materialId, whsNo,
					locationNo, employee.getEnterpriseCode());
			if (!EMPTY.equals(lotInvId)) {
				InvJLotFacadeRemote invLotRemote = (InvJLotFacadeRemote) factory
						.getFacadeRemote("InvJLotFacade");
				InvJLot entity = invLotRemote
						.findById(Long.parseLong(lotInvId));
				InvJLot modifiedEntity = invJLotLogicalProcess(entity, thisQty);
				updateInvJLotList.add(modifiedEntity);
				// invLotRemote.update(modifiedEntity);
			} else {
				InvJLot entity = new InvJLot();
				entity.setOpenBalance(0d);
				entity.setReceipt(0d);
				entity.setAdjust(0d);
				entity.setIssue(0d);
				InvJLot modifiedEntity = invJLotLogicalProcess(entity, thisQty);
				// 物料ID
				modifiedEntity.setMaterialId(materialId);
				// 批号
				modifiedEntity.setLotNo(lotCodeTemp);
				// 仓库编码
				modifiedEntity.setWhsNo(whsNo);
				// 库位编码
				modifiedEntity.setLocationNo(locationNo);
				// 本期预留
				modifiedEntity.setReserved(0d);
				// 是否使用
				modifiedEntity.setIsUse(YES);
				modifiedEntity.setEnterpriseCode(employee.getEnterpriseCode());
				saveInvJLotList.add(modifiedEntity);
				// invLotRemote.save(modifiedEntity);
			}

		} else {

			InvJLot entity = new InvJLot();
			entity.setOpenBalance(0d);
			entity.setReceipt(0d);
			entity.setAdjust(0d);
			entity.setIssue(0d);
			InvJLot modifiedEntity = invJLotLogicalProcess(entity, thisQty);
			// 物料ID
			modifiedEntity.setMaterialId(materialId);
			// 批号
			modifiedEntity.setLotNo(lotCodeTemp);
			// 仓库编码
			modifiedEntity.setWhsNo(whsNo);
			// 库位编码
			modifiedEntity.setLocationNo(locationNo);
			// 本期预留
			modifiedEntity.setReserved(0d);
			// 是否使用
			modifiedEntity.setIsUse(YES);
			modifiedEntity.setEnterpriseCode(employee.getEnterpriseCode());
			saveInvJLotList.add(modifiedEntity);
			// invLotRemote.save(modifiedEntity);
		}

	}

	private InvJLot invJLotLogicalProcess(InvJLot entity, Double thisQty) {
		String transId = remote.findTransId("P", employee.getEnterpriseCode());
		InvCTransactionFacadeRemote invCTransactionRemote = (InvCTransactionFacadeRemote) factory
				.getFacadeRemote("InvCTransactionFacade");
		InvCTransaction invcTransEntity = invCTransactionRemote.findById(Long
				.parseLong(transId));
		Double openBalance = entity.getOpenBalance();
		Double receipt = entity.getReceipt();
		Double adjust = entity.getAdjust();
		Double issue = entity.getIssue();
		// 是否影响期初TEMP = 'Y'
		if (YES.equals(invcTransEntity.getIsOpenBalance())) {
			entity.setOpenBalance(openBalance + new Double(thisQty));
		}
		if (YES.equals(invcTransEntity.getIsReceive())) {
			entity.setReceipt(receipt + new Double(thisQty));
		}
		if (YES.equals(invcTransEntity.getIsAdjust())) {
			entity.setAdjust(adjust + new Double(thisQty));
		}
		if (YES.equals(invcTransEntity.getIsIssues())) {
			entity.setIssue(issue + new Double(thisQty));
		}
		entity.setLastModifiedBy(employee.getWorkerCode());
		entity.setLastModifiedDate(new Date());
		return entity;
	}

	private InvJWarehouse warehouseLogicalProcess(InvJWarehouse entity,
			Double thisQty) {
		String transId = remote.findTransId(TRANS_NAME, employee
				.getEnterpriseCode());
		InvCTransactionFacadeRemote invCTransactionRemote = (InvCTransactionFacadeRemote) factory
				.getFacadeRemote("InvCTransactionFacade");
		InvCTransaction invcTransEntity = invCTransactionRemote.findById(Long
				.parseLong(transId));
		Double openBalance = entity.getOpenBalance();
		Double receipt = entity.getReceipt();
		Double adjust = entity.getAdjust();
		Double issue = entity.getIssue();
		// 是否影响期初TEMP = 'Y'
		if (YES.equals(invcTransEntity.getIsOpenBalance())) {
			entity.setOpenBalance(openBalance + new Double(thisQty));
		}
		if (YES.equals(invcTransEntity.getIsReceive())) {
			entity.setReceipt(receipt + new Double(thisQty));
		}
		if (YES.equals(invcTransEntity.getIsAdjust())) {
			entity.setAdjust(adjust + new Double(thisQty));
		}
		if (YES.equals(invcTransEntity.getIsIssues())) {
			entity.setIssue(issue + new Double(thisQty));
		}
		entity.setLastModifiedBy(employee.getWorkerCode());
		entity.setLastModifiedDate(new Date());
		return entity;
	}

	private InvJLocation locationLogicalProcess(InvJLocation entity,
			Double thisQty) {
		String transId = remote.findTransId("P", employee.getEnterpriseCode());
		InvCTransactionFacadeRemote invCTransactionRemote = (InvCTransactionFacadeRemote) factory
				.getFacadeRemote("InvCTransactionFacade");
		InvCTransaction invcTransEntity = invCTransactionRemote.findById(Long
				.parseLong(transId));
		Double openBalance = entity.getOpenBalance();
		Double receipt = entity.getReceipt();
		Double adjust = entity.getAdjust();
		Double issue = entity.getIssue();
		// 是否影响期初TEMP = 'Y'
		if (YES.equals(invcTransEntity.getIsOpenBalance())) {
			entity.setOpenBalance(openBalance + new Double(thisQty));
		}
		if (YES.equals(invcTransEntity.getIsReceive())) {
			entity.setReceipt(receipt + new Double(thisQty));
		}
		if (YES.equals(invcTransEntity.getIsAdjust())) {
			entity.setAdjust(adjust + new Double(thisQty));
		}
		if (YES.equals(invcTransEntity.getIsIssues())) {
			entity.setIssue(issue + new Double(thisQty));
		}
		entity.setLastModifiedBy(employee.getWorkerCode());
		entity.setLastModifiedDate(new Date());
		return entity;
	}

	// 入库物资对账查询 add by drdu 090507
	public void findPurchasehouseList() throws JSONException {
		// add by fyyang 100112 增加到货单号、采购单号查询条件
		String purNo = request.getParameter("purNo");
		String arrivalNo = request.getParameter("arrivalNo");

		String startdate = request.getParameter("startdate");
		String enddate = request.getParameter("enddate");
		String materialNo = request.getParameter("materialNo");
		String materialName = request.getParameter("materialName");
		String specNo = request.getParameter("specNo");
		String warehouseName = request.getParameter("warehouseName");
		String supplyName = request.getParameter("supplyName");
		String whsBy = request.getParameter("whsBy");
		String purBy = request.getParameter("purBy");
		// 添加红单查询
		String isRedBill = request.getParameter("redBill");
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj = remote.findPurchasehouseList(employee
				.getEnterpriseCode(), startdate, enddate, materialNo,
				materialName, warehouseName, specNo, supplyName, purBy, whsBy,
				isRedBill, purNo, arrivalNo, start, limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"
				+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}

	// 入库验收查询 add by drdu 090507
	public void getArrivalCheckList() throws JSONException {
		// add by fyyang 100113 到货单号
		String arrialNo = request.getParameter("arrialNo");

		String startDate = request.getParameter("dateFrom");
		String endDate = request.getParameter("dateTo");
		String IsPurchasehouse = request.getParameter("IsPurchasehouse");
		String supplyName = request.getParameter("supplierH");
		String checkPerson = request.getParameter("checkPerson");
		String purBy = request.getParameter("purchaser");
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj = remote.findArrivalCheckList(employee
				.getEnterpriseCode(), startDate, endDate, checkPerson,
				supplyName, IsPurchasehouse, purBy, arrialNo, start, limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"
				+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}

	// 入库验收明细查询 add by drdu 090507
	public void getArrivalCheckDetailListByArrivalNo() throws JSONException {
		String arrivalNo = request.getParameter("headId");
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
		}
		PageObject obj = remote.findArrivalCheckMaterialDetailList(employee
				.getEnterpriseCode(), arrivalNo, start, limit);
		String str = "{total:" + obj.getTotalCount() + ",root:"
				+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
	}

	// 根据物料ID和入库情况查询入库验收明细
	public void getArrivalCheckDetailListByMaterial() throws JSONException {
		String startDate = request.getParameter("dateFrom");
		String endDate = request.getParameter("dateTo");
		String checkPerson = request.getParameter("checkPerson");
		String IsPurchasehouse = request.getParameter("IsPurchasehouse");
		String whsNo = request.getParameter("whsNo");
		String materialNo = request.getParameter("materialNo");
		int start = 0;
		int limit = 99999999;
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject obj = new PageObject();
		if (objstart != null && objlimit != null) {
			start = Integer.parseInt(request.getParameter("start"));
			limit = Integer.parseInt(request.getParameter("limit"));
			obj = remote.findArrivalCheckListByMaterial(employee
					.getEnterpriseCode(), startDate, endDate, checkPerson,
					materialNo, whsNo, IsPurchasehouse, start, limit);
		} else {
			obj = remote.findArrivalCheckListByMaterial(employee
					.getEnterpriseCode(), startDate, endDate, checkPerson,
					materialNo, whsNo, IsPurchasehouse);
		}
		String str = "{total:" + obj.getTotalCount() + ",root:"
				+ JSONUtil.serialize(obj.getList()) + "}";
		write(str);
		
	}

	private String getStringToDouble(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "0";
		}
	}

	private String DateToString(Date date) {
		SimpleDateFormat defaultFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String sysDate = defaultFormat.format(date);
		return sysDate;
	}

	/**
	 * 入库审核查询列表 modify by fyyang 090803
	 * 
	 * @throws JSONException
	 */
	public void getArrivalBillList() throws JSONException {
		// add by fyyang 091215
		String purNo = request.getParameter("purNo");
		String client = request.getParameter("client");
		// add by drdu 091103
		String status = request.getParameter("checkStatus");

		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// 明细部的操作员
		String workCodeName = employee.getWorkerName();
		// 企业编码
		String enterpriseCode = employee.getEnterpriseCode();
		// 加合同号及发票号查询条件
		String contractNo = request.getParameter("contractNo");
		String invoiceNo = request.getParameter("invoiceNo");
		PageObject obj = remote.findArrivalBillList(Integer.parseInt(start),
				Integer.parseInt(limit), workCodeName, status, enterpriseCode,
				contractNo, invoiceNo, purNo, client);
		write(JSONUtil.serialize(obj));
	}

	/**
	 * 入库审核详细信息列表
	 * 
	 * @throws JSONException
	 */
	public void getArrivalBillDetailList() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String id = request.getParameter("id");
		PageObject obj = remote.findArrivalBillDetailList(Long.parseLong(id),
				Integer.parseInt(start), Integer.parseInt(limit), employee
						.getEnterpriseCode());

		write(JSONUtil.serialize(obj));
	}

	/**
	 * 批量审核入库单
	 */
	@SuppressWarnings("unchecked")
	public void checkArrivalBillList() throws JSONException {
		String workCodeName = employee.getWorkerCode();
		String checkData = request.getParameter("checkData");
		List leftDataList = (List) JSONUtil.deserialize(checkData);
		
		//add by drdu 20100408------
		String str = request.getParameter("updateDetailData");
		Object object = JSONUtil.deserialize(str);
		//-------------------
		
		Boolean result = remote.checkArrivalBillList(leftDataList, workCodeName,(List<Map>)object);
		if (result) {
			write(Constants.ADD_SUCCESS);
		}
	}

	/**
	 * 批量取消审核入库单 add by drdu 091103
	 * 
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void cancelCheckArrivalBillList() throws JSONException {
		String workCodeName = employee.getWorkerCode();
		String checkData = request.getParameter("checkData");
		List leftDataList = (List) JSONUtil.deserialize(checkData);
		Boolean result = remote.cancelCheckArrival(leftDataList, workCodeName);
		if (result) {
			write("{success:true,msg:'取消审核成功！'}");
		}
	}

	/**
	 * 查询“取消出库”列表 add by drdu 090708
	 * 
	 * @throws JSONException
	 */
	public void findCancelPurchasewarehouseList() throws JSONException {
		String arrivalNo = request.getParameter("arrivalNo");
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
		obj = remote.findCancelPurchaseWarehouseList(employee
				.getEnterpriseCode(), arrivalNo, materialId, start, limit);
		if (obj == null || obj.getList() == null) {
			write("{\"list\":[],\"totalCount\":0}");
		} else {
			write(JSONUtil.serialize(obj));
		}
	}

	/**
	 * 出库回滚 修改库存物料记录表、领料单明细表、事务历史表、库位物料记录、批号记录表中的出库数量 add by drdu 090708
	 */
	public void updatePurchasewarehouseQty() {
		Long tHisId = Long.parseLong(request.getParameter("tHisId"));
		// Long qty = Long.parseLong(request.getParameter("qty"));
		// modify by fyyang 091109 数量改为小数点后4位
		Double qty = Double.parseDouble(request.getParameter("qty"));
		remote.updatePurchaseWarehouseQty(employee.getEnterpriseCode(), tHisId,
				qty, employee.getWorkerCode());

		write("{success:true,msg:'修改成功！'}");
	}

	/**
	 * 根据到货单号检索采购入库页面中补打印列表记录 add by drdu 091120
	 * 
	 * @throws JSONException
	 */
	public void findAfterPrintList() throws JSONException {
		String arrivalNo = request.getParameter("arrivalNo");
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
		obj = remote.findAfterPrintList(arrivalNo, start, limit, employee
				.getEnterpriseCode());
		if (obj == null || obj.getList() == null) {
			write("{\"list\":[],\"totalCount\":0}");
		} else {
			write(JSONUtil.serialize(obj));
		}
	}

	/**
	 * 查询已经审核过的采购单列表 add by fyyang 091224
	 * 
	 * @throws JSONException
	 */
	public void queryCheckedPurList() throws JSONException {
		String sdate = request.getParameter("sdate");
		String edate = request.getParameter("edate");
		String purNo = request.getParameter("purNo");
		String buyer = request.getParameter("buyer");
		String invoiceNo = request.getParameter("invoiceNo");
		PageObject obj = new PageObject();

		String strStart = request.getParameter("start");
		String strlimit = request.getParameter("limit");

		if (strStart != null && strlimit != null) {
			obj = remote.queryCheckedPurList(sdate, edate, purNo, buyer,
					invoiceNo, employee.getEnterpriseCode(), Integer
							.parseInt(strStart), Integer.parseInt(strlimit));
		} else {
			obj = remote.queryCheckedPurList(sdate, edate, purNo, buyer,
					invoiceNo, employee.getEnterpriseCode());
		}
		write(JSONUtil.serialize(obj));
	}
}
