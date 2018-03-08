package power.ejb.hr.reward;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.print.attribute.standard.MediaSize.Engineering;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrJMonthReward.
 * 
 * @see power.ejb.hr.reward.HrJMonthReward
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJMonthRewardFacade implements HrJMonthRewardFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public HrJMonthReward save(HrJMonthReward entity,Boolean detailIsNull) throws CodeRepeatException{
		LogUtil.log("saving HrJMonthReward instance", Level.INFO, null);
		try {
			String isExistSql = "select count(*) from HR_J_MONTH_REWARD t where t.is_use = 'Y' and t.REWARD_MONTH = '"+entity.getRewardMonth()+"'";
			int isExist = Integer.parseInt(bll.getSingal(isExistSql).toString());
			if(isExist == 0) {
				entity.setRewardId(bll.getMaxId("HR_J_MONTH_REWARD", "REWARD_ID"));
				entity.setIsUse("Y");
				entity.setWorkFlowState("0");
				entity.setFillDate(new Date());
				entityManager.persist(entity);
				if(detailIsNull) {
					Calendar lastMonth = Calendar.getInstance();
					SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
					try {
						lastMonth.setTime(monthFormat.parse(entity.getRewardMonth()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					lastMonth.set(lastMonth.MONTH,lastMonth.get(lastMonth.MONTH)-1);
					StringBuffer sb = new StringBuffer();
					sb.append("begin\n");
					String insertDetailSql = "Insert into hr_j_month_reward_detail (SELECT (SELECT nvl(MAX(a.detail_id), 0)\n" +
										"          FROM HR_J_MONTH_REWARD_DETAIL a) + rownum,\n" + 
										"       '"+entity.getRewardId()+"',\n" + 
										"       t.dept_id,\n" + 
										"       (SELECT COUNT(*)\n" + 
										"          FROM hr_j_emp_info a\n" + 
										"         WHERE a.is_use = 'Y'\n" + 
										"           AND a.emp_state = 'U'\n" + 
										"           AND a.dept_id IN\n" + 
										"               (SELECT b.dept_id\n" + 
										"                  FROM hr_c_dept b\n" + 
										"                 WHERE b.is_use = 'Y'\n" + //update by sychen 20100901 
//										"                 WHERE b.is_use = 'U'\n" + 
										"                  and b.dept_status <> '1'" +
										"                 START WITH b.dept_id = t.dept_id\n" + 
										"                CONNECT BY PRIOR b.dept_id = b.pdept_id)),\n" + 
										" nvl(ceil((SELECT nvl(b.total_num,0) -\n" + 
										"               nvl((SELECT SUM(TOTAL_NUM)\n" + 
										"                  FROM HR_J_REWARD_GRANT        e,\n" + 
										"                       HR_J_REWARD_GRANT_DETAIL f\n" + 
										"                 WHERE e.grant_id = f.grant_id\n" + 
										"                   AND e.grant_month = c.reward_month\n" + 
										"                   AND e.dept_id = t.dept_id),0)\n" + 
										"          FROM HR_J_MONTH_REWARD        c,\n" + 
										"               HR_J_MONTH_REWARD_DETAIL b\n" + 
										"         WHERE c.reward_id = b.reward_id and c.reward_month = '"+monthFormat.format(lastMonth.getTime())+"'" + 
										"			AND	b.dept_id = t.dept_id)),0),\n" +
										" (SELECT SUM(ceil(nvl("+entity.getMonthBase()+" * g.month_award * (1 - h.quantify_proportion)*"+entity.getCoefficient()+"" +
												"*(1-getfactabsenceDays(g.emp_id,'"+entity.getRewardMonth()+"')),0)))\n" + 
										"          FROM hr_c_salary_personal  g,\n" +
										"               hr_j_emp_info b,\n" + 
										"               HR_C_STATION_QUANTIFY h\n" + 
										"         WHERE g.quantify_id = h.quantify_id\n" + 
										"           and b.emp_id = g.emp_id\n" + 
										"           and b.is_use = 'Y'\n" + 
										"           and b.emp_state ='U'\n" + 
										"           AND g.dept_id IN\n" + 
										"               (SELECT b.dept_id\n" + 
										"                  FROM hr_c_dept b\n" + 
										"                 WHERE b.is_use = 'Y'\n" +  //update by sychen 20100901 
//										"                 WHERE b.is_use = 'U'\n" + 
										"                 and b.dept_status <> '1'"+
										"                 START WITH b.dept_id = t.dept_id\n" + 
										"                CONNECT BY PRIOR b.dept_id = b.pdept_id)),\n" + 
										" (SELECT SUM(ceil(nvl("+entity.getMonthBase()+" * g.month_award * h.quantify_proportion *\n" + 
										"                   i.quantify_coefficient*(1-getfactabsenceDays(g.emp_id,'"+entity.getRewardMonth()+"')),0)))\n" + 
										"          FROM hr_c_salary_personal           g,\n" + 
										"               HR_C_STATION_QUANTIFY          h,\n" + 
										"               HR_C_DEPT_QUANTIFY_COEFFICIENT i,\n" + 
										"               hr_j_emp_info b\n" + 
										"         WHERE g.quantify_id = h.quantify_id\n" + 
										"           AND (select b.dept_id from hr_c_dept b  where b.dept_code= getfirstlevelbyid(g.dept_id))= i.dept_id\n" + 
										"           AND i.coefficient_month = '"+entity.getRewardMonth()+"'\n" + 
										"           and b.emp_id = g.emp_id\n" + 
										"           and b.is_use = 'Y'\n" + 
										"           and b.emp_state ='U'\n" + 
										"           AND g.dept_id IN\n" + 
										"               (SELECT b.dept_id\n" + 
										"                  FROM hr_c_dept b\n" + 
										"                 WHERE b.is_use = 'Y'\n" + //update by sychen 20100901 
//										"                 WHERE b.is_use = 'U'\n" +
										"                  and b.dept_status <> '1'"+
										"                 START WITH b.dept_id = t.dept_id\n" + 
										"                CONNECT BY PRIOR b.dept_id = b.pdept_id)),\n" + 
										" nvl(ceil(nvl((SELECT COUNT(*)\n" + 
										"          FROM hr_j_emp_info a\n" + 
										"         WHERE a.is_use = 'Y'\n" + 
										"           AND a.emp_state = 'U'\n" + 
										"           AND a.is_chairman = 'Y'\n" + 
										"           AND a.dept_id IN\n" + 
										"               (SELECT b.dept_id\n" + 
										"                  FROM hr_c_dept b\n" + 
										"                 WHERE b.is_use = 'Y'\n" + //update by sychen 20100901 
//										"                 WHERE b.is_use = 'U'\n" + 
										"                 and b.dept_status <> '1'"+
										"                 START WITH b.dept_id = t.dept_id\n" + 
										"                CONNECT BY PRIOR b.dept_id = b.pdept_id)) *\n" + 
										"       (SELECT UNION_PER_STANDARD\n" + 
										"          FROM HR_C_UNION_PRESIDENT\n" + 
										"         WHERE is_use = 'Y'\n" + 
										"           AND SYSDATE BETWEEN EFFECT_START_TIME AND EFFECT_END_TIME),0) +\n" + 
										"       nvl((SELECT COUNT(*)\n" + 
										"          FROM hr_j_emp_info a\n" + 
										"         WHERE a.is_use = 'Y'\n" + 
										"           AND a.emp_state = 'U'\n" + 
										"           AND a.TECHNOLOGY_GRADE_ID = '4'\n" + 
										"           AND a.dept_id IN\n" + 
										"               (SELECT b.dept_id\n" + 
										"                  FROM hr_c_dept b\n" + 
										"                 WHERE b.is_use = 'Y'\n" +  //update by sychen 20100901 
//										"                 WHERE b.is_use = 'U'\n" + 
										"                  and b.dept_status <> '1'"+
										"                 START WITH b.dept_id = t.dept_id\n" + 
										"                CONNECT BY PRIOR b.dept_id = b.pdept_id)) *\n" + 
										"       (SELECT TECH_STANDARD\n" + 
										"          FROM HR_C_TECHNICIAN_STANDARDS\n" + 
										"         WHERE is_use = 'Y'\n" + 
										"           AND IS_EMPLOY = 'Y'\n" + 
										"           AND SYSDATE BETWEEN EFFECT_START_TIME AND EFFECT_END_TIME),0) +\n" + 
										"       nvl((SELECT COUNT(*)\n" + 
										"          FROM hr_j_emp_info a\n" + 
										"         WHERE a.is_use = 'Y'\n" + 
										"           AND a.emp_state = 'U'\n" + 
										"           AND a.TECHNOLOGY_GRADE_ID = '5'\n" + 
										"           AND a.dept_id IN\n" + 
										"               (SELECT b.dept_id\n" + 
										"                  FROM hr_c_dept b\n" + 
										"                 WHERE b.is_use = 'Y'\n" + //update by sychen 20100901
//										"                 WHERE b.is_use = 'U'\n" +  
										"                 and b.dept_status <> '1'"+
										"                 START WITH b.dept_id = t.dept_id\n" + 
										"                CONNECT BY PRIOR b.dept_id = b.pdept_id)) *\n" + 
										"       (SELECT TECH_STANDARD\n" + 
										"          FROM HR_C_TECHNICIAN_STANDARDS\n" + 
										"         WHERE is_use = 'Y'\n" + 
										"           AND IS_EMPLOY = 'N'\n" + 
										"           AND SYSDATE BETWEEN EFFECT_START_TIME AND EFFECT_END_TIME),0)),0),\n" + 
										"       0,\n" + 
										"       0,\n" + 
										"       '',\n" + 
										"       '',\n" + 
										"       0,\n" + 
										"       '',\n" + 
										"       'Y',\n" + 
										"       'hfdc'\n" + 
										"  FROM hr_c_dept t\n" + 
										" WHERE t.dep_feature = '1'\n" + 
										"   AND t.dept_level = '1'\n" + 
										"   AND t.is_use = 'Y');";//update by sychen 20100901
                    //					"   AND t.is_use = 'U')";
					sb.append(insertDetailSql);
					sb.append("commit;\n");
					sb.append("end;\n");
	                System.out.println("the sb"+insertDetailSql);
					bll.exeNativeSQL(sb.toString());
				}
				LogUtil.log("save successful", Level.INFO, null);
				return entity;
			} else {
				throw new CodeRepeatException("当月已经存在,不能重复填写！");
			}
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String rewardId) {
		LogUtil.log("deleting HrJMonthReward instance", Level.INFO, null);
		try {
			HrJMonthReward entity = this.findById(Long.parseLong(rewardId));
			entity.setIsUse("N");
			String deleteDetailSql = "update HR_J_MONTH_REWARD_DETAIL t set t.is_use = 'N' where t.REWARD_ID ='"+rewardId+"'";
			bll.exeNativeSQL(deleteDetailSql);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public HrJRewardApprove monthRewardReport(String rewardId) {
		HrJMonthReward entity = this.findById(Long.parseLong(rewardId));
		entity.setWorkFlowState("1");
		this.update(entity);
		HrJRewardApprove approve = new HrJRewardApprove();
		approve.setApproveId(bll.getMaxId("HR_J_REWARD_APPROVE", "APPROVE_ID"));
		approve.setDeptId(null);
		approve.setDetailId(entity.getRewardId());
		approve.setFlag("1");
		approve.setContent(entity.getRewardMonth()+"月奖等待审批！");
		approve.setFlowListUrl("hr/reward/monthAward/business/rewardProvide/mainApprove/monthRewardProvide.jsp");
		entityManager.persist(approve);
		return approve;
	}

	public HrJMonthReward update(HrJMonthReward entity) {
		LogUtil.log("updating HrJMonthReward instance", Level.INFO, null);
		try {
			HrJMonthReward result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJMonthReward findById(Long id) {
		LogUtil.log("finding HrJMonthReward instance with id: " + id,
				Level.INFO, null);
		try {
			HrJMonthReward instance = entityManager.find(HrJMonthReward.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<HrJMonthReward> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJMonthReward instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJMonthReward model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findAll(String rewardMonth,String workFlowState) {
		LogUtil.log("finding all HrJMonthReward instances", Level.INFO, null);
		try {
			String queryString = "SELECT t.reward_id,\n" +
							"       t.reward_month,\n" + 
							"       t.month_base,\n" + 
							"		t.HANDED_DATE,\n" +
							"       getworkername(t.fill_by) FILL_BY,\n" + 
							"       t.fill_date,\n" + 
							"       t.work_flow_state,\n" + 
							"       t.work_flow_no,\n" + 
							"       t.is_use,\n" + 
							"       t.enterprise_code,\n" +
							"       t.coefficient \n"+
							"  FROM hr_j_month_reward t\n" + 
							" WHERE t.is_use = 'Y'";
			if(rewardMonth != null && !"".equals(rewardMonth)) {
			 	queryString += "and t.reward_month = '"+rewardMonth+"'";
			}
			if(workFlowState != null && !"".equals(workFlowState)) {
			 	queryString += "and t.WORK_FLOW_STATE = '"+workFlowState+"'";
			}
			queryString += " order by t.reward_month desc";
			List<HrJMonthReward> list = bll.queryByNativeSQL(queryString, HrJMonthReward.class);
			PageObject result = new PageObject();
			result.setList(list);
			result.setTotalCount(((Integer)list.size()).longValue());
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * 月奖发放审批方法 add by sychen 20100830
	 * @param rewardId
	 */
	public void appvoveMonthAward(String rewardId) {
	
		String sql = "update hr_j_month_reward t\n" + "   set t.work_flow_state = '5'\n"
				+ " where t.reward_id ='" + rewardId + "'";
		bll.exeNativeSQL(sql);
		
		String st="delete hr_j_reward_approve t where t.dept_id is null and t.flag=1";
		
		bll.exeNativeSQL(st);
	}
	

}