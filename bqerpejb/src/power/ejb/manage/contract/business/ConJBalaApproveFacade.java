package power.ejb.manage.contract.business;

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
 * Facade for entity ConJBalaApprove.
 * 
 * @see power.ejb.manage.contract.business.ConJBalaApprove
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class ConJBalaApproveFacade implements ConJBalaApproveFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * Perform an initial save of a previously unsaved ConJBalaApprove entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            ConJBalaApprove entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void save(ConJBalaApprove entity) {
		LogUtil.log("saving ConJBalaApprove instance", Level.INFO, null);
		try {
			if(entity.getApproveId() == null)
			{
				entity.setApproveId(bll.getMaxId("CON_J_BALA_APPROVE", "approve_id"));
			}
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent ConJBalaApprove entity.
	 * 
	 * @param entity
	 *            ConJBalaApprove entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(ConJBalaApprove entity) {
		LogUtil.log("deleting ConJBalaApprove instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(ConJBalaApprove.class, entity
					.getApproveId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved ConJBalaApprove entity and return it or a copy
	 * of it to the sender. A copy of the ConJBalaApprove entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            ConJBalaApprove entity to update
	 * @return ConJBalaApprove the persisted ConJBalaApprove entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public ConJBalaApprove update(ConJBalaApprove entity) {
		LogUtil.log("updating ConJBalaApprove instance", Level.INFO, null);
		try {
			ConJBalaApprove result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ConJBalaApprove findById(Long id) {
		LogUtil.log("finding ConJBalaApprove instance with id: " + id,
				Level.INFO, null);
		try {
			ConJBalaApprove instance = entityManager.find(
					ConJBalaApprove.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all ConJBalaApprove entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the ConJBalaApprove property to query
	 * @param value
	 *            the property value to match
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            number of results to return.
	 * @return List<ConJBalaApprove> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<ConJBalaApprove> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding ConJBalaApprove instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from ConJBalaApprove model where model."
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
	 * Find all ConJBalaApprove entities.
	 * 
	 * @param rowStartIdxAndCount
	 *            Optional int varargs. rowStartIdxAndCount[0] specifies the the
	 *            row index in the query result-set to begin collecting the
	 *            results. rowStartIdxAndCount[1] specifies the the maximum
	 *            count of results to return.
	 * @return List<ConJBalaApprove> all ConJBalaApprove entities
	 */
	@SuppressWarnings("unchecked")
	public List<ConJBalaApprove> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all ConJBalaApprove instances", Level.INFO, null);
		try {
			final String queryString = "select model from ConJBalaApprove model";
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