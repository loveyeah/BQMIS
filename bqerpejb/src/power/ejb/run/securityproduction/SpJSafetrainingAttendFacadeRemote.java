package power.ejb.run.securityproduction;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJSafetrainingAttendFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSafetrainingAttendFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved SpJSafetrainingAttend
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            SpJSafetrainingAttend entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(SpJSafetrainingAttend entity);

	/**
	 * Delete a persistent SpJSafetrainingAttend entity.
	 * 
	 * @param entity
	 *            SpJSafetrainingAttend entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SpJSafetrainingAttend entity);

	/**
	 * Persist a previously saved SpJSafetrainingAttend entity and return it or
	 * a copy of it to the sender. A copy of the SpJSafetrainingAttend entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            SpJSafetrainingAttend entity to update
	 * @return SpJSafetrainingAttend the persisted SpJSafetrainingAttend entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SpJSafetrainingAttend update(SpJSafetrainingAttend entity);
	
	public void save(List<SpJSafetrainingAttend> addList);

	
	public boolean delete(String ids);
	
	public void update(List<SpJSafetrainingAttend> updateList);
	
	public SpJSafetrainingAttend findById(Long id);

	/**
	 * Find all SpJSafetrainingAttend entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJSafetrainingAttend property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJSafetrainingAttend> found by query
	 */
	public List<SpJSafetrainingAttend> findByProperty(String propertyName,
			Object value);


	/**
	 * Find all SpJSafetrainingAttend entities.
	 * 
	 * @return List<SpJSafetrainingAttend> all SpJSafetrainingAttend entities
	 */
	public PageObject findAll(String trainingId, String enterpriseCode,
			final int... rowStartIdxAndCount);
	public void deleteById(Long trainingId);
}