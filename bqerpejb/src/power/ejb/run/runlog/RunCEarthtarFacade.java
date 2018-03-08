package power.ejb.run.runlog;

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
 * Facade for entity RunCEarthtar.
 * 
 * @see power.ejb.run.runlog.RunCEarthtar
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCEarthtarFacade implements RunCEarthtarFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved RunCEarthtar entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            RunCEarthtar entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(RunCEarthtar entity) {
		LogUtil.log("saving RunCEarthtar instance", Level.INFO, null);
		try {
			if(entity.getEarthId() == null){
				entity.setEarthId(bll.getMaxId("run_c_earthtar", "earth_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent RunCEarthtar entity.
	 * 
	 * @param entity
	 *            RunCEarthtar entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(RunCEarthtar entity) {
		LogUtil.log("deleting RunCEarthtar instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCEarthtar.class, entity
					.getEarthId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved RunCEarthtar entity and return it or a copy of
	 * it to the sender. A copy of the RunCEarthtar entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            RunCEarthtar entity to update
	 * @returns RunCEarthtar the persisted RunCEarthtar entity instance, may not
	 *          be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public RunCEarthtar update(RunCEarthtar entity) {
		LogUtil.log("updating RunCEarthtar instance", Level.INFO, null);
		try {
			RunCEarthtar result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCEarthtar findById(Long id) {
		LogUtil.log("finding RunCEarthtar instance with id: " + id, Level.INFO,
				null);
		try {
			RunCEarthtar instance = entityManager.find(RunCEarthtar.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all RunCEarthtar entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the RunCEarthtar property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<RunCEarthtar> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<RunCEarthtar> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunCEarthtar instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunCEarthtar model where model."
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
	 * Find all RunCEarthtar entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<RunCEarthtar> all RunCEarthtar entities
	 */
	@SuppressWarnings("unchecked")
	public List<RunCEarthtar> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCEarthtar instances", Level.INFO, null);
		try {
			final String queryString = "select model from RunCEarthtar model";
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
	public List<RunCEarthtar> findList(String enterprisecode)
	{
		String sql="select r.*\n" +
			"  from run_c_earthtar r\n" + 
			" where r.enterprise_code = '"+enterprisecode+"'\n" + 
			"   and r.is_use = 'Y' order by r.display_no";
		return bll.queryByNativeSQL(sql, RunCEarthtar.class);
	}
}