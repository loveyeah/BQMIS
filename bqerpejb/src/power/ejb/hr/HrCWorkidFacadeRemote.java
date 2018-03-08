package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCWorkidFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCWorkidFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCWorkid entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCWorkid entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCWorkid entity);

	/**
	 * Delete a persistent HrCWorkid entity.
	 *
	 * @param entity
	 *            HrCWorkid entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCWorkid entity);

	/**
	 * Persist a previously saved HrCWorkid entity and return it or a copy of it
	 * to the sender. A copy of the HrCWorkid entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCWorkid entity to update
	 * @return HrCWorkid the persisted HrCWorkid entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCWorkid update(HrCWorkid entity);

	public HrCWorkid findById(Long id);

	/**
	 * Find all HrCWorkid entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCWorkid property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCWorkid> found by query
	 */
	public List<HrCWorkid> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrCWorkid entities.
	 *
	 * @return List<HrCWorkid> all HrCWorkid entities
	 */
	public List<HrCWorkid> findAll();
	/**
	 * 查找所有员工身份
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllWorkIds(String enterpriseCode);
	
	/**
	 * add by liuyi 20100610 
	 * 通过名称需找id
	 * @param name
	 * @return
	 */
	Long getWorkIdByWorkName(String name);
}