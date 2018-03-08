package power.ejb.productiontec.powertest;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJYqybjyjlFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJYqybjyjlFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJYqybjyjl entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJYqybjyjl entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public PtJYqybjyjl save(PtJYqybjyjl entity);

	/**
	 * Delete a persistent PtJYqybjyjl entity.
	 * 
	 * @param entity
	 *            PtJYqybjyjl entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJYqybjyjl entity);

	/**
	 * Persist a previously saved PtJYqybjyjl entity and return it or a copy of
	 * it to the sender. A copy of the PtJYqybjyjl entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJYqybjyjl entity to update
	 * @return PtJYqybjyjl the persisted PtJYqybjyjl entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJYqybjyjl update(PtJYqybjyjl entity);

	public PtJYqybjyjl findById(Long id);

	/**
	 * Find all PtJYqybjyjl entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJYqybjyjl property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJYqybjyjl> found by query
	 */
	public List<PtJYqybjyjl> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all PtJYqybjyjl entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJYqybjyjl> all PtJYqybjyjl entities
	 */
	public List<PtJYqybjyjl> findAll(int... rowStartIdxAndCount);
	public void deleteJLAndJYZ(String ids);
	public PageObject findJYJLList(String fuzzy, String jdzyId,
			String enterpriseCode, int... rowStartIdxAndCount);
	public PtJYqybjyjl findLastJl(String regulatorId,String enterpriseCode);
}