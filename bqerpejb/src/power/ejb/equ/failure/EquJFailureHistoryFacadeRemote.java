package power.ejb.equ.failure;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for EquJFailureHistoryFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface EquJFailureHistoryFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved EquJFailureHistory
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            EquJFailureHistory entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJFailureHistory entity);

	/**
	 * Delete a persistent EquJFailureHistory entity.
	 * 
	 * @param entity
	 *            EquJFailureHistory entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJFailureHistory entity);

	/**
	 * Persist a previously saved EquJFailureHistory entity and return it or a
	 * copy of it to the sender. A copy of the EquJFailureHistory entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquJFailureHistory entity to update
	 * @return EquJFailureHistory the persisted EquJFailureHistory entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJFailureHistory update(EquJFailureHistory entity);

	public EquJFailureHistory findById(Long id);

	/**
	 * Find all EquJFailureHistory entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJFailureHistory property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJFailureHistory> found by query
	 */
	public List<EquJFailureHistory> findByProperty(String propertyName,
			Object value, int... rowStartIdxAndCount);

	/**
	 * Find all EquJFailureHistory entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJFailureHistory> all EquJFailureHistory entities
	 */
	public List<EquJFailureHistory> findAll(int... rowStartIdxAndCount);
	/**
	 * 获取某种审批类型的最后一条历史记录
	 * @param code
	 * @param type
	 * @return
	 */
	public EquFailuresHisInfo findApplyType(String code,String type,String enterprisecode);
	/**
	 * 查询缺陷已延期次数
	 * @param code
	 * @param enterprisecode
	 * @return
	 */
	public String  findAwaitCount(String code,String enterprisecode);
	/**
	 * 查询缺陷审批列表
	 * @param failurecode
	 * @param enterprisecode
	 * @return
	 */
	public List<EquFailuresHisInfo> findApproveList(String failurecode,String enterprisecode);
}