package power.ejb.resource;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for InvJTransactionHisFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface InvJTransactionHisFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved InvJTransactionHis
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            InvJTransactionHis entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(InvJTransactionHis entity);

	/**
	 * Delete a persistent InvJTransactionHis entity.
	 * 
	 * @param entity
	 *            InvJTransactionHis entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(InvJTransactionHis entity);

	/**
	 * Persist a previously saved InvJTransactionHis entity and return it or a
	 * copy of it to the sender. A copy of the InvJTransactionHis entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            InvJTransactionHis entity to update
	 * @return InvJTransactionHis the persisted InvJTransactionHis entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public InvJTransactionHis update(InvJTransactionHis entity);

	public InvJTransactionHis findById(Long id);

	/**
	 * Find all InvJTransactionHis entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the InvJTransactionHis property to query
	 * @param value
	 *            the property value to match
	 * @return List<InvJTransactionHis> found by query
	 */
	public List<InvJTransactionHis> findByProperty(String propertyName,
			Object value);

	public List<InvJTransactionHis> findByOrderNo(Object orderNo);

	public List<InvJTransactionHis> findBySequenceId(Object sequenceId);

	public List<InvJTransactionHis> findByMaterialId(Object materialId);

	public List<InvJTransactionHis> findByTransId(Object transId);

	public List<InvJTransactionHis> findByReasonId(Object reasonId);

	public List<InvJTransactionHis> findByLotNo(Object lotNo);

	public List<InvJTransactionHis> findByTransQty(Object transQty);

	public List<InvJTransactionHis> findByTransUmId(Object transUmId);

	public List<InvJTransactionHis> findByPrice(Object price);

	public List<InvJTransactionHis> findByActCost(Object actCost);

	public List<InvJTransactionHis> findByStdCost(Object stdCost);

	public List<InvJTransactionHis> findByOriCurrencyId(Object oriCurrencyId);

	public List<InvJTransactionHis> findByCurrencyId(Object currencyId);

	public List<InvJTransactionHis> findByExchangeRate(Object exchangeRate);

	public List<InvJTransactionHis> findByTaxRate(Object taxRate);

	public List<InvJTransactionHis> findByFromWhsNo(Object fromWhsNo);

	public List<InvJTransactionHis> findByFromLocationNo(Object fromLocationNo);

	public List<InvJTransactionHis> findByToWhsNo(Object toWhsNo);

	public List<InvJTransactionHis> findByToLocationNo(Object toLocationNo);

	public List<InvJTransactionHis> findByCustomerNo(Object customerNo);

	public List<InvJTransactionHis> findBySupplier(Object supplier);

	public List<InvJTransactionHis> findByManufacturerNo(Object manufacturerNo);

	public List<InvJTransactionHis> findByReceiveMan(Object receiveMan);

	public List<InvJTransactionHis> findByReceiveDept(Object receiveDept);

	public List<InvJTransactionHis> findByCostMan(Object costMan);

	public List<InvJTransactionHis> findByCostDept(Object costDept);

	public List<InvJTransactionHis> findByLastModifiedBy(Object lastModifiedBy);

	public List<InvJTransactionHis> findByMemo(Object memo);

	public List<InvJTransactionHis> findByEnterpriseCode(Object enterpriseCode);

	public List<InvJTransactionHis> findByIsUse(Object isUse);

	/**
	 * Find all InvJTransactionHis entities.
	 * 
	 * @return List<InvJTransactionHis> all InvJTransactionHis entities
	 */
	public List<InvJTransactionHis> findAll();
	/**
	 * 查找事务历史表的最大流水号，以插入新的事务记录
	 * @return Long 最大流水号
	 * @throws Exception
	 */
	public Long getMaxId();
	/**
	 * 条件查找事务历史表中最近的一条记录，获得其流水号（is_use='Y' and 企业编码='hfdc'）
	 * @param enterpriseCode 企业编码
	 * @return 流水号
	 */
	public Long getLatestId(String enterpriseCode);
	/**
	 * 查找事务历史表
	 * @param enterpriseCode
	 * @param sdate
	 * @param edate
	 * @param materialId
	 * @param transType
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject queryForMaterialMove(String enterpriseCode, String sdate,
			String edate, String materialId,String transType,
			final int... rowStartIdxAndCount);
	
	/**
	 *  根据物料编码，领料单号查找出库操作明细列表
	 * @param enterpriseCode
	 * @param materialCode
	 * @param issueNo
	 * @param rowStartIdxAndCount
	 * @return
	 * 
	 * add by drdu 20090514
	 */
	public PageObject queryForissuList(String enterpriseCode, String materialCode,String issueNo,
			final int... rowStartIdxAndCount);
	
	/**
	 * 根据物料编码，到货单号查找出库操作明细列表
	 * @param enterpriseCode
	 * @param materialCode
	 * @param arrivalNo
	 * @param purNo
	 * @param rowStartIdxAndCount
	 * @return
	 * 
	 *  add by drdu 
	 */
	public PageObject queryForArrivalList(String enterpriseCode, String materialCode,String arrivalNo,
			String purNo,final int... rowStartIdxAndCount);
}