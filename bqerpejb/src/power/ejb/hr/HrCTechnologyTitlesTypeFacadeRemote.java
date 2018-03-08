package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * Remote interface for HrCTechnologyTitlesTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCTechnologyTitlesTypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCTechnologyTitlesType
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrCTechnologyTitlesType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCTechnologyTitlesType entity) throws CodeRepeatException;;

	/**
	 * Delete a persistent HrCTechnologyTitlesType entity.
	 * 
	 * @param entity
	 *            HrCTechnologyTitlesType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCTechnologyTitlesType entity);

	/**
	 * Persist a previously saved HrCTechnologyTitlesType entity and return it
	 * or a copy of it to the sender. A copy of the HrCTechnologyTitlesType
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCTechnologyTitlesType entity to update
	 * @return HrCTechnologyTitlesType the persisted HrCTechnologyTitlesType
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCTechnologyTitlesType update(HrCTechnologyTitlesType entity) throws CodeRepeatException;;

	public HrCTechnologyTitlesType findById(Long id);

	/**
	 * Find all HrCTechnologyTitlesType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCTechnologyTitlesType property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCTechnologyTitlesType> found by query
	 */
	public List<HrCTechnologyTitlesType> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);
	public List<HrCTechnologyTitlesType> findByPropertys(String strWhere,
			Object o, int... rowStartIdxAndCount);

	public List<HrCTechnologyTitlesType> findByTechnologyTitlesTypeName(
			Object technologyTitlesTypeName, int... rowStartIdxAndCount);

	public List<HrCTechnologyTitlesType> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<HrCTechnologyTitlesType> findByRetrieveCode(
			Object retrieveCode, int... rowStartIdxAndCount);

	/**
	 * Find all HrCTechnologyTitlesType entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCTechnologyTitlesType> all HrCTechnologyTitlesType
	 *         entities
	 */
	public List<HrCTechnologyTitlesType> findAll(int... rowStartIdxAndCount);
}