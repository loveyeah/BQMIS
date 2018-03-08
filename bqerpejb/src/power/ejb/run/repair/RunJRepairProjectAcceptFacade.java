package power.ejb.run.repair;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.contract.form.ConApproveForm;
import power.ejb.run.protectinoutapply.RunJProtectinoutApprove;
import power.ejb.run.protectinoutapply.RunJProtectinoutapply;
import power.ejb.run.repair.form.RepairAcceptForm;

/**
 * Facade for entity RunJRepairProjectAccept.
 * 
 * @see power.ejb.run.repair.RunJRepairProjectAccept
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJRepairProjectAcceptFacade implements
		RunJRepairProjectAcceptFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;

	public RunJRepairProjectAcceptFacade() {
		service = new WorkflowServiceImpl();
	}

	public RunJRepairProjectAccept save(RunJRepairProjectAccept entity) {
		try {
			entity.setAcceptId(bll.getMaxId("RUN_J_REPAIR_PROJECT_ACCEPT",
					"accept_id"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			throw re;
		}
	}

	public void delete(RunJRepairProjectAccept entity) {
		LogUtil.log("deleting RunJRepairProjectAccept instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(RunJRepairProjectAccept.class,
					entity.getAcceptId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJRepairProjectAccept update(RunJRepairProjectAccept entity) {
		LogUtil.log("updating RunJRepairProjectAccept instance", Level.INFO,
				null);
		try {
			RunJRepairProjectAccept result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJRepairProjectAccept findById(Long id) {
		LogUtil.log("finding RunJRepairProjectAccept instance with id: " + id,
				Level.INFO, null);
		try {
			RunJRepairProjectAccept instance = entityManager.find(
					RunJRepairProjectAccept.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject getRepairItemList(String workflowStatus,
			String enterPriseCode, String year, String repairType,
			String edition, final int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		String sql = "select (select s.speciality_name\n"
				+ "          from run_c_specials s\n"
				+ "         where s.speciality_id = a.speciality_id\n"
				+ "           and s.is_use = 'Y'),\n"
				+ "       p.repair_project_name,\n"
				+ "       d.working_charge,\n"
				+ "       getworkername(d.working_charge),\n"
				+ "       d.working_menbers,\n"
				+ "       to_char(d.start_date, 'yyyy-MM-dd'),\n"
				+ "       to_char(d.end_date, 'yyyy-MM-dd'),\n"
				+ "       p.acceptance_level,\n" + "       a.speciality_id,\n"
				+ "       a.project_main_year,\n"
				+ "       a.repair_type_id,\n"
				+ "       (select t.repair_type_name\n"
				+ "          from RUN_C_REPAIR_TYPE t\n"
				+ "         where t.repair_type_id = a.repair_type_id\n"
				+ "           and t.is_use = 'Y') repairTypeName,\n"
				+ "       decode(a.version,'0','初版','第'||a.version||'版本')as version\n"
				+ "  from RUN_J_REPAIR_PROJECT_DETAIL d,\n"
				+ "       run_c_repair_project        p,\n"
				+ "       run_j_repair_project_main   a\n"
				+ " where d.repair_project_id = p.repair_project_id\n"
				+ "   and a.project_main_id = d.project_main_id\n"
				+ "   and p.is_use = 'Y'\n" + "   and d.is_use = 'Y'\n"
				+ "   and a.is_use = 'Y'\n" + "   and a.enterprise_code = '"
				+ enterPriseCode + "'\n" + "   and d.enterprise_code = '"
				+ enterPriseCode + "'";
		if (workflowStatus != null && !"".equals(workflowStatus)) {
			sql += "   and a.workflow_status = '" + workflowStatus + "'\n";
		}
		if (year != null && year.length() > 0) {
			sql += " and a.project_main_year='" + year + "'\n";
		}
		if (repairType != null && repairType.length() > 0) {
			sql += " and a.repair_type_id =" + repairType + "\n";
		}
		if (edition != null && edition.length() > 0) {
			sql += "   and a.version like '%" + edition + "%'\n";
		}
		String sqlCount = "select count(1) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setList(list);
		result.setTotalCount(totalCount);
		return result;
	}

	@SuppressWarnings("unchecked")
	public PageObject getRepairAcceptList(String workflowStatus,
			String enterPriseCode, String workerCode, String year,
			String projectName, String startDate, String endDate,
			String isFillBy,String entryIds, String fillSdate,String 
			fillEdate,final int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		String sql = "select t.accept_id,\n"
				+ "       t.repair_project_name,\n"
				+ "       getworkername(t.working_charge),\n"
				+ "       t.working_charge,\n" + "       t.working_menbers,\n"
				+ "       to_char(t.start_time, 'yyyy-mm-dd'),\n"
				+ "       to_char(t.end_time, 'yyyy-mm-dd'),\n"
				+ "       to_char(t.start_time, 'yyyy-mm-dd') || '----' ||\n"
				+ "       to_char(t.end_time, 'yyyy-mm-dd'),\n"
				+ "       (select s.speciality_name\n"
				+ "          from run_c_specials s\n"
				+ "         where s.speciality_id = t.speciality_id\n"
				+ "           and s.is_use = 'Y'),\n"
				+ "       t.speciality_id,\n" 
				+ "       t.memo,\n"
				+ "       t.completion,\n" 
				+ "       decode(t.acceptance_level, '2', '二级', '3', '三级'),\n"
				+ "       t.workflow_no,\n"
				+ "       t.workflow_status,\n"
				+ "       t.plan_start_date,\n"
				+ "       t.plan_end_date,\n"
				+ "       to_char(t.plan_start_date, 'yyyy-mm-dd') || '----' ||to_char(t.plan_end_date, 'yyyy-mm-dd'),\n"
			    +"       t.fill_by,\n" + 
				"        getworkername(t.fill_by),\n" + 
				"        to_char(t.fill_date,'yyyy-MM-dd'),\n" + 
				"        t.acceptance_level\n" +
				 "  from RUN_J_REPAIR_PROJECT_ACCEPT t\n"
				+ " where t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterPriseCode + "'";
		if ("1".equals(isFillBy)) {
			sql += " and t.fill_by = '" + workerCode + "'\n";
		}
		if ("0".equals(workflowStatus)) {
			//sql += " and t.workflow_status in('0','5')\n";
			sql += " and (t.workflow_status ='0' or t.workflow_status ='5' or t.workflow_status is null)\n";
		}else if(workflowStatus != null && workflowStatus.equals("approve"))
		{
			if (entryIds != null) {
				String inEntriyId = bll.subStr(entryIds, ",", 500,
						"t.workflow_no");
				sql += " \n and " + inEntriyId;
				sql += " \n and workflow_status in(1,2,3)";
			}
			else {
				sql += " \n and m.workflow_no=null";
			}
		//	sql+=" and t.workflow_no in (" + entryIds + ")";
			//sql+=" and (t.workflow_status ='1' or t.workflow_status ='2' or t.workflow_status='3')\n";
		}
		if (fillSdate != null && !fillSdate.equals("")) {
			sql += "   and t.fill_date >=to_date('" + fillSdate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (fillEdate != null && !fillEdate.equals("")) {
			sql += "   and t.fill_date <=to_date('" + fillEdate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (year != null && year.length() > 0) {
			sql += " and t.project_main_year='" + year + "'\n";
		}
		if (projectName != null && projectName.length() > 0) {
			sql += " and t.repair_project_name like '%" + projectName + "%'\n";
		}
		if (startDate != null && startDate.length() > 0) {
			sql += "    and to_char(t.start_time, 'yyyy-mm-dd') >= '"
					+ startDate + "'\n";
		}
		if (endDate != null && endDate.length() > 0) {
			sql += "    and to_char(t.end_time, 'yyyy-mm-dd') <= '" + endDate
					+ "'\n";
		}
		String sqlCount = "select count(1) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setList(list);
		result.setTotalCount(totalCount);
		return result;
	}

	@SuppressWarnings("unchecked")
	public void repairAcceptApprove(Long acceptId, Long actionId, Long entryId,
			String eventIdentify, String workerCode, String approveText,
			String nextRoles) {
		RunJRepairProjectAccept model = entityManager.find(
				RunJRepairProjectAccept.class, acceptId);
		if (eventIdentify.equals("TH")) {
			model.setWorkflowStatus("5");
		} else {
			if (model.getWorkflowStatus().equals("1")) {
				if (eventIdentify.equals("TY")) {
					model.setWorkflowStatus("2");
				}
			} else if (model.getWorkflowStatus().equals("2")) {
				if (eventIdentify.equals("XYSJYS")
						&& model.getAcceptanceLevel().equals("3")) {
					model.setWorkflowStatus("3");
				} else if (eventIdentify.equals("BXYSJYS（ZJ）")) {
					model.setWorkflowStatus("4");
				}
			} else if (model.getWorkflowStatus().equals("3")) {
				model.setWorkflowStatus("4");
			}
		}
		
		boolean checkLevel = true;
		if(model.getAcceptanceLevel() != null && model.getAcceptanceLevel().equals("2"))
			checkLevel = false;
		Map m = new java.util.HashMap();
		m.put("ACCEPTANCE_LEVEL", checkLevel);
		service.doAction(entryId,workerCode,actionId,approveText,m,"",nextRoles);
		
		entityManager.merge(model);
	}

	public void reportTo(String protectNo, String workflowType,
			String workerCode, Long actionId, String approveText,
			String nextRoles,String nextRolePs, String eventIdentify, String enterpriseCode) {
		RunJRepairProjectAccept entity = this.findById(Long
				.parseLong(protectNo));
		if (entity.getWorkflowNo() != null) {
			// 已退回的票
			service.doAction(entity.getWorkflowNo(), workerCode, actionId, approveText,
					null,nextRoles,nextRolePs);
		} else {
			// 未上报的票
			if (!workflowType.equals("")) {
				long entryId = service.doInitialize(workflowType, workerCode,
						protectNo);
				service.doAction(entryId, workerCode, actionId, approveText,
						null,nextRoles,nextRolePs);
				entity.setWorkflowNo(entryId);
			}
		}
		entity.setWorkflowStatus("1"); // 1---已上报
		this.update(entity);
	}
	
	@SuppressWarnings("unchecked")
	public List<RepairAcceptForm> getRepairAcceptBase(String acceptId)
	{
		List<RepairAcceptForm> baseList = new ArrayList<RepairAcceptForm>();
		
		String sql = "select t.accept_id,\n" +
			"       t.repair_project_name,\n" + 
			"       to_char(t.plan_start_date, 'yyyy-mm-dd') || '----' ||\n" + 
			"       to_char(t.plan_end_date, 'yyyy-mm-dd'),\n" + 
			"       to_char(t.start_time, 'yyyy-mm-dd') || '----' ||\n" + 
			"       to_char(t.end_time, 'yyyy-mm-dd'),\n" + 
			"       t.memo,\n" + 
			"       t.completion,\n" + 
			"       t.workflow_no,\n" + 
			"       t.workflow_status\n" + 
			"  from RUN_J_REPAIR_PROJECT_ACCEPT t\n" + 
			" where t.is_use = 'Y'\n" + 
			"   and t.accept_id = "+acceptId+"";

		List list = bll.queryByNativeSQL(sql);
		Iterator it = list.iterator();
		while (it.hasNext()) {
			RepairAcceptForm model = new RepairAcceptForm();
			Object[] data = (Object[]) it.next();
			if(data[0] != null)
				model.setAcceptId(Long.parseLong(data[0].toString()));
			if(data[1] != null)
				model.setRepairProjectName(data[1].toString());
			if(data[2] != null)
				model.setPlanStartEndDate(data[2].toString());
			if(data[3] != null)
				model.setStartEndTime(data[3].toString());
			if(data[4] != null)
				model.setMemo(data[4].toString());
			if(data[5] != null)
				model.setCompletion(data[5].toString());
			
			baseList.add(model);
		}
		return baseList;
	}
	
	@SuppressWarnings("unchecked")
	public List<ConApproveForm>  getRepairApprove(Long acceptId)
	{
		Long entryId = this.findById(acceptId).getWorkflowNo();
		List list = new ArrayList();
		String sql = 
			"SELECT t.*,getworkername(t.caller)\n" +
			" FROM wf_j_historyoperation t\n" + 
			" WHERE t.entry_id = "+entryId+"\n" + 
			"AND t.step_id in (4,5,6)\n" + 
			" AND opinion_time >\n" + 
			"(SELECT MAX(opinion_time)\n" + 
			"FROM wf_j_historyoperation t\n" + 
			" WHERE t.entry_id = "+entryId+"\n" + 
			"AND t.step_id = '2')\n" + 
			" ORDER BY t.opinion_time";

		list = bll.queryByNativeSQL(sql);
		List<ConApproveForm> arraylist = new ArrayList<ConApproveForm>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			Object[] data = (Object[]) it.next();
			ConApproveForm model = new ConApproveForm();
			if (data[2] != null) {
				model.setId(Long.parseLong(data[2].toString()));
			}
			if (data[3] != null) {
				model.setStepName(data[3].toString());
			}
			if(data[10] !=null)
				model.setCaller(data[10].toString());
			if (data[9] != null) {
				model.setOpinionTime(data[9].toString());
			}
			if(data[8]!=null){
				model.setOpinion(data[8].toString());
			}
			arraylist.add(model);
		}
		return arraylist;
	}
}