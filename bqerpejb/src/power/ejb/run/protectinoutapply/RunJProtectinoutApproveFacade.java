package power.ejb.run.protectinoutapply;

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
 * Facade for entity RunJProtectinoutApprove.
 * 
 * @see power.ejb.run.protectinoutapply.RunJProtectinoutApprove
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJProtectinoutApproveFacade implements
		RunJProtectinoutApproveFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;


	public void save(RunJProtectinoutApprove entity) {
		LogUtil
				.log("saving RunJProtectinoutApprove instance", Level.INFO,
						null);
		try {
			entity.setApproveId(bll.getMaxId("run_j_protectinout_approve", "approve_id"));
			entity.setApproveDate(new java.util.Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(RunJProtectinoutApprove entity) {
		LogUtil.log("deleting RunJProtectinoutApprove instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(RunJProtectinoutApprove.class,
					entity.getApproveId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public RunJProtectinoutApprove update(RunJProtectinoutApprove entity) {
		LogUtil.log("updating RunJProtectinoutApprove instance", Level.INFO,
				null);
		try {
			RunJProtectinoutApprove result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJProtectinoutApprove findById(Long id) {
		LogUtil.log("finding RunJProtectinoutApprove instance with id: " + id,
				Level.INFO, null);
		try {
			RunJProtectinoutApprove instance = entityManager.find(
					RunJProtectinoutApprove.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	


}