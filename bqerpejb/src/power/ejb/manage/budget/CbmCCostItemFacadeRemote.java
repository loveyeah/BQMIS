package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.CodeRepeatException;
import power.ejb.comm.TreeNode;

/**
 * Remote interface for CbmCCostItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCCostItemFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved CbmCCostItem entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            CbmCCostItem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(CbmCCostItem entity) throws CodeRepeatException;

	
	public void delete(CbmCCostItem entity);

	/**
	 * Persist a previously saved CbmCCostItem entity and return it or a copy of
	 * it to the sender. A copy of the CbmCCostItem entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            CbmCCostItem entity to update
	 * @return CbmCCostItem the persisted CbmCCostItem entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public CbmCCostItem update(CbmCCostItem entity) throws CodeRepeatException;

	public CbmCCostItem findById(Long id);

	/**
	 * Find all CbmCCostItem entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CbmCCostItem property to query
	 * @param value
	 *            the property value to match
	 * @return List<CbmCCostItem> found by query
	 */
	public List<CbmCCostItem> findByProperty(String propertyName, Object value);

	/**
	 * 
	 * @param id
	 * @return
	 */
	public List<CbmCCostItem> findAll(String id);
	
	/**
	 * 取得结点编码为node的所有第一层子结点
	 * @param node
	 * @return
	 */
	public List<TreeNode> findCostTreeList(String node);
	/**
	 * 计算等级
	 * @param id
	 * @return
	 */
	public Long getaccountOrder(Long id);
}