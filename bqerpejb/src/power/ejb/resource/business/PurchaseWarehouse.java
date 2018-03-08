package power.ejb.resource.business;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.resource.InvCMaterial;
import power.ejb.resource.InvJLocation;
import power.ejb.resource.InvJLot;
import power.ejb.resource.InvJTransactionHis;
import power.ejb.resource.InvJWarehouse;
import power.ejb.resource.PurJArrival;
import power.ejb.resource.PurJArrivalDetails;
import power.ejb.resource.PurJOrderDetails;
import power.ejb.resource.form.PurchaseWarehouseDetailInfo;

@Remote 
public interface PurchaseWarehouse{
	/**
	 * 画面初始化数据检索
	 */
	public PageObject findPurchaseOrderList(String fuzzy,int start, int limit,String operator,String enterpriseCode);
	/**
	 * 获取事务名称
	 * 
	 */
	public String findTransName(String transCode,String enterpriseCode);
	/**
	 * 获取事务流水号
	 * 
	 */
	public String findTransId(String transCode,String enterpriseCode);
	/**
	 * 获取仓库编码和名称
	 */
	public PageObject findAllWarehouse(String flag,String enterpriseCode);
	/**
	 * 获取仓位编码和名称
	 */
	public PageObject  findLocation(String whsNo,String enterpriseCode);
	/**
	 * 入库详细数据
	 */
	public PageObject findPurchaseOrderDetailList(Long id,int start,int limit,String enterpriseCode);
	/**
	 * 仓库名称
	 * @param whsNo
	 * @return
	 */
	public String findWarehouseName(String whsNo,String enterpriseCode);
	/**
	 * 库位名称获取
	 * 
	 */
	public String findLocationName(String whsNo,String locationNo,String enterpriseCode);
	/**
	 * 仓库保管人获取
	 */
	public String findWarehouseSaveName(String whsNo,String enterpriseCode);
	
	/**
	 * 库存物料的数量
	 */
	public String findWarehouseQty(Long materialId,String enterpriseCode);
	/**
	 * 确认当前的仓库中是否有该种物料
	 */
	public String findInvJWarehouse(Long materialId,String whsNo,String enterpriseCode);
	/**
	 * 确认当前的库位中是否有该种物料
	 */
	public String findInvJLocation(Long materialId,String whsNo,String locationNo,String enterpriseCode);
	/**
	 * 批号记录表的ID
	 */
	public String findInvJLotId(Long materialId,String whsNo,String locationNo,String enterpriseCode);
	/**
	 * 库位所有记录检索
	 */
	public String findLocationAllRecord(String enterpriseCode);
	/**
	 * 到货入库管理所有数据库操作
	 */
	public void saveORUpdateAllDB(PurJArrival purjArrivalBean,List<PurJArrivalDetails> updatePurJArrivalDetailList,List<PurJOrderDetails> updatePurJOrderDetailList,
			List<InvJTransactionHis> saveInvJTransactionHisList,List<InvJWarehouse> saveInvJWarehouseList,List<InvJWarehouse> updateInvJWarehouseList,
			List<InvJLocation> saveInvJLocationList,List<InvJLocation> updateInvJLocationList,List<InvJLot> saveInvJLotList,List<InvJLot> updateInvJLotList,
			List<InvCMaterial> updateInvCMaterialList);
	/**
	 * 获取汇率
	 */
	public String findExchangeRate(Long currentId,String enterPriseCode,Long dstCurrencyId);
	/**
	 * 获取人员信息
	 */
	public String findPersonInfo(String empCode);
	/**
	 * 批号记录表的最大批号
	 * 
	 */
	public String findMaxLotNo(Long materialId);
	
	
	/**
	 * 到货验收页面的到货单信息列表
	 * @param fuzzy 到货单号
	 * @param start
	 * @param limit
	 * @param operator
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject findArrivalListForArrivalCheck(String fuzzy, int start, int limit,
			String operator, String enterpriseCode);
	
	
	/**
	 * 到货验收物资详细信息检索
	 * @param id 到货单id
	 * @param start
	 * @param limit
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject findMaterialDetailListForArrivalCheck(Long id, int start,
			int limit, String enterpriseCode);
	
	/**
	 * 入库物资对账查询列表
	 * @param enterpriseCode
	 * @param sDate 入库时间段
	 * @param eDate
	 * @param materialNo 物资编码
	 * @param materialName物资名称
	 * @param whsName仓库名称
	 * @param specNo规格型号
	 * @param supplierName供应商
	 * @param purBy采购人员
	 * @param whsBy入库人人员
	 * @param rowStartIdxAndCount
	 * @return
	 * 
	 * add by drdu  @@@20090507
	 * 
	 */
	public PageObject findPurchasehouseList(String enterpriseCode,String sDate, String eDate,
			String materialNo,String materialName,String whsName,String specNo,String supplierName,
			String purBy,String whsBy,String isRedBill,String purNo,String arrivalNo,final int...rowStartIdxAndCount);
	
	/**
	 * 根据到货单查询物料明细列表
	 * @param enterpriseCode
	 * @param sDate 验收时间段
	 * @param eDate 
	 * @param supplierName 供应商
	 * @param purBy 采购人员
	 * @param rowStartIdxAndCount
	 * @return
	 * 
	 * add by ywliu  200905011
	 * 
	 */
	public PageObject findArrivalCheckMaterialDetailList(String enterpriseCode,String arrivalNo ,final int...rowStartIdxAndCount);
	
	/**
	 * 入库验收查询列表
	 * @param enterpriseCode
	 * @param sDate 验收时间段
	 * @param eDate 
	 * @param supplierName 供应商
	 * @param purBy 采购人员
	 * @param arrialNo 到货单号 add by fyyang 20100113
	 * @param rowStartIdxAndCount
	 * @return
	 * 
	 * add by ywliu  200905011
	 * 
	 */
	public PageObject findArrivalCheckList(String enterpriseCode,String sDate, String eDate,
			String checkPerson,String supplierName,String IsPurchasehouse,
			String purBy,String arrialNo,final int...rowStartIdxAndCount);
	
	/**
	 * 根据物料ID和入库情况查询入库验收明细
	 * @param enterpriseCode
	 * @param sDate 验收时间段
	 * @param eDate 
	 * @param MaterialNo 物料编码
	 * @param IsPurchasehouse 入库情况
	 * @param rowStartIdxAndCount
	 * @return
	 * 
	 * add by ywliu  200905011
	 * 
	 */
	public PageObject findArrivalCheckListByMaterial(String enterpriseCode,String sDate, String eDate,
			String checkPerson,String materialNo,String whsNo,String IsPurchasehouse,
			final int...rowStartIdxAndCount);
	
	/**
	 * modified by liuyi 20100430
	 * 取得收料单报表数据
	 * @param purNo 采购单号
	 * @param whsNo 仓库号
	 * @param metailIdNotIn 来源为固定资产类的物资
	 * @return
	 */
//	public List<PurchaseWarehouseDetailInfo> getReceiptBillMaterialInfo(String purNo, String whsNo, String arrivalNo);
	public List<PurchaseWarehouseDetailInfo> getReceiptBillMaterialInfo(String purNo, String whsNo, String arrivalNo,String metailIdNotIn);
	
	/**
	 * 查询入库审核列表
	 * modify by fyyang 090803 增加合同号及发票号查询条件
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * modify by drdu 091103 新增加审核状态查询条件
	 */
	public PageObject findArrivalBillList(int start, int limit,String operator,String checkStatus, String enterpriseCode,String contractNo,String invoiceNo,String purNo,String clientName);
	
	/**
	 * 查询入库审核详细信息列表
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findArrivalBillDetailList(Long id,int start,int limit,String enterpriseCode);
	
	/**
	 * 批量审核入库单
	 * @param checkList
	 * update by drdu 20100408
	 */
	public Boolean checkArrivalBillList(List checkList,String workCodeName,List<Map> list);
	
	/**
	 * 批量取消审核入库单
	 * add by drdu 091103
	 * @param checkList
	 * @param workCodeName
	 * @return
	 */
	public Boolean cancelCheckArrival(List checkList,String workCodeName);
	/**
	 * 查询取消入库列表
	 * add by ywliu 090709
	 * @param enterpriseCode
	 * @param issueNo
	 * @param materialId
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findCancelPurchaseWarehouseList(String enterpriseCode,String issueNo,String materialId,final int... rowStartIdxAndCount);
	
	/**
	 * 修改数量（入库回滚操作）
	 * add by ywliu 090709
	 * @param enterpriseCode
	 * @param tHisId
	 * @param qty
	 */
	public void updatePurchaseWarehouseQty(String enterpriseCode,Long tHisId,Double qty,String workCode);
	
	/**
	 * 采购入库页面中补打印列表
	 * add by drdu 091120
	 * @param arrivalNo
	 * @param start
	 * @param limit
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject findAfterPrintList(String arrivalNo, int start, int limit, String enterpriseCode);
	
	/**
	 * modified by liuyi 20100430 
	 *  采购入库中补打印报表数据信息
	 * add by drdu 091120
	 * @param operateDate
	 * @param arrivalNo
	 * @param whsNo
	 * @param metailIdNotIn 来源为固定资产类的物资
	 * @return 
	 */
//	public List<PurchaseWarehouseDetailInfo> getAfterReceiptBillMaterialInfo(String operateDate,String arrivalNo,String whsNo);	
	public List<PurchaseWarehouseDetailInfo> getAfterReceiptBillMaterialInfo(String operateDate,String arrivalNo,String whsNo,String metailIdNotIn);

	/**
	 * 查询已经审核过的采购单列表
	 * add by fyyang 091224
	 * @param sdate
	 * @param edate
	 * @param purNo
	 * @param buyer
	 * @param invoiceNo
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject queryCheckedPurList(String sdate,String edate,String purNo,String buyer,String invoiceNo,String enterpriseCode,final int... rowStartIdxAndCount);
	
	
	/**
	 * add by liuyi 20100430
	 * 取得固定资产类收料单报表数据
	 * @param purNo 采购单号
	 * @param whsNo 仓库号
	 * @param materialId 来源为固定资产类的物资
	 * @return
	 */
	public List<PurchaseWarehouseDetailInfo> getReceiptGdMaterialInfo(String purNo, String whsNo, String arrivalNo,String materialId);
	
	/**
	 * modified by liuyi 20100430 
	 *  采购入库中补打印报表数据信息  固定资产类
	 * @param operateDate
	 * @param arrivalNo
	 * @param whsNo
	 * @param materialId 来源为固定资产类的物资
	 * @return 
	 */
	public List<PurchaseWarehouseDetailInfo> getAfterReceiptGdMaterialInfo(String operateDate,String arrivalNo,String whsNo,String materialId);

}
