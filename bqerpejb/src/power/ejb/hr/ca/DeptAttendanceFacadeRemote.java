/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;

/**
 * 部门考勤登记Remote
 * 
 * @author jincong
 * @version 1.0
 */
@Remote
public interface DeptAttendanceFacadeRemote {

	/**
	 * 查询所有考勤部门
	 * 
	 * @param loginId
	 *            系统登录人
	 * @param enterpriseCode
	 *            企业编码
	 * @return 考勤部门
	 */
	public PageObject getAttendanceDeptForRegister(String loginId,
			String enterpriseCode);

	/**
	 * 查询是否已经审核信息
	 * 
	 * @param attendanceYear
	 *            考勤年份
	 * @param attendanceMonth
	 *            考勤月份
	 * @param attendanceDeptId
	 *            考勤部门
	 * @param enterpriseCode
	 *            企业编码
	 * @return 是否已经审核信息
	 */
	public PageObject getApprovedForRegister(String attendanceYear,
			String attendanceMonth, String attendanceDeptId,
			String enterpriseCode);

	/**
	 * 查询所有假别
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @return 所有假别
	 */
	public PageObject getVacationTypeCommon(String enterpriseCode);
	
	/**
	 * 查询所有基本天数 add by liuyi 20100202
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @return 所有基本天数
	 */
	public PageObject getBasicDaysCommon(String enterpriseCode);

	/**
	 * 查询所有加班类别
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @return 所有加班类别
	 */
	public PageObject getOvertimeTypeCommon(String enterpriseCode);

	/**
	 * 查询所有运行班类别
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @return 所有运行班类别
	 */
	public PageObject getWorkshiftTypeCommon(String enterpriseCode);

	/**
	 * 已审核明细部查询
	 * 
	 * @param attendanceDate
	 *            考勤日期
	 * @param attendanceDeptId
	 *            考勤部门
	 * @param enterpriseCode
	 *            企业编码
	 * @return 明细部信息
	 */
	public PageObject getApprovedDetailInfoForRegister(String attendanceDate,
			String attendanceDeptId, String enterpriseCode);

	/**
	 * 未审核明细部查询
	 * 
	 * @param attendanceDate
	 *            考勤日期
	 * @param attendanceYear
	 *            考勤年份
	 * @param attendanceMonth
	 *            考勤月份
	 * @param attendanceDeptId
	 *            考勤部门
	 * @param workResetFlag
	 *            上班休息标识
	 * @param enterpriseCode
	 *            企业编码
	 * @return 未审核明细部信息
	 */
	public PageObject getDetailInfoForRegister(String attendanceDate,
			String attendanceYear, String attendanceMonth,
			String attendanceDeptId, String workResetFlag, String enterpriseCode);
	
	/**
	 * 查询符合条件的人员个数
	 * 
	 * @param attendanceDate 考勤日期
	 * @param attendanceDeptId 考勤部门
	 * @param enterpriseCode 企业编码
	 * @return 个数
	 */
	public int getEmpCountForRegister(String attendanceDate,
			String attendanceDeptId, String enterpriseCode);
	
	/**
	 * 查询节假日信息
	 * 
	 * @param attendanceDate 考勤日期
	 * @param enterpriseCode 企业编码
	 * @return 节假日信息
	 */
	public PageObject getHolidayForRegister(String attendanceDate,
			String enterpriseCode);
}
