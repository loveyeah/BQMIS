package power.ejb.productiontec.dependabilityAnalysis;

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
 * Facade for entity PtJHeaterParameter.
 * 
 * @see power.ejb.productiontec.dependabilityAnalysis.PtJHeaterParameter
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJHeaterParameterFacade implements PtJHeaterParameterFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	
	public PtJHeaterParameter save(PtJHeaterParameter entity) {
		try {
			entity.setHeaterId(bll.getMaxId("PT_J_HEATER_PARAMETER", "HEATER_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(PtJHeaterParameter entity) {
		LogUtil.log("deleting PtJHeaterParameter instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(PtJHeaterParameter.class,
					entity.getHeaterId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PtJHeaterParameter update(PtJHeaterParameter entity) {
		LogUtil.log("updating PtJHeaterParameter instance", Level.INFO, null);
		try {
			PtJHeaterParameter result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJHeaterParameter findById(Long id) {
		LogUtil.log("finding PtJHeaterParameter instance with id: " + id,
				Level.INFO, null);
		try {
			PtJHeaterParameter instance = entityManager.find(
					PtJHeaterParameter.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<PtJHeaterParameter> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PtJHeaterParameter instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PtJHeaterParameter model where model."
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
	public List<PtJHeaterParameter> findAll() {
		LogUtil.log("finding all PtJHeaterParameter instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from PtJHeaterParameter model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<PtJHeaterParameter> findInfoBypId(String pId) {
		String sql =  "select t.* from pt_j_heater_parameter t where t.auxiliary_id = '"
			+ pId + "'";
		List<PtJHeaterParameter> list = bll.queryByNativeSQL(sql,PtJHeaterParameter.class);
		return list;
	}

}