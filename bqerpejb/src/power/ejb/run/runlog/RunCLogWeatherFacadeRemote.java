package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunCLogWeatherFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCLogWeatherFacadeRemote {
	
	/**
	 * Perform an initial save of a previously unsaved RunCLogWeather entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCLogWeather entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public RunCLogWeather save(RunCLogWeather entity) throws CodeRepeatException;

	/**
	 * Delete a persistent RunCLogWeather entity.
	 * 
	 * @param entity
	 *            RunCLogWeather entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
//	public void delete(RunCLogWeather entity);

	/**
	 * Persist a previously saved RunCLogWeather entity and return it or a copy
	 * of it to the sender. A copy of the RunCLogWeather entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCLogWeather entity to update
	 * @return RunCLogWeather the persisted RunCLogWeather entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCLogWeather update(RunCLogWeather entity) throws CodeRepeatException;

	public RunCLogWeather findById(Long id);
	public void delete(Long weatherId) throws CodeRepeatException;
	/**
	 * Find all RunCLogWeather entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCLogWeather property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCLogWeather> found by query
	 */

	/**
	 * Find all RunCLogWeather entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCLogWeather> all RunCLogWeather entities
	 */
	public List<RunCLogWeather> findAll(int... rowStartIdxAndCount);
	
	public List<RunCLogWeather> findAllList() throws Exception;
	/**
	 * 根据企业编码查询天气列表
	 * @param enterpriseCode
	 * @return
	 */
	public List<RunCLogWeather> findWeatherList(String enterpriseCode);
	public List<RunCLogWeather> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount);
	public List<RunCLogWeather> findByIsUse(Object isUse,int... rowStartIdxAndCount);
    public PageObject findWeather(String fuzzy,String enterpriseCode,final int... rowStartIdxAndCount);
}