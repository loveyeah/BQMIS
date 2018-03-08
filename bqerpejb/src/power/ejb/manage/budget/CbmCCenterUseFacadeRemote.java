package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for CbmCCenterUseFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCCenterUseFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved CbmCCenterUse entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            CbmCCenterUse entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(CbmCCenterUse entity);

	/**
	 * Delete a persistent CbmCCenterUse entity.
	 * 
	 * @param entity
	 *            CbmCCenterUse entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(CbmCCenterUse entity);

	/**
	 * Persist a previously saved CbmCCenterUse entity and return it or a copy
	 * of it to the sender. A copy of the CbmCCenterUse entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            CbmCCenterUse entity to update
	 * @return CbmCCenterUse the persisted CbmCCenterUse entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public CbmCCenterUse update(CbmCCenterUse entity);

	public CbmCCenterUse findById(Long id);

	/**
	 * Find all CbmCCenterUse entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CbmCCenterUse property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CbmCCenterUse> found by query
	 */
	public List<CbmCCenterUse> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all CbmCCenterUse entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<CbmCCenterUse> all CbmCCenterUse entities
	 */
	public List<CbmCCenterUse> findAll(int... rowStartIdxAndCount);
	
	//add by ypan 20100906
	public boolean checkDepName(long itemIds,String depName,String enterpriseCode,long useId);
	
	public void delete(String ids);
	
	public void saveCenter(List<CbmCCenterUse> addList,
			List<CbmCCenterUse> updateList, String ids);
	public PageObject findCenterList(String useId,
			String enterpriseCode, int... rowStartIdxAndCount);
}