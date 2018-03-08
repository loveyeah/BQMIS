/**
　* Copyright ustcsoft.com
　* All right reserved.
　*/
package power.ejb.resource;

import java.util.List;
import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SysCExchangeRateFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SysCExchangeRateFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved SysCExchangeRate entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SysCExchangeRate entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(SysCExchangeRate entity);

	/**
	 * Delete a persistent SysCExchangeRate entity.
	 * 
	 * @param entity
	 *            SysCExchangeRate entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SysCExchangeRate entity);

	/**
	 * Persist a previously saved SysCExchangeRate entity and return it or a
	 * copy of it to the sender. A copy of the SysCExchangeRate entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            SysCExchangeRate entity to update
	 * @return SysCExchangeRate the persisted SysCExchangeRate entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SysCExchangeRate update(SysCExchangeRate entity);

	public SysCExchangeRate findById(Long id);

	/**
	 * Find all SysCExchangeRate entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SysCExchangeRate property to query
	 * @param value
	 *            the property value to match
	 * @return List<SysCExchangeRate> found by query
	 */
	public List<SysCExchangeRate> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all SysCExchangeRate entities.
	 * 
	 * @return List<SysCExchangeRate> all SysCExchangeRate entities
	 */
	public List<SysCExchangeRate> findAll();
	
	/**
	 * 获得汇率信息列表
	 * 
	 */
	public PageObject getExchangeRateList(String enterpriseCode, int... rowStartIdxAndCount);
	/**
	 * 获得所有货币种类名称的列表
	 * 
	 */
	public PageObject getCurrencyNameList();
	/**
	 * 判断相同的基准货币和兑换货币,在相同时段内有无不同汇率。
	 */
	public PageObject isDateExist(SysCExchangeRate entity);
	 
}