package power.ejb.manage.stat;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.manage.stat.form.BpCEnthalpyFormulaForm;

/**
 * Remote interface for BpCEnthalpyFormulaFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCEnthalpyFormulaFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpCEnthalpyFormula
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            BpCEnthalpyFormula entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCEnthalpyFormula entity);

	/**
	 * Delete a persistent BpCEnthalpyFormula entity.
	 * 
	 * @param entity
	 *            BpCEnthalpyFormula entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCEnthalpyFormula entity);

	/**
	 * Persist a previously saved BpCEnthalpyFormula entity and return it or a
	 * copy of it to the sender. A copy of the BpCEnthalpyFormula entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCEnthalpyFormula entity to update
	 * @return BpCEnthalpyFormula the persisted BpCEnthalpyFormula entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCEnthalpyFormula update(BpCEnthalpyFormula entity);

	public BpCEnthalpyFormula findById(String id);

	public BpCEnthalpyFormulaForm getBpCEnthalpyFormula(String id,
			String enterpriseCode);

	/**
	 * Find all BpCEnthalpyFormula entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCEnthalpyFormula property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCEnthalpyFormula> found by query
	 */
	public List<BpCEnthalpyFormula> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all BpCEnthalpyFormula entities.
	 * 
	 * @return List<BpCEnthalpyFormula> all BpCEnthalpyFormula entities
	 */
	public List<BpCEnthalpyFormula> findAll();
}