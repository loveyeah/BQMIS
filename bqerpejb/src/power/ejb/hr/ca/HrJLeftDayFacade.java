package power.ejb.hr.ca;
import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import java.util.List;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

@Stateless
public class HrJLeftDayFacade implements HrJLeftDayFacadeRemote{
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	public PageObject findLeftDayList(String deptId,final int ...rowStartIdxAndCount){
		PageObject pg = new PageObject();
		String sql = 
			"select d.dept_name,d.dept_name,ei.emp_code,ei.chs_name,ld.his_day,ld.left_day\n" +
			"from hr_j_left_day ld,hr_j_emp_info ei,hr_c_dept d\n" + 
			"where ld.emp_id = ei.emp_id and ei.dept_id = d.dept_id and d.dept_id = '" + deptId + "'";
		String sqlCount = "sellect count(*) from (" + sql + ")tt";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}
}
