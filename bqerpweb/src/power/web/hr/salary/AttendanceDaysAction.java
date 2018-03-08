package power.web.hr.salary;

import java.text.ParseException;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCAttendanceDays;
import power.ejb.hr.salary.HrCAttendanceDaysFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class AttendanceDaysAction extends AbstractAction {

	private HrCAttendanceDaysFacadeRemote remote;
	private HrCAttendanceDays attendDays;

	public AttendanceDaysAction() {
		remote = (HrCAttendanceDaysFacadeRemote) factory
				.getFacadeRemote("HrCAttendanceDaysFacade");
	}

	/**
	 * 增加一条月出勤天数记录
	 * 
	 * @throws ParseException
	 */
	public void addAttendanceDays() throws ParseException {
//		String month = request.getParameter("month");
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
//		attendDays.setMonth(formatter.parse(month));
		attendDays.setStartDate(formatter.parse(startDate));
		attendDays.setEndDate(formatter.parse(endDate));
		attendDays.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(attendDays);
		write("{success:true,msg:'增加成功！'}");
	}

	/**
	 * 修改月出勤天数记录
	 * 
	 * @throws ParseException
	 */
	public void updateAttendanceDays() throws ParseException {
		String startDate = request.getParameter("startDate");
		String endDate = request.getParameter("endDate");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
		HrCAttendanceDays model = remote.findById(attendDays
				.getAttendanceDaysId());
		model.setAttendanceDays(attendDays.getAttendanceDays());
		model.setMemo(attendDays.getMemo());
		model.setStartDate(formatter.parse(startDate));
		model.setEndDate(formatter.parse(endDate));

		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}

	/**
	 * 查询所有月出勤天数记录
	 * 
	 * @throws JSONException
	 */
	public void findAttendanceDaysList() throws JSONException {
		String sMonth = request.getParameter("month");

		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findAttendanceDaysList(sMonth, employee
					.getEnterpriseCode(), start, limit);
		} else {
			object = remote.findAttendanceDaysList(sMonth, employee
					.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	/**
	 * 查询某个月的出勤天数是否已经维护
	 * 
	 * @throws JSONException
	 */
	public void findByMonth() throws JSONException {
		String sMonth = request.getParameter("month");
		boolean flag= remote.findByMonth(sMonth);
		String strOutput = "";
		if (flag) {
			strOutput = "{true}";
		} else {
			strOutput = "{false}";
		}
		write(strOutput);
	}

	/**
	 * 删除一条或多条月出勤天数记录
	 */
	public void deleteAttendanceDays() {
		String ids = request.getParameter("ids");
		remote.deleteAttendanceDays(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public HrCAttendanceDays getAttendDays() {
		return attendDays;
	}

	public void setAttendDays(HrCAttendanceDays attendDays) {
		this.attendDays = attendDays;
	}


}
