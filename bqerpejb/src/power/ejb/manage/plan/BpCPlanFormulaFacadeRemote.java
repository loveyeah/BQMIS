package power.ejb.manage.plan;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.plan.form.PlanItemFormula;

/**
 * Remote interface for BpCPlanFormulaFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCPlanFormulaFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpCPlanFormula entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCPlanFormula entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCPlanFormula entity);

	/**
	 * Delete a persistent BpCPlanFormula entity.
	 * 
	 * @param entity
	 *            BpCPlanFormula entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCPlanFormula entity);

	/**
	 * Persist a previously saved BpCPlanFormula entity and return it or a copy
	 * of it to the sender. A copy of the BpCPlanFormula entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCPlanFormula entity to update
	 * @return BpCPlanFormula the persisted BpCPlanFormula entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCPlanFormula update(BpCPlanFormula entity);

	public BpCPlanFormula findById(BpCPlanFormulaId id);

	/**
	 * Find all BpCPlanFormula entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCPlanFormula property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCPlanFormula> found by query
	 */
	public List<BpCPlanFormula> findByProperty(String propertyName, Object value);

	/**
	 * Find all BpCPlanFormula entities.
	 * 
	 * @return List<BpCPlanFormula> all BpCPlanFormula entities
	 */
	public List<PlanItemFormula> findAll(String enterpriseCode, String itemCode);

	public PageObject findAllPlanItem(String argFuzzy,
			int... rowStartIdxAndCount);

	public boolean delete(String itemCode);

	public void save(List<BpCPlanFormula> addList);
}