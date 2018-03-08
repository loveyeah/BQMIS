package power.ejb.equ.workbill;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquJSafetyrules.
 * 
 * @see power.ejb.equ.workbill.EquJSafetyrules
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJSafetyrulesFacade implements EquJSafetyrulesFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved EquJSafetyrules entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            EquJSafetyrules entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(EquJSafetyrules entity) {
		LogUtil.log("saving EquJSafetyrules instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent EquJSafetyrules entity.
	 * 
	 * @param entity
	 *            EquJSafetyrules entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJSafetyrules entity) {
		LogUtil.log("deleting EquJSafetyrules instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquJSafetyrules.class, entity
					.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved EquJSafetyrules entity and return it or a copy
	 * of it to the sender. A copy of the EquJSafetyrules entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            EquJSafetyrules entity to update
	 * @return EquJSafetyrules the persisted EquJSafetyrules entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJSafetyrules update(EquJSafetyrules entity) {
		LogUtil.log("updating EquJSafetyrules instance", Level.INFO, null);
		try {
			EquJSafetyrules result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJSafetyrules findById(Long id) {
		LogUtil.log("finding EquJSafetyrules instance with id: " + id,
				Level.INFO, null);
		try {
			EquJSafetyrules instance = entityManager.find(
					EquJSafetyrules.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all EquJSafetyrules entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the EquJSafetyrules property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<EquJSafetyrules> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<EquJSafetyrules> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding EquJSafetyrules instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from EquJSafetyrules model where model."
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
	 * Find all EquJSafetyrules entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<EquJSafetyrules> all EquJSafetyrules entities
	 */
	@SuppressWarnings("unchecked")
	public List<EquJSafetyrules> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquJSafetyrules instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquJSafetyrules model";
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