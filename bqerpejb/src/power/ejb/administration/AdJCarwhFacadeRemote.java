package power.ejb.administration;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for AdJCarwhFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJCarwhFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJCarwh entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJCarwh entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJCarwh entity);

	/**
	 * Delete a persistent AdJCarwh entity.
	 * 
	 * @param entity
	 *            AdJCarwh entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJCarwh entity);

	/**
	 * Persist a previously saved AdJCarwh entity and return it or a copy of it
	 * to the sender. A copy of the AdJCarwh entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJCarwh entity to update
	 * @return AdJCarwh the persisted AdJCarwh entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJCarwh update(AdJCarwh entity);

	public AdJCarwh findById(Long id);

	/**
	 * Find all AdJCarwh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarwh property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarwh> found by query
	 */
	public List<AdJCarwh> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJCarwh entities.
	 * 
	 * @return List<AdJCarwh> all AdJCarwh entities
	 */
	public List<AdJCarwh> findAll();
}