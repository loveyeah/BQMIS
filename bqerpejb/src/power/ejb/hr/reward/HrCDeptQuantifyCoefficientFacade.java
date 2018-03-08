package power.ejb.hr.reward;

import java.util.ArrayList;
import java.util.Iterator;
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
import power.ejb.run.securityproduction.SpJSafeawardDetails;
import power.ejb.run.securityproduction.form.SpJSafeawardDetailsForm;

@Stateless
public class HrCDeptQuantifyCoefficientFacade implements
		HrCDeptQuantifyCoefficientFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public void save(HrCDeptQuantifyCoefficient entity) {
		LogUtil.log("saving HrCDeptQuantifyCoefficient instance", Level.INFO,
				null);
		try {
			Long coeffId = bll.getMaxId("HR_C_DEPT_QUANTIFY_COEFFICIENT",
					"coefficient_id");
			entity.setCoefficientId(coeffId);
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(HrCDeptQuantifyCoefficient entity) {
		LogUtil.log("deleting HrCDeptQuantifyCoefficient instance", Level.INFO,
				null);
		try {
			entity = entityManager
					.getReference(HrCDeptQuantifyCoefficient.class, entity
							.getCoefficientId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCDeptQuantifyCoefficient update(HrCDeptQuantifyCoefficient entity) {
		LogUtil.log("updating HrCDeptQuantifyCoefficient instance", Level.INFO,
				null);
		try {
			HrCDeptQuantifyCoefficient result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCDeptQuantifyCoefficient findById(Long id) {
		LogUtil.log("finding HrCDeptQuantifyCoefficient instance with id: "
				+ id, Level.INFO, null);
		try {
			HrCDeptQuantifyCoefficient instance = entityManager.find(
					HrCDeptQuantifyCoefficient.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public void saveFirstDept(String monthDate, String enterprise) {
		String sqlCount = "select COUNT(1)\n"
				+ "  from HR_C_DEPT_QUANTIFY_COEFFICIENT t\n"
				+ " where t.coefficient_month = '" + monthDate + "'\n"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterprise + "'";
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		if (count == 0) {
			String sqlString = "select d.dept_id\n" + "  from hr_c_dept d\n"
					+ " where d.dept_level = '1'\n" + 
					"   and d.dept_id != '0'" +
					"   and d.is_use = 'Y'\n" + 
					"   and d.dep_feature = '1'\n" ;
			sqlString += " order by d.dept_id";
			System.out.println(sqlString);
			List list1 = bll.queryByNativeSQL(sqlString);
			Iterator it = list1.iterator();
			while (it.hasNext()) {
				HrCDeptQuantifyCoefficient model = new HrCDeptQuantifyCoefficient();
				model.setDeptId(Long.parseLong(it.next().toString()));
				model.setCoefficientMonth(monthDate);
				model.setIsUse("Y");
				model.setEnterpriseCode(enterprise);
				this.save(model);
				entityManager.flush();
			}
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getDeptQuantifyList(String monthDate, String enterprise,
			final int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		this.saveFirstDept(monthDate, enterprise);
		String sql = "select t.coefficient_id,\n"
				+ "       t.dept_id,\n"
				+ "       (select d.dept_name from hr_c_dept d where d.dept_id = t.dept_id) as deptName,\n"
				+ "       t.quantify_coefficient\n"
				+ "  from HR_C_DEPT_QUANTIFY_COEFFICIENT t\n"
				+ " where t.coefficient_month = '" + monthDate + "'\n"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterprise + "'";
		String sqlCount = "select COUNT(1)\n"
				+ "  from HR_C_DEPT_QUANTIFY_COEFFICIENT t\n"
				+ " where t.coefficient_month = '" + monthDate + "'\n"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterprise + "'";
		sql += " order by deptName ";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		List arrlist = new ArrayList();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			HrCDeptQuantifyCoefficient model = new HrCDeptQuantifyCoefficient();
			Object[] data = (Object[]) it.next();
			model.setCoefficientId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setDeptId(Long.parseLong(data[1].toString()));
			if (data[2] != null)
				model.setIsUse(data[2].toString());
			if (data[3] != null)
				model.setQuantifyCoefficient(Double.parseDouble(data[3]
						.toString()));
			arrlist.add(model);
		}
		result.setList(arrlist);
		result.setTotalCount(count);
		return result;
	}

	public void deleteDeptQuantifyList(String ids) {
		String sql = "update HR_C_DEPT_QUANTIFY_COEFFICIENT t "
				+ " set t.is_use='N'" + "where t.coefficient_id  in (" + ids
				+ ") ";
		bll.exeNativeSQL(sql);
	}

	public void saveDeptQuantifyList(List<HrCDeptQuantifyCoefficient> addList,
			List<HrCDeptQuantifyCoefficient> updateList) {
		if (addList != null && addList.size() > 0) {

			for (HrCDeptQuantifyCoefficient entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (HrCDeptQuantifyCoefficient entity : updateList) {
				this.update(entity);
			}
		}
	}
}