package power.ejb.run.securityproduction.safesupervise;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;


@Stateless
public class SpCDynamicCheckMainFacade implements
		SpCDynamicCheckMainFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public SpCDynamicCheckMain save(SpCDynamicCheckMain entity) {
		LogUtil.log("saving SpCDynamicCheckMain instance", Level.INFO, null);
		try {
			
			entity.setMainId(bll.getMaxId("SP_C_DYNAMIC_CHECK_MAIN", "main_id"));
			entity.setIsUse("Y");
			entity.setEntryDate(new java.util.Date());
			entity.setStatus("1");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(String ids) {
	   String sql=
		   "update SP_C_DYNAMIC_CHECK_MAIN t\n" +
		   "   set t.is_use = 'N'\n" + 
		   " where t.main_id in ("+ids+")\n" + 
		   "   and t.is_use = 'Y'";
	   bll.exeNativeSQL(sql);
	}

	
	public SpCDynamicCheckMain update(SpCDynamicCheckMain entity) {
		LogUtil.log("updating SpCDynamicCheckMain instance", Level.INFO, null);
		try {
			SpCDynamicCheckMain result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public SpCDynamicCheckMain findById(Long id) {
		LogUtil.log("finding SpCDynamicCheckMain instance with id: " + id,
				Level.INFO, null);
		try {
			SpCDynamicCheckMain instance = entityManager.find(
					SpCDynamicCheckMain.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}


}