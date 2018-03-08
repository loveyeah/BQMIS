package power.ejb.manage.exam;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpJCbmAwardDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJCbmAwardDetailFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpJCbmAwardDetail entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJCbmAwardDetail entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */

	/**
	 * Delete a persistent BpJCbmAwardDetail entity.
	 * 
	 * @param entity
	 *            BpJCbmAwardDetail entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJCbmAwardDetail entity);

	/**
	 * Persist a previously saved BpJCbmAwardDetail entity and return it or a
	 * copy of it to the sender. A copy of the BpJCbmAwardDetail entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJCbmAwardDetail entity to update
	 * @return BpJCbmAwardDetail the persisted BpJCbmAwardDetail entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */

	public BpJCbmAwardDetail findById(Long id);

	/**
	 * Find all BpJCbmAwardDetail entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJCbmAwardDetail property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJCbmAwardDetail> found by query
	 */
	public List<BpJCbmAwardDetail> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all BpJCbmAwardDetail entities.
	 * 
	 * @return List<BpJCbmAwardDetail> all BpJCbmAwardDetail entities
	 */
	public List<BpJCbmAwardDetail> findAll();

	public PageObject getDeptCash(String datetime, String enterpriseCode);

	public boolean saveDeptCash(List<BpJCbmAwardDetail> addlist,
			List<BpJCbmAwardDetail> updatelist, String enterpriseCode);
}