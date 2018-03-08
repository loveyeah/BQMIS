package power.ejb.productiontec.insulation;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PtJyjdJYqybysjhFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJyjdJYqybysjhFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJyjdJYqybysjh entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJyjdJYqybysjh entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJyjdJYqybysjh entity);

	/**
	 * Delete a persistent PtJyjdJYqybysjh entity.
	 * 
	 * @param entity
	 *            PtJyjdJYqybysjh entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJyjdJYqybysjh entity);

	/**
	 * Persist a previously saved PtJyjdJYqybysjh entity and return it or a copy
	 * of it to the sender. A copy of the PtJyjdJYqybysjh entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PtJyjdJYqybysjh entity to update
	 * @return PtJyjdJYqybysjh the persisted PtJyjdJYqybysjh entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJyjdJYqybysjh update(PtJyjdJYqybysjh entity);

	public PtJyjdJYqybysjh findById(Long id);

	/**
	 * Find all PtJyjdJYqybysjh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJyjdJYqybysjh property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJyjdJYqybysjh> found by query
	 */
	public List<PtJyjdJYqybysjh> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all PtJyjdJYqybysjh entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJyjdJYqybysjh> all PtJyjdJYqybysjh entities
	 */
	public List<PtJyjdJYqybysjh> findAll(int... rowStartIdxAndCount);
}