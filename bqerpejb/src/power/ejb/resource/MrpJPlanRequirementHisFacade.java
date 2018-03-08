package power.ejb.resource;

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
 * Facade for entity MrpJPlanRequirementHis.
 * 
 * @see power.ejb.resource.MrpJPlanRequirementHis
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class MrpJPlanRequirementHisFacade implements
		MrpJPlanRequirementHisFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	 @EJB(beanName = "NativeSqlHelper")
	 protected NativeSqlHelperRemote bll;

	public void save(MrpJPlanRequirementHis entity) {
		LogUtil.log("saving MrpJPlanRequirementHis instance", Level.INFO, null);
		try {
			entity.setApproveId(bll.getMaxId("MRP_J_PLAN_REQUIREMENT_HIS", "approve_id"));
			entity.setApproveDate(new java.util.Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(MrpJPlanRequirementHis entity) {
		LogUtil.log("deleting MrpJPlanRequirementHis instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(MrpJPlanRequirementHis.class,
					entity.getApproveId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public MrpJPlanRequirementHis update(MrpJPlanRequirementHis entity) {
		LogUtil.log("updating MrpJPlanRequirementHis instance", Level.INFO,
				null);
		try {
			MrpJPlanRequirementHis result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public MrpJPlanRequirementHis findById(Long id) {
		LogUtil.log("finding MrpJPlanRequirementHis instance with id: " + id,
				Level.INFO, null);
		try {
			MrpJPlanRequirementHis instance = entityManager.find(
					MrpJPlanRequirementHis.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

}