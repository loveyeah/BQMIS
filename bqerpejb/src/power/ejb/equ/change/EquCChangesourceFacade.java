package power.ejb.equ.change;

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
 * Facade for entity EquCChangesource.
 * 
 * @see power.ejb.equ.change.EquCChangesource
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquCChangesourceFacade implements EquCChangesourceFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;


	public int save(EquCChangesource entity) {
	 if(!this.CheckSourceCodeSame(entity.getEnterpriseCode(), entity.getSourceCode()))
	 {
		 if(entity.getSourceId()==null)
			{
				entity.setSourceId(bll.getMaxId("EQU_C_CHANGESOURCE", "source_id"));
				entity.setIsUse("Y");
			
			}
			entityManager.persist(entity);
			return Integer.parseInt(entity.getSourceId().toString());
			
		}
		else
			
		{
			return -1;
		}
	}


	public void delete(Long sourceId) {
		EquCChangesource entity=this.findById(sourceId);
		entity.setIsUse("N");
		this.update(entity);
		
	}


	public boolean update(EquCChangesource entity) {
		if(!this.CheckSourceCodeSame(entity.getEnterpriseCode(), entity.getSourceCode(), entity.getSourceId()))
		{
			
			entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return true;
		} 
		else
		{
			return  false;
		}
	}

	public EquCChangesource findById(Long id) {
		LogUtil.log("finding EquCChangesource instance with id: " + id,
				Level.INFO, null);
		try {
			EquCChangesource instance = entityManager.find(
					EquCChangesource.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public boolean CheckSourceCodeSame(String enterpriseCode,String sourceCode,Long... SourceCodeId) 
	{ 
		boolean isSame = false;
		String sql =
			"select count(1) from EQU_C_CHANGESOURCE t\n" +
			"where t.source_code='"+sourceCode+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";

	    if(SourceCodeId !=null&& SourceCodeId.length>0){
	    	sql += "  and t.source_id <> " + SourceCodeId[0];
	    } 
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}
	
	
	public PageObject findChangeSourceList(String fuzzy,String enterpriseCode,final int... rowStartIdxAndCount)
	{
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select * from  EQU_C_CHANGESOURCE t\n" +
				"where (t.source_code like '%"+fuzzy+"%' or t.source_name like '%"+fuzzy+"%')\n" + 
				"and t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'";

			List<EquCChangesource> list=bll.queryByNativeSQL(sql, EquCChangesource.class, rowStartIdxAndCount);
			String sqlCount=
				"select count(*) from  EQU_C_CHANGESOURCE t\n" +
				"where (t.source_code like '%"+fuzzy+"%' or t.source_name like '%"+fuzzy+"%')\n" + 
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
	
	
	
	







}