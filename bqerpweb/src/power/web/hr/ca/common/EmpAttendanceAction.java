package power.web.hr.ca.common;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ca.HrJAttendancecheck;
import power.ejb.hr.ca.HrJAttendancecheckFacadeRemote;
import power.ejb.hr.ca.HrJAttendancecheckId;
import power.ejb.hr.ca.cacommon.EmpAttendanceManage;
import power.web.comm.AbstractAction;
@SuppressWarnings("serial")
public class EmpAttendanceAction extends AbstractAction
{
	// 员工考勤统计共用接口
	private EmpAttendanceManage remote;
	// 部门考勤审核接口
//	private HrJAttendancecheckFacadeRemote checkRemote;
	public EmpAttendanceAction()
	{
		remote = (EmpAttendanceManage)factory.getFacadeRemote("EmpAttendanceManageImpl");
//		checkRemote = (HrJAttendancecheckFacadeRemote)factory.getFacadeRemote("HrJAttendancecheckFacade");
	}
	
//	/**
//	 * 判断一个部门该月份时候已经审批 
//	 */
//	public void judgeDeptIsApproved()
//	{
//		String deptId = request.getParameter("deptId");
//		String month = request.getParameter("month");
//		if(deptId != null && !deptId.equals("") && month != null && !month.equals(""))
//		{
//			// 考勤部门树和组织部门树中id一样，直接使用组织部门id判断该部门是否已审批
//			HrJAttendancecheckId tempId = new HrJAttendancecheckId(month.substring(0,4),
//					month.substring(5,7),Long.parseLong(deptId));
//			HrJAttendancecheck attendanceCheck = checkRemote.findById(tempId, employee.getEnterpriseCode());
//			if(attendanceCheck != null && attendanceCheck.getCheckedDate2() != null)
//				write("{success:true,msg:'该部门考勤负责人已审核'}");
//			else 
//				write("{success:true,msg:'该部门考勤负责人尚未审核，请先审核该月考勤！'}");
//			
//		}
//	}
	
	/**
	 * 获得人员在某一月份下的考勤记录
	 * @throws JSONException 
	 */
	public void getEmpAttendanceRecordList() throws JSONException
	{
		String empId = request.getParameter("empId");
		String deptId = request.getParameter("deptId");
		String attendanceDeptId = request.getParameter("attendanceDeptId");
		String month = request.getParameter("month");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		PageObject pg = new PageObject();
		if(start != null && limit != null)
			pg = remote.getEmpAttendanceInfo(empId, deptId, attendanceDeptId, month, employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
		else 
			pg =remote.getEmpAttendanceInfo(empId, deptId, attendanceDeptId, month, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
}