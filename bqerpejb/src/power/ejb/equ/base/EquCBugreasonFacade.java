package power.ejb.equ.base;

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

/**
 * Facade for entity EquCBugreason.
 * 
 * @see power.ejb.equ.base.EquCBugreason
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCBugreasonFacade implements EquCBugreasonFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public int save(EquCBugreason entity) {
		 if(!this.checkReasonDescSame(entity.getBugCode(), entity.getBugReasonDesc(), entity.getEnterpriseCode()))
		 {
			 if(entity.getBugReasonId()==null)
				{
					entity.setBugReasonId(bll.getMaxId("equ_c_bugreason", "bug_reason_id"));
					entity.setIsUse("Y");
				
				}
				entityManager.persist(entity);
				return Integer.parseInt(entity.getBugReasonId().toString());
			
		 }
		 else
			{
				return -1;
			}
	}

	
	public void delete(Long solutinId) {
		EquCBugreason entity=this.findById(solutinId);
		entity.setIsUse("N");
		this.update(entity);
		
	}

	public boolean update(EquCBugreason entity) {
		try {
			if(!this.checkReasonDescSame(entity.getBugCode(), entity.getBugReasonDesc(), entity.getEnterpriseCode(), entity.getBugReasonId()))
			{
			    entityManager.merge(entity);
			    LogUtil.log("update successful", Level.INFO, null);
			    return true;
			}
			else
			{
				return false;
			}
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquCBugreason findById(Long id) {
		LogUtil.log("finding EquCBugreason instance with id: " + id,
				Level.INFO, null);
		try {
			EquCBugreason instance = entityManager
					.find(EquCBugreason.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}





	@SuppressWarnings("unchecked")
	public List<EquCBugreason> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCBugreason instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCBugreason model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject findBugReasonList(String reasonDesc,String bugCode,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select * from equ_c_bugreason t\n" +
				"where t.bug_reason_desc like '%"+reasonDesc+"%'\n" + 
				"and  t.bug_code='"+bugCode+"'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'";

			List<EquCBugreason> list=bll.queryByNativeSQL(sql, EquCBugreason.class, rowStartIdxAndCount);
			String sqlCount=
				"select count(*) from equ_c_bugreason t\n" +
				"where t.bug_reason_desc like '%"+reasonDesc+"%'\n" + 
				"and  t.bug_code='"+bugCode+"'\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'";

			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public boolean checkReasonDescSame(String bugCode,String reasonDesc,String enterpriseCode,final Long... reasonId)
	{
		boolean isSame = false;
		String sql =
			"select count(*) from equ_c_bugreason t\n" +
			"where t.bug_code='"+bugCode+"'\n" + 
			"and t.bug_reason_desc='"+reasonDesc+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";

	    if(reasonId !=null&& reasonId.length>0){
	    	sql += "  and t.bug_reason_id <> " + reasonId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	
	
	public void deleteReasonByBugCode(String bugCode,String enterpriseCode)
	{
		String sql=
			"update equ_c_bugreason a\n" +
			"set a.is_use='N'\n" + 
			"where a.bug_code='"+bugCode+"'\n" + 
			"and a.enterprise_code='"+enterpriseCode+"'\n" + 
			"and a.is_use='Y'";
		bll.exeNativeSQL(sql);

	}
	
	
	
	

}