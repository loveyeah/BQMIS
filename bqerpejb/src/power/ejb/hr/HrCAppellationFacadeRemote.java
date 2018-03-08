package power.ejb.hr;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCAppellationFacade.
 *
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCAppellationFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCAppellation entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 *
	 * @param entity
	 *            HrCAppellation entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCAppellation entity);

	/**
	 * Delete a persistent HrCAppellation entity.
	 *
	 * @param entity
	 *            HrCAppellation entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCAppellation entity);

	/**
	 * Persist a previously saved HrCAppellation entity and return it or a copy
	 * of it to the sender. A copy of the HrCAppellation entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 *
	 * @param entity
	 *            HrCAppellation entity to update
	 * @return HrCAppellation the persisted HrCAppellation entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCAppellation update(HrCAppellation entity);

	public HrCAppellation findById(Long id);

	/**
	 * Find all HrCAppellation entities with a specific property value.
	 *
	 * @param propertyName
	 *            the name of the HrCAppellation property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCAppellation> found by query
	 */
	public List<HrCAppellation> findByProperty(String propertyName, Object value);

	public List<HrCAppellation> findByCallsName(Object callsName);

	public List<HrCAppellation> findByRetrieveCode(Object retrieveCode);

	public List<HrCAppellation> findByEnterpriseCode(Object enterpriseCode);

	public List<HrCAppellation> findByIsUse(Object isUse);

	public List<HrCAppellation> findByLastModifiedBy(Object lastModifiedBy);

	public List<HrCAppellation> findByInsertby(Object insertby);

	public List<HrCAppellation> findByOrderBy(Object orderBy);

	/**
	 * Find all HrCAppellation entities.
	 *
	 * @return List<HrCAppellation> all HrCAppellation entities
	 */
	public List<HrCAppellation> findAll();
	/**
	 * 查找所有称谓
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAllAppellations(String enterpriseCode);
	
	
	/** **********员工档案 基础表维护专用*开始**************** */
	/**
	 * 基础表维护（员工档案）查询
	 * 
	 * @param tableName
	 *            查询表的名字
	 * @param argEnterpriseCode
	 *            企业编码
	 * @return 查询结果
	 */
	@SuppressWarnings("unchecked")
	public PageObject getRecordList(String tableName, String argEnterpriseCode,
			final int... rowStartIdxAndCount) throws SQLException;
	
	/**
	 * add by liuyi 20100611 
	 * 通过名称获得id
	 * @param name
	 * @param enterpriseCode
	 * @return
	 */
	Long getCallsCodeIdByName(String name,String enterpriseCode);
}