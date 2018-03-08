package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCRunWayFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCRunWayFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCRunWay entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCRunWay entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public RunCRunWay save(RunCRunWay entity) throws CodeRepeatException;

	/**
	 * Delete a persistent RunCRunWay entity.
	 * 
	 * @param entity
	 *            RunCRunWay entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCRunWay entity);

	public void deleteMulti(String runwayIds);
	/**
	 * Persist a previously saved RunCRunWay entity and return it or a copy of
	 * it to the sender. A copy of the RunCRunWay entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunCRunWay entity to update
	 * @return RunCRunWay the persisted RunCRunWay entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCRunWay update(RunCRunWay entity) throws CodeRepeatException;

	public RunCRunWay findById(Long id);

	/**
	 * Find all RunCRunWay entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCRunWay property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCRunWay> found by query
	 */
	public List<RunCRunWay> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all RunCRunWay entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCRunWay> all RunCRunWay entities
	 */
	public List<RunCRunWay> findAll(int... rowStartIdxAndCount);
	public List<RunCRunWay> findByIsUse(Object isUse,int... rowStartIdxAndCount);
	/**
	 * 根据企业编码查询运行方式列表
	 * @param enterpriseCode
	 * @return
	 */
	public List<RunCRunWay> findAllList(String enterpriseCode);
	
	/**
	 * 判断运行方式编码是否重复
	 * @param enterpriseCode
	 * @param runwayCode
	 * @return
	 */
	public boolean CheckRunWayCodeSame(String enterpriseCode,String runwayCode);
}