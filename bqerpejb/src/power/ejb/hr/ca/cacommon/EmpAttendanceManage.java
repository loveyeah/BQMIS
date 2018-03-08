package power.ejb.hr.ca.cacommon;

import javax.ejb.Remote;

import power.ear.comm.ejb.PageObject;
/**
 * @author liuyi 20100202
 *
 */
@Remote
public interface EmpAttendanceManage
{
	
	/**
	 * 获得人员在某一月份下的考勤记录
	 * @param empId 人员Id
	 * @param deptId 部门Id
	 * @param attendanceDeptId 考勤Id
	 * @param month 月份
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	
	PageObject getEmpAttendanceInfo(String empId,String deptId,String attendanceDeptId,String month,
			String enterpriseCode,int... rowStartIdxAndCount);
}