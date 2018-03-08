package power.ejb.hr.reward;

import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class HrJBigRewardGrantImpl implements HrJBigRewardGrantRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public HrJBigRewardGrant save(HrJBigRewardGrant entity) {
		Long id = bll.getMaxId("HR_J_BIG_REWARD_GRANT", "BIG_GRANT_ID");
		entity.setBigGrantId(id);
		entity.setIsUse("Y");
		entityManager.persist(entity);
		return entity;
	}

	public void delete(String bigGrantId) {
		String sql = "update HR_J_BIG_REWARD_GRANT a set a.is_use='N' where a.BIG_GRANT_ID ='"
				+ bigGrantId + "'";
		bll.exeNativeSQL(sql);
	}

	public HrJBigRewardGrant findById(Long id) {
		// TODO Auto-generated method stub
		try {
			HrJBigRewardGrant instance = entityManager.find(
					HrJBigRewardGrant.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJBigRewardGrant update(HrJBigRewardGrant entity) {
		// TODO Auto-generated method stub
		try {
			HrJBigRewardGrant result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List getBigRewardGrandList(String month, String fillBy,
			String enterpriseCode) {
		if (month == null) {
			String sqlMonth = "select max(t.big_grant_month) from hr_j_big_reward_grant t where t.is_use='Y'";
			if (bll.getSingal(sqlMonth) != null
					&& bll.getSingal(sqlMonth) != "")
				month = bll.getSingal(sqlMonth).toString();
		}
		String sql = "select t.big_grant_id,\n"
				+ "       t.big_grant_month,\n"
				+ "       t.big_award_id,\n"
				+ "       (select a.big_award_name\n"
				+ "          from hr_j_big_reward a\n"
				+ "         where a.big_award_id = t.big_award_id\n"
				+ "           and a.big_reward_month = t.big_grant_month\n"
				+ "           and a.is_use = 'Y') as bigAwardName,\n"
				+ "       t.dept_id,\n"
				+ "       (select a.dept_name from hr_c_dept a where a.dept_id = t.dept_id) as deptName,\n"
				+ "       t.group_id,\n"
				+ "       (select a.dept_name from hr_c_dept a where a.dept_id = t.group_id) as groupName,\n"
				+ "       t.fill_by,\n"
				+ "       to_char(t.fill_date,'yyyy-mm-dd'),\n"
				+ "       getworkername(t.fill_by) as workerName\n"
				+ "  from hr_j_big_reward_grant t\n"
				+ " where t.work_flow_state = '0'\n"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'";

		if (month != null && !"".equals(month)) {
			sql += "   and t.big_grant_month = '" + month + "'\n";
		}
		if (fillBy != null && !"".equals(fillBy)) {
			sql += "   and t.fill_by = '" + fillBy + "'";
		}
		return bll.queryByNativeSQL(sql);

	}

	/** *********************明细***************************** */

	public void deleteBigDetail(String bigGrantId) {
		// TODO Auto-generated method stub
		String sql = "update HR_J_BIG_REWARD_GRANT_DETAIL a set a.is_use='N' where a.BIG_GRANT_ID ='"
				+ bigGrantId + "'";
		bll.exeNativeSQL(sql);
	}

	public void saveOrUpdateBigRewardDetail(
			List<HrJBigRewardGrantDetail> addList,
			List<HrJBigRewardGrantDetail> updateList, String ids) {
		if (addList != null && addList.size() > 0) {
			for (HrJBigRewardGrantDetail entity : addList) {
				entity.setBigDetailId(bll.getMaxId(
						"HR_J_BIG_REWARD_GRANT_DETAIL", "BIG_DETAIL_ID"));
				entityManager.persist(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (HrJBigRewardGrantDetail entity : updateList) {
				entityManager.merge(entity);
			}
		}
		if (ids != null && ids.length() > 0) {
			String sql = "update HR_J_BIG_REWARD_GRANT_DETAIL a set a.is_use='N' where a.BIG_DETAIL_ID in ("
					+ ids + ")";
			bll.exeNativeSQL(sql);

		}

	}

	public boolean findByBigRewardDetail(String bigGrantId) {
		String sql = "select count(1) from HR_J_BIG_REWARD_GRANT_DETAIL t where t.BIG_GRANT_ID = '"
				+ bigGrantId + "' ";
		long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0)
			return true;
		else
			return false;

	}

	@SuppressWarnings( { "unchecked", "unused" })
	private List insetIntoBigRewardDetail(List list, String bigGrantId,
			String enterpriseCode) {
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Object[] data = (Object[]) iter.next();
			HrJBigRewardGrantDetail entity = new HrJBigRewardGrantDetail();
			Long detailId = bll.getMaxId("HR_J_BIG_REWARD_GRANT_DETAIL",
					"BIG_DETAIL_ID");
			entity.setBigDetailId(detailId);
			entity.setBigGrantId(Long.parseLong(data[0].toString()));
			entity.setEmpId(Long.parseLong(data[1].toString()));
			entity.setCoefficientNum(Double.parseDouble(data[2].toString()));
			entity.setBaseNum(Double.parseDouble(data[3].toString()));
			entity.setAmountNum(entity.getCoefficientNum()
					* entity.getBaseNum());
			entity.setIsUse("Y");
			entity.setEnterpriseCode(data[4].toString());
			entityManager.persist(entity);
			entityManager.flush();

		}
		String sql = "select t.big_detail_id,\n"
				+ "       t.big_grant_id,\n"
				+ "       t.emp_id,\n"
				+ "       t.coefficient_num,\n"
				+ "       t.base_num,\n"
				+ "       t.amount_num,\n"
				+ "       t.sign_by,\n"
				+ "       t.memo,\n"
				+ "       (select a.chs_name from hr_j_emp_info a where a.emp_id = t.emp_id) as empName\n"
				+ "  from hr_j_big_reward_grant_detail t\n"
				+ " where t.big_grant_id = '" + bigGrantId + "'\n"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'";

		return bll.queryByNativeSQL(sql);
	}

	@SuppressWarnings("unchecked")
	public List getBigRewardGrantDetailList(String bigGrantId,
			String enterpriseCode) {
		String sql;
		if (this.findByBigRewardDetail(bigGrantId)) {
			sql = "select t.big_detail_id,\n"
					+ "       t.big_grant_id,\n"
					+ "       t.emp_id,\n"
					+ "       t.coefficient_num,\n"
					+ "       t.base_num,\n"
					+ "       t.amount_num,\n"
					+ "       t.sign_by,\n"
					+ "       t.memo,\n"
					+ "       (select a.chs_name from hr_j_emp_info a where a.emp_id = t.emp_id) as empName\n"
					+ "  from hr_j_big_reward_grant_detail t\n"
					+ " where t.big_grant_id = '" + bigGrantId + "'\n"
					+ "   and t.is_use = 'Y'\n"
					+ "   and t.enterprise_code = '" + enterpriseCode + "'";
			return bll.queryByNativeSQL(sql);
		} else {
			sql = "select "
					+ bigGrantId
					+ ",\n"
					+ "             t.emp_id,\n"
					+ "            nvl(t.month_award,0),\n"
					+ "            nvl((select d.BIG_REWARD_BASE  from HR_J_BIG_REWARD d where d.BIG_REWARD_MONTH = a.big_grant_month and rownum=1),0),\n"
					+ "            t.enterprise_code\n"
					+ "        from hr_c_salary_personal t\n"
					+ "        left join hr_j_big_reward_grant a on a.big_grant_id = '"
					+ bigGrantId
					+ "'\n"
					+ "        left join hr_j_emp_info b on b.dept_id = a.group_id and b.is_use='Y'\n"
					+ "       where t.emp_id = b.emp_id";
			return this.insetIntoBigRewardDetail(bll.queryByNativeSQL(sql),
					bigGrantId, enterpriseCode);
		}
	}

	// 公共
	@SuppressWarnings("unchecked")
	public List getInitBigRewardGrantDept(String deptId) {
		String sql = "select t.dept_id,t.dept_name\n" + "  from hr_c_dept t\n"
				+ "  where t.dept_level =1\n"
				+ " and rownum =1 start with t.dept_id = '" + deptId + "'\n"
				+ "connect by prior t.pdept_id = t.dept_id";
		return bll.queryByNativeSQL(sql);
	}

	@SuppressWarnings("unchecked")
	public List<HrJBigRewardGrant> getInintBigRewardGrand(String deptId,
			String bigGrantMonth, String groupId) {
		String sql = "select t.* \n" + "  from hr_j_big_reward_grant t"
				+ " where t.big_grant_month = '" + bigGrantMonth + "'\n"
				+ "   and t.dept_id = '" + deptId + "'\n"
				+ "   and t.group_id = '" + groupId + "'\n"
				+ "   and t.is_use = 'Y'\n";

		return bll.queryByNativeSQL(sql, HrJBigRewardGrant.class);
	}

	@SuppressWarnings("unchecked")
	public List getBigRewardMonthAward(String empId, String bigGrantId) {
		String sql = "select nvl((select a.big_reward_base\n"
				+ "             from HR_J_BIG_REWARD a, hr_j_big_reward_grant b\n"
				+ "            where b.big_grant_id = '"
				+ bigGrantId
				+ "'\n"
				+ "              and a.big_reward_month = b.big_grant_month),\n"
				+ "           0)\n" + "  from dual\n" + "union all\n"
				+ "select nvl((select t.month_award\n"
				+ "             from hr_c_salary_personal t\n"
				+ "            where t.emp_id = '" + empId + "'),\n"
				+ "           0)\n" + "  from dual";

		return bll.queryByNativeSQL(sql);
	}

	@SuppressWarnings("unchecked")
	public String getMaxBigGarntMonth() {
		String sql = "select max(t.big_grant_month) from hr_j_big_reward_grant t where t.is_use='Y'";
		String str = null;
		if (bll.getSingal(sql) != null) {
			str = bll.getSingal(sql).toString();
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public List getBigApproveGroup(String monthDate, String deptId,
			String enterpriseCode) {
		if (monthDate == null) {
			String sqlMonth = "select max(t.big_grant_month) from hr_j_big_reward_grant t where t.is_use='Y'";
			if (bll.getSingal(sqlMonth) != null
					&& bll.getSingal(sqlMonth) != "")
				monthDate = bll.getSingal(sqlMonth).toString();
		}
		String sql = "select t.group_id,\n"
				+ "       (select a.dept_name\n"
				+ "          from hr_c_dept a\n"
				+ "         where a.dept_id = t.group_id\n"
				+ "           and a.is_use = 'Y'\n"
				+ "           and a.enterprise_code = '"
				+ enterpriseCode
				+ "') as groupName\n"
				+ "  from hr_j_big_reward_grant t\n"
				+ " where t.dept_id = nvl((select a.dept_id\n"
				+ "                        from hr_c_dept a\n"
				+ "                       where a.dept_level = 1 and rownum = 1\n"
				+ "                       start with a.dept_id = '"
				+ deptId
				+ "'\n"
				+ "                      connect by prior a.pdept_id = a.dept_id),\n"
				+ "                      " + deptId + ")"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'";
		if (monthDate != null && !"".equals(monthDate)) {
			sql += "		and t.big_grant_month = '" + monthDate + "'";
		}
		return bll.queryByNativeSQL(sql);
	}

	@SuppressWarnings("unchecked")
	public List getApproveBigRewardGrandList(String month, String deptId,
			String groupId, String roleId, String workFlowState, String enterpriseCode,
			String bigAwardId) {
		if (month == null || "".equals(month)) {
			String sqlMonth = "select max(t.big_grant_month) from hr_j_big_reward_grant t where t.is_use='Y'";
			if (bll.getSingal(sqlMonth) != null
					&& bll.getSingal(sqlMonth) != "")
				month = bll.getSingal(sqlMonth).toString();
		}
		String sql = "select a.big_grant_id,\n" + "       b.big_detail_id,\n"
				+ "       (select t.big_award_name\n"
				+ "          from hr_j_big_reward t\n"
				+ "         where t.big_award_id = a.big_award_id\n"
				+ "           and t.big_reward_month = a.big_grant_month\n"
				+ "           and t.is_use = 'Y'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "') as awardName,\n"
				+ "       (select d.dept_name\n"
				+ "          from hr_c_dept d\n"
				+ "         where d.dept_id = a.dept_id\n"
				+ "           and d.is_use = 'Y'\n"
				+ "           and d.enterprise_code = '"
				+ enterpriseCode
				+ "') as deptName,\n"
				+ "       (select d.dept_name\n"
				+ "          from hr_c_dept d\n"
				+ "         where d.dept_id = a.group_id\n"
				+ "           and d.is_use = 'Y'\n"
				+ "           and d.enterprise_code = '"
				+ enterpriseCode
				+ "') as groupName,\n"
				+ "       (select e.chs_name\n"
				+ "          from hr_j_emp_info e\n"
				+ "         where e.emp_id = b.emp_id\n"
				+ "           and e.is_use = 'Y'\n"
				+ "           and e.enterprise_code = '"
				+ enterpriseCode
				+ "') as chsName,\n"
				+ "       b.coefficient_num,\n"
				+ "       b.base_num,\n"
				+ "       b.amount_num,\n"
				+ "       b.sign_by,\n"
				+ "       b.memo,\n"
				+ "       getworkername(a.fill_by),\n"
				+ "       to_char(a.fill_date, 'yyyy-MM-dd'),\n"
				+ "       b.emp_id\n"
				+ "  from hr_j_big_reward_grant a, hr_j_big_reward_grant_detail b\n"
				+ " where   a.is_use = 'Y'\n"
				+ "   and a.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and b.is_use = 'Y'\n"
				+ "   and b.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and a.big_grant_id = b.big_grant_id";
		if (month != null && !"".equals(month)) {
			sql += "		and a.big_grant_month = '" + month + "'";
		}
		if (groupId != null && !"".equals(groupId)) {
			sql += "   and a.group_id = '" + groupId + "'\n";
		}
		if (workFlowState != null && !"".equals(workFlowState)) {
			sql += "   and a.work_flow_state = '" + workFlowState + "'";
		}
		if (bigAwardId != null && !"".equals(bigAwardId)) {
			sql += "   and a.big_award_id = '" + bigAwardId + "'";
		}
		if(roleId != null && !"".equals(roleId)) {
			if("199".equals(roleId)) {
				sql += "   and a.group_id = dept_id\n";
			} else if ("168".equals(roleId)) {
				sql += "   and a.group_id <> dept_id\n";
			}
		}
		if(deptId!=null&&!"".equals(deptId))
		{
			//modify by fyyang 20100729
			sql+=" \n and  a.dept_id = nvl((select t.dept_id\n"
					+ "                        from hr_c_dept t\n"
					+ "                       where t.dept_level = 1 and rownum = 1\n"
					+ "                       start with t.dept_id = '"
					+ deptId
					+ "'\n"
					+ "                      connect by prior t.pdept_id = t.dept_id),\n"
					+ "                      "
					+ deptId
					+ ")";
		}
		return bll.queryByNativeSQL(sql);

	}

	/**  modify by fyyang 20100715*/
	public List<HrJBigRewardGrant> bigRewardApprove(String monthDate, String deptId,String roleId, 
			String workFlowState, String enterpriseCode, String bigAwardId) {
		String sqlquery="select * from hr_j_big_reward_grant t\n"
			+ " where t.big_grant_month = '" + monthDate + "'\n"
			+ "   and t.work_flow_state = '" + workFlowState + "'\n"
			+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
			+ enterpriseCode + "'";
		
		Long newWorkFlowState = Long.parseLong(workFlowState) + 1;
		String sql = "update hr_j_big_reward_grant t\n"
				+ "   set t.work_flow_state = '" + newWorkFlowState + "'\n"
				+ " where t.big_grant_month = '" + monthDate + "'\n"
			//	+ "   and t.dept_id = '" + deptId + "'\n"
				+ "   and t.work_flow_state = '" + workFlowState + "'\n"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'";
		if (bigAwardId != null && !"".equals(bigAwardId)) {
			sql += "   and t.big_award_id = '" + bigAwardId + "'";
			sqlquery+="   and t.big_award_id = '" + bigAwardId + "'";
		}
		//modify by fyyang 2010-07-12
		if(deptId!=null&&!deptId.equals("")&&!workFlowState.equals("4"))
		{
		  sql+="   and t.dept_id = '" + deptId + "'\n";
		  sqlquery+="   and t.dept_id = '" + deptId + "'\n";
		  // add by ywliu 20100820 
		  if(roleId != null && !"".equals(roleId)) {
				if("199".equals(roleId)) {
					sql += "   and a.group_id = dept_id\n";
					sqlquery += "   and a.group_id = dept_id\n";
				} else if ("168".equals(roleId)) {
					sql += "   and a.group_id <> dept_id\n";
					sqlquery += "   and a.group_id = dept_id\n";
				}
			}
		}
		List<HrJBigRewardGrant> list=bll.queryByNativeSQL(sqlquery, HrJBigRewardGrant.class);
		bll.exeNativeSQL(sql);
		return list;
		
		
	}

	@SuppressWarnings("unchecked")
	public List getBigAwareNameList(String monthDate, String enterpriseCode,
			String deptId) {
		String sql = "select t.big_reward_month,\n"
				+ "       t.big_award_id,\n" + "       t.big_award_name,\n"
				+ "       t.big_reward_base\n"
				+ "  from hr_j_big_reward t, hr_j_big_reward_detail l\n"
				+ " where t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'\n"
				+ "   and t.big_reward_id = l.big_reward_id\n"
				+ "   and l.dept_id = '" + deptId + "'";

		if (monthDate != null && !"".equals(monthDate)) {
			sql += "	and t.big_reward_month='" + monthDate + "'";
		}
		return bll.queryByNativeSQL(sql);
	}

	public String getBigRewardNum(String monthDate, String bigAwardId,
			String deptId, String enterpriseCode) {
		String sql = "select a.big_reward_num\n"
				+ "  from HR_J_BIG_REWARD_DETAIL a, HR_J_BIG_REWARD b\n"
				+ " where b.big_reward_month = '" + monthDate + "'\n"
			//	+ "   and b.big_award_id = '" + bigAwardId + "'\n"
				+ "   and b.big_reward_id = a.big_reward_id\n"
				+ "   and a.dept_id = '" + deptId + "'\n"
				+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
				+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and b.enterprise_code = '" + enterpriseCode + "'";
		String str = null;
		if (bll.getSingal(sql) != null) {
			str = bll.getSingal(sql).toString();
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public List getBigRewardApproveAwareList(String monthDate, String deptId,
			String groupId, String workFlowState, String enterpriseCode) {
		if (monthDate == null || "".equals(monthDate)) {
			String sqlMonth = "select max(t.big_grant_month) from hr_j_big_reward_grant t where t.is_use='Y'";
			if (bll.getSingal(sqlMonth) != null
					&& bll.getSingal(sqlMonth) != "")
				monthDate = bll.getSingal(sqlMonth).toString();
		}
		String sql = "select t.big_award_id, l.big_award_name\n"
				+ "  from hr_j_big_reward_grant t, hr_j_big_reward l\n"
				+ " where  t.dept_id = nvl((select a.dept_id\n"
				+ "                        from hr_c_dept a\n"
				+ "                       where a.dept_level = 1 and rownum = 1\n"
				+ "                       start with a.dept_id = '"
				+ deptId
				+ "'\n"
				+ "                      connect by prior a.pdept_id = a.dept_id),\n"
				+ "                      " + deptId + ")"
				+ "   and t.work_flow_state = '" + workFlowState + "'\n"
				+ "   and t.big_award_id = l.big_award_id\n"
				+ "   and t.big_grant_month = l.big_reward_month\n"
				+ "   and t.is_use = 'Y'\n" + "   and l.is_use = 'Y'\n"
				+ "   and t.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and l.enterprise_code = '" + enterpriseCode + "'\n";
		if (monthDate != null && !"".equals(monthDate)) {
			sql += "		and t.big_grant_month = '" + monthDate + "'";
		}
		if (groupId != null && !"".equals(groupId)) {
			sql += "   and t.group_id = '" + groupId + "'\n";
		}
		return bll.queryByNativeSQL(sql);

	}
	
//   /**
//    * add by fyyang 20100712
//    * @param model
//    */
//	public void reportBigeRewardGrandReport(HrJBigRewardGrant model)
//	{
//		HrJRewardApprove approve = new HrJRewardApprove();
//		approve.setApproveId(bll.getMaxId("HR_J_REWARD_APPROVE", "APPROVE_ID"));
//		approve.setDeptId(model.getDeptId());
//		approve.setDetailId(model.getBigGrantId());
//		approve.setFlag("3");
//		approve.setContent(model.getBigGrantMonth()+"大奖上报等待汇总！");
//		approve.setFlowListUrl("hr/reward/bigReward/bigRewardReport/approve/gather/bigRewardGrantGather.jsp");
//		entityManager.persist(approve);
//	}
	
	
}