package power.ejb.manage.plan;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for BpCPlanTopicItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */

/**
 * @author Administrator
 * 
 */

@Remote
public interface BpCPlanTopicItemFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpCPlanTopicItem entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCPlanTopicItem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(BpCPlanTopicItem entity);

	/**
	 * Delete a persistent BpCPlanTopicItem entity.
	 * 
	 * @param entity
	 *            BpCPlanTopicItem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpCPlanTopicItem entity);

	/**
	 * Persist a previously saved BpCPlanTopicItem entity and return it or a
	 * copy of it to the sender. A copy of the BpCPlanTopicItem entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpCPlanTopicItem entity to update
	 * @return BpCPlanTopicItem the persisted BpCPlanTopicItem entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpCPlanTopicItem update(BpCPlanTopicItem entity);

	public BpCPlanTopicItem findById(BpCPlanTopicItemId id);

	/**
	 * @param propertyName
	 * @param value
	 * @return
	 */
	public List<BpCPlanTopicItem> findByProperty(String propertyName,
			Object value);

	/**
	 * @param updateList
	 */
	public void update(List<BpCPlanTopicItem> updateList);

	/**
	 * 删除某一主题选中的所有指标，主题编码在复合主键id中
	 * 
	 * @param ids
	 * @return
	 */
	public boolean delete(String ids);

	/**
	 * 删除某一主题所有指标
	 * 
	 * @param topicCode
	 * @param enterpriseCode
	 * @return
	 */
	public boolean deleteTopicItem(String topicCode, String enterpriseCode);

	/**
	 * 判断是否重复，返回数据库记录条数
	 * 
	 * @param topicCode
	 * @param itemCode
	 * @return
	 */
	public Long isNew(String topicCode, String itemCode);

	/**
	 * @param addList
	 */
	public void save(List<BpCPlanTopicItem> addList,
			List<BpCPlanTopicItem> updateList, String ids);

	/**
	 * 取数据 Find all BpCPlanTopicItem entities.
	 * 
	 * @return List<BpCPlanTopicItem> all BpCPlanTopicItem entities
	 */
	public PageObject findAll(String enterpriseCode, String topicCode,
			int... rowStartIdxAndCount);
}