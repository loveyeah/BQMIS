package power.ejb.report;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for JxlReportsRightFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface JxlReportsRightFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved JxlReportsRight entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            JxlReportsRight entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(JxlReportsRight entity);
	
	public void save(List<JxlReportsRight> addList)throws CodeRepeatException ;

	/**
	 * Delete a persistent JxlReportsRight entity.
	 * 
	 * @param entity
	 *            JxlReportsRight entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(JxlReportsRight entity);
	public boolean delete(String ids);
	/**
	 * Persist a previously saved JxlReportsRight entity and return it or a copy
	 * of it to the sender. A copy of the JxlReportsRight entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            JxlReportsRight entity to update
	 * @return JxlReportsRight the persisted JxlReportsRight entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public JxlReportsRight update(JxlReportsRight entity);
	
	public void update(List<JxlReportsRight> updateList) throws CodeRepeatException;

	public JxlReportsRight findById(Long id);

	/**
	 * Find all JxlReportsRight entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the JxlReportsRight property to query
	 * @param value
	 *            the property value to match
	 * @return List<JxlReportsRight> found by query
	 */
	public List<JxlReportsRight> findByProperty(String propertyName,
			Object value);

	public List<JxlReportsRight> findByCode(Object code);

	public List<JxlReportsRight> findByWorkerCode(Object workerCode);

	/**
	 * Find all JxlReportsRight entities.
	 * 
	 * @return List<JxlReportsRight> all JxlReportsRight entities
	 */
	public List<JxlReportsRight> findAll();
	public PageObject getAllUsers(String code,final int... rowStartIdxAndCount);
}