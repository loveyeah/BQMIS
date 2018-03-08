package power.ejb.equ.failure;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity EquCFailureType.
 * 
 * @see power.ejb.equ.failure.EquCFailureType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCFailureTypeFacade implements EquCFailureTypeFacadeRemote {
	// property constants
	public static final String FAILURETYPE_CODE = "failuretypeCode";
	public static final String FAILURETYPE_NAME = "failuretypeName";
	public static final String FAILURE_PRI = "failurePri";
	public static final String FAILURETYPE_DESC = "failuretypeDesc";
	public static final String NEED_CACL_OVERTIME = "needCaclOvertime";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved EquCFailureType entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCFailureType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquCFailureType entity) {
		LogUtil.log("saving EquCFailureType instance", Level.INFO, null);
		try {
			if (entity.getId() == null) {
				entity.setId(Long.parseLong(bll.getMaxId("equ_c_failure_type",
						"id").toString()));
			}
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent EquCFailureType entity.
	 * 
	 * @param entity
	 *            EquCFailureType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCFailureType entity) {
		try {
			entityManager.merge(entity);
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved EquCFailureType entity and return it or a copy
	 * of it to the sender. A copy of the EquCFailureType entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCFailureType entity to update
	 * @return EquCFailureType the persisted EquCFailureType entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCFailureType update(EquCFailureType entity) {
		try {
			entity.setIsUse("Y");
			EquCFailureType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCFailureType findById(Long id) {
		LogUtil.log("finding EquCFailureType instance with id: " + id,
				Level.INFO, null);
		try {
			EquCFailureType instance = entityManager.find(
					EquCFailureType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquCFailureType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCFailureType property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<EquCFailureType> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCFailureType> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding EquCFailureType instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquCFailureType model where model."
					+ propertyName + "= :propertyValue order by model.id";
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

	public List<EquCFailureType> findByFailuretypeCode(Object failuretypeCode,
			int... rowStartIdxAndCount) {
		return findByProperty(FAILURETYPE_CODE, failuretypeCode,
				rowStartIdxAndCount);
	}

	public List<EquCFailureType> findByFailuretypeName(Object failuretypeName,
			int... rowStartIdxAndCount) {
		return findByProperty(FAILURETYPE_NAME, failuretypeName,
				rowStartIdxAndCount);
	}

	public List<EquCFailureType> findByFailurePri(Object failurePri,
			int... rowStartIdxAndCount) {
		return findByProperty(FAILURE_PRI, failurePri, rowStartIdxAndCount);
	}

	public List<EquCFailureType> findByFailuretypeDesc(Object failuretypeDesc,
			int... rowStartIdxAndCount) {
		return findByProperty(FAILURETYPE_DESC, failuretypeDesc,
				rowStartIdxAndCount);
	}

	public List<EquCFailureType> findByNeedCaclOvertime(
			Object needCaclOvertime, int... rowStartIdxAndCount) {
		return findByProperty(NEED_CACL_OVERTIME, needCaclOvertime,
				rowStartIdxAndCount);
	}

	public List<EquCFailureType> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<EquCFailureType> findByEnterpriseCode(Object enterpriseCode,
			int... rowStartIdxAndCount) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode,
				rowStartIdxAndCount);
	}

	/**
	 * Find all EquCFailureType entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquCFailureType> all EquCFailureType entities
	 */
	@SuppressWarnings("unchecked")
	public List<EquCFailureType> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCFailureType instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCFailureType model";
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
	 * 查询所有的可用的缺陷类别
	 * 
	 * @param enterprise_code
	 *            企业编码
	 * @author nliu
	 * @since 2008-10-31
	 * 
	 * @return List<EquCFailureType> all EquCFailureType entities
	 */
	public List<EquCFailureType> findAllList(String enterprise_code)
			throws Exception {
		String strSql = "select t.id,\n" + "       t.failuretype_code,\n"
				+ "       t.failuretype_name,\n" + "       t.failure_pri,\n"
				+ "       t.failuretype_desc,\n"
				+ "       t.need_cacl_overtime,\n" + "       t.is_use,\n"
				+ "       t.enterprise_code\n"
				+ "  from equ_c_failure_type t\n"
				+ " where t.is_use = 'Y' and t.enterprise_code = '"
				+ enterprise_code + "'";

		try {
			return bll.queryByNativeSQL(strSql);
		} catch (Exception se) {
			LogUtil.log("find all failed", Level.SEVERE, se);
			throw se;
		}
	}

}