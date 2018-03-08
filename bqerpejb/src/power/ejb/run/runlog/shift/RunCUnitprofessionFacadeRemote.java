package power.ejb.run.runlog.shift;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.Remote;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBlock;
import power.ejb.run.runlog.RunCSpecials;

/**
 * Remote interface for RunCUnitprofessionFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface RunCUnitprofessionFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved RunCUnitprofession
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            RunCUnitprofession entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCUnitprofession entity);

	/**
	 * Delete a persistent RunCUnitprofession entity.
	 * 
	 * @param entity
	 *            RunCUnitprofession entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCUnitprofession entity);

	/**
	 * Persist a previously saved RunCUnitprofession entity and return it or a
	 * copy of it to the sender. A copy of the RunCUnitprofession entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCUnitprofession entity to update
	 * @returns RunCUnitprofession the persisted RunCUnitprofession entity
	 *          instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCUnitprofession update(RunCUnitprofession entity);

	public RunCUnitprofession findById(Long id);

	/**
	 * Find all RunCUnitprofession entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCUnitprofession property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCUnitprofession> found by query
	 */
	public List<RunCUnitprofession> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all RunCUnitprofession entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCUnitprofession> all RunCUnitprofession entities
	 */
	public List<RunCUnitprofession> findAll(int... rowStartIdxAndCount);
	
	public List findUnitList(String enterpriseCode);
	
	/**
	 * 根据专业编码判断是否已经存在
	 * 
	 * @param code
	 * @param enterpriseCode
	 * @return
	 */
	public boolean existsByCode(String code, String enterpriseCode);
	/**
	 * 取除去自身的运行对应的专业列表
	 * @param code
	 * @param enterpriseCode
	 * @return
	 */
	public List<RunCUnitprofession> findUnitExp(String code,String enterpriseCode);
	public List<RunCSpecials> findUnitPList(String enterprisecode);
}