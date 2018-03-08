package power.ejb.hr.ca.cacommon;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
/**
 * 
 * @author liuyi 20100202
 *
 */
@Stateless
public class EmpAttendanceManageImpl implements EmpAttendanceManage
{
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	

	public PageObject getEmpAttendanceInfo(String empId, String deptId,
			String attendanceDeptId, String month, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "";
		String sqlCount = "";
		boolean connect = true;// true:关联 false:不关联
		// 判断考勤部门的考勤类别，若类别是考勤部门时，不需要关联考勤类别表
		String judgeSql = 
			"select distinct a.attendance_dept_id,b.attend_dep_type,b.attendance_dept_name\n" +
			" from HR_J_WORKATTENDANCE a,HR_C_ATTENDANCEDEP b\n" + 
			" where a.attendance_dept_id=b.attendance_dept_id\n" + 
			" and a.is_use='Y'\n" + 
			" and a.enterprise_code='"+enterpriseCode+"'\n";
		if(empId != null && !empId.equals(""))
			judgeSql += " and a.emp_id ="+empId+" \n";
		if(deptId != null && !deptId.equals(""))
			judgeSql +=" and a.dept_id ="+deptId+" \n";
		if(attendanceDeptId != null && !attendanceDeptId.equals(""))
			judgeSql += " and a.attendance_dept_id = "+attendanceDeptId+" \n";
		if(month != null && !month.equals(""))
			judgeSql += " and to_char(a.attendance_date,'yyyy-mm')='"+month+"' \n";
		List judgeList = bll.queryByNativeSQL(judgeSql);
		if(judgeList != null && judgeList.size() > 0)
		{
			Object[] judgeObj = (Object[])judgeList.get(0);
			if(judgeObj[1] != null && judgeObj[1].toString().equals("1"))
				connect = false;
			else
				connect = true;
		}

		sql = 
			"select a.emp_id,\n" +
			" b.chs_name,\n" + 
			" a.dept_id,\n" + 
			" c.dept_name,\n" + 
			" a.attendance_dept_id,\n" + 
			" d.attendance_dept_name,\n" + 
			" nvl(sum(f.base_days),0)  as sickleavecount,\n" + 
			" nvl(sum(g.base_days),0)  as eventcount,\n" + 
			"  nvl(sum(h.base_days),0)  as absentcount,\n" + 
			" nvl(sum((select t.base_days from hr_c_day t where a.overtime_time_id=t.id and a.overtime_type_id=1)),0)  as addcount,\n" + 
			" nvl(sum((select t.base_days from hr_c_day t where a.overtime_time_id=t.id and a.overtime_type_id=2)),0) as weekendcount,\n" + 
			" nvl(sum((select t.base_days from hr_c_day t where a.overtime_time_id=t.id and a.overtime_type_id=3)),0)  as holidaycount, \n" +
			" nvl(sum(tt.base_days),0)  as othertimecount \n" +
			" from HR_J_WORKATTENDANCE a,Hr_j_Emp_Info b,hr_c_dept c,hr_c_attendancedep d,hr_c_day f,hr_c_day g,hr_c_day h,hr_c_day tt \n";
			if(connect)
				sql +=",HR_J_ATTENDANCECHECK i \n"; 
			sql += " where a.emp_id=b.emp_id\n" + 
			" and a.dept_id=c.dept_id\n" + 
			" and a.attendance_dept_id=d.attendance_dept_id\n" + 
			" and a.sick_leave_time_id=f.id(+)\n" + 
			" and a.event_time_id=g.id(+)\n" + 
			" and a.absent_time_id=h.id(+)\n" +
			" and a.OTHER_TIME_ID=tt.id(+)\n" + 
			" and a.is_use='Y'\n" + 
			" and a.enterprise_code='"+enterpriseCode+"'\n"; 

			if (connect) {
			sql += " and a.attendance_dept_id=i.attendance_dep \n"
					+ " and i.checked_date2 is not null \n";
		}
			
		if(empId != null && !empId.equals(""))
			sql += "  and a.emp_id=" + empId + " \n";
		if(deptId != null && !deptId.equals(""))
			sql += " and a.dept_id="+deptId+" \n";
		if(attendanceDeptId != null && !attendanceDeptId.equals(""))
			sql +=" and a.attendance_dept_id="+attendanceDeptId+" \n"; 
		if (month != null && !month.equals("")) {
			sql += " and to_char(a.attendance_date,'yyyy-mm')='" + month
					+ "'\n";
			if (connect) {
				sql += " and i.attendance_year='" + month.substring(0, 4)
						+ "'\n" + " and i.attendance_month='"
						+ month.substring(5, 7) + "' \n";
			}
		}
		
		sql += 
			" group by a.emp_id,\n" + 
			" b.chs_name,\n" + 
			" a.dept_id,\n" + 
			" c.dept_name,\n" + 
			" a.attendance_dept_id,\n" + 
			" d.attendance_dept_name\n";
		sqlCount = "select count(*) from (" + sql + ") \n";
		sql += " order by a.emp_id";
		
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		List<EmpAttendanceForm> arrlist = new ArrayList<EmpAttendanceForm>();
		if(list != null && list.size() > 0)
		{
			Iterator it = list.iterator();
			while(it.hasNext())
			{
				EmpAttendanceForm form = new EmpAttendanceForm();
				Object[] data = (Object[])it.next();
				if(data[0] != null)
					form.setEmpId(data[0].toString());
				if(data[1] != null)
					form.setEmpName(data[1].toString());
				if(data[2] != null)
					form.setDeptId(data[2].toString());
				if(data[3] != null)
					form.setDeptName(data[3].toString());
				if(data[4] != null)
					form.setAttendanceDeptId(data[4].toString());
				if(data[5] != null)
					form.setAttendanceDeptName(data[5].toString());
				if(data[6] != null)
					form.setSickLeavlCount(data[6].toString());
				if(data[7] != null)
					form.setEventCount(data[7].toString());
				if(data[8] != null)
					form.setAbsentCount(data[8].toString());
				if(data[9] != null)
					form.setEveningAddCount(data[9].toString());
				if(data[10] != null)
					form.setWeekendAddCount(data[10].toString());
				if(data[11] != null)
					form.setHolidayAddCount(data[11].toString());
				if(data[12] != null)
					form.setOtherTimeCount(data[12].toString());
				
				form.setMonth(month);
				arrlist.add(form);
			}
		}
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(arrlist);
		pg.setTotalCount(totalCount);
		return pg;
	}

	
}