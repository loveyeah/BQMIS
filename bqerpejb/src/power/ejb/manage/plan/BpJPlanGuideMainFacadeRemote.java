package power.ejb.manage.plan;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ejb.manage.plan.form.BpJPlanGuideMainForm;

/**
 * Remote interface for BpJPlanGuideMainFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJPlanGuideMainFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpJPlanGuideMain entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJPlanGuideMain entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public BpJPlanGuideMain save(BpJPlanGuideMain entity);

	/**
	 * Delete a persistent BpJPlanGuideMain entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideMain entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanGuideMain entity);

	/**
	 * Persist a previously saved BpJPlanGuideMain entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanGuideMain entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideMain entity to update
	 * @return BpJPlanGuideMain the persisted BpJPlanGuideMain entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanGuideMain update(BpJPlanGuideMain entity);

	public BpJPlanGuideMain findById(Date id);

	/**
	 * Find all BpJPlanGuideMain entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanGuideMain property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanGuideMain> found by query
	 */
	public List<BpJPlanGuideMain> findByProperty(String propertyName,
			Object value);

	public List<BpJPlanGuideMain> findByEditBy(Object editBy);

	public List<BpJPlanGuideMain> findByPlanStatus(Object planStatus);

	public List<BpJPlanGuideMain> findByWorkFlowNo(Object workFlowNo);

	public List<BpJPlanGuideMain> findBySignStatus(Object signStatus);

	public List<BpJPlanGuideMain> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all BpJPlanGuideMain entities.
	 * 
	 * @return List<BpJPlanGuideMain> all BpJPlanGuideMain entities
	 */
	public List<BpJPlanGuideMain> findAll();

	public BpJPlanGuideMainForm getBpJPlanGuideMain(String planTime,
			String enterpriseCode);

	public BpJPlanGuideMain reportTo(String prjNo, String workflowType,
			String workerCode, String actionId, String approveText,
			String nextRoles, String enterpriseCode) throws ParseException;

	public BpJPlanGuideMain approveStep(String prjNo, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode) throws ParseException;
}