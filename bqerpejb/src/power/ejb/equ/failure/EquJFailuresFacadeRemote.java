package power.ejb.equ.failure;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJFailuresFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJFailuresFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquJFailures entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJFailures entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJFailures entity);

	/**
	 * Delete a persistent EquJFailures entity.
	 * 
	 * @param entity
	 *            EquJFailures entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJFailures entity);

	/**
	 * Persist a previously saved EquJFailures entity and return it or a copy of
	 * it to the sender. A copy of the EquJFailures entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJFailures entity to update
	 * @return EquJFailures the persisted EquJFailures entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJFailures update(EquJFailures entity);

	public EquJFailures findById(Long id);

	/**
	 * Find all EquJFailures entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJFailures property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJFailures> found by query
	 */
	public List<EquJFailures> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all EquJFailures entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJFailures> all EquJFailures entities
	 */
	
	public List<EquJFailures> findAll(int... rowStartIdxAndCount);
	/**
	 * 根据缺陷编码查找缺陷
	 * @param failureCode
	 * @param enterpriseCode
	 * @return
	 */
	public EquJFailures findFailureByCode(String failureCode,String enterpriseCode);
}