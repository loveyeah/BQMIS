package power.ejb.run.runlog.shift;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunJEquRealtimeStatus.
 * 
 * @see power.ejb.run.runlog.shift.RunJEquRealtimeStatus
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJEquRealtimeStatusFacade implements
		RunJEquRealtimeStatusFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * Perform an initial save of a previously unsaved RunJEquRealtimeStatus
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            RunJEquRealtimeStatus entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunJEquRealtimeStatus entity) {
		LogUtil.log("saving RunJEquRealtimeStatus instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunJEquRealtimeStatus entity.
	 * 
	 * @param entity
	 *            RunJEquRealtimeStatus entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunJEquRealtimeStatus entity) {
		LogUtil
				.log("deleting RunJEquRealtimeStatus instance", Level.INFO,
						null);
		try {
			entity = entityManager.getReference(RunJEquRealtimeStatus.class,
					entity.getRealtimeStatusId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunJEquRealtimeStatus entity and return it or
	 * a copy of it to the sender. A copy of the RunJEquRealtimeStatus entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            RunJEquRealtimeStatus entity to update
	 * @return RunJEquRealtimeStatus the persisted RunJEquRealtimeStatus entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunJEquRealtimeStatus update(RunJEquRealtimeStatus entity) {
		LogUtil
				.log("updating RunJEquRealtimeStatus instance", Level.INFO,
						null);
		try {
			RunJEquRealtimeStatus result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJEquRealtimeStatus findById(Long id) {
		LogUtil.log("finding RunJEquRealtimeStatus instance with id: " + id,
				Level.INFO, null);
		try {
			RunJEquRealtimeStatus instance = entityManager.find(
					RunJEquRealtimeStatus.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunJEquRealtimeStatus entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunJEquRealtimeStatus property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunJEquRealtimeStatus> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunJEquRealtimeStatus> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunJEquRealtimeStatus instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunJEquRealtimeStatus model where model."
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
	 * Find all RunJEquRealtimeStatus entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunJEquRealtimeStatus> all RunJEquRealtimeStatus entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunJEquRealtimeStatus> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJEquRealtimeStatus instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from RunJEquRealtimeStatus model";
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