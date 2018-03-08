package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCStandardToolsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardToolsFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardTools entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCStandardTools entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardTools entity);
	public void save(List<EquCStandardTools> addList);

	/**
	 * Delete a persistent EquCStandardTools entity.
	 * 
	 * @param entity
	 *            EquCStandardTools entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */

	 
	public boolean delete(String ids);

	/**
	 * Persist a previously saved EquCStandardTools entity and return it or a
	 * copy of it to the sender. A copy of the EquCStandardTools entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardTools entity to update
	 * @return EquCStandardTools the persisted EquCStandardTools entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardTools update(EquCStandardTools entity);
	public void update(List<EquCStandardTools> updateList);

	public EquCStandardTools findById(Long id);

	/**
	 * Find all EquCStandardTools entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardTools property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardTools> found by query
	 */
	public List<EquCStandardTools> findByProperty(String propertyName,
			Object value);
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount);

	/**
	 * Find all EquCStandardTools entities.
	 * 
	 * @return List<EquCStandardTools> all EquCStandardTools entities
	 */

}