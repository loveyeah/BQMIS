package power.ejb.hr.salary;

import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.ca.HrCOvertime;

/**
 * Facade for entity HrCOvertimeSalaryScale.
 * 
 * @see power.ejb.hr.salary.HrCOvertimeSalaryScale
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCOvertimeSalaryScaleFacade implements
		HrCOvertimeSalaryScaleFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public void save(HrCOvertimeSalaryScale entity) {
		LogUtil.log("saving HrCOvertimeSalaryScale instance", Level.INFO, null);
		try {
			entity.setOvertimeSalaryScaleId(bll.getMaxId("HR_C_OVERTIME_SALARY_SCALE", "overtime_salary_scale_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public void deleteAndAddRecord(String ids,String enterpriseCode) 
	{
		String sql= "update HR_C_OVERTIME_SALARY_SCALE s\n" +
			"   set s.is_use = 'N'\n" + 
			" where s.overtime_salary_scale_id in ("+ids+")\n" + 
			"   and s.is_use = 'Y'";
		bll.exeNativeSQL(sql);
		entityManager.flush();
		
		String scaleSql = "select count(1)\n" +
		"  from HR_C_OVERTIME_SALARY_SCALE a\n" + 
		" where a.is_use = 'Y'\n" + 
		"   and a.enterprise_code = '"+enterpriseCode+"'";
		Long tscalecount = Long.parseLong(bll.getSingal(scaleSql).toString());
		
		if(tscalecount == 0)
		{
			String typeSql = "select o.*\n" +
				"  from HR_C_OVERTIME o\n" + 
				" where o.is_use = 'Y'\n" + 
				"   and o.enterprise_code = '"+enterpriseCode+"'\n" + 
				" order by o.overtime_type_id asc ";
			List<HrCOvertime> list = bll.queryByNativeSQL(typeSql,HrCOvertime.class);
			
			String typesqlcount = "select count(1)\n" +
				"  from HR_C_OVERTIME o\n" + 
				" where o.is_use = 'Y'\n" + 
				"   and o.enterprise_code = '"+enterpriseCode+"'";
			Long typecount = Long.parseLong(bll.getSingal(typesqlcount).toString());
	
			if (typecount > 0) {
				for (int i = 0; i < list.size(); i++) {
					HrCOvertimeSalaryScale model = new HrCOvertimeSalaryScale();
					model.setOvertimeTypeId(list.get(i).getOvertimeTypeId().toString());
					model.setIsUse(list.get(i).getIsUse().toString());
					model.setEnterpriseCode(list.get(i).getEnterpriseCode().toString());
					this.save(model);
					entityManager.flush();
				}
			}
		}
	}

	public HrCOvertimeSalaryScale update(HrCOvertimeSalaryScale entity) {
		LogUtil.log("updating HrCOvertimeSalaryScale instance", Level.INFO,
				null);
		try {
			HrCOvertimeSalaryScale result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCOvertimeSalaryScale findById(Long id) {
		LogUtil.log("finding HrCOvertimeSalaryScale instance with id: " + id,
				Level.INFO, null);
		try {
			HrCOvertimeSalaryScale instance = entityManager.find(
					HrCOvertimeSalaryScale.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public void saveOvertimeSalaryRecord(List<Map> list) {
		try {
			for (Map data : list) {
				HrCOvertimeSalaryScale model = entityManager.find(HrCOvertimeSalaryScale.class,Long.parseLong(data.get("id").toString()));
				model.setOvertimeSalaryScale(Double.parseDouble(data.get("scale").toString()));
				if(data.get("memo") != null)
				{
					model.setMemo(data.get("memo").toString());
				}else{
					model.setMemo("");
				}
				entityManager.merge(model);
			}

		} catch (RuntimeException e) {
			throw e;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findSalaryScaleList(String enterpriseCode,
			final int... rowStartIdxAndCount) {

		String scaleSql = "select count(1)\n"
				+ "  from HR_C_OVERTIME_SALARY_SCALE a\n"
				+ " where a.is_use = 'Y'\n" + "   and a.enterprise_code = '"
				+ enterpriseCode + "'";
		Long tscalecount = Long.parseLong(bll.getSingal(scaleSql).toString());

		if (tscalecount == 0) {
			String typeSql = "select o.*\n" + "  from HR_C_OVERTIME o\n"
					+ " where o.is_use = 'Y'\n"
					+ "   and o.enterprise_code = '" + enterpriseCode + "'\n"
					+ " order by o.overtime_type_id asc ";
			List<HrCOvertime> list = bll.queryByNativeSQL(typeSql,
					HrCOvertime.class);

			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					HrCOvertimeSalaryScale model = new HrCOvertimeSalaryScale();
					model.setOvertimeTypeId(list.get(i).getOvertimeTypeId()
							.toString());
					model.setIsUse(list.get(i).getIsUse().toString());
					model.setEnterpriseCode(list.get(i).getEnterpriseCode()
							.toString());
					this.save(model);
					entityManager.flush();
				}
			}
		}

		PageObject pg = new PageObject();
		String sql = "select a.overtime_salary_scale_id,\n"
				+ "       a.overtime_type_id,\n"
				+ "       (select o.overtime_type\n"
				+ "          from HR_C_OVERTIME o\n"
				+ "         where o.overtime_type_id = a.overtime_type_id\n"
				+ "           and o.is_use = 'Y'),\n"
				+ "       a.overtime_salary_scale,\n" + "       a.memo\n"
				+ "  from HR_C_OVERTIME_SALARY_SCALE a\n"
				+ " where a.is_use = 'Y'\n" + "   and a.enterprise_code = '"
				+ enterpriseCode + "'";

		String sqlCount = "select count(1)\n"
				+ "  from HR_C_OVERTIME_SALARY_SCALE a\n"
				+ " where a.is_use = 'Y'\n" + "   and a.enterprise_code = '"
				+ enterpriseCode + "'";

		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}

}