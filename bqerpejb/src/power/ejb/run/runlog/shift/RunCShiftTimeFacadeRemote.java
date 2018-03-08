package power.ejb.run.runlog.shift;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for RunCShiftTimeFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCShiftTimeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCShiftTime entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCShiftTime entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCShiftTime entity);

	/**
	 * Delete a persistent RunCShiftTime entity.
	 * 
	 * @param entity
	 *            RunCShiftTime entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCShiftTime entity);

	/**
	 * Persist a previously saved RunCShiftTime entity and return it or a copy
	 * of it to the sender. A copy of the RunCShiftTime entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCShiftTime entity to update
	 * @returns RunCShiftTime the persisted RunCShiftTime entity instance, may
	 *          not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCShiftTime update(RunCShiftTime entity);

	public RunCShiftTime findById(Long id);

	/**
	 * Find all RunCShiftTime entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCShiftTime property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftTime> found by query
	 */
	public List<RunCShiftTime> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunCShiftTime entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCShiftTime> all RunCShiftTime entities
	 */
	public List<RunCShiftTime> findAll(int... rowStartIdxAndCount);
	/*
	 * 查询班次设置列表
	 */
	public List findShfitTimeListBySpecial(String specialCode,String enterpriseCode);
	/*
	 * 查询班次设置列表(不包括休息班)
	 */
	public List<RunCShiftTime> findIsShfitTimeListBySpecial(String specialCode,String enterpriseCode);
	/*
	 * 判断班次名称是否重复
	 */
	public boolean isTimeNameExsit(String name,Long initalNo,Long id,String enterpriseCode);
	/*
	 * 获取班次实际设置条数
	 */
	public Long tiemCount(Long intialNo,String enterpriseCode);
	/*
	 * 获取班次详细信息
	 */
	public Object findTimeModel(Long id);
	/*
	 * 包括休息班
	 */
	public List<RunCShiftTime> findTimeListByNo(Long intialNo,String enterpriseCode);
	/*
	 * 不包括休息班
	 */
	public List<RunCShiftTime> findTimeListByNoEr(Long intialNo,String enterpriseCode);
	/*
	 * 根据班次id获得对应的设置序号所设置的班次总数
	 */
	public int getShiftTimeAmount(Long shifttimeId);
}