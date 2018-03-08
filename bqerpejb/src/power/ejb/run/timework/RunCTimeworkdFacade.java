package power.ejb.run.timework;

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
 * Facade for entity RunCTimeworkd.
 * 
 * @see power.ejb.run.timework.RunCTimeworkd
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCTimeworkdFacade implements RunCTimeworkdFacadeRemote {
	// property constants
	public static final String WORK_ITEM_CODE = "workItemCode";
	public static final String MONTH = "month";
	public static final String WEEK_NO = "weekNo";
	public static final String TEST_DAY = "testDay";
	public static final String CLASS_SEQUENCE = "classSequence";
	public static final String CYCLE_SEQUENCE = "cycleSequence";
	public static final String STATUS = "status";
	public static final String ENTERPRISECODE = "enterprisecode";
	public static final String IS_USE = "isUse";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved RunCTimeworkd entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCTimeworkd entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public boolean save(RunCTimeworkd entity) {
		try {
			entity.setId(dll.getMaxId("RUN_C_TIMEWORKD", "ID"));
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunCTimeworkd entity.
	 * 
	 * @param entity
	 *            RunCTimeworkd entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCTimeworkd entity) {
		LogUtil.log("deleting RunCTimeworkd instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCTimeworkd.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunCTimeworkd entity and return it or a copy
	 * of it to the sender. A copy of the RunCTimeworkd entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCTimeworkd entity to update
	 * @return RunCTimeworkd the persisted RunCTimeworkd entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCTimeworkd update(RunCTimeworkd entity) {
		LogUtil.log("updating RunCTimeworkd instance", Level.INFO, null);
		try {
			RunCTimeworkd result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCTimeworkd findById(Long id) {
		LogUtil.log("finding RunCTimeworkd instance with id: " + id,
				Level.INFO, null);
		try {
			RunCTimeworkd instance = entityManager
					.find(RunCTimeworkd.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCTimeworkd entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCTimeworkd property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCTimeworkd> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCTimeworkd> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCTimeworkd instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCTimeworkd model where model."
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

	public List<RunCTimeworkd> findByWorkItemCode(Object workItemCode,
			int... rowStartIdxAndCount) {
		return findByProperty(WORK_ITEM_CODE, workItemCode, rowStartIdxAndCount);
	}

	public List<RunCTimeworkd> findByMonth(Object month,
			int... rowStartIdxAndCount) {
		return findByProperty(MONTH, month, rowStartIdxAndCount);
	}

	public List<RunCTimeworkd> findByWeekNo(Object weekNo,
			int... rowStartIdxAndCount) {
		return findByProperty(WEEK_NO, weekNo, rowStartIdxAndCount);
	}

	public List<RunCTimeworkd> findByTestDay(Object testDay,
			int... rowStartIdxAndCount) {
		return findByProperty(TEST_DAY, testDay, rowStartIdxAndCount);
	}

	public List<RunCTimeworkd> findByClassSequence(Object classSequence,
			int... rowStartIdxAndCount) {
		return findByProperty(CLASS_SEQUENCE, classSequence,
				rowStartIdxAndCount);
	}

	public List<RunCTimeworkd> findByCycleSequence(Object cycleSequence,
			int... rowStartIdxAndCount) {
		return findByProperty(CYCLE_SEQUENCE, cycleSequence,
				rowStartIdxAndCount);
	}

	public List<RunCTimeworkd> findByStatus(Object status,
			int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	public List<RunCTimeworkd> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount) {
		return findByProperty(ENTERPRISECODE, enterprisecode,
				rowStartIdxAndCount);
	}

	public List<RunCTimeworkd> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	@SuppressWarnings("unchecked")
	public PageObject findByIsUse(Object isUse, String workitemcode,
			int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		String sql = "select * from RUN_C_TIMEWORKD t where t.WORK_ITEM_CODE='"
				+ workitemcode + "'and t.is_use='Y' order by id desc";
		List<RunCTimeworkd> list = dll.queryByNativeSQL(sql,
				RunCTimeworkd.class, rowStartIdxAndCount);
		result.setList(list);
		return result;
	}

	@SuppressWarnings("unchecked")
	public List<RunCTimeworkd> findToGenerate(String enterpriseCode,
			int... rowStartIdxAndCount) {
		String sql = "select * from RUN_C_TIMEWORKD t where t.is_use='Y' and STATUS='C' and ENTERPRISECODE ='"
				+ enterpriseCode + "' order by id desc";
		List<RunCTimeworkd> list = dll.queryByNativeSQL(sql,
				RunCTimeworkd.class, rowStartIdxAndCount);
		return list;
	}

	/**
	 * Find all RunCTimeworkd entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimeworkd> all RunCTimeworkd entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCTimeworkd> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCTimeworkd instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCTimeworkd model";
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