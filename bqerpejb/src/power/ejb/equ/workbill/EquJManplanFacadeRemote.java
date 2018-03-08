package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJManplanFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJManplanFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquJManplan entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJManplan entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquJManplan entity);
	
	public boolean save(List<EquJManplan> addList,
			List<EquJManplan> updateList, String delIds);

	/**
	 * Delete a persistent EquJManplan entity.
	 * 
	 * @param entity
	 *            EquJManplan entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(Long id) ;

	public boolean delete(String ids); 
	/**
	 * Persist a previously saved EquJManplan entity and return it or a copy of
	 * it to the sender. A copy of the EquJManplan entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJManplan entity to update
	 * @return EquJManplan the persisted EquJManplan entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJManplan update(EquJManplan entity);

	public EquJManplan findById(Long id);

	/**
	 * Find all EquJManplan entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJManplan property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJManplan> found by query
	 */
	public List<EquJManplan> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	public PageObject getEquCManplan(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount);	
	/**
	 * Find all EquJManplan entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJManplan> all EquJManplan entities
	 */
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount);
}