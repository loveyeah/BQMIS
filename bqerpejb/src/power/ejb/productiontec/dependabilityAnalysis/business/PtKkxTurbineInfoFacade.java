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
 * Facade for entity PtKkxTurbineInfo.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.business.PtKkxTurbineInfo
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtKkxTurbineInfoFacade implements PtKkxTurbineInfoFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;

	public void save(PtKkxTurbineInfo entity) {
		LogUtil.log("saving PtKkxTurbineInfo instance", Level.INFO, null);
		try {
			entity.setTurbineId(bll.getMaxId("PT_KKX_TURBINE_INFO", "TURBINE_ID"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(PtKkxTurbineInfo entity) {
		LogUtil.log("deleting PtKkxTurbineInfo instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtKkxTurbineInfo.class, entity
					.getTurbineId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PtKkxTurbineInfo update(PtKkxTurbineInfo entity) {
		LogUtil.log("updating PtKkxTurbineInfo instance", Level.INFO, null);
		try {
			PtKkxTurbineInfo result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtKkxTurbineInfo findById(Long id) {
		LogUtil.log("finding PtKkxTurbineInfo instance with id: " + id,
				Level.INFO, null);
		try {
			PtKkxTurbineInfo instance = entityManager.find(
					PtKkxTurbineInfo.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtKkxTurbineInfo> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PtKkxTurbineInfo instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtKkxTurbineInfo model where model."
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
	public List<PtKkxTurbineInfo> findAll() {
		LogUtil.log("finding all PtKkxTurbineInfo instances", Level.INFO, null);
		try {
			final String queryString = "select model from PtKkxTurbineInfo model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

}