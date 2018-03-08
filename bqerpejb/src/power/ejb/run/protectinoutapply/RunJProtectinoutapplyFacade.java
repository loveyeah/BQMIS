package power.ejb.run.protectinoutapply;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
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
import power.ejb.run.protectinoutapply.form.ProAppInfoForm;
import power.ejb.run.protectinoutapply.form.ProtectInOutApplyInfo;

/**
 * Facade for entity RunJProtectinoutapply.
 * 
 * @see power.ejb.run.protectinoutapply.RunJProtectinoutapply
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class RunJProtectinoutapplyFacade implements
		RunJProtectinoutapplyFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	/**
	 * 保存
	 */
	public RunJProtectinoutapply save(RunJProtectinoutapply entity) {
		LogUtil.log("saving RunJProtectinoutapply instance", Level.INFO, null);
		try {
			entity.setApplyId(bll.getMaxId("run_j_protectinoutapply",
					"apply_id"));
			entity.setProtectNo(this.createProtectNo());
			entity.setIsUse("Y");
			entity.setApplyDate(new Date());
			entity.setLastModifyDate(new Date());
			entity.setStatusId(1l); // 未上报
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;

		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * 删除
	 */
	public void delete(Long id) {
		RunJProtectinoutapply entity = this.findById(id);
		if (entity != null) {
			entity.setIsUse("N");
			this.update(entity);
		}
	}

	/**
	 * 删除多条记录
	 */
	public void deleteMulti(String ids) {
		String sql = "update run_j_protectinoutapply a\n"
				+ "   set a.is_use = 'N'\n" + " where a.apply_id in (" + ids
				+ ")\n" + "   and a.is_use = 'Y'";
		bll.exeNativeSQL(sql);
	}

	public RunJProtectinoutapply update(RunJProtectinoutapply entity) {
		LogUtil
				.log("updating RunJProtectinoutapply instance", Level.INFO,
						null);
		try {
			RunJProtectinoutapply result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public RunJProtectinoutapply findById(Long id) {
		LogUtil.log("finding RunJProtectinoutapply instance with id: " + id,
				Level.INFO, null);
		try {
			RunJProtectinoutapply instance = entityManager.find(
					RunJProtectinoutapply.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunJProtectinoutapply> findByProperty(String propertyName,
			final Object value, final int... rowStartIdxAndCount) {
		LogUtil.log("finding RunJProtectinoutapply instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from RunJProtectinoutapply model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public List<RunJProtectinoutapply> findAll(final int... rowStartIdxAndCount) {
		LogUtil.log("finding all RunJProtectinoutapply instances", Level.INFO,
				null);
		try {
			final String queryString = "select model from RunJProtectinoutapply model";
			Query query = entityManager.createQuery(queryString);
			if (rowStartIdxAndCount != null && rowStartIdxAndCount.length > 0) {
				int rowStartIdx = Math.max(0, rowStartIdxAndCount[0]);
				if (rowStartIdx > 0) {
					query.setFirstResult(rowStartIdx);
				}

				if (rowStartIdxAndCount.length > 1) {
					int rowCount = Math.max(0, rowStartIdxAndCount[1]);
					if (rowCount > 0) {
						query.setMaxResults(rowCount);
					}
				}
			}
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public PageObject findList(String strWhere,
			final int... rowStartIdxAndCount) {
		try {
			PageObject result = new PageObject();
			String sql = "select * from  run_j_protectinoutapply \n";
			if (strWhere != "") {
				sql = sql + " where  " + strWhere;
			}
			List<RunJProtectinoutapply> list = bll.queryByNativeSQL(sql,
					RunJProtectinoutapply.class, rowStartIdxAndCount);
			String sqlCount = "select count(1)　from run_j_protectinoutapply \n";
			if (strWhere != "") {
				sqlCount = sqlCount + " where  " + strWhere;
			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			result.setList(list);
			result.setTotalCount(totalCount);
			return result;

		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}

	}

	/**
	 * 通过申请单号查找信息
	 */
	public RunJProtectinoutapply findByProtectNo(String protectNo,
			String enterpriseCode) {
		String strWhere = " protect_no='" + protectNo + "'\n"
				+ "and enterprise_code='" + enterpriseCode + "'\n"
				+ "and is_use='Y'";
		PageObject result = findList(strWhere);
		if (result.getList() != null) {
			if (result.getList().size() > 0) {
				return (RunJProtectinoutapply) result.getList().get(0);
			}
		}
		return null;
	}

	/**
	 * 生成保护投退申请编号
	 * 
	 * @return
	 */
	private String createProtectNo() {
		String mymonth = "";
		java.text.SimpleDateFormat tempDate = new java.text.SimpleDateFormat(
				"yyyy-MM-dd" + " " + "hh:mm:ss");
		mymonth = tempDate.format(new java.util.Date());
		mymonth = mymonth.substring(0, 4) + mymonth.substring(5, 7);
		String no = "P" + mymonth;
		String sql = "select '"
				+ no
				+ "' ||\n"
				+ "       (select Trim(case\n"
				+ "                 when max(t.protect_no) is null then\n"
				+ "                  '001'\n"
				+ "                 else\n"
				+ "                  to_char(to_number(substr(max(t.protect_no), 8, 3) + 1),\n"
				+ "                          '000')\n"
				+ "               end)\n"
				+ "          from run_j_protectinoutapply t\n"
				+ "         where t.is_use = 'Y'\n"
				+ "           and substr(t.protect_no, 0, 7) = '" + no + "')\n"
				+ "  from dual";
		no = bll.getSingal(sql).toString().trim();
		return no;
	}

	/** add by fyyang */
	@SuppressWarnings("unchecked")
	private PageObject findQueryList(String strWhere,
			final int... rowStartIdxAndCount) {
		try {

			String sqlCount = "select count(1)　from run_j_protectinoutapply \n";
			if (strWhere != "") {
				sqlCount = sqlCount + " where  " + strWhere;
			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			if (totalCount > 0) {
				PageObject result = new PageObject();
				String sql = "select t.apply_id,t.protect_no,\n"
						+ "t.apply_by,GETWORKERNAME(t.apply_by),\n"
						+ "to_char(t.apply_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "t.apply_dept,GETDEPTNAME(t.apply_dept) deptName,\n"
						+ "t.equ_code,GETEQUNAMEBYCODE(t.equ_code) equName,\n"
						+ "t.protect_name,t.protect_reason,\n"
						+ "t.equ_effect,t.out_safety,\n"
						+ "to_char(t.apply_start_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "to_char(t.apply_end_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "t.execute_by,GETWORKERNAME(t.execute_by),\n"
						+ "t.keeper,GETWORKERNAME(t.keeper),\n"
						+ "t.permit_by,GETWORKERNAME(t.permit_by),\n"
						+ "t.checkup_by,GETWORKERNAME(t.checkup_by),\n"
						+ "to_char(t.protect_in_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "to_char(t.protect_out_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "t.memo,t.work_flow_no,\n"
						+ "t.status_id,t.last_modify_by,\n"
						+ "to_char(t.last_modify_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "to_char(t.approve_start_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "to_char(t.approve_end_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
						+ "t.is_in ,\n" + "t.is_select ,\n"
						+ "t.relative_no \n"
						+ "from run_j_protectinoutapply t \n";

				if (strWhere != "") {
					sql = sql + " where  " + strWhere;
				}
				List list = new ArrayList();
				List myList = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				Iterator it = myList.iterator();
				while (it.hasNext()) {
					ProtectInOutApplyInfo model = new ProtectInOutApplyInfo();
					RunJProtectinoutapply entity = new RunJProtectinoutapply();
					Object[] data = (Object[]) it.next();
					if (data[0] != null) {
						entity.setApplyId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null) {
						entity.setProtectNo(data[1].toString());
					}
					if (data[2] != null) {
						entity.setApplyBy(data[2].toString());
					}
					if (data[3] != null) {
						model.setApplyName(data[3].toString());
					}
					if (data[4] != null) {
						model.setApplyDate(data[4].toString());
					}
					if (data[5] != null) {
						entity.setApplyDept(data[5].toString());
					}
					if (data[6] != null) {
						model.setApplyDeptName(data[6].toString());
					}
					if (data[7] != null) {
						entity.setEquCode(data[7].toString());

					}
					if (data[8] != null) {
						model.setEquName(data[8].toString());
					}
					if (data[9] != null) {
						entity.setProtectName(data[9].toString());
					}
					if (data[10] != null) {
						entity.setProtectReason(data[10].toString());
					}
					if (data[11] != null) {
						entity.setEquEffect(data[11].toString());
					}
					if (data[12] != null) {
						entity.setOutSafety(data[12].toString());
					}
					if (data[13] != null) {
						model.setApplyStartDate(data[13].toString());
					}
					if (data[14] != null) {
						model.setApplyEndDate(data[14].toString());
					}
					if (data[15] != null) {
						entity.setExecuteBy(data[15].toString());
					}
					if (data[16] != null) {
						model.setExecuteByName(data[16].toString());
					}
					if (data[17] != null) {
						entity.setKeeper(data[17].toString());
					}
					if (data[18] != null) {
						model.setKeeperName(data[18].toString());
					}
					if (data[19] != null) {
						entity.setPermitBy(data[19].toString());
					}
					if (data[20] != null) {
						model.setPermitByName(data[20].toString());
					}
					if (data[21] != null) {
						entity.setCheckupBy(data[21].toString());
					}
					if (data[22] != null) {
						model.setCheckupByName(data[22].toString());
					}
					if (data[23] != null) {
						model.setStrProtectInDate(data[23].toString());
					}
					if (data[24] != null) {
						model.setStrProtectOutDate(data[24].toString());
					}
					if (data[25] != null) {
						entity.setMemo(data[25].toString());
					}
					if (data[26] != null) {
						entity.setWorkFlowNo(Long
								.parseLong(data[26].toString()));
					}
					if (data[27] != null) {
						entity.setStatusId(Long.parseLong(data[27].toString()));
					}
					if (data[30] != null) {
						model.setStrApproveStartDate(data[30].toString());
					}
					if (data[31] != null) {
						model.setStrApproveEndDate(data[31].toString());
					}
					if (data[32] != null) {
						entity.setIsIn(data[32].toString());
					}
					if (data[33] != null) {
						entity.setIsSelect(data[33].toString());
					}
					if (data[34] != null) {
						entity.setRelativeNo(data[34].toString());
					}

					model.setPower(entity);
					list.add(model);
				}

				result.setList(list);
				result.setTotalCount(totalCount);
				return result;
			}
			return null;

		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}

	}

	/** add by fyyang */
	public PageObject findApproveList(String startDate, String endDate,
			String applyDeptId, String status, String entryIds,
			String enterpriseCode, final int... rowStartIdxAndCount) {
		String sqlWhere = " enterprise_code='" + enterpriseCode
				+ "'  and is_use='Y' \n";
		if (startDate != null && !startDate.equals("")) {
			sqlWhere += "   and apply_start_date >=to_date('" + startDate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";

		}
		if (endDate != null && !endDate.equals("")) {
			sqlWhere += "   and apply_end_date <=to_date('" + endDate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (applyDeptId != null && !applyDeptId.equals("")) {
			sqlWhere += " and  apply_dept=(\n"
					+ "select a.dept_code from hr_c_dept a\n"
					+ "where a.dept_id=" + applyDeptId
					+ "  and a.is_use='Y' and rownum=1) \n";//update by sychen 20100902
//			+ "  and a.is_use='U' and rownum=1) \n";
		}
		if (status != null && !status.equals("")) {
			sqlWhere += " and status_id=" + status + "  \n";
		}

		if (entryIds == null || "".equals(entryIds)) {
			entryIds = "''";
		}
		sqlWhere += " and work_flow_no in (" + entryIds
				+ ")    order by apply_start_date\n";

		return this.findQueryList(sqlWhere, rowStartIdxAndCount);

	}

	public PageObject findListForQuery(String startDate, String endDate,
			String applyDeptId, String status, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		String sqlWhere = " enterprise_code='" + enterpriseCode
				+ "'  and is_use='Y' \n";
		if (startDate != null && !startDate.equals("")) {
			sqlWhere += "   and apply_start_date >=to_date('" + startDate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";

		}
		if (endDate != null && !endDate.equals("")) {
			sqlWhere += "   and apply_end_date <=to_date('" + endDate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (applyDeptId != null && !applyDeptId.equals("")) {
			sqlWhere += " and  apply_dept=(\n"
					+ "select a.dept_code from hr_c_dept a\n"
					+ "where a.dept_id=" + applyDeptId
					+ "  and a.is_use='Y' and rownum=1) \n";  //update by sychen 20100902
//			+ "  and a.is_use='U' and rownum=1) \n"; 
		}
		if (status != null && !status.equals("")) {
			sqlWhere += " and status_id=" + status + "  \n";
		}
		sqlWhere += " order by apply_start_date";
		return this.findQueryList(sqlWhere, rowStartIdxAndCount);

	}

	@SuppressWarnings("unchecked")
	public PageObject findRegisterList(String startDate, String endDate,
			String applyDeptId, String status, String enterpriseCode,
			final int... rowStartIdxAndCount) {
		String sqlWhere = " enterprise_code='" + enterpriseCode
				+ "'  and is_use='Y' \n";
		if (startDate != null && !startDate.equals("")) {
			sqlWhere += "   and apply_start_date >=to_date('" + startDate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";

		}
		if (endDate != null && !endDate.equals("")) {
			sqlWhere += "   and apply_end_date <=to_date('" + endDate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (applyDeptId != null && !applyDeptId.equals("")) {
			sqlWhere += " and  apply_dept=(\n"
					+ "select a.dept_code from hr_c_dept a\n"
					+ "where a.dept_id=" + applyDeptId
					+ "  and a.is_use='Y' and rownum=1) \n";  //update by sychen 20100902
//			+ "  and a.is_use='U' and rownum=1) \n"; 
		}
		if (status != null && status.length() > 0) {
			sqlWhere += " and status_id in (" + status + ")";
		}
		sqlWhere += " order by apply_start_date";
		return this.findQueryList(sqlWhere, rowStartIdxAndCount);
	}

	public PageObject findByIsin(String startDate, String endDate,
			String applyDeptId, String status, String enterpriseCode,
			String isIn, final int... rowStartIdxAndCount) {
		String sqlWhere = " enterprise_code='" + enterpriseCode
				+ "'  and is_use='Y' and is_select='N'\n";
		if (startDate != null && !startDate.equals("")) {
			sqlWhere += "   and apply_start_date >=to_date('" + startDate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";

		}
		if (endDate != null && !endDate.equals("")) {
			sqlWhere += "   and apply_end_date <=to_date('" + endDate
					+ "'||' 00:00:00', 'yyyy-MM-dd hh24:mi:ss')\n";
		}
		if (applyDeptId != null && !applyDeptId.equals("")) {
			sqlWhere += " and  apply_dept=(\n"
					+ "select a.dept_code from hr_c_dept a\n"
					+ "where a.dept_id=" + applyDeptId
					+ "  and a.is_use='Y' and rownum=1) \n"; //update by sychen 20100902
//			+ "  and a.is_use='U' and rownum=1) \n"; 
		}
		if (status != null && status.length() > 0) {
			sqlWhere += " and status_id in (" + status + ")";
		}
		if (isIn != null && !isIn.equals("")) {
			sqlWhere += " and is_in='" + isIn + "' ";
		}
		sqlWhere += " order by apply_start_date";
		try {

			String sqlCount = "select count(1)　from run_j_protectinoutapply \n";
			if (sqlWhere != "") {
				sqlCount = sqlCount + " where  " + sqlWhere;
			}
			Long totalCount = Long
					.parseLong(bll.getSingal(sqlCount).toString());
			if (totalCount > 0) {
				PageObject result = new PageObject();
				String sql = "select t.apply_id,t.protect_no,\n"
						+ "t.apply_by,GETWORKERNAME(t.apply_by),\n"
						+ "to_char(t.apply_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "t.apply_dept,GETDEPTNAME(t.apply_dept) deptName,\n"
						+ "t.equ_code,GETEQUNAMEBYCODE(t.equ_code) equName,\n"
						+ "t.protect_name,t.protect_reason,\n"
						+ "t.equ_effect,t.out_safety,\n"
						+ "to_char(t.apply_start_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "to_char(t.apply_end_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "t.execute_by,GETWORKERNAME(t.execute_by),\n"
						+ "t.keeper,GETWORKERNAME(t.keeper),\n"
						+ "t.permit_by,GETWORKERNAME(t.permit_by),\n"
						+ "t.checkup_by,GETWORKERNAME(t.checkup_by),\n"
						+ "to_char(t.protect_in_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "to_char(t.protect_out_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "t.memo,t.work_flow_no,\n"
						+ "t.status_id,t.last_modify_by,\n"
						+ "to_char(t.last_modify_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "to_char(t.approve_start_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
						+ "to_char(t.approve_end_date, 'yyyy-MM-dd hh24:mi:ss')\n"
						+ "from run_j_protectinoutapply t \n";

				if (sqlWhere != "") {
					sql = sql + " where  " + sqlWhere;
				}
				List list = new ArrayList();
				List myList = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
				Iterator it = myList.iterator();
				while (it.hasNext()) {
					ProtectInOutApplyInfo model = new ProtectInOutApplyInfo();
					RunJProtectinoutapply entity = new RunJProtectinoutapply();
					Object[] data = (Object[]) it.next();
					if (data[0] != null) {
						entity.setApplyId(Long.parseLong(data[0].toString()));
					}
					if (data[1] != null) {
						entity.setProtectNo(data[1].toString());
					}
					if (data[2] != null) {
						entity.setApplyBy(data[2].toString());
					}
					if (data[3] != null) {
						model.setApplyName(data[3].toString());
					}
					if (data[4] != null) {
						model.setApplyDate(data[4].toString());
					}
					if (data[5] != null) {
						entity.setApplyDept(data[5].toString());
					}
					if (data[6] != null) {
						model.setApplyDeptName(data[6].toString());
					}
					if (data[7] != null) {
						entity.setEquCode(data[7].toString());

					}
					if (data[8] != null) {
						model.setEquName(data[8].toString());
					}
					if (data[9] != null) {
						entity.setProtectName(data[9].toString());
					}
					if (data[10] != null) {
						entity.setProtectReason(data[10].toString());
					}
					if (data[11] != null) {
						entity.setEquEffect(data[11].toString());
					}
					if (data[12] != null) {
						entity.setOutSafety(data[12].toString());
					}
					if (data[13] != null) {
						model.setApplyStartDate(data[13].toString());
					}
					if (data[14] != null) {
						model.setApplyEndDate(data[14].toString());
					}
					if (data[15] != null) {
						entity.setExecuteBy(data[15].toString());
					}
					if (data[16] != null) {
						model.setExecuteByName(data[16].toString());
					}
					if (data[17] != null) {
						entity.setKeeper(data[17].toString());
					}
					if (data[18] != null) {
						model.setKeeperName(data[18].toString());
					}
					if (data[19] != null) {
						entity.setPermitBy(data[19].toString());
					}
					if (data[20] != null) {
						model.setPermitByName(data[20].toString());
					}
					if (data[21] != null) {
						entity.setCheckupBy(data[21].toString());
					}
					if (data[22] != null) {
						model.setCheckupByName(data[22].toString());
					}
					if (data[23] != null) {
						model.setStrProtectInDate(data[23].toString());
					}
					if (data[24] != null) {
						model.setStrProtectOutDate(data[24].toString());
					}
					if (data[25] != null) {
						entity.setMemo(data[25].toString());
					}
					if (data[26] != null) {
						entity.setWorkFlowNo(Long
								.parseLong(data[26].toString()));
					}
					if (data[27] != null) {
						entity.setStatusId(Long.parseLong(data[27].toString()));
					}
					if (data[30] != null) {
						model.setStrApproveStartDate(data[30].toString());
					}
					if (data[31] != null) {
						model.setStrApproveEndDate(data[31].toString());
					}
					model.setPower(entity);
					list.add(model);
				}

				result.setList(list);
				result.setTotalCount(totalCount);
				return result;
			}
			return null;

		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	public ProAppInfoForm findByNo(String protectNo,
			String enterpriseCode) {
		String sql = "select t.apply_id,t.protect_no,\n"
				+ "t.apply_by,GETWORKERNAME(t.apply_by),\n"
				+ "to_char(t.apply_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
				+ "t.apply_dept,GETDEPTNAME(t.apply_dept) deptName,\n"
				+ "t.equ_code,GETEQUNAMEBYCODE(t.equ_code) equName,\n"
				+ "t.protect_name,t.protect_reason,\n"
				+ "t.equ_effect,t.out_safety,\n"
				+ "to_char(t.apply_start_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
				+ "to_char(t.apply_end_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
				+ "t.execute_by,GETWORKERNAME(t.execute_by),\n"
				+ "t.keeper,GETWORKERNAME(t.keeper),\n"
				+ "t.permit_by,GETWORKERNAME(t.permit_by),\n"
				+ "t.checkup_by,GETWORKERNAME(t.checkup_by),\n"
				+ "to_char(t.protect_in_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
				+ "to_char(t.protect_out_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
				+ "t.memo,t.work_flow_no,\n"
				+ "t.status_id,t.last_modify_by,\n"
				+ "to_char(t.last_modify_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
				+ "to_char(t.approve_start_date, 'yyyy-MM-dd hh24:mi:ss') ,\n"
				+ "to_char(t.approve_end_date, 'yyyy-MM-dd hh24:mi:ss'),\n"
				+ "t.is_in ,\n" + "t.is_select ,\n" + "t.relative_no \n"
				+ "from run_j_protectinoutapply t \n" + "where t.protect_no='"
				+ protectNo + "' and t.enterprise_code='" + enterpriseCode
				+ "'";
		List myList = bll.queryByNativeSQL(sql);
		if (myList != null) {
			Object obj = myList.get(0);
//			ProtectInOutApplyInfo model = new ProtectInOutApplyInfo();
			ProAppInfoForm entity = new ProAppInfoForm();
			Object[] data = (Object[]) obj;
			if (data[0] != null) {
				entity.setApplyId(Long.parseLong(data[0].toString()));
			}
			if (data[1] != null) {
				entity.setProtectNo(data[1].toString());
			}
			if (data[2] != null) {
				entity.setApplyBy(data[2].toString());
			}
			if (data[3] != null) {
				entity.setApplyName(data[3].toString());
			}
			if (data[4] != null) {
				entity.setApplyDate(data[4].toString());
			}
			if (data[5] != null) {
				entity.setApplyDept(data[5].toString());
			}
			if (data[6] != null) {
				entity.setApplyDeptName(data[6].toString());
			}
			if (data[7] != null) {
				entity.setEquCode(data[7].toString());

			}
			if (data[8] != null) {
				entity.setEquName(data[8].toString());
			}
			if (data[9] != null) {
				entity.setProtectName(data[9].toString());
			}
			if (data[10] != null) {
				entity.setProtectReason(data[10].toString());
			}
			if (data[11] != null) {
				entity.setEquEffect(data[11].toString());
			}
			if (data[12] != null) {
				entity.setOutSafety(data[12].toString());
			}
			if (data[13] != null) {
				entity.setApplyStartDate(data[13].toString());
			}
			if (data[14] != null) {
				entity.setApplyEndDate(data[14].toString());
			}
			if (data[15] != null) {
				entity.setExecuteBy(data[15].toString());
			}
			if (data[16] != null) {
				entity.setExecuteByName(data[16].toString());
			}
			if (data[17] != null) {
				entity.setKeeper(data[17].toString());
			}
			if (data[18] != null) {
				entity.setKeeperName(data[18].toString());
			}
			if (data[19] != null) {
				entity.setPermitBy(data[19].toString());
			}
			if (data[20] != null) {
				entity.setPermitByName(data[20].toString());
			}
			if (data[21] != null) {
				entity.setCheckupBy(data[21].toString());
			}
			if (data[22] != null) {
				entity.setCheckupByName(data[22].toString());
			}
			if (data[23] != null) {
				entity.setStrProtectInDate(data[23].toString());
			}
			if (data[24] != null) {
				entity.setStrProtectOutDate(data[24].toString());
			}
			if (data[25] != null) {
				entity.setMemo(data[25].toString());
			}
			if (data[26] != null) {
				entity.setWorkFlowNo(Long.parseLong(data[26].toString()));
			}
			if (data[27] != null) {
				entity.setStatusId(Long.parseLong(data[27].toString()));
			}
			if (data[30] != null) {
				entity.setStrApproveStartDate(data[30].toString());
			}
			if (data[31] != null) {
				entity.setStrApproveEndDate(data[31].toString());
			}
			return entity;
		} else {
			return null;
		}

	}
}