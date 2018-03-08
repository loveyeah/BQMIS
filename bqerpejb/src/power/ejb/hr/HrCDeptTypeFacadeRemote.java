package power.ejb.hr;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for HrCDeptTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCDeptTypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCDeptType entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCDeptType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCDeptType entity);

	/**
	 * Delete a persistent HrCDeptType entity.
	 * 
	 * @param entity
	 *            HrCDeptType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCDeptType entity);

	/**
	 * Persist a previously saved HrCDeptType entity and return it or a copy of
	 * it to the sender. A copy of the HrCDeptType entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCDeptType entity to update
	 * @return HrCDeptType the persisted HrCDeptType entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCDeptType update(HrCDeptType entity);

	public HrCDeptType findById(Long id);

	/**
	 * Find all HrCDeptType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCDeptType property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCDeptType> found by query
	 */
	public List<HrCDeptType> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public List<HrCDeptType> findByDeptTypeName(Object deptTypeName,
			int... rowStartIdxAndCount);

	public List<HrCDeptType> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<HrCDeptType> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount);

	/**
	 * Find all HrCDeptType entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCDeptType> all HrCDeptType entities
	 */
	public List<HrCDeptType> findAll(int... rowStartIdxAndCount);
}