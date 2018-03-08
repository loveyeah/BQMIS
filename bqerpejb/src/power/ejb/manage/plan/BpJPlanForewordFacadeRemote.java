package power.ejb.manage.plan;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import javax.ejb.Remote;

import power.ejb.manage.plan.form.PlanForewordForm;

/**
 * Remote interface for BpJPlanForewordFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpJPlanForewordFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpJPlanForeword entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJPlanForeword entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public BpJPlanForeword save(BpJPlanForeword entity);

	/**
	 * Delete a persistent BpJPlanForeword entity.
	 * 
	 * @param entity
	 *            BpJPlanForeword entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(BpJPlanForeword entity);

	/**
	 * Persist a previously saved BpJPlanForeword entity and return it or a copy
	 * of it to the sender. A copy of the BpJPlanForeword entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanForeword entity to update
	 * @return BpJPlanForeword the persisted BpJPlanForeword entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanForeword update(BpJPlanForeword entity);

	public BpJPlanForeword findById(Date id);

	/**
	 * Find all BpJPlanForeword entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanForeword property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanForeword> found by query
	 */
	public List<BpJPlanForeword> findByProperty(String propertyName,
			Object value);

	public List<BpJPlanForeword> findByPlanForeword(Object planForeword);

	public List<BpJPlanForeword> findByPlanStatus(Object planStatus);

	public List<BpJPlanForeword> findByEditBy(Object editBy);

	public List<BpJPlanForeword> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all BpJPlanForeword entities.
	 * 
	 * @return List<BpJPlanForeword> all BpJPlanForeword entities
	 */
	public List<BpJPlanForeword> findAll();

	public PlanForewordForm findModelByPlantime(String planTime,
			String enterpriseCode) throws ParseException;
}