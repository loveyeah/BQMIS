/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.hr.ca;
import java.sql.SQLException;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import power.ear.comm.ejb.PageObject;
import bsh.ParseException;

/**
 * 考勤标准维护
 * AttendancestandardFacadeRemote interface.
 * 
 * @author chenshoujiang
 */
@Remote
public interface AttendanceStandardFacadeRemote {


	/**
	 * 查询考勤部门维护信息
	 * @param id
	 * @param workcode
	 * @param enterpriseCode
	 * @return
	 * @throws SQLException
	 */
	@TransactionAttribute(TransactionAttributeType.REQUIRED)
	public void moveDeptData(String workcode, String enterpriseCode,String depType)throws SQLException;
	
	/**
	 * 通过考勤年份和考勤部门id查询信息
	 * @param attendanceYear
	 * @param attendanceDeptId
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 * @throws SQLException
	 */
	@SuppressWarnings("unchecked")
	public PageObject getAttendanceStandard(String attendanceYear,
			String attendanceDeptId, String attendanceDeptName,String isRoot,
			String enterpriseCode) throws SQLException, ParseException;
}
