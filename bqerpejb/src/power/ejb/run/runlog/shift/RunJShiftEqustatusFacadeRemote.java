package power.ejb.run.runlog.shift;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJShiftEqustatusFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJShiftEqustatusFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunJShiftEqustatus
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            RunJShiftEqustatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunJShiftEqustatus entity);

	/**
	 * Delete a persistent RunJShiftEqustatus entity.
	 * 
	 * @param entity
	 *            RunJShiftEqustatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJShiftEqustatus entity);

	/**
	 * Persist a previously saved RunJShiftEqustatus entity and return it or a
	 * copy of it to the sender. A copy of the RunJShiftEqustatus entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            RunJShiftEqustatus entity to update
	 * @return RunJShiftEqustatus the persisted RunJShiftEqustatus entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJShiftEqustatus update(RunJShiftEqustatus entity);

	public RunJShiftEqustatus findById(Long id);

	/**
	 * Find all RunJShiftEqustatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJShiftEqustatus property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJShiftEqustatus> found by query
	 */
	public List<RunJShiftEqustatus> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunJShiftEqustatus entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJShiftEqustatus> all RunJShiftEqustatus entities
	 */
	public List<RunJShiftEqustatus> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 根据专业查询某种运行方式下设备状态信息
	 * @param fuzzy
	 * @param specialcode
	 * @param runKeyId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject getShiftEquStatusList(String fuzzy,String specialcode,Long runlogId,Long runKeyId,String enterpriseCode,final int... rowStartIdxAndCount);
	
	/**
	 * 根据运行日志id和设备功能码得到一个对象实体
	 * @param runlogid
	 * @param equcode
	 * @param enterprisecode
	 * @return
	 */
	public RunJShiftEqustatus getModelByRunLog(Long runlogid,String equcode,String enterprisecode);

	/**
	 * 根据运行日志ID，运行日志号查询交接班设备状态记录
	 * @param runLogId
	 * @param runlogNo
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findEquStatusByRunLog(Long runLogId,final int... rowStartIdxAndCount);
/**
 * 根据专业查询列表
 * @param specialCode
 * @param enterpriseCode
 * @return
 */
	public List<RunJShiftEqustatus> findBySpecial(String specialCode,String enterpriseCode);
	public RunJShiftEqustatus findModle(String specialCode,String enterpriseCoce,String equcode);
	/**
	 * 查询一段时间下运行方式关心的设备状态列表
	 * @param specialcode
	 * @param runkeyid
	 * @param enterprisecode
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	public PageObject queryEquStatusList(String specialcode,Long runkeyid,String enterprisecode,String fromdate,String todate,final int... rowStartIdxAndCount);
}