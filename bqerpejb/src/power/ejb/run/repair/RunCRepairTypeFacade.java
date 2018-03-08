package power.ejb.run.repair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
import power.ejb.manage.plan.BpJPlanRepairDep;
import power.ejb.manage.plan.BpJPlanRepairDetail;

/**
 * Facade for entity RunCRepairType.
 * 
 * @see power.ejb.run.repair.RunCRepairType
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunCRepairTypeFacade implements RunCRepairTypeFacadeRemote {
	// property constants
	public static final String REPAIR_TYPE_NAME = "repairTypeName";
	public static final String ENTRY_BY = "entryBy";
	public static final String MEMO = "memo";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	public PageObject getRepairType(String enterPriseCode)
	{
		PageObject result=new PageObject();
		String sql=
			"select   distinct  t.repair_type_id,\n" +
			"       t.repair_type_name," +
			"       t.entry_by,\n" + 
			"       getworkername(t.entry_by),\n" + 
			"       to_char(t.entry_date, 'yyyy-MM-dd'),\n" + 
			"       t.memo,\n" + 
			"       t.is_use\n" + 
			"  from RUN_C_REPAIR_TYPE  t\n" + 
			"  where t.enterprise_code='"+enterPriseCode+"' and t.is_use = 'Y'";
       List list=bll.queryByNativeSQL(sql);
       result.setList(list);
		return result;
		
	}
	public boolean isHas(String name,String memo,String isuse,String enterpriseCode )
	{
		String sql="select count(*) from  RUN_C_REPAIR_TYPE  t " +
				"where t.is_use='"+isuse+"'" +
				" and  t.memo='"+memo+"' " +
				" and t.repair_type_name='"+name+"'" +
				" and t.enterprise_code='"+enterpriseCode+"'  ";
		int count=Integer.parseInt(bll.getSingal(sql).toString());
		if(count>0)
		{
			return false;
			
		}else 
		{
			return true;
		}
		
	}
	public  void saveRepairType(List<RunCRepairType> addList,List<RunCRepairType> updateList) throws CodeRepeatException{  
		
		if (addList != null &&addList.size() > 0) {

			for (RunCRepairType entity : addList) {
				Long repairTypeID = bll.getMaxId("RUN_C_REPAIR_TYPE ",
				"repair_type_id");
				boolean flag=this.isHas(entity.getRepairTypeName(),entity.getMemo(),entity.getIsUse(),entity.getEnterpriseCode());
				if(flag)
				{
				entity.setRepairTypeId(repairTypeID);
				this.save(entity);
				}else 
				{
					throw new CodeRepeatException("该修理类别已经存在！");
				}
			}
		}
		if (updateList!=null && updateList.size() > 0) {
			for (RunCRepairType entity : updateList) {
				boolean flag=this.isHas(entity.getRepairTypeName(),entity.getMemo(),entity.getIsUse(),entity.getEnterpriseCode());
				if(flag)
				{
				this.update(entity);
				}else {
					throw new CodeRepeatException("该修理类别已经存在！");
				}
			}
		}
		
	}
	public void save(RunCRepairType entity) {
		LogUtil.log("saving RunCRepairType instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			entityManager.flush();
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(RunCRepairType entity) {
		LogUtil.log("deleting RunCRepairType instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunCRepairType.class, entity
					.getRepairTypeId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public RunCRepairType update(RunCRepairType entity) {
		LogUtil.log("updating RunCRepairType instance", Level.INFO, null);
		try {
			RunCRepairType result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunCRepairType findById(Long id) {
		LogUtil.log("finding RunCRepairType instance with id: " + id,
				Level.INFO, null);
		try {
			RunCRepairType instance = entityManager.find(RunCRepairType.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	

}