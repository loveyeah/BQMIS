package power.ejb.administration;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for AdJCarwhMxFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJCarwhMxFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJCarwhMx entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJCarwhMx entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJCarwhMx entity);

	/**
	 * Delete a persistent AdJCarwhMx entity.
	 * 
	 * @param entity
	 *            AdJCarwhMx entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJCarwhMx entity);

	/**
	 * Persist a previously saved AdJCarwhMx entity and return it or a copy of
	 * it to the sender. A copy of the AdJCarwhMx entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJCarwhMx entity to update
	 * @return AdJCarwhMx the persisted AdJCarwhMx entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJCarwhMx update(AdJCarwhMx entity);

	public AdJCarwhMx findById(Long id);

	/**
	 * Find all AdJCarwhMx entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJCarwhMx property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJCarwhMx> found by query
	 */
	public List<AdJCarwhMx> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJCarwhMx entities.
	 * 
	 * @return List<AdJCarwhMx> all AdJCarwhMx entities
	 */
	public List<AdJCarwhMx> findAll();
}