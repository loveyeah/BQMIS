package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for CbmCItemFininceItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCItemFininceItemFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved CbmCItemFininceItem
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            CbmCItemFininceItem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(CbmCItemFininceItem entity);

	/**
	 * Delete a persistent CbmCItemFininceItem entity.
	 * 
	 * @param entity
	 *            CbmCItemFininceItem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(CbmCItemFininceItem entity);

	/**
	 * Persist a previously saved CbmCItemFininceItem entity and return it or a
	 * copy of it to the sender. A copy of the CbmCItemFininceItem entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            CbmCItemFininceItem entity to update
	 * @return CbmCItemFininceItem the persisted CbmCItemFininceItem entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public CbmCItemFininceItem update(CbmCItemFininceItem entity);

	public CbmCItemFininceItem findById(Long id);

	/**
	 * 
	 * @param itemId
	 *            指标id
	 * @return
	 */
	public CbmCItemFininceItem findByItemId(Long itemId);

	/**
	 * Find all CbmCItemFininceItem entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CbmCItemFininceItem property to query
	 * @param value
	 *            the property value to match
	 * @return List<CbmCItemFininceItem> found by query
	 */
	public List<CbmCItemFininceItem> findByProperty(String propertyName,
			Object value);

	public List<CbmCItemFininceItem> findByItemId(Object itemId);

	public List<CbmCItemFininceItem> findByFinanceId(Object financeId);

	public List<CbmCItemFininceItem> findByDebitCredit(Object debitCredit);

	public List<CbmCItemFininceItem> findByIsUse(Object isUse);

	public List<CbmCItemFininceItem> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all CbmCItemFininceItem entities.
	 * 
	 * @return List<CbmCItemFininceItem> all CbmCItemFininceItem entities
	 */
	public List<CbmCItemFininceItem> findAll();
}