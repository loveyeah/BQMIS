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
import power.ejb.manage.plan.form.BpJPlanGuideMainForm;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

/**
 * Facade for entity BpJPlanGuideMain.
 * 
 * @see power.ejb.manage.plan.BpJPlanGuideMain
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJPlanGuideMainFacade implements BpJPlanGuideMainFacadeRemote {
	// property constants
	public static final String EDIT_BY = "editBy";
	public static final String PLAN_STATUS = "planStatus";
	public static final String WORK_FLOW_NO = "workFlowNo";
	public static final String SIGN_STATUS = "signStatus";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;

	/**
	 * Perform an initial save of a previously unsaved BpJPlanGuideMain entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJPlanGuideMain entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public BpJPlanGuideMainFacade() {
		service = new WorkflowServiceImpl();
	}

	public BpJPlanGuideMain save(BpJPlanGuideMain entity) {
		LogUtil.log("saving BpJPlanGuideMain instance", Level.INFO, null);
		try {
			entityManager.persist(entity);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpJPlanGuideMain entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideMain entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanGuideMain entity) {
		LogUtil.log("deleting BpJPlanGuideMain instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJPlanGuideMain.class, entity
					.getPlanTime());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpJPlanGuideMain entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanGuideMain entity parameter
	 * is returned when the JPA persistence mechanism has not previously been
	 * tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanGuideMain entity to update
	 * @return BpJPlanGuideMain the persisted BpJPlanGuideMain entity instance,
	 *         may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanGuideMain update(BpJPlanGuideMain entity) {
		LogUtil.log("updating BpJPlanGuideMain instance", Level.INFO, null);
		try {
			BpJPlanGuideMain result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJPlanGuideMain findById(Date id) {
		LogUtil.log("finding BpJPlanGuideMain instance with id: " + id,
				Level.INFO, null);
		try {
			BpJPlanGuideMain instance = entityManager.find(
					BpJPlanGuideMain.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpJPlanGuideMain entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanGuideMain property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanGuideMain> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanGuideMain> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpJPlanGuideMain instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanGuideMain model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<BpJPlanGuideMain> findByEditBy(Object editBy) {
		return findByProperty(EDIT_BY, editBy);
	}

	public List<BpJPlanGuideMain> findByPlanStatus(Object planStatus) {
		return findByProperty(PLAN_STATUS, planStatus);
	}

	public List<BpJPlanGuideMain> findByWorkFlowNo(Object workFlowNo) {
		return findByProperty(WORK_FLOW_NO, workFlowNo);
	}

	public List<BpJPlanGuideMain> findBySignStatus(Object signStatus) {
		return findByProperty(SIGN_STATUS, signStatus);
	}

	public List<BpJPlanGuideMain> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all BpJPlanGuideMain entities.
	 * 
	 * @return List<BpJPlanGuideMain> all BpJPlanGuideMain entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanGuideMain> findAll() {
		LogUtil.log("finding all BpJPlanGuideMain instances", Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanGuideMain model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJPlanGuideMainForm getBpJPlanGuideMain(String planTime,
			String enterpriseCode) {
		try {
			String sql = "select t.*,\n"
					+ "       getworkername(t.edit_by) editByName,\n"
					+ "       to_char(t.edit_date, 'yyyy-MM-dd') editDate,\n"
					+ "       to_char(t.release_date,'yyyy-MM-dd') releaseDate,\n"
					+ "       to_char(t.plan_time, 'yyyy-MM') planTimeString\n"
					+ "  from bp_j_plan_guide_main t\n"
					+ " where to_char(t.plan_time, 'yyyy-MM') = '" + planTime
					+ "'\n" + "   and t.enterprise_code = '" + enterpriseCode
					+ "'";
			Object[] data = (Object[]) bll.getSingal(sql);
			if (data != null) {
				BpJPlanGuideMainForm form = new BpJPlanGuideMainForm();
				BpJPlanGuideMain model = new BpJPlanGuideMain();
				if (data[1] != null)
					model.setEditBy(data[1].toString());
				if (data[3] != null)
					model.setPlanStatus(Long.parseLong(data[3].toString()));
				if (data[4] != null)
					model.setWorkFlowNo(Long.parseLong(data[4].toString()));
				if (data[5] != null)
					model.setSignStatus(Long.parseLong(data[5].toString()));
				if (data[8] != null)
					form.setEditByName(data[8].toString());
				if (data[9] != null)
					form.setEditDate(data[9].toString());
				if (data[10] != null)
					form.setReleaseDate(data[10].toString());
				if (data[11] != null)
					form.setPlanTimeString(data[11].toString());
				form.setBaseInfo(model);
				return form;
			} else {
				return null;
			}

		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			return null;
		}
	}

	private void changeWfInfo(Long entryId, String workerCode, Long actionId,
			String approveText, String nextRoles) {
		try {
			service.doAction(entryId, workerCode, actionId, approveText, null,
					nextRoles, "");
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// 审批步骤：立项 上报
	// 对应信息：编辑页面点“上报”→4
	public BpJPlanGuideMain reportTo(String prjNo, String workflowType,
			String workerCode, String actionId, String approveText,
			String nextRoles, String enterpriseCode) throws ParseException {
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
		BpJPlanGuideMain entity = findById(sdf1.parse(prjNo));
		String prjNo1 = prjNo.replace("-", "");
		try {
			if (entity.getWorkFlowNo() == null) {
				// 处理未上报
				if (!workflowType.equals("")) {
					long entryId = service.doInitialize(workflowType,
							workerCode, prjNo1);
					long actionIdl = Long.parseLong(actionId);
					service.doAction(entryId, workerCode, actionIdl,
							approveText, null, nextRoles);
					entity.setWorkFlowNo(entryId);
					entity.setSignStatus(1l);
					entity.setPlanStatus(1L);
				}
			} else {
				// 处理已退回
				java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat(
						"yyyy-MM-dd");
				long entryId = service.doInitialize(workflowType, workerCode,
						sdf.format(entity.getPlanTime()));
				long actionIdl = Long.parseLong(actionId);
				service.doAction(entryId, workerCode, actionIdl, approveText,
						null, nextRoles);
				entity.setWorkFlowNo(entryId);
				entity.setSignStatus(1l);
				entity.setPlanStatus(1L);
			}
			// entity.setAccSignStartDate(new Date());
			update(entity);

		} catch (Exception e) {

		}
		return entity;
	}

	// 立项审批
	public BpJPlanGuideMain approveStep(String prjNo, String workflowType,
			String workerCode, String actionId, String actionWord,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
		Date planDate = sdf.parse(prjNo);
		BpJPlanGuideMain model = findById(planDate);
		try {
			long actionIdl = Long.parseLong(actionId);
			this.changeWfInfo(model.getWorkFlowNo(), workerCode, actionIdl,
					approveText, nextRoles);
			model.setSignStatus(getapproveStatus(stepId, actionId));
			if (getapproveStatus(stepId, actionId) == 3) {
				model.setPlanStatus(getplanStatus(actionId));
			}
			if (getplanStatus(actionId) == 2) {
				model.setPlanStatus(2l);
				model.setReleaseDate(new Date());
			}
			update(model);
			// 修改各部门计划状态
			// String sqlString = "update bp_j_plan_guide_main t "
			// + " set t.plan_status=" + getplanStatus(actionId)
			// + " where t.plan_time=" + "to_date('" + prjNo
			// + "'||'-01','yyyy-MM-dd ') " +

			// " and t.enterprise_code='" + enterpriseCode + "'";
			// bll.exeNativeSQL(sqlString);

		} catch (Exception e) {

		}
		return model;
	}

	private Long getapproveStatus(String stepID, String actionId) {
//		int idInt = Integer.parseInt(actionId);
//		if (idInt == 42 || idInt == 52 || idInt == 62)
//			return 3l;
//
//		else if (idInt == 24 || idInt == 45 || idInt == 56)
//			return 1l;
//		else if (idInt == 43)
//
//			return 2L;
//		else {
//			return 0L;
//		}
		switch (Integer.parseInt(actionId)) {
		case 42:
			return 3l;

		case 43:
			return 2l;

		default:
			return 0l;
		}

	}

	private Long getplanStatus(String actionId) {

		int idInt = Integer.parseInt(actionId);
		if (idInt == 42 )
			return 1l;

		else if (idInt == 43)
			return 2l;
		else {
			return 0L;
		}

	}

}