package power.ejb.run.runlog.shift;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCShiftFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCShiftFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCShift entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCShift entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCShift entity);

	/**
	 * Delete a persistent RunCShift entity.
	 * 
	 * @param entity
	 *            RunCShift entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCShift entity);

	/**
	 * Persist a previously saved RunCShift entity and return it or a copy of it
	 * to the sender. A copy of the RunCShift entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunCShift entity to update
	 * @returns RunCShift the persisted RunCShift entity instance, may not be
	 *          the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCShift update(RunCShift entity);

	public RunCShift findById(Long id);

	/**
	 * Find all RunCShift entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCShift property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShift> found by query
	 */
	public List<RunCShift> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all RunCShift entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShift> all RunCShift entities
	 */
	public List<RunCShift> findAll(int... rowStartIdxAndCount);
	/*
	 * 查询班组列表(所有)
	 */
	public List findShiftList(String enterpriseCode,String specialCode);
	/*
	 * 查询班组列表(所有值班班组)
	 */
	public List findIsShiftList(String enterpriseCode,String specialCode);
	/*
	 * 判断名称是否重复
	 */
	public boolean isShiftNameExsit(Long initalNo,String shiftName,String enterpriseCode,Long id);
	/*
	 * 查询班组实际设置条数
	 */
	public Long shiftCount(Long initalNO,String enterpriseCode);
	/*
	 * 查询班组列表
	 */
	public List<RunCShift> findListByNO(Long initalNO,String enterpriseCode);
	/**
	 * 查询班组列表
	 * @param initalNO
	 * @param enterpriseCode
	 * @return
	 */
	public List<RunCShift> findListByNoC(Long initalNO,String enterpriseCode);
}