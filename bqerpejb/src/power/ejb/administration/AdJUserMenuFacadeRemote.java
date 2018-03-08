package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.DataChangeException;

/**
 * Remote interface for AdJUserMenuFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdJUserMenuFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdJUserMenu entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdJUserMenu entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdJUserMenu entity);

	/**
	 * Delete a persistent AdJUserMenu entity.
	 * 
	 * @param entity
	 *            AdJUserMenu entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdJUserMenu entity);

	/**
	 * Persist a previously saved AdJUserMenu entity and return it or a copy of
	 * it to the sender. A copy of the AdJUserMenu entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdJUserMenu entity to update
	 * @return AdJUserMenu the persisted AdJUserMenu entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdJUserMenu update(AdJUserMenu entity);

	public AdJUserMenu findById(Long id);

	/**
	 * Find all AdJUserMenu entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdJUserMenu property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdJUserMenu> found by query
	 */
	public List<AdJUserMenu> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdJUserMenu entities.
	 * 
	 * @return List<AdJUserMenu> all AdJUserMenu entities
	 */
	public List<AdJUserMenu> findAll();
	/**
	 * 退回处理
	 * 
	 * @param oldUpdateTime
	 * @param entity
	 * @throws SQLException 
	 * @throws DataChangeException 
	 */
	public void backUpdate(AdJUserMenu entity, String oldUpdateTime) throws SQLException, DataChangeException;
}