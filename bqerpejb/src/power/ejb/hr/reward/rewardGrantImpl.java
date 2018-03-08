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
public class rewardGrantImpl implements rewardGrant {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public HrJRewardGrant save(HrJRewardGrant entity) {
		Long id = bll.getMaxId("HR_J_REWARD_GRANT", "GRANT_ID");
		entity.setGrantId(id);
		entity.setIsUse("Y");
		entityManager.persist(entity);
		return entity;
	}

	public void delete(String grantId) {
		String sql = "update HR_J_REWARD_GRANT a set a.is_use='N' where a.GRANT_ID ='"
				+ grantId + "'";
		bll.exeNativeSQL(sql);

	}

	public List<HrJRewardGrant> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public HrJRewardGrant findById(Long id) {
		// TODO Auto-generated method stub
		try {
			HrJRewardGrant instance = entityManager.find(HrJRewardGrant.class,
					id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<HrJRewardGrant> findByProperty(String propertyName, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	public HrJRewardGrant update(HrJRewardGrant entity) {
		// TODO Auto-generated method stub
		try {
			HrJRewardGrant result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List getRewardGrandList(String month, String deptId, String groupId,
			String fillBy, String enterpriseCode) {
		if (month == null) {
			String sqlMonth = "select max(t.grant_month) from hr_j_reward_grant t where t.is_use='Y'";
			if (bll.getSingal(sqlMonth) != null
					&& !"".equals(bll.getSingal(sqlMonth)))
				month = bll.getSingal(sqlMonth).toString();
		}
		String sql = "select t.grant_id,\n"
				+ "       t.grant_month,\n"
				+ "       t.dept_id,\n"
				+ "       t.group_id,\n"
				+ "       t.fill_by,\n"
				+ "       to_char(t.fill_date, 'yyyy-MM-dd'),\n"
				+ "       t.work_flow_no,\n"
				+ "       t.work_flow_state,\n"
				+ "       (select a.dept_name from hr_c_dept a where a.dept_id = t.dept_id) as deptName,\n"
				+ "       (select b.dept_name from hr_c_dept b where b.dept_id = t.group_id) as groupName,\n"
				+ "       getworkername(t.fill_by) as workName\n"
				+ "  from hr_j_reward_grant t\n" + " where t.is_use = 'Y' \n"
				+ "   and t.enterprise_code = '" + enterpriseCode + "'\n"
				+ "		and t.work_flow_state='0'\n";
		if (month != null && !"".equals(month)) {
			sql += "   and t.grant_month = '" + month + "'\n";
		}
		if (fillBy != null && !"".equals(fillBy)) {
			sql += "   and t.fill_by = '" + fillBy + "'";
		}
//		System.out.println(sql);
		return bll.queryByNativeSQL(sql);

	}

	/** *********************明细***************************** */
	public void deleteDetail(String grantId) {
		// TODO Auto-generated method stub
		String sql = "update HR_J_REWARD_GRANT_DETAIL a set a.is_use='N' where a.GRANT_ID ='"
				+ grantId + "'";
		bll.exeNativeSQL(sql);
	}

	public List<HrJRewardGrantDetail> findAllDetail() {
		// TODO Auto-generated method stub
		return null;
	}

	public HrJRewardGrantDetail findByDetailId(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveOrUpdateRewardDetail(List<HrJRewardGrantDetail> addList,
			List<HrJRewardGrantDetail> updateList, String ids) {
		if (addList != null && addList.size() > 0) {
			for (HrJRewardGrantDetail entity : addList) {
				entity.setDetailId(bll.getMaxId("HR_J_REWARD_GRANT_DETAIL",
						"DETAIL_ID"));
				entityManager.persist(entity);
				entityManager.flush();
			}
		}
		if (updateList != null && updateList.size() > 0) {
			for (HrJRewardGrantDetail entity : updateList) {
				entityManager.merge(entity);
			}
		}
		if (ids != null && ids.length() > 0) {
			String sql = "update HR_J_REWARD_GRANT_DETAIL a set a.is_use='N' where a.DETAIL_ID in ("
					+ ids + ")";
			bll.exeNativeSQL(sql);

		}

	}

	public HrJRewardGrantDetail updateDetail(HrJRewardGrantDetail entity) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean findByRewardDetail(String grandId) {
		String sql = "select count(1) from hr_j_reward_grant_detail t where t.GRANT_ID = '"
				+ grandId + "' ";
		long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0)
			return true;
		else
			return false;

	}

	@SuppressWarnings("unchecked")
	private List insetIntoRewardDetail(List list, String grintId) {
		Iterator iter = list.iterator();
		while (iter.hasNext()) {
			Object[] data = (Object[]) iter.next();
			HrJRewardGrantDetail entity = new HrJRewardGrantDetail();
			Long detailId = bll.getMaxId("HR_J_REWARD_GRANT_DETAIL",
					"DETAIL_ID");
			entity.setDetailId(detailId);
			entity.setGrantId(Long.parseLong(data[0].toString()));
			entity.setEmpId(Long.parseLong(data[1].toString()));
			entity.setCoefficientNum(Double.parseDouble(data[2].toString()));
			entity.setBaseNum(Double.parseDouble(data[3].toString()));
			entity.setAmountNum(entity.getCoefficientNum()
					* entity.getBaseNum());
			entity.setAwardNum(0d);
//			entity.setTotalNum(entity.getAmountNum() + entity.getAwardNum());
			// modify by ywliu 20100813
			entity.setIsUse("Y");
			entity.setMonthRewardNum(Double.parseDouble(data[4].toString()));
			entity.setQuantifyCash(Double.parseDouble(data[5].toString()));
		    entity.setAddValue(Double.parseDouble(data[7].toString()));//add by sychen 20100903
			entity.setTotalNum(entity.getMonthRewardNum()+entity.getQuantifyCash()+entity.getAddValue());
//			entity.setTotalNum(entity.getMonthRewardNum()+entity.getQuantifyCash());
			entity.setEnterpriseCode(data[6].toString());
			entityManager.persist(entity);
			entityManager.flush();

		}
		String sql = "select t.detail_id,\n"
				+ "       t.grant_id,\n"
				+ "       t.emp_id,\n"
				+ "       t.coefficient_num,\n"
				+ "       t.base_num,\n"
				+ "       t.amount_num,\n"
				+ "       t.award_num,\n"
				+ "       t.MONTH_REWARD_NUM,\n"
				+ "       t.QUANTIFY_CASH,\n"
				+ "       t.MONTH_ASSESS_NUM,\n"
				+ "       t.QUANTIFY_ASSESS_NUM,\n"
				+ "       t.total_num,\n"
				+ "       t.sign_by,\n"
				+ "       t.memo,\n"
				+ "       (select a.chs_name from hr_j_emp_info a where a.emp_id = t.emp_id) empName\n"
				+ "       ,t.add_value\n" //add by sychen 20100903
				+ "  from hr_j_reward_grant_detail t where  t.grant_id='"
				+ grintId + "'" + "		and t.is_use='Y'";
		return bll.queryByNativeSQL(sql);
	}

	@SuppressWarnings("unchecked")
	public List getRewardGrantDetailList(String grandId) {
		String sql;
		if (this.findByRewardDetail(grandId)) {
			sql = "select t.detail_id,\n"
					+ "       t.grant_id,\n"
					+ "       t.emp_id,\n"
					+ "       t.coefficient_num,\n"
					+ "       t.base_num,\n"
					+ "       t.amount_num,\n"
					+ "       t.award_num,\n"
					+ "       t.MONTH_REWARD_NUM,\n"
					+ "       t.QUANTIFY_CASH,\n"
					+ "       t.MONTH_ASSESS_NUM,\n"
					+ "       t.QUANTIFY_ASSESS_NUM,\n"
					+ "       t.total_num,\n"
					+ "       t.sign_by,\n"
					+ "       t.memo,\n"
					+ "       (select a.chs_name from hr_j_emp_info a where a.emp_id = t.emp_id) empName\n"
					+ "       ,t.add_value\n"//add by sychen 20100903 增加技师工会增加值
					+ "  from hr_j_reward_grant_detail t where t.grant_id ='"
					+ grandId + "'" + "		and t.is_use='Y'\n"
					+"order by empName";
			return bll.queryByNativeSQL(sql);
		} else {
			sql = "select "
					+ grandId
					+ ",\n"
					+ "             t.emp_id,\n"
					+ "            nvl(t.month_award,0),\n"
					+ "            nvl((select d.month_base  from HR_J_MONTH_REWARD d where d.reward_month = a.grant_month ),0),\n" +
					"ceil(nvl((select c.month_base * c.coefficient * t.month_award\n" +
					"         from hr_j_month_reward c\n" + 
					"        where c.reward_month = a.grant_month) *\n" + 
					"      (select (1 - d.quantify_proportion)\n" + 
					"         from HR_C_STATION_QUANTIFY d\n" + 
					"        where t.quantify_id = d.quantify_id),0)),\n" + 
					"      ceil(nvl((select c.month_base * t.month_award\n" + 
					"         from hr_j_month_reward c\n" + 
					"        where c.reward_month = a.grant_month) *\n" + 
					"      (select d.quantify_proportion\n" + 
					"         from HR_C_STATION_QUANTIFY d\n" + 
					"        where t.quantify_id = d.quantify_id) *\n" + 
					"      (select e.quantify_coefficient\n" + 
					"         from HR_C_DEPT_QUANTIFY_COEFFICIENT e\n" + 
					"        where e.dept_id =\n" + 
					"              (select b.dept_id\n" + 
					"                 from hr_c_dept b\n" + 
					"                where b.dept_code = getfirstlevelbyid(t.dept_id))\n" + 
					"          and e.coefficient_month = a.grant_month and e.is_use='Y'),0)),"
					+ "            t.enterprise_code\n"
					//add by sychen 20100903 增加技师工会增加值
					+  ",decode(b.is_chairman,\n" 
                    + "                     'Y',\n"  
                    +"                     (SELECT UNION_PER_STANDARD\n"  
                    + "                        FROM HR_C_UNION_PRESIDENT\n" 
                    + "                       WHERE is_use = 'Y'\n" 
                    + "                         AND SYSDATE BETWEEN EFFECT_START_TIME AND\n" 
                    + "                             EFFECT_END_TIME),\n" 
                    + "                     0) + decode(b.TECHNOLOGY_GRADE_ID,\n" 
                    + "                                 4,\n" 
                    +  "                                 (SELECT TECH_STANDARD\n"  
                    + "                                    FROM HR_C_TECHNICIAN_STANDARDS\n"  
                    + "                                   WHERE is_use = 'Y'\n" 
                    +  "                                     AND IS_EMPLOY = 'Y'\n" 
                    +  "                                     AND SYSDATE BETWEEN EFFECT_START_TIME AND\n" 
                    + "                                         EFFECT_END_TIME),\n"
                    + "                                 5,\n" 
                    + "                                 (SELECT TECH_STANDARD\n" 
                    + "                                    FROM HR_C_TECHNICIAN_STANDARDS\n" 
                    + "                                   WHERE is_use = 'Y'\n" 
                    + "                                     AND IS_EMPLOY = 'Y'\n" 
                    +  "                                     AND SYSDATE BETWEEN EFFECT_START_TIME AND\n" 
                    +  "                                         EFFECT_END_TIME),\n" 
                    +  "                                 0)as addValue\n"
					//add by sychen 20100903 end 
					+ "        from hr_c_salary_personal t\n"
					+ "        left join hr_j_reward_grant a on a.grant_id = '"
					+ grandId
					+ "'\n"
					+ "        left join hr_j_emp_info b on b.dept_id = a.group_id and b.is_use='Y'  and b.emp_state ='U'\n"
					+ "       where t.emp_id = b.emp_id";
			return this.insetIntoRewardDetail(bll.queryByNativeSQL(sql),
					grandId);
		}
	}

	// 公共
	@SuppressWarnings("unchecked")
	public List getInitRewardGrantDept(String deptId) {
		String sql = "select t.dept_id,t.dept_name\n" + "  from hr_c_dept t\n"
				+ "  where t.dept_level =1\n"
				+ " and rownum =1 start with t.dept_id = '" + deptId + "'\n"
				+ "connect by prior t.pdept_id = t.dept_id";
		// System.out.println(sql);
		return bll.queryByNativeSQL(sql);

	}

	@SuppressWarnings("unchecked")
	public List<HrJRewardGrant> getInintRewardGrand(String deptId,
			String grantMonth, String groupId) {
		String sql = "select t.*\n" + "  from hr_j_reward_grant t"
				+ " where t.grant_month = '" + grantMonth + "'\n"
				+ "   and t.dept_id = '" + deptId + "'\n"

				+ "   and t.group_id = '" + groupId + "'\n"
				+" and t.is_use = 'Y'";
		return bll.queryByNativeSQL(sql, HrJRewardGrant.class);
	}

	@SuppressWarnings("unchecked")
	public List getGroupNameList(String deptId, String enterpriseCode) {
		String sql = "select t.dept_id, t.Dept_Name as groupName,(select a.dept_name from hr_c_dept a where a.dept_id = '"
				+ deptId
				+ "' ) as deptName\n"
				+ "  from hr_c_dept t\n"
				+ " where t.is_banzu = 1\n"
				+ "   and (t.dept_status = 0 or t.dept_status = 3)\n"
				+ "   and t.is_use = 'Y'\n" //update by sychen 20100901
//				+ "   and t.is_use = 'U'\n" 
				+ "	and t.enterprise_code='"
				+ enterpriseCode + "'\n";
		if (deptId != null && !"".equals(deptId)) {
			sql += " start with t.pdept_id = '" + deptId + "'\n"
					+ "connect by prior t.dept_id = t.pdept_id";
		}
		return bll.queryByNativeSQL(sql);
	}

	@SuppressWarnings("unchecked")
	public List getRewardMonthAward(String empId, String grandId) {
		//update by sychen 20100903 增加根据选人员带入月度奖金、量化奖金、技师公会增加值的值
		String sql="select nvl((select a.month_base\n" +
			"             from hr_j_month_reward a, hr_j_reward_grant b\n" + 
			"            where b.grant_id ='" + grandId + "'" + 
			"              and a.reward_month = b.grant_month),\n" + 
			"           0)\n" + 
			"  from dual\n" + 
			"union all\n" + 
			"select nvl((select t.month_award\n" + 
			"             from hr_c_salary_personal t\n" + 
			"            where t.emp_id ='" + empId + "'),\n" + 
			"           0)\n" + 
			"  from dual\n" + 
			"union all\n" + 
			"select round(nvl((select c.month_base * c.coefficient * t.month_award\n" + 
			"                    from hr_j_month_reward    c,\n" + 
			"                         hr_j_reward_grant    a,\n" + 
			"                         hr_c_salary_personal t\n" + 
			"                   where c.reward_month = a.grant_month\n" + 
			"                     and a.grant_id ='" + grandId + "'\n" + 
			"                     and t.emp_id ='" + empId + "') *\n" + 
			"                 (select (1 - d.quantify_proportion)\n" + 
			"                    from HR_C_STATION_QUANTIFY d, hr_c_salary_personal t\n" + 
			"                   where t.quantify_id = d.quantify_id\n" + 
			"                     and t.emp_id = '" + empId + "'),\n" + 
			"                 0),\n" + 
			"             0)\n" + 
			"  from dual\n" + 
			"union all\n" + 
			"select round(nvl((select c.month_base * t.month_award\n" + 
			"                    from hr_j_month_reward    c,\n" + 
			"                         hr_j_reward_grant    a,\n" + 
			"                         hr_c_salary_personal t\n" + 
			"                   where c.reward_month = a.grant_month\n" + 
			"                     and a.grant_id = '" + grandId + "'\n" + 
			"                     and t.emp_id = '" + empId + "') *\n" + 
			"                 (select d.quantify_proportion\n" + 
			"                    from HR_C_STATION_QUANTIFY d, hr_c_salary_personal t\n" + 
			"                   where t.quantify_id = d.quantify_id\n" + 
			"                     and t.emp_id = '" + empId + "') *\n" + 
			"                 (select e.quantify_coefficient\n" + 
			"                    from HR_C_DEPT_QUANTIFY_COEFFICIENT e,\n" + 
			"                         hr_j_reward_grant              a,\n" + 
			"                         hr_c_salary_personal           t\n" + 
			"                   where a.grant_id = '" + grandId + "'\n" + 
			"                     and t.emp_id ='" + empId + "'\n" + 
			"                     and e.dept_id =\n" + 
			"                         (select b.dept_id\n" + 
			"                            from hr_c_dept b\n" + 
			"                           where b.dept_code = getfirstlevelbyid(t.dept_id))\n" + 
			"                     and e.coefficient_month = a.grant_month\n" + 
			"                     and e.is_use = 'Y'),\n" + 
			"                 0),\n" + 
			"             0)\n" + 
			"  from dual\n" + 
			"union all\n" + 
			"select nvl((select decode(a.is_chairman,\n" + 
			"                         'Y',\n" + 
			"                         (SELECT UNION_PER_STANDARD\n" + 
			"                            FROM HR_C_UNION_PRESIDENT\n" + 
			"                           WHERE is_use = 'Y'\n" + 
			"                             AND SYSDATE BETWEEN EFFECT_START_TIME AND\n" + 
			"                                 EFFECT_END_TIME),\n" + 
			"                         0) +\n" + 
			"                  decode(a.TECHNOLOGY_GRADE_ID,\n" + 
			"                         4,\n" + 
			"                         (SELECT TECH_STANDARD\n" + 
			"                            FROM HR_C_TECHNICIAN_STANDARDS\n" + 
			"                           WHERE is_use = 'Y'\n" + 
			"                             AND IS_EMPLOY = 'Y'\n" + 
			"                             AND SYSDATE BETWEEN EFFECT_START_TIME AND\n" + 
			"                                 EFFECT_END_TIME),\n" + 
			"                         5,\n" + 
			"                         (SELECT TECH_STANDARD\n" + 
			"                            FROM HR_C_TECHNICIAN_STANDARDS\n" + 
			"                           WHERE is_use = 'Y'\n" + 
			"                             AND IS_EMPLOY = 'Y'\n" + 
			"                             AND SYSDATE BETWEEN EFFECT_START_TIME AND\n" + 
			"                                 EFFECT_END_TIME),\n" + 
			"                         0)\n" + 
			"             from hr_j_emp_info a\n" + 
			"            where a.emp_id = '" + empId + "'),\n" + 
			"           0)\n" + 
			"  from dual";
		//update by sychen 20100903 end 
//		String sql = "select nvl((select a.month_base\n"
//				+ "             from hr_j_month_reward a, hr_j_reward_grant b\n"
//				+ "            where b.grant_id = '" + grandId + "'\n"
//				+ "              and a.reward_month = b.grant_month),\n"
//				+ "           0)\n" + "  from dual\n" + "union all\n"
//				+ "select nvl((select t.month_award\n"
//				+ "             from hr_c_salary_personal t\n"
//				+ "            where t.emp_id = '" + empId + "'),\n"
//				+ "           0)\n" + "  from dual";

		return bll.queryByNativeSQL(sql);
	}

	@SuppressWarnings("unchecked")
	public String getMaxGarntMonth() {
		String sql = "select max(t.grant_month) from hr_j_reward_grant t";
		String str = null;
		if (bll.getSingal(sql) != null) {
			str = bll.getSingal(sql).toString();
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public List getApproveGroup(String monthDate, String deptId,
			String enterpriseCode) {
		if (monthDate == null) {
			String sqlMonth = "select max(t.grant_month) from hr_j_reward_grant t where t.is_use='Y'";
			if (bll.getSingal(sqlMonth) != null
					&& !"".equals(bll.getSingal(sqlMonth)))
				monthDate = bll.getSingal(sqlMonth).toString();
		}
		String sql = "select t.group_id,\n"
				+ "       (select a.dept_name\n"
				+ "          from hr_c_dept a\n"
				+ "         where a.dept_id = t.group_id\n"
				+ "           and a.is_use = 'Y'\n" //update by sychen 20100901
//				+ "           and a.is_use = 'U'\n" 
				+ "           and a.enterprise_code = '"
				+ enterpriseCode
				+ "') as groupName\n"
				+ "  from hr_j_reward_grant t\n"
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
			sql += "		and t.grant_month = '" + monthDate + "'";
		}
		return bll.queryByNativeSQL(sql);
	}

	@SuppressWarnings("unchecked")
	public List getApproveRewardGrandList(String month, String deptId,
			String groupId, String roleId, String workFlowState, String enterpriseCode) {
		if (month == null) {
			String sqlMonth = "select max(t.grant_month) from hr_j_reward_grant t";
			if (bll.getSingal(sqlMonth) != null
					&& !"".equals(bll.getSingal(sqlMonth)))
				month = bll.getSingal(sqlMonth).toString();
		}
		String sql = "select a.grant_id,\n" + "       b.detail_id,\n"
				+ "       (select t.dept_name\n"
				+ "          from hr_c_dept t\n"
				+ "         where t.dept_id = a.dept_id\n"
				+ "           and t.is_use = 'Y'\n" //update by sychen 20100901
//				+ "           and t.is_use = 'U'\n"
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "') dept_name,\n"
				+ "       (select t.dept_name\n"
				+ "          from hr_c_dept t\n"
				+ "         where t.dept_id = a.group_id\n"
				+ "           and t.is_use = 'Y'\n"  //update by sychen 20100901
//				+ "           and t.is_use = 'U'\n" 
				+ "           and t.enterprise_code = '"
				+ enterpriseCode
				+ "')  banzu_name,\n"
				+ "       (select d.chs_name\n"
				+ "          from hr_j_emp_info d\n"
				+ "         where d.emp_id = b.emp_id\n"
				+ "           and d.is_use = 'Y'\n"
				+ "           and d.enterprise_code = '"
				+ enterpriseCode
				+ "')  chs_name,\n"
				+ "       b.coefficient_num,\n"
				+ "       b.base_num,\n"
				+ "       b.amount_num,\n"
				+ "       b.award_num,\n"
				+ "       b.MONTH_REWARD_NUM,\n"
				+ "       b.QUANTIFY_CASH,\n"
				+ "       b.MONTH_ASSESS_NUM,\n"
				+ "       b.QUANTIFY_ASSESS_NUM,\n"
				+ "       b.total_num,\n"
				+ "       b.sign_by,\n"
				+ "       b.memo,"
				+ "       getworkername(a.fill_by),"
				+ "       to_char(a.fill_date, 'yyyy-MM-dd'),"
				+ "       b.emp_id,"
				//add by qxjiao 20100727
				+"        a.work_flow_state\n"
				+"        ,b.add_value"//add by sychen 20100903
				+ "  from hr_j_reward_grant a, hr_j_reward_grant_detail b\n"
				+ " where "
				+ "   a.is_use = 'Y'\n"
				+ "   and a.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and b.is_use = 'Y'\n"
				+ "   and b.enterprise_code = '"
				+ enterpriseCode
				+ "'\n"
				+ "   and b.grant_id = a.grant_id\n";
		if (month != null && !"".equals(month)) {
			sql += "		and a.grant_month = '" + month + "'";
		}
		if (groupId != null && !"".equals(groupId)) {
			sql += "   and a.group_id = '" + groupId + "'\n";
		}
		if(roleId != null && !"".equals(roleId)) {
			if("199".equals(roleId)) {
				sql += "   and a.group_id = dept_id\n";
			} else if ("168".equals(roleId)) {
				sql += "   and a.group_id <> dept_id\n";
			}
		}
		if (workFlowState != null && !"".equals(workFlowState)) {
			sql += "   and a.work_flow_state = '" + workFlowState + "'";
		}
		
		
		//modify by wpzhu 20100728----------------高管审批页面不需要部门过滤
		 if(deptId!=null&&!"".equals(deptId))
		 {
			 sql+="and a.dept_id = nvl((select a.dept_id\n"
				+ "                        from hr_c_dept a\n"
				+ "                       where a.dept_level = 1 and rownum = 1\n"
				+ "                       start with a.dept_id = '"
				+ deptId
				+ "'\n"
				+ "                      connect by prior a.pdept_id = a.dept_id),\n"
				+ "                      "
				+ deptId
				+ ")";
		 }
		 sql = sql+"order by dept_name,banzu_name,chs_name";
//		 System.out.println(sql);
		return bll.queryByNativeSQL(sql);

	}

	@SuppressWarnings("unchecked")
	public List<HrJRewardGrant> rewardApprove(String monthDate, String deptId,String groupId,String roleId,
			String workFlowState, String enterpriseCode) {
		Long newWorkFlowState = Long.parseLong(workFlowState) + 1;
		
		String sqlQuery="select * from  hr_j_reward_grant t\n"
			+ " where t.grant_month = '" + monthDate + "'\n";
		if(!workFlowState.equals("4"))
		{
			sqlQuery += "   and t.dept_id = '" + deptId + "'\n";
		}
		sqlQuery +=  "   and t.work_flow_state = '" + workFlowState + "'\n"
			+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
			+ enterpriseCode + "'";
		
		String sql = "update hr_j_reward_grant t\n"
				+ "   set t.work_flow_state = '" + newWorkFlowState + "'\n"
				+ " where t.grant_month = '" + monthDate + "'\n";
		if(!workFlowState.equals("4"))
		{
			// modify by ywliu 20100819
			if("2".equals(workFlowState)) {
				if("199".equals(roleId)) {
					sql += "   and t.group_id = t.dept_id\n";
				} else if ("168".equals(roleId)) {
					sql += "   and t.group_id <> t.dept_id\n";
				}
			}
			sql +="   and t.dept_id = '" + deptId + "'\n";
		}
			sql	+= "   and t.work_flow_state = '" + workFlowState + "'\n"
				+ "   and t.is_use = 'Y'\n" + "   and t.enterprise_code = '"
				+ enterpriseCode + "'";
		 List<HrJRewardGrant> list=bll.queryByNativeSQL(sqlQuery, HrJRewardGrant.class);
		bll.exeNativeSQL(sql);
		return list;
	}

	@SuppressWarnings("unchecked")
	public String getDeptPeopleNum(String depiId, String enterpriseCode) {
		String sql = "SELECT COUNT(1)\n" +
				"  FROM hr_j_emp_info t\n" + 
				" WHERE t.dept_id IN (SELECT b.dept_id\n" + 
				"                       FROM hr_c_dept b\n" + 
				"                      WHERE b.is_use = 'Y'\n" +  //update by sychen 20100901
//				"                      WHERE b.is_use = 'U'\n" + 
				"                      START WITH b.dept_id = '"+depiId+"'\n" + 
				"                     CONNECT BY PRIOR b.dept_id = b.pdept_id)\n" + 
				"   AND t.is_use = 'Y'\n" + 
				"   AND t.enterprise_code = '"+enterpriseCode+"'";
		String str = null;
		if (bll.getSingal(sql) != null) {
			str = bll.getSingal(sql).toString();
		}
		return str;
	}

	@SuppressWarnings("unchecked")
	public List getAllFirstDept(String enterpriseCode) {
		String sql = "select a.dept_id, a.dept_name\n" + "  from hr_c_dept a\n"
				+ " where a.dept_level = '1'\n" + "   and a.is_use = 'Y'\n"//update by sychen 20100901
//				+ " where a.dept_level = '1'\n" + "   and a.is_use = 'U'\n"
				+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   order by a.dept_id";
		return bll.queryByNativeSQL(sql);
	}

	public String getMonthRewardNum(String monthDate, String deptId,
			String enterpriseCode) {
		String sql =

			"select nvl(b.last_month_num, 0) + nvl(b.month_reward_num, 0) +\n" +
			"       nvl(b.quantify_cash, 0) + nvl(b.extra_add_num, 0) +\n" + 
			"       nvl(b.month_assess_num, 0) + nvl(b.other_num, 0) \n"
				+ "  from hr_j_month_reward a, hr_j_month_reward_detail b\n"
				+ " where b.reward_id = a.reward_id\n"
				+ "   and a.reward_month = '" + monthDate + "'\n"
				+ "   and b.dept_id = '" + deptId + "'\n"
				+ "   and a.is_use = 'Y'\n" + "   and b.is_use = 'Y'\n"
				+ "   and a.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   and b.enterprise_code = '" + enterpriseCode + "'";
		String str = null;
		if (bll.getSingal(sql) != null) {
			str = bll.getSingal(sql).toString();
		}
		return str;
	}
	
	/**
	 * 根据当前登陆人workerID等到对应角色ID
	 * @param workerID
	 * @param enterpriseCode
	 * @return
	 */
	public List findRoleIDByWorkerId(Long workerID,String enterpriseCode) {
		String roleString = "select a.role_id\n" +
				"  from sys_j_ur a\n" + 
				" where a.worker_id = '"+workerID+"'\n" + 
				"   and a.is_use = 'Y'\n" + 
				"   and a.enterprise_code = '"+enterpriseCode+"'";
		List list = bll.queryByNativeSQL(roleString);
		return list;
	}
}