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
import power.ejb.hr.salary.form.SickSalaryForm;

/**
 * Facade for entity HrCSickSalary.
 * 
 * @see power.ejb.hr.salary.HrCSickSalary
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCSickSalaryFacade implements HrCSickSalaryFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 保存一条病假工资记录
	 * 
	 * @param entity HrCSickSalary
	 */
	public void save(HrCSickSalary entity) {
		LogUtil.log("saving HrCSickSalary instance", Level.INFO, null);
		try {
			entity.setSickSalaryId(bll.getMaxId("HR_C_SICK_SALARY",
			"SICK_SALARY_ID"));
	        entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除病假工资记录
	 * 
	 * @param sickSalary String
	 */
	public void delete(String sickSalary) {
		LogUtil.log("deleting HrCSickSalary instance", Level.INFO, null);
		String sql = "update HR_C_SICK_SALARY t\n"
				+ "   set t.IS_USE = 'N'\n"
				+ " where t.SICK_SALARY_ID in (" + sickSalary + ")";

		bll.exeNativeSQL(sql);
	}

	/**
	 * 更新一条病假工资记录
	 * @param entity HrCSickSalary
	 * @return HrCSickSalary
	 */
	public HrCSickSalary update(HrCSickSalary entity) {
		LogUtil.log("updating HrCSickSalary instance", Level.INFO, null);
		try {
			HrCSickSalary result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 根据主key病假工资id检索数据
	 * 
	 * @param id Long
	 * @return HrCSickSalary
	 */
	public HrCSickSalary findById(Long id) {
		LogUtil.log("finding HrCSickSalary instance with id: " + id,
				Level.INFO, null);
		try {
			HrCSickSalary instance = entityManager
					.find(HrCSickSalary.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 查询所有的病假工资记录
	 * 
	 * @param rowStartIdxAndCount int
	 * @return PageObject
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCSickSalary instances", Level.INFO, null);
		PageObject pg = new PageObject();
		String sql = "select t.SICK_SALARY_ID, t.FACT_WORKYEAR_BOTTOM,\n "
			+ " t.FACT_WORKYEAR_TOP,\n"
			+ " t.LOCAL_WORKAGE_BOTTOM,\n"
			+ " t.LOCAL_WORKAGE_TOP,\n"
			+ " t.SALARY_SCALE,\n"
			+ " t.MEMO"
			+ " from HR_C_SICK_SALARY t"
			+ " where t.IS_USE = 'Y'"
			+ " and t.ENTERPRISE_CODE = '" + enterpriseCode + "'"
		    + "order by t.FACT_WORKYEAR_BOTTOM, t.LOCAL_WORKAGE_BOTTOM";
		String sqlCount = "select count(1)\n"
			+ " from HR_C_SICK_SALARY t\n" + " where t.is_use = 'Y'\n"
			+ " and t.enterprise_code = '" + enterpriseCode + "'";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Iterator it = list.iterator();
		List<SickSalaryForm> arrlist = new ArrayList();
		while (it.hasNext()) {
			SickSalaryForm form = new SickSalaryForm();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				form.setSickSalaryId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				form.setFactWorkyearBottom(Double.parseDouble(data[1].toString()));
			if (data[2] != null)
				form.setFactWorkyearTop(Double.parseDouble(data[2].toString()));
			if (data[3] != null)
			    form.setLocalWorkageBottom(Double.parseDouble(data[3].toString()));
			if (data[4] != null)
			    form.setLocalWorkageTop(Double.parseDouble(data[4].toString()));
			if (data[5] != null)
			    form.setSalaryScale(Double.parseDouble(data[5].toString()));
			if (data[6] != null)
			    form.setMemo(data[6].toString());
			arrlist.add(form);
		}
		pg.setList(arrlist);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

}