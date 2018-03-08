package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpCAnalyseAccountItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCAnalyseAccountItemFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpCAnalyseAccountItem
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            BpCAnalyseAccountItem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCAnalyseAccountItem entity);

	public void save(List<BpCAnalyseAccountItem> addList);

	/**
	 * Delete a persistent BpCAnalyseAccountItem entity.
	 * 
	 * @param entity
	 *            BpCAnalyseAccountItem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCAnalyseAccountItem entity);

	public boolean delete(String ids);

	public boolean deleteAccountItem(Long accountCode, String enterpriseCode);

	public Long isNew(String accountCode, String itemCode);

	/**
	 * Persist a previously saved BpCAnalyseAccountItem entity and return it or
	 * a copy of it to the sender. A copy of the BpCAnalyseAccountItem entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCAnalyseAccountItem entity to update
	 * @return BpCAnalyseAccountItem the persisted BpCAnalyseAccountItem entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCAnalyseAccountItem update(BpCAnalyseAccountItem entity);

	public void update(List<BpCAnalyseAccountItem> updateList);

	public BpCAnalyseAccountItem findById(BpCAnalyseAccountItemId id);

	/**
	 * Find all BpCAnalyseAccountItem entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCAnalyseAccountItem property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCAnalyseAccountItem> found by query
	 */
	public List<BpCAnalyseAccountItem> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all BpCAnalyseAccountItem entities.
	 * 
	 * @return List<BpCAnalyseAccountItem> all BpCAnalyseAccountItem entities
	 */
	public PageObject findAll(String enterpriseCode, String accountCode,
			int... rowStartIdxAndCount);
}