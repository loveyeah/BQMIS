package power.ejb.run.timework;

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
 * Facade for entity RunCTimeworktype.
 * 
 * @see power.ejb.run.timework.RunCTimeworktype
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCTimeworktypeFacade implements RunCTimeworktypeFacadeRemote {
	// property constants
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String ORDERBY = "orderby";
	public static final String MEMO = "memo";
	public static final String SHOWTYPE = "showtype";
	public static final String KEYWORDSINSQL = "keywordsinsql";
	public static final String STATUS = "status";
	public static final String ENTERPRISECODE = "enterprisecode";
	public static final String IS_USE = "isUse";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved RunCTimeworktype entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCTimeworktype entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(RunCTimeworktype entity) {
		try {
			entity.setId(dll.getMaxId("RUN_C_TIMEWORKTYPE", "ID"));
			entity.setOrderby(entity.getId());
			if (entity.getId() < 10) {
				entity.setCode("WT0" + entity.getId());
			} else {
				entity.setCode("WT" + entity.getId());
			}
			if (CheckCode(entity.getCode())) {
				entityManager.persist(entity);
				return entity.getId();
			} else {
				return 0;
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunCTimeworktype entity.
	 * 
	 * @param entity
	 *            RunCTimeworktype entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCTimeworktype entity) {
		LogUtil.log("deleting RunCTimeworktype instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCTimeworktype.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunCTimeworktype entity and return it or a
	 * copy of it to the sender. A copy of the RunCTimeworktype entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCTimeworktype entity to update
	 * @return RunCTimeworktype the persisted RunCTimeworktype entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCTimeworktype update(RunCTimeworktype entity) {
		LogUtil.log("updating RunCTimeworktype instance", Level.INFO, null);
		try {
			RunCTimeworktype result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCTimeworktype findById(Long id) {
		LogUtil.log("finding RunCTimeworktype instance with id: " + id,
				Level.INFO, null);
		try {
			RunCTimeworktype instance = entityManager.find(
					RunCTimeworktype.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCTimeworktype entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCTimeworktype property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCTimeworktype> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCTimeworktype> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCTimeworktype instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCTimeworktype model where model."
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

	public List<RunCTimeworktype> findByCode(Object code,
			int... rowStartIdxAndCount) {
		return findByProperty(CODE, code, rowStartIdxAndCount);
	}

	public List<RunCTimeworktype> findByName(Object name,
			int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<RunCTimeworktype> findByOrderby(Object orderby,
			int... rowStartIdxAndCount) {
		return findByProperty(ORDERBY, orderby, rowStartIdxAndCount);
	}

	public List<RunCTimeworktype> findByMemo(Object memo,
			int... rowStartIdxAndCount) {
		return findByProperty(MEMO, memo, rowStartIdxAndCount);
	}

	public List<RunCTimeworktype> findByShowtype(Object showtype,
			int... rowStartIdxAndCount) {
		return findByProperty(SHOWTYPE, showtype, rowStartIdxAndCount);
	}

	public List<RunCTimeworktype> findByKeywordsinsql(Object keywordsinsql,
			int... rowStartIdxAndCount) {
		return findByProperty(KEYWORDSINSQL, keywordsinsql, rowStartIdxAndCount);
	}

	public List<RunCTimeworktype> findByStatus(Object status,
			int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	public List<RunCTimeworktype> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount) {
		return findByProperty(ENTERPRISECODE, enterprisecode,
				rowStartIdxAndCount);
	}

	// 为维护展示数据
	@SuppressWarnings("unchecked")
	public List<RunCTimeworktype> findForManage(int... rowStartIdxAndCount) {
		String sql = "select * from RUN_C_TIMEWORKTYPE where IS_USE='Y'and STATUS='C' order by orderby";
		List<RunCTimeworktype> list = dll.queryByNativeSQL(sql,
				RunCTimeworktype.class, rowStartIdxAndCount);
		return list;
	}

	@SuppressWarnings("unchecked")
	public List<RunCTimeworktype> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		String sql = "select * from RUN_C_TIMEWORKTYPE where IS_USE='Y' order by orderby";
		List<RunCTimeworktype> list = dll.queryByNativeSQL(sql,
				RunCTimeworktype.class, rowStartIdxAndCount);
		return list;
	}

	/**
	 * Find all RunCTimeworktype entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimeworktype> all RunCTimeworktype entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCTimeworktype> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCTimeworktype instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCTimeworktype model";
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

	/**
	 * 判断定期工作编号是否重复
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param code
	 *            编号
	 * @param Id
	 *            主键
	 * @return boolean
	 */
	private boolean CheckCode(String workitemcode, Long... Id) {
		boolean isSame = true;
		String sql = "select count(1) from RUN_C_TIMEWORKTYPE where IS_USE='Y' and code='"
				+ workitemcode + "' ";
		if (Id != null && Id.length > 0) {
			sql += "Id <> " + Id[0];
		}
		if (Long.parseLong((dll.getSingal(sql).toString())) > 0) {
			isSame = false;
		}
		return isSame;
	}
}