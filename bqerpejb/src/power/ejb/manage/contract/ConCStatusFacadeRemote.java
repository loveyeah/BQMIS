package power.ejb.manage.contract;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for ConCStatusFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface ConCStatusFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved ConCStatus entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            ConCStatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(ConCStatus entity);

	/**
	 * Delete a persistent ConCStatus entity.
	 * 
	 * @param entity
	 *            ConCStatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(ConCStatus entity);

	/**
	 * Persist a previously saved ConCStatus entity and return it or a copy of
	 * it to the sender. A copy of the ConCStatus entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            ConCStatus entity to update
	 * @return ConCStatus the persisted ConCStatus entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ConCStatus update(ConCStatus entity);

	public ConCStatus findById(Long id);

	/**
	 * Find all ConCStatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ConCStatus property to query
	 * @param value
	 *            the property value to match
	 * @return List<ConCStatus> found by query
	 */
	public List<ConCStatus> findByProperty(String propertyName, Object value);

	/**
	 * Find all ConCStatus entities.
	 * 
	 * @return List<ConCStatus> all ConCStatus entities
	 */
	public List<ConCStatus> findAll();
}