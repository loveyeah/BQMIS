package power.ejb.productiontec.retrenchenergy;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;


@Stateless
public class PtJnjdJJnzjFacade implements PtJnjdJJnzjFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public PtJnjdJJnzj save(PtJnjdJJnzj entity) {
		LogUtil.log("saving PtJnjdJJnzj instance", Level.INFO, null);
		try {
			entity.setSummaryId(bll.getMaxId("PT_JNJD_J_JNZJ", "summary_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public PtJnjdJJnzj update(PtJnjdJJnzj entity) {
		LogUtil.log("updating PtJnjdJJnzj instance", Level.INFO, null);
		try {
			PtJnjdJJnzj result = entityManager.merge(entity);
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
			"delete PT_JNJD_J_JNZJ t\n" +
			"where t.summary_id in ("+ids+")";
         bll.exeNativeSQL(sql);
	}

	public PtJnjdJJnzj findById(Long id) {
		LogUtil.log("finding PtJnjdJJnzj instance with id: " + id, Level.INFO,
				null);
		try {
			PtJnjdJJnzj instance = entityManager.find(PtJnjdJJnzj.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String dateYear,String enterpriseCode,final int... rowStartIdxAndCount) {
		String sqlCount=
			"select count(*) from PT_JNJD_J_JNZJ  t\n" +
			"where to_char(t.year,'yyyy')='"+dateYear+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'";

		Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
		if(totalCount>0)
		{
			PageObject obj=new PageObject();
			obj.setTotalCount(totalCount);
			String sql=
				"select * from PT_JNJD_J_JNZJ  t\n" +
				"where to_char(t.year,'yyyy')='"+dateYear+"'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'";
			List<PtJnjdJJnzj> list=bll.queryByNativeSQL(sql, PtJnjdJJnzj.class, rowStartIdxAndCount);
			obj.setList(list);
			return obj;
		}
		else
		{
		return null;
		}
	}

}