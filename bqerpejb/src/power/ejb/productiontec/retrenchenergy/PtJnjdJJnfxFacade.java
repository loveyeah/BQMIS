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
public class PtJnjdJJnfxFacade implements PtJnjdJJnfxFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public PtJnjdJJnfx save(PtJnjdJJnfx entity) {
		LogUtil.log("saving PtJnjdJJnfx instance", Level.INFO, null);
		try {
			entity.setAnalyseId(bll.getMaxId("PT_JNJD_J_JNFX", "analyse_id"));
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

  public void deleteMulti(String ids)
  {
	  String sql=
		  "delete PT_JNJD_J_JNFX t\n" +
		  "where t.analyse_id in ("+ids+")";
	  bll.exeNativeSQL(sql);

  }

	
	public PtJnjdJJnfx update(PtJnjdJJnfx entity) {
		LogUtil.log("updating PtJnjdJJnfx instance", Level.INFO, null);
		try {
			PtJnjdJJnfx result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJnjdJJnfx findById(Long id) {
		LogUtil.log("finding PtJnjdJJnfx instance with id: " + id, Level.INFO,
				null);
		try {
			PtJnjdJJnfx instance = entityManager.find(PtJnjdJJnfx.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public PageObject findAll(String dateMonth,String enterpriseCode,final int... rowStartIdxAndCount) {
	
		String sqlCount=
			"select count(*) from PT_JNJD_J_JNFX t\n" +
			"where to_char(t.month,'yyyy-MM')='"+dateMonth+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'";
       Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
       if(totalCount>0)
       {
    	   PageObject obj=new PageObject();
    	   obj.setTotalCount(totalCount);
    	   String sql=
    		   "select * from PT_JNJD_J_JNFX t\n" +
    		   "where to_char(t.month,'yyyy-MM')='"+dateMonth+"'\n" + 
   			   "and t.enterprise_code='"+enterpriseCode+"'";
    	   List<PtJnjdJJnfx> list=bll.queryByNativeSQL(sql, PtJnjdJJnfx.class, rowStartIdxAndCount);
           obj.setList(list);
           return obj;
       }
       else
       {
    	   return null;
       }
		
	}

}