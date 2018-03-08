package power.ejb.run.runlog.shift;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunJEquRealtimeStatusFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJEquRealtimeStatusFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunJEquRealtimeStatus
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            RunJEquRealtimeStatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunJEquRealtimeStatus entity);

	/**
	 * Delete a persistent RunJEquRealtimeStatus entity.
	 * 
	 * @param entity
	 *            RunJEquRealtimeStatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJEquRealtimeStatus entity);

	/**
	 * Persist a previously saved RunJEquRealtimeStatus entity and return it or
	 * a copy of it to the sender. A copy of the RunJEquRealtimeStatus entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            RunJEquRealtimeStatus entity to update
	 * @return RunJEquRealtimeStatus the persisted RunJEquRealtimeStatus entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJEquRealtimeStatus update(RunJEquRealtimeStatus entity);

	public RunJEquRealtimeStatus findById(Long id);

	/**
	 * Find all RunJEquRealtimeStatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJEquRealtimeStatus property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJEquRealtimeStatus> found by query
	 */
	public List<RunJEquRealtimeStatus> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunJEquRealtimeStatus entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJEquRealtimeStatus> all RunJEquRealtimeStatus entities
	 */
	public List<RunJEquRealtimeStatus> findAll(int... rowStartIdxAndCount);
}