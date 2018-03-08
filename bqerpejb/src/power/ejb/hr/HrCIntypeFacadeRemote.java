package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCIntypeFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCIntypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCIntype entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCIntype entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCIntype entity);

	/**
	 * Delete a persistent HrCIntype entity.
	 *
	 * @param entity
	 *            HrCIntype entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCIntype entity);

	/**
	 * Persist a previously saved HrCIntype entity and return it or a copy of it
	 * to the sender. A copy of the HrCIntype entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCIntype entity to update
	 * @return HrCIntype the persisted HrCIntype entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCIntype update(HrCIntype entity);

	public HrCIntype findById(Long id);

	/**
	 * Find all HrCIntype entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCIntype property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCIntype> found by query
	 */
	public List<HrCIntype> findByProperty(String propertyName, Object value);

	public List<HrCIntype> findByInType(Object inType);

	public List<HrCIntype> findByEnterpriseCode(Object enterpriseCode);

	public List<HrCIntype> findByIsUse(Object isUse);

	public List<HrCIntype> findByLastModifiedBy(Object lastModifiedBy);

	public List<HrCIntype> findByInsertby(Object insertby);

	public List<HrCIntype> findByOrderBy(Object orderBy);

	/**
	 * Find all HrCIntype entities.
	 *
	 * @return List<HrCIntype> all HrCIntype entities
	 */
	public List<HrCIntype> findAll();

	/**
	 * 按进厂类别ID，是否使用和企业编码检索
	 *
	 * @param inTypeId,isUse,enterprisecode
	 *
	 * @return Boolean
	 */
	public Boolean findByIdIsuseCode(Long inTypeId,String isUse,String enterpriseCode);

	/**
	 * 查询所有员工进厂类别
	 */
	public List<HrCIntype> queryAllInType(String isUse,String enterpriseCode,int... rowStartIdxAndCount);
	/**
	 * 查找所有进厂类别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllInTypes(String enterpriseCode);
}