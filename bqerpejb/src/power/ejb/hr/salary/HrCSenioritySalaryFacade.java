package power.ejb.hr.salary;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.salary.form.SenioritySalaryForm;

/**
 * Facade for entity HrCSenioritySalary.
 * 
 * @see power.ejb.hr.salary.HrCSenioritySalary
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCSenioritySalaryFacade implements HrCSenioritySalaryFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public HrCSenioritySalary save(HrCSenioritySalary entity) {
		LogUtil.log("saving HrCSenioritySalary instance", Level.INFO, null);
		try {
			entity.setSenioritySalaryId(bll.getMaxId("HR_C_SENIORITY_SALARY", "seniority_salary_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids)
	{
		String sql="update HR_C_SENIORITY_SALARY t\n" +
			"   set t.is_use = 'N'\n" + 
			" where t.seniority_salary_id in ("+ids+")";

       bll.exeNativeSQL(sql);
	}

	public HrCSenioritySalary update(HrCSenioritySalary entity) {
		LogUtil.log("updating HrCSenioritySalary instance", Level.INFO, null);
		try {
			HrCSenioritySalary result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCSenioritySalary findById(Long id) {
		LogUtil.log("finding HrCSenioritySalary instance with id: " + id,
				Level.INFO, null);
		try {
			HrCSenioritySalary instance = entityManager.find(
					HrCSenioritySalary.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findSenioritySalaryList(String sDate,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		PageObject pg = new PageObject();
		
		String sql ="select b.seniority_salary_id,\n" +
			"       b.seniority_salary,\n" + 
			"       to_char(b.effect_start_time, 'yyyy-MM'),\n" + 
			"       to_char(b.effect_end_time, 'yyyy-MM'),\n" + 
			"       b.memo,\n" + 
			"       decode(b.effect_start_time,\n" + 
			"              (select max(a.effect_start_time)\n" + 
			"                 from HR_C_SENIORITY_SALARY a\n" + 
			"                where a.is_use = 'Y'\n" + 
			"                  and a.enterprise_code = '"+enterpriseCode+"'),\n" + 
			"              '1',\n" + 
			"              '0') status\n" + 
			"  from HR_C_SENIORITY_SALARY b\n" + 
			" where b.is_use = 'Y'\n" + 
			"   and b.enterprise_code = '"+enterpriseCode+"'";

		String sqlCount = "select count(1)\n" +
			"  from HR_C_SENIORITY_SALARY b\n" + 
			" where b.is_use = 'Y'\n" + 
			"   and b.enterprise_code = '"+enterpriseCode+"'";
		
		String whereStr = "";
		if (sDate != null && sDate.length() > 0) {
			whereStr +=" and to_char(b.effect_start_time,'yyyy-MM') <= '"+sDate+"%'";
			whereStr +=" and to_char(b.effect_end_time,'yyyy-MM') >= '"+sDate+"%'";
		}

		sql += whereStr;
		sqlCount += whereStr;
		sql += " order by b.effect_start_time";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<SenioritySalaryForm> arrlist = new ArrayList();
		while (it.hasNext()) {
			SenioritySalaryForm form = new SenioritySalaryForm();
			Object[] data = (Object[]) it.next();
			if(data[0] != null)
				form.setSenioritySalaryId(Long.parseLong(data[0].toString()));
			if(data[1] != null)
				form.setSenioritySalary(Double.parseDouble(data[1].toString()));
			if(data[2] != null)
				form.setEffectStartTime(data[2].toString());
			if(data[3] != null)
				form.setEffectEndTime(data[3].toString());
			if(data[4] != null)
				form.setMemo(data[4].toString());
			if(data[5] != null)
				form.setStatus(data[5].toString());
			
			arrlist.add(form);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}
}