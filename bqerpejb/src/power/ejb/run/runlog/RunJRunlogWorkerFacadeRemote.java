package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJRunlogWorkerFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJRunlogWorkerFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunJRunlogWorker entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunJRunlogWorker entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunJRunlogWorker entity);

	/**
	 * Delete a persistent RunJRunlogWorker entity.
	 * 
	 * @param entity
	 *            RunJRunlogWorker entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJRunlogWorker entity);

	/**
	 * Persist a previously saved RunJRunlogWorker entity and return it or a
	 * copy of it to the sender. A copy of the RunJRunlogWorker entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunJRunlogWorker entity to update
	 * @returns RunJRunlogWorker the persisted RunJRunlogWorker entity instance,
	 *          may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJRunlogWorker update(RunJRunlogWorker entity);

	public RunJRunlogWorker findById(Long id);

	/**
	 * Find all RunJRunlogWorker entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJRunlogWorker property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJRunlogWorker> found by query
	 */
	public List<RunJRunlogWorker> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunJRunlogWorker entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJRunlogWorker> all RunJRunlogWorker entities
	 */
	public List<RunJRunlogWorker> findAll(int... rowStartIdxAndCount);
	/**
	 * 查询某运行日志下该专业及其下级专业值班人员情况列表
	 * @param enterpriseCode
	 * @param runlogno
	 * @param specialcode
	 * @return
	 */
	public List<RunJRunlogWorkerModel> findRunLogWorkerAll(String enterpriseCode,String runlogno,String specialcode);
	/**
	 * 判断值班人员是否重复
	 * @param runlogid
	 * @param empcode
	 * @param enterprisecode
	 * @return
	 */
	public boolean isExsit(Long runlogid,String empcode,String enterprisecode);
	public RunJRunlogWorker findModelByemp(Long runlogid,String workercode,String enterprisecode);
	/**
	 * 根据专业查询一段时间的值班人员列表
	 * @param specialcode
	 * @param enterprisecode
	 * @param fromdate
	 * @param todate
	 * @return
	 */
	public PageObject queryWorkerList(String specialcode,String enterprisecode,String fromdate,String todate,final int... rowStartIdxAndCount);
	/**
	 * 根据专业查询一段时间的缺勤人员列表
	 * @param specialcode
	 * @param enterprisecode
	 * @param fromdate
	 * @param todate
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject queryAbsentWorkerList(String specialcode,String enterprisecode,String fromdate,String todate,final int... rowStartIdxAndCount);
}