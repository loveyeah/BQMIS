package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCLanguageFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCLanguageFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCLanguage entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCLanguage entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCLanguage entity);

	/**
	 * Delete a persistent HrCLanguage entity.
	 *
	 * @param entity
	 *            HrCLanguage entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCLanguage entity);

	/**
	 * Persist a previously saved HrCLanguage entity and return it or a copy of
	 * it to the sender. A copy of the HrCLanguage entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCLanguage entity to update
	 * @return HrCLanguage the persisted HrCLanguage entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCLanguage update(HrCLanguage entity);

	public HrCLanguage findById(Long id);

	/**
	 * Find all HrCLanguage entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCLanguage property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCLanguage> found by query
	 */
	public List<HrCLanguage> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrCLanguage entities.
	 *
	 * @return List<HrCLanguage> all HrCLanguage entities
	 */
	public List<HrCLanguage> findAll();
	/**
	 * 查找所有语种
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllLanguages(String enterpriseCode);
}