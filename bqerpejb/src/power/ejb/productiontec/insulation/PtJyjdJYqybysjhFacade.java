package power.ejb.productiontec.insulation;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity PtJyjdJYqybysjh.
 * 
 * @see power.ejb.productiontec.insulation.PtJyjdJYqybysjh
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJyjdJYqybysjhFacade implements PtJyjdJYqybysjhFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved PtJyjdJYqybysjh entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtJyjdJYqybysjh entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtJyjdJYqybysjh entity) {
		LogUtil.log("saving PtJyjdJYqybysjh instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtJyjdJYqybysjh entity.
	 * 
	 * @param entity
	 *            PtJyjdJYqybysjh entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtJyjdJYqybysjh entity) {
		LogUtil.log("deleting PtJyjdJYqybysjh instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJyjdJYqybysjh.class, entity
					.getYqybysjhId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtJyjdJYqybysjh entity and return it or a copy
	 * of it to the sender. A copy of the PtJyjdJYqybysjh entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            PtJyjdJYqybysjh entity to update
	 * @return PtJyjdJYqybysjh the persisted PtJyjdJYqybysjh entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtJyjdJYqybysjh update(PtJyjdJYqybysjh entity) {
		LogUtil.log("updating PtJyjdJYqybysjh instance", Level.INFO, null);
		try {
			PtJyjdJYqybysjh result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJyjdJYqybysjh findById(Long id) {
		LogUtil.log("finding PtJyjdJYqybysjh instance with id: " + id,
				Level.INFO, null);
		try {
			PtJyjdJYqybysjh instance = entityManager.find(
					PtJyjdJYqybysjh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtJyjdJYqybysjh entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtJyjdJYqybysjh property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<PtJyjdJYqybysjh> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtJyjdJYqybysjh> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJyjdJYqybysjh instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJyjdJYqybysjh model where model."
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
	 * Find all PtJyjdJYqybysjh entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtJyjdJYqybysjh> all PtJyjdJYqybysjh entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtJyjdJYqybysjh> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJyjdJYqybysjh instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtJyjdJYqybysjh model";
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