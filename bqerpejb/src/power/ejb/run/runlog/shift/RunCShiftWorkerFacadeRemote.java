package power.ejb.run.runlog.shift;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCShiftWorkerFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCShiftWorkerFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCShiftWorker entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCShiftWorker entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCShiftWorker entity);

	/**
	 * Delete a persistent RunCShiftWorker entity.
	 * 
	 * @param entity
	 *            RunCShiftWorker entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCShiftWorker entity);

	/**
	 * Persist a previously saved RunCShiftWorker entity and return it or a copy
	 * of it to the sender. A copy of the RunCShiftWorker entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCShiftWorker entity to update
	 * @returns RunCShiftWorker the persisted RunCShiftWorker entity instance,
	 *          may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCShiftWorker update(RunCShiftWorker entity);

	public RunCShiftWorker findById(Long id);

	/**
	 * Find all RunCShiftWorker entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCShiftWorker property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftWorker> found by query
	 */
	public List<RunCShiftWorker> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunCShiftWorker entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftWorker> all RunCShiftWorker entities
	 */
	public List<RunCShiftWorker> findAll(int... rowStartIdxAndCount);
	/*
	 * 查询班组值班人员列表
	 */
	public List<RunCShiftWorker> findListByShift(Long shiftId,String enterpriseCode);
	/*
	 * 查询班组值班人员列表
	 */
	public List findWorkerList(Long shiftId,String enterpriseCode);
	/*
	 * 判断班组值班人员是否存在
	 */
	public boolean isWorkerExsit(Long shiftId,String empCode,String enterpriseCode);
	/*
	 * 查询班组值班人员岗位列表
	 */
	public String takeWorkerStation(Long shiftId,String worker,String enterpriseCode);
	/*
	 * 查询是否是和某班组同专业下的值班人员
	 */
	public boolean isPrShiftWorker(Long shiftid,String enterpriseCode,String workerCode);
	/*
	 * 判断某员工是否是某班组的值班人员
	 */
	public RunCShiftWorker getShiftWorker(String enterprisecode,String workercode,Long shiftid);
	/*
	 * 判断某员工是否具备交接班权限
	 */
	public boolean isHand(String workercode,String enterprisecode,Long shiftid);
	/*
	 * 判断某员工是否维护在运行中，如是的话返回所在的运行专业
	 */
	public String getRunSpecialByEmp(String workercode,String enterprisecode);
}