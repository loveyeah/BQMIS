package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;
import power.ear.comm.CodeRepeatException;
import power.ejb.resource.SysCCurrency;

/**
 * Remote interface for SysCCurrencyFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SysCCurrencyFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved SysCCurrency entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SysCCurrency entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(SysCCurrency entity);

	/**
	 * Delete a persistent SysCCurrency entity.
	 * 
	 * @param entity
	 *            SysCCurrency entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SysCCurrency entity);

	/**
	 * Persist a previously saved SysCCurrency entity and return it or a copy of
	 * it to the sender. A copy of the SysCCurrency entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            SysCCurrency entity to update
	 * @return SysCCurrency the persisted SysCCurrency entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SysCCurrency update(SysCCurrency entity);

	public SysCCurrency findById(Long id);

	/**
	 * Find all SysCCurrency entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SysCCurrency property to query
	 * @param value
	 *            the property value to match
	 * @return List<SysCCurrency> found by query
	 */
	public List<SysCCurrency> findByProperty(String propertyName, Object value);

	/**
	 * Find all SysCCurrency entities.
	 * 
	 * @return List<SysCCurrency> all SysCCurrency entities
	 */
	public List<SysCCurrency> findAll();
	
	/**
	 * 逻辑删除一条币种信息.
	 * 
	 * @param entity
	 *            SysCCurrency entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void deleteAdd(Long id,String workerCode);

	/**
	 * 按名称查找币种信息.
	 * 
	 * @param entity
	 *            SysCCurrency entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public SysCCurrency findByName(String currencyName);
	
	
	/**
	 * 按名称查找币种信息.
	 * 
	 * @param entity
	 *            SysCCurrency entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public SysCCurrency findByNo(String currencyNo);
	/**
	 * 更新币种信息
	 * 
	 * @return void
	 * @throws CodeRepeatException 
	 * @throws CodeRepeatException 
	 */
	public int updateAdd(SysCCurrency entity) throws CodeRepeatException;
	/**
     * 增加一条币种记录
     *
     * @param entity 要增加的记录
     * @throws CodeRepeatException
     */
    public int insert(SysCCurrency entity) throws CodeRepeatException ;
    /**
     * 查询
     * 
     * @param 
     * @return PageObject  查询结果
     */
    public PageObject findAllAdd(String enterpriseCode ,final int... rowStartIdxAndCount) ;
}