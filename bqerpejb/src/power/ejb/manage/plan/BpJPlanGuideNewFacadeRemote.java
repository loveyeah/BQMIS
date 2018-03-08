package power.ejb.manage.plan;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.manage.plan.form.BpJPlanGuideNewForm;

/**
 * Remote interface for BpJPlanGuideNewFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJPlanGuideNewFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpJPlanGuideNew entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJPlanGuideNew entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public BpJPlanGuideNew save(BpJPlanGuideNew entity);

	/**
	 * Delete a persistent BpJPlanGuideNew entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideNew entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanGuideNew entity);

	/**
	 * Persist a previously saved BpJPlanGuideNew entity and return it or a copy
	 * of it to the sender. A copy of the BpJPlanGuideNew entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideNew entity to update
	 * @return BpJPlanGuideNew the persisted BpJPlanGuideNew entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanGuideNew update(BpJPlanGuideNew entity);

	public BpJPlanGuideNew findById(String id);

	/**
	 * Find all BpJPlanGuideNew entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanGuideNew property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanGuideNew> found by query
	 */
	public List<BpJPlanGuideNew> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all BpJPlanGuideNew entities.
	 * 
	 * @return List<BpJPlanGuideNew> all BpJPlanGuideNew entities
	 */
	public List<BpJPlanGuideNewForm> findAll(String month);
}