package power.ejb.productiontec.insulation;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity PtJyjdJSybglh.
 * 
 * @see power.ejb.productiontec.insulation.PtJyjdJSybglh
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PtJyjdJSybglhFacade implements PtJyjdJSybglhFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public PtJyjdJSybglh save(PtJyjdJSybglh entity) {
		LogUtil.log("saving PtJyjdJSybglh instance", Level.INFO, null);
		try {
			entity.setReportId(bll.getMaxId("PT_JYJD_J_SYBGLH", "Report_Id"));
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
			  "delete PT_JYJD_J_SYBGLH t\n" +
			  "where t.Report_Id in ("+ids+")";
		  bll.exeNativeSQL(sql);

	  }

	public PtJyjdJSybglh update(PtJyjdJSybglh entity) {
		LogUtil.log("updating PtJyjdJSybglh instance", Level.INFO, null);
		try {
			PtJyjdJSybglh result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PtJyjdJSybglh findById(Long id) {
		LogUtil.log("finding PtJyjdJSybglh instance with id: " + id,
				Level.INFO, null);
		try {
			PtJyjdJSybglh instance = entityManager
					.find(PtJyjdJSybglh.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String equName,String enterpriseCode,final int... rowStartIdxAndCount) {
		
		String sqlCount= "select count(1)\n" +
			"  from PT_JYJD_J_SYBGLH a\n" + 
			" where a.equ_name like '%" + equName + "%'" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'";

       Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
       if(totalCount>0)
       {
    	   PageObject obj=new PageObject();
    	   obj.setTotalCount(totalCount);
    	   String sql= "select *\n" +
    		   "  from PT_JYJD_J_SYBGLH a\n" + 
    		   " where a.equ_name like '%" + equName + "%'" + 
    		   "   and a.enterprise_code = '"+enterpriseCode+"'";

    	   List<PtJyjdJSybglh> list=bll.queryByNativeSQL(sql, PtJyjdJSybglh.class, rowStartIdxAndCount);
           obj.setList(list);
           return obj;
       }
       else
       {
    	   return null;
       }
		
	}

}