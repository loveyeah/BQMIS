package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCPoliticsFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCPoliticsFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCPolitics entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCPolitics entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCPolitics entity);

	/**
	 * Delete a persistent HrCPolitics entity.
	 *
	 * @param entity
	 *            HrCPolitics entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCPolitics entity);

	/**
	 * Persist a previously saved HrCPolitics entity and return it or a copy of
	 * it to the sender. A copy of the HrCPolitics entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCPolitics entity to update
	 * @return HrCPolitics the persisted HrCPolitics entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCPolitics update(HrCPolitics entity);

	public HrCPolitics findById(Long id);

	/**
	 * Find all HrCPolitics entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCPolitics property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCPolitics> found by query
	 */
	public List<HrCPolitics> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrCPolitics entities.
	 *
	 * @return List<HrCPolitics> all HrCPolitics entities
	 */
	public List<HrCPolitics> findAll();
	/**
	 * 查找所有政治面貌
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllPolitics(String enterpriseCode);
}