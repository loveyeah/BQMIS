package power.ejb.productiontec.powertest;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for PtCYqybjycsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface PtCYqybjycsFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved PtCYqybjycs entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtCYqybjycs entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtCYqybjycs entity);

	/**
	 * Delete a persistent PtCYqybjycs entity.
	 * 
	 * @param entity
	 *            PtCYqybjycs entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtCYqybjycs entity);

	/**
	 * Persist a previously saved PtCYqybjycs entity and return it or a copy of
	 * it to the sender. A copy of the PtCYqybjycs entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtCYqybjycs entity to update
	 * @return PtCYqybjycs the persisted PtCYqybjycs entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtCYqybjycs update(PtCYqybjycs entity);

	public PtCYqybjycs findById(Long id);

	/**
	 * Find all PtCYqybjycs entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtCYqybjycs property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtCYqybjycs> found by query
	 */
	public List<PtCYqybjycs> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all PtCYqybjycs entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtCYqybjycs> all PtCYqybjycs entities
	 */
	public List<PtCYqybjycs> findAll(int... rowStartIdxAndCount);

	public void save(List<PtCYqybjycs> addList, List<PtCYqybjycs> updateList,
			String deleteId);

	public List<PtCYqybjycs> findByLb(Long jdzyId, Long yqyblbId,
			String enterpriseCode);
}