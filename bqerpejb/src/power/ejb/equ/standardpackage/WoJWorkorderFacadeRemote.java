package power.ejb.equ.standardpackage;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for WoJWorkorderFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface WoJWorkorderFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved WoJWorkorder entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            WoJWorkorder entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(WoJWorkorder entity);

	/**
	 * Delete a persistent WoJWorkorder entity.
	 * 
	 * @param entity
	 *            WoJWorkorder entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(WoJWorkorder entity);

	/**
	 * Persist a previously saved WoJWorkorder entity and return it or a copy of
	 * it to the sender. A copy of the WoJWorkorder entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            WoJWorkorder entity to update
	 * @return WoJWorkorder the persisted WoJWorkorder entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public WoJWorkorder update(WoJWorkorder entity);

	public WoJWorkorder findById(Long id);

	/**
	 * Find all WoJWorkorder entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the WoJWorkorder property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<WoJWorkorder> found by query
	 */
	public List<WoJWorkorder> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all WoJWorkorder entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<WoJWorkorder> all WoJWorkorder entities
	 */
	public List<WoJWorkorder> findAll(int... rowStartIdxAndCount);
	public PageObject findListByCode(String equCode,String enterpriseCode,int start,int limit);
	public WoJWorkorder findModelByFailure(String failureCode,String enterpriseCode);
}