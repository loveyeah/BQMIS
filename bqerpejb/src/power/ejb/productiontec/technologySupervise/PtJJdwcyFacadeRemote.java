package power.ejb.productiontec.technologySupervise;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJJdwcyFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Remote
public interface PtJJdwcyFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJJdwcy entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJJdwcy entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJJdwcy entity);

	/**
	 * Delete a persistent PtJJdwcy entity.
	 * 
	 * @param entity
	 *            PtJJdwcy entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void deleteMulti(String ids);

	/**
	 * Persist a previously saved PtJJdwcy entity and return it or a copy of it
	 * to the sender. A copy of the PtJJdwcy entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJJdwcy entity to update
	 * @return PtJJdwcy the persisted PtJJdwcy entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJJdwcy update(PtJJdwcy entity);

	public PtJJdwcy findById(Long id);

	/**
	 * Find all PtJJdwcy entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJJdwcy property to query
	 * @param value
	 *            the property value to match
	 * @return List<PtJJdwcy> found by query
	 */
	public List<PtJJdwcy> findByProperty(String propertyName, Object value);

	public List<PtJJdwcy> findByWorkerCode(Object workerCode);

	public List<PtJJdwcy> findByJdzyId(Object jdzyId);

	public List<PtJJdwcy> findByNetDuty(Object netDuty);

	public List<PtJJdwcy> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all PtJJdwcy entities.
	 * 
	 * @return List<PtJJdwcy> all PtJJdwcy entities
	 */
	public PageObject findAll(String jdzyId,String enterpriseCode,final int... rowStartIdxAndCount);
}