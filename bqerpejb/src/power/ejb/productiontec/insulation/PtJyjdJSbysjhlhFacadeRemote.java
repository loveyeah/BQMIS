package power.ejb.productiontec.insulation;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PtJyjdJSbysjhlhFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJyjdJSbysjhlhFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJyjdJSbysjhlh entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJyjdJSbysjhlh entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJyjdJSbysjhlh entity);

	/**
	 * Delete a persistent PtJyjdJSbysjhlh entity.
	 * 
	 * @param entity
	 *            PtJyjdJSbysjhlh entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJyjdJSbysjhlh entity);

	/**
	 * Persist a previously saved PtJyjdJSbysjhlh entity and return it or a copy
	 * of it to the sender. A copy of the PtJyjdJSbysjhlh entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PtJyjdJSbysjhlh entity to update
	 * @return PtJyjdJSbysjhlh the persisted PtJyjdJSbysjhlh entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJyjdJSbysjhlh update(PtJyjdJSbysjhlh entity);

	public PtJyjdJSbysjhlh findById(Long id);

	/**
	 * Find all PtJyjdJSbysjhlh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJyjdJSbysjhlh property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJyjdJSbysjhlh> found by query
	 */
	public List<PtJyjdJSbysjhlh> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all PtJyjdJSbysjhlh entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJyjdJSbysjhlh> all PtJyjdJSbysjhlh entities
	 */
	public List<PtJyjdJSbysjhlh> findAll(int... rowStartIdxAndCount);
}