package power.ejb.workticket;

import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunCWorkticketType.
 * 
 * @see power.ejb.workticket.RunCWorkticketType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCWorkticketTypeFacade implements RunCWorkticketTypeFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	
	public RunCWorkticketType save(RunCWorkticketType entity) throws CodeRepeatException {
		LogUtil.log("saving RunCWorkticketType instance", Level.INFO, null);
		try {
			
			if(!this.checkTypeCode(entity.getWorkticketTypeCode(), entity.getEnterpriseCode()))
			{
				entity.setWorkticketTypeId(bll.getMaxId("RUN_C_WORKTICKET_TYPE", "workticket_type_id"));
				entity.setModifyDate(new java.util.Date());
				entity.setIsUse("Y");
				 entityManager.persist(entity);
				 
				LogUtil.log("save successful", Level.INFO, null);
				return  entity;
			}
			else
			{
				throw new CodeRepeatException("编码不能重复!");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(Long workticketTypeId) throws CodeRepeatException {
		RunCWorkticketType entity=this.findById(workticketTypeId);
		if(entity!=null)
		{
			entity.setIsUse("N");
			this.update(entity);
		}
	}
	
	public void deleteMulti(String workticketTypeIds)
	{
		String sql=
			"update  RUN_C_WORKTICKET_TYPE t\n" +
			"set t.is_use='N'\n" + 
			"where t.workticket_type_id in ("+workticketTypeIds+")";
                 bll.exeNativeSQL(sql);
		
	}
	


	public RunCWorkticketType update(RunCWorkticketType entity) throws CodeRepeatException {
		LogUtil.log("updating RunCWorkticketType instance", Level.INFO, null);
		try {
			if(!this.checkTypeCode(entity.getWorkticketTypeCode(),entity.getEnterpriseCode(),entity.getWorkticketTypeId()))
			{
				entity.setModifyDate(new java.util.Date());
			RunCWorkticketType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
			}
			else
			{
				throw new CodeRepeatException("编码不能重复!");
			}
			
			
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCWorkticketType findById(Long id) {
		LogUtil.log("finding RunCWorkticketType instance with id: " + id,
				Level.INFO, null);
		try {
			RunCWorkticketType instance = entityManager.find(
					RunCWorkticketType.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	



	@SuppressWarnings("unchecked")
	public PageObject findAll(String enterpriseCode, final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunCWorkticketType instances", Level.INFO,
				null);
		try {
			PageObject result = new PageObject(); 
			String sql=
				"select * from RUN_C_WORKTICKET_TYPE t\n" +
				"where  t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'";
			List<RunCWorkticketType> list=bll.queryByNativeSQL(sql, RunCWorkticketType.class, rowStartIdxAndCount);
			String sqlCount=
				"select count(*) from RUN_C_WORKTICKET_TYPE t\n" +
				"where  t.enterprise_code='"+enterpriseCode+"'\n" + 
				"and t.is_use='Y'";
			Long totalCount=Long.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;
			
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public boolean checkTypeCode(String typeCode,String enterpriseCode,Long... workticketTypeId)
	{
		boolean isSame = false;
		String sql =
			"select count(*) from RUN_C_WORKTICKET_TYPE t\n" +
			"where t.workticket_type_code='"+typeCode+"'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n" + 
			"and t.is_use='Y'";
		  if(workticketTypeId !=null&& workticketTypeId.length>0){
		    	sql += "  and t.workticket_type_id <> " + workticketTypeId[0];
		    }
	    if(Long.parseLong((bll.getSingal(sql).toString()))>0)
		{
	    	isSame = true;
		}
	    return isSame;
	}

}