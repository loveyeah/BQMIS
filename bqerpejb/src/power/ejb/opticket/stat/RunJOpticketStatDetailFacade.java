package power.ejb.opticket.stat;

import java.util.List;
import java.util.logging.Level;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunJOpticketStatDetail.
 * 
 * @see power.ejb.opticket.stat.RunJOpticketStatDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJOpticketStatDetailFacade implements
		RunJOpticketStatDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager; 
	public void save(RunJOpticketStatDetail entity) {
		LogUtil.log("saving RunJOpticketStatDetail instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	} 
	public void delete(RunJOpticketStatDetail entity) {
		LogUtil.log("deleting RunJOpticketStatDetail instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(RunJOpticketStatDetail.class,
					entity.getId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	} 
	public RunJOpticketStatDetail update(RunJOpticketStatDetail entity) {
		LogUtil.log("updating RunJOpticketStatDetail instance", Level.INFO,
				null);
		try {
			RunJOpticketStatDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	} 
	public RunJOpticketStatDetail findById(Long id) {
		LogUtil.log("finding RunJOpticketStatDetail instance with id: " + id,
				Level.INFO, null);
		try {
			RunJOpticketStatDetail instance = entityManager.find(
					RunJOpticketStatDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	} 
}