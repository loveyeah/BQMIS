package power.ejb.system;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity SysJLoginLog.
 * 
 * @see power.ejb.system.SysJLoginLog
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SysJLoginLogFacade implements SysJLoginLogFacadeRemote {
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	public void save(SysJLoginLog entity) {
		try {
			if (entity.getId() == null) {
				entity.setId(Long.parseLong(bll.getMaxId("sys_j_login_log",
						"id").toString()));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(SysJLoginLog entity) {
		LogUtil.log("deleting SysJLoginLog instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SysJLoginLog.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved SysJLoginLog entity and return it or a copy of
	 * it to the sender. A copy of the SysJLoginLog entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            SysJLoginLog entity to update
	 * @returns SysJLoginLog the persisted SysJLoginLog entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SysJLoginLog update(SysJLoginLog entity) {
		LogUtil.log("updating SysJLoginLog instance", Level.INFO, null);
		try {
			SysJLoginLog result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysJLoginLog findById(Long id) {
		LogUtil.log("finding SysJLoginLog instance with id: " + id, Level.INFO,
				null);
		try {
			SysJLoginLog instance = entityManager.find(SysJLoginLog.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SysJLoginLog entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SysJLoginLog property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<SysJLoginLog> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<SysJLoginLog> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding SysJLoginLog instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SysJLoginLog model where model."
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
	 * Find all SysJLoginLog entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<SysJLoginLog> all SysJLoginLog entities
	 */
	@SuppressWarnings("unchecked")
	public List<SysJLoginLog> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all SysJLoginLog instances", Level.INFO, null);
		try {
			final String queryString = "select model from SysJLoginLog model";
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

	public PageObject findByWorkerCode(String workerCode, String beginDate,
			String endDate, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select *\n" + "  from sys_j_login_log t\n"
				+ " where t.login_date > to_date('" + beginDate
				+ "'||' 00:00:00','yyyy-MM-dd HH24:MI:SS')\n"
				+ "   and t.login_date < to_date('" + endDate
				+ "'||' 23:59:59','yyyy-MM-dd HH24:MI:SS')\n"
				+ "   and t.worker_code like '%" + workerCode + "%'";
		String sqlCount = "select count(1)\n" + "  from sys_j_login_log t\n"
				+ " where t.login_date > to_date('" + beginDate
				+ "'||' 00:00:00','yyyy-MM-dd HH24:MI:SS')\n"
				+ "   and t.login_date < to_date('" + endDate
				+ "'||' 23:59:59','yyyy-MM-dd HH24:MI:SS')\n"
				+ "   and t.worker_code like '%" + workerCode + "%'";
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		if (count > 0) {
			List<SysJLoginLog> list = bll.queryByNativeSQL(sql,
					SysJLoginLog.class, rowStartIdxAndCount);
			pg.setList(list);
			pg.setTotalCount(count);
		} else {
			pg = null;
		}
		return pg;
	}
}