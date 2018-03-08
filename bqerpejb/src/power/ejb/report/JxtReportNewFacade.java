package power.ejb.report;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.monthaward.monthAwardForm;
import power.ejb.report.form.JxtReportNewEmpForm;

@Stateless
public class JxtReportNewFacade implements JxtReportNewEmpFacadeRemote {

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public List<JxtReportNewEmpForm> getNewEmpInfo(String newEmpids) {
		List<JxtReportNewEmpForm> result = null;

		String sql = "select   i.chs_name,\n"
				+ " (select s.station_name\n"
				+ "          from hr_c_station s\n"
				+ "         where s.station_id = a.station_id\n"
				+ "           and s.is_use = 'U') as station_name,\n" //update by sychen 20100902
//				+ "           and s.is_use = 'Y') as station_name,\n" 
				+ "                (select l.station_level_name\n"
				+ "          from hr_c_station_level l\n"
				+ "         where l.station_level_id = a.check_station_grade\n"
				+ "           and l.is_use = 'Y') as station_level_name,\n" //update by sychen 20100902
//				+ "           and l.is_use = 'U') as station_level_name,\n"
				+ "             a.salary_point,\n" 
				+ "         (select d.dept_name\n"
				+ "          from Hr_c_dept d\n"
				+ "         where d.dept_id = a.new_deptid\n"
				+ "           and d.is_use = 'Y') as dept_name,\n" //update by sychen 20100902
//				+ "           and d.is_use = 'U') as dept_name,\n" 
				+ "       to_char(a.mission_date,'yyyy') as mission_year,\n"
				+ "       to_char(a.mission_date,'mm') as mission_month,\n"
				+ "       to_char(a.mission_date,'dd') as mission_day,\n"
				+ "       to_char(a.startsalary_date,'yyyy') as startsalary_year,\n"
				+ "       to_char(a.startsalary_date,'mm') as startsalary_month,\n"
				+ "       to_char(a.startsalary_date,'dd') as startsalary_day,\n"
				+ "       a.memo\n"
				+ "  from hr_j_newemployee a, hr_j_emp_info i\n"
				+ " where a.emp_id = i.emp_id\n" + "   and a.is_use = 'Y'\n"
				+ "   and i.is_use = 'Y'\n"
				+ "   and a.enterprise_code = 'hfdc'\n"
				+ "   and i.enterprise_code = 'hfdc'\n"
				+ "   and  instr('"+newEmpids+"',','||a.new_empid||',')<>0";

		List<Object[]> list = bll.queryByNativeSQL(sql);
		if (list != null) {
			result = new ArrayList<JxtReportNewEmpForm>();
			for (Object[] rec : list) {
				JxtReportNewEmpForm f = new JxtReportNewEmpForm();
				if (rec[0] != null)
					f.setChsName(rec[0].toString());
				if (rec[1] != null)
					f.setStationName(rec[1].toString());
				if (rec[2] != null)
					f.setStaLevelName(rec[2].toString());
				if (rec[3] != null)
					f.setSalaryPoint(rec[3].toString());
				if (rec[4] != null)
					f.setDeptName(rec[4].toString());
				if (rec[5] != null)
					f.setMissonYear(rec[5].toString());
				if (rec[6] != null)
					f.setMissonMonth(rec[6].toString());
				if (rec[7] != null)
					f.setMissonDay(rec[7].toString());

				if (rec[8] != null)
					f.setStarsalaryYear(rec[8].toString());
				if (rec[9] != null)
					f.setStarsalaryMonth(rec[9].toString());
				if (rec[10] != null)
					f.setStarsalaryDay(rec[10].toString());
				if (rec[11] != null)
					f.setMemo(rec[11].toString());
				result.add(f);
			
			}
		}
		return result;
	}
	
	public List<Object[]>  linZhiOrder(String empids)
	{
		String sql= "select B.CHS_NAME as chsName,d.station_name,\n" +
			"       c.dept_name dept_name,\n" +
			"       to_char(A.DIMISSION_DATE,'yyyy') as DIMISSION_year,\n" + 
			"       to_char(A.DIMISSION_DATE,'mm') as DIMISSION_month,\n" + 
			"       to_char(A.DIMISSION_DATE,'dd') as DIMISSION_day,\n" + 
			"       to_char(A.stopsalary_date,'yyyy') as stopsalary_year,\n" + 
			"       to_char(A.stopsalary_date,'mm') as stopsalary_month,\n" + 
			"       to_char(A.stopsalary_date,'dd') as stopsalary_day,\n" +
			"       A.MEMO\n" + 
			"  from HR_J_DIMISSION A, HR_J_EMP_INFO B, HR_C_DEPT C,HR_C_STATION d\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.emp_id = b.emp_id\n" + 
			"   and b.dept_id = c.dept_id\n" + 
			"   and b.station_id = d.station_id(+)\n" + 
			"   and instr('"+empids+"', ',' || a.EMP_ID || ',') <> 0";
//		System.out.println("the sql"+sql);
		return bll.queryByNativeSQL(sql);
		
	}
	public List<Object[]>  deptInMoveOrder(String stationRemoveIds)
	{ 
		String sql= 
			"select\n" +
			"       B.CHS_NAME as chsName,\n" + 
			"       E.STATION_NAME as bfStationName,\n" + 
			"       i.station_level_name,\n" + 
			"       a.old_salary_point,\n" + 
			"       c.dept_name dept_name,\n" + 
			"       f.station_name new_station_name,\n" + 
			"       j.station_level_name new_station_level_name,\n" + 
			"       a.new_salary_point,\n" + 
			"       d.dept_name new_dept_name ,\n" + 
			"       to_char(a.remove_date,'yyyy') as remove_year,\n" + 
			"       to_char(a.remove_date,'mm') as remove_month,\n" + 
			"       to_char(a.remove_date,'dd') as remove_day,\n" + 
			"       a.memo\n" + 
			" from HR_J_STATIONREMOVE A,HR_J_EMP_INFO B,HR_C_DEPT C,  HR_C_DEPT D ,    HR_C_STATION E,HR_C_STATION F,  HR_C_STATION_LEVEL I,HR_C_STATION_LEVEL J\n" + 
			" where a.is_use='Y'\n" + 
			" and a.emp_id=b.emp_id(+)\n" + 
			" and b.is_use='Y'\n" + 
			" and a.old_station_id=e.station_id(+)\n" + 
			" and a.new_station_id=f.station_id(+)\n" + 
			" and a.old_dep_id=c.dept_id(+)\n" + 
			" and a.new_dep_id=d.dept_id(+)\n" + 
			" and a.old_station_grade=i.station_level_id(+)\n" + 
			" and a.new_station_grade=j.station_level_id(+)\n" + 
			"and  instr('"+stationRemoveIds+"',','||a.stationremoveid||',')<>0";
		return bll.queryByNativeSQL(sql);
		
	}
	public List<Object[]>  deptBetMoveOrder(String stationRemoveIds) {
		
		
		String sql=
			"select\n" +
			"       B.CHS_NAME as chsName,\n" + 
			"       E.STATION_NAME as bfStationName,\n" + 
			"       i.station_level_name,\n" + 
			"       a.old_salary_point,\n" + 
			"       getdeptname(getfirstlevelbyid(a.old_dep_id)) dept_name,\n" + 
			"       f.station_name new_station_name,\n" + 
			"       j.station_level_name new_station_level_name,\n" + 
			"       a.new_salary_point,\n" + 
			"       getdeptname(getfirstlevelbyid(a.new_dep_id)) new_dept_name ,\n" + 
			"       to_char(a.remove_date,'yyyy') as remove_year,\n" + 
			"       to_char(a.remove_date,'mm') as remove_month,\n" + 
			"       to_char(a.remove_date,'dd') as remove_day,\n" + 
			"       a.memo\n" + 
			" from HR_J_STATIONREMOVE A,HR_J_EMP_INFO B,HR_C_DEPT C,  HR_C_DEPT D ,    HR_C_STATION E,HR_C_STATION F,  HR_C_STATION_LEVEL I,HR_C_STATION_LEVEL J\n" + 
			" where a.is_use='Y'\n" + 
			" and a.emp_id=b.emp_id(+)\n" + 
			" and b.is_use='Y'\n" + 
			" and a.old_station_id=e.station_id(+)\n" + 
			" and a.new_station_id=f.station_id(+)\n" + 
			" and a.old_dep_id=c.dept_id(+)\n" + 
			" and a.new_dep_id=d.dept_id(+)\n" + 
			" and a.old_station_grade=i.station_level_id(+)\n" + 
			" and a.new_station_grade=j.station_level_id(+)\n" + 
			"\n" + 
			"\n" + 
			"and  instr('"+stationRemoveIds+"',','||a.stationremoveid||',')<>0\n" + 
			"";

		return bll.queryByNativeSQL(sql);
	}
	
	public List<Object[]>  neiBuJieDiaoOrder(String stationRemoveIds)
	{
		
		
		String sql=
			"select\n" +
			"       B.CHS_NAME as chsName,\n" + 
			"       E.STATION_NAME as bfStationName,\n" + 
			"       i.station_level_name,\n" + 
			"       a.old_salary_point,\n" + 
			"       c.dept_name dept_name,\n" + 
			"       f.station_name new_station_name,\n" + 
			"       j.station_level_name new_station_level_name,\n" + 
			"       a.new_salary_point,\n" + 
			"       d.dept_name new_dept_name ,\n" + 
			"       to_char(a.remove_date,'yyyy') as remove_year,\n" + 
			"       to_char(a.remove_date,'mm') as remove_month,\n" + 
			"       to_char(a.remove_date,'dd') as remove_day,\n" + 
			"       a.memo\n" + 
			" from HR_J_STATIONREMOVE A,HR_J_EMP_INFO B,HR_C_DEPT C,  HR_C_DEPT D ,    HR_C_STATION E,HR_C_STATION F,  HR_C_STATION_LEVEL I,HR_C_STATION_LEVEL J\n" + 
			" where a.is_use='Y'\n" + 
			" and a.emp_id=b.emp_id(+)\n" + 
			" and b.is_use='Y'\n" + 
			" and a.old_station_id=e.station_id(+)\n" + 
			" and a.new_station_id=f.station_id(+)\n" + 
			" and a.old_dep_id=c.dept_id(+)\n" + 
			" and a.new_dep_id=d.dept_id(+)\n" + 
			" and a.old_station_grade=i.station_level_id(+)\n" + 
			" and a.new_station_grade=j.station_level_id(+)\n" + 
			"\n" + 
			"  and  instr('',','||a.stationremoveid||',')<>0\n" + 
			"";

		return bll.queryByNativeSQL(sql);
		
	}
	
	
	

}