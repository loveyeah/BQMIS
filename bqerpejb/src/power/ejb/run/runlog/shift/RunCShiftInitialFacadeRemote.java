package power.ejb.run.runlog.shift;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCShiftInitialFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCShiftInitialFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCShiftInitial entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCShiftInitial entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public Long save(RunCShiftInitial entity);

	/**
	 * Delete a persistent RunCShiftInitial entity.
	 * 
	 * @param entity
	 *            RunCShiftInitial entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCShiftInitial entity);

	/**
	 * Persist a previously saved RunCShiftInitial entity and return it or a
	 * copy of it to the sender. A copy of the RunCShiftInitial entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCShiftInitial entity to update
	 * @returns RunCShiftInitial the persisted RunCShiftInitial entity instance,
	 *          may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCShiftInitial update(RunCShiftInitial entity);

	public RunCShiftInitial findById(Long id);

	/**
	 * Find all RunCShiftInitial entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCShiftInitial property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftInitial> found by query
	 */
	public List<RunCShiftInitial> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunCShiftInitial entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftInitial> all RunCShiftInitial entities
	 */
	public List<RunCShiftInitial> findAll(int... rowStartIdxAndCount);
	/*
	 * 查询初始化列表
	 */
	public List findInitialList(String enterpriseCode);
	/*
	 * 查询某专业值班初始化情况
	 */
	public List<RunCShiftInitial> findInitialBySpecial(String specialCode,String enterpriseCode);
	/*
	 * 判断名称是否重复
	 */
	public boolean isInitialNameExsit(String enterpriseCode,String initialName,Long id);
	/*
	 * 查询一条记录
	 */
	public Object findModel(Long id);
	/*
	 * 查询有效期内专业初始化设置
	 */
	public List<RunCShiftInitial> findListByDate(String specialCode,String enterpriseCode,String startDate,String endDate);
	/**
	 * 查询在初始化有效期设置内的专业列表
	 * @param enterpriseCode
	 * @param date
	 * @return
	 */
	public List findActiveSpecialList(String enterpriseCode,String date);
}