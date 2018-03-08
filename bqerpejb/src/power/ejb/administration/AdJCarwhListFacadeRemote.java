package power.ejb.administration;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for AdJCarwhListFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJCarwhListFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJCarwhList entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJCarwhList entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJCarwhList entity);

	/**
	 * Delete a persistent AdJCarwhList entity.
	 * 
	 * @param entity
	 *            AdJCarwhList entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJCarwhList entity);

	/**
	 * Persist a previously saved AdJCarwhList entity and return it or a copy of
	 * it to the sender. A copy of the AdJCarwhList entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJCarwhList entity to update
	 * @return AdJCarwhList the persisted AdJCarwhList entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJCarwhList update(AdJCarwhList entity);

	public AdJCarwhList findById(Long id);

	/**
	 * Find all AdJCarwhList entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarwhList property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarwhList> found by query
	 */
	public List<AdJCarwhList> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJCarwhList entities.
	 * 
	 * @return List<AdJCarwhList> all AdJCarwhList entities
	 */
	public List<AdJCarwhList> findAll();
}