package power.ejb.run.timework;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCTimeworktypeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCTimeworktypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCTimeworktype entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCTimeworktype entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(RunCTimeworktype entity);

	/**
	 * Delete a persistent RunCTimeworktype entity.
	 * 
	 * @param entity
	 *            RunCTimeworktype entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCTimeworktype entity);

	/**
	 * Persist a previously saved RunCTimeworktype entity and return it or a
	 * copy of it to the sender. A copy of the RunCTimeworktype entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCTimeworktype entity to update
	 * @return RunCTimeworktype the persisted RunCTimeworktype entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCTimeworktype update(RunCTimeworktype entity);

	public RunCTimeworktype findById(Long id);

	/**
	 * Find all RunCTimeworktype entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCTimeworktype property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimeworktype> found by query
	 */
	public List<RunCTimeworktype> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<RunCTimeworktype> findByCode(Object code,
			int... rowStartIdxAndCount);

	public List<RunCTimeworktype> findByName(Object name,
			int... rowStartIdxAndCount);

	public List<RunCTimeworktype> findByOrderby(Object orderby,
			int... rowStartIdxAndCount);

	public List<RunCTimeworktype> findByMemo(Object memo,
			int... rowStartIdxAndCount);

	public List<RunCTimeworktype> findByShowtype(Object showtype,
			int... rowStartIdxAndCount);

	public List<RunCTimeworktype> findByKeywordsinsql(Object keywordsinsql,
			int... rowStartIdxAndCount);

	public List<RunCTimeworktype> findByStatus(Object status,
			int... rowStartIdxAndCount);

	public List<RunCTimeworktype> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount);

	public List<RunCTimeworktype> findByIsUse(Object isUse,
			int... rowStartIdxAndCount);

	public List<RunCTimeworktype> findForManage(int... rowStartIdxAndCount);

	/**
	 * Find all RunCTimeworktype entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimeworktype> all RunCTimeworktype entities
	 */
	public List<RunCTimeworktype> findAll(int... rowStartIdxAndCount);
}