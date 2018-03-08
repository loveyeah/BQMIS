package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;

/**
 * Remote interface for RunCEqustatusFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCEqustatusFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCEqustatus entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCEqustatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public RunCEqustatus save(RunCEqustatus entity) throws CodeRepeatException;

	/**
	 * Delete a persistent RunCEqustatus entity.
	 * 
	 * @param entity
	 *            RunCEqustatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCEqustatus entity);

	/**
	 * Persist a previously saved RunCEqustatus entity and return it or a copy
	 * of it to the sender. A copy of the RunCEqustatus entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCEqustatus entity to update
	 * @return RunCEqustatus the persisted RunCEqustatus entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCEqustatus update(RunCEqustatus entity) throws CodeRepeatException;

	public RunCEqustatus findById(Long id);

	/**
	 * Find all RunCEqustatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCEqustatus property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCEqustatus> found by query
	 */
	public List<RunCEqustatus> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunCEqustatus entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCEqustatus> all RunCEqustatus entities
	 */
	public List<RunCEqustatus> findAll(int... rowStartIdxAndCount);
	public List<RunCEqustatus> findByIsUse(Object isUse,int... rowStartIdxAndCount);
	public List<RunCEqustatus> findList(String enterpriseCode);
}