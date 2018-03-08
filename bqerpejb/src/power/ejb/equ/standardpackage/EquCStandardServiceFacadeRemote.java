package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCStandardServiceFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardServiceFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardService
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardService entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquCStandardService entity);
	public void save(List<EquCStandardService> addList);

	/**
	 * Delete a persistent EquCStandardService entity.
	 * 
	 * @param entity
	 *            EquCStandardService entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCStandardService entity);
	public boolean delete(String ids);

	/**
	 * Persist a previously saved EquCStandardService entity and return it or a
	 * copy of it to the sender. A copy of the EquCStandardService entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardService entity to update
	 * @return EquCStandardService the persisted EquCStandardService entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardService update(EquCStandardService entity);
	public void update(List<EquCStandardService> updateList);

	public EquCStandardService findById(Long id);

	/**
	 * Find all EquCStandardService entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardService property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardService> found by query
	 */
	public List<EquCStandardService> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all EquCStandardService entities.
	 * 
	 * @return List<EquCStandardService> all EquCStandardService entities
	 */
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount);
}