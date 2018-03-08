package power.ejb.run.timework;

import java.util.List;
import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCTimeworkdFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCTimeworkdFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCTimeworkd entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCTimeworkd entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(RunCTimeworkd entity);

	/**
	 * Delete a persistent RunCTimeworkd entity.
	 * 
	 * @param entity
	 *            RunCTimeworkd entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCTimeworkd entity);

	/**
	 * Persist a previously saved RunCTimeworkd entity and return it or a copy
	 * of it to the sender. A copy of the RunCTimeworkd entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCTimeworkd entity to update
	 * @return RunCTimeworkd the persisted RunCTimeworkd entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCTimeworkd update(RunCTimeworkd entity);

	public RunCTimeworkd findById(Long id);

	/**
	 * Find all RunCTimeworkd entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCTimeworkd property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimeworkd> found by query
	 */
	public List<RunCTimeworkd> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	public List<RunCTimeworkd> findByWorkItemCode(Object workItemCode,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkd> findByMonth(Object month,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkd> findByWeekNo(Object weekNo,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkd> findByTestDay(Object testDay,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkd> findByClassSequence(Object classSequence,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkd> findByCycleSequence(Object cycleSequence,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkd> findByStatus(Object status,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkd> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkd> findByIsUse(Object status,
			int... rowStartIdxAndCount);

	public PageObject findByIsUse(Object isUse, String workitemcode,
			int... rowStartIdxAndCount);

	public List<RunCTimeworkd> findToGenerate(String enterpriseCode,
			int... rowStartIdxAndCount);

	/**
	 * Find all RunCTimeworkd entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimeworkd> all RunCTimeworkd entities
	 */
	public List<RunCTimeworkd> findAll(int... rowStartIdxAndCount);
}