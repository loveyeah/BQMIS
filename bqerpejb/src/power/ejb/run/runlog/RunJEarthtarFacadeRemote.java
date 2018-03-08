package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJEarthtarFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJEarthtarFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunJEarthtar entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunJEarthtar entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunJEarthtar entity);

	/**
	 * Delete a persistent RunJEarthtar entity.
	 * 
	 * @param entity
	 *            RunJEarthtar entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJEarthtar entity);

	/**
	 * Persist a previously saved RunJEarthtar entity and return it or a copy of
	 * it to the sender. A copy of the RunJEarthtar entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunJEarthtar entity to update
	 * @returns RunJEarthtar the persisted RunJEarthtar entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJEarthtar update(RunJEarthtar entity);

	public RunJEarthtar findById(Long id);

	/**
	 * Find all RunJEarthtar entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJEarthtar property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJEarthtar> found by query
	 */
	public List<RunJEarthtar> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all RunJEarthtar entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJEarthtar> all RunJEarthtar entities
	 */
	public List<RunJEarthtar> findAll(int... rowStartIdxAndCount);
	/**
	 * 根据专业查询未拆除记录
	 * @param specialcode
	 * @param enterprisecode
	 * @return
	 */
	public PageObject findInstallListBySpecial(String specialcode,String enterprisecode,final int...rowStartIdxAndCount);
	public PageObject queryInstallListBySpecial(String specialcode,String enterprisecode,final int...rowStartIdxAndCount);
//	public List findInstallListBySpecial(String specialcode,String enterprisecode);
//	public List queryInstallListBySpecial(String specialcode,String enterprisecode);
}