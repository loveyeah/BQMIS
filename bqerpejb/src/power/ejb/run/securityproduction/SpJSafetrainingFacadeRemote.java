package power.ejb.run.securityproduction;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJSafetrainingFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSafetrainingFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved SpJSafetraining entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SpJSafetraining entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(SpJSafetraining entity);

	/**
	 * Delete a persistent SpJSafetraining entity.
	 * 
	 * @param entity
	 *            SpJSafetraining entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(SpJSafetraining entity);

	/**
	 * Persist a previously saved SpJSafetraining entity and return it or a copy
	 * of it to the sender. A copy of the SpJSafetraining entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            SpJSafetraining entity to update
	 * @return SpJSafetraining the persisted SpJSafetraining entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public boolean update(SpJSafetraining entity);

	public SpJSafetraining findById(Long id);

	/**
	 * Find all SpJSafetraining entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJSafetraining property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJSafetraining> found by query
	 */
	public List<SpJSafetraining> findByProperty(String propertyName,
			Object value);



	/**
	 * Find all SpJSafetraining entities.
	 * 
	 * @return List<SpJSafetraining> all SpJSafetraining entities
	 */
	public PageObject findAll(String argFuzzy,String enterpriseCode,final int... rowStartIdxAndCount);
}