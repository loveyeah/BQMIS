package power.ejb.administration;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for AdJMeetMxFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJMeetMxFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJMeetMx entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJMeetMx entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJMeetMx entity);

	/**
	 * Delete a persistent AdJMeetMx entity.
	 * 
	 * @param entity
	 *            AdJMeetMx entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJMeetMx entity);

	/**
	 * Persist a previously saved AdJMeetMx entity and return it or a copy of it
	 * to the sender. A copy of the AdJMeetMx entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJMeetMx entity to update
	 * @return AdJMeetMx the persisted AdJMeetMx entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJMeetMx update(AdJMeetMx entity);

	public AdJMeetMx findById(Long id);

	/**
	 * Find all AdJMeetMx entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJMeetMx property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJMeetMx> found by query
	 */
	public List<AdJMeetMx> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJMeetMx entities.
	 * 
	 * @return List<AdJMeetMx> all AdJMeetMx entities
	 */
	public List<AdJMeetMx> findAll();
}