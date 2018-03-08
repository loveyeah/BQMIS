/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import java.sql.SQLException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCStatitemFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCStatitemFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCStatitem entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCStatitem entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCStatitem entity);

	/**
	 * Delete a persistent HrCStatitem entity.
	 * 
	 * @param entity
	 *            HrCStatitem entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCStatitem entity);

	/**
	 * Persist a previously saved HrCStatitem entity and return it or a copy of
	 * it to the sender. A copy of the HrCStatitem entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCStatitem entity to update
	 * @return HrCStatitem the persisted HrCStatitem entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCStatitem update(HrCStatitem entity);

	public HrCStatitem findById(Long id);

	/**
	 * Find all HrCStatitem entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCStatitem property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCStatitem> found by query
	 */
	public List<HrCStatitem> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrCStatitem entities.
	 * 
	 * @return List<HrCStatitem> all HrCStatitem entities
	 */
	public List<HrCStatitem> findAll();

	/**
	 * 获得考勤合计项信息
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 * @throws SQLException
	 */
	public PageObject getStatItemList(String enterpriseCode,
			final int... rowStartIdxAndCount);

	/**
	 * 通过合计项类型code获得合计项名称list
	 * 
	 * @param enterpriseCode
	 * @param typeCode
	 * @return PageObject
	 */
	public PageObject getStatNameList(String enterpriseCode, String typeCode);
}