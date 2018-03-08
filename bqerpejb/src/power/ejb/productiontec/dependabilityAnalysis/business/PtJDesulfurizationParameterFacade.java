package power.ejb.productiontec.dependabilityAnalysis.business;

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
 * 
 */
@Stateless
public class PtJDesulfurizationParameterFacade implements
		PtJDesulfurizationParameterFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	
	public void save(PtJDesulfurizationParameter entity) {
		LogUtil.log("saving PtJDesulfurizationParameter instance", Level.INFO,
				null);
		try {
			entity.setDesulfurizationId(bll.getMaxId("PT_J_DESULFURIZATION_PARAMETER", "DESULFURIZATION_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(PtJDesulfurizationParameter entity) {
		LogUtil.log("deleting PtJDesulfurizationParameter instance",
				Level.INFO, null);
		try {
			entity = entityManager.getReference(
					PtJDesulfurizationParameter.class, entity
							.getDesulfurizationId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PtJDesulfurizationParameter update(PtJDesulfurizationParameter entity) {
		LogUtil.log("updating PtJDesulfurizationParameter instance",
				Level.INFO, null);
		try {
			PtJDesulfurizationParameter result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJDesulfurizationParameter findById(Long id) {
		LogUtil.log("finding PtJDesulfurizationParameter instance with id: "
				+ id, Level.INFO, null);
		try {
			PtJDesulfurizationParameter instance = entityManager.find(
					PtJDesulfurizationParameter.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtJDesulfurizationParameter> findByProperty(
			String propertyName, final Object value,
			final int... rowStartIdxAndCount) {
		LogUtil.log(
				"finding PtJDesulfurizationParameter instance with property: "
						+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJDesulfurizationParameter model where model."
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

	
	@SuppressWarnings("unchecked")
	public List<PtJDesulfurizationParameter> findAll(
			final int... rowStartIdxAndCount) {
		LogUtil.log("finding all PtJDesulfurizationParameter instances",
				Level.INFO, null);
		try {
			final String queryString = "select model from PtJDesulfurizationParameter model";
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