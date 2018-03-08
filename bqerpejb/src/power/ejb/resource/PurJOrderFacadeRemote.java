package power.ejb.resource;

import java.util.List;
import java.util.Map;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PurJOrderFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PurJOrderFacadeRemote {

    /**
     * 增加一条记录
     *
     * @param entity 要增加的记录
     * @return entity 增加后记录
     */
	public PurJOrder save(PurJOrder entity);

    /**
     * 删除一条记录
     *
     * @param entityId 采购单流水号
     * @throws RuntimeException when the operation fails
     */
	public void delete(Long entityId);

    /**
     * 修改记录
     *
     * @param entity 要修改的记录
     * @return entity 修改后记录
     */
	public PurJOrder update(PurJOrder entity);

	/**
	 * 通过采购单流水号得到采购单
	 * 
	 * @param id 采购单流水号
     * @return PurJOrder 采购单
	 */
	public PurJOrder findById(Long id);

	/**
	 * 通过采购单编号得到采购单
	 * 
	 * @param purNo 采购单编号
     * @return List<PurJOrder> 采购单
	 */
	public List<PurJOrder> findByPurNo(Object purNo);

	/**
	 * 查找所有的采购单
	 * 
	 * @return List<PurJOrder> 采购单
	 */
	public List<PurJOrder> findAll();

	/**
	 * 模糊查询采购单
	 *
	 * @param argFuzzy 模糊查询字段
	 * @param enterpriseCode 企业编码
	 * @param argShowAll 是否显示所有订单
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return PageObject 符合条件的采购单
	 */
	public PageObject findByFuzzy(String argFuzzy, String enterpriseCode, boolean argShowAll, final int... rowStartIdxAndCount);

	/**
	 * 通过采购单流水号得到采购单
	 * 
	 * @param argOrderId 采购单流水号
	 * @param enterpriseCode 企业编码
	 * @return PurJOrderRegister 符合条件的采购单
	 */
	public PurJOrderRegister findByOrderId(String argOrderId, String enterpriseCode);
	
	/**
	 * 增加采购单
	 * @param argOrder 需要增加的采购单信息
	 * @param argPlanedDetail 需要更新的采购单明细对象(需求单)信息
	 * @param argUnplanedDetail 需要更新的采购单明细对象(行政单)信息
	 * @param argPlans 需要更新的需求物资信息
	 * @return PurJOrder 增加的采购单
	 */
	public PurJOrder addOrderData(PurJOrder argOrder,
			Map<String, List<Map<String, Object>>> argPlanedDetail,
			Map<String, List<PurJOrderDetails>> argUnplanedDetail,
			Map<String, List<MrpJPlanRequirementDetail>> argPlans);
	
	/**
	 * 修改采购单
	 * @param argOrder 需要修改的采购单信息
	 * @param argPlanedDetail 需要更新的采购单明细对象(需求单)信息
	 * @param argUnplanedDetail 需要更新的采购单明细对象(行政单)信息
	 * @param argPlans 需要更新的需求物资信息
	 */
	public void updateOrderData(PurJOrder argOrder,
			Map<String, List<Map<String, Object>>> argPlanedDetail,
			Map<String, List<PurJOrderDetails>> argUnplanedDetail,
			Map<String, List<MrpJPlanRequirementDetail>> argPlans);

	/**
	 * 删除采购单
	 * @param argOrder 需要删除的采购单信息
	 * @param argPlanedDetail 需要删除的采购单明细对象(需求单)信息
	 * @param argUnplanedDetail 需要删除的采购单明细对象(行政单)信息
	 * @param argPlans 需要更新的需求物资信息
	 */
	public void deleteOrderData(PurJOrder argOrder,
			Map<String, List<Map<String, Object>>> argPlanedDetail,
			Map<String, List<PurJOrderDetails>> argUnplanedDetail,
			Map<String, List<MrpJPlanRequirementDetail>> argPlans);
	

	/**
	 * 条件查询采购单
	 *
	 * @param argFuzzy 模糊查询字段
	 * @param enterpriseCode 企业编码
	 * @param argShowAll 是否显示所有订单
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return PageObject 符合条件的采购单
	 * @author yiliu
	 */
	public PageObject findByQuery(String strQueryTimeFrom, String strQueryTimeTo, String buyer, String purNo, 
			String enterpriseCode, final int... rowStartIdxAndCount);
	
	/**
	 * 采购信息查询
	 *
	 * @param argFuzzy 模糊查询字段
	 * @param enterpriseCode 企业编码
	 * @param argShowAll 是否显示所有订单
	 * @param supplyName 供应商名称 add by fyyang 20100114
	 * @param rowStartIdxAndCount 动态参数(开始行数和查询行数)
	 * @return PageObject 符合条件的采购单物资明细
	 * @author ywliu
	 */
	public PageObject getOrdersMaterial(String strQueryTimeFrom, String strQueryTimeTo, String buyer, String materialNo, String materialName,
			String specNo,String enterpriseCode,String supplyName, final int... rowStartIdxAndCount);
}