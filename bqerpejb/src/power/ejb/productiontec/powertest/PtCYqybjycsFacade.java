package power.ejb.productiontec.powertest;

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
 * Facade for entity PtCYqybjycs.
 * 
 * @see power.ejb.productiontec.powertest.PtCYqybjycs
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtCYqybjycsFacade implements PtCYqybjycsFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved PtCYqybjycs entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PtCYqybjycs entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(PtCYqybjycs entity) {
		LogUtil.log("saving PtCYqybjycs instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PtCYqybjycs entity.
	 * 
	 * @param entity
	 *            PtCYqybjycs entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(PtCYqybjycs entity) {
		LogUtil.log("deleting PtCYqybjycs instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtCYqybjycs.class, entity
					.getParameterId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved PtCYqybjycs entity and return it or a copy of
	 * it to the sender. A copy of the PtCYqybjycs entity parameter is returned
	 * when the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PtCYqybjycs entity to update
	 * @return PtCYqybjycs the persisted PtCYqybjycs entity instance, may not be
	 *         the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PtCYqybjycs update(PtCYqybjycs entity) {
		LogUtil.log("updating PtCYqybjycs instance", Level.INFO, null);
		try {
			PtCYqybjycs result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtCYqybjycs findById(Long id) {
		LogUtil.log("finding PtCYqybjycs instance with id: " + id, Level.INFO,
				null);
		try {
			PtCYqybjycs instance = entityManager.find(PtCYqybjycs.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PtCYqybjycs entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PtCYqybjycs property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<PtCYqybjycs> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PtCYqybjycs> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtCYqybjycs instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtCYqybjycs model where model."
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
	 * Find all PtCYqybjycs entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<PtCYqybjycs> all PtCYqybjycs entities
	 */
	@SuppressWarnings("unchecked")
	public List<PtCYqybjycs> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtCYqybjycs instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtCYqybjycs model";
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

	public void delete(String ids) {
		String sqlString = "delete from pt_c_yqybjycs  s "
				+ " where s.parameter_id in (" + ids + ")";
		bll.exeNativeSQL(sqlString);
	}

	public void save(List<PtCYqybjycs> addList, List<PtCYqybjycs> updateList,
			String deleteId) {
		if (addList.size() > 0) {
			Long idLong = bll.getMaxId("pt_c_yqybjycs ", "parameter_id");
			for (PtCYqybjycs entity : addList) {
				entity.setParameterId(idLong++);
				this.save(entity);
			}
		}
		if (updateList.size() > 0) {
			for (PtCYqybjycs entity : updateList) {
				this.update(entity);
			}

		}
		if (deleteId.length() > 0)
			this.delete(deleteId);
	}

	@SuppressWarnings("unchecked")
	public List<PtCYqybjycs> findByLb(Long jdzyId, Long yqyblbId,
			String enterpriseCode) {
		String sqlString = " select t.* from pt_c_yqybjycs t"
				+ " where t.yqyblb_id= " + yqyblbId + " and t.jdzy_id="
				+ jdzyId + " and t.enterprise_code='" + enterpriseCode + "'";
		List<PtCYqybjycs> list = bll.queryByNativeSQL(sqlString,
				PtCYqybjycs.class);
		return list;

	}
}