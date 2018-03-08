package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquCStandardPackfilenameFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCStandardPackfilenameFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCStandardPackfilename
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquCStandardPackfilename entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquCStandardPackfilename entity);

	/**
	 * Delete a persistent EquCStandardPackfilename entity.
	 * 
	 * @param entity
	 *            EquCStandardPackfilename entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCStandardPackfilename entity);

	/**
	 * Persist a previously saved EquCStandardPackfilename entity and return it
	 * or a copy of it to the sender. A copy of the EquCStandardPackfilename
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCStandardPackfilename entity to update
	 * @return EquCStandardPackfilename the persisted EquCStandardPackfilename
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCStandardPackfilename update(EquCStandardPackfilename entity);

	public EquCStandardPackfilename findById(Long id);

	/**
	 * Find all EquCStandardPackfilename entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the EquCStandardPackfilename property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquCStandardPackfilename> found by query
	 */
	public List<EquCStandardPackfilename> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all EquCStandardPackfilename entities.
	 * 
	 * @return List<EquCStandardPackfilename> all EquCStandardPackfilename
	 *         entities
	 */
	public List<EquCStandardPackfilename> findAll();
}