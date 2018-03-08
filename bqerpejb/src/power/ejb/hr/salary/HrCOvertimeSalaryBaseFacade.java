package power.ejb.hr.salary;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrCOvertimeSalaryBase.
 * 
 * @see power.ejb.hr.salary.HrCOvertimeSalaryBase
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCOvertimeSalaryBaseFacade implements
		HrCOvertimeSalaryBaseFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public void save(HrCOvertimeSalaryBase entity) {
		LogUtil.log("saving HrCOvertimeSalaryBase instance", Level.INFO, null);
		try {
			entity.setOvertimeSalaryBaseId(bll.getMaxId("HR_C_OVERTIME_SALARY_BASE", "overtime_salary_base_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids)
	{
		String sql="update HR_C_OVERTIME_SALARY_BASE t\n" +
			"   set t.is_use = 'N'\n" + 
			" where t.overtime_salary_base_id in ("+ids+")";

       bll.exeNativeSQL(sql);
	}
	
	public HrCOvertimeSalaryBase update(HrCOvertimeSalaryBase entity) {
		LogUtil.log("updating HrCOvertimeSalaryBase instance", Level.INFO,null);
		try {
			HrCOvertimeSalaryBase result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCOvertimeSalaryBase findById(Long id) {
		LogUtil.log("finding HrCOvertimeSalaryBase instance with id: " + id,
				Level.INFO, null);
		try {
			HrCOvertimeSalaryBase instance = entityManager.find(
					HrCOvertimeSalaryBase.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findOverTimeSalaryBaseList(String sDate,String enterpriseCode,final int...rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		String  sql = "select b.overtime_salary_base_id,\n" +
			"               b.overtime_salary_base,\n" + 
			"               to_char(b.effect_start_time, 'yyyy-MM'),\n" + 
			"               to_char(b.effect_end_time, 'yyyy-MM'),\n" + 
			"               b.memo,\n" + 
			"               decode(b.effect_start_time,\n" + 
			"                      (select max(c.effect_start_time)\n" + 
			"                         from HR_C_OVERTIME_SALARY_BASE c\n" + 
			"                        where c.is_use = 'Y'\n" + 
			"                          and c.enterprise_code = '"+enterpriseCode+"'),\n" + 
			"                      '1',\n" + 
			"                      '0') status\n" + 
			"          from HR_C_OVERTIME_SALARY_BASE b\n" + 
			"         where b.is_use = 'Y'\n" + 
			"           and b.enterprise_code = '"+enterpriseCode+"'";

		String sqlCount = "select count(1)\n" +
			"  from HR_C_OVERTIME_SALARY_BASE b\n" + 
			" where b.is_use = 'Y'\n" + 
			"   and b.enterprise_code = '"+enterpriseCode+"'";
		
		String whereStr = "";
		if (sDate != null && sDate.length() > 0) {
			whereStr +=" and to_char(b.effect_start_time,'yyyy-MM') <= '"+sDate+"%'";
			whereStr +=" and to_char(b.effect_end_time,'yyyy-MM') >= '"+sDate+"%'";
		}

		sql += whereStr;
		sqlCount += whereStr;
		
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}
}