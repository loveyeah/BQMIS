package power.ejb.hr.ca;

import java.util.List;
import javax.ejb.Remote;

/**
 * Remote interface for HrJAttendanceApproveFacade.
 * 
 * @author MyEclipse Persistence Tools
 */
@Remote
public interface HrJAttendanceApproveFacadeRemote {
	
	public void save(HrJAttendanceApprove entity);

	
	public void delete(HrJAttendanceApprove entity);

	
	public HrJAttendanceApprove update(HrJAttendanceApprove entity);

	public HrJAttendanceApprove findById(Long id);

	/**
	 * 查找月份记录列表
	 * for考勤登记/上报
	 * @param month
	 * @param attendanceDeptId
	 * @return
	 */
	public List<HrJAttendanceApprove> getAttendanceApprove(String month,Long attendanceDeptId);
	
	/**
	 * 查询月份记录审批列表
	 * for 审批页面
	 * @param month
	 * @param entryIds
	 * @param deptId
	 * @return
	 */
	public List<HrJAttendanceApprove> getAttendanceListForApprove(String month,String entryIds,Long deptId);
	
	/**
	 * 考勤登记上报
	 * @param attendanceDeptId
	 * @param workerCode
	 * @param actionId
	 * @param nextRoles
	 * @param remark
	 * @param month
	 */
	public void reportAttendanceDept(Long attendanceDeptId, String workerCode,
			Long actionId, String nextRoles, String remark, String month,String enterpriseCode);
	
//	/**
//	 * 考勤审批部门管理员汇总审批时授权部门过滤 add by sychen 20100729  delete by fyyang 0803
//	 * @param nextRole
//	 * @param deptId
//	 * @return
//	 */
//	public String getnextRolesList(String nextRole, Long deptId);
	
	
	/**
	 * 考勤审批
	 * @param strMonth
	 * @param approveText
	 * @param workerCode
	 * @param actionId
	 * @param nextRoles
	 * @param eventIdentify
	 * @return
	 */
	public String approveAttendance(Long deptId,String strMonth,String approveText,String workerCode,Long actionId,String nextRoles,String eventIdentify,String entryIds);
	
	/**
	 * 考勤事务提醒 
	 * add by fyyang 20100803
	 * @param workCode
	 * @return
	 */
	public String getMsgList(String workCode);
}