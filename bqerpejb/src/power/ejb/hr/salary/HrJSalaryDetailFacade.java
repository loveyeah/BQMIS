package power.ejb.hr.salary;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 *薪酬明细表
 *@author liuyi 090928
 */
@Stateless
public class HrJSalaryDetailFacade implements HrJSalaryDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 新增一条薪酬明细表数据
	 */
	public void save(HrJSalaryDetail entity) {
		LogUtil.log("saving HrJSalaryDetail instance", Level.INFO, null);
		try {
			entity.setSalaryDetailId(bll.getMaxId("HR_J_SALARY_DETAIL", "SALARY_DETAIL_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除一条薪酬明细表数据
	 */
	public void delete(HrJSalaryDetail entity) {
		LogUtil.log("deleting HrJSalaryDetail instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJSalaryDetail.class, entity
					.getSalaryDetailId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 更新一条薪酬明细表数据
	 */
	public HrJSalaryDetail update(HrJSalaryDetail entity) {
		LogUtil.log("updating HrJSalaryDetail instance", Level.INFO, null);
		try {
			HrJSalaryDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJSalaryDetail findById(Long id) {
		LogUtil.log("finding HrJSalaryDetail instance with id: " + id,
				Level.INFO, null);
		try {
			HrJSalaryDetail instance = entityManager.find(
					HrJSalaryDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<HrJSalaryDetail> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrJSalaryDetail instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJSalaryDetail model where model."
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

	
	@SuppressWarnings("unchecked")
	public List<HrJSalaryDetail> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrJSalaryDetail instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrJSalaryDetail model";
			Query query = entityManager.createQuery(queryString);
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
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}