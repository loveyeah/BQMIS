package power.ejb.hr;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * Facade for entity SysCRole.
 * 
 * @see powereai.po.sys.SysCRole
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SysCRoleFacade implements SysCRoleFacadeRemote {
	// property constants
	public static final String ROLE_TYPE_ID = "roleTypeId";
	public static final String ROLE_NAME = "roleName";
	public static final String ROLE_STATUS = "roleStatus";
	public static final String MEMO = "memo";
	public static final String ORDER_BY = "orderBy";
	public static final String CREATE_BY = "createBy";
	public static final String LAST_MODIFIY_BY = "lastModifiyBy";

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved SysCRole entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            SysCRole entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(SysCRole entity) {
		LogUtil.log("saving SysCRole instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent SysCRole entity.
	 * 
	 * @param entity
	 *            SysCRole entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(SysCRole entity) {
		LogUtil.log("deleting SysCRole instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(SysCRole.class, entity
					.getRoleId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved SysCRole entity and return it or a copy of it
	 * to the sender. A copy of the SysCRole entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            SysCRole entity to update
	 * @return SysCRole the persisted SysCRole entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public SysCRole update(SysCRole entity) {
		LogUtil.log("updating SysCRole instance", Level.INFO, null);
		try {
			SysCRole result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysCRole findById(Long id) {
		LogUtil.log("finding SysCRole instance with id: " + id, Level.INFO,
				null);
		try {
			SysCRole instance = entityManager.find(SysCRole.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all SysCRole entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the SysCRole property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<SysCRole> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<SysCRole> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding SysCRole instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from SysCRole model where model."
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

	public List<SysCRole> findByPropertys(String strWhere, Object[] o,
			final int... rowStartIdxAndCount) {
		try {
			final String queryString = "select model from SysCRole model where "+strWhere;
			Query query = entityManager.createQuery(queryString);
			for(int i =0 ;i<o.length;i++)
			{
				query.setParameter("param"+i, o[i]);
			}
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

	public List<SysCRole> findByRoleTypeId(Object roleTypeId,
			int... rowStartIdxAndCount) {
		return findByProperty(ROLE_TYPE_ID, roleTypeId, rowStartIdxAndCount);
	}

	public List<SysCRole> findByRoleName(Object roleName,
			int... rowStartIdxAndCount) {
		return findByProperty(ROLE_NAME, roleName, rowStartIdxAndCount);
	}

	public List<SysCRole> findByRoleStatus(Object roleStatus,
			int... rowStartIdxAndCount) {
		return findByProperty(ROLE_STATUS, roleStatus, rowStartIdxAndCount);
	}

	public List<SysCRole> findByMemo(Object memo, int... rowStartIdxAndCount) {
		return findByProperty(MEMO, memo, rowStartIdxAndCount);
	}

	public List<SysCRole> findByOrderBy(Object orderBy,
			int... rowStartIdxAndCount) {
		return findByProperty(ORDER_BY, orderBy, rowStartIdxAndCount);
	}

	public List<SysCRole> findByCreateBy(Object createBy,
			int... rowStartIdxAndCount) {
		return findByProperty(CREATE_BY, createBy, rowStartIdxAndCount);
	}

	public List<SysCRole> findByLastModifiyBy(Object lastModifiyBy,
			int... rowStartIdxAndCount) {
		return findByProperty(LAST_MODIFIY_BY, lastModifiyBy,
				rowStartIdxAndCount);
	}

	/**
	 * Find all SysCRole entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<SysCRole> all SysCRole entities
	 */
	@SuppressWarnings("unchecked")
	public List<SysCRole> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all SysCRole instances", Level.INFO, null);
		try {
			final String queryString = "select model from SysCRole model";
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