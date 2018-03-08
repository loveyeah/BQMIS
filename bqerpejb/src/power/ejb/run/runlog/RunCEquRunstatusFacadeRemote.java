package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCEquRunstatusFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCEquRunstatusFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCEquRunstatus entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCEquRunstatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public int save(RunCEquRunstatus entity);

	/**
	 * Delete a persistent RunCEquRunstatus entity.
	 * 
	 * @param entity
	 *            RunCEquRunstatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCEquRunstatus entity);

	/**
	 * Persist a previously saved RunCEquRunstatus entity and return it or a
	 * copy of it to the sender. A copy of the RunCEquRunstatus entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCEquRunstatus entity to update
	 * @return RunCEquRunstatus the persisted RunCEquRunstatus entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCEquRunstatus update(RunCEquRunstatus entity);

	public RunCEquRunstatus findById(Long id);

	/**
	 * Find all RunCEquRunstatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCEquRunstatus property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCEquRunstatus> found by query
	 */
	public List<RunCEquRunstatus> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunCEquRunstatus entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCEquRunstatus> all RunCEquRunstatus entities
	 */
	public List<RunCEquRunstatus> findAll(int... rowStartIdxAndCount);
	
	/**
	 * 根据使用状态显示列表
	 * @param isUse
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public List<RunCEquRunstatus> findByIsUse(Object isUse,int... rowStartIdxAndCount);
	/**
	 * 根据企业编码查询
	 * @param run_equ_id
	 * @param enterpriseCode
	 * @return
	 */
	public List findRunStatusList(String run_equ_id ,String enterpriseCode);
	
	/**
	 * 判断状态编码是否重复
	 * @param enterpriseCode
	 * @param equstatusId
	 * @param runequId
	 * @return
	 */
	public boolean CheckEquStatusSame(String enterpriseCode,Long equstatusId,Long runequId);
	/**
	 * 根据运行方式下设备ID获取某设备除了实时状态外的其它状态列表
	 * @param runEquId
	 * @param statusId
	 * @param enterpriseCode
	 * @return
	 */
	public List GetListExcept(Long runEquId, Long statusId, String enterpriseCode);
}