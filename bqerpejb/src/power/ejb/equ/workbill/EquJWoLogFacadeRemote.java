package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJWoLogFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJWoLogFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquJWoLog entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJWoLog entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJWoLog entity);
	
	public void save(List<EquJWoLog> addList,
			List<EquJWoLog> updateList, String delIds);

	/**
	 * Delete a persistent EquJWoLog entity.
	 * 
	 * @param entity
	 *            EquJWoLog entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJWoLog entity);

	/**
	 * Persist a previously saved EquJWoLog entity and return it or a copy of it
	 * to the sender. A copy of the EquJWoLog entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJWoLog entity to update
	 * @return EquJWoLog the persisted EquJWoLog entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJWoLog update(EquJWoLog entity);

	public EquJWoLog findById(Long id);

	/**
	 * Find all EquJWoLog entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJWoLog property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquJWoLog> found by query
	 */
	public List<EquJWoLog> findByProperty(String propertyName, Object value);



	/**
	 * Find all EquJWoLog entities.
	 * 
	 * @return List<EquJWoLog> all EquJWoLog entities
	 */
	public PageObject findAll(String woCode,int... rowStartIdxAndCount);
}