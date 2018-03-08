package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for EquJMainmatFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJMainmatFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquJMainmat entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJMainmat entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(EquJMainmat entity);
   
	public boolean save(List<EquJMainmat> addList,
			List<EquJMainmat> updateList, String delIds);
	/**
	 * Delete a persistent EquJMainmat entity.
	 * 
	 * @param entity
	 *            EquJMainmat entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(String ids);

	/**
	 * Persist a previously saved EquJMainmat entity and return it or a copy of
	 * it to the sender. A copy of the EquJMainmat entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            EquJMainmat entity to update
	 * @return EquJMainmat the persisted EquJMainmat entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJMainmat update(EquJMainmat entity);

	public EquJMainmat findById(Long id);

	/**
	 * Find all EquJMainmat entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJMainmat property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJMainmat> found by query
	 */
	public List<EquJMainmat> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all EquJMainmat entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJMainmat> all EquJMainmat entities
	 */
	public PageObject findAll(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount);
	public PageObject getEquCMainmat(String enterpriseCode, String woCode,
			String operationStep, int... rowStartIdxAndCount);
	/**
	 * 根据工单编号查找
	 */
//	public List<EquJMainmat> findByWoCode(String woCode,String enterprisecode);
}