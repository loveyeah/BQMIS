package power.ejb.hr.ca;

import java.util.List;
import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrJWorkattendancenewFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJWorkattendancenewFacadeRemote {
	/**
	 * Perform an initial save of a previously unsaved HrJWorkattendancenew
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrJWorkattendancenew entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrJWorkattendancenew entity);

	/**
	 * Delete a persistent HrJWorkattendancenew entity.
	 * 
	 * @param entity
	 *            HrJWorkattendancenew entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrJWorkattendancenew entity);

	/**
	 * Persist a previously saved HrJWorkattendancenew entity and return it or a
	 * copy of it to the sender. A copy of the HrJWorkattendancenew entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrJWorkattendancenew entity to update
	 * @return HrJWorkattendancenew the persisted HrJWorkattendancenew entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrJWorkattendancenew update(HrJWorkattendancenew entity);

	public HrJWorkattendancenew findById(HrJWorkattendancenewId id);

	/**
	 * Find all HrJWorkattendancenew entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrJWorkattendancenew property to query
	 * @param value
	 *            the property value to match
	 * @return List<HrJWorkattendancenew> found by query
	 */
	public List<HrJWorkattendancenew> findByProperty(String propertyName,
			Object value);

	/**
	 * Find all HrJWorkattendancenew entities.
	 * 
	 * @return List<HrJWorkattendancenew> all HrJWorkattendancenew entities
	 */
	public List<HrJWorkattendancenew> findAll();
	
	
	/**
	 * 根据时间、考勤部门查找考勤人员
	 * @param attendanceDate
	 * @param attendanceYear
	 * @param attendanceMonth
	 * @param attendanceDeptId
	 * @param workResetFlag
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject getDetailInfoForRegister(String attendanceDate,
			String attendanceYear, String attendanceMonth,
			String attendanceDeptId, String workResetFlag, String enterpriseCode);
	/**
	 * 判断是否上报
	 * @param attendanceMonth
	 * @param attendanceDeptId
	 * @return
	 */
	public boolean getApprovedForRegister(String attendanceMonth, String attendanceDeptId);
	
	/**
	 * 根据ID查找考勤记录
	 * 
	 * @param id ID
	 * @param enterpriseCode 企业编码
	 * @return 考勤记录
	 */
	public HrJWorkattendancenew findById(HrJWorkattendancenewId id,
			String enterpriseCode);
	
	/**
	 * 年度缺勤汇总统计
	 * add by fyyang 20100706
	 * @param deptId
	 * @param strYear
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findAattendanceListByYear(Long deptId,String strYear,String enterpriseCode,final int... rowStartIdxAndCount);
	
	
	
	/**
	 * 年休查询
	 * @param deptId
	 * @param strYear
	 * @param enterpriseCode
	 * @param chsName
	 * @param rowStartIdxAndCount
	 * @return
	 */
	public PageObject findYearRestList(Long deptId,String strYear,String enterpriseCode,String chsName,final int... rowStartIdxAndCount);
	
}