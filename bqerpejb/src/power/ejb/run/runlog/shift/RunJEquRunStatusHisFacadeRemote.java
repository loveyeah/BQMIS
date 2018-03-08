package power.ejb.run.runlog.shift;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for RunJEquRunStatusHisFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunJEquRunStatusHisFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunJEquRunStatusHis
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            RunJEquRunStatusHis entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunJEquRunStatusHis entity);

	/**
	 * Delete a persistent RunJEquRunStatusHis entity.
	 * 
	 * @param entity
	 *            RunJEquRunStatusHis entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJEquRunStatusHis entity);

	/**
	 * Persist a previously saved RunJEquRunStatusHis entity and return it or a
	 * copy of it to the sender. A copy of the RunJEquRunStatusHis entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            RunJEquRunStatusHis entity to update
	 * @return RunJEquRunStatusHis the persisted RunJEquRunStatusHis entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJEquRunStatusHis update(RunJEquRunStatusHis entity);

	public RunJEquRunStatusHis findById(Long id);

	/**
	 * Find all RunJEquRunStatusHis entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJEquRunStatusHis property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJEquRunStatusHis> found by query
	 */
	public List<RunJEquRunStatusHis> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunJEquRunStatusHis entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJEquRunStatusHis> all RunJEquRunStatusHis entities
	 */
	public List<RunJEquRunStatusHis> findAll(int... rowStartIdxAndCount);
	public PageObject findList(String strWhere,final int... rowStartIdxAndCount);
	/**
	 * 取得某设备最近一次状态变化记录
	 * @param equcode
	 * @param enterprisecode
	 * @return
	 */
	public List<RunJEquRunStatusHis> getModelByStatus(String equcode,String enterprisecode);
	
	/**
	 * 根据设备功能码，企业编码查询设备状态变更列表
	 * @param equCode
	 * @param enterpriseCode
	 * @return
	 */
	public List getEquStatusHisList(String equCode,String enterpriseCode);
}