package power.ejb.equ.workbill;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquJSafetyrulesFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJSafetyrulesFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquJSafetyrules entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJSafetyrules entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJSafetyrules entity);

	/**
	 * Delete a persistent EquJSafetyrules entity.
	 * 
	 * @param entity
	 *            EquJSafetyrules entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJSafetyrules entity);

	/**
	 * Persist a previously saved EquJSafetyrules entity and return it or a copy
	 * of it to the sender. A copy of the EquJSafetyrules entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquJSafetyrules entity to update
	 * @return EquJSafetyrules the persisted EquJSafetyrules entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJSafetyrules update(EquJSafetyrules entity);

	public EquJSafetyrules findById(Long id);

	/**
	 * Find all EquJSafetyrules entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJSafetyrules property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJSafetyrules> found by query
	 */
	public List<EquJSafetyrules> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all EquJSafetyrules entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJSafetyrules> all EquJSafetyrules entities
	 */
	public List<EquJSafetyrules> findAll(int... rowStartIdxAndCount);
}