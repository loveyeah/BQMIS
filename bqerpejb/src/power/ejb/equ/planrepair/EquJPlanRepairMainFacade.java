package power.ejb.equ.planrepair;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.equ.planrepair.form.EquPlanRepairMainForm;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity EquJPlanRepairMain.
 * 
 * @see power.ejb.equ.planrepair.EquJPlanRepairMain
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class EquJPlanRepairMainFacade implements EquJPlanRepairMainFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;

	public EquJPlanRepairMainFacade()
	{
		service = new WorkflowServiceImpl();
	}
	
	public EquJPlanRepairMain save(EquJPlanRepairMain entity) {
		try {
			entity.setRepairId(bll.getMaxId("EQU_J_PLAN_REPAIR_MAIN",
					"REPAIR_ID"));
			entity.setStatus("0");
			entity.setIsUse("Y");
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public boolean judgeAddPlanCode(String repairCode) {
		String sql = "select count(1)\n" + "  from equ_j_plan_repair_main t\n"
				+ " where t.is_use = 'Y'\n" + "   and t.repair_code = '"
				+ repairCode + "'";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}

	public boolean judgeUpdatePlanCode(String repairId, String repairCode) {
		String sql = "select count(1)\n" + "  from equ_j_plan_repair_main t\n"
				+ " where t.is_use = 'Y'\n" + "   and t.repair_code = '"
				+ repairCode + "'\n" + "   and t.repair_id != '" + repairId
				+ "'";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Delete a persistent EquJPlanRepairMain entity.
	 * 
	 * @param entity
	 *            EquJPlanRepairMain entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(EquJPlanRepairMain entity) {
		LogUtil.log("deleting EquJPlanRepairMain instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(EquJPlanRepairMain.class,
					entity.getRepairId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved EquJPlanRepairMain entity and return it or a
	 * copy of it to the sender. A copy of the EquJPlanRepairMain entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            EquJPlanRepairMain entity to update
	 * @return EquJPlanRepairMain the persisted EquJPlanRepairMain entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public EquJPlanRepairMain update(EquJPlanRepairMain entity) {
		try {
			EquJPlanRepairMain result = entityManager.merge(entity);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public EquJPlanRepairMain findById(Long id) {
		try {
			EquJPlanRepairMain instance = entityManager.find(
					EquJPlanRepairMain.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	



	@SuppressWarnings("unchecked")
	public PageObject getPlanRepairList(String startDate, String endDate,
			int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		String sql = "select t.*,\n"
				+ "       decode(t.status,\n"
				+ "              0,\n"
				+ "              '初始',\n"
				+ "              1,\n"
				+ "              '准备中',\n"
				+ "              2,\n"
				+ "              '准备完毕',\n"
				+ "              5,\n"
				+ "              '审批退回') statusName,\n"
				+ "       to_char(t.plan_start_time, 'yyyy-MM-dd HH:mm'),\n"
				+ "       to_char(t.plan_end_time, 'yyyy-MM-dd HH:mm'),\n"
				+ "       to_char(t.fill_date, 'yyyy-MM-dd HH:mm'),\n"
				+ "       getworkername(t.fill_by),\n"
				+ "       a.plan_type_name,\n"
				+ "       b.item_name\n"
				+ "  from equ_j_plan_repair_main t, equ_c_plan_type a, cbm_c_item b\n"
				+ " where t.is_use = 'Y'\n"
				+ "   and a.plan_type_id(+) = t.plan_type_id\n"
				+ "   and b.item_id(+) = t.fare_soruce\n"
				+ "   and t.status in (0, 1, 2, 5)\n"
				+ "   and to_char(t.fill_date, 'yyyy-MM-dd') >= '"+startDate+"'\n"
				+ "   and to_char(t.fill_date, 'yyyy-MM-dd') <= '"+endDate+"'";
		String sqlcount = "select count(1)\n"
				+ "  from equ_j_plan_repair_main t\n"
				+ " where t.is_use = 'Y'\n"
				+ "   and t.status in (0, 1, 2, 5)\n"
				+ "   and to_char(t.fill_date, 'yyyy-MM-dd') >= '"+startDate+"'\n"
				+ "   and to_char(t.fill_date, 'yyyy-MM-dd') <= '"+endDate+"'";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlcount).toString());
		List<EquPlanRepairMainForm> arrList = new ArrayList<EquPlanRepairMainForm>();
		Iterator it = list.iterator();
		if (count > 0) {
			while (it.hasNext()) {
				Object[] o = (Object[]) it.next();
				EquPlanRepairMainForm mForm = new EquPlanRepairMainForm();
				EquJPlanRepairMain model = new EquJPlanRepairMain();
				if (o[0] != null)
					model.setRepairId(Long.parseLong(o[0].toString()));
				if (o[1] != null)
					model.setRepairCode(o[1].toString());
				if (o[2] != null)
					model.setRepairName(o[2].toString());
				if (o[3] != null)
					model.setPlanTypeId(Long.parseLong(o[3].toString()));
				if (o[6] != null)
					model.setFare(Double.parseDouble(o[6].toString()));
				if (o[7] != null)
					model.setFareSoruce(Long.parseLong(o[7].toString()));
				if (o[8] != null)
					model.setContent(o[8].toString());
				if (o[9] != null)
					model.setRemark(o[9].toString());
				if (o[10] != null)
					model.setFillBy(o[10].toString());
				if (o[13] != null)
					model.setStatus(o[13].toString());
				if (o[16] != null)
					mForm.setStatusName(o[16].toString());
				if (o[17] != null)
					mForm.setPlanStartTime(o[17].toString());
				if (o[18] != null)
					mForm.setPlanEndTime(o[18].toString());
				if (o[19] != null)
					mForm.setFillDate(o[19].toString());
				if (o[20] != null)
					mForm.setFillByName(o[20].toString());
				if (o[21] != null)
					mForm.setPlanTypeName(o[21].toString());
				if (o[22] != null)
					mForm.setFareSoruceName(o[22].toString());
				mForm.setPrMain(model);
				arrList.add(mForm);
			}
			result.setList(arrList);
			result.setTotalCount(count);
			return result;
		} else {
			return null;
		}
	}

	public void reportPlanRepair(Long repairId, String nextRoles,
			String workercode,Long actionId, String approveText) {
		EquJPlanRepairMain model = this.findById(repairId);
		String workflowType = "bpPlanRepair";
	
		long entryId = service.doInitialize(workflowType, workercode, repairId.toString());
		service.doAction(entryId, workercode, actionId, approveText, null,
				nextRoles);
		model.setWorkFlowNo(entryId);
		model.setStatus("3");
		this.update(model);
	}
	
	///add  by fyyang 090925
	@SuppressWarnings("unchecked")
	private PageObject getListByWhere(String sqlWhere,int... rowStartIdxAndCount)
	{
		PageObject result = new PageObject();
		String sql = "select t.*,\n"
				+ "       decode(t.status,\n"
				+ "              0,\n"
				+ "              '初始',\n"
				+ "              1,\n"
				+ "              '准备中',\n"
				+ "              2,\n"
				+ "              '准备完毕',\n"
				+ "              3,'审批中',\n"
				+ "              4,'审批结束',\n"
				+ "              5,\n"
				+ "              '审批退回') statusName,\n"
				+ "       to_char(t.plan_start_time, 'yyyy-MM-dd'),\n"
				+ "       to_char(t.plan_end_time, 'yyyy-MM-dd'),\n"
				+ "       to_char(t.fill_date, 'yyyy-MM-dd'),\n"
				+ "       getworkername(t.fill_by),\n"
				+ "       a.plan_type_name,\n"
				+ "       b.item_name \n"
				+ "  from equ_j_plan_repair_main t, equ_c_plan_type a, cbm_c_item b\n"
				+ " where t.is_use = 'Y'\n"
				+ "   and a.plan_type_id(+) = t.plan_type_id\n"
				+ "   and b.item_id(+) = t.fare_soruce\n";
		String sqlcount = "select count(1)\n"
				+ "  from equ_j_plan_repair_main t\n"
				+ " where t.is_use = 'Y'\n";
		sql+=sqlWhere;
		sqlcount+=sqlWhere;
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlcount).toString());
		List<EquPlanRepairMainForm> arrList = new ArrayList<EquPlanRepairMainForm>();
		Iterator it = list.iterator();
		if (count > 0) {
			while (it.hasNext()) {
				Object[] o = (Object[]) it.next();
				EquPlanRepairMainForm mForm = new EquPlanRepairMainForm();
				EquJPlanRepairMain model = new EquJPlanRepairMain();
				if (o[0] != null)
					model.setRepairId(Long.parseLong(o[0].toString()));
				if (o[1] != null)
					model.setRepairCode(o[1].toString());
				if (o[2] != null)
					model.setRepairName(o[2].toString());
				if (o[3] != null)
					model.setPlanTypeId(Long.parseLong(o[3].toString()));
				if (o[6] != null)
					model.setFare(Double.parseDouble(o[6].toString()));
				if (o[7] != null)
					model.setFareSoruce(Long.parseLong(o[7].toString()));
				if (o[8] != null)
					model.setContent(o[8].toString());
				if (o[9] != null)
					model.setRemark(o[9].toString());
				if (o[10] != null)
					model.setFillBy(o[10].toString());
				if(o[12]!=null)
				{
					model.setWorkFlowNo(Long.parseLong(o[12].toString()));
				}
				if (o[13] != null)
					model.setStatus(o[13].toString());
				if (o[16] != null)
					mForm.setStatusName(o[16].toString());
				if (o[17] != null)
					mForm.setPlanStartTime(o[17].toString());
				if (o[18] != null)
					mForm.setPlanEndTime(o[18].toString());
				if (o[19] != null)
					mForm.setFillDate(o[19].toString());
				if (o[20] != null)
					mForm.setFillByName(o[20].toString());
				if (o[21] != null)
					mForm.setPlanTypeName(o[21].toString());
				if (o[22] != null)
					mForm.setFareSoruceName(o[22].toString());
				
				mForm.setPrMain(model);
				arrList.add(mForm);
			}
			result.setList(arrList);
			result.setTotalCount(count);
			return result;
		} else {
			return null;
		}
	}
	
	///add  by fyyang 090925
	public PageObject findRepairApproveList(String startDate, String endDate,String entryId,
			int... rowStartIdxAndCount)
	{
		if(entryId==null||entryId.equals(""))
		{
			return null;
		}
		
		String sqlWhere=" \n and t.work_flow_no in ("+entryId+") \n";
		if(startDate!=null&&!startDate.equals(""))
		{
			sqlWhere+="  and t.fill_date >=to_date('" + startDate
			+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if(endDate!=null&&!endDate.equals(""))
		{
			sqlWhere+="  and t.fill_date <=to_date('" + endDate
			+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		
		return this.getListByWhere(sqlWhere, rowStartIdxAndCount);
	}
	
	/**
	 * 执行
	 * 
	 * @param entryId
	 *            实例编号
	 * @param workerCode
	 *            审批人
	 * @param actionId
	 *            动作
	 * @param approveText
	 *            意见
	 */
	private void changeWfInfo(Long entryId, String workerCode, Long actionId,
			String approveText, String nextRoles) {
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
	}

	///审批签字
	public void planRepairSign(Long repairId, String approveText,
			String workerCode, Long actionId, String responseDate,
			String nextRoles, String eventIdentify)
	{
		EquJPlanRepairMain model=this.findById(repairId);
		if(eventIdentify.equals("WC"))
		{
			model.setStatus("4");
		}
		if(eventIdentify.equals("TH"))
		{
		  model.setStatus("5");	
		}
		this.update(model);
		this.changeWfInfo(model.getWorkFlowNo(), workerCode, actionId,
				approveText, nextRoles);
	}
//综合查询
	public PageObject getAllPlanRepairList(String startDate, String endDate,
			int... rowStartIdxAndCount){
		String sqlWhere = "";
		if(startDate!=null&&!startDate.equals(""))
		{
			sqlWhere+="  and t.fill_date >=to_date('" + startDate
			+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if(endDate!=null&&!endDate.equals(""))
		{
			sqlWhere+="  and t.fill_date <=to_date('" + endDate
			+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		return this.getListByWhere(sqlWhere, rowStartIdxAndCount);
	}
}