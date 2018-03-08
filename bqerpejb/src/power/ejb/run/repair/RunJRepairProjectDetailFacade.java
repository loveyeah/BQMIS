package power.ejb.run.repair;

import java.text.ParseException;
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
 * Facade for entity RunJRepairProjectDetail.
 * 
 * @see power.ejb.run.repair.RunJRepairProjectDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJRepairProjectDetailFacade implements
		RunJRepairProjectDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	
	public  void saveRepairDetail(List<RunJRepairProjectDetail> addList,List<RunJRepairProjectDetail> updateList) throws ParseException{  
		if (addList != null &&addList.size() > 0) {
			for (RunJRepairProjectDetail entity : addList) {
				Long projectDetailId = bll.getMaxId("RUN_J_REPAIR_PROJECT_DETAIL ","project_detail_id");
				entity.setProjectDetailId(projectDetailId);
				this.save(entity);
			}
		}
		if (updateList!=null && updateList.size() > 0) {
			for (RunJRepairProjectDetail entity : updateList) {
				this.update(entity);
			}
		}
	}
	
	public void save(RunJRepairProjectDetail entity) {
		LogUtil.log("saving RunJRepairProjectDetail instance", Level.INFO,null);
		try {
			entityManager.persist(entity);
			entityManager.flush();
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void deleteMult(String ids)
	{
		String sql=
			"update RUN_J_REPAIR_PROJECT_DETAIL  t  " +
			" set t.is_use='N'\n" +
			"where t.project_detail_id  in ("+ids+")";
		bll.exeNativeSQL(sql);

	}

	public RunJRepairProjectDetail update(RunJRepairProjectDetail entity) {
		LogUtil.log("updating RunJRepairProjectDetail instance", Level.INFO,
				null);
		try {
			RunJRepairProjectDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJRepairProjectDetail findById(Long id) {
		LogUtil.log("finding RunJRepairProjectDetail instance with id: " + id,
				Level.INFO, null);
		try {
			RunJRepairProjectDetail instance = entityManager.find(
					RunJRepairProjectDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	@SuppressWarnings("unchecked")
	public PageObject getRepairDetailList(String repairMainId,String enterPriseCode)
	{
		PageObject result=new PageObject();
		String sql = "";
		if(repairMainId != null)
		{
		 sql= "select d.project_detail_id,\n" +
			 "       d.project_main_id,\n" + 
			 "       d.repair_project_id,\n" + 
			 "       d.repair_project_name,\n" + 
			 "       d.working_charge,\n" + 
			 "       getworkername(d.working_charge),\n" + 
			 "       d.working_menbers,\n" + 
			 "       d.working_time,\n" + 
			 "       d.standard,\n" + 
			 "       d.material,\n" + 
			 "       d.situation,\n" + 
			 "       d.reason,\n" + 
			 "       to_char(d.start_date, 'yyyy-MM-dd'),\n" + 
			 "       to_char(d.end_date, 'yyyy-MM-dd'),\n" +
			 "       d.acceptance_second ,\n" +
			 "       getworkername(d.acceptance_second),\n" +
			 "       d.acceptance_three,\n" +
			 "       getworkername(d.acceptance_three) \n" + 
			 "  from RUN_J_REPAIR_PROJECT_DETAIL d\n" + 
			 " where d.is_use = 'Y'\n" + 
			 "   and d.project_main_id = "+repairMainId+"\n" + 
			 "   and d.enterprise_code = '"+enterPriseCode+"'";
		
	       List list=bll.queryByNativeSQL(sql);
	       result.setList(list);
		}
	    return result;
	}

}