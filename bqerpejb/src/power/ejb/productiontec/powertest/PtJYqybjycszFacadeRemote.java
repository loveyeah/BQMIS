package power.ejb.productiontec.powertest;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for PtJYqybjycszFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtJYqybjycszFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtJYqybjycsz entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJYqybjycsz entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJYqybjycsz entity);

	/**
	 * Delete a persistent PtJYqybjycsz entity.
	 * 
	 * @param entity
	 *            PtJYqybjycsz entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJYqybjycsz entity);

	/**
	 * Persist a previously saved PtJYqybjycsz entity and return it or a copy of
	 * it to the sender. A copy of the PtJYqybjycsz entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtJYqybjycsz entity to update
	 * @return PtJYqybjycsz the persisted PtJYqybjycsz entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJYqybjycsz update(PtJYqybjycsz entity);

	public PtJYqybjycsz findById(Long id);

	/**
	 * Find all PtJYqybjycsz entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJYqybjycsz property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJYqybjycsz> found by query
	 */
	public List<PtJYqybjycsz> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all PtJYqybjycsz entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJYqybjycsz> all PtJYqybjycsz entities
	 */
	public List<PtJYqybjycsz> findAll(int... rowStartIdxAndCount);
	public void save(List<PtJYqybjycsz> addList, List<PtJYqybjycsz> updateList);
	public PageObject findCSZ(String regulatorId,String jyjlId);
	
}