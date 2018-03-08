package power.ejb.manage.plan;

import java.util.List;
import javax.ejb.Remote;

import power.ejb.comm.TreeNode;

/**
 * Remote interface for BpCPlanItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCPlanItemFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpCPlanItem entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCPlanItem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(BpCPlanItem entity);

	/**
	 * Delete a persistent BpCPlanItem entity.
	 * 
	 * @param entity
	 *            BpCPlanItem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(BpCPlanItem entity);

	/**
	 * Persist a previously saved BpCPlanItem entity and return it or a copy of
	 * it to the sender. A copy of the BpCPlanItem entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            BpCPlanItem entity to update
	 * @return BpCPlanItem the persisted BpCPlanItem entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public boolean update(BpCPlanItem entity, String oldItemCode);

	public BpCPlanItem findById(String id);

	/**
	 * Find all BpCPlanItem entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpCPlanItem property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpCPlanItem> found by query
	 */
	public List<BpCPlanItem> findByProperty(String propertyName, Object value);

	/**
	 * Find all BpCPlanItem entities.
	 * 
	 * @return List<BpCPlanItem> all BpCPlanItem entities
	 */
	public List<BpCPlanItem> findAll();

	public List<TreeNode> findStatTreeList(String node, String enterpriseCode);

	public boolean isleaf(String itemCode);

	public List<Object[]> getAllReferItem(String itemCode);

	public BpCPlanItem setAccountOrder(BpCPlanItem entity);
}