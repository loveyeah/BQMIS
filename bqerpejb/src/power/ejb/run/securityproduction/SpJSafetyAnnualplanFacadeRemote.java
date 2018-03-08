package power.ejb.run.securityproduction;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ejb.run.securityproduction.form.SafetyAnnualPlanForm;

/**
 * Remote interface for SpJSafetyAnnualplanFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSafetyAnnualplanFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved SpJSafetyAnnualplan
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            SpJSafetyAnnualplan entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public SpJSafetyAnnualplan save(SpJSafetyAnnualplan entity);

	/**
	 * Delete a persistent SpJSafetyAnnualplan entity.
	 * 
	 * @param entity
	 *            SpJSafetyAnnualplan entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SpJSafetyAnnualplan entity);

	/**
	 * Persist a previously saved SpJSafetyAnnualplan entity and return it or a
	 * copy of it to the sender. A copy of the SpJSafetyAnnualplan entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            SpJSafetyAnnualplan entity to update
	 * @return SpJSafetyAnnualplan the persisted SpJSafetyAnnualplan entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SpJSafetyAnnualplan update(SpJSafetyAnnualplan entity);

	public SpJSafetyAnnualplan findById(Long id);

	/**
	 * Find all SpJSafetyAnnualplan entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJSafetyAnnualplan property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJSafetyAnnualplan> found by query
	 */
	public List<SpJSafetyAnnualplan> findByProperty(String propertyName,
			Object value);

	public List<SpJSafetyAnnualplan> findByPlanYear(Object planYear);

	public List<SpJSafetyAnnualplan> findByDepCode(Object depCode);

	public List<SpJSafetyAnnualplan> findByPlanContent(Object planContent);

	public List<SpJSafetyAnnualplan> findByMemo(Object memo);

	public List<SpJSafetyAnnualplan> findByFillBy(Object fillBy);

	public List<SpJSafetyAnnualplan> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all SpJSafetyAnnualplan entities.
	 * 
	 * @return List<SpJSafetyAnnualplan> all SpJSafetyAnnualplan entities
	 */
	public List<SafetyAnnualPlanForm> findAll(String year);
}