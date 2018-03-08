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
 * Facade for entity PtJBlowerParameter.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.business.PtJBlowerParameter
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJBlowerParameterFacade implements PtJBlowerParameterFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;


	public void save(PtJBlowerParameter entity) {
		LogUtil.log("saving PtJBlowerParameter instance", Level.INFO, null);
		try {
			entity.setBlowerId(bll.getMaxId("PT_J_BLOWER_PARAMETER", "BLOWER_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(PtJBlowerParameter entity) {
		LogUtil.log("deleting PtJBlowerParameter instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJBlowerParameter.class,
					entity.getBlowerId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PtJBlowerParameter update(PtJBlowerParameter entity) {
		LogUtil.log("updating PtJBlowerParameter instance", Level.INFO, null);
		try {
			PtJBlowerParameter result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJBlowerParameter findById(Long id) {
		LogUtil.log("finding PtJBlowerParameter instance with id: " + id,
				Level.INFO, null);
		try {
			PtJBlowerParameter instance = entityManager.find(
					PtJBlowerParameter.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtJBlowerParameter> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PtJBlowerParameter instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJBlowerParameter model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtJBlowerParameter> findAll() {
		LogUtil.log("finding all PtJBlowerParameter instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from PtJBlowerParameter model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}