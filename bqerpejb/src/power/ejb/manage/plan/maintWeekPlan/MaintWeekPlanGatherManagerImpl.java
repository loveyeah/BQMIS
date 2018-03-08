package power.ejb.manage.plan.maintWeekPlan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.plan.maintWeekPlan.BpJPlanRepairGatherDetail;
import power.ejb.manage.plan.maintWeekPlan.BpJPlanRepairGather;
import power.ejb.manage.plan.maintWeekPlan.MaintWeekPlanGatherManager;

@Stateless
public class MaintWeekPlanGatherManagerImpl implements
		MaintWeekPlanGatherManager {
	@PersistenceContext
	private EntityManager entityManager;
	WorkflowService service;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public MaintWeekPlanGatherManagerImpl() {
		bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("NativeSqlHelper");
		service = new WorkflowServiceImpl();
	}

	public BpJPlanRepairGather findByGatherId(Long gatherId) {
		try {
			BpJPlanRepairGather model = entityManager.find(
					BpJPlanRepairGather.class, gatherId);
			return model;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public List<BpJPlanRepairGatherDetail> findDetailByGatherId(Long gatherId) {
		String sql =
			"select t.* from BP_J_PLAN_REPAIR_GATHER_DETAIL t where t.is_use='Y' and t.gather_id='"+gatherId+"'";
		List<BpJPlanRepairGatherDetail> instance = bll.queryByNativeSQL(sql, BpJPlanRepairGatherDetail.class);
		return instance;
	}

	public BpJPlanRepairGatherDetail findByGatherDetailId(Long planDetailId) {
		try {
			BpJPlanRepairGatherDetail instance = entityManager.find(
					BpJPlanRepairGatherDetail.class, planDetailId);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findRepairGatherList(String approve, String entryIds,
			String planTime, String planWeek,String queryKey, String enterpriseCode,
			int... rowStartIdxAndCount) throws Exception {
		String strWhere = " and a.enterprise_code='" + enterpriseCode + "'\n"
				+ " and b.enterprise_code='" + enterpriseCode + "'\n";

		if (planTime != null && !planTime.equals("")) {
			String priorStr = planTime.substring(0, 4);
			String priorStr2 = planTime.substring(5, 7);
			String planTimeStr = priorStr + priorStr2 + planWeek;
			strWhere += " and a.plan_time='" + planTimeStr + "'\n";
		}

		if (queryKey != null && !queryKey.equals("")) {
			strWhere += "  and b.content like'%" + queryKey + "%'\n";
		}

		if (approve != null && !approve.equals("")) {
			if (approve.equals("Y")) {
				strWhere += " and a.work_flow_no in (" + entryIds + ")";

			}
			if (approve.equals("N")) {
				strWhere += " and (a.sign_status =0 or a.sign_status =3 or a.sign_status is null)\n";
			}
		}

		PageObject pg = new PageObject();
		String sql = "select b.plan_detail_id,\n"
				+ "b.gather_id,\n"
				+ "b.content,\n"
				+ "b.charge_dep,\n"
				+ "getdeptname(b.charge_dep)chargeDeptName,\n"
				+ "b.assort_dep,\n"
				+ "getdeptname(b.assort_dep)assortDeptName,\n"
				+ "to_char(b.begin_time,'yyyy-mm-dd')begin_time,\n"
				+ "to_char(b.end_time,'yyyy-mm-dd')end_time,\n"
				+ "b.days,\n"
				+ "b.memo,\n"
				+ "a.plan_time,\n"
				+ "a.gather_dep,\n"
				+ "getdeptname(a.gather_dep)gatherDeptName,\n"
				+ "a.gather_by,\n"
				+ "getworkername(a.gather_by)gatherName,\n"
				+ "to_char(a.gather_date,'yyyy-mm-dd')gather_date,\n"
				+ "a.work_flow_no,\n"
				+ "a.sign_status,\n"
				+ "a.memo,\n"
				+ "to_char(a.week_start_time,'yyyy-mm-dd')week_start_time,\n"
				+ "to_char(a.week_end_time,'yyyy-mm-dd')week_end_time\n"
				+ "from BP_J_PLAN_REPAIR_GATHER a, BP_J_PLAN_REPAIR_GATHER_DETAIL b\n"
				+ "where b.is_use='Y'and a.gather_id=b.gather_id";

		if (strWhere != null && !strWhere.equals("")) {
			sql += strWhere;
		}
		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += " order by b.charge_dep";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	// 明细
	public BpJPlanRepairGatherDetail addRepairGatherDetail(
			BpJPlanRepairGatherDetail entity, String planTime, Long mainId)
			throws ParseException, CodeRepeatException {

		entity.setGatherId(mainId);
		Long id = bll.getMaxId("BP_J_PLAN_REPAIR_GATHER_DETAIL",
				"PLAN_DETAIL_ID");
		entity.setPlanDetailId(id);
		entity.setIsUse("Y");
		entityManager.persist(entity);
		entityManager.flush();

		return entity;
	}

	public BpJPlanRepairGatherDetail updateRepairGatherDetail(
			BpJPlanRepairGatherDetail entity, Long mainId)
			throws ParseException, CodeRepeatException {
		try {
			entityManager.merge(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}

	}

	public BpJPlanRepairGatherDetail delRepairGatherDetail(String ids) {
		try {
			String sql = "UPDATE BP_J_PLAN_REPAIR_GATHER_DETAIL t\n"
					+ "   SET t.is_use = 'N'\n"
					+ " WHERE t.plan_detail_id IN (" + ids + ")";
			bll.exeNativeSQL(sql);
			return null;
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}

	}

	public void save(List<BpJPlanRepairGatherDetail> addList,
			List<BpJPlanRepairGatherDetail> updateList, String deleteId,
			String planTime, String planWeek, String gatherDept,
			String weekStartTime, String weekEndTime, String totalMemo,
			String workerCode, String enterpriseCode) throws ParseException,
			CodeRepeatException {
		Long mainId;
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		if (this.addRepairGatherMain(planTime,planWeek) == null) {
			// 增加主表
			BpJPlanRepairGather model = new BpJPlanRepairGather();
			mainId = bll.getMaxId("BP_J_PLAN_REPAIR_GATHER", "gather_id");
			String priorStr = planTime.substring(0, 4);
			String priorStr2 = planTime.substring(5, 7);
			model.setGatherId(mainId);
			model.setSignStatus(0l);
			model.setGatherDep(gatherDept);
			model.setGatherBy(workerCode);
			model.setGatherDate(new Date());
			model.setEnterpriseCode(enterpriseCode);
			model.setPlanTime(priorStr + priorStr2 + planWeek);
			if (weekStartTime != null && !weekStartTime.equals(""))
				model.setWeekStartTime(df.parse(weekStartTime));
			if (weekEndTime != null && !weekEndTime.equals(""))
				model.setWeekEndTime(df.parse(weekEndTime));
			model.setMemo(totalMemo);
			entityManager.persist(model);
		} else {
			mainId = this.addRepairGatherMain(planTime,planWeek);

		}

		if (addList.size() > 0) {

			for (BpJPlanRepairGatherDetail entity : addList) {
				this.addRepairGatherDetail(entity, planTime, mainId);
			}
		}
		if (updateList.size() > 0) {
			for (BpJPlanRepairGatherDetail entity : updateList) {
				this.updateRepairGatherDetail(entity, mainId);
			}
		}
		if (deleteId.length() > 0) {
			this.delRepairGatherDetail(deleteId);
		}
	}

	private Long addRepairGatherMain(String planTime,String planWeek) {
		String priorStr = planTime.substring(0, 4);
		String priorStr2 = planTime.substring(5, 7);
		String planTimeStr = priorStr + priorStr2 + planWeek;
		String sql = "select t.gather_id from BP_J_PLAN_REPAIR_GATHER t where t.plan_time='"
				+ planTimeStr + "'";
		Long mainId = (bll.getSingal(sql) == null) ? 0 : Long.parseLong(bll
				.getSingal(sql).toString());
		if (mainId > 0) {
			return mainId;
		} else {
			return null;
		}

	}

	public BpJPlanRepairGather updateRepairGatherMain(BpJPlanRepairGather entity) {
		try {
			BpJPlanRepairGather model = entityManager.merge(entity);
			return model;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			return null;
		}
	}

	public void RepairGatherReport(Long gatherId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType) {
		BpJPlanRepairGather model = findByGatherId(gatherId);
		Long entryId;
		if (model.getWorkFlowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, gatherId
					.toString());
			model.setWorkFlowNo(entryId);
		} else {
			entryId = model.getWorkFlowNo();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		model.setSignStatus(1l);
		updateRepairGatherMain(model);
	}

	public void RepairGatherApprove(Long gatherId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles) {
		BpJPlanRepairGather model = findByGatherId(gatherId);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 52l || actionId == 42l||actionId==122l||actionId==132l) {
			model.setSignStatus(3l);
		} else if (actionId == 53l||actionId==133l) {
			model.setSignStatus(2l);
		}else if(actionId==412l||actionId==1213l||actionId==135l)
		{
			model.setSignStatus(1l);
		}
		updateRepairGatherMain(model);
	}

	public PageObject getRepairApproveList(String status, String entryIds,
			String planTime,String planWeek, String enterpriseCode, int... rowStartIdxAndCount)
			throws Exception {
		String strWhere = " and a.enterprise_code='" + enterpriseCode + "'\n"
				+ " and b.enterprise_code='" + enterpriseCode + "'\n";
	
		if (planTime != null && !planTime.equals("")) {
			String priorStr = planTime.substring(0, 4);
			String priorStr2 = planTime.substring(5, 7);
			String planTimeStr = priorStr + priorStr2 + planWeek;
			strWhere += " and a.plan_time='" + planTimeStr + "'\n";
		}

		if (status != null && !status.equals("")) {
			strWhere += " and a.status in (" + status + ")\n";
		}
		PageObject pg = new PageObject();
		String sql ="select b.plan_detail_id,\n" +
			"b.plan_dep_id,\n" + 
			"b.content,\n" + 
			"b.charge_dep,\n" + 
			"getdeptname(b.charge_dep)chargeDeptName,\n"+ 
			"b.assort_dep,\n"+ 
			"getdeptname(b.assort_dep)assortDeptName,\n" + 
			"to_char(b.begin_time,'yyyy-mm-dd')begin_time,\n" + 
			"to_char(b.end_time,'yyyy-mm-dd')end_time,\n" + 
			"b.days,\n" + 
			"b.memo,\n" + 
			"a.plan_time,\n" + 
			"a.edit_dep,\n" + 
			"getdeptname(a.edit_dep)editDeptName,\n" + 
			"a.edit_by,\n" + 
			"getworkername(a.edit_by)editrName,\n" + 
			"to_char(a.edit_date,'yyyy-mm-dd')edit_date,\n" + 
			"a.work_flow_no,\n" + 
			"a.status\n" + 
			"from BP_J_PLAN_REPAIR_DEP a, BP_J_PLAN_REPAIR_DETAIL b\n" + 
			"where b.is_use='Y'and a.plan_dep_id=b.plan_dep_id";


		if (strWhere != null && !strWhere.equals("")) {
			sql += strWhere;
		}
		String sqlCount = "select count(*) from (" + sql + ") \n";
		sql += " order by b.charge_dep";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}

	public String getRepairGatherApproveList(String enterpriseCode) {
		String sql = "select max(a.plan_time) from BP_J_PLAN_REPAIR_GATHER a\n"
				+ "where a.enterprise_code='"+enterpriseCode+"'\n"
				+ "and a.sign_status in(1)";

		Object maxNoObj = bll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}
	
	public String getRepairApproveGatherId(String planTime,String planWeek,String enterpriseCode) {
		String priorStr = planTime.substring(0, 4);
		String priorStr2 = planTime.substring(5, 7);
		String planTimeStr = priorStr + priorStr2 + planWeek;
		String sql = "select a.gather_id from bp_j_plan_repair_gather a\n"
			+ "where a.plan_time='"+planTimeStr+"'\n"
			+ "and a.enterprise_code='"+enterpriseCode+"'\n"
			+ "and a.sign_status in(1)";

		Object maxNoObj = bll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}
	
}
