package power.web.resource.arrivalcheck.action;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJTransactionHisFacadeRemote;
import power.ejb.resource.PurJArrival;
import power.ejb.resource.PurJArrivalDetails;
import power.ejb.resource.PurJArrivalDetailsFacadeRemote;
import power.ejb.resource.PurJArrivalFacadeRemote;
import power.ejb.resource.business.PurchaseWarehouse;
import power.web.comm.AbstractAction;

/**
 * 到货验收
 * @author fyyang 
 * 090422
 */
public class ArrivalCheckAction extends AbstractAction{

	protected PurchaseWarehouse remote;
	protected PurJArrivalFacadeRemote purJArrivalRemote;
	protected PurJArrivalDetailsFacadeRemote purJArrivalDetailsRemote;
	/** 事务历史表远程对象 */
	protected InvJTransactionHisFacadeRemote transHisRemote;
	public ArrivalCheckAction()
	{
		remote = (PurchaseWarehouse) factory
		.getFacadeRemote("PurchaseWarehouseImpl");
		purJArrivalDetailsRemote=(PurJArrivalDetailsFacadeRemote)factory.getFacadeRemote("PurJArrivalDetailsFacade");
		purJArrivalRemote = (PurJArrivalFacadeRemote) factory
		.getFacadeRemote("PurJArrivalFacade");
		// 事务历史表远程对象
		transHisRemote = (InvJTransactionHisFacadeRemote) factory
				.getFacadeRemote("InvJTransactionHisFacade");
	}
	
	/**
	 * 到货验收的到货单列表
	 * @throws JSONException
	 */
	public void getArrivalListForArrivalCheck() throws JSONException {
		// 查询值
		String fuzzy = request.getParameter("fuzzy");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		// 明细部的操作员
		String workCodeName = employee.getWorkerName();
		// 企业编码
		String enterpriseCode = employee.getEnterpriseCode();
		PageObject obj = remote.findArrivalListForArrivalCheck(fuzzy, Integer
				.parseInt(start), Integer.parseInt(limit), workCodeName,
				enterpriseCode);
		write(JSONUtil.serialize(obj));
	}
	
	
	/**
	 * 到货验收物资详细信息列表
	 * @throws JSONException
	 */
	public void getArrivalDetailListForArrivalCheck() throws JSONException {
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String id = request.getParameter("id");
		PageObject obj = remote.findMaterialDetailListForArrivalCheck(Long.parseLong(id),
				Integer.parseInt(start), Integer.parseInt(limit), employee
						.getEnterpriseCode());

		write(JSONUtil.serialize(obj));
	}
	
	@SuppressWarnings("unchecked")
	public void updateForArrivalCheck() throws JSONException
	{
		String id = request.getParameter("id");
		// 到货登记/验收表备注
		String detailMemo = request.getParameter("detailMemo");
		// 备注是否修改 1：修改 0：未修改
		String memoFlag = request.getParameter("memoFlag");
		// 到货物资明细数据
		String str = request.getParameter("data");
		 Object obj = JSONUtil.deserialize(str);
		 List<Map> list = (List<Map>)obj;
		   for(Map data : list)
		   {
			   String itemStatus = ((Map) ((Map) data)).get("itemStatus").toString();
			   String detailId=((Map) ((Map) data)).get("id").toString();
			   if(!detailId.equals(""))
			   {
				   PurJArrivalDetails model= purJArrivalDetailsRemote.findById(Long.parseLong(detailId));
				   model.setItemStatus(itemStatus);
				   purJArrivalDetailsRemote.update(model);
			   }
		   }
		   
		   if(memoFlag.equals("1"))
		   {
			   PurJArrival entity= purJArrivalRemote.findById(Long.parseLong(id));
			   entity.setMemo(detailMemo);
			   entity.setLastModifiedBy(employee.getWorkerCode());
			   entity.setLastModifiedDate(new Date());
			   purJArrivalRemote.update(entity);
		   }
		   
		   write("{success:true,msg:'OK'}");
		   
		
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
	private String getStringToDouble(Object obj) {
		if (obj != null) {
			return obj.toString();
		} else {
			return "0";
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
	private String DateToString(Date date) {
		SimpleDateFormat defaultFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		String sysDate = defaultFormat.format(date);
		return sysDate;
	}
//	private void saveInvJTransactionHisTable(Map map, Map leftMap,
//			List<InvJTransactionHis> saveInvJTransactionHisList) {
//		// 采购订单明细表.流水号
//		Long purOrderDetailsId = (Long) map.get("purOrderDetailsId");
//		// 本次入库数
//		Double thisQty = Double.parseDouble(map.get("thisQty").toString());
//		// 物料ID
//		Long materialId = getLong(map.get("materialId"));
//		// 批次ＴＥＭＰ
//		String lotCodeTemp = "0";
//	
//		// 登陆事务历史表
//		Double warehouseQty = Double.parseDouble(tempQty);
//		// 物料主文件.标准成本
//		Double stdCost = Double.parseDouble(getStringToDouble(map
//				.get("stdCost")));
//		// 画面.单价
//		Double unitPrice = Double.parseDouble(getStringToDouble(map
//				.get("unitPrice")));
//		// 画面.计划价格
//		Double frozenCost = Double.parseDouble(getStringToDouble(map
//				.get("frozenCost")));
//		// 标准成本ＴＥＭＰ
//		Double stdCostTemp = (stdCost * warehouseQty + unitPrice * thisQty)
//				/ (warehouseQty + thisQty);
//		// 画面.计价方式
//		String costMethod = getString(map.get("costMethod"));
//		// 事务标准成本ＴＥＭＰ
//		Double transStdCostTemp = 0d;
//		if (TWO.equals(costMethod)) {
//			transStdCostTemp = new Double(unitPrice);
//		} else if (THERE.equals(costMethod)) {
//			transStdCostTemp = new Double(frozenCost);
//		} else {
//			transStdCostTemp = stdCostTemp;
//		}
//
//		// 画面.税率
//		String taxRate = getString(map.get("taxRate"));
//		// 画面.币种
//		String currencyType = getString(map.get("currencyType"));
//		// 画面.存货计量单位
//		Long stockUmId = Long
//				.parseLong(getStringToDouble((map.get("stockUmId"))));
//
//		// 仓库编码
//		String whsNo = getString(map.get("whsNo"));
//		// 仓位编码
//		String locationNo = getString(map.get("locationNo"));
//		// 缺省仓库编码
//		String defaultWhsNo = getString(map.get("defaultWhsNo"));
//		// 缺省库位编码
//		String defaultLocationNo = getString(map.get("defaultLocationNo"));
//		if (whsNo.equals(EMPTY)) {
//			whsNo = defaultWhsNo;
//		}
//		if (locationNo.equals(EMPTY)) {
//			locationNo = defaultLocationNo;
//		}
//
//		// 画面．供应商编码
//		String supplier = "";
//		// 画面．供应商全称
//		String supplyName = "";
//		if (leftMap != null) {
//			supplier = getStringToDouble(leftMap.get("supplier"));
//			supplyName = getStringToDouble(leftMap.get("supplyName"));
//		}
//		String gridMemo = getString(map.get("gridMemo"));
//
//		InvJTransactionHis transHisEntity = new InvJTransactionHis();
//		// 式样变更 单据编号--》订单编号
//		String purNo = getString(leftMap.get("purNo"));
//		// 单号 = 画面.单据编号
//		transHisEntity.setOrderNo(purNo);
//		// 序号 = 画面.流水号
//		transHisEntity.setSequenceId(purOrderDetailsId);
//		// 事务类型 =‘P’
//
//		transHisEntity.setTransId(Long.parseLong(remote.findTransId("P",
//				employee.getEnterpriseCode())));
//		// 物料编码
//		transHisEntity.setMaterialId(materialId);
//		// 批号 ＝ 批次ＴＥＭＰ
//		transHisEntity.setLotNo(lotCodeTemp);
//		// 异动数量 ＝ 画面.入库数
//		transHisEntity.setTransQty(new Double(thisQty));
//		// 备注 = 画面.备注
//		transHisEntity.setMemo(gridMemo);
//		// 物料单价 ＝ 画面.单价
//		transHisEntity.setPrice(new Double(unitPrice));
//		if(!EMPTY.equals(taxRate)){
//		// 税率 ＝ 画面.税率
//		transHisEntity.setTaxRate(new Double(taxRate));
//		}
//		// 事务标准成本 ＝ 事务标准成本ＴＥＭＰ
//		transHisEntity.setStdCost(transStdCostTemp);
//		// 本币 ＝ 系统配置文件中取得（共通的方法读取配置文件）
//		 Long currentId = Long.parseLong(managerRemote.getOriCurrency()); 
//		// 
//		transHisEntity.setOriCurrencyId(currentId);
//		if(!EMPTY.equals(currencyType)){
//		// 交易币种 ＝ 画面.币种
//		transHisEntity.setCurrencyId(Long.parseLong(currencyType));
//		}
//		// 汇率 
//		// 对应bug 
//		if(!EMPTY.equals(currencyType)){
//		String exchangeRate = remote.findExchangeRate(currentId,employee.getEnterpriseCode(),Long.parseLong(currencyType));
//			if(!ZERO.equals(exchangeRate)){
//				transHisEntity.setExchangeRate(Double.parseDouble(exchangeRate));
//			}
//		}
//		// 计量单位 ＝ 画面.存货计量单位
//		transHisEntity.setTransUmId(stockUmId);
//		// 操作仓库 ＝ 画面．仓库
//
//		transHisEntity.setFromWhsNo(whsNo);
//		// 操作库位 ＝ 画面．库位
//		transHisEntity.setFromLocationNo(locationNo);
//		// 供应商编码 ＝ 画面．供应商编码
//		transHisEntity.setSupplier(Long.parseLong(supplier));
//		// 供应商 ＝ 画面．供应商全称
//		transHisEntity.setCustomerNo(supplyName);
//
//		// 操作人 = 当前用户
//		transHisEntity.setLastModifiedBy(employee.getWorkerCode());
//		// 操作日期 = 系统时间
//		transHisEntity.setLastModifiedDate(new Date());
//		// 企业代码 = 画面.企业代码(session)
//		transHisEntity.setEnterpriseCode(employee.getEnterpriseCode());
//		// 是否使用 = 'Y'
//		transHisEntity.setIsUse(YES);
//		transHisEntity.setTransHisId(transHisRemote.getMaxId());
//		saveInvJTransactionHisList.add(transHisEntity);
//		// transHisRemote.save(transHisEntity);
//
//	}

}
