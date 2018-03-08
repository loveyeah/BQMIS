package power.ejb.hr.salary;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.Iterator;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrCOperationSalary.
 * 
 * @see power.ejb.hr.salary.HrCOperationSalary
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCOperationSalaryFacade implements HrCOperationSalaryFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved HrCOperationSalary
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            HrCOperationSalary entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCOperationSalary entity) {
		try {
			entity.setOperationSalaryId(bll.getMaxId("HR_C_OPERATION_SALARY",
					"OPERATION_SALARY_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCOperationSalary entity.
	 * 
	 * @param entity
	 *            HrCOperationSalary entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String ids) {
		try {
			String sql = "update hr_c_operation_salary t set t.is_use ='N' where t.operation_salary_id in ("
					+ ids + ")";
			bll.exeNativeSQL(sql);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCOperationSalary entity and return it or a
	 * copy of it to the sender. A copy of the HrCOperationSalary entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            HrCOperationSalary entity to update
	 * @return HrCOperationSalary the persisted HrCOperationSalary entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCOperationSalary update(HrCOperationSalary entity) {
		try {
			HrCOperationSalary result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCOperationSalary findById(Long id) {
		try {
			HrCOperationSalary instance = entityManager.find(
					HrCOperationSalary.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCOperationSalary entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCOperationSalary property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrCOperationSalary> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCOperationSalary> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCOperationSalary instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCOperationSalary model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCOperationSalary entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCOperationSalary> all HrCOperationSalary entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String EnterpriseCode,
			final int... rowStartIdxAndCount) {
		PageObject object = new PageObject();
		
		String sql = "select t.*,\n" +
			"       decode(t.start_seniority,\n" + 
			"              (select max(a.start_seniority)\n" + 
			"                 from hr_c_operation_salary a\n" + 
			"                where a.is_use = 'Y'),\n" + 
			"              '1',\n" + 
			"              '0')\n" + 
			"  from hr_c_operation_salary t\n" + 
			" where t.is_use = 'Y'\n" + 
			"   and t.enterprise_code = '"+EnterpriseCode+"'";

		String sqlcount = "select count(1) from hr_c_operation_salary t  where t.is_use ='Y' and t.enterprise_code='"
				+ EnterpriseCode + "'";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlcount).toString());
		List<HrCOperationSalary> arrList = new ArrayList<HrCOperationSalary>();
		Iterator it = list.iterator();
		if (count > 0) {
			while (it.hasNext()) {
				Object[] o = (Object[]) it.next();
				HrCOperationSalary model = new HrCOperationSalary();
				if (o[0] != null) {
					model.setOperationSalaryId(Long.parseLong(o[0].toString()));
				}
				if (o[1] != null)
					model.setStartSeniority(Long.parseLong(o[1].toString()));
				if (o[2] != null)
					model.setEndSeniority(Long.parseLong(o[2].toString()));
				if (o[3] != null)
					model.setOperationSalaryPoint(Double.parseDouble(o[3]
							.toString()));
				if (o[4] != null)
					model.setMemo(o[4].toString());
				if (o[7] != null)
					model.setIsUse(o[7].toString());
				arrList.add(model);
			}
			object.setList(arrList);
			object.setTotalCount(count);
		}
		return object;
	}
}