package power.ejb.productiontec.metalSupervise;


import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJsjdJHgjbxxFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJsjdJHgjbxxFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJsjdJHgjbxx entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJsjdJHgjbxx entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJsjdJHgjbxx entity);

	/**
	 * Delete a persistent PtJsjdJHgjbxx entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjbxx entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJsjdJHgjbxx entity);
	public void delete(String  ids);

	/**
	 * Persist a previously saved PtJsjdJHgjbxx entity and return it or a copy
	 * of it to the sender. A copy of the PtJsjdJHgjbxx entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PtJsjdJHgjbxx entity to update
	 * @return PtJsjdJHgjbxx the persisted PtJsjdJHgjbxx entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJsjdJHgjbxx update(PtJsjdJHgjbxx entity);

	public PtJsjdJHgjbxx findById(Long id);

	/**
	 * Find all PtJsjdJHgjbxx entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJsjdJHgjbxx property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJsjdJHgjbxx> found by query
	 */
	public List<PtJsjdJHgjbxx> findByProperty(String propertyName, Object value);

	public List<PtJsjdJHgjbxx> findByWorkerCode(Object workerCode);

	public List<PtJsjdJHgjbxx> findByWeldCode(Object weldCode);

	public List<PtJsjdJHgjbxx> findByWeldAge(Object weldAge);

	public List<PtJsjdJHgjbxx> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all PtJsjdJHgjbxx entities.
	 * 
	 * @return List<PtJsjdJHgjbxx> all PtJsjdJHgjbxx entities
	 */
	public List<PtJsjdJHgjbxx> findAll();

	public PageObject getPtJsjdJHgjbxxList(String fuzzy, String enterpriseCode,
			int... rowStartIdxAndCount);
}