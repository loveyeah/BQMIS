package power.ejb.hr.ca;

import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.DataChangeException;

/**
 * Remote interface for HrCAttendancestandardFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCAttendancestandardFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrCAttendancestandard
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrCAttendancestandard entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCAttendancestandard entity) throws RuntimeException;

	/**
	 * Delete a persistent HrCAttendancestandard entity.
	 * 
	 * @param entity
	 *            HrCAttendancestandard entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCAttendancestandard entity);

	/**
	 * Persist a previously saved HrCAttendancestandard entity and return it or
	 * a copy of it to the sender. A copy of the HrCAttendancestandard entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCAttendancestandard entity to update
	 * @return HrCAttendancestandard the persisted HrCAttendancestandard entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public void update(HrCAttendancestandard entity, String strUpdateTime) throws DataChangeException, SQLException;

	/**
	 * 批量增加修改考勤标准维护信息
	 * @param list
	 * @param enterpriseCode
	 * @throws DataChangeException
	 * @throws SQLException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void saveBat(List<HrCAttendancestandard> list,String enterpriseCode,String flag,String workerCode) throws Exception;

	public HrCAttendancestandard findById(Long id);

	/**
	 * Find all HrCAttendancestandard entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCAttendancestandard property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrCAttendancestandard> found by query
	 */
	public List<HrCAttendancestandard> findByProperty(String propertyName,
			Object value);

	public List<HrCAttendancestandard> findByAttendanceYear(
			Object attendanceYear);

	public List<HrCAttendancestandard> findByAttendanceMonth(
			Object attendanceMonth);

	public List<HrCAttendancestandard> findByAttendanceDeptId(
			Object attendanceDeptId);

	public List<HrCAttendancestandard> findByStandardDay(Object standardDay);

	public List<HrCAttendancestandard> findByAmBegingTime(Object amBegingTime);

	public List<HrCAttendancestandard> findByAmEndTime(Object amEndTime);

	public List<HrCAttendancestandard> findByPmBegingTime(Object pmBegingTime);

	public List<HrCAttendancestandard> findByPmEndTime(Object pmEndTime);

	public List<HrCAttendancestandard> findByStandardTime(Object standardTime);

	public List<HrCAttendancestandard> findByLastModifiyBy(Object lastModifiyBy);

	public List<HrCAttendancestandard> findByIsUse(Object isUse);

	public List<HrCAttendancestandard> findByEnterpriseCode(
			Object enterpriseCode);

	/**
	 * Find all HrCAttendancestandard entities.
	 * 
	 * @return List<HrCAttendancestandard> all HrCAttendancestandard entities
	 */
	public List<HrCAttendancestandard> findAll();
}