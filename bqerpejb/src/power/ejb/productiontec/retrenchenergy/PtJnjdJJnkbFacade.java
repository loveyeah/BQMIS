package power.ejb.productiontec.retrenchenergy;

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
public class PtJnjdJJnkbFacade implements PtJnjdJJnkbFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public PtJnjdJJnkb save(PtJnjdJJnkb entity) {
		LogUtil.log("saving PtJnjdJJnkb instance", Level.INFO, null);
		try {
			entity.setJnkbId(bll.getMaxId("PT_JNJD_J_JNKB", "jnkb_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	

	
	public PtJnjdJJnkb update(PtJnjdJJnkb entity) {
		LogUtil.log("updating PtJnjdJJnkb instance", Level.INFO, null);
		try {
			PtJnjdJJnkb result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteMulti(String ids)
	{
		String sql=
			"delete PT_JNJD_J_JNKB t\n" +
			"where t.jnkb_id in ("+ids+")";
		bll.exeNativeSQL(sql);

	}

	public PtJnjdJJnkb findById(Long id) {
		LogUtil.log("finding PtJnjdJJnkb instance with id: " + id, Level.INFO,
				null);
		try {
			PtJnjdJJnkb instance = entityManager.find(PtJnjdJJnkb.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String mainTopic,String enterpriseCode,final int... rowStartIdxAndCount) {
		String sqlCount=
			"select count(*) from PT_JNJD_J_JNKB t\n" +
			"where t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.main_topic like '%"+mainTopic+"%'";
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(totalCount>0)
		{
			PageObject obj=new PageObject();
			obj.setTotalCount(totalCount);
			String sql=
				"select * from PT_JNJD_J_JNKB t\n" +
			"where t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.main_topic like '%"+mainTopic+"%'";
			List<PtJnjdJJnkb> list=bll.queryByNativeSQL(sql, PtJnjdJJnkb.class, rowStartIdxAndCount);
			obj.setList(list);
			return obj;
		}
		else
		{
			return null;
		}

	}

}