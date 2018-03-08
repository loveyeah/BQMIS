package power.ejb.manage.budget;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;

/**
 * Remote interface for CbmCModelFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface CbmCModelFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved CbmCModel entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            CbmCModel entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(CbmCModel entity);

	/**
	 * Delete a persistent CbmCModel entity.
	 * 
	 * @param entity
	 *            CbmCModel entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(CbmCModel entity);

	/**
	 * Persist a previously saved CbmCModel entity and return it or a copy of it
	 * to the sender. A copy of the CbmCModel entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            CbmCModel entity to update
	 * @return CbmCModel the persisted CbmCModel entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public CbmCModel update(CbmCModel entity);

	public CbmCModel findById(Long id);

	/**
	 * Find all CbmCModel entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the CbmCModel property to query
	 * @param value
	 *            the property value to match
	 * @return List<CbmCModel> found by query
	 */
	public List<CbmCModel> findByProperty(String propertyName, Object value);

	/**
	 * Find all CbmCModel entities.
	 * 
	 * @return List<CbmCModel> all CbmCModel entities
	 */
	public PageObject findAll(String argFuzzy);

	public List<TreeNode> findModelTreeList(String itemCode,
			String enterpriseCode);
}