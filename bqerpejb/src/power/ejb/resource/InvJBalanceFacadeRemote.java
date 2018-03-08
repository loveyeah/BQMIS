package power.ejb.resource;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for InvJBalanceFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvJBalanceFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvJBalance entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            InvJBalance entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJBalance entity);

	/**
	 * Delete a persistent InvJBalance entity.
	 * 
	 * @param entity
	 *            InvJBalance entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJBalance entity);

	/**
	 * Persist a previously saved InvJBalance entity and return it or a copy of
	 * it to the sender. A copy of the InvJBalance entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            InvJBalance entity to update
	 * @return InvJBalance the persisted InvJBalance entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJBalance update(InvJBalance entity);

	public InvJBalance findById(Long id);

	/**
	 * Find all InvJBalance entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJBalance property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJBalance> found by query
	 */
	public List<InvJBalance> findByProperty(String propertyName, Object value);

	public List<InvJBalance> findByBalanceType(Object balanceType);

	public List<InvJBalance> findByWhsNo(Object whsNo);

	public List<InvJBalance> findByLocationNo(Object locationNo);

	public List<InvJBalance> findByBalanceYearMonth(Object balanceYearMonth);

	public List<InvJBalance> findByBalanceYear(Object balanceYear);

	public List<InvJBalance> findByTransHisMinid(Object transHisMinid);

	public List<InvJBalance> findByTransHisMaxid(Object transHisMaxid);

	public List<InvJBalance> findByBalanceStatus(Object balanceStatus);

	public List<InvJBalance> findByLastModifiedBy(Object lastModifiedBy);

	public List<InvJBalance> findByEnterpriseCode(Object enterpriseCode);

	public List<InvJBalance> findByIsUse(Object isUse);

	/**
	 * Find all InvJBalance entities.
	 * 
	 * @return List<InvJBalance> all InvJBalance entities
	 */
	public List<InvJBalance> findAll();
	/**
	 * 查找结算主表的最大流水号，以插入新的结算记录
	 * @return Long 最大流水号
	 * @throws Exception
	 */
	public Long getMaxId();
	/**
	 * 条件查找结算主表中最近的一条记录，获得其流水号
     * @param enterprisecode 企业编码
	 * @return 流水号
	 */
	public Long getLatestId(String enterprisecode);
	/**
	 * 事务方式保存下列表中数据
	 * @param listUpdateLot 批号记录表
	 * @param listUpdateLocation 库位记录表
	 * @param listUpdateWarehouse 库存记录表
	 * @param listSaveTrans 事务历史表
	 * @param balance 结算主表
	 * @throws Exception 
	 */
	public void saveAll(List<InvJLot> listUpdateLot, 
			List<InvJLocation> listUpdateLocation, 
			List<InvJWarehouse> listUpdateWarehouse,
			List<InvJTransactionHis> listSaveTrans,
			InvJBalance balance
			) throws Exception;
	
	/**
	 * add by fyyang 091124
	 */
	
	public String  checkInvLot(String enterpriseCode,Long lastId);
	public String checkInvWarehouse(String enterpriseCode,Long lastId);
	public String checkInvLocation(String enterpriseCode,Long lastId);
	public void updateForBalance(String enterpriseCode,String workCode);
	
	/**
	 * add by fyyang 091204
	 */
	public void deleteLastRecord();
}