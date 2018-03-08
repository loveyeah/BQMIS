package power.ejb.administration;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for AdCCarwhPayFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface AdCCarwhPayFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved AdCCarwhPay entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            AdCCarwhPay entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(AdCCarwhPay entity) throws SQLException;

	/**
	 * Delete a persistent AdCCarwhPay entity.
	 * 
	 * @param entity
	 *            AdCCarwhPay entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(AdCCarwhPay entity) throws SQLException;

	/**
	 * Persist a previously saved AdCCarwhPay entity and return it or a copy of
	 * it to the sender. A copy of the AdCCarwhPay entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            AdCCarwhPay entity to update
	 * @return AdCCarwhPay the persisted AdCCarwhPay entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public AdCCarwhPay update(AdCCarwhPay entity) throws SQLException;

	public AdCCarwhPay findById(Long id);

	/**
	 * Find all AdCCarwhPay entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the AdCCarwhPay property to query
	 * @param value
	 *            the property value to match
	 * @return List<AdCCarwhPay> found by query
	 */
	public List<AdCCarwhPay> findByProperty(String propertyName, Object value);

	public List<AdCCarwhPay> findByPayCode(Object payCode);

	public List<AdCCarwhPay> findByPayName(Object payName);

	public List<AdCCarwhPay> findByIsUse(Object isUse);

	public List<AdCCarwhPay> findByUpdateUser(Object updateUser);

	/**
	 * Find all AdCCarwhPay entities.
	 * 
	 * @return List<AdCCarwhPay> all AdCCarwhPay entities
	 */
	public PageObject findAll(String enterpriseCode,final int... rowStartIdxAndCount) throws SQLException;
	@SuppressWarnings("unchecked")
	public PageObject findAllData(String enterpriseCode) throws SQLException;
}