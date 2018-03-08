package power.ejb.hr.reward;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.hr.salary.HrCBigAward;

/**
 * Facade for entity HrJBigRewardDetail.
 * 
 * @see power.ejb.hr.reward.HrJBigRewardDetail
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJBigRewardDetailFacade implements HrJBigRewardDetailFacadeRemote {
	// property constants
	public static final String BIG_REWARD_ID = "bigRewardId";
	public static final String DEPT_ID = "deptId";
	public static final String EMP_COUNT = "empCount";
	public static final String BIG_REWARD_NUM = "bigRewardNum";
	public static final String MEMO = "memo";
	public static final String WORK_FLOW_NO = "workFlowNo";
	public static final String WORK_FLOW_STATE = "workFlowState";
	public static final String IS_USE = "isUse";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	@EJB (beanName="HrJRewardApproveFacade")
	protected HrJRewardApproveFacadeRemote approveFacade;
	WorkflowService service;

	public HrJBigRewardDetailFacade() {
		service = new WorkflowServiceImpl();
	}

	public List<HrJRewardApprove> rewardDetailReport(String detailIds, String rewardId) {
		StringBuffer sb = new StringBuffer();
		List<HrJRewardApprove> approvelist = new ArrayList<HrJRewardApprove>();
		sb.append("begin\n");
		String updateSql = "update HR_J_BIG_REWARD_DETAIL t set t.work_flow_state = '1' where t.BIG_DETAIL_ID in ("+detailIds+");\n";
		sb.append(updateSql);
		sb.append("commit;\n");
		sb.append("end;\n");
		bll.exeNativeSQL(sb.toString());
		//delete by fyyang 20100830 分发到部门
//		String sql = "select count(*) from HR_J_BIG_REWARD_DETAIL t where t.BIG_REWARD_ID = '"+rewardId+"' and t.work_flow_state = '0'";
//		int count = Integer.parseInt(bll.getSingal(sql).toString());
//		if(count == 0) {
//			sb = new StringBuffer();
//			sb.append("begin\n");
//			updateSql = "update HR_J_BIG_REWARD t set t.work_flow_state = '2' where t.BIG_REWARD_ID in ("+rewardId+");\n";
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
		String monthSql = "select t.BIG_AWARD_NAME from HR_J_BIG_REWARD t where t.BIG_REWARD_ID = '"+rewardId+"'";
		String bigAwardName = bll.getSingal(monthSql).toString();
		if(detailIdArray != null && detailIdArray.length > 0) {
			Long approveId = bll.getMaxId("HR_J_REWARD_APPROVE", "APPROVE_ID");
			for(int i=0;i<detailIdArray.length;i++) {
				HrJBigRewardDetail detailEntity = this.findById(Long.parseLong(detailIdArray[i]));
				HrJRewardApprove approve = new HrJRewardApprove();
				approve.setDeptId(detailEntity.getDeptId());
				approve.setDetailId(detailEntity.getBigDetailId());
				approve.setFlag("2");
				approve.setContent(bigAwardName+"大奖等待审批！");
				approve.setApproveId(approveId+i);
				approve.setFlowListUrl("hr/reward/bigReward/deptAdminApprove/deptAdminList.jsp");
				approvelist.add(approve);
				approveFacade.save(approve);
			}
		}
		return approvelist;
	}

	public boolean IsallReport(Long id) {

		String sql1 = "select count(*)" + " from HR_J_BIG_REWARD_DETAIL  d\n"
				+ "where  d.big_reward_id='" + id + "'";
		String sql2 = "select  count(*)" + "from HR_J_BIG_REWARD_DETAIL  d\n"
				+ "where  d.big_reward_id='" + id + "'\n"
				+ "and d.work_flow_state='1'";
		Object obj1 = bll.getSingal(sql1);
		Object obj2 = bll.getSingal(sql2);
		if (obj1 != null && obj2 != null) {
			Long count1 = Long.parseLong(obj1.toString());
			Long count2 = Long.parseLong(obj2.toString());
			if (count1.equals(count2) || count1 == count2) {
				return true;
			}

		}

		return false;

	}

	public PageObject getAllawardName(String yearMonth) {

		PageObject result = new PageObject();
		String sql = "select " + "a.big_award_id," + "a.big_award_name,"
				+ "a.big_award_base  " + "from hr_c_big_award  a\n"
				+ "where a.is_use='Y'";
		if (yearMonth != null && !yearMonth.equals("")) {
			sql += "and  to_char(a.award_month,'yyyy-MM')='" + yearMonth + "'";
		}
		// System.out.println("the sql"+sql);
		List list = bll.queryByNativeSQL(sql);
		result.setList(list);
		return result;

	}

	public boolean hasDept(Long deptId) {
		int count = 0;
		String sql = "select " + "count(*) "
				+ "from   HR_J_BIG_REWARD_DETAIL  d\n" + "where  d.dept_id='"
				+ deptId + "'\n" + "and  d.is_use='Y'";
		// System.out.println("the sql"+sql);
		Object obj = bll.getSingal(sql);
		if (obj != null) {
			count = Integer.parseInt(obj.toString());
		}
		if (count > 0) {
			return true;
		} else {
			return false;
		}

	}

	public void saveBigRewardDetail(List<HrJBigRewardDetail> addList,
			List<HrJBigRewardDetail> updateList) throws CodeRepeatException {
		if (addList != null && addList.size() > 0) {

			for (HrJBigRewardDetail entity : addList) {
//				boolean hasDept = this.hasDept(entity.getDeptId());
//				if (!hasDept) {
					this.save(entity);
					entityManager.flush();
//				} else {
//					throw new CodeRepeatException("该部门在表中已经存在！");
//				}
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (HrJBigRewardDetail entity : updateList) {
				boolean hasDept = this.hasDept(entity.getDeptId());

				this.update(entity);
				entityManager.flush();
			}
		}
	}

	public void deleteBigRewardDetail(String ids) {
		String sql = "update HR_J_BIG_REWARD_DETAIL  d "
				+ "   set d.is_use='N'" + "where  d.big_detail_id in (" + ids
				+ ")";
		bll.exeNativeSQL(sql);

	}

	public PageObject getBigRewardDetail(Long mainId,String workFlowState) {
		String sql = "SELECT a.dept_id,\n" +
				"       b.dept_code,\n" + 
				"       b.dept_name,\n" + 
				"       a.big_detail_id,\n" + 
				"       a.big_reward_id,\n" + 
				"       a.emp_count,\n" + 
				"       a.big_reward_num,\n" + 
				"       a.memo,\n" + 
				"       a.work_flow_no,\n" + 
				"       a.work_flow_state\n" + 
				"  FROM hr_j_big_reward_detail a,\n" + 
				"       hr_c_dept              b\n" + 
				" WHERE a.dept_id = b.dept_id\n" + 
				"   AND a.is_use = 'Y'\n" + 
				"   AND a.big_reward_id = '"+mainId+"'";
		PageObject result = new PageObject();
//		boolean flag = false;
//		flag = this.isDept(mainId);
//		if (!flag) {
//			sql = "select "
//					+ "a.dept_id,"
//					+ "a.dept_code,"
//					+ "a.dept_name ,"
//					+ "d.big_detail_id,"
//					+ "d.big_reward_id,"
//					+ "(SELECT COUNT(*)\n"
//					+ "               FROM hr_j_emp_info a\n"
//					+ "               WHERE a.is_use = 'Y'\n"
//					+ "               AND a.emp_state = 'U'\n"
//					+ "               AND a.dept_id IN\n"
//					+ "               (SELECT b.dept_id\n"
//					+ "                  FROM hr_c_dept b\n"
//					+ "                 WHERE b.is_use = 'U'\n"
//					+ "                 START WITH b.dept_id = a.dept_id\n"
//					+ "                CONNECT BY PRIOR b.dept_id = b.pdept_id)),"
//					+
//					// "(select sum(a.people_number) from hr_c_dept a where
//					// a.dept_id=d.dept_id group by a.dept_id ) sumpeople,\n" +
//					// "d.emp_count," +
//					"d.big_reward_num," + "d.memo ," + "d.work_flow_no,"
//					+ "d.work_flow_state\n"
//					+ "from hr_c_dept  a,HR_J_BIG_REWARD_DETAIL  d\n"
//					+ "where  a.dept_id =d.dept_id (+)\n"
//					+ " and a.dep_feature='1'\n" + "and a.dept_level='1'\n"
//					+ "and a.is_use='U'\n" + "order by a.dept_name ";

//		} else {
//			sql = "select " + "d.dept_id," + "a.dept_code ," + "a.dept_name,"
//					+ "d.big_detail_id," + "d.big_reward_id," + "d.emp_count,"
//					+ "d.big_reward_num," + "d.memo,\n" + "d.work_flow_no,"
//					+ "d.work_flow_state\n"
//					+ "from   hr_c_dept  a,HR_J_BIG_REWARD_DETAIL  d\n"
//					+ "where a.dept_id=d.dept_id\n" + "and a.is_use='U'"
//					+ "and d.is_use='Y' and d.big_reward_id='" + mainId
//					+ "'\n  ";
//			sql += "union  select null,'','',null,null,"
//					+ "sum(d.emp_count),sum(d.big_reward_num),'',null,'' "
//					+ "from   hr_c_dept  a,HR_J_BIG_REWARD_DETAIL  d\n"
//					+ "where a.dept_id=d.dept_id\n" + "and a.is_use='U'"
//					+ "and d.is_use='Y'  ";
//
//		}
		if(workFlowState != null && !"".equals(workFlowState)) {
			sql += " AND a.work_flow_state = '"+workFlowState+"'";
		}
//		System.out.println("the sql" + sql);
		List list = bll.queryByNativeSQL(sql);
		result.setList(list);
		return result;

	}

	public boolean isDept(Long mainId) {
		Long count = 0l;
		String sql = "select count(d.dept_id) "
				+ "from HR_J_BIG_REWARD_DETAIL d \n"
				+ "where d.big_reward_id='" + mainId + "'\n"
				+ "and d.is_use='Y'";
		// System.out.println("the sql"+sql);
		Object obj = bll.getSingal(sql).toString();

		if (obj != null) {
			count = Long.parseLong(obj.toString());
		}
		if (count > 0) {
			return true;
		}

		return false;

	}

	public void save(HrJBigRewardDetail entity) {
		LogUtil.log("saving HrJBigRewardDetail instance", Level.INFO, null);
		try {
			Long bigDetailId = bll.getMaxId("HR_J_BIG_REWARD_DETAIL",
					"big_detail_id");
			entity.setBigDetailId(bigDetailId);
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	public void delete(HrJBigRewardDetail entity) {
		LogUtil.log("deleting HrJBigRewardDetail instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJBigRewardDetail.class,
					entity.getBigDetailId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJBigRewardDetail update(HrJBigRewardDetail entity) {
		LogUtil.log("updating HrJBigRewardDetail instance", Level.INFO, null);
		try {
			HrJBigRewardDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJBigRewardDetail findById(Long id) {
		LogUtil.log("finding HrJBigRewardDetail instance with id: " + id,
				Level.INFO, null);
		try {
			HrJBigRewardDetail instance = entityManager.find(
					HrJBigRewardDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PageObject getRewardNotice(String rewardName) {
		String sql = "";

		return null;

	}

	public void buildData(String rewardId, String rewardMonth,
			String bigRewardBase) {
		String sql="select a.* from hr_c_big_award a,hr_j_big_reward b where a.is_use='Y'and b.is_use='Y'and a.big_award_id=b.big_award_id and b.big_reward_id='"+rewardId+"'";
		List<HrCBigAward> list = bll.queryByNativeSQL(sql, HrCBigAward.class);
		Calendar lastMonth = Calendar.getInstance();
		SimpleDateFormat monthFormat = new SimpleDateFormat("yyyy-MM");
		try {
			lastMonth.setTime(monthFormat.parse(rewardMonth));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		lastMonth.set(lastMonth.MONTH, lastMonth.get(lastMonth.MONTH) - 1);
		StringBuffer sb = new StringBuffer();
		sb.append("begin\n");
		String insertDetailSql = "Insert into hr_j_big_reward_detail (SELECT (SELECT nvl(MAX(a.BIG_DETAIL_ID), 0)\n"
				+ "          FROM hr_j_big_reward_detail a) + rownum,\n"
				+ "       '"
				+ rewardId
				+ "',\n"
				+ "       t.dept_id,\n"
				+ "       (SELECT COUNT(*)\n"
				+ "          FROM hr_j_emp_info a\n"
				+ "         WHERE a.is_use = 'Y'\n"
				+ "           AND a.emp_state = 'U'\n"
				+ "           AND a.dept_id IN\n"
				+ "               (SELECT b.dept_id\n"
				+ "                  FROM hr_c_dept b\n"
				+ "                 WHERE b.is_use = 'Y'\n" //update by sychen 20100901
//				+ "                 WHERE b.is_use = 'U'\n" 
				+ "                       and b.dept_status <>'1'"
				+ "                 START WITH b.dept_id = t.dept_id\n"
				+ "                CONNECT BY PRIOR b.dept_id = b.pdept_id)),\n"
				+ "       (SELECT SUM(("
				+ bigRewardBase
				+ " * g.month_award )*(getabsencedays(g.emp_id, '"+rewardId+"')))\n"
				+ "          FROM hr_c_salary_personal  g\n"
				+ "         WHERE g.dept_id IN\n"
				+ "               (SELECT b.dept_id\n"
				+ "                  FROM hr_c_dept b\n"
				+ "                 WHERE b.is_use = 'Y'\n" //update by sychen 20100901
//				+ "                 WHERE b.is_use = 'U'\n"
				+ "                       and b.dept_status <>'1'"
				+ "                 START WITH b.dept_id = t.dept_id\n"
				+ "                CONNECT BY PRIOR b.dept_id = b.pdept_id)),\n"
				+ "       '',\n"
				+ "       '',\n"
				+ "       0,\n"
				+ "       'Y',\n"
				+ "       'hfdc'\n"
				+ "  FROM hr_c_dept t\n"
				+ " WHERE t.dep_feature = '1'\n"
				+ "   AND t.dept_level = '1'\n" + "   AND t.is_use = 'Y');";//update by sychen 20100901
//		+ "   AND t.dept_level = '1'\n" + "   AND t.is_use = 'U');";
		sb.append(insertDetailSql);
		sb.append("commit;\n");
		sb.append("end;\n");
		System.out.println(insertDetailSql);
		bll.exeNativeSQL(sb.toString());
	}
	
	public PageObject findAllByBigAwardName(String bigRewardId,String workFlowState, String deptId) {
		String queryString = "SELECT b.big_detail_id,\n" +
				"       b.dept_id,\n" + 
				"       c.dept_name,\n" + 
				"       b.big_reward_id,\n" + 
				"       a.big_award_name,\n" + 
				"       a.big_reward_month,\n" + 
				"       b.emp_count,\n" + 
				"       b.big_reward_num,\n" + 
				"       b.memo\n," + 
				"		b.work_flow_state\n"+//add by kzhang 
				"  FROM hr_j_big_reward        a,\n" + 
				"       hr_j_big_reward_detail b,\n" + 
				"       hr_c_dept              c\n" + 
				" WHERE a.big_reward_id = b.big_reward_id\n" + 
				"   AND b.dept_id = c.dept_id\n" + 
				"   AND a.is_use = 'Y'\n" + 
				"   AND b.is_use = 'Y'";
		if(bigRewardId != null && !"".equals(bigRewardId)) {
			queryString += " AND b.big_reward_id = '"+bigRewardId+"'";
		}
		if(workFlowState != null && !"".equals(workFlowState)) {
			queryString += " and b.work_flow_state in("+workFlowState+")";
		}
		if(deptId != null && !"".equals(deptId)) {
			queryString += " and b.dept_id = (select t.dept_id from hr_c_dept t where t.dept_code= GETFirstLevelBYID("+deptId+")) \n";
		}
//		System.out.println("the querystring"+queryString);
		List list = bll.queryByNativeSQL(queryString);
		PageObject result = new PageObject();
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
			Object[] data = (Object[])list.get(0);
			String sql = "select count(*) from hr_j_reward_approve t where t.flag = '2'and t.dept_id='"+deptId+"' and t.detail_id='"+data[0].toString()+"'";
			int count = Integer.parseInt(bll.getSingal(sql).toString());
			if(count > 0 || "1".equals(workFlowState)) {
				result.setList(list);
				result.setTotalCount(((Integer) list.size()).longValue());
			} else {
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
			HrJBigRewardDetail entity = this.findById(Long.parseLong(detailId));
			if("1".equals(entity.getWorkFlowState())){
				if("1".equals(actionId)) {
					entity.setWorkFlowState("4");
					StringBuffer sb = new StringBuffer();
					sb.append("begin\n");
					//modify by fyyang 20100802   部门等于登录人的一级部门
					String deleteSql = "delete from hr_j_reward_approve t \n" +
							" where t.dept_id = \n" +
							"(SELECT a.dept_id\n" +
							"  FROM hr_c_dept a\n" + 
							" where a.dept_level = 1\n" + 
							"   and rownum = 1\n" + 
							" START WITH a.dept_id = "+deptId+"\n" + 
							"CONNECT BY PRIOR a.pdept_id = a.dept_id) \n"+
							"and t.detail_id = '"+entity.getBigDetailId()+"';";
					sb.append(deleteSql);
					sb.append("commit;\n");
					sb.append("end;\n");
					bll.exeNativeSQL(sb.toString());
				} else if("2".equals(actionId)){
					String monthSql = "select t.BIG_AWARD_NAME from hr_j_big_reward t where t.big_reward_id = '"+entity.getBigRewardId()+"'";
					String rewardMonth = bll.getSingal(monthSql).toString();
					entity.setWorkFlowState("2");
					// 修改审批记录表
					StringBuffer sb = new StringBuffer();
					sb.append("begin\n");
					//modify by fyyang 20100802   部门等于登录人的一级部门
					String deleteSql = "delete  from   hr_j_reward_approve t where t.dept_id = " +
					"(SELECT a.dept_id\n" +
					"  FROM hr_c_dept a\n" + 
					" where a.dept_level = 1\n" + 
					"   and rownum = 1\n" + 
					" START WITH a.dept_id = "+deptId+"\n" + 
					"CONNECT BY PRIOR a.pdept_id = a.dept_id) \n"+
						" and t.detail_id = '"+entity.getBigDetailId()+"';\n";
					sb.append(deleteSql);
					sb.append("commit;\n");
					String insertSql = "INSERT INTO hr_j_reward_approve\n" +
								"  (SELECT (SELECT nvl(MAX(a.approve_id), 0)\n" + 
								"             FROM hr_j_reward_approve a) + rownum,\n" + 
								"          t.dept_id,\n" + 
								"          '"+detailId+"',\n" + 
								"		   '"+rewardMonth+"大奖等待审批！"+"',\n" +
								"		   'hr/reward/bigReward/shfitAdminApprove/shfitAdminList.jsp',\n" +
								"		   '2'" +
								"     FROM hr_c_dept t\n" + 
								"    WHERE (t.dept_status = '0' OR t.dept_status = '3')\n" + 
								"      AND t.is_banzu = '1'\n" + 
								"      AND t.is_use = 'Y'\n" + //update by sychen 20100901
//								"      AND t.is_use = 'U'\n" +
//								"      AND t.pdept_id = '1');\n";
								"      START WITH t.dept_id = (select t.dept_id from hr_c_dept t where t.dept_code= GETFirstLevelBYID("+deptId+"))"+
								"      CONNECT BY PRIOR t.dept_id = t.pdept_id);";
					sb.append(insertSql);
					sb.append("commit;\n");
					sb.append("end;\n");
					bll.exeNativeSQL(sb.toString());
					String approveSql = "select * from hr_j_reward_approve t where t.flag = '2' and t.detail_id = '"+detailId+"'";
					approvelist = bll.queryByNativeSQL(approveSql, HrJRewardApprove.class);
				}
			} else if("2".equals(entity.getWorkFlowState())) {
				entity.setWorkFlowState("3");
				StringBuffer sb = new StringBuffer();
				sb.append("begin\n");
				String deleteSql = "delete from hr_j_reward_approve t where t.dept_id = '"+deptId+"' and t.detail_id = '"+entity.getBigDetailId()+"';";
				sb.append(deleteSql);
				sb.append("commit;\n");
				sb.append("end;\n");
				bll.exeNativeSQL(sb.toString());
			} else if("3".equals(entity.getWorkFlowState())) {
				StringBuffer sb = new StringBuffer();
				sb.append("begin\n");
				String deleteSql = "delete from hr_j_reward_approve t where t.dept_id = '"+deptId+"' and t.detail_id = '"+entity.getBigDetailId()+"';";
				sb.append(deleteSql);
				sb.append("commit;\n");
				sb.append("end;\n");
				bll.exeNativeSQL(sb.toString());
			}
			this.update(entity);
		}
		return approvelist;
	}
	
	public PageObject queryBigRewardDetailByRewardMonth(String rewardMonth) {
		String queryString = "SELECT b.BIG_DETAIL_ID,\n" +
					"       b.BIG_REWARD_ID,\n" + 
					"       b.dept_id,\n" + 
					"       (select a.dept_name from hr_c_dept a where a.dept_id= b.dept_id) deptName,\n" + 
					"       b.emp_count,\n" + 
					"       b.BIG_REWARD_NUM,\n" + 
					"       b.MEMO,\n" + 
					"       decode(b.work_flow_state,'0','未分发','1','已分发','2','部门管理员已查看','3','班组已查看','4','部门管理员已查看') work_flow_state,\n" + 
					"       b.is_use,\n" + 
					"       b.enterprise_code\n" + 
					"		from hr_j_big_reward a, hr_j_big_reward_detail b where a.is_use='Y' and b.is_use='Y' and a.BIG_REWARD_ID=b.BIG_REWARD_ID and a.BIG_REWARD_MONTH = '"+rewardMonth+"'";
		List list = bll.queryByNativeSQL(queryString);
		PageObject result = new PageObject();
		result.setList(list);
		result.setTotalCount(((Integer) list.size()).longValue());
		return result;
	}
	
	/**
	 * add by fyyang 20100724
	 * 查询大奖部门明细信息
	 */
	public PageObject queryDetailBigRewardByDept(Long rewardId,Long deptId)
	{
		System.out.println(rewardId+":"+deptId);
		String sql=
			"SELECT a.emp_id,\n" +
			//modify by kzhang 2010727
			//"       b.dept_name,\n" + 
			" 		GETDEPTNAME( GETFirstLevelBYID(b.dept_id)),\n" + 
			//modify by kzhang 2010727
			//"       decode(b.is_banzu, '1', b.dept_name, '') as banzhu,\n" + 
			"		b.dept_name,\n" +
			"       a.chs_name,\n" + 
			"       (select d.big_reward_base\n" + 
			"          from HR_J_BIG_REWARD d\n" + 
			"         where d.big_reward_id="+rewardId+") base,\n" + 
			"       g.month_award,\n" + 
			//modify by kzhang 2010727
			//"       (1000000 * g.month_award) * (getabsencedays(a.emp_id, "+rewardId+")) money\n" + 
			"       ((select d.big_reward_base\n" + 
			"          from HR_J_BIG_REWARD d\n" + 
			"         where d.big_reward_id="+rewardId+") * g.month_award) * (getabsencedays(a.emp_id, "+rewardId+")) money,\n" +
            //add by fyyang 20100802
			" to_char(h.assessment_from,'yyyy-MM-dd'),\n" +
			"      to_char(h.assessment_to,'yyyy-MM-dd'),\n" + 
			"      nvl((SELECT floor(nvl(sum(nvl(GETHRDAYSBYID(t.absent_time_id), 0) +\n" + 
			"                               nvl(GETHRDAYSBYID(t.other_time_id), 0)),\n" + 
			"                           0))\n" + 
			"            FROM Hr_j_Workattendancenew t\n" + 
			"           WHERE t.is_use = 'Y'\n" + 
			"             and t.emp_id = a.emp_id\n" + 
			"             AND t.attendance_date BETWEEN h.assessment_from AND\n" + 
			"                 h.assessment_to),\n" + 
			"          0) \n"+

			"  FROM hr_j_emp_info a, hr_c_salary_personal g, hr_c_dept b\n" + 

			",(select f.assessment_from, f.assessment_to\n" +
			"          from HR_J_BIG_REWARD e, hr_c_big_award f\n" + 
			"         where e.big_award_id = f.big_award_id\n" + 
			"           and e.big_reward_id = "+rewardId+"   and f.is_use='Y' \n" + 
			"           and rownum = 1) h \n"+

			" WHERE a.is_use = 'Y'\n" + 
			"   and a.dept_id = b.dept_id\n" + 
			"   AND a.emp_state = 'U'\n" + 
			"   AND b.dept_status <> 1\n" + 
			"   and a.emp_id = g.emp_id(+)\n" + 
			"   AND a.dept_id IN\n" + 
			"       (SELECT b.dept_id\n" + 
			"          FROM hr_c_dept b\n" + 
			"         WHERE b.is_use = 'Y'\n" +  //update by sychen 20100901
//			"         WHERE b.is_use = 'U'\n" + 
			"         START WITH b.dept_id = "+deptId+"\n" + 
			"        CONNECT BY PRIOR b.dept_id = b.pdept_id)";
		sql = sql + "order by b.dept_name";
		List list=bll.queryByNativeSQL(sql);
		PageObject obj=new PageObject();
		obj.setList(list);
		obj.setTotalCount(Long.parseLong(list.size()+""));
		return obj;

	}
	
	@SuppressWarnings("unchecked")
	public List queryRewardDeptList(Long rewardId)
	{
		String sql=
			"select distinct b.dept_id, b.dept_name\n" +
			"  from HR_J_BIG_REWARD_DETAIL t, hr_c_dept b\n" + 
			" where t.big_reward_id = "+rewardId+"\n" + 
			"   and t.dept_id = b.dept_id\n" + 
			"   and t.is_use = 'Y'";
		List list=bll.queryByNativeSQL(sql);
	
		return list;

	}
}