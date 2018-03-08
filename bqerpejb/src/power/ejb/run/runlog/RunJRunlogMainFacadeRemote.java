package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJRunlogMainFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJRunlogMainFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunJRunlogMain entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunJRunlogMain entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public Long save(RunJRunlogMain entity);

	/**
	 * Delete a persistent RunJRunlogMain entity.
	 * 
	 * @param entity
	 *            RunJRunlogMain entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJRunlogMain entity);

	/**
	 * Persist a previously saved RunJRunlogMain entity and return it or a copy
	 * of it to the sender. A copy of the RunJRunlogMain entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunJRunlogMain entity to update
	 * @returns RunJRunlogMain the persisted RunJRunlogMain entity instance, may
	 *          not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJRunlogMain update(RunJRunlogMain entity);

	public RunJRunlogMain findById(Long id);

	/**
	 * Find all RunJRunlogMain entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJRunlogMain property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJRunlogMain> found by query
	 */
	public List<RunJRunlogMain> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunJRunlogMain entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJRunlogMain> all RunJRunlogMain entities
	 */
	
	public List<RunJRunlogMain> findAll(int... rowStartIdxAndCount);
	/**
	 * 查询各专业最近值班信息
	 */
	public List<RunJRunLogMainModel> findLatesRunLogList(String enterpriseCode);
	/**
	 * 根据工号查询运行日志信息
	 * @param enterpriseCode
	 * @param worker
	 * @return
	 */
	public List findRunLogByWorker(String enterpriseCode,String worker);
	/**
	 * 查询某专业的下级专业值班情况
	 * @param enterpriseCode
	 * @param specialcode
	 * @param runlogNo
	 * @return
	 */
	public List<RunJRunlogMain> findLowSpecial(String enterpriseCode,String specialcode,String runlogNo);
	/**
	 * 取前一班次运行日志id
	 * @param latestId
	 * @param enterpriseCode
	 * @return
	 */
	public Long findPreviousLogid(Long latestId,String enterpriseCode);
	/**
	 * 判断运行日志是否已存在
	 * @param runlogno
	 * @param specialcode
	 * @param enterprisecode
	 * @return
	 */
	public boolean isRunlogExsit(String runlogno,String specialcode,String enterprisecode);
	/**
	 * 判断当前运行日志是不是某班组值班，有返回运行日志id
	 * @param enterprisecode
	 * @param shiftid
	 * @return
	 */
	public Long findLogidByShift(String enterprisecode,Long shiftid);
	/**
	 * 查询一段时间的运行日志列表
	 * @param specialcode
	 * @param enterprisecode
	 * @param shiftid
	 * @param fromdate
	 * @param enddate
	 * @return
	 */
	public PageObject getRunLogList(String specialcode,String enterprisecode,String fromdate,String enddate,final int...rowStartIdxAndCount);
	public RunJRunlogMain findLatestModelBySpecial(String specialcode,String enterprisecode);
	public RunJRunlogMain shiftHandOperation(Long oldLogid,String newlogno,Long newshiftid,Long newtimeid,String handworker);
}