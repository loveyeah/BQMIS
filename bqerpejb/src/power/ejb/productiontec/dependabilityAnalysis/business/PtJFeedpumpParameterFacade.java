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
 * Facade for entity PtJFeedpumpParameter.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.business.PtJFeedpumpParameter
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJFeedpumpParameterFacade implements
		PtJFeedpumpParameterFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public void save(PtJFeedpumpParameter entity) {
		LogUtil.log("saving PtJFeedpumpParameter instance", Level.INFO, null);
		try {
			entity.setFeedpumpId(bll.getMaxId("PT_J_FEEDPUMP_PARAMETER", "feedpump_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	 public void deleteMulti(String ids){
			String sql  = "update PT_J_FEEDPUMP_PARAMETER a\n" +
				"set a.is_use = 'N'\n" + 
				" where a.feedpump_id in ("+ids+")";
			
			bll.exeNativeSQL(sql);
		}

	public PtJFeedpumpParameter update(PtJFeedpumpParameter entity) {
		LogUtil.log("updating PtJFeedpumpParameter instance", Level.INFO, null);
		try {
			PtJFeedpumpParameter result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJFeedpumpParameter findById(Long id) {
		LogUtil.log("finding PtJFeedpumpParameter instance with id: " + id,
				Level.INFO, null);
		try {
			PtJFeedpumpParameter instance = entityManager.find(
					PtJFeedpumpParameter.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PtJFeedpumpParameter> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding PtJFeedpumpParameter instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJFeedpumpParameter model where model."
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

}