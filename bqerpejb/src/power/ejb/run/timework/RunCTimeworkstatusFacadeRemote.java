package power.ejb.run.timework;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCTimeworkstatusFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCTimeworkstatusFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCTimeworkstatus
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            RunCTimeworkstatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(RunCTimeworkstatus entity);

	/**
	 * Delete a persistent RunCTimeworkstatus entity.
	 * 
	 * @param entity
	 *            RunCTimeworkstatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCTimeworkstatus entity);

	/**
	 * Persist a previously saved RunCTimeworkstatus entity and return it or a
	 * copy of it to the sender. A copy of the RunCTimeworkstatus entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCTimeworkstatus entity to update
	 * @return RunCTimeworkstatus the persisted RunCTimeworkstatus entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCTimeworkstatus update(RunCTimeworkstatus entity);

	public RunCTimeworkstatus findById(Long id);

	/**
	 * Find all RunCTimeworkstatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCTimeworkstatus property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimeworkstatus> found by query
	 */
	public List<RunCTimeworkstatus> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<RunCTimeworkstatus> findByCode(Object code,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkstatus> findByName(Object name,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkstatus> findByOrderby(Object orderby,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkstatus> findByMemo(Object memo,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkstatus> findByStatus(Object status,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkstatus> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkstatus> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	/**
	 * Find all RunCTimeworkstatus entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimeworkstatus> all RunCTimeworkstatus entities
	 */
	public List<RunCTimeworkstatus> findAll(int... rowStartIdxAndCount);
}