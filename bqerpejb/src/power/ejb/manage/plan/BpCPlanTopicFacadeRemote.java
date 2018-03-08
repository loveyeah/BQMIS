package power.ejb.manage.plan;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpCPlanTopicFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCPlanTopicFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpCPlanTopic entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCPlanTopic entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCPlanTopic entity);

	/**
	 * Delete a persistent BpCPlanTopic entity.
	 * 
	 * @param entity
	 *            BpCPlanTopic entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCPlanTopic entity);

	/**
	 * Persist a previously saved BpCPlanTopic entity and return it or a copy of
	 * it to the sender. A copy of the BpCPlanTopic entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            BpCPlanTopic entity to update
	 * @return BpCPlanTopic the persisted BpCPlanTopic entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCPlanTopic update(BpCPlanTopic entity);

	public BpCPlanTopic findById(String id);

	/**
	 * Find all BpCPlanTopic entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCPlanTopic property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCPlanTopic> found by query
	 */
	public List<BpCPlanTopic> findByProperty(String propertyName, Object value);

	/**
	 * 检查主题名是否重复，返回数据库记录条数
	 * 
	 * @param topicName
	 * @return
	 */
	public Long checkTopicName(String topicName);

	/**
	 * @param updateList
	 */
	public void update(List<BpCPlanTopic> updateList);

	/**
	 * @param ids
	 * @return
	 */
	public boolean delete(String ids);

	/**
	 * @param addList
	 */
	public void save(List<BpCPlanTopic> addList);

	/**
	 * 根据指标编码、指标名称查询指标
	 * 
	 * @param argFuzzy
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAllFitItem(String argFuzzy,
			int... rowStartIdxAndCount);

	/**
	 * Find all BpCPlanTopic entities.
	 * 
	 * @return List<BpCPlanTopic> all BpCPlanTopic entities
	 */
	public PageObject findAll(String enterpriseCode, int... rowStartIdxAndCount);
}