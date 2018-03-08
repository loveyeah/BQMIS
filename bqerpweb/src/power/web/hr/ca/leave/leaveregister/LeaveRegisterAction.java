/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.web.hr.ca.leave.leaveregister;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.HrCAttendancedepFacadeRemote;
import power.ejb.hr.ca.HrCAttendancestandard;
import power.ejb.hr.ca.HrCHolidayFacadeRemote;
import power.ejb.hr.ca.HrJVacation;
import power.ejb.hr.ca.HrJVacationFacadeRemote;
import power.ejb.hr.ca.employeeLeaveBean;
import power.web.comm.AbstractAction;
import power.web.hr.ca.leave.empvacationregister.action.EmpVacationRegisterAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

/**
 * 员工销假登记Action
 * 
 * @author liuxin
 * 
 */
public class LeaveRegisterAction extends AbstractAction {
	private static final long serialVersionUID = 1L;
	private HrJVacationFacadeRemote remote;
	/** 考勤标准维护ejb远程对象 */
	protected HrCAttendancedepFacadeRemote hrCAttendremote;
	/** 节假日维护ejb远程对象 */
	protected HrCHolidayFacadeRemote hrCHolidayremote;
	/** 员工Id */
	private String empId;
	/** 部门Id */
	private String deptId;
	// 分页
	private int start;
	private int limit;
	/** 开始时间 */
	private String startTime;
	/** 销假时间 */
	private String clearTime;
	/** 结束时间 */
	private String endTime;
	/** 请假天数 */
	private String leaveDays;
	/** 请假时长 */
	private String leaveLong;
	/** 请假ID */
	private String vacationId;
	/** 上次修改时间 */
	private String lastModifyTime;
	/** 日期形式字符串 */
	private static final String DATE_FORMAT_YYYYMMDD = "yyyy-MM-dd HH:mm";
	private static final String DATE_FORMAT_YYYYMMDD_HHMMSS = "yyyy-MM-dd HH:mm:ss";
	/** 是否包括周末 */
	private String ifWeekend;

	public String getIfWeekend() {
		return ifWeekend;
	}

	public void setIfWeekend(String ifWeekend) {
		this.ifWeekend = ifWeekend;
	}

	public String getVacationId() {
		return vacationId;
	}

	public void setVacationId(String vacationId) {
		this.vacationId = vacationId;
	}

	public String getLeaveDays() {
		return leaveDays;
	}

	public void setLeaveDays(String leaveDays) {
		this.leaveDays = leaveDays;
	}

	public String getLeaveLong() {
		return leaveLong;
	}

	public void setLeaveLong(String leaveLong) {
		this.leaveLong = leaveLong;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	// 构造函数
	public LeaveRegisterAction() {
		remote = (HrJVacationFacadeRemote) factory
				.getFacadeRemote("HrJVacationFacade");
		// 考勤标准维护ejb远程对象
		hrCAttendremote = (HrCAttendancedepFacadeRemote) factory
				.getFacadeRemote("HrCAttendancedepFacade");
		// 节假日维护ejb远程对象
		hrCHolidayremote = (HrCHolidayFacadeRemote) factory
				.getFacadeRemote("HrCHolidayFacade");
	}

	/**
	 * 员工销假登记查询
	 */
	public void getEmpLeave() {
		LogUtil.log("Action:员工销假登记查询开始。", Level.INFO, null);
		PageObject result = new PageObject();
		List<employeeLeaveBean> list = remote.getLeaveInfo(employee
				.getEnterpriseCode(), empId, deptId, start, limit);
		List<employeeLeaveBean> listSize = remote.getLeaveInfo(employee
				.getEnterpriseCode(), empId, deptId);
		String str = null;
		if (list != null && list.size() > 0) {
			result.setList(list);
			result.setTotalCount(new Long(listSize.size()));
			try {
				str = JSONUtil.serialize(result);
			} catch (JSONException e) {
				LogUtil.log("Action:员工销假登记查询失败。", Level.SEVERE, null);
			}
		}
		else str =   "{\"list\":[],\"totalCount\":null}";
		write(str);
		LogUtil.log("Action:员工销假登记查询成功。", Level.INFO, null);
	}

	/**
	 * 销假处理
	 * 
	 * @return
	 */
	public void clearLeave() {
		LogUtil.log("Action:员工销假开始。", Level.INFO, null);
		final String IF_CLEAR = "1";
		try {
			HrJVacation entity = new HrJVacation();
			entity = remote.findById(new Long(vacationId));
			// 请假天数
			String strLday;
			// 请假时长
			String strLlong;

			// 销假时间与结束时间相等，不计算请假天数和时长
			if (endTime.equals(clearTime)) {
				strLday = leaveDays;
				strLlong = leaveLong;
			} else {
				String[] strArray = new String[2];
				EmpVacationRegisterAction calculate = new EmpVacationRegisterAction();
				strArray = calculate.calculateNums(startTime, clearTime, empId,
						ifWeekend, employee.getEnterpriseCode());
				strLlong = strArray[0];
				strLday = strArray[1];
			}
			entity.setVacationDays(new Double(strLday));
			entity.setVacationTime(new Double(strLlong));
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD);
			entity.setClearDate(sdf.parse(clearTime));
			// 是否销假设为'1'
			entity.setIfClear(IF_CLEAR);
			sdf = new SimpleDateFormat(DATE_FORMAT_YYYYMMDD_HHMMSS);
			entity.setLastModifiyDate(sdf.parse(lastModifyTime));
			entity.setLastModifiyBy(employee.getWorkerCode());
			remote.update1(entity);
			write("S");
			LogUtil.log("Action:员工销假成功。", Level.INFO, null);
		} catch (DataChangeException de) {
			write("U");
			LogUtil.log("Action:员工销假失败。", Level.SEVERE, null);
		} catch (Exception e) {
			write("E");
			LogUtil.log("Action:员工销假失败。", Level.SEVERE, null);
		}
	}

	public String getEmpId() {
		return empId;
	}

	public void setEmpId(String empId) {
		this.empId = empId;
	}

	public String getDeptId() {
		return deptId;
	}

	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getClearTime() {
		return clearTime;
	}

	public void setClearTime(String clearTime) {
		this.clearTime = clearTime;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

}
