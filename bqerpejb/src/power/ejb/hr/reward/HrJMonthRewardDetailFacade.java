package power.ejb.hr.reward;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrJMonthRewardDetail.
 * 
 * @see power.ejb.hr.reward.HrJMonthRewardDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJMonthRewardDetailFacade implements
		HrJMonthRewardDetailFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB (beanName="NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB (beanName="HrJRewardApproveFacade")
	protected HrJRewardApproveFacadeRemote approveFacade;

	public void save(HrJMonthRewardDetail entity) {
		LogUtil.log("saving HrJMonthRewardDetail instance", Level.INFO, null);
		try {
			entity.setDetailId(bll.getMaxId("HR_J_MONTH_REWARD_DETAIL", "DETAIL_ID"));
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(String detailIds) {
		LogUtil.log("deleting HrJMonthRewardDetail instance", Level.INFO, null);
		try {
			String deleteDetailSql = "update HR_J_MONTH_REWARD_DETAIL t set t.is_use = 'N' where t.DETAIL_ID in("+detailIds+")";
			bll.exeNativeSQL(deleteDetailSql);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJMonthRewardDetail update(HrJMonthRewardDetail entity) {
		LogUtil.log("updating HrJMonthRewardDetail instance", Level.INFO, null);
		try {
			HrJMonthRewardDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJMonthRewardDetail findById(Long id) {
		LogUtil.log("finding HrJMonthRewardDetail instance with id: " + id,
				Level.INFO, null);
		try {
			HrJMonthRewardDetail instance = entityManager.find(
					HrJMonthRewardDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<HrJMonthRewardDetail> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding HrJMonthRewardDetail instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from HrJMonthRewardDetail model where model."
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
	public PageObject findAllByRewardId(String rewardId,String workFlowState) {
		LogUtil.log("finding all HrJMonthRewardDetail instances", Level.INFO,
				null);
		try {
			
			String queryString = "SELECT t.detail_id,\n" +
					"       t.reward_id,\n" + 
					"       t.dept_id,\n" + 
					"       t.emp_count,\n" + 
					"       t.last_month_num,\n" + 
					"       round(t.month_reward_num,2) month_reward_num,\n" + 
					"       t.quantify_cash,\n" + 
					"       t.extra_add_num,\n" + 
					"       t.month_assess_num,\n" + 
					"       t.other_num,\n" + 
					"       t.total_num,\n" + 
					"       t.memo,\n" + 
					"       t.work_flow_state,\n" + 
					"       (select a.dept_name from hr_c_dept a where a.dept_id= t.dept_id) work_flow_no,\n" + 
					"       t.is_use,\n" + 
					"       t.enterprise_code\n" + 
					"  FROM hr_j_month_reward_detail t where t.is_use = 'Y'";
			if(rewardId != null && !"".equals(rewardId)) {
			 	queryString += "and t.reward_id='"+rewardId+"'";
			}
			if(workFlowState != null && !"".equals(workFlowState)) {
			 	queryString += "and t.work_flow_state  in  ("+workFlowState+")"; //modify by wpzhu 20100819
			}
			
			queryString += "order by work_flow_no";
			queryString ="select tt.*," +
			"(nvl(tt.last_month_num,0)+nvl(tt.month_reward_num,0)+nvl(tt.quantify_cash,0)+nvl(tt.extra_add_num,0)+nvl(tt.month_assess_num,0)+nvl(tt.other_num,0))as totalnum\n " +
			"from  ("+queryString+")tt ";
//			System.out.println("the querystring"+queryString);
			List list = bll.queryByNativeSQL(queryString);
			PageObject result = new PageObject();
			result.setList(list);
			result.setTotalCount(((Integer) list.size()).longValue());
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void saveOrUpdateDetailList(List<HrJMonthRewardDetail> addList, List<HrJMonthRewardDetail> updateList) {
		if (addList != null && addList.size() > 0) {
			for (HrJMonthRewardDetail entity : addList) {
				this.save(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (HrJMonthRewardDetail entity : updateList) {
				this.update(entity);
			}
		}
	}
	
	public List<HrJRewardApprove> monthRewardApprove(String detailIds,String rewardId) {
		StringBuffer sb = new StringBuffer();
		List<HrJRewardApprove> approvelist = new ArrayList<HrJRewardApprove>();
		sb.append("begin\n");
		String updateSql = "update hr_j_month_reward_detail t set t.work_flow_state = '1' where t.detail_id in ("+detailIds+");\n";
		sb.append(updateSql);
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
		//delete by fyyang 20100830
//		String sql = "select count(*) from hr_j_month_reward_detail t where t.reward_id = '"+rewardId+"' and t.work_flow_state = '0'";
//		int count = Integer.parseInt(bll.getSingal(sql).toString());
//		if(count == 0) {
//			sb = new StringBuffer();
//			sb.append("begin\n");
//			updateSql = "update hr_j_month_reward t set t.work_flow_state = '2' where t.reward_id in ("+rewardId+");\n";
//			sb.append(updateSql);
//			sb.append("commit;\n");
//			String deleteSql = "delete hr_j_reward_approve t where t.dept_id is null and t.detail_id = '"+rewardId+"';\n";
//			sb.append(deleteSql);
//			sb.append("commit;\n");
//			sb.append("end;\n");
//			bll.exeNativeSQL(sb.toString());
//		}
		// 往审批表中插入几条记录
		String[] detailIdArray = detailIds.split(",");
		String monthSql = "select t.reward_month from hr_j_month_reward t where t.reward_id = '"+rewardId+"'";
		String rewardMonth = bll.getSingal(monthSql).toString();
		if(detailIdArray != null && detailIdArray.length > 0) {
			Long approveId = bll.getMaxId("HR_J_REWARD_APPROVE", "APPROVE_ID");
			for(int i=0;i<detailIdArray.length;i++) {
				HrJMonthRewardDetail detailEntity = this.findById(Long.parseLong(detailIdArray[i]));
				HrJRewardApprove approve = new HrJRewardApprove();
				approve.setDeptId(detailEntity.getDeptId());
				approve.setDetailId(detailEntity.getDetailId());
				approve.setFlag("1");
				approve.setContent(rewardMonth+"月奖等待审批！");
				approve.setApproveId(approveId+i);
				approve.setFlowListUrl("hr/reward/monthAward/business/rewardProvide/deptAdminApprove/deptAdminList.jsp");
				approvelist.add(approve);
				approveFacade.save(approve);
			}
		}
		return approvelist;
	}
	public String getMaxMonth(String workstatus,Long deptId)
	{
		String sql="select nvl(max(a.reward_month),0)  from  hr_j_month_reward a, hr_j_month_reward_detail  b\n" +
				"where a.reward_id=b.reward_id " +
				" and a.is_use='Y'\n " +
				" and b.is_use='Y' \n  " ;
		if(workstatus != null && !"".equals(workstatus)) {
			sql += "and b.work_flow_state in("+workstatus+")";
		}
		if(deptId != null && !"".equals(deptId)) {
			sql += " and b.dept_id = (select t.dept_id from hr_c_dept t where t.dept_code= GETFirstLevelBYID("+deptId+")) \n";

		}
		String Status="";
//		System.out.println("the sql"+sql);
		Object  obj=bll.getSingal(sql);
		if(obj!=null)
		{
			Status=obj.toString();
		}
		return Status;
		
	}
	public PageObject findAllByRewardMonth(String rewardMonth,String workFlowState, String deptId) {
		
		String queryString = "SELECT b.detail_id,\n" +
					"       b.reward_id,\n" + 
					"       b.dept_id,\n" + 
					"       b.emp_count,\n" + 
					"       b.last_month_num,\n" + 
					"       b.month_reward_num,\n" + 
					"       b.quantify_cash,\n" + 
					"       b.extra_add_num,\n" + 
					"       b.month_assess_num,\n" + 
					"       b.other_num,\n" + 
					"       b.total_num,\n" + 
					"       b.memo,\n" + 
					"       b.work_flow_state,\n" + 
					"       (select a.dept_name from hr_c_dept a where a.dept_id= b.dept_id) work_flow_no,\n" + 
					"       b.is_use,\n" + 
					"       b.enterprise_code\n" + 
					"		from hr_j_month_reward a, hr_j_month_reward_detail b where a.reward_id=b.reward_id and a.reward_month = '"+rewardMonth+"'";
		if(workFlowState != null && !"".equals(workFlowState)) {
			queryString += "and b.work_flow_state in("+workFlowState+")";
		}
		if(deptId != null && !"".equals(deptId)) {
			queryString += " and b.dept_id = (select t.dept_id from hr_c_dept t where t.dept_code= GETFirstLevelBYID("+deptId+")) \n";

		}
		PageObject result = new PageObject();
		List<HrJMonthRewardDetail> list = bll.queryByNativeSQL(queryString, HrJMonthRewardDetail.class);
		if(workFlowState.indexOf("1")!=-1)
		{
			//部门
			result.setList(list);
			result.setTotalCount(((Integer) list.size()).longValue());
			
		}
		else
		{
			//班组
		if(list != null && list.size() > 0) {
			String sql = "select count(*) from hr_j_reward_approve t where t.flag = '1'and t.dept_id='"+deptId+"' and t.detail_id='"+list.get(0).getDetailId()+"'";

			int count = Integer.parseInt(bll.getSingal(sql).toString());
			if(count > 0) {
				result.setList(list);
				result.setTotalCount(((Integer) list.size()).longValue());
			} 
			else {
				result.setList(new ArrayList());
				result.setTotalCount(0l);
			}
		} else {
			result.setList(new ArrayList());
			result.setTotalCount(0l);
		}
		
		}
		return result;
	}
	
	public List<HrJRewardApprove> administratorApprove(String detailId,String actionId,Long deptId) {
		List<HrJRewardApprove> approvelist = new ArrayList<HrJRewardApprove>();
		if(detailId != null && !"".equals(detailId)) {
			HrJMonthRewardDetail entity = this.findById(Long.parseLong(detailId));
			if("1".equals(entity.getWorkFlowState())){
				if("1".equals(actionId)) {
					entity.setWorkFlowState("4");
					StringBuffer sb = new StringBuffer();
					sb.append("begin\n");
					//modify by fyyang 20100729    部门等于登录人的一级部门
					String deleteSql = "delete from  hr_j_reward_approve t where t.dept_id = " +
					"(SELECT a.dept_id\n" +
					"  FROM hr_c_dept a\n" + 
					" where a.dept_level = 1\n" + 
					"   and rownum = 1\n" + 
					" START WITH a.dept_id = "+deptId+"\n" + 
					"CONNECT BY PRIOR a.pdept_id = a.dept_id) \n"+

					"and t.detail_id = '"+entity.getDetailId()+"';\n";
					sb.append(deleteSql);
					sb.append("commit;\n");
					sb.append("end;\n");
					bll.exeNativeSQL(sb.toString());
				} else if("2".equals(actionId)){
					String monthSql = "select t.reward_month from hr_j_month_reward t where t.REWARD_ID = '"+entity.getRewardId()+"'";
					String rewardMonth = bll.getSingal(monthSql).toString();
					entity.setWorkFlowState("2");
					// 修改审批记录表
					StringBuffer sb = new StringBuffer();
					sb.append("begin\n");
					//modify by fyyang 20100729    部门等于登录人的一级部门
					String deleteSql = "delete  from  hr_j_reward_approve t where t.dept_id = " +
					"(SELECT a.dept_id\n" +
					"  FROM hr_c_dept a\n" + 
					" where a.dept_level = 1\n" + 
					"   and rownum = 1\n" + 
					" START WITH a.dept_id = "+deptId+"\n" + 
					"CONNECT BY PRIOR a.pdept_id = a.dept_id) \n"+
							"and t.detail_id = '"+entity.getDetailId()+"';\n";
					sb.append(deleteSql);
					sb.append("commit;\n");
					String insertSql = "INSERT INTO hr_j_reward_approve\n" +
								"  (SELECT (SELECT nvl(MAX(a.approve_id), 0)\n" + 
								"             FROM hr_j_reward_approve a) + rownum,\n" + 
								"          t.dept_id,\n" + 
								"          '"+detailId+"',\n" + 
								"		   '"+rewardMonth+"月奖等待审批！"+"',\n" +
								"		   'hr/reward/monthAward/business/rewardProvide/shfitAdminApprove/shfitAdminList.jsp',\n" +
								"		   '1'" +
								"     FROM hr_c_dept t\n" + 
								"    WHERE (t.dept_status = '0' OR t.dept_status = '3')\n" + 
								"      AND t.is_banzu = '1'\n" + 
								"      AND t.is_use = 'Y'\n" +  //update by sychen 20100901
//								"      AND t.is_use = 'U'\n" + 
//								"      AND t.pdept_id = '1');\n";
								"      START WITH t.dept_id = (select t.dept_id from hr_c_dept t where t.dept_code= GETFirstLevelBYID("+deptId+"))"+
								"      CONNECT BY PRIOR t.dept_id = t.pdept_id);";
					sb.append(insertSql);
					sb.append("commit;\n");
					sb.append("end;\n");
					bll.exeNativeSQL(sb.toString());
					String approveSql = "select * from hr_j_reward_approve t where t.flag = '1' and t.detail_id = '"+detailId+"'";
					approvelist = bll.queryByNativeSQL(approveSql, HrJRewardApprove.class);
				}
			} else if("2".equals(entity.getWorkFlowState())) {
				entity.setWorkFlowState("3");
				StringBuffer sb = new StringBuffer();
				sb.append("begin\n");
				String deleteSql = "delete from hr_j_reward_approve t where t.dept_id = '"+deptId+"' and t.detail_id = '"+entity.getDetailId()+"';";
				sb.append(deleteSql);
				sb.append("commit;\n");
				sb.append("end;\n");
				bll.exeNativeSQL(sb.toString());
			} else if("3".equals(entity.getWorkFlowState())) {
				StringBuffer sb = new StringBuffer();
				sb.append("begin\n");
				String deleteSql = "delete from hr_j_reward_approve t where t.dept_id = '"+deptId+"' and t.detail_id = '"+entity.getDetailId()+"';";
				sb.append(deleteSql);
				sb.append("commit;\n");
				sb.append("end;\n");
				bll.exeNativeSQL(sb.toString());
			}
			this.update(entity);
		}
		return approvelist;
	}
	
	public int isHasShfit(String deptId) {
		String sql = "SELECT COUNT(*)\n" +
				"  FROM hr_c_dept t\n" + 
				" WHERE (t.dept_status = '0' OR t.dept_status = '3')\n" + 
				"   AND t.is_banzu = '1'\n" + 
				"   AND t.is_use = 'Y'\n" + //update by sychen 20100901 
//				"   AND t.is_use = 'U'\n" +
//				"   AND t.pdept_id = '"+deptId+"'";
				"   START WITH t.dept_id = (select t.dept_id from hr_c_dept t where t.dept_code= GETFirstLevelBYID("+deptId+"))"+
				"   CONNECT BY PRIOR t.dept_id = t.pdept_id";
		return Integer.parseInt(bll.getSingal(sql).toString());
	}
	
	public PageObject queryMonthRewardDetailByRewardMonth(String rewardMonth) {
		String queryString = "SELECT b.detail_id,\n" +
					"       b.reward_id,\n" + 
					"       b.dept_id,\n" + 
					"       b.emp_count,\n" + 
					"       b.last_month_num,\n" + 
					"       b.month_reward_num,\n" + 
					"       b.quantify_cash,\n" + 
					"       b.extra_add_num,\n" + 
					"       b.month_assess_num,\n" + 
					"       b.other_num,\n" + 
					"       b.total_num,\n" + 
					"       b.memo,\n" + 
					"       decode(b.work_flow_state,'0','未分发','1','已分发','2','部门管理员已查看','3','班组已查看','4','部门管理员已查看') work_flow_state,\n" + 
					"       (select a.dept_name from hr_c_dept a where a.dept_id= b.dept_id) work_flow_no,\n" + 
					"       b.is_use,\n" + 
					"       a.reward_month enterprise_code\n" + 
					"		from hr_j_month_reward a, hr_j_month_reward_detail b where a.reward_id=b.reward_id ";
		if(rewardMonth != null && !"".equals(rewardMonth)) {
			queryString += "and a.reward_month = '"+rewardMonth+"'";
		}
		queryString += " order by work_flow_no ";
		List<HrJMonthRewardDetail> list = bll.queryByNativeSQL(queryString, HrJMonthRewardDetail.class);
		PageObject result = new PageObject();
		result.setList(list);
		result.setTotalCount(((Integer) list.size()).longValue());
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject findDetailMonthRewardByDept(Long rewardId,Long deptId,String month)
	{
		
		
		Object[]  absence=	this.getBetweenTime(rewardId,deptId);
		
		String sql=
			"select tt.*,\n" +
			"       nvl(tt.yuejiang, 0) + nvl(tt.lianghua, 0) + nvl(tt.addValue, 0) totalMoney\n" + 
			"  from (\n" + 
			"\n" + 
			"        SELECT GETDEPTNAME(GETFirstLevelBYID(b.dept_id)) dept_name,\n" + 
			"                b.dept_name  banzhu,\n" + 
			"                a.new_emp_code,\n" + 
			"                a.chs_name,\n" + 
			"                e.month_base,\n" + 
			"                e.coefficient,\n" + 
			"                d.month_award,\n" + 
			"                nvl(ceil(e.month_base * d.month_award * e.coefficient *\n" + 
			"                (1 - d.quantify_proportion)*(1-getfactabsenceDays(a.emp_id,'"+month+"'))),0) yuejiang,\n" + 
			"                nvl(ceil(e.month_base * d.month_award * d.quantify_proportion * \n" + 
			"                (select f.quantify_coefficient\n" + 
			"                   from HR_C_DEPT_QUANTIFY_COEFFICIENT f\n" + 
			"                  where f.dept_id = "+deptId+"\n" + 
			"					 and f.is_use='Y'" +// modify by ywliu 20100816
			"                    and trim(f.coefficient_month) = trim(e.reward_month)\n" + 
			"                    and rownum = 1)*(1-getfactabsenceDays(a.emp_id,'"+month+"'))),0) lianghua,\n" + 
			"\n" + 
			"                decode(a.is_chairman,\n" + 
			"                       'Y',\n" + 
			"                       (SELECT UNION_PER_STANDARD\n" + 
			"                          FROM HR_C_UNION_PRESIDENT\n" + 
			"                         WHERE is_use = 'Y'\n" + 
			"                           AND SYSDATE BETWEEN EFFECT_START_TIME AND\n" + 
			"                               EFFECT_END_TIME),\n" + 
			"                       0) + decode(a.TECHNOLOGY_GRADE_ID,\n" + 
			"                                   4,\n" + 
			"                                   (SELECT TECH_STANDARD\n" + 
			"                                      FROM HR_C_TECHNICIAN_STANDARDS\n" + 
			"                                     WHERE is_use = 'Y'\n" + 
			"                                       AND IS_EMPLOY = 'Y'\n" + 
			"                                       AND SYSDATE BETWEEN EFFECT_START_TIME AND\n" + 
			"                                           EFFECT_END_TIME),\n" + 
			"                                   5,\n" + 
			"                                   (SELECT TECH_STANDARD\n" + 
			"                                      FROM HR_C_TECHNICIAN_STANDARDS\n" + 
			"                                     WHERE is_use = 'Y'\n" + 
			"                                       AND IS_EMPLOY = 'Y'\n" + 
			"                                       AND SYSDATE BETWEEN EFFECT_START_TIME AND\n" + 
			"                                           EFFECT_END_TIME),\n" + 
			"                                   0) as addValue," +
			""+standards+"\n,"+// add by wpzhu 标准天数
			"           (SELECT  floor( nvl(SUM(nvl((SELECT t1.base_days\n" + 
			"               FROM hr_c_day t1\n" + 
			"              WHERE t1.id = t.absent_time_id),0) +\n" + 
			"            nvl((SELECT t1.base_days\n" + 
			"               FROM hr_c_day t1\n" + 
			"              WHERE t1.id = t.other_time_id),0)),0))\n" + 
			"   FROM Hr_j_Workattendancenew t\n" + 
			"    WHERE t.emp_id = a.emp_id\n" + 
			"    AND t.is_use = 'Y'\n" + 
			"and  t.attendance_date  BETWEEN  trunc(to_date('"+month+"','yyyy-MM'),'MM') AND  trunc(last_day(to_date('"+month+"','yyyy-MM')),'dd'))absenceDays,\n"+  
//			"  AND  to_char(t.attendance_date,'yyyy-MM-dd')  between '"+startTime+"'   and '"+endTime+"')absenceDays,\n" + //add by wpzhu 缺勤天数
//       " ,decode("+standards+",0.00,0,"+absenceDays+"/"+standards+")"+
			"  b.dept_code\n" + 
			"\n" + 
			"          FROM hr_j_emp_info a,\n" + 
			"                hr_c_dept b,\n" + 
			"                (select distinct g.emp_id, g.month_award, h.quantify_proportion\n" + 
			"                   FROM hr_c_salary_personal g, HR_C_STATION_QUANTIFY h\n" + 
			"                  WHERE g.quantify_id = h.quantify_id(+)\n" + 
			"                    and g.is_use = 'Y'\n" + 
			"                    and h.is_use(+) = 'Y') d,\n" + 
			"                (select * from HR_J_MONTH_REWARD c where c.reward_id = "+rewardId+") e\n" + 
			"         WHERE a.is_use = 'Y'\n" + 
			"           and  b.is_use = 'Y'\n" +  //update by sychen 20100901 
//			"           and  b.is_use = 'U'\n" + 
			"           and b.dept_status <> '1'" +
			"           and a.dept_id = b.dept_id\n" + 
			"           AND a.emp_state = 'U'\n" + 
			"           and a.emp_id = d.emp_id(+)\n" + 
			"           AND a.dept_id IN\n" + 
			"               (SELECT b.dept_id\n" + 
			"                  FROM hr_c_dept b\n" + 
			"                 WHERE b.is_use = 'Y'\n" + //update by sychen 20100901 
//			"                 WHERE b.is_use = 'U'\n" + 
			"                 START WITH b.dept_id = "+deptId+"\n" + 
			"                CONNECT BY PRIOR b.dept_id = b.pdept_id)) tt   order by tt.dept_code,tt.new_emp_code";
//   System.out.println("the sql"+sql);
		 List list=bll.queryByNativeSQL(sql);
		PageObject obj=new PageObject();
		obj.setList(list);
		obj.setTotalCount(Long.parseLong(list.size()+""));
		return obj;
		
	}
	Double   standards=0.00;
	String  startTime="";
	String endTime="";
	
	public Object[]  getBetweenTime(Long rewardId,Long deptId)
	{
		
		Object[]  days =new Object[100];
 	
		String sql1=" select a.effect_start_time,a.effect_end_time ,\n"+
				" nvl(a.standarddays,0)  " +
				" FROM HR_C_MONTH_STANDARDDAYS  a   " +
				" where  a.is_use = 'Y'" +
				" and  to_char(a.effect_start_time,'yyyy-MM') <=" +
				"(select  r.reward_month from  HR_J_MONTH_REWARD  r  where r.reward_id = "+rewardId+")\n"+
				" and  to_char(a.effect_end_time,'yyyy-MM')>=" +
				"(select  r.reward_month from  HR_J_MONTH_REWARD  r  where r.reward_id = "+rewardId+")";
	
	List  list=bll.queryByNativeSQL(sql1);
	if(list!=null&&list.size()>0)
	{
		
		Object[] obj=(Object[]) list.get(0);
		if(obj[0]!=null)
		{
		startTime=obj[0].toString();
		}
		if(obj[1]!=null)
		{
		endTime=obj[1].toString();
		}
		if(obj[2]!=null)
		{
		standards=Double.parseDouble(obj[2].toString());
		}
		
		
		
	}
		return days;
		
	}
	@SuppressWarnings("unchecked")
	public List getRewardDetailDeptList(Long rewardId)
	{

		String sql="select distinct a.dept_id, a.dept_name\n" +
		"  from HR_J_MONTH_REWARD_DETAIL t, hr_c_dept a\n" + 
		" where t.reward_id = "+rewardId+"\n" + 
		"   and t.dept_id = a.dept_id";  
		List list=bll.queryByNativeSQL(sql);
		return list;

	}

}