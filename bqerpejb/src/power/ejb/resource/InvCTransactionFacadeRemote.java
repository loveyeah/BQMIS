package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvCTransactionFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvCTransactionFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvCTransaction entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvCTransaction entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvCTransaction entity);

	/**
	 * Delete a persistent InvCTransaction entity.
	 * 
	 * @param entity
	 *            InvCTransaction entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvCTransaction entity);

	/**
	 * Persist a previously saved InvCTransaction entity and return it or a copy
	 * of it to the sender. A copy of the InvCTransaction entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            InvCTransaction entity to update
	 * @return InvCTransaction the persisted InvCTransaction entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvCTransaction update(InvCTransaction entity);

	public InvCTransaction findById(Long id);

	/**
	 * Find all InvCTransaction entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvCTransaction property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvCTransaction> found by query
	 */
	public List<InvCTransaction> findByProperty(String propertyName,
			Object value);

	public List<InvCTransaction> findByTransCode(Object transCode);

	public List<InvCTransaction> findByTransName(Object transName);

	public List<InvCTransaction> findByTransDesc(Object transDesc);

	public List<InvCTransaction> findByIsOpenBalance(Object isOpenBalance);

	public List<InvCTransaction> findByIsReceive(Object isReceive);

	public List<InvCTransaction> findByIsAdjust(Object isAdjust);

	public List<InvCTransaction> findByIsIssues(Object isIssues);

	public List<InvCTransaction> findByIsReserved(Object isReserved);

	public List<InvCTransaction> findByIsInspection(Object isInspection);

	public List<InvCTransaction> findByIsSaleAmount(Object isSaleAmount);

	public List<InvCTransaction> findByIsEntryCost(Object isEntryCost);

	public List<InvCTransaction> findByIsPoCost(Object isPoCost);

	public List<InvCTransaction> findByIsAjustCost(Object isAjustCost);

	public List<InvCTransaction> findByIsActualCost(Object isActualCost);

	public List<InvCTransaction> findByIsCheckPo(Object isCheckPo);

	public List<InvCTransaction> findByIsPoQuantity(Object isPoQuantity);

	public List<InvCTransaction> findByIsShopOrder(Object isShopOrder);

	public List<InvCTransaction> findByIsCheckShopOrder(Object isCheckShopOrder);

	public List<InvCTransaction> findByIsShopOrderIssue(Object isShopOrderIssue);

	public List<InvCTransaction> findByLastModifiedBy(Object lastModifiedBy);

	public List<InvCTransaction> findByEnterpriseCode(Object enterpriseCode);

	public List<InvCTransaction> findByIsUse(Object isUse);

	/**
	 * Find all InvCTransaction entities.
	 * 
	 * @return List<InvCTransaction> all InvCTransaction entities
	 */
	public List<InvCTransaction> findAll();
	/**
	 * 通过事务编码查询事务信息
	 * @param code 事务编码
	 * @return 事务信息
	 */
	public InvCTransaction findByCode(String code, String ... args);
	/**
	 * 模糊查询所有事物作用信息
	 * @param fuzzy 事务编码/名称
	 * @return PageObject 事务作用信息
	 */
	public PageObject findByFuzzy(String fuzzy,String enterpriseCode,final int... rowStartIdxAndCount);
	/**
	 * 查询事物编码是否是唯一的
	 * 
	 * @param transCode
	 * @param enterpriseCode          
	 * @return boolean
	 */
	public boolean checkTransCode(String transCode,String enterpriseCode); 
	/**
	 * 查询事物名称是否是唯一的
	 * 
	 * @param transName
	 * @param enterpriseCode          
	 * @return boolean
	 */
	public boolean checkTransName(String transName,String enterpriseCode);
	 /**
     * 删除一条记录
     *
     * @param transId 流水号
	 * @throws CodeRepeatException 
     * @throws CodeRepeatException
     * @throws RuntimeException
     *             when the operation fails
     */
    public void deleteOfLogic(Long transId) throws CodeRepeatException ;
    /**
     * 根据事务编号查找记录
     * @param transCode
     */
    public InvCTransaction findByTransCode(String enterpriseCode, String transCode);
    
    /**
	 * 更新
     * 查询事物作用名称是否是唯一的
     * @param transName
     * @
     * @return list 
     */
	public InvCTransaction checkTransNameForUpdate(String transName, String enterpriseCode);
	/**
	 * 查询所有事物作用信息
	 * @return PageObject 事务作用信息
	 */
	public PageObject findTransCodeByAll(String enterpriseCode);
		
}