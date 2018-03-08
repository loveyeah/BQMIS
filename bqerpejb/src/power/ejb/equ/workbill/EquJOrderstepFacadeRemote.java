package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJOrderstepFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJOrderstepFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquJOrderstep entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJOrderstep entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJOrderstep entity,int i);
	public void save(List<EquJOrderstep> addList,
			List<EquJOrderstep> updateList, String delIds) ;
	/**
	 * Delete a persistent EquJOrderstep entity.
	 * 
	 * @param entity
	 *            EquJOrderstep entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids);

	/**
	 * Persist a previously saved EquJOrderstep entity and return it or a copy
	 * of it to the sender. A copy of the EquJOrderstep entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquJOrderstep entity to update
	 * @return EquJOrderstep the persisted EquJOrderstep entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJOrderstep update(EquJOrderstep entity);

	public EquJOrderstep findById(Long id);

	/**
	 * Find all EquJOrderstep entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJOrderstep property to query
	 * @param value
	 *            the property value to match
	 * @return List<EquJOrderstep> found by query
	 */
	public List<EquJOrderstep> findByProperty(String propertyName, Object value);



	/**
	 * Find all EquJOrderstep entities.
	 * 
	 * @return List<EquJOrderstep> all EquJOrderstep entities
	 */

	public PageObject findAll(String woCode,String enterpriseCode);
	public PageObject getEquCOrderstepList(String woCode,String enterpriseCode);
}