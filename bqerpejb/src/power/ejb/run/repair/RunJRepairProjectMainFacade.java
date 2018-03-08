package power.ejb.run.repair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jaxen.function.SubstringAfterFunction;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity RunJRepairProjectMain.
 * 
 * @see power.ejb.run.repair.RunJRepairProjectMain
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJRepairProjectMainFacade implements
		RunJRepairProjectMainFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;

	public RunJRepairProjectMainFacade() {
		service = new WorkflowServiceImpl();
	}
	public  String getsituation(String id,String enterpriseCode)
	{
		String sql="select " +
				"r.situation_project  " +
				"from RUN_J_REPAIR_PROJECT_MAIN r where r.project_main_id='"+id+"' " +
				"and r.is_use='Y' " +
				"and r.enterprise_code='"+enterpriseCode+"'";
		String situation=bll.getSingal(sql).toString();
		return situation;
		
	}
	public RunJRepairProjectMain save(RunJRepairProjectMain entity) {
		try {
			entity.setProjectMainId(bll.getMaxId("RUN_J_REPAIR_PROJECT_MAIN",
					"project_main_id"));
			entity.setIsUse("Y");
			entity.setWorkflowStatus(0l);
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void deleteMult(String ids) {
		String sql = "update RUN_J_REPAIR_PROJECT_MAIN m\n"
				+ "   set m.is_use = 'N'\n" + " where m.project_main_id in ("
				+ ids + ")";
		
		bll.exeNativeSQL(sql);

		String detailSql = "update RUN_J_REPAIR_PROJECT_DETAIL  t  "
				+ " set t.is_use='N'\n" + "where t.project_main_id  in ("
				+ ids + ")";

		bll.exeNativeSQL(detailSql);
	}

	public RunJRepairProjectMain update(RunJRepairProjectMain entity) {
		LogUtil
				.log("updating RunJRepairProjectMain instance", Level.INFO,
						null);
		try {
			RunJRepairProjectMain result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJRepairProjectMain findById(Long id) {
		LogUtil.log("finding RunJRepairProjectMain instance with id: " + id,
				Level.INFO, null);
		try {
			RunJRepairProjectMain instance = entityManager.find(
					RunJRepairProjectMain.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunJRepairProjectMain> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJRepairProjectMain instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from RunJRepairProjectMain model";
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
	public PageObject findRepairList(String enterpriseCode, String year,
			String repairType, String speciality, String tastlist,
			String editTime, String flag, String entryIds,
			final int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select m.project_main_id,\n"
				+ "       m.project_main_year,\n"
				+ "       m.repair_type_id,\n"
				+ "       (select t.repair_type_name\n"
				+ "          from RUN_C_REPAIR_TYPE t\n"
				+ "         where t.repair_type_id = m.repair_type_id\n"
				+ "           and t.is_use = 'Y') repairTypeName,\n"
				+ "       m.fill_by,\n"
				+ "       getworkername(m.fill_by),\n"
				+ "       to_char(m.fill_time,'yyyy-mm-dd'),\n"
				+ "       m.speciality_id,\n"
				+ "       (select s.speciality_name from run_c_specials s where s.speciality_id = m.speciality_id and s.is_use = 'Y'),\n"
				+ "       m.tasklist_id,\n"
				+ "       (select p.tasklist_name\n"
				+ "          from RUN_J_REPAIR_TASKLIST p\n"
				+ "         where p.tasklist_id = m.tasklist_id\n"
				+ "           and p.is_use = 'Y'),\n" + "       m.memo,\n"
				+ "       m.version,\n" + "       m.workflow_no,\n"
				+ "       m.workflow_status,\n" + "       m.final_version,\n"
				+ "       m.situation_project\n"
				+ "  from RUN_J_REPAIR_PROJECT_MAIN m\n"
				+ " where m.is_use = 'Y'\n" + "   and m.enterprise_code = '"
				+ enterpriseCode + "'";

		String whereStr = "";
		if (flag != null && flag.equals("approve")) {
			if (entryIds != null) {
				String inEntriyId = bll.subStr(entryIds, ",", 500,
						"m.workflow_no");
				sql += " \n and " + inEntriyId;
				sql += " \n and m.workflow_status in(1,2,3)";
			}
			else {
				sql += " \n and m.workflow_no=null";
			}
			whereStr += "\n and to_char(m.fill_time,'yyyy-mm') like '"
					+ editTime + "%'\n";
		} else {
			whereStr += " and workflow_status in (0,8) \n";
		}

		if (year != null && year.length() > 0) {
			whereStr += " and m.project_main_year='" + year + "'\n";
		}
		if (repairType != null && repairType.length() > 0) {
			whereStr += " and m.repair_type_id =" + repairType + "\n";
		}
		if (speciality != null && speciality.length() > 0) {
			whereStr += "  and m.speciality_id = " + speciality + "\n";
		}
		if (tastlist != null && tastlist.length() > 0) {
			whereStr += "  and (select p.tasklist_name from RUN_J_REPAIR_TASKLIST p where p.tasklist_id = m.tasklist_id and p.is_use = 'Y') = "
					+ tastlist + "\n";
		}
		sql += whereStr;
		String sqlCount = "select count(1) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);

		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}

	public Object findInfoById(Long mainId) {
		String sql = "select m.project_main_id,\n"
				+ "       m.project_main_year,\n"
				+ "       m.repair_type_id,\n"
				+ "       (select t.repair_type_name\n"
				+ "          from RUN_C_REPAIR_TYPE t\n"
				+ "         where t.repair_type_id = m.repair_type_id\n"
				+ "           and t.is_use = 'Y') repairTypeName,\n"
				+ "       m.fill_by,\n" + "       getworkername(m.fill_by),\n"
				+ "       to_char(m.fill_time, 'yyyy-mm-dd'),\n"
				+ "       m.speciality_id,\n"
				+ "       (select s.speciality_name\n"
				+ "          from run_c_specials s\n"
				+ "         where s.speciality_id = m.speciality_id\n"
				+ "           and s.is_use = 'Y'),\n"
				+ "       m.tasklist_id,\n"
				+ "       (select p.tasklist_name\n"
				+ "          from RUN_J_REPAIR_TASKLIST p\n"
				+ "         where p.tasklist_id = m.tasklist_id\n"
				+ "           and p.is_use = 'Y'),\n" + "       m.memo,\n"
				+ "       m.version,\n" + "       m.workflow_no,\n"
				+ "       m.workflow_status,\n" + "       m.final_version,\n"
				+ "       decode(m.situation_project,'Y','是','N','否')\n"
				+ "  from RUN_J_REPAIR_PROJECT_MAIN m\n"
				+ " where m.is_use = 'Y'\n" + "   and m.project_main_id = "
				+ mainId + "";

		return bll.getSingal(sql);
	}

	public void reportRepairRecord(Long repairId, Long actionId,
			String workerCode, String approveText, String nextRoles, 
			String nextRolePs,String workflowType) {
		
		WorkflowService service = new WorkflowServiceImpl();
		long entryId = service.doInitialize(workflowType, workerCode, repairId
				.toString());

		service.doAction(entryId, workerCode, actionId, "上报", null, nextRoles, nextRolePs);

		RunJRepairProjectMain model = this.findById(repairId);
		model.setWorkflowNo(entryId);
		String situation = model.getSituationProject();
		if (situation != null && situation.equals("Y")) {
			model.setWorkflowStatus(1l);
		} else {
			model.setWorkflowStatus(3l);
		}
		entityManager.merge(model);
		// String sql = " update RUN_J_REPAIR_PROJECT_MAIN set
		// workflow_status=1,workflow_no="
		// + entryId + " where project_main_id='" + repairId + "'";
		// bll.exeNativeSQL(sql);
	}
	
	public RunJRepairProjectMain RepairRecordApprove(Long projectMainId, Long actionId, 
			Long entryId,String workerCode, String approveText, String nextRoles,
			String eventIdentify,String tasklistId,String specialityId,String enterpriseCode) {
		RunJRepairProjectMain entity = findById(projectMainId);
		try {
			if(eventIdentify.equals("TH"))
			{
				entity.setWorkflowStatus(8l);
			}
			else 
			{
				if(entity.getWorkflowStatus()==1)
				{
					if(eventIdentify.equals("TY"))
					{
						entity.setWorkflowStatus(2l);
					}
				}
				else if(entity.getWorkflowStatus()==2)
				{
				  entity.setWorkflowStatus(3l);
				}
				else if(entity.getWorkflowStatus()==3)
				{
					if (eventIdentify.equals("FZZBB（JS）")) {
						entity.setWorkflowStatus(7l);
					} else if (eventIdentify.equals("ZZBB（ZSBB）")) {
						entity.setWorkflowStatus(4l);
						entity.setFinalVersion("Y");
					}

					String sql = "SELECT MAX(t.version)\n"
							+ "  FROM run_j_repair_project_main t\n"
							+ " WHERE t.tasklist_id = '" + tasklistId + "'\n"
							+ "   AND t.speciality_id = '" + specialityId
							+ "'\n" + "   AND t.is_use = 'Y'\n"
							+ "   AND t.enterprise_code = '" + enterpriseCode
							+ "'";
					Object maxNoObj = bll.getSingal(sql);
					String maxNo = (maxNoObj == null) ? "" : maxNoObj
							.toString();
					String version = "";
					if (maxNo.equals("")) {
						entity.setVersion(1l);
					} 
					else {
						Integer tempNo = Integer.parseInt(maxNo);
						tempNo++;
						version = tempNo.toString();
						entity.setVersion(Long.parseLong(version));
					}
				}
//				else if(entity.getWorkflowStatus()==4)
//				{
//				  entity.setWorkflowStatus(5l);
//				}
//				else if(entity.getWorkflowStatus()==5)
//				{
//				  entity.setWorkflowStatus(6l);
//				}
//				else if(entity.getWorkflowStatus()==6)
//				{
//				  entity.setWorkflowStatus(7l);
//				}
				
			}
			WorkflowService service = new WorkflowServiceImpl();
			service.doAction(entity.getWorkflowNo(),workerCode,actionId,approveText,null,"",nextRoles);
			entityManager.merge(entity);
	}catch (Exception e) {}
	return entity;
}
	
	public RunJRepairProjectMain RepairRecordLeaderApprove(Long projectMainId, Long actionId, 
			Long entryId,String workerCode, String approveText, String nextRoles,
			String eventIdentify,String enterpriseCode) {
		RunJRepairProjectMain entity = findById(projectMainId);
		try {
			if(eventIdentify.equals("TH"))
			{
				entity.setWorkflowStatus(8l);
			}
			else 
			{
				if(entity.getWorkflowStatus()==4)
				{
				  entity.setWorkflowStatus(5l);
				}
				else if(entity.getWorkflowStatus()==5)
				{
				  entity.setWorkflowStatus(6l);
				}
				else if(entity.getWorkflowStatus()==6)
				{
				  entity.setWorkflowStatus(7l);
				}
				
			}
			WorkflowService service = new WorkflowServiceImpl();
			service.doAction(entity.getWorkflowNo(),workerCode,actionId,approveText,null,nextRoles,"");
			entityManager.merge(entity);
	}catch (Exception e) {}
	return entity;
}
	
	public String getRepairStatusMain(String repairMainId,String selectMainId,String flag, String enterpriseCode) {
		
		if(selectMainId!= null&& !selectMainId.equals("")){
			repairMainId=selectMainId;
		}
		else if(flag!= null&& flag.equals("many")){
			String [] mainId= repairMainId.split(",");
			repairMainId=mainId[0];
		}
		String sql ="SELECT t.workflow_status\n" +
			"  FROM run_j_repair_project_main t\n" + 
			" WHERE t.project_main_id = '"+repairMainId+"'\n" + 
			"   AND t.is_use = 'Y'\n" + 
			"   AND T.ENTERPRISE_CODE = '"+enterpriseCode+"'";

		Object maxNoObj = bll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}
	
	public String getRepairMaxVersionMain(String tasklistId,String enterpriseCode) {
		String sql ="SELECT MAX(t.version)\n"
							+ "  FROM run_j_repair_project_main t\n"
							+ " WHERE t.tasklist_id = '" + tasklistId + "'\n"
							+ "   AND t.is_use = 'Y'\n"
							+ "   AND t.enterprise_code = '" + enterpriseCode + "'";

		Object maxNoObj = bll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject getRepairVerisonList(String tasklistId,String enterpirseCode) {
		PageObject result=new PageObject();
		String sql="SELECT DISTINCT t.version,\n" +
			"  decode(t.version,'0','初稿','第' || t.version || '版本')AS versionName\n" + 
			"  FROM run_j_repair_project_main t\n" + 
			" WHERE t.tasklist_id = '"+tasklistId+"'\n" + 
			"   AND t.is_use = 'Y'\n" + 
			"   AND t.enterprise_code = '"+enterpirseCode+"'\n" + 
			"   AND t.version IS NOT NULL\n" + 
			" ORDER BY t.version";
		
		List list=bll.queryByNativeSQL(sql);
		result.setList(list);
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject getRepairTastListApprove(String entryIds,String enterpriseCode,
			final int... rowStartIdxAndCount) {
		
		PageObject pg = new PageObject();
		
		String st = "select distinct m.tasklist_id\n"
			+ "  from RUN_J_REPAIR_PROJECT_MAIN m\n"
			+ " where m.workflow_status in(4,5,6)\n" 
			+ " and m.is_use = 'Y'\n" 
			+ " and m.enterprise_code = '"+ enterpriseCode + "'";

		if (entryIds != null&& !entryIds.equals("")) {
			String inEntriyId = bll.subStr(entryIds, ",", 500,
					"m.workflow_no");
			st += " \n and " + inEntriyId;
		}
		else {
			st += " \n and m.workflow_no=null";
		}
		List taskst=bll.queryByNativeSQL(st);
		String tasklist = "";
		if(taskst.size()>0){
			for (int i = 0; i < taskst.size(); i++) {
				if (taskst.get(i) != null && !taskst.get(i).toString().equals("")) {
					tasklist += taskst.get(i).toString() + ",";
				}
			}
			tasklist=tasklist.substring(0,tasklist.length()-1);
		}
		else {
			tasklist=null;
		}
		
		String sql = "SELECT t.tasklist_id,\n" +
			"       to_char(t.tasklist_year, 'yyyy-MM-dd') tasklist_year,\n" + 
			"       t.tasklist_name,\n" + 
			"       t.entry_by,\n" + 
			"       getworkername(t.entry_by) entryByName,\n" + 
			"       to_char(t.entry_date, 'yyyy-MM-dd') entry_date\n" + 
			"  FROM RUN_J_REPAIR_TASKLIST t\n" + 
			" WHERE t.is_use = 'Y'\n" + 
			"   AND t.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   AND t.tasklist_id IN ("+tasklist+")";
		
		String sqlCount = "select count(1) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);

		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setList(list);
		pg.setTotalCount(totalCount);
		return pg;
	}	
	
	
	@SuppressWarnings("unchecked")
	public PageObject getLeaderApproveInfo(String tasklist,String entryIds,String enterPriseCode)
	{
		PageObject result=new PageObject();
		String sql = "";
		if(tasklist != null)
		{
		 sql= "SELECT t.project_main_id,\n" +
			 "t.workflow_no,\n" + 
			 "t.workflow_status\n" + 
			 "FROM run_j_repair_project_main t\n" + 
			 "WHERE t.is_use='Y'\n" + 
			 "AND t.enterprise_code='"+enterPriseCode+"'\n" + 
			 "AND t.tasklist_id ='"+tasklist+"'";
		 
			if (entryIds != null&& !entryIds.equals("")) {
				String inEntriyId = bll.subStr(entryIds, ",", 500,
						"t.workflow_no");
				sql += " \n and " + inEntriyId;
				sql += " \n and t.workflow_status in(4,5,6)";
			}
			else {
				sql += " \n and t.workflow_no=null";
			}
			sql += " \n order by t.project_main_id";
	       List list=bll.queryByNativeSQL(sql);
	      
	       result.setList(list);
		}
	    return result;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject getLeaderApproveDetail(String projectMainId,String enterPriseCode)
	{
		PageObject result=new PageObject();
		String sql = "";
		if(projectMainId != null)
		{
		 sql=  "SELECT d.project_detail_id,\n" +
			 "       d.project_main_id,\n" + 
			 "       d.repair_project_id,\n" + 
			 "       d.repair_project_name,\n" + 
			 "       d.working_charge,\n" + 
			 "       getworkername(d.working_charge),\n" + 
			 "       d.working_menbers,\n" + 
			 "       d.working_time,\n" + 
			 "       d.STANDARD,\n" + 
			 "       d.material,\n" + 
			 "       d.situation,\n" + 
			 "       d.reason,\n" + 
			 "       to_char(d.start_date, 'yyyy-MM-dd'),\n" + 
			 "       to_char(d.end_date, 'yyyy-MM-dd'),\n" + 
			 "       m.speciality_id,\n" + 
			 "       (SELECT s.speciality_name\n" + 
			 "          FROM run_c_specials s\n" + 
			 "         WHERE s.speciality_id = m.speciality_id\n" + 
			 "           AND s.is_use = 'Y') specialityName,\n" + 
			 "       m.workflow_no,\n" + 
			 "       m.workflow_status\n" + 
			 "  FROM RUN_J_REPAIR_PROJECT_DETAIL d,\n" + 
			 "       RUN_J_REPAIR_PROJECT_MAIN   m\n" + 
			 " WHERE d.project_main_id = m.project_main_id\n" + 
			 "   AND d.is_use = 'Y'\n" + 
			 "   AND m.is_use = 'Y'\n" + 
			 "   AND d.enterprise_code = '"+enterPriseCode+"'\n" + 
			 "   AND m.enterprise_code = '"+enterPriseCode+"'\n" + 
			 "   AND d.project_main_id in("+projectMainId+")";

		 
	       List list=bll.queryByNativeSQL(sql);
	      
	       result.setList(list);
		}
	    return result;
	}
	
	public String selectApproveByWorderCode(String workerCode) {
		String sql = "SELECT COUNT(1)\n" +
			"   FROM sys_j_ur a,\n" + 
			"        sys_c_ul b\n" + 
			"  WHERE a.worker_id = b.worker_id\n" + 
			"    AND a.is_use = 'Y'\n" + 
			"    AND b.is_use = 'Y'\n" + 
			"    AND b.worker_code = '"+workerCode+"'\n" + 
			"    AND a.role_id IN (SELECT t.role_id\n" + 
			"                        FROM wf_j_rrs t\n" + 
			"                       WHERE t.flow_type = 'bqRepairPlanApprove-v1.0'\n" + 
			"                         AND t.step_id IN (7, 8, 9))";

		Object countObj = bll.getSingal(sql);
		String countSql = (countObj == null) ? "" : countObj.toString();
		return countSql;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject getRepairQueryList(String tasklistId,String version ,String enterPriseCode)
	{
		PageObject result=new PageObject();
		String sql = "";
		String versionId="";
		String versionString=version.substring(0,1);
		if(versionString.equals("初")){
			versionId ="0"; //modify by fyyang 20100524
			}
		else if(versionString.equals("第")){
			versionId = version.substring(1, version.length()-2);
		}
		else {
			versionId=version;
		}
		if(tasklistId != null)
		{
		 sql= "SELECT d.project_detail_id,\n" +
			 "       d.project_main_id,\n" + 
			 "       d.repair_project_id,\n" + 
			 "       d.repair_project_name,\n" + 
			 "       d.working_charge,\n" + 
			 "       getworkername(d.working_charge),\n" + 
			 "       d.working_menbers,\n" + 
			 "       to_char(d.start_date, 'yyyy-MM-dd')start_date,\n" + 
			 "       to_char(d.end_date, 'yyyy-MM-dd')end_date,\n" + 
			 "       d.STANDARD,\n" + 
			 "       d.material,\n" + 
			 "       d.situation,\n" + 
			 "       d.reason,\n" + 
			 "       m.workflow_no,\n" + 
			 "       m.workflow_status,\n" + 
			 "       (select t.speciality_name from run_c_specials  t where t.speciality_id=m.speciality_id)specialityName \n" + 
			 "  FROM run_j_repair_project_main   m,\n" + 
			 "       RUN_J_REPAIR_PROJECT_DETAIL d\n" + 
			 " WHERE m.project_main_id = d.project_main_id\n" + 
			 "   AND m.tasklist_id = '"+tasklistId+"'\n" + 
			 "   AND m.version = '"+versionId+"'\n" + 
			 "   AND d.is_use = 'Y'\n" +  
			 "   AND d.enterprise_code = '"+enterPriseCode+"'\n" + 
			 "   ORDER BY m.speciality_id";

		
	       List list=bll.queryByNativeSQL(sql);
	       result.setList(list);
		}
	    return result;
	}
	
	public PageObject findHistoryInfo(String spId, String version) {
		PageObject result = new PageObject();
		String sql = "select a.speciality_id,\n"
				+ "       (select s.speciality_name\n"
				+ "          from run_c_specials s\n"
				+ "         where s.speciality_id = a.speciality_id\n"
				+ "           and s.is_use = 'Y'),\n"
				+ "       c.repair_project_name,\n"
				+ "       c.working_charge,\n"
				+ "       getworkername(c.working_charge),\n"
				+ "       c.working_menbers,\n" 
				+ "       c.working_time,\n"
				+ "       c.repair_project_id ,\n" +
						"c.acceptance_second ,\n" +// add by wpzhu 20100603 增加二级和三级验收人
						" getworkername(c.acceptance_second),\n" +
						"c.acceptance_third,\n" +
						" getworkername(c.acceptance_third)"
				+ "  from RUN_J_REPAIR_PROJECT_MAIN   A,\n"
				+ "       RUN_J_REPAIR_PROJECT_DETAIL B,\n"
				+ "       RUN_C_REPAIR_PROJECT        C\n"
				+ " where a.project_main_id = b.project_main_id\n"
				+ "   and b.repair_project_id = c.repair_project_id\n"
				+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
				+ "   and c.is_use = 'Y'";

		if (spId != null && !"".equals(spId))
			sql += "\n and a.speciality_id='" + spId + "'";
		if (version != null && !"".equals(version))
			sql += "\n and a.version='" + version + "'";

		List list = bll.queryByNativeSQL(sql);
		result.setList(list);
		return result;
	}
	
	/**
	 * add by fyyang 20100624
	 */
	@SuppressWarnings("unchecked")
	public PageObject findVersionBySpecial(String specialId,String enterpriseCode)
	{
		PageObject result=new PageObject();
		String sql=
			"select DISTINCT A.Version,\n" +
			"       decode(A.Version, 0, '初稿', '第' || A.Version || '版本')\n" + 
			"  from RUN_J_REPAIR_PROJECT_MAIN A\n" + 
			" where A.SPECIALITY_ID = "+specialId+"\n" + 
			"   and A.Enterprise_Code = '"+enterpriseCode+"'\n" + 
			"   and A.Is_Use = 'Y'";
		List list=bll.queryByNativeSQL(sql);
		result.setList(list);
		return result;

	}
	
	/**
	 * add by fyyang 20100624
	 */
	public String findNewVersionBySpecial(String specialId,String enterpriseCode)
	{
		String sql=
			"select max(A.Version)\n" +
			"  from RUN_J_REPAIR_PROJECT_MAIN A\n" + 
			" where A.SPECIALITY_ID = "+specialId+"\n" + 
			"   and A.Enterprise_Code = '"+enterpriseCode+"'\n" + 
			"   and A.Is_Use = 'Y'";
        Object obj=bll.getSingal(sql);
        if(obj!=null&&!obj.equals(""))
        {
        	return obj.toString();
        }
        else
        {
        	return "";
        }
	}
	public List getWBInfo(String projectMainId) {
		String sql = "SELECT t3.repair_project_name,\n" +
			"       t1.repair_type_id,\n" + 
			"       (SELECT s.speciality_code\n" + 
			"          FROM run_c_specials s\n" + 
			"         WHERE s.speciality_id = t1.speciality_id\n" + 
			"           AND s.is_use = 'Y'),\n" + 
			"       t2.working_charge,\n" + 
			"       getfirstlevelbyid((SELECT p.dept_id\n" + 
			"                           FROM hr_j_emp_info p\n" + 
			"                          WHERE p.emp_code = t1.fill_by)),\n" + 
			"       t2.start_date,\n" + 
			"       t2.end_date\n" + 
			"  FROM run_j_repair_project_main   t1,\n" + 
			"       RUN_J_REPAIR_PROJECT_DETAIL t2,\n" + 
			"       RUN_C_REPAIR_PROJECT        t3\n" + 
			" WHERE t1.project_main_id = t2.project_main_id\n" + 
			"   AND t2.repair_project_id = t3.repair_project_id\n" + 
			"   AND t1.is_use = 'Y'\n" + 
			"   AND t2.is_use = 'Y'\n" + 
			"   AND t1.project_main_id = "+projectMainId;
		List result = bll.queryByNativeSQL(sql);
		return result;
	}
	
}