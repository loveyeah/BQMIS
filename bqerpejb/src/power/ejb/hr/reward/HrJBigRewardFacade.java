package power.ejb.hr.reward;

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

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.plan.BpJPlanRepairDep;
import power.ejb.manage.plan.BpJPlanRepairDepFacadeRemote;

/**
 * Facade for entity HrJBigReward.
 * 
 * @see power.ejb.hr.reward.HrJBigReward
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJBigRewardFacade implements HrJBigRewardFacadeRemote {
	// property constants
	public static final String BIG_REWARD_MONTH = "bigRewardMonth";
	public static final String BIG_AWARD_ID = "bigAwardId";
	public static final String BIG_AWARD_NAME = "bigAwardName";
	public static final String BIG_REWARD_BASE = "bigRewardBase";
	public static final String FILL_BY = "fillBy";
	public static final String WORK_FLOW_STATE = "workFlowState";
	public static final String WORK_FLOW_NO = "workFlowNo";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;

	public HrJBigRewardFacade() {
		service = new WorkflowServiceImpl();
	}

	public PageObject getBigReward(String workflowStatus,String rewardMonth,
			String enterPriseCode, final int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		String sql = "select " + "a.big_reward_id," + "a.big_reward_month,"
				+ "b.big_award_id," + "a.big_award_name,"
				+ "a.big_reward_base,\n"
				+ "to_char(a.handed_date,'yyyy-MM-dd')," + "a.fill_by,"
				+ "getworkername(a.fill_by),"
				+ "to_char(a.fill_date,'yyyy-MM-dd')," + "a.work_flow_state,"
				+ "a.work_flow_no\n"
				+ "from  HR_J_BIG_REWARD  a ,hr_c_big_award b\n"
				+ "where a.big_award_id=b.big_award_id\n"
				+ "and a.is_use='Y'\n" + "and b.is_use='Y'"
				+ "and a.enterprise_code= '" + enterPriseCode + "'"
				+ "and b.enterprise_code='" + enterPriseCode + "'";
		if (workflowStatus != null && !workflowStatus.equals("")) {
			sql += "and a.work_flow_state='" + workflowStatus + "'";
		}
		if (rewardMonth != null && !rewardMonth.equals("")) {
			sql += "and a.big_reward_month='" + rewardMonth + "'";
		}
		// System.out.println("the sql"+sql);
		String sqlCount = "select count(*)  from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		Long count = Long.parseLong(bll.getSingal(sqlCount).toString());
		result.setList(list);
		result.setTotalCount(count);

		return result;

	}

	public void delBigReward(Long id) {
		String sql = "update HR_J_BIG_REWARD  a " + "   set a.is_use='N'"
				+ "   where a.big_reward_id ='" + id + "'";
		// System.out.println("the sql"+sql);
		bll.exeNativeSQL(sql);
	}

	public HrJRewardApprove rewardReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType) {
		System.out.println("the mainid is " + mainId);
		HrJBigReward model = this.findById(mainId);
//		Long entryId;
//		if (model.getWorkFlowNo() == null) {
//			entryId = service.doInitialize(workflowType, workerCode, mainId
//					.toString());
//			model.setWorkFlowNo(entryId);
//		} else {
//			entryId = model.getWorkFlowNo();
//		}
//		service.doAction(entryId, workerCode, actionId, approveText, null,
//				nextRoles, "");
		model.setWorkFlowState("1");
		this.update(model);
		HrJRewardApprove approve = new HrJRewardApprove();
		if("1".equals(model.getWorkFlowState())) {
			approve.setApproveId(bll.getMaxId("HR_J_REWARD_APPROVE", "APPROVE_ID"));
			approve.setDeptId(null);
			approve.setDetailId(model.getBigRewardId());
			approve.setFlag("2");
			approve.setContent(model.getBigAwardName()+"大奖等待审批！");
			approve.setFlowListUrl("hr/reward/bigReward/bigRewardApprove/bigRewardApprove.jsp");
			entityManager.persist(approve);
		}
		return approve;
	}

	public Long save(HrJBigReward entity) throws CodeRepeatException {
		LogUtil.log("saving HrJBigReward instance", Level.INFO, null);
		try {
			String isExistSql = "select count(*) from hr_j_big_reward t where t.is_use = 'Y' and t.BIG_REWARD_MONTH = '"
					+ entity.getBigRewardMonth() + "' and t.BIG_AWARD_ID = '"+entity.getBigAwardId()+"'";
			int isExist = Integer
					.parseInt(bll.getSingal(isExistSql).toString());
			if (isExist == 0) {
				Long rewardId = bll
						.getMaxId("HR_J_BIG_REWARD", "big_reward_id");
				entity.setBigRewardId(rewardId);
				entityManager.persist(entity);
				LogUtil.log("save successful", Level.INFO, null);
				return rewardId;
			} else {
				throw new CodeRepeatException("当月已经存在,不能重复填写！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(HrJBigReward entity) {
		LogUtil.log("deleting HrJBigReward instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJBigReward.class, entity
					.getBigRewardId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJBigReward update(HrJBigReward entity) {
		LogUtil.log("updating HrJBigReward instance", Level.INFO, null);
		try {
			HrJBigReward result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJBigReward findById(Long id) {
		LogUtil.log("finding HrJBigReward instance with id: " + id, Level.INFO,
				null);
		try {
			HrJBigReward instance = entityManager.find(HrJBigReward.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 大奖发放审批 add by sychen 20100830
	 * @param bigRewardId
	 */	
	public void appvoveBigAward(String bigRewardId) {
		
		String sql = "update hr_j_big_reward t\n" + "   set t.work_flow_state = '5'\n"
				+ " where t.big_reward_id ='" + bigRewardId + "'";
		bll.exeNativeSQL(sql);
		
		String st="delete hr_j_reward_approve t where t.dept_id is null and t.flag=2";
		
		bll.exeNativeSQL(st);
	}

}