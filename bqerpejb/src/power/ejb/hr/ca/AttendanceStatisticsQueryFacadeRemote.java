/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.hr.ca;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Remote;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;

import bsh.ParseException;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.TreeNode;

/**
 * 考勤统计查询
 * AttendanceStatisticsQueryFacadeRemote interface.
 * 
 * @author fangjihu
 */
@Remote
public interface AttendanceStatisticsQueryFacadeRemote {
	/**
	 * 部门出勤统计查询
	 * @param deptId 部门id
	 * @param year 考勤年份
	 * @param month 考勤月份
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	public PageObject findDeptOndutyStatisticsQueryInfo(String deptId,String year,String month,String enterpriseCode);
	
	/**
	 * 部门请假单查询
	 * @param deptId 部门id
	 * @param yearMonth 选择年月
	 * @param signState 签字状态
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	public PageObject findDeptleaveStatisticsQueryInfo(String deptId,String yearMonth,String signState,String enterpriseCode);
	
	/**
	 * 请假统计查询
	 * @param year 考勤年份
	 * @param month 考勤月份
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	public PageObject findLeaveStatisticsQueryInfo(String year,String month,String enterpriseCode);
	
	/**
	 * 假别编码表查询
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject findVacationTypeId(String enterpriseCode);
	
	/**
	 * 加班统计查询
	 * @param year 考勤年份
	 * @param month 考勤月份
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	public PageObject findWorkOvertimeStatisticsQueryInfo(String year,String month,String enterpriseCode);
	
	/**
	 * 加班类别维护表查询
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject findOvertimeTypeId(String enterpriseCode);
	
	
	/**
	 * 运行班统计查询
	 * @param year 考勤年份
	 * @param month 考勤月份
	 * @param enterpriseCode 企业编码
	 * @return
	 */
	public PageObject findWorkshiftStatisticsQueryInfo(String year,String month,String enterpriseCode);
	
	/**
	 * 运行班类别维护表查询
	 * @param enterpriseCode
	 * @return
	 */
	public PageObject findWorkshiftTypeId(String enterpriseCode);
	
}
