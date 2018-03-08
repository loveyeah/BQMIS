package power.ejb.run.repair;

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

/**
 * Facade for entity RunJRepairTasklist.
 * 
 * @see power.ejb.run.repair.RunJRepairTasklist
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJRepairTasklistFacade implements RunJRepairTasklistFacadeRemote {
	// property constants
	public static final String TASKLIST_NAME = "tasklistName";
	public static final String ENTRY_BY = "entryBy";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	public boolean  isHas(String tasklistName,String  enterpirseCode)
	{
		String sql="select count(*) from  RUN_J_REPAIR_TASKLIST t " +
				  "where  t.is_use='Y'" +
				  "and t.enterprise_code='"+enterpirseCode+"' " +
				  "and  t.tasklist_name='"+tasklistName+"'";
		int   count=Integer.parseInt(bll.getSingal(sql).toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}

	}

	public PageObject getRepairTask(String year, String enterpirseCode) {
		PageObject result=new PageObject();
		String sql=
			"select distinct t.tasklist_id,\n" +
			"       t.tasklist_year,\n" + 
			"       t.tasklist_name,\n" + 
			"       t.entry_by,\n" + 
			"       getworkername(t.entry_by),\n" + 
			"       to_char(t.entry_date, 'yyyy-MM-dd')\n" + 
			"  from RUN_J_REPAIR_TASKLIST t\n" + 
			"  where t.is_use='Y'\n" + 
			"  and t.enterprise_code='"+enterpirseCode+"'";
		if(year!=null&&!year.equals(""))
		{
			sql+="and to_char( t.tasklist_year,'yyyy')='"+year+"'";
		}
//		System.out.println("the sql"+sql);
		List list=bll.queryByNativeSQL(sql);
		result.setList(list);
		return result;
	}
	
	public PageObject getRepairTaskSelect(String year,String sepeciality,String enterpirseCode)
	{
		PageObject result=new PageObject();
		String sql=
			"select distinct a.tasklist_id,a.tasklist_year, a.tasklist_name\n" +
			"  from RUN_J_REPAIR_TASKLIST a\n" + 
			" where a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpirseCode+"'\n" + 
			"   and a.tasklist_id not in\n" + 
			"       (select b.tasklist_id\n" + 
			"          from RUN_J_REPAIR_PROJECT_MAIN b\n" + 
			"         where b.final_version = 'Y'\n" + 
			"           and (select s.speciality_name\n" + 
			"                  from run_c_specials s\n" + 
			"                 where s.speciality_id = b.speciality_id\n" + 
			"                   and s.is_use = 'Y') = '"+sepeciality+"')";

		if(year!=null&&!year.equals(""))
		{
			sql+="and to_char( a.tasklist_year,'yyyy')='"+year+"'";
		}
		List list=bll.queryByNativeSQL(sql);
		result.setList(list);
		return result;
		
	}
	
	public  void saveRepairTask(List<RunJRepairTasklist>  addList,List<RunJRepairTasklist> updateList) throws CodeRepeatException
	{
		if (addList != null &&addList.size() > 0) {

			for (RunJRepairTasklist entity : addList) {
				Long tasklistId = bll.getMaxId("RUN_J_REPAIR_TASKLIST ",
				"tasklist_id");
				boolean flag=this.isHas(entity.getTasklistName(), entity.getEnterpriseCode());
				if(flag)
				{
				entity.setTasklistId(tasklistId);
				this.save(entity);
				}else {  
					throw new CodeRepeatException("该任务单名称已经存在！");
				}
			}
		}
		if (updateList!=null && updateList.size() > 0) {
			for (RunJRepairTasklist entity : updateList) {
				boolean flag=this.isHas(entity.getTasklistName(), entity.getEnterpriseCode());
				if(flag)
				{
				this.update(entity);
				}else 
				{
					throw new CodeRepeatException("该任务单名称已经存在！");
				}
			}
		}
		
	}
	public void delRepairTask(String ids)
	{
		String sql=
			"update RUN_J_REPAIR_TASKLIST  t  " +
			" set t.is_use='N'\n" +
			"where t.tasklist_id  in ("+ids+")";
		bll.exeNativeSQL(sql);

	}
	public void save(RunJRepairTasklist entity) {
		LogUtil.log("saving RunJRepairTasklist instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			entityManager.flush();
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(RunJRepairTasklist entity) {
		LogUtil.log("deleting RunJRepairTasklist instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(RunJRepairTasklist.class,
					entity.getTasklistId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public RunJRepairTasklist update(RunJRepairTasklist entity) {
		LogUtil.log("updating RunJRepairTasklist instance", Level.INFO, null);
		try {
			RunJRepairTasklist result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJRepairTasklist findById(Long id) {
		LogUtil.log("finding RunJRepairTasklist instance with id: " + id,
				Level.INFO, null);
		try {
			RunJRepairTasklist instance = entityManager.find(
					RunJRepairTasklist.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	

}