package power.ejb.hr.ca;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCHolidayFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCHolidayFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCHoliday entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCHoliday entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCHoliday entity);

	/**
	 * Delete a persistent HrCHoliday entity.
	 * 
	 * @param entity
	 *            HrCHoliday entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCHoliday entity);

	/**
	 * Persist a previously saved HrCHoliday entity and return it or a copy of
	 * it to the sender. A copy of the HrCHoliday entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCHoliday entity to update
	 * @return HrCHoliday the persisted HrCHoliday entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCHoliday update(HrCHoliday entity);

	public HrCHoliday findById(Long id);

	/**
	 * Find all HrCHoliday entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCHoliday property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCHoliday> found by query
	 */
	public List<HrCHoliday> findByProperty(String propertyName, Object value);

	/**
	 * Find all HrCHoliday entities.
	 * 
	 * @return List<HrCHoliday> all HrCHoliday entities
	 */
	public List<HrCHoliday> findAll();

	/**
	 * 节假日天数获取
	 * 
	 * @param argStartDate
	 *            开始日期
	 * @param argEndDate
	 *            结束日期
	 * @param strEnterpriseCode
	 *            企业代码
	 * @return 节假日天数
	 * @throws SQLException
	 */
	public long getHolidayDays(String argStartDate, String argEndDate,
			String strEnterpriseCode) throws SQLException;

	/**
	 * 非节假日，周末上班天数获取
	 * 
	 * @param argStartDate
	 *            开始日期
	 * @param argEndDate
	 *            结束日期
	 * @param strEnterpriseCode
	 *            企业代码
	 * @return 节假日天数
	 * @throws SQLException
	 */
	public long getHolidayWeekendDays(String argStartDate, String argEndDate,
			String strEnterpriseCode) throws SQLException;

	/**
	 * 查询节假日信息
	 * 
	 * @param strEnterpriseCode
	 *            企业代码
	 * @return PageObject 节假日信息
	 * @throws SQLException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public PageObject findHoliday(String strEnterpriseCode)
			throws SQLException, ParseException;

	/**
	 * 查询节假日信息
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param dateType
	 *            节假日类别
	 * @param year
	 *            年份
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 * @throws SQLException
	 * @throws ParseException
	 */
	public PageObject getHolidayDateList(String enterpriseCode,
			String dateType, String year, final int... rowStartIdxAndCount)
			throws SQLException, ParseException;
	/**
	 * 时间重复check
	 * 
	 * @param entity
	 *            节假日实体
	 */
	public PageObject isDateExist(HrCHoliday entity);
}