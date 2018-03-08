package power.ejb.manage.exam;

import java.util.List;

import javax.ejb.Remote;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;

/**
 * Remote interface for BpCCbmItemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface BpCCbmItemFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved BpCCbmItem entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpCCbmItem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(BpCCbmItem entity);

	/**
	 * Delete a persistent BpCCbmItem entity.
	 * 
	 * @param entity
	 *            BpCCbmItem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean delete(BpCCbmItem entity);

	public boolean delete(String ids);

	/**
	 * Persist a previously saved BpCCbmItem entity and return it or a copy of
	 * it to the sender. A copy of the BpCCbmItem entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            BpCCbmItem entity to update
	 * @return BpCCbmItem the persisted BpCCbmItem entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public boolean update(BpCCbmItem entity);

	/**
	 * Find all BpCCbmItem entities.
	 * 
	 * @return List<BpCCbmItem> all BpCCbmItem entities
	 */
	public PageObject findAllForTopic(String enterpriseCode);

	public PageObject findAllItemToSelect(String searchKey,
			String enterpriseCode, int... rowStartIdxAndCount) throws Exception;

	public Object findItemInfo(String node);

	public BpCCbmItem findById(Long id);

	public boolean namePermitted(String name);

	public boolean namePermitted(String name, Long id);

	public List<TreeNode> findStatTreeList(String node, String enterpriseCode,
			String searchKey);

}