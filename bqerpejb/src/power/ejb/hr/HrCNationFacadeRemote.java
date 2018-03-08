package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCNationFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCNationFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCNation entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCNation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCNation entity);

	/**
	 * Delete a persistent HrCNation entity.
	 *
	 * @param entity
	 *            HrCNation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCNation entity);

	/**
	 * Persist a previously saved HrCNation entity and return it or a copy of it
	 * to the sender. A copy of the HrCNation entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCNation entity to update
	 * @return HrCNation the persisted HrCNation entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCNation update(HrCNation entity);

	public HrCNation findById(Long id);

	/**
	 * Find all HrCNation entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCNation property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCNation> found by query
	 */
	public List<HrCNation> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrCNation entities.
	 *
	 * @return List<HrCNation> all HrCNation entities
	 */
	public List<HrCNation> findAll();
	/**
	 * 查找所有民族
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllNations(String enterpriseCode);
	
	/**
	 * add by liuyi 20100603
	 * 通过民族名称查找该民族的id,不存在则放回null
	 * @param nationName
	 * @param enterpriseCode
	 * @return
	 */
	Long findNationIdByName(String nationName,String enterpriseCode);
}