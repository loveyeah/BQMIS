package power.ejb.administration;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for AdJUserSubFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJUserSubFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJUserSub entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJUserSub entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJUserSub entity);

	/**
	 * Delete a persistent AdJUserSub entity.
	 * 
	 * @param entity
	 *            AdJUserSub entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJUserSub entity);

	/**
	 * Persist a previously saved AdJUserSub entity and return it or a copy of
	 * it to the sender. A copy of the AdJUserSub entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJUserSub entity to update
	 * @return AdJUserSub the persisted AdJUserSub entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJUserSub update(AdJUserSub entity);

	public AdJUserSub findById(Long id);

	/**
	 * Find all AdJUserSub entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJUserSub property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJUserSub> found by query
	 */
	public List<AdJUserSub> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJUserSub entities.
	 * 
	 * @return List<AdJUserSub> all AdJUserSub entities
	 */
	public List<AdJUserSub> findAll();
}