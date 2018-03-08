package power.ejb.hr;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCOuttypeFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCOuttypeFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCOuttype entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCOuttype entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCOuttype entity);

	/**
	 * Delete a persistent HrCOuttype entity.
	 *
	 * @param entity
	 *            HrCOuttype entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCOuttype entity);

	/**
	 * Persist a previously saved HrCOuttype entity and return it or a copy of
	 * it to the sender. A copy of the HrCOuttype entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 *
	 * @param entity
	 *            HrCOuttype entity to update
	 * @return HrCOuttype the persisted HrCOuttype entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCOuttype update(HrCOuttype entity);

	public HrCOuttype findById(Long id);

	/**
	 * Find all HrCOuttype entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCOuttype property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCOuttype> found by query
	 */
	public List<HrCOuttype> findByProperty(String propertyName, Object value);

	public List<HrCOuttype> findByOutTypeType(Object outTypeType);

	public List<HrCOuttype> findByEnterpriseCode(Object enterpriseCode);

	public List<HrCOuttype> findByIsUse(Object isUse);

	public List<HrCOuttype> findByLastModifiedBy(Object lastModifiedBy);

	public List<HrCOuttype> findByInsertby(Object insertby);

	public List<HrCOuttype> findByOrderBy(Object orderBy);

	/**
	 * Find all HrCOuttype entities.
	 *
	 * @return List<HrCOuttype> all HrCOuttype entities
	 */
	public List<HrCOuttype> findAll();
	/**
	 * 离职类别维护查询
	 */
	public List<HrCOuttype> getOutTypeList(String isUse , String enterprisCode,int... rowStartIdxAndCount);
	/**
	 * 按Id，企业编码，和是否使用字段查询
	 *
	 */
	public Boolean findByIdIsuseCode(Long Id ,String isUse , String enterprisCode);
	/**
	 * 查找所有离职类别
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllOuttypes(String enterpriseCode);
}