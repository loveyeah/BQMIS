package power.ejb.manage.plan;

import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpJPlanGuideDetailFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJPlanGuideDetailFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpJPlanGuideDetail
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            BpJPlanGuideDetail entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpJPlanGuideDetail entity);

	/**
	 * Delete a persistent BpJPlanGuideDetail entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideDetail entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanGuideDetail entity);

	/**
	 * Persist a previously saved BpJPlanGuideDetail entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanGuideDetail entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideDetail entity to update
	 * @return BpJPlanGuideDetail the persisted BpJPlanGuideDetail entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanGuideDetail update(BpJPlanGuideDetail entity);

	public BpJPlanGuideDetail findById(Long id);

	/**
	 * Find all BpJPlanGuideDetail entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanGuideDetail property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanGuideDetail> found by query
	 */
	public List<BpJPlanGuideDetail> findByProperty(String propertyName,
			Object value);

	public List<BpJPlanGuideDetail> findByGuideContent(Object guideContent);

	public List<BpJPlanGuideDetail> findByReferDepcode(Object referDepcode);

	public List<BpJPlanGuideDetail> findByMainDepcode(Object mainDepcode);

	public List<BpJPlanGuideDetail> findByTaskDepcode(Object taskDepcode);

	public List<BpJPlanGuideDetail> findByIfComplete(Object ifComplete);

	public List<BpJPlanGuideDetail> findByCompleteDesc(Object completeDesc);

	public List<BpJPlanGuideDetail> findByIfCheck(Object ifCheck);

	public List<BpJPlanGuideDetail> findByCheckStatus(Object checkStatus);

	public List<BpJPlanGuideDetail> findByTargetDepcode(Object targetDepcode);

	public List<BpJPlanGuideDetail> findByCheckDesc(Object checkDesc);

	public List<BpJPlanGuideDetail> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all BpJPlanGuideDetail entities.
	 * 
	 * @return List<BpJPlanGuideDetail> all BpJPlanGuideDetail entities
	 */
	public List<BpJPlanGuideDetail> findAll();

	public void save(List<BpJPlanGuideDetail> addList);

	public void update(List<BpJPlanGuideDetail> updateList);

	public boolean delete(String ids);

	public PageObject getPlanGuideDetail(String enterpriseCode,
			String planTime, int... rowStartIdxAndCount);
}