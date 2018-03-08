package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJToolsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJToolsFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquJTools entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJTools entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquJTools entity);
	public boolean save(List<EquJTools> addList,
			List<EquJTools> updateList, String delIds);
	/**
	 * Delete a persistent EquJTools entity.
	 * 
	 * @param entity
	 *            EquJTools entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids) ;

	/**
	 * Persist a previously saved EquJTools entity and return it or a copy of it
	 * to the sender. A copy of the EquJTools entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJTools entity to update
	 * @return EquJTools the persisted EquJTools entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJTools update(EquJTools entity);

	public EquJTools findById(Long id);

	/**
	 * Find all EquJTools entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJTools property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJTools> found by query
	 */
	public List<EquJTools> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all EquJTools entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJTools> all EquJTools entities
	 */
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) ;
	public PageObject getEquCTools(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount) ;
}