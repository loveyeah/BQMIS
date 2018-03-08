package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquCStandardStepdocumentsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardStepdocumentsFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardStepdocuments
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardStepdocuments entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquCStandardStepdocuments entity);

	/**
	 * Delete a persistent EquCStandardStepdocuments entity.
	 * 
	 * @param entity
	 *            EquCStandardStepdocuments entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id);

	public boolean delete(String ids);
	
	public boolean deleteFiles(String ids);

	/**
	 * Persist a previously saved EquCStandardStepdocuments entity and return it
	 * or a copy of it to the sender. A copy of the EquCStandardStepdocuments
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardStepdocuments entity to update
	 * @return EquCStandardStepdocuments the persisted EquCStandardStepdocuments
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardStepdocuments update(EquCStandardStepdocuments entity);

	public EquCStandardStepdocuments findById(Long id);

	/**
	 * Find all EquCStandardStepdocuments entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardStepdocuments property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardStepdocuments> found by query
	 */
	public List<EquCStandardStepdocuments> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all EquCStandardStepdocuments entities.
	 * 
	 * @return List<EquCStandardStepdocuments> all EquCStandardStepdocuments
	 *         entities
	 */
	public PageObject findAll(String enterpriseCode, String woCode,
			 int... rowStartIdxAndCount);
}