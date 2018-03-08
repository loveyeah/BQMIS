package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquCStandardSafetyrulesFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardSafetyrulesFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardSafetyrules
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardSafetyrules entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquCStandardSafetyrules entity);

	/**
	 * Delete a persistent EquCStandardSafetyrules entity.
	 * 
	 * @param entity
	 *            EquCStandardSafetyrules entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCStandardSafetyrules entity);

	/**
	 * Persist a previously saved EquCStandardSafetyrules entity and return it
	 * or a copy of it to the sender. A copy of the EquCStandardSafetyrules
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardSafetyrules entity to update
	 * @return EquCStandardSafetyrules the persisted EquCStandardSafetyrules
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardSafetyrules update(EquCStandardSafetyrules entity);

	public EquCStandardSafetyrules findById(Long id);

	/**
	 * Find all EquCStandardSafetyrules entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardSafetyrules property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardSafetyrules> found by query
	 */
	public List<EquCStandardSafetyrules> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all EquCStandardSafetyrules entities.
	 * 
	 * @return List<EquCStandardSafetyrules> all EquCStandardSafetyrules
	 *         entities
	 */
	public List<EquCStandardSafetyrules> findAll();
}