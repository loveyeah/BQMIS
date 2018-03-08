package power.ejb.run.securityproduction;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;
import power.ejb.run.securityproduction.form.SafetyMonthForm;
/**
 * Remote interface for SpJSafetyMonthFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface SpJSafetyMonthFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved SpJSafetyMonth entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SpJSafetyMonth entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public SpJSafetyMonth save(SpJSafetyMonth entity);

	/**
	 * Delete a persistent SpJSafetyMonth entity.
	 * 
	 * @param entity
	 *            SpJSafetyMonth entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(SpJSafetyMonth entity);

	/**
	 * Persist a previously saved SpJSafetyMonth entity and return it or a copy
	 * of it to the sender. A copy of the SpJSafetyMonth entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            SpJSafetyMonth entity to update
	 * @return SpJSafetyMonth the persisted SpJSafetyMonth entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SpJSafetyMonth update(SpJSafetyMonth entity);

	public SpJSafetyMonth findById(Long id);

	/**
	 * Find all SpJSafetyMonth entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SpJSafetyMonth property to query
	 * @param value
	 *            the property value to match
	 * @return List<SpJSafetyMonth> found by query
	 */
	public List<SpJSafetyMonth> findByProperty(String propertyName, Object value);

	public List<SpJSafetyMonth> findBySubject(Object subject);

	public List<SpJSafetyMonth> findByContent(Object content);

	public List<SpJSafetyMonth> findBySummary(Object summary);

	public List<SpJSafetyMonth> findByFillBy(Object fillBy);

	public List<SpJSafetyMonth> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all SpJSafetyMonth entities.
	 * 
	 * @return List<SpJSafetyMonth> all SpJSafetyMonth entities
	 */
//	public List <SafetyMonthForm> findAll(String month);
//	public List<SpJSafetyMonth> findByMonth(String Month,String enterpriseCode);
	
	public SafetyMonthForm findModelForm( String monthString,String enterpriseCode);
	
	public String DateToString(Date date);
}