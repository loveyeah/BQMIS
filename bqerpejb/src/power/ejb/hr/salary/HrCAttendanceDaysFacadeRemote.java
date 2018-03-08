package power.ejb.hr.salary;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * Remote interface for HrCAttendanceDaysFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrCAttendanceDaysFacadeRemote {

	/**
	 * 保存一条出勤天数记录
	 * 
	 * @param entity
	 * @return HrCAttendanceDays
	 */
	public HrCAttendanceDays save(HrCAttendanceDays entity);

	/**
	 * 删除出勤天数记录
	 * 
	 * @param attendanceDaysId String
	 */
	public void deleteAttendanceDays(String attendanceDaysId);

	/**
	 * 更新一条出勤天数记录
	 * 
	 * @param entity
	 * @return HrCAttendanceDays
	 */
	public HrCAttendanceDays update(HrCAttendanceDays entity);

	/**
	 * 根据主key查询
	 * 
	 * @param id Long 出勤天数ID
	 * @return HrCAttendanceDays
	 */
	public HrCAttendanceDays findById(Long id);

	/**
	 * 查询该月的出勤天数是否已经维护
	 * 
	 * @param month String
	 * @return true：已经维护、false：没有维护
	 */
	public boolean findByMonth(String month);

	/**
	 * 根据条件查询出勤天数List
	 * 
	 * @param sMonth String 月份
	 * @param enterpriseCode 
	 * @param rowStartIdxAndCount
	 * @return PageObject
	 */
	public PageObject findAttendanceDaysList(String sMonth, String enterpriseCode, int... rowStartIdxAndCount);
}