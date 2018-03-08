package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCEarthtarFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCEarthtarFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCEarthtar entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCEarthtar entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCEarthtar entity);

	/**
	 * Delete a persistent RunCEarthtar entity.
	 * 
	 * @param entity
	 *            RunCEarthtar entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCEarthtar entity);

	/**
	 * Persist a previously saved RunCEarthtar entity and return it or a copy of
	 * it to the sender. A copy of the RunCEarthtar entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunCEarthtar entity to update
	 * @returns RunCEarthtar the persisted RunCEarthtar entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCEarthtar update(RunCEarthtar entity);

	public RunCEarthtar findById(Long id);

	/**
	 * Find all RunCEarthtar entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCEarthtar property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCEarthtar> found by query
	 */
	public List<RunCEarthtar> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all RunCEarthtar entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCEarthtar> all RunCEarthtar entities
	 */
	public List<RunCEarthtar> findAll(int... rowStartIdxAndCount);
	/**
	 * 查询地线列表
	 * @param enterprisecode
	 * @return
	 */
	public List<RunCEarthtar> findList(String enterprisecode);
}