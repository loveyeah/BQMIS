package power.ejb.item;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for ItemJDataFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ItemJDataFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved ItemJData entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            ItemJData entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	
	public List GetElectricList(String enterpriseCode,String machineNo);
	public List GetPreElectricList(String enterpriseCode,String machineNo);
	public List GetElectricListOnUnit(String enterpriseCode,String machineNo);
	public List GetElectricUnit(String enterpriseCode,String machineNo);
	
	public void save(ItemJData entity);

	/**
	 * Delete a persistent ItemJData entity.
	 * 
	 * @param entity
	 *            ItemJData entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(ItemJData entity);

	/**
	 * Persist a previously saved ItemJData entity and return it or a copy of it
	 * to the sender. A copy of the ItemJData entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            ItemJData entity to update
	 * @return ItemJData the persisted ItemJData entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ItemJData update(ItemJData entity);

	public ItemJData findById(ItemJDataId id);

	/**
	 * Find all ItemJData entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ItemJData property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ItemJData> found by query
	 */
	public List<ItemJData> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all ItemJData entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ItemJData> all ItemJData entities
	 */
	public List<ItemJData> findAll(int... rowStartIdxAndCount);
}