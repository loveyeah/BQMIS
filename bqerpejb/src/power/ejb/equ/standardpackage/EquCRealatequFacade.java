package power.ejb.equ.standardpackage;

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

/**
 * Facade for entity EquCRealatequ.
 * 
 * @see power.ejb.equ.standardpackage.EquCRealatequ
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCRealatequFacade implements EquCRealatequFacadeRemote {
	// property constants
	public static final String CODE = "code";
	public static final String ATTRIBUTE_CODE = "attributeCode";
	public static final String STATUS = "status";
	public static final String ENTERPRISECODE = "enterprisecode";
	public static final String IS_USE = "isUse";

	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved EquCRealatequ entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquCRealatequ entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public long save(EquCRealatequ entity) {
		try {
			if (entity.getEquId() == null) {
				entity.setEquId(dll.getMaxId("equ_c_realatequ", "EQU_ID"));
			}
			entityManager.persist(entity);
			return 0;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent EquCRealatequ entity.
	 * 
	 * @param entity
	 *            EquCRealatequ entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquCRealatequ entity) {
		LogUtil.log("deleting EquCRealatequ instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquCRealatequ.class, entity
					.getEquId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved EquCRealatequ entity and return it or a copy
	 * of it to the sender. A copy of the EquCRealatequ entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquCRealatequ entity to update
	 * @return EquCRealatequ the persisted EquCRealatequ entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquCRealatequ update(EquCRealatequ entity) {
		LogUtil.log("updating EquCRealatequ instance", Level.INFO, null);
		try {
			EquCRealatequ result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCRealatequ findById(Long id) {
		LogUtil.log("finding EquCRealatequ instance with id: " + id,
				Level.INFO, null);
		try {
			EquCRealatequ instance = entityManager
					.find(EquCRealatequ.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquCRealatequ entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquCRealatequ property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<EquCRealatequ> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquCRealatequ> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding EquCRealatequ instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquCRealatequ model where model."
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

	public List<EquCRealatequ> findByCode(Object code,
			int... rowStartIdxAndCount) {
		return findByProperty(CODE, code, rowStartIdxAndCount);
	}

	public List<EquCRealatequ> findByAttributeCode(Object attributeCode,
			int... rowStartIdxAndCount) {
		return findByProperty(ATTRIBUTE_CODE, attributeCode,
				rowStartIdxAndCount);
	}

	public List<EquCRealatequ> findByStatus(Object status,
			int... rowStartIdxAndCount) {
		return findByProperty(STATUS, status, rowStartIdxAndCount);
	}

	public List<EquCRealatequ> findByEnterprisecode(Object enterprisecode,
			int... rowStartIdxAndCount) {
		return findByProperty(ENTERPRISECODE, enterprisecode,
				rowStartIdxAndCount);
	}

	public List<EquCRealatequ> findByIsUse(Object isUse,
			int... rowStartIdxAndCount) {
		return findByProperty(IS_USE, isUse, rowStartIdxAndCount);
	}

	@SuppressWarnings("unchecked")
	public PageObject findByIsuse(Object isUse, String code,
			int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select t.*,GETEQUNAMEBYCODE(t.attribute_code) equname from equ_c_realatequ t where t.IS_USE='Y' and t.code='"
					+ code + "' order by equ_id desc";
			List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
			result.setList(list);
			return result;
		} catch (RuntimeException e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
			throw e;
		}
	}

	/**
	 * Find all EquCRealatequ entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquCRealatequ> all EquCRealatequ entities
	 */
	@SuppressWarnings("unchecked")
	public List<EquCRealatequ> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCRealatequ instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCRealatequ model";
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