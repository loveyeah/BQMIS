package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCStudytypeFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCStudytypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCStudytype entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCStudytype entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCStudytype entity);

	/**
	 * Delete a persistent HrCStudytype entity.
	 *
	 * @param entity
	 *            HrCStudytype entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCStudytype entity);

	/**
	 * Persist a previously saved HrCStudytype entity and return it or a copy of
	 * it to the sender. A copy of the HrCStudytype entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCStudytype entity to update
	 * @return HrCStudytype the persisted HrCStudytype entity instance, may not
	 *         be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCStudytype update(HrCStudytype entity);

	public HrCStudytype findById(Long id);

	/**
	 * Find all HrCStudytype entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCStudytype property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCStudytype> found by query
	 */
	public List<HrCStudytype> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrCStudytype entities.
	 *
	 * @return List<HrCStudytype> all HrCStudytype entities
	 */
	public List<HrCStudytype> findAll();
	/**
	 * 查找所有学习类别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllStudytypes(String enterpriseCode);
	
	/**
	 * add by liuyi 20100611 
	 * 通过名称获得id
	 * @param name
	 * @param enterpriseCode
	 * @return
	 */
	Long getStudytypeIdByName(String name,String enterpriseCode);
}