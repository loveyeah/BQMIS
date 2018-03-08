package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * Remote interface for HrCAssistantManagerUnitsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCAssistantManagerUnitsFacadeRemote {
	 
	public void save(HrCAssistantManagerUnits entity) throws CodeRepeatException ;

	 
	public void delete(HrCAssistantManagerUnits entity);

	/**
	 * Persist a previously saved HrCAssistantManagerUnits entity and return it
	 * or a copy of it to the sender. A copy of the HrCAssistantManagerUnits
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCAssistantManagerUnits entity to update
	 * @return HrCAssistantManagerUnits the persisted HrCAssistantManagerUnits
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCAssistantManagerUnits update(HrCAssistantManagerUnits entity) throws CodeRepeatException ;

	public HrCAssistantManagerUnits findById(Long id);

	/**
	 * Find all HrCAssistantManagerUnits entities with a specific property
	 * value.
	 * 
	 * @param propertyName
	 *            the name of the HrCAssistantManagerUnits property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCAssistantManagerUnits> found by query
	 */
	public List<HrCAssistantManagerUnits> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<HrCAssistantManagerUnits> findByProperties(int start, int limit);

	public List<HrCAssistantManagerUnits> findByAssistantManagerUnitsName(
			Object assistantManagerUnitsName, int... rowStartIdxAndCount);

	public List<HrCAssistantManagerUnits> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<HrCAssistantManagerUnits> findByRetrieveCode(
			Object retrieveCode, int... rowStartIdxAndCount);

	/**
	 * Find all HrCAssistantManagerUnits entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCAssistantManagerUnits> all HrCAssistantManagerUnits
	 *         entities
	 */
	public List<HrCAssistantManagerUnits> findAll(int... rowStartIdxAndCount);
}