package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCSpecialtyFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCSpecialtyFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCSpecialty entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCSpecialty entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCSpecialty entity);

	/**
	 * Delete a persistent HrCSpecialty entity.
	 *
	 * @param entity
	 *            HrCSpecialty entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCSpecialty entity);

	/**
	 * Persist a previously saved HrCSpecialty entity and return it or a copy of
	 * it to the sender. A copy of the HrCSpecialty entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCSpecialty entity to update
	 * @return HrCSpecialty the persisted HrCSpecialty entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCSpecialty update(HrCSpecialty entity);

	public HrCSpecialty findById(Long id);

	/**
	 * Find all HrCSpecialty entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCSpecialty property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCSpecialty> found by query
	 */
	public List<HrCSpecialty> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrCSpecialty entities.
	 *
	 * @return List<HrCSpecialty> all HrCSpecialty entities
	 */
	public List<HrCSpecialty> findAll();
	/**
	 * 查找所有学习专业
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllSpecialtys(String enterpriseCode);
	
	/**
	 * add by liuyi 20100611
	 * 根据名称获取id
	 * @param name
	 * @param enterpriseCode
	 * @return
	 */
	Long getSpecialyIdByName(String name,String enterpriseCode);
}