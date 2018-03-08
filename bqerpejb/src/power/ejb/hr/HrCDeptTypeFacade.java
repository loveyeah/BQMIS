package power.ejb.hr;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Facade for entity HrCDeptType.
 * 
 * @see powereai.po.hr.HrCDeptType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrCDeptTypeFacade implements HrCDeptTypeFacadeRemote {
	// property constants
	public static final String DEPT_TYPE_NAME = "deptTypeName";
	public static final String IS_USE = "isUse";
	public static final String RETRIEVE_CODE = "retrieveCode";

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved HrCDeptType entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            HrCDeptType entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(HrCDeptType entity) {
		LogUtil.log("saving HrCDeptType instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent HrCDeptType entity.
	 * 
	 * @param entity
	 *            HrCDeptType entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(HrCDeptType entity) {
		LogUtil.log("deleting HrCDeptType instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrCDeptType.class, entity
					.getDeptTypeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved HrCDeptType entity and return it or a copy of
	 * it to the sender. A copy of the HrCDeptType entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            HrCDeptType entity to update
	 * @return HrCDeptType the persisted HrCDeptType entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public HrCDeptType update(HrCDeptType entity) {
		LogUtil.log("updating HrCDeptType instance", Level.INFO, null);
		try {
			HrCDeptType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrCDeptType findById(Long id) {
		LogUtil.log("finding HrCDeptType instance with id: " + id, Level.INFO,
				null);
		try {
			HrCDeptType instance = entityManager.find(HrCDeptType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all HrCDeptType entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the HrCDeptType property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<HrCDeptType> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<HrCDeptType> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding HrCDeptType instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrCDeptType model where model."
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

	public List<HrCDeptType> findByDeptTypeName(Object deptTypeName,
			int... rowStartIdxAndCount) {
		return findByProperty(DEPT_TYPE_NAME, deptTypeName, rowStartIdxAndCount);
	}

	public List<HrCDeptType> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	public List<HrCDeptType> findByRetrieveCode(Object retrieveCode,
			int... rowStartIdxAndCount) {
		return findByProperty(RETRIEVE_CODE, retrieveCode, rowStartIdxAndCount);
	}

	/**
	 * Find all HrCDeptType entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<HrCDeptType> all HrCDeptType entities
	 */
	@SuppressWarnings("unchecked")
	public List<HrCDeptType> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all HrCDeptType instances", Level.INFO, null);
		try {
			final String queryString = "select model from HrCDeptType model";
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