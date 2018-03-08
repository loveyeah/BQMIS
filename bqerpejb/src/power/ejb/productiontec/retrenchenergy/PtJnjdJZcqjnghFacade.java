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
public class PtJnjdJZcqjnghFacade implements PtJnjdJZcqjnghFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public PtJnjdJZcqjngh save(PtJnjdJZcqjngh entity) {
		LogUtil.log("saving PtJnjdJZcqjngh instance", Level.INFO, null);
		try {
			entity.setJnghzdId(bll.getMaxId("PT_JNJD_J_ZCQJNGH", "jnghzd_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PtJnjdJZcqjngh update(PtJnjdJZcqjngh entity) {
		LogUtil.log("updating PtJnjdJZcqjngh instance", Level.INFO, null);
		try {
			PtJnjdJZcqjngh result = entityManager.merge(entity);
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
			"delete PT_JNJD_J_ZCQJNGH t\n" +
			"where t.jnghzd_id in ("+ids+")";
		bll.exeNativeSQL(sql);

	}

	public PtJnjdJZcqjngh findById(Long id) {
		LogUtil.log("finding PtJnjdJZcqjngh instance with id: " + id,
				Level.INFO, null);
		try {
			PtJnjdJZcqjngh instance = entityManager.find(PtJnjdJZcqjngh.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String strYear,String enterpriseCode,final int... rowStartIdxAndCount) {
		String sqlCount=
			"select count(*) from PT_JNJD_J_ZCQJNGH t\n" +
			"where to_char(t.common_date,'yyyy')='"+strYear+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'";
		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(totalCount>0)
		{
			PageObject obj=new PageObject();
			obj.setTotalCount(totalCount);
			String sql=
				"select * from PT_JNJD_J_ZCQJNGH t\n" +
				"where to_char(t.common_date,'yyyy')='"+strYear+"'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'";
			List<PtJnjdJZcqjngh> list=bll.queryByNativeSQL(sql, PtJnjdJZcqjngh.class, rowStartIdxAndCount);
			obj.setList(list);
			return obj;
		}
		else
		{
			return null;
		}

	}

}