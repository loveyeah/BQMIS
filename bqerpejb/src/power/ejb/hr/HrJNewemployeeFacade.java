package power.ejb.hr;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity HrJNewemployee.
 * 
 * @see power.ejb.hr.HrJNewemployee
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJNewemployeeFacade implements HrJNewemployeeFacadeRemote {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public HrJNewemployee save(HrJNewemployee entity) {
		LogUtil.log("saving HrJNewemployee instance", Level.INFO, null);
		try {
			entity.setNewEmpid(bll.getMaxId("hr_j_newemployee", "new_empid"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMult(String ids) {
		String sql = "update hr_j_newemployee a\n" +
			"set a.is_use = 'N'\n" + 
			"where a.new_empid in ("+ids+")";
		bll.exeNativeSQL(sql);
	}

	public HrJNewemployee update(HrJNewemployee entity) {
		LogUtil.log("updating HrJNewemployee instance", Level.INFO, null);
		try {
			HrJNewemployee result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJNewemployee findById(Long id) {
		LogUtil.log("finding HrJNewemployee instance with id: " + id,
				Level.INFO, null);
		try {
			HrJNewemployee instance = entityManager.find(HrJNewemployee.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findNewEmployeeList(String enterpriseCode,String year,String advicenoteNo,String dept,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String sql = "select a.new_empid,\n" +
			"       a.emp_id,\n" + 
			"       i.emp_code,\n" + 
			"       i.chs_name,\n" + 
			"       a.advicenote_no,\n" + 
			"       a.new_deptid,\n" + 
			"       (select d.dept_name\n" + 
			"          from Hr_c_dept d\n" + 
			"         where d.dept_id = a.new_deptid\n" + 
			"           and d.is_use = 'Y'),\n" + //update by sychen 20100901
//			"           and d.is_use = 'U'),\n" + 
			"       a.check_station_grade,\n" + 
			"       a.station_id,\n" + 
			"       (select s.station_name\n" + 
			"          from hr_c_station s\n" + 
			"         where s.station_id = a.station_id\n" + 
			"           and s.is_use = 'Y'),\n" + //update by sychen 20100901
//			"           and s.is_use = 'U'),\n" + 
			"       a.salary_point,\n" + 
			"       to_char(a.mission_date, 'yyyy-MM-dd'),\n" + 
			"       to_char(a.startsalary_date, 'yyyy-MM-dd'),\n" + 
			"       a.memo,\n" + 
			"       (select l.station_level_name from hr_c_station_level l where l.station_level_id = a.check_station_grade and l.is_use = 'Y')\n" +//update by sychen 20100901
//			"       (select l.station_level_name from hr_c_station_level l where l.station_level_id = a.check_station_grade and l.is_use = 'U')\n" +
			"       , to_char(a.register_date, 'yyyy-MM-dd')\n" +  			//add by sychen 20100717
			"       , a.emp_type\n" +  			                                   //add by sychen 20100721
			"       , a.emp_time\n" +  			                                  //add by sychen 20100721
			"       , to_char(a.print_date, 'yyyy-MM-dd')\n" +  			 //add by sychen 20100721
			"  from hr_j_newemployee a, hr_j_emp_info i\n" + 
			" where a.emp_id = i.emp_id\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and i.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and i.enterprise_code = '"+enterpriseCode+"'\n";

		if(year != null && !year.equals(""))
		{
			sql += "  and to_char(a.mission_date,'yyyy') = '"+year+"'\n";
		}
		if(advicenoteNo != null && !advicenoteNo.equals(""))
		{
			//update by sychen 20100716
			
			sql += "  and  decode(a.advicenote_no,'','','人新进（'||to_char(sysdate,'yyyy')||'）第'||a.advicenote_no||'号') LIKE  '%"+advicenoteNo+"%' \n";
			
//			sql += "  and a.advicenote_no like '%" + advicenoteNo + "%'\n";
			//update by sychen 20100716 end
		}
		if(dept != null && !dept.equals(""))
		{
			sql += "  and a.new_deptid in\n" +
				"       (select t.dept_id\n" + 
				"          from hr_c_dept t\n" + 
				"         where t.is_use = 'Y'\n" + //update by sychen 20100901
//				"         where t.is_use = 'U'\n" +
				"         start with t.dept_id = "+dept+"\n" + 
				"        connect by prior t.dept_id = t.pdept_id)";
		}
		String sqlCount = "select count(1) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}

}