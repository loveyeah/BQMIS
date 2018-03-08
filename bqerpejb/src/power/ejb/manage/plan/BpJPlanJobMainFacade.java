package power.ejb.manage.plan;

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

import power.ear.comm.LogUtil;
import power.ejb.comm.NativeSqlHelperRemote;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

/**
 * Facade for entity BpJPlanJobMain.
 * 
 * @see power.ejb.manage.plan.BpJPlanJobMain
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJPlanJobMainFacade implements BpJPlanJobMainFacadeRemote {
	// property constants
	public static final String EDIT_BY = "editBy";
	public static final String WORK_FLOW_NO = "workFlowNo";
	public static final String SIGN_STATUS = "signStatus";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	WorkflowService service;

	public BpJPlanJobMainFacade() {
		service = new WorkflowServiceImpl();
	}

	/**
	 * Perform an initial save of a previously unsaved BpJPlanJobMain entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJPlanJobMain entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public BpJPlanJobMain save(BpJPlanJobMain entity) {
		LogUtil.log("saving BpJPlanJobMain instance", Level.INFO, null);
		try {
			entityManager.persist(entity);

			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpJPlanJobMain entity.
	 * 
	 * @param entity
	 *            BpJPlanJobMain entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanJobMain entity) {
		LogUtil.log("deleting BpJPlanJobMain instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJPlanJobMain.class, entity
					.getPlanTime());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpJPlanJobMain entity and return it or a copy
	 * of it to the sender. A copy of the BpJPlanJobMain entity parameter is
	 * returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanJobMain entity to update
	 * @return BpJPlanJobMain the persisted BpJPlanJobMain entity instance, may
	 *         not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanJobMain update(BpJPlanJobMain entity) {
		LogUtil.log("updating BpJPlanJobMain instance", Level.INFO, null);
		try {
			BpJPlanJobMain result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJPlanJobMain findById(Date id) {
		LogUtil.log("finding BpJPlanJobMain instance with id: " + id,
				Level.INFO, null);
		try {
			BpJPlanJobMain instance = entityManager.find(BpJPlanJobMain.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpJPlanJobMain entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanJobMain property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanJobMain> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanJobMain> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpJPlanJobMain instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanJobMain model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	private void changeWfInfo(Long entryId, String workerCode, Long actionId,
			String approveText, String nextRoles) {
		try {
			service.doAction(entryId, workerCode, actionId, approveText, null,
					"", nextRoles);
		} catch (RuntimeException e) {
			e.printStackTrace();
		}
	}

	// 审批步骤：立项 上报
	// 对应信息：编辑页面点“上报”→4
//nextRoles 代表下步审批人
	public BpJPlanJobMain reportTo(String prjNo, String workflowType,
			String workerCode, String actionId, String approveText,
			String nextRoles, String enterpriseCode) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyyMM");
		Date planDate = sdf.parse(prjNo);
		String prjNo1 = sdf1.format(planDate);
		BpJPlanJobMain entity = findById(planDate);
		try {

			if (entity.getWorkFlowNo() == null) {
				// 处理未上报
				if (!workflowType.equals("")) {
					long entryId = service.doInitialize(workflowType,
							workerCode, prjNo.substring(2));
					long actionIdl = Long.parseLong(actionId);
					service.doAction(entryId, workerCode, actionIdl,
							approveText, null, "", nextRoles);

					entity.setWorkFlowNo(entryId);
					entity.setSignStatus(1l);

				}
			} else {
				// 处理已退回
				long entryId = service.doInitialize(workflowType, workerCode,
						entity.getPlanTime().toString());
				long actionIdl = Long.parseLong(actionId);
				service.doAction(entryId, workerCode, actionIdl, approveText,
						null,"", nextRoles);
				entity.setWorkFlowNo(entryId);
				entity.setSignStatus(1l);

			}
			update(entity);
			// 修改各部门计划状态
			// String sqlString = "update bp_j_plan_job_dep_main t "
			// + " set t.plan_status='" + getplanStatus(actionId) + "'"
			// + " where t.plan_time=" + "to_date('" + prjNo
			// + "'||'-01','yyyy-MM-dd ') " +
			//
			// " and t.enterprise_code='" + enterpriseCode + "'";
			// bll.exeNativeSQL(sqlString);

		} catch (Exception e) {

		}
		return entity;
	}

	/**
	 * @param prjNo
	 *            项目编号
	 * @param workflowType
	 *            工作流流程
	 * @param entryId
	 *            实例编号
	 * @param workerCode
	 *            审批人
	 * @param actionId
	 *            动作
	 * @param approveText
	 *            意见
	 * @param nextRoles
	 *            下一步角色
	 * 
	 * 审批步骤：部门工作计划 审批
	 * 
	 * 对应信息：流程 actionId
	 * 
	 * 
	 * 
	 * →上报→24 →计划专工审批退回→42
	 * 
	 * →→计划专工审批通过→43
	 * @throws ParseException
	 * 
	 * 
	 * 
	 */

	// 立项审批
	public BpJPlanJobMain approveStep(String prjNo, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date planDate = sdf.parse(prjNo);
		BpJPlanJobMain model = findById(planDate);
		try {
			long actionIdl = Long.parseLong(actionId);
			this.changeWfInfo(model.getWorkFlowNo(), workerCode, actionIdl,
					approveText, nextRoles);
			model.setSignStatus(getapproveStatus(stepId, actionId));

			update(model);
			// 修改各部门计划状态
//			String sqlString = "update  bp_j_plan_job_dep_main t "
//					+ "  set t.plan_status='" + getplanStatus(actionId) + "'"
//					+ " where t.plan_time=" + "to_date('" + prjNo
//					+ "'||'-01','yyyy-MM-dd ') " +
//
//					" and t.enterprise_code='" + enterpriseCode + "'";
//			bll.exeNativeSQL(sqlString);

		} catch (Exception e) {

		}
		return model;
	}

	private Long getapproveStatus(String stepID, String actionId) {
		switch (Integer.parseInt(actionId)) {
		case 42:
			return 3l;
		case 52:
			return 3l;
		case 62:
			return 3l;
		case 72:
			return 3l;
		case 73:
			return 2l;
		default:
			return 0l;
		}

	}

//	private Long getplanStatus(String actionId) {
//
////		 int idInt = Integer.parseInt(actionId);
////		 if (idInt == 42 || idInt == 52)
////		 return 2l;
////		
////		 else if (idInt == 24 || idInt == 45)
////		 return 3l;
////		 else if (idInt == 53)
////		
////		 return 4L;
////		 else {
////		 return 0L;
////		 }
//		switch (Integer.parseInt(actionId)) {
//		case 42:
//			return 3l;
//		case 52:
//			return 3l;
//		case 62:
//			return 3l;
//
//		case 63:
//			return 4l;
//
//		default:
//			return 2l;
//		}
//
//	}

	/**
	 * Find all BpJPlanJobMain entities.
	 * 
	 * @return List<BpJPlanJobMain> all BpJPlanJobMain entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanJobMain> findAll() {
		LogUtil.log("finding all BpJPlanJobMain instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanJobMain model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	// 计划完成情况整理上报
	public void allDeptWorkFinishReport(String planTime, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date planDate = sdf.parse(planTime);
		BpJPlanJobMain entity = findById(planDate);
		Long entryId = null;
			if (entity.getFinishWorkFlowNo() == null) {
				// 处理未上报
					 entryId = service.doInitialize(workflowType,workerCode, planTime.substring(2));
					 entity.setFinishWorkFlowNo(entryId);
			} else {
				// 处理已退回
				entryId = entity.getFinishWorkFlowNo();
			}
			service.doAction(entryId, workerCode, actionId, approveText,
					null, nextRoles,"");
			entity.setFinishSignStatus(1l);
			update(entity);
	
	}
	
	public void allDeptWorkFinishApprove(String planTime, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles) throws ParseException{
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date planDate = sdf.parse(planTime);
		BpJPlanJobMain model = findById(planDate);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 42l||actionId == 52l) {
			model.setFinishSignStatus(3l);
		} else if (actionId == 53l) {
			model.setFinishSignStatus(2l);
		}
		update(model);
	}
	
	public String findNoReadInfo(String planTime,String flag)
	{

	String sql=	"";
	if(flag.equals("2"))
	{
	sql+=	"select nvl(sum(decode(t.finish_sign_status,11,1,0)),0)-nvl(sum(1),-1)\n" +
	" from bp_j_plan_job_dep_main t where t.finish_sign_status<>13\n";
	}
	else if(flag.equals("1"))
	{
		sql+=	"select nvl(sum(decode(t.sign_status,11,1,0)),0)-nvl(sum(1),-1)\n" +
		" from bp_j_plan_job_dep_main t  where t.sign_status<>13 \n";
	}
		sql+=
		" and  to_char(t.plan_time,'yyyy-MM')='"+planTime+"'";

	String num= bll.getSingal(sql).toString();
	if(num.equals("0"))
	{
		return "1";
	}
	else
	{
		return "2";
	}

	}
}