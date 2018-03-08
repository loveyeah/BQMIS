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
 * Facade for entity RunCTimeworkstatus.
 * 
 * @see power.ejb.run.timework.RunCTimeworkstatus
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCTimeworkstatusFacade implements RunCTimeworkstatusFacadeRemote {
	// property constants
	public static final String CODE = "code";
	public static final String NAME = "name";
	public static final String ORDERBY = "orderby";
	public static final String MEMO = "memo";
	public static final String STATUS = "status";
	public static final String ENTERPRISECODE = "enterprisecode";
	public static final String IS_USE = "isUse";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved RunCTimeworkstatus
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            RunCTimeworkstatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(RunCTimeworkstatus entity) {
		try {
			if (entity.getId() == null) {
				entity.setId(dll.getMaxId("RUN_C_TIMEWORKSTATUS", "ID"));
			}
			if (entity.getOrderby() == null) {
				entity.setOrderby(entity.getId());
				if (entity.getId() < 10) {
					entity.setCode("ST0" + entity.getId());
				} else {
					entity.setCode("ST" + entity.getId());
				}
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
	 * Delete a persistent RunCTimeworkstatus entity.
	 * 
	 * @param entity
	 *            RunCTimeworkstatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCTimeworkstatus entity) {
		LogUtil.log("deleting RunCTimeworkstatus instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCTimeworkstatus.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunCTimeworkstatus entity and return it or a
	 * copy of it to the sender. A copy of the RunCTimeworkstatus entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            RunCTimeworkstatus entity to update
	 * @return RunCTimeworkstatus the persisted RunCTimeworkstatus entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCTimeworkstatus update(RunCTimeworkstatus entity) {
		LogUtil.log("updating RunCTimeworkstatus instance", Level.INFO, null);
		try {
			RunCTimeworkstatus result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCTimeworkstatus findById(Long id) {
		LogUtil.log("finding RunCTimeworkstatus instance with id: " + id,
				Level.INFO, null);
		try {
			RunCTimeworkstatus instance = entityManager.find(
					RunCTimeworkstatus.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCTimeworkstatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCTimeworkstatus property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCTimeworkstatus> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCTimeworkstatus> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCTimeworkstatus instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCTimeworkstatus model where model."
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

	public List<RunCTimeworkstatus> findByCode(Object code,
			int... rowStartIdxAndCount) {
		return findByProperty(CODE, code, rowStartIdxAndCount);
	}

	public List<RunCTimeworkstatus> findByName(Object name,
			int... rowStartIdxAndCount) {
		return findByProperty(NAME, name, rowStartIdxAndCount);
	}

	public List<RunCTimeworkstatus> findByOrderby(Object orderby,
			int... rowStartIdxAndCount) {
		return findByProperty(ORDERBY, orderby, rowStartIdxAndCount);
	}

	public List<RunCTimeworkstatus> findByMemo(Object memo,
			int... rowStartIdxAndCount) {
		return findByProperty(MEMO, memo, rowStartIdxAndCount);
	}

	public List<RunCTimeworkstatus> findByStatus(Object status,
			int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	public List<RunCTimeworkstatus> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount) {
		return findByProperty(ENTERPRISECODE, enterprisecode,
				rowStartIdxAndCount);
	}

	public List<RunCTimeworkstatus> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	/**
	 * Find all RunCTimeworkstatus entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCTimeworkstatus> all RunCTimeworkstatus entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCTimeworkstatus> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCTimeworkstatus instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from RunCTimeworkstatus model";
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
		String sql = "select count(1) from RUN_C_TIMEWORKSTATUS where IS_USE='Y' and code='"
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