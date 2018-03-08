package power.ejb.run.runlog.shift;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJShiftRecordFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJShiftRecordFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunJShiftRecord entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunJShiftRecord entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunJShiftRecord entity);

	/**
	 * Delete a persistent RunJShiftRecord entity.
	 * 
	 * @param entity
	 *            RunJShiftRecord entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJShiftRecord entity);

	/**
	 * Persist a previously saved RunJShiftRecord entity and return it or a copy
	 * of it to the sender. A copy of the RunJShiftRecord entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunJShiftRecord entity to update
	 * @return RunJShiftRecord the persisted RunJShiftRecord entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJShiftRecord update(RunJShiftRecord entity);

	public RunJShiftRecord findById(Long id);

	/**
	 * Find all RunJShiftRecord entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJShiftRecord property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJShiftRecord> found by query
	 */
	public List<RunJShiftRecord> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunJShiftRecord entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJShiftRecord> all RunJShiftRecord entities
	 */
	public List<RunJShiftRecord> findAll(int... rowStartIdxAndCount);

	/**
	 * Find all RunJShiftRecord entities.
	 * 
	 * @param runLogId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJShiftRecord> all RunJShiftRecord entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJShiftRecord> findAll(Long runLogId, String enterpriseCode,
			final int... rowStartIdxAndCount);

	// /**
	// * Find all RunJShiftRecord entities.
	// *
	// * @param enterpriseCode
	// * @param rowStartIdxAndCount
	// * Optional int varargs. rowStartIdxAndCount[0] specifies the the
	// * row index in the query result-set to begin collecting the
	// * results. rowStartIdxAndCount[1] specifies the the maximum
	// * count of results to return.
	// * @return List<RunJShiftRecord> all RunJShiftRecord entities
	// */
	// @SuppressWarnings("unchecked")
	// public List<RunJShiftRecord> findListByRunLogId(String runlogid,
	// String enterpriseCode, final int... rowStartIdxAndCount);

	/**
	 * 根据运行日志id，是否完成标志查找值班记事列表
	 * 
	 * @param runLogId
	 * @param enterpriseCode
	 * @return String
	 */
	public PageObject findListByRunLogId(Long runLogId, String enterpriseCode,
			final int... rowStartIdxAndCount);

	/**
	 * 查找未完成项
	 * 
	 * @param runlogid
	 * @param enterpriseCode
	 * @return List<RunJShiftRecrodForm>
	 */
	public PageObject findNotCompletionList(Long runlogid, String enterpriseCode,
			final int... rowStartIdxAndCount);

	/**
	 * 运行日志查询---值班记事查询
	 * 
	 * @param startDate
	 * @param endDate
	 * @param specialityCode
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getShiftRecordListByDate(String startDate, String endDate,
			String specialityCode, String enterpriseCode,
			final int... rowStartIdxAndCount);

	/**
	 * 运行日志查询---未完成项查询
	 * 
	 * @param startDate
	 * @param endDate
	 * @param specialityCode
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getNotCompletionListByDate(String startDate, String endDate,
			String specialityCode, String enterpriseCode,
			final int... rowStartIdxAndCount);

	/**
	 * 查找未完成项
	 * 
	 * @param runLogId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJShiftRecord> all RunJShiftRecord entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJShiftRecord> findNotCompletion(Long runLogId,
			String enterpriseCode, final int... rowStartIdxAndCount);

	/**
	 * 查找没有状态的值班记事
	 * 
	 * @param runLogId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJShiftRecord> all RunJShiftRecord entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJShiftRecord> findNoStatus(Long runLogId,
			String enterpriseCode, final int... rowStartIdxAndCount);

	/**
	 * 运行日志查询---其他班组值班记事查询
	 * 
	 * @param specialityCode
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getOtherRecordListByDate(Long runLogId,
			String specialityCode, String enterpriseCode,
			final int... rowStartIdxAndCount);

	/**
	 * 导入其它班组值班记事
	 * 
	 * @param workerCode
	 * @param runLogId
	 * @param ids
	 */
	public void impOtherRecord(String workerCode, Long runLogId, String[] ids);
	
	/**
	 * 未完成项追溯
	 * 
	 * @param recordId
	 * @param enterpriseCode
	 * @param start
	 * @param limit
	 * @return String
	 */
	public String reviewNotCompletion(Long recordId, String enterpriseCode,
			final int... rowStartIdxAndCount);
	
}