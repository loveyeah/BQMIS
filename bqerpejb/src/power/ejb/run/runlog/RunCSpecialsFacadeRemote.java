package power.ejb.run.runlog;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.equ.base.EquCLocation;
import power.ejb.run.runlog.shift.RunCUnitprofession;

/**
 * Remote interface for RunCSpecialsFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCSpecialsFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCSpecials entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCSpecials entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCSpecials entity);

	/**
	 * Delete a persistent RunCSpecials entity.
	 * 
	 * @param entity
	 *            RunCSpecials entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCSpecials entity);

	/**
	 * Persist a previously saved RunCSpecials entity and return it or a copy of
	 * it to the sender. A copy of the RunCSpecials entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunCSpecials entity to update
	 * @return RunCSpecials the persisted RunCSpecials entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCSpecials update(RunCSpecials entity);

	public RunCSpecials findById(Long id);

	/**
	 * Find all RunCSpecials entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCSpecials property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCSpecials> found by query
	 */
	public List<RunCSpecials> findByProperty(String propertyName, Object value,
			int... rowStartIdxAndCount);

	/**
	 * Find all RunCSpecials entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCSpecials> all RunCSpecials entities
	 */
	public List<RunCSpecials> findAll(int... rowStartIdxAndCount);
	
	public List<RunCSpecials> getListByParent(String speciality_code,String enterpriseCode);
	
	public RunCSpecials findByCode(String RunCSpecials,String enterpriseCode);
	
	public boolean isParentNode(String specialCode,String enterpriseCode);
	
	public List<RunCSpecials> findSpeList(String enterpriseCode);
	
	public boolean existsByCode(String code, String enterpriseCode,Long... specialId);
	
	/**
	 * 根据类型查找专业列表
	 * @param specialityType 0表示公用,1表示运行,2表示检修，3表示检修管理专用
	 * @param enterpriseCode
	 * @return RunCSpecials
	 */
	public List<RunCSpecials> findByType(String specialityType, String enterpriseCode);
	
}