package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCSchoolFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCSchoolFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCSchool entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCSchool entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCSchool entity);

	/**
	 * Delete a persistent HrCSchool entity.
	 *
	 * @param entity
	 *            HrCSchool entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCSchool entity);

	/**
	 * Persist a previously saved HrCSchool entity and return it or a copy of it
	 * to the sender. A copy of the HrCSchool entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCSchool entity to update
	 * @return HrCSchool the persisted HrCSchool entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCSchool update(HrCSchool entity);

	public HrCSchool findById(Long id);

	/**
	 * Find all HrCSchool entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCSchool property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCSchool> found by query
	 */
	public List<HrCSchool> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrCSchool entities.
	 *
	 * @return List<HrCSchool> all HrCSchool entities
	 */
	public List<HrCSchool> findAll();
	/**
	 * 查找所有学校
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllSchools(String enterpriseCode);
	
	/**
	 * add by liuyi 20100611
	 * 通过名称获得id
	 * @param name
	 * @param enterpriseCode
	 * @return
	 */
	Long getSchoolCodeIdByName(String name,String enterpriseCode);
}