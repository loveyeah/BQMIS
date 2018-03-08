package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCStandardManplanFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardManplanFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardManplan
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardManplan entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardManplan entity);
	public void save(List<EquCStandardManplan> addList);

	/**
	 * Delete a persistent EquCStandardManplan entity.
	 * 
	 * @param entity
	 *            EquCStandardManplan entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id);

	public boolean delete(String ids);

	/**
	 * Persist a previously saved EquCStandardManplan entity and return it or a
	 * copy of it to the sender. A copy of the EquCStandardManplan entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardManplan entity to update
	 * @return EquCStandardManplan the persisted EquCStandardManplan entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardManplan update(EquCStandardManplan entity);
	public void update(List<EquCStandardManplan> updateList);

	public EquCStandardManplan findById(Long id);

	/**
	 * Find all EquCStandardManplan entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardManplan property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardManplan> found by query
	 */
	public List<EquCStandardManplan> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all EquCStandardManplan entities.
	 * 
	 * @return List<EquCStandardManplan> all EquCStandardManplan entities
	 */
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount);
}