package power.ejb.workticket.business;

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
 * Facade for entity RunJWorkticketMeasure.
 * 
 * @see power.ejb.workticket.business.RunJWorkticketMeasure
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJWorkticketMeasureFacade implements
		RunJWorkticketMeasureFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public RunJWorkticketMeasure save(RunJWorkticketMeasure entity) {
		LogUtil.log("saving RunJWorkticketMeasure instance", Level.INFO, null);
		try {
			entity.setMeasureId(bll.getMaxId("RUN_J_WORKTICKET_MEASURE", "measure_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			
			LogUtil.log("save successful", Level.INFO, null);
		    return	entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(Long id) {
		RunJWorkticketMeasure entity=this.findById(id);
		entity.setIsUse("N");
		this.update(entity);
	
	}

	
	public RunJWorkticketMeasure update(RunJWorkticketMeasure entity) {
		LogUtil
				.log("updating RunJWorkticketMeasure instance", Level.INFO,
						null);
		try {
			RunJWorkticketMeasure result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJWorkticketMeasure findById(Long id) {
		LogUtil.log("finding RunJWorkticketMeasure instance with id: " + id,
				Level.INFO, null);
		try {
			RunJWorkticketMeasure instance = entityManager.find(
					RunJWorkticketMeasure.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
}