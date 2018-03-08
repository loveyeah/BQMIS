package power.ejb.manage.project;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.Remote;
import power.ejb.manage.project.PrjJStartContent;
import power.ear.comm.ejb.PageObject;

import power.ejb.hr.LogUtil;
import power.ejb.hr.reward.HrJMonthRewardDetail;

/**
 * Remote interface for PrjJStartContentFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PrjJStartContentFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PrjJStartContent entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PrjJStartContent entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	//add by ypan 20100817
	public PageObject findOperationList(String e,String s,int d,int... rowStartIdxAndCount);
	public void save(PrjJStartContent entity);

	/**
	 * Delete a persistent PrjJStartContent entity.
	 * 
	 * @param entity
	 *            PrjJStartContent entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String detailIds);
	

	/**
	 * Persist a previously saved PrjJStartContent entity and return it or a
	 * copy of it to the sender. A copy of the PrjJStartContent entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PrjJStartContent entity to update
	 * @return PrjJStartContent the persisted PrjJStartContent entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PrjJStartContent update(PrjJStartContent entity);
	//add by ypan 20100817
	public void saveOrUpdateDetailList(List<PrjJStartContent> addList, List<PrjJStartContent> updateList); 


	public PrjJStartContent findById(Long id);

	/**
	 * Find all PrjJStartContent entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PrjJStartContent property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PrjJStartContent> found by query
	 */
	public List<PrjJStartContent> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all PrjJStartContent entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PrjJStartContent> all PrjJStartContent entities
	 */
	public List<PrjJStartContent> findAll(int... rowStartIdxAndCount);
}