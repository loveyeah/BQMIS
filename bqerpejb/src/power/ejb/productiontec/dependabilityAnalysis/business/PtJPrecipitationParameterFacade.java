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
 * Facade for entity PtJPrecipitationParameter.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.business.PtJPrecipitationParameter
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJPrecipitationParameterFacade implements
		PtJPrecipitationParameterFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public void save(PtJPrecipitationParameter entity) {
		LogUtil.log("saving PtJPrecipitationParameter instance", Level.INFO,
				null);
		try {
			entity.setPrecipitationId(bll.getMaxId("PT_J_PRECIPITATION_PARAMETER", "precipitation_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJPrecipitationParameter update(PtJPrecipitationParameter entity) {
		LogUtil.log("updating PtJPrecipitationParameter instance", Level.INFO,
				null);
		try {
			PtJPrecipitationParameter result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJPrecipitationParameter findById(Long id) {
		LogUtil.log(
				"finding PtJPrecipitationParameter instance with id: " + id,
				Level.INFO, null);
		try {
			PtJPrecipitationParameter instance = entityManager.find(
					PtJPrecipitationParameter.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<PtJPrecipitationParameter> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log(
				"finding PtJPrecipitationParameter instance with property: "
						+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJPrecipitationParameter model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}
}