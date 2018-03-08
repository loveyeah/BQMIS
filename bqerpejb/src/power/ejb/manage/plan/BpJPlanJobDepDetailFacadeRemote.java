package power.ejb.manage.plan;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpJPlanJobDepDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJPlanJobDepDetailFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpJPlanJobDepDetail
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            BpJPlanJobDepDetail entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpJPlanJobDepDetail entity);

	/**
	 * Delete a persistent BpJPlanJobDepDetail entity.
	 * 
	 * @param entity
	 *            BpJPlanJobDepDetail entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanJobDepDetail entity);

	/**
	 * Persist a previously saved BpJPlanJobDepDetail entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanJobDepDetail entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanJobDepDetail entity to update
	 * @return BpJPlanJobDepDetail the persisted BpJPlanJobDepDetail entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanJobDepDetail update(BpJPlanJobDepDetail entity);

	public BpJPlanJobDepDetail findById(Long id);

	/**
	 * Find all BpJPlanJobDepDetail entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanJobDepDetail property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanJobDepDetail> found by query
	 */
	public List<BpJPlanJobDepDetail> findByProperty(String propertyName,
			Object value);

//	public List<BpJPlanJobDepDetail> findByDepMainId(Object depMainId);
	public PageObject findByDepMainId(String depMainId,String enterpriseCode,int... rowStartIdxAndCount) ;
	
	//add by sychen 20100406
	public PageObject findByDepMainIdApprove(String depMainId) throws Exception;

	public List<BpJPlanJobDepDetail> findByJobContent(Object jobContent);

	public List<BpJPlanJobDepDetail> findByIfComplete(Object ifComplete);

	public List<BpJPlanJobDepDetail> findByCompleteDesc(Object completeDesc);

	public List<BpJPlanJobDepDetail> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all BpJPlanJobDepDetail entities.
	 * 
	 * @return List<BpJPlanJobDepDetail> all BpJPlanJobDepDetail entities
	 */
	public List<BpJPlanJobDepDetail> findAll();

	public void save(List<BpJPlanJobDepDetail> addList);

	public boolean delete(String ids);

	public void update(List<BpJPlanJobDepDetail> updateList);

//	public PageObject queryBpJPlanJobDepDetail(String planTime,
//			String editDepcode, String enterpriseCode, int start, int limit);
	//update by sychen 20100414
	public PageObject queryBpJPlanJobDepDetail(String planTime,
			String editDepcode,String editBy,String flag, String enterpriseCode, int start, int limit);

	public PageObject getBpJPlanJobDepDetailStat(String planTime,
			String enterpriseCode);
}