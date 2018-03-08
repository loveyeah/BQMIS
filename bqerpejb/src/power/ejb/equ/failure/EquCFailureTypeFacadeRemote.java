package power.ejb.equ.failure;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquCFailureTypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquCFailureTypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquCFailureType entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCFailureType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquCFailureType entity);

	/**
	 * Delete a persistent EquCFailureType entity.
	 * 
	 * @param entity
	 *            EquCFailureType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCFailureType entity);

	/**
	 * Persist a previously saved EquCFailureType entity and return it or a copy
	 * of it to the sender. A copy of the EquCFailureType entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCFailureType entity to update
	 * @return EquCFailureType the persisted EquCFailureType entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCFailureType update(EquCFailureType entity);

	public EquCFailureType findById(Long id);

	/**
	 * Find all EquCFailureType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCFailureType property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquCFailureType> found by query
	 */
	public List<EquCFailureType> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<EquCFailureType> findByFailuretypeCode(Object failuretypeCode,
			int... rowStartIdxAndCount);

	public List<EquCFailureType> findByFailuretypeName(Object failuretypeName,
			int... rowStartIdxAndCount);

	public List<EquCFailureType> findByFailurePri(Object failurePri,
			int... rowStartIdxAndCount);

	public List<EquCFailureType> findByFailuretypeDesc(Object failuretypeDesc,
			int... rowStartIdxAndCount);

	public List<EquCFailureType> findByNeedCaclOvertime(
			Object needCaclOvertime, int... rowStartIdxAndCount);

	public List<EquCFailureType> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<EquCFailureType> findByEnterpriseCode(Object enterpriseCode,
			int... rowStartIdxAndCount);

	/**
	 * Find all EquCFailureType entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquCFailureType> all EquCFailureType entities
	 */
	public List<EquCFailureType> findAll(int... rowStartIdxAndCount);
}