package power.ejb.resource;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PurJOrderDetailsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PurJOrderDetailsFacadeRemote {

    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @return entity 增加后记录
     */
	public PurJOrderDetails save(PurJOrderDetails entity);
	
	/**
	 * 增加一条记录(多次增加时用)
	 *
	 * @param entity 要增加的记录
	 * @param argId 上次增加记录的流水号
	 * @return Long 增加后记录的流水号
	 */
	public Long save(PurJOrderDetails entity, Long argId);
	
    /**
     * 删除一条记录
     *
     * @param argPurOrderDetailsId 采购单明细流水号
     * @throws RuntimeException when the operation fails
     */
	public void delete(Long argPurOrderDetailsId);
	

	/**
	 * 删除一条记录
	 *
	 * @param entity 采购单明细
	 * @throws RuntimeException when the operation fails
	 */
	public void delete(PurJOrderDetails entity);

    /**
     * 修改记录
     *
     * @param entity 要修改的记录
     * @return entity 修改后记录
     */
	public PurJOrderDetails update(PurJOrderDetails entity);

	/**
	 * 通过采购单明细流水号得到采购单明细
	 * 
	 * @param id 采购单明细流水号
     * @return PurJOrderDetails 采购单明细
	 */
	public PurJOrderDetails findById(Long id);

	/**
	 * 通过采购单编号得到采购单明细
	 *
	 * @param argEnterpriseCd 企业编码
	 * @param purNo 采购单编号
	 * @param argSupplier 供应商编号
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 采购单明细
	 */
	public PageObject findByPurNo(String argEnterpriseCd, String purNo, String argSupplier, final int... rowStartIdxAndCount);


	/*
	 * 通过采购单编号得到采购单明细
	 * @param argEnterpriseCd 企业编码
	 * @param purNo 采购单编号
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return PageObject 采购单明细
	 */
	public PageObject findDetailByPurNo(String argEnterpriseCd, String purNo, final int... rowStartIdxAndCount);
	/**
	 * 后期维护比较用的数据检索
	 *
	 * @param argEnterpriseCd 企业编码
	 * @param purNo 采购单编号
	 * @param argSupplier 供应商编号
	 * @return List<PurJOrderRegister> 采购单明细
	 */
	public List<PurJOrderRegister> getMeasureByPurNo(String argEnterpriseCd, String purNo, String argSupplier);
	
	/**
	 * 通过币别和供应商编号取得汇率
	 * @param argEnterpriseCd 企业编码
	 * @param argCurrencyId 币别
	 * @param argSupplier 供应商编号
	 * @return
	 */
	public String getExchangeRate(String argEnterpriseCd, String argCurrencyId, String argSupplier);

	/**
	 * 通过物料ID和供应商编号取得报价信息
	 * @param argEnterpriseCd 企业编码
	 * @param argMaterialId 物料ID
	 * @param argSupplier 供应商编号
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return PageObject 报价信息
	 */
	public PageObject getUnitPrice(String argEnterpriseCd, String argMaterialId, String argSupplier
			, final int... rowStartIdxAndCount);

	/**
	 * 得到物料的当前库存
	 * @param argEnterpriseCd 企业编码
	 * @param argMaterialIds 物料ID(复数)
	 * @return 物料的当前库存
	 */
	public Map<String, Double> getCurrentMatCounts(String argEnterpriseCd, String argMaterialIds);
	
	/**
	 * 通过采购员ID和供应商编号取得需求计划明细数据
	 * @param argEnterpriseCd 企业编码
	 * @param argBuyer 采购员ID
	 * @param argSupplier 供应商编号
	 * @param argIsSelfPlan 是否检索当前采购员的需求计划明细数据
	 * @return 符合条件的需求计划明细数据
	 */
	public List<PurJOrderRegister> getPlans(String argEnterpriseCd, String argBuyer,
			String argSupplier, boolean argIsSelfPlan);
	
	/**
	 * 通过物料ID得到采购单明细
	 *
	 * @param materialId 物料ID
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return List<PurJOrderDetails> 采购单明细
	 */
	public List<PurJOrderDetails> findByMaterialId(Object materialId, final int... rowStartIdxAndCount);
	
	/**
	 * 通过生产厂家得到采购单明细
	 *
	 * @param factory 生产厂家
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
     * @return List<PurJOrderDetails> 采购单明细
	 */
	public List<PurJOrderDetails> findByFactory(Object factory, final int... rowStartIdxAndCount);

	/**
	 * 查找所有的采购单明细
	 * 
	 * @return List<PurJOrderDetails> 采购单明细
	 */
	public List<PurJOrderDetails> findAll();

	/**
	 * 查询一条无参数sql语句,返回查询结果 
	 * @param argSql SQL语句
	 * @param argClass JavaBean class对象
	 * @param rowStartIdxAndCount  动态参数(开始行数和查询行数)
	 * @return 符合条件的List对象
	 */
	@SuppressWarnings("unchecked")
	public List queryDescribeByNativeSQL(String argSql, Class<?> argClass, final int ...rowStartIdxAndCount);
	
	/**
	 * 采购单信息查询
	 *
	 * @param purNo 采购单号
	 * @param enterpriseCode 企业编码
     * @return PurchaseBean 采购单明细
	 */
	public PurchaseBean findAllForPurchase(String purNo,String enterpriseCode);
	
	public PurchaseListBean findOrdersByDetailID(Long requirementDetailId);
	
	/**
	 * 发票补录 更改采购数量方法 add by sychen 20100427
	 * @param list
	 */
	public void updatePurQty(List<Map> list);
}