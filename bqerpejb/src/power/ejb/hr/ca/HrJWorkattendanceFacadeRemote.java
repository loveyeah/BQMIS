package power.ejb.hr.ca;

import java.util.List;

import javax.ejb.Remote;

import power.ear.comm.DataChangeException;

/**
 * Remote interface for HrJWorkattendanceFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJWorkattendanceFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJWorkattendance entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrJWorkattendance entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJWorkattendance entity);

	/**
	 * Delete a persistent HrJWorkattendance entity.
	 * 
	 * @param entity
	 *            HrJWorkattendance entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJWorkattendance entity);

	/**
	 * 更新考勤记录
	 * 
	 * @param entity
	 *            需要被更新的bean的内容
	 * @throws RuntimeException
	 *             更新失败
	 * @throws DataChangeException
	 *             排他失败
	 */
	public void update(HrJWorkattendance entity) throws DataChangeException;

	/**
	 * 根据ID查找考勤记录
	 * 
	 * @param id ID
	 * @param enterpriseCode 企业编码
	 * @return 考勤记录
	 */
	public HrJWorkattendance findById(HrJWorkattendanceId id,
			String enterpriseCode);

	/**
	 * Find all HrJWorkattendance entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJWorkattendance property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJWorkattendance> found by query
	 */
	public List<HrJWorkattendance> findByProperty(String propertyName,
			Object value);

	public List<HrJWorkattendance> findByDeptId(Object deptId);

	public List<HrJWorkattendance> findByAttendanceDeptId(
			Object attendanceDeptId);

	public List<HrJWorkattendance> findByAmBegingTime(Object amBegingTime);

	public List<HrJWorkattendance> findByAmEndTime(Object amEndTime);

	public List<HrJWorkattendance> findByPmBegingTime(Object pmBegingTime);

	public List<HrJWorkattendance> findByPmEndTime(Object pmEndTime);

	public List<HrJWorkattendance> findByWorkShiftId(Object workShiftId);

	public List<HrJWorkattendance> findByVacationTypeId(Object vacationTypeId);

	public List<HrJWorkattendance> findByOvertimeTypeId(Object overtimeTypeId);

	public List<HrJWorkattendance> findByWork(Object work);

	public List<HrJWorkattendance> findByRestType(Object restType);

	public List<HrJWorkattendance> findByAbsentWork(Object absentWork);

	public List<HrJWorkattendance> findByLateWork(Object lateWork);

	public List<HrJWorkattendance> findByLeaveEarly(Object leaveEarly);

	public List<HrJWorkattendance> findByOutWork(Object outWork);

	public List<HrJWorkattendance> findByEvectionType(Object evectionType);

	public List<HrJWorkattendance> findByMemo(Object memo);

	public List<HrJWorkattendance> findByInsertby(Object insertby);

	public List<HrJWorkattendance> findByLastModifiyBy(Object lastModifiyBy);

	public List<HrJWorkattendance> findByIsUse(Object isUse);

	public List<HrJWorkattendance> findByEnterpriseCode(Object enterpriseCode);

	/**
	 * Find all HrJWorkattendance entities.
	 * 
	 * @return List<HrJWorkattendance> all HrJWorkattendance entities
	 */
	public List<HrJWorkattendance> findAll();
}