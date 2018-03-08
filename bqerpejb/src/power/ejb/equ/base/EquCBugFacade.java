package power.ejb.equ.base;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;

/**
 * Facade for entity EquCBug.
 * 
 * @see power.ejb.equ.base.EquCBug
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCBugFacade implements EquCBugFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;


	public int save(EquCBug entity) {
	 if(!this.CheckBugCodeSame(entity.getBugCode(), entity.getEnterpriseCode()))
	 {
		 if(entity.getBugId()==null)
			{
				entity.setBugId(bll.getMaxId("equ_c_bug", "bug_id"));
				entity.setIsUse("Y");
			
			}
			entityManager.persist(entity);
			return Integer.parseInt(entity.getBugId().toString());
			
		 
	 }
	 else
		{
			return -1;
		}
	}

	
	public boolean delete(Long bugId) {
		EquCBug entity=this.findById(bugId);
		if(!this.ifHasChild(entity.getBugId(), entity.getEnterpriseCode()))
		{
		entity.setIsUse("N");
	    this.update(entity);
	    return true;
		}
		else
		{
			return false;
		}
	}


	public boolean update(EquCBug entity) {
		try {
			if(!this.CheckBugCodeSame(entity.getBugCode(), entity.getEnterpriseCode(), entity.getBugId()))
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

	public EquCBug findById(Long id) {
		LogUtil
				.log("finding EquCBug instance with id: " + id, Level.INFO,
						null);
		try {
			EquCBug instance = entityManager.find(EquCBug.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	



	@SuppressWarnings("unchecked")
	public List<EquCBug> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all EquCBug instances", Level.INFO, null);
		try {
			final String queryString = "select model from EquCBug model";
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
	private PageObject findList(String strWhere,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql="select * from  equ_c_bug \n";
			if(strWhere!="")
			{
				sql=sql+" where  "+strWhere;
			}
			List<EquCBug> list=bll.queryByNativeSQL(sql, EquCBug.class, rowStartIdxAndCount);
			String sqlCount="select count(*)　from equ_c_bug \n";
			if(strWhere!="")
			{
				sqlCount=sqlCount+" where  "+strWhere;
			}
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
			
		}catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
		
	}
	
	
	public boolean ifHasChild(Long pBugId,String enterpriseCode)
	{
		String strWhere=
			" p_bug_id="+pBugId+"\n" + 
			"and enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y'";
		PageObject result=findList(strWhere);
		if(result.getTotalCount()==0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	
	public boolean CheckBugCodeSame(String bugCode,String enterpriseCode,Long... bugId)
	{
		boolean isSame = false;
		String sql =
			"select count(*) from equ_c_bug t\n" +
			"where t.bug_code='"+bugCode+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			" and t.is_use='Y'";
	    if(bugId !=null&& bugId.length>0){
	    	sql += "  and t.bug_id <> " + bugId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	
	@SuppressWarnings("unchecked")
	public List<EquCBug> getListByParent(Long bugId,String enterpriseCode)
	{
		String strWhere=
			"   p_bug_id="+bugId+"\n" +
			"and  enterprise_code='"+enterpriseCode+"'\n" + 
			"and  is_use='Y'  order by bug_name";
		PageObject result=findList(strWhere);
		return result.getList();
		
	}
	
	public EquCBug findByCode(String bugCode,String enterpriseCode)
	{
		String strWhere=
			" bug_code='"+bugCode+"'\n" +
			"and enterprise_code='"+enterpriseCode+"'\n" + 
			"and is_use='Y'";

		PageObject result=findList(strWhere);
		if(result.getList()!=null)
		{
			if(result.getList().size()>0)
			{
			return (EquCBug)result.getList().get(0);
			}
		}
		return null;
	}
	
	public PageObject findListByName(String enterpriseCode,String bugName,final int... rowStartIdxAndCount)
	{
		PageObject result = new PageObject(); 
		String sql=
			"   is_use = 'Y'\n" +
			"   and enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and bug_name like '%"+bugName+"%'  order by bug_name";
		if(rowStartIdxAndCount!=null&&rowStartIdxAndCount.length>1)
		{
			result=this.findList(sql, rowStartIdxAndCount[0],rowStartIdxAndCount[1]);
		}
		else
		{
			result=this.findList(sql);
		}
		return result;

	}
	


}