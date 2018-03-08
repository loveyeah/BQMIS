package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdCCarwhProFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdCCarwhProFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdCCarwhPro entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdCCarwhPro entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdCCarwhPro entity) throws SQLException;

	/**
	 * Delete a persistent AdCCarwhPro entity.
	 * 
	 * @param entity
	 *            AdCCarwhPro entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdCCarwhPro entity) throws SQLException;

	/**
	 * Persist a previously saved AdCCarwhPro entity and return it or a copy of
	 * it to the sender. A copy of the AdCCarwhPro entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdCCarwhPro entity to update
	 * @return AdCCarwhPro the persisted AdCCarwhPro entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdCCarwhPro update(AdCCarwhPro entity) throws SQLException;

	public AdCCarwhPro findById(Long id);

	/**
	 * Find all AdCCarwhPro entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCCarwhPro property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCCarwhPro> found by query
	 */
	public List<AdCCarwhPro> findByProperty(String propertyName, Object value);

	/**
	 * Find all AdCCarwhPro entities.
	 * 
	 * @return List<AdCCarwhPro> all AdCCarwhPro entities
	 * @throws SQLException 
	 */
	public PageObject findAll(String enterpriseCode,final int... rowStartIdxAndCount) throws SQLException;
}