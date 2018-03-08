package power.ejb.hr.salary;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.salary.form.SalaryPointForm;
import java.util.Iterator;

/**
 * Facade for entity HrCSalaryPoint.
 * 
 * @see power.ejb.hr.salary.HrCSalaryPoint
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCSalaryPointFacade implements HrCSalaryPointFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(HrCSalaryPoint entity) {
		try {
			entity.setSalaryPointId(bll.getMaxId("HR_C_SALARY_POINT",
					"SALARY_POINT_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMulti(String ids)
	{
		String sql="update HR_C_SALARY_POINT t\n" +
			"   set t.is_use = 'N'\n" + 
			" where t.SALARY_POINT_ID in ("+ids+")";

       bll.exeNativeSQL(sql);
	}
     
	public HrCSalaryPoint update(HrCSalaryPoint entity) {
		try {
			HrCSalaryPoint result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCSalaryPoint findById(Long id) {
		try {
			HrCSalaryPoint instance = entityManager.find(HrCSalaryPoint.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String sDate, String enterprisecode,
			final int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		String sql = "select t.*,\n"
				+ "       to_char(t.effect_start_time, 'yyyy-MM'),\n"
				+ "       to_char(t.effect_end_time, 'yyyy-MM'),\n"
				+ "       decode(t.effect_start_time,(select max(a.effect_start_time) from hr_c_salary_point a where a.is_use='Y'\n"
				+ "  and a.enterprise_code ='hfdc'),'1','0') status\n"
				+ "  from hr_c_salary_point t\n" + "  where t.is_use='Y'\n"
				+ "  and t.enterprise_code ='hfdc' ";

		String sqlcount = "select count(1)\n" + "  from hr_c_salary_point t\n"
				+ "  where t.is_use='Y'\n" + "  and t.enterprise_code ='"
				+ enterprisecode + "'";
		String whereStr = "";
		if (sDate != null && sDate.length() > 0) {
			whereStr +=" and to_char(t.effect_start_time,'yyyy-MM') <= '"+sDate+"%'";
			whereStr +=" and to_char(t.effect_end_time,'yyyy-MM') >= '"+sDate+"%'";
		}

		sql += whereStr;
		sql += " order by t.effect_start_time";
		sqlcount += whereStr;
		Long count = Long.parseLong(bll.getSingal(sqlcount).toString());
		if (count > 0) {
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			List<SalaryPointForm> arrList = new ArrayList<SalaryPointForm>();
			Iterator it = list.iterator();
			while (it.hasNext()) {
				HrCSalaryPoint model = new HrCSalaryPoint();
				SalaryPointForm mForm = new SalaryPointForm();
				Object[] o = (Object[]) it.next();
				if (o[0] != null)
					model.setSalaryPointId(Long.parseLong(o[0].toString()));
				if (o[1] != null)
					model.setSalaryPoint(Double.parseDouble(o[1].toString()));
				if (o[4] != null)
					model.setMemo(o[4].toString());
				if (o[7] != null)
					mForm.setStartTime(o[7].toString());
				if (o[8] != null)
					mForm.setEndTime(o[8].toString());
				if(o[9]!=null)
					mForm.setStatus(o[9].toString());
				mForm.setSpoint(model);
				arrList.add(mForm);
			}
			result.setList(arrList);
			result.setTotalCount(count);
		}
		return result;
	}

}