package power.ejb.run.securityproduction;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for SpJSafetyDaysrecordFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSafetyDaysrecordFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved SpJSafetyDaysrecord
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            SpJSafetyDaysrecord entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public SpJSafetyDaysrecord save(SpJSafetyDaysrecord entity);

	/**
	 * Delete a persistent SpJSafetyDaysrecord entity.
	 * 
	 * @param entity
	 *            SpJSafetyDaysrecord entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SpJSafetyDaysrecord entity);

	/**
	 * Persist a previously saved SpJSafetyDaysrecord entity and return it or a
	 * copy of it to the sender. A copy of the SpJSafetyDaysrecord entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            SpJSafetyDaysrecord entity to update
	 * @return SpJSafetyDaysrecord the persisted SpJSafetyDaysrecord entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SpJSafetyDaysrecord update(SpJSafetyDaysrecord entity);

	public SpJSafetyDaysrecord findById(Long id);

	/**
	 * Find all SpJSafetyDaysrecord entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJSafetyDaysrecord property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJSafetyDaysrecord> found by query
	 */
	public List<SpJSafetyDaysrecord> findByProperty(String propertyName,
			Object value);

	public List<SpJSafetyDaysrecord> findByIfBreak(Object ifBreak);

	public List<SpJSafetyDaysrecord> findBySafetyDays(Object safetyDays);

	public List<SpJSafetyDaysrecord> findByMemo(Object memo);

	public List<SpJSafetyDaysrecord> findByRecordBy(Object recordBy);

	public List<SpJSafetyDaysrecord> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all SpJSafetyDaysrecord entities.
	 * 
	 * @return List<SpJSafetyDaysrecord> all SpJSafetyDaysrecord entities
	 */
	public PageObject findAll(String enterpriseCode,int... rowStartIdxAndCount);
}