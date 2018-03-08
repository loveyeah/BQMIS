package power.ejb.message;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity SysJMessage.
 * 
 * @see power.ejb.message.SysJMessage
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class SysJMessageFacade implements SysJMessageFacadeRemote {
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	public Long save(SysJMessage entity) {
		LogUtil.log("saving SysJMessage instance", Level.INFO, null);
		try {
			if( entity.getMessageId() == null){
				entity.setMessageId(bll.getMaxId("SYS_J_MESSAGE", "message_id"));
			}
			entity.setIsUse("Y");
			entityManager.persist(entity);
			return entity.getMessageId();
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void delete(SysJMessage entity) {
		entity.setIsUse("N");
		this.update(entity);
	}

	public SysJMessage update(SysJMessage entity) {
		LogUtil.log("updating SysJMessage instance", Level.INFO, null);
		try {
			SysJMessage result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SysJMessage findById(Long id) {
		LogUtil.log("finding SysJMessage instance with id: " + id, Level.INFO,
				null);
		try {
			SysJMessage instance = entityManager.find(SysJMessage.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
}