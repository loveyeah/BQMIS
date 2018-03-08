package power.ejb.resource;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import power.ear.comm.Employee;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.resource.form.CostFromInfo;
import power.ejb.resource.form.MRPGatherDetailInfo;
import power.ejb.resource.form.MrpJPlanRequirementDetailEdit;
import power.ejb.resource.form.MrpJPlanRequirementDetailInfo;

/**
 * Facade for entity MrpJPlanRequirementHead.
 * 
 * @see power.ejb.resource.MrpJPlanRequirementHead
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class MrpJPlanRequirementHeadFacade implements
		MrpJPlanRequirementHeadFacadeRemote {
	// property constants
	public static final String MR_NO = "mrNo";
	public static final String WO_NO = "woNo";
	public static final String PLAN_ORIGINAL_ID = "planOriginalId";
	public static final String ITEM_ID = "itemId";
	public static final String MR_BY = "mrBy";
	public static final String MR_DEPT = "mrDept";
	public static final String PLAN_GRADE = "planGrade";
	public static final String COST_DEPT = "costDept";
	public static final String COST_SPECIAL = "costSpecial";
	public static final String MR_REASON = "mrReason";
	public static final String MR_TYPE = "mrType";
	public static final String WF_NO = "wfNo";
	public static final String WF_STATE = "wfState";
	public static final String ENTERPRISE_CODE = "enterpriseCode";
	public static final String IS_USE = "isUse";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	BaseDataManager bllEmp = (BaseDataManager) Ejb3Factory.getInstance()
			.getFacadeRemote("BaseDataManagerImpl");
	
	
	public PageObject getCostFrom(String sTime,String eTime,String outSTime,String outETime,String deptCode,String costFrom)
	{
		
		String sql=
			"select a.fee_by_dep,\n" +
			"       d.dept_name,\n" + 
			"       a.zbbmtx_code,\n" + 
			"       LPAD(a.zbbmtx_name,\n" + 
			"            length(a.zbbmtx_code) - 3 + length(a.zbbmtx_name) * 2),\n" + 
			"       a.ll 已发生,\n" + 
			"       b.advice_budget * 10000 预算费用\n" + 
			"  from (select i.fee_by_dep,\n" + 
			"               c.zbbmtx_code,\n" + 
			"               c.item_id,\n" + 
			"               c.zbbmtx_name,\n" + 
			"               sum(round(t.trans_qty * t.std_cost, 2)) ll," +
			"               t.check_date as checkDate," +
			"               t.last_modified_date as lastModifyDate\n" + 
			"          from inv_j_transaction_his t, inv_j_issue_head i, cbm_c_itemtx c\n" + 
			"         where t.order_no = i.issue_no\n" + 
			"           and instr(i.item_code, c.zbbmtx_code) = 1\n" + 
			"           and i.item_code like '002%'\n" + 
			"           and c.is_use = 'Y'\n" + 
			"           and i.is_use = 'Y'\n" + 
			"           and t.is_use = 'Y'\n" + 
			"         group by c.zbbmtx_code, i.fee_by_dep, c.item_id, c.zbbmtx_name,t.check_date,t.last_modified_date\n" + 
			"         order by i.fee_by_dep, c.zbbmtx_code) a,\n" + 
			"       (select b.item_id, b.advice_budget, e.dep_code\n" + 
			"          from cbm_j_budget_item b, cbm_j_budget_make m, cbm_c_center e\n" + 
			"         where b.budget_make_id = m.budget_make_id\n" + 
			"           and m.center_id = e.center_id) b,\n" + 
			"       hr_c_dept d\n" + 
			" where a.item_id = b.item_id(+)\n" + 
			"   and a.fee_by_dep = b.dep_code(+)\n" + 
			"   and a.fee_by_dep = d.dept_code\n" ;
		if(sTime!=null&&!sTime.equals(""))
		{
			sql+="and a.checkDate>=to_date('"+sTime+"', 'yyyy-MM-dd hh24:MI:ss ')";
		}
		if(eTime!=null&&!eTime.equals(""))
		{
			sql+="and a.checkDate<=to_date('"+eTime+"', 'yyyy-MM-dd hh24:MI:ss ')";
		}
		if(outSTime!=null&&!outSTime.equals(""))
		{
			sql+="and a.lastModifyDate>=to_date('"+outSTime+"', 'yyyy-MM-dd hh24:MI:ss ')";
		}
		if(outETime!=null&&!outETime.equals(""))
		{
			sql+="and a.lastModifyDate<=to_date('"+outETime+"', 'yyyy-MM-dd hh24:MI:ss ')";
		}
		if(deptCode!=null&&!deptCode.equals(""))
		{
			sql+=" and a.fee_by_dep like '"+deptCode+"'\n ";
		}
		if(costFrom!=null&&!costFrom.equals(""))
		{
			sql+="and a.zbbmtx_code like '"+costFrom+"'\n ";
		}
		sql+=" order by a.fee_by_dep, a.zbbmtx_code";
//		System.out.println("the sql"+sql);
		List list = bll.queryByNativeSQL(sql);
		List<CostFromInfo> arrayList = new ArrayList();
		Iterator it = list.iterator();
		while(it.hasNext())
		{
			CostFromInfo model=new CostFromInfo();
			Object[] o = (Object[]) it.next();
			if (o[0] != null)
				model.setDeptCode(o[0].toString());
			if (o[1] != null)
				model.setDeptName(o[1].toString());
			if (o[2] != null)
				model.setCostCode(o[2].toString());
			if (o[3] != null)
				model.setCostName(o[3].toString());
			if (o[4] != null)
				model.setCostQty(Double.parseDouble(o[4].toString()));
			if (o[5] != null)
				model.setBudgect(Double.parseDouble(o[5].toString()));
			
			arrayList.add(model);
		}
		PageObject result=new PageObject();
		result.setList(arrayList);
		return result;
		
	
		
	}
	

	/**
	 * Perform an initial save of a previously unsaved MrpJPlanRequirementHead
	 * entity. All subsequent persist actions of this entity should use the
	 * #update() method.
	 * 
	 * @param entity
	 *            MrpJPlanRequirementHead entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public MrpJPlanRequirementHead save(MrpJPlanRequirementHead entity) {
		LogUtil
				.log("saving MrpJPlanRequirementHead instance", Level.INFO,
						null);
		try {
			long maxId = bll.getMaxId("MRP_J_PLAN_REQUIREMENT_HEAD",
					"REQUIREMENT_HEAD_ID");
			entity.setLastModifiedDate(new Date());
			entity.setRequirementHeadId(maxId);
			entityManager.persist(entity);
			return this.findById(maxId);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent MrpJPlanRequirementHead entity.
	 * 
	 * @param entity
	 *            MrpJPlanRequirementHead entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(MrpJPlanRequirementHead entity) {
		LogUtil.log("deleting MrpJPlanRequirementHead instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(MrpJPlanRequirementHead.class,
					entity.getRequirementHeadId());
			entity.setLastModifiedDate(new Date());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved MrpJPlanRequirementHead entity and return it
	 * or a copy of it to the sender. A copy of the MrpJPlanRequirementHead
	 * entity parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            MrpJPlanRequirementHead entity to update
	 * @return MrpJPlanRequirementHead the persisted MrpJPlanRequirementHead
	 *         entity instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public MrpJPlanRequirementHead update(MrpJPlanRequirementHead entity) {
		LogUtil.log("updating MrpJPlanRequirementHead instance", Level.INFO,
				null);
		try {
			entity.setLastModifiedDate(new Date());
			MrpJPlanRequirementHead result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	@SuppressWarnings("unchecked")
	public MrpJPlanRequirementHead findByIdWithName(Long id) {
		LogUtil.log("finding MrpJPlanRequirementHead instance with id: " + id,
				Level.INFO, null);
		try {
			String sql = "select a.  REQUIREMENT_HEAD_ID     ,\n"
					+ "  a.  MR_NO                   ,\n"
					+ "  a.  MR_DATE                 ,\n"
					+ "  a.  WO_NO                   ,\n"
					+ "  a.  PLAN_ORIGINAL_ID        ,\n"
					+ "  a.  ITEM_CODE                     ,\n"// modify by
					// ywliu
					// 2009/7/6
					+ "  a.  MR_BY                       ,\n"
					+ "  GETDEPTNAME(a.MR_DEPT)  as MR_DEPT     ,\n"
					+ "  a.  DUE_DATE                ,\n"
					+ "  a.  PLAN_GRADE              ,\n"
					+ "  GETDEPTNAME(a.COST_DEPT) as COST_DEPT  ,\n"
					+ "  a.  COST_SPECIAL            ,\n"
					+ "  a.  MR_REASON               ,\n"
					+ "  a.  MR_TYPE                 ,\n"
					+ "  a.  WF_NO                       ,\n"
					+ "  a.  MR_STATUS               ,\n"
					+ "  a.  ENTERPRISE_CODE         ,\n"
					+ "  a.  IS_USE                      ,\n"
					+ "  a.  LAST_MODIFIED_BY        ,\n"
					+ "  to_char(a.LAST_MODIFIED_DATE,'yyyy-MM-dd HH24:MI:SS') as LAST_MODIFIED_DATE,  \n"
					+ "   a.PLAN_DATE_MEMO, \n"
					+"    a.prj_no \n"
					+ "  from MRP_J_PLAN_REQUIREMENT_HEAD a \n"
					+ "  where a.REQUIREMENT_HEAD_ID='" + id
					+ "' and (a.MR_STATUS= '0' or a.MR_STATUS= '9')";
			
			List<MrpJPlanRequirementHead> arrlist = bll.queryByNativeSQL(sql,
					MrpJPlanRequirementHead.class);
			MrpJPlanRequirementHead instance = new MrpJPlanRequirementHead();
			if (arrlist.size() > 0) {
				instance = arrlist.get(0);
				String tempString = instance.getMrBy();
				Employee employee = bllEmp.getEmployeeInfo(tempString);
				String temp2String = employee.getWorkerName();
				instance.setMrBy(temp2String);
			}
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	@SuppressWarnings("unchecked")
	public MrpJPlanRequirementHead findMaterialById(Long id) {
		LogUtil.log("finding MrpJPlanRequirementHead instance with id: " + id,
				Level.INFO, null);
		try {
			String sql = "select a.  REQUIREMENT_HEAD_ID     ,\n"
					+ "  a.  MR_NO                   ,\n"
					+ "  a.  MR_DATE                 ,\n"
					+ "  a.  WO_NO                   ,\n"
					+ "  a.  PLAN_ORIGINAL_ID        ,\n"
					+ "  a.  ITEM_CODE                     ,\n"// modify by
					// ywliu
					// 2009/7/6
					+ "  a.  MR_BY                       ,\n"
					+ "  GETDEPTNAME(a.MR_DEPT)  as MR_DEPT     ,\n"
					+ "  a.  DUE_DATE                ,\n"
					+ "  a.  PLAN_GRADE              ,\n"
					+ "  GETDEPTNAME(a.COST_DEPT) as COST_DEPT  ,\n"
					+ "  a.  COST_SPECIAL            ,\n"
					+ "  a.  MR_REASON               ,\n"
					+ "  a.  MR_TYPE                 ,\n"
					+ "  a.  WF_NO                       ,\n"
					+ "  a.  MR_STATUS               ,\n"
					+ "  a.  ENTERPRISE_CODE         ,\n"
					+ "  a.  IS_USE                      ,\n"
					+ "  a.  LAST_MODIFIED_BY        ,\n"
					+ "  to_char(a.LAST_MODIFIED_DATE,'yyyy-MM-dd HH24:MI:SS') as LAST_MODIFIED_DATE,  \n"
					+ "   a.PLAN_DATE_MEMO \n"
					+ "  from MRP_J_PLAN_REQUIREMENT_HEAD a \n"
					+ "  where a.REQUIREMENT_HEAD_ID='" + id+ "'";
//			System.out.println("the sql"+sql);
			List<MrpJPlanRequirementHead> arrlist = bll.queryByNativeSQL(sql,
					MrpJPlanRequirementHead.class);
			MrpJPlanRequirementHead instance = new MrpJPlanRequirementHead();
			if (arrlist.size() > 0) {
				instance = arrlist.get(0);
				String tempString = instance.getMrBy();
				Employee employee = bllEmp.getEmployeeInfo(tempString);
				String temp2String = employee.getWorkerName();
				instance.setMrBy(temp2String);
			}
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	public int inDeptCase(String entryIds, String deptCode, String tableName,
			String columnNameWf, String columnNameDp) {
		int i = 0;
		String sql = "";
		if ("mrp_j_plan_requirement_head".equals(tableName)
				|| "inv_j_issue_head".equals(tableName)) {
			String status = "mrp_j_plan_requirement_head".equals(tableName) ? "MR_STATUS"
					: "ISSUE_STATUS";
			sql = "SELECT (SELECT COUNT(1)\n" + "          FROM " + tableName
					+ " t\n" + "         WHERE t." + columnNameWf + " IN ("
					+ entryIds + ")\n" + "           AND t." + status
					+ " <> '1') +\n" + "       (SELECT COUNT(1)\n"
					+ "          FROM " + tableName + " t\n"
					+ "         WHERE t." + columnNameWf + " IN (" + entryIds
					+ ")\n" + "           AND t." + columnNameDp + " = '"
					+ deptCode + "'\n" + "           AND t." + status
					+ " = '1')\n" + "  FROM dual";
		} else
			sql = "SELECT COUNT(1)\n" + "  FROM " + tableName + " t\n"
					+ " WHERE t." + columnNameWf + " IN (" + entryIds + ")\n"
					+ "   AND t." + columnNameDp + " = '" + deptCode + "'";
		i = Integer.parseInt(bll.getSingal(sql).toString());
		return i;
	}

	public MrpJPlanRequirementHead findById(Long id) {
		LogUtil.log("finding MrpJPlanRequirementHead instance with id: " + id,
				Level.INFO, null);
		try {
			MrpJPlanRequirementHead instance = entityManager.find(
					MrpJPlanRequirementHead.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all MrpJPlanRequirementHead entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the MrpJPlanRequirementHead property to query
	 * @param value
	 *            the property value to match
	 * @return List<MrpJPlanRequirementHead> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<MrpJPlanRequirementHead> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding MrpJPlanRequirementHead instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from MrpJPlanRequirementHead model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<MrpJPlanRequirementHead> findByMrNo(Object mrNo) {
		return findByProperty(MR_NO, mrNo);
	}

	public List<MrpJPlanRequirementHead> findByWoNo(Object woNo) {
		return findByProperty(WO_NO, woNo);
	}

	public List<MrpJPlanRequirementHead> findByPlanOriginalId(
			Object planOriginalId) {
		return findByProperty(PLAN_ORIGINAL_ID, planOriginalId);
	}

	public List<MrpJPlanRequirementHead> findByItemId(Object itemId) {
		return findByProperty(ITEM_ID, itemId);
	}

	public List<MrpJPlanRequirementHead> findByMrBy(Object mrBy) {
		return findByProperty(MR_BY, mrBy);
	}

	public List<MrpJPlanRequirementHead> findByMrDept(Object mrDept) {
		return findByProperty(MR_DEPT, mrDept);
	}

	public List<MrpJPlanRequirementHead> findByPlanGrade(Object planGrade) {
		return findByProperty(PLAN_GRADE, planGrade);
	}

	public List<MrpJPlanRequirementHead> findByCostDept(Object costDept) {
		return findByProperty(COST_DEPT, costDept);
	}

	public List<MrpJPlanRequirementHead> findByCostSpecial(Object costSpecial) {
		return findByProperty(COST_SPECIAL, costSpecial);
	}

	public List<MrpJPlanRequirementHead> findByMrReason(Object mrReason) {
		return findByProperty(MR_REASON, mrReason);
	}

	public List<MrpJPlanRequirementHead> findByMrType(Object mrType) {
		return findByProperty(MR_TYPE, mrType);
	}

	public List<MrpJPlanRequirementHead> findByWfNo(Object wfNo) {
		return findByProperty(WF_NO, wfNo);
	}

	public List<MrpJPlanRequirementHead> findByWfState(Object wfState) {
		return findByProperty(WF_STATE, wfState);
	}

	public List<MrpJPlanRequirementHead> findByEnterpriseCode(
			Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	public List<MrpJPlanRequirementHead> findByIsUse(Object isUse) {
		return findByProperty(IS_USE, isUse);
	}

	/**
	 * Find all MrpJPlanRequirementHead entities.
	 * 
	 * @return List<MrpJPlanRequirementHead> all MrpJPlanRequirementHead
	 *         entities
	 */
	@SuppressWarnings("unchecked")
	public List<MrpJPlanRequirementHead> findAll() {
		LogUtil.log("finding all MrpJPlanRequirementHead instances",
				Level.INFO, null);
		try {
			final String queryString = "select model from MrpJPlanRequirementHead model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	/**
	 * add by ywliu 20091110
	 */
	@SuppressWarnings("unchecked")
	public PageObject findPlanHeadByLogin(String enterpriseCode,
			String startDate, String endDate, String mrBy, String maName,
			String maClassName, String status, String planOriginalID,
			String queryType, String dept, String observer, String mrPlanNo,
			int... rowStartIdxAndCount) {
		PageObject result = new PageObject();

		String sql = "select distinct a.  REQUIREMENT_HEAD_ID     ,\n"
				+ "  a.  MR_NO                   ,\n"
				+ "  a.  MR_DATE                 ,\n"
				+ "  a.  WO_NO                   ,\n"
				+ "  a.  PLAN_ORIGINAL_ID        ,\n"
				+ "  a.  ITEM_CODE                     ,\n"
				+ "  a.  MR_BY                       ,\n"
				+ "  GETDEPTNAME(a.MR_DEPT)||'/'||a.MR_DEPT  as MR_DEPT               ,\n"
				+ "  a.  DUE_DATE                ,\n"
				+ "  a.  PLAN_GRADE              ,\n"
				+ "  a.  COST_DEPT               ,\n"
				+ "  a.  COST_SPECIAL            ,\n"
				+ "  a.  MR_REASON               ,\n"
				+ "  a.  MR_TYPE                 ,\n"
				+ "  a.  WF_NO                       ,\n"
				+ "  a.  MR_STATUS               ,\n"
				+ "  a.  ENTERPRISE_CODE         ,\n"
				+ "  a.  IS_USE                      ,\n"
				+ "  a.  LAST_MODIFIED_BY        ,\n"
				+ "  a.  LAST_MODIFIED_DATE ,     \n"
				+ "   a.PLAN_DATE_MEMO \n"
				+"   ,a.prj_no \n";

		String strFromStr = "  from MRP_J_PLAN_REQUIREMENT_HEAD a \n";
		String strWhere = " where   a.enterprise_code = '" + enterpriseCode
				+ "'\n" + "  and a.is_use = 'Y'\n";

		if (startDate != null && !"".equals(startDate)) {
			strWhere += "  and to_char(a.MR_DATE,'yyyy-MM-dd') >='" + startDate
					+ "'\n";
		}
		if (endDate != null && !endDate.equals("")) {
			strWhere += "  and to_char(a.MR_DATE,'yyyy-MM-dd') <='" + endDate
					+ "'\n";
		}
		if (maName != null && !"".equals(maName)) {
			strFromStr += " , MRP_J_PLAN_REQUIREMENT_DETAIL b,\n"
					+ " INV_C_MATERIAL c \n";
			strWhere += " " 
					+"   and c.MATERIAL_NAME like '%" + maName + "%'\n"
					+ " and b.MATERIAL_ID = c.MATERIAL_ID"
					+ " and a.REQUIREMENT_HEAD_ID = b.REQUIREMENT_HEAD_ID"
					+ " and b.is_use = 'Y'\n" + " and c.is_use = 'Y'\n"
					+ " and b.enterprise_code = '" + enterpriseCode + "'\n"
					+ " and c.enterprise_code = '" + enterpriseCode + "'\n";
		}
		if (maClassName != null && !"".equals(maClassName)) {
			if (maName != null && !"".equals(maName)) {
				strFromStr += " ,INV_C_MATERIAL_CLASS d\n";
				strWhere += " and d.class_no like '" + maClassName + "%'\n"
						+ " and d.is_use = 'Y'\n"
						+ " and d.enterprise_code = '" + enterpriseCode + "'\n";
			} else {
				strFromStr += " , MRP_J_PLAN_REQUIREMENT_DETAIL b,\n"
						+ " INV_C_MATERIAL c,\n" + " INV_C_MATERIAL_CLASS d\n";
				strWhere += "and d.class_no like '" + maClassName + "%'\n"
						+ " and c.MAERTIAL_CLASS_ID = d.MAERTIAL_CLASS_ID \n"
						+ " and b.MATERIAL_ID = c.MATERIAL_ID"
						+ " and a.REQUIREMENT_HEAD_ID = b.REQUIREMENT_HEAD_ID"
						+ " and b.is_use = 'Y'\n" + " and c.is_use = 'Y'\n"
						+ " and d.is_use = 'Y'\n"
						+ " and b.enterprise_code = '" + enterpriseCode + "'\n"
						+ " and c.enterprise_code = '" + enterpriseCode + "'\n"
						+ " and d.enterprise_code = '" + enterpriseCode + "'\n";
			}
		}
		if (status != null && !status.equals("")
				&& !"ALL".equalsIgnoreCase(status)) {
			strWhere += "  and a.mr_status='" + status + "'  \n";
		} else if ("ALL".equalsIgnoreCase(status)) {
			strWhere += "  and a.mr_status in ('0','9')  \n";
		}

		if (planOriginalID != null && !"".equals(planOriginalID)
				&& !"null".equals(planOriginalID)) {
			strWhere += "  and a.plan_original_id='" + planOriginalID + "'  \n";
		}
		if ("1".equals(queryType)) {// add by ywliu 20091113 我上报的
			strWhere += "and a.mr_by ='" + observer + "' \n";
			// modified by liuyi 20100317 我的时 取上报的和未上报的
			// + " and a.MR_STATUS not in ('0')";
			// " and a.wf_no in(select distinct a.entry_id from wf_c_entry a,
			// wf_j_historyoperation b \n " +
			// " where a.entry_id = b.entry_id \n" +
			// " and (a.flow_type = 'hfResourcePlanXZ-v1.0' or a.flow_type =
			// 'hfResourcePlanGDZC-v1.0' or a.flow_type =
			// 'hfResourcePlanSC-v1.0' or a.flow_type =
			// 'hfResourcePlanJSJ-v1.0')\n" +
			// " and b.caller = '"+observer+"') " +

		} else if ("2".equals(queryType)) {// add by ywliu 20091113 我审批的
			strWhere += " and a.wf_no in(select distinct a.entry_id from wf_c_entry a, wf_j_historyoperation b \n "
					+ " where a.entry_id = b.entry_id \n"
					+ " and (a.flow_type = 'hfResourcePlanXZ-v1.0' or a.flow_type = 'hfResourcePlanGDZC-v1.0' or a.flow_type = 'hfResourcePlanSC-v1.0' or a.flow_type = 'hfResourcePlanJSJ-v1.0')\n"
					+ " and b.caller = '"
					+ observer
					+ "') "
					+ " and a.MR_STATUS not in ('0','1')";
		} else if ("4".equals(queryType)) {// add by ywliu 20091113 本部门的
			strWhere += " and a.MR_DEPT = (select b.dept_code from hr_j_emp_info a,hr_c_dept b  where a.dept_id = b.dept_id and  a.emp_code = '"
					+ observer + "')\n";
		}
		if (mrBy != null && !mrBy.equals(""))// add by ywliu 20091113
			strWhere += "and a.mr_by ='" + mrBy + "' \n";
		if (dept != null && !"".equals(dept)) {// add by ywliu 20091113
			strWhere += " and a.MR_DEPT='" + dept + "'\n";
		}
		if (mrPlanNo != null && !mrPlanNo.equals("")) {
			strWhere += "  and   a.MR_NO like '%" + mrPlanNo.trim() + "%'  ";
		}
		strWhere += " order by a.REQUIREMENT_HEAD_ID desc";
		sql += strFromStr;
		sql += strWhere;
		String sqlCount = "select count(a.REQUIREMENT_HEAD_ID)\n";
		sqlCount += strFromStr;
		sqlCount += strWhere;
		List<MrpJPlanRequirementHead> arrlist = bll.queryByNativeSQL(sql,
				MrpJPlanRequirementHead.class, rowStartIdxAndCount);
		
		if (arrlist.size() > 0) {
			for (MrpJPlanRequirementHead temp : arrlist) {
                 
				if (temp.getMrBy() != null) {
					if (bllEmp.getEmployeeInfo(temp.getMrBy()) != null) {
						temp.setMrBy(bllEmp.getEmployeeInfo(temp.getMrBy())
								.getWorkerName());
					}
				}

			}
			result.setList(arrlist);
			result.setTotalCount(Long.parseLong(bll.getSingal(sqlCount)
					.toString()));
		}
		return result;

	}

	/**
	 * modify by fyyang 090807 增加了flag条件
	 * 不传值时表示查询未作废的（is_use='Y'），传值1时表示查询已作废和未作废的(is_use=Y or C)
	 * 
	 * @param enterpriseCode
	 * @param headId
	 * @param flag
	 * @return
	 */
	@SuppressWarnings( { "unchecked", "unchecked" })
	public PageObject getMaterialDetail(String enterpriseCode, Long headId,
			String flag) {
		PageObject result = new PageObject();
		// add by fyyang------090807------
		String isUse = "";
		if (flag != null && flag.equals("1")) {
			isUse = "'Y','C'";
		} else {
			isUse = "'Y'";

		}
		// -------------------------------
		String sql = "  SELECT          \n"
				+ "      a.REQUIREMENT_DETAIL_ID as requirementDetailId,            \n"
				+ "      b.MATERIAL_NO as materialNo,            \n"
				+ "      b.MATERIAL_NAME as materialName,            \n"
				+ "      b.SPEC_NO as materSize,         \n"
				+ "      a.APPLIED_QTY as appliedQty,            \n"
				+ "      a.APPROVED_QTY as apprpvedQty,          \n"
				+ "      a.ISS_QTY as issQty,            \n"
				+ "      a.ESTIMATED_PRICE as estimatedPrice,            \n"
				+ "      (a.APPLIED_QTY * a.ESTIMATED_PRICE ) as estimatedSum,           \n"
				+ "      tempSum.purQty as purQty,         \n"
				+ "      b.STOCK_UM_ID as stockUmName,           \n"
				+ "      a.USAGE as usage,           \n"
				+ "      a.memo as memo,         \n"
				+ "      to_char(a.DUE_DATE,'YYYY-mm-dd') as needDate,         \n"
				+ "      b.PARAMETER as parameter,           \n"
				+ "      b.DOC_NO as docNo,          \n"
				+ "      c.WHS_NAME as whsName,          \n"
				+ "      b.QUALITY_CLASS as qualityClass,            \n"
				+ "      temp.stock as left,         \n"
				+ "      tempSum.tempNum  as tempNum,      \n"
				+ "      a.ITEM_CODE as itemId,          \n"
				+ "       a.cancel_reason,a.is_use \n" 
				+ "      ,a.supplier,b.factory \n" 
				+ ",a.modify_memo \n" 
				+ "  FROM            \n"
				+ "      MRP_J_PLAN_REQUIREMENT_DETAIL  a,           \n"
				+ "      INV_C_MATERIAL b,           \n"
				+ "      INV_C_WAREHOUSE c,          \n"
				+ "      (SELECT         \n"
				+ "          MATERIAL_ID as id,          \n"
				+ "          sum(OPEN_BALANCE + RECEIPT + ADJUST - ISSUE) as stock           \n"
				+ "      FROM            \n"
				+ "          INV_J_WAREHOUSE,            \n"
				+ "          INV_C_WAREHOUSE         \n"
				+ "      WHERE           \n"
				+ "          INV_J_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "          INV_J_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND \n"
				+ "          INV_J_WAREHOUSE.WHS_NO = INV_C_WAREHOUSE.WHS_NO         AND \n"
				+ "          INV_C_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND \n"
				+ "          INV_C_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "          INV_C_WAREHOUSE.IS_INSPECT = 'N'            \n"
				+ "      GROUP BY            \n"
				+ "          MATERIAL_ID         \n"
				+ "      ) temp,         \n"
				+ "      (SELECT         \n"
				+ "          d.REQUIREMENT_DETAIL_ID as id,          \n"
				+ "          sum(d.MR_QTY) as purQty,            \n"
				+ "          sum(e.INS_QTY) as tempNum           \n"
				+ "      FROM            \n"
				+ "          PUR_J_PLAN_ORDER d,         \n"
				+ "          PUR_J_ORDER_DETAILS e,          \n"
				+ "          (SELECT         \n"
				+ "              a.REQUIREMENT_DETAIL_ID         \n"
				+ "          FROM            \n"
				+ "              MRP_J_PLAN_REQUIREMENT_DETAIL  a,           \n"
				+ "              INV_C_MATERIAL b,           \n"
				+ "              INV_C_WAREHOUSE c,          \n"
				+ "              (SELECT         \n"
				+ "                  MATERIAL_ID as id,          \n"
				+ "                  sum(OPEN_BALANCE + RECEIPT + ADJUST - ISSUE) as stock           \n"
				+ "              FROM            \n"
				+ "                  INV_J_WAREHOUSE,            \n"
				+ "                  INV_C_WAREHOUSE         \n"
				+ "              WHERE           \n"
				+ "                  INV_J_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "                  INV_J_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND \n"
				+ "                  INV_J_WAREHOUSE.WHS_NO = INV_C_WAREHOUSE.WHS_NO         AND \n"
				+ "                  INV_C_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND \n"
				+ "                  INV_C_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "                  INV_C_WAREHOUSE.IS_INSPECT = 'N'            \n"
				+ "              GROUP BY            \n"
				+ "                  MATERIAL_ID         \n"
				+ "              ) temp          \n"
				+ "          WHERE           \n"
				+ "              a.REQUIREMENT_HEAD_ID= '"
				+ headId
				+ "' AND          \n"
				+ "              a.MATERIAL_ID=b.MATERIAL_ID AND         \n"
				+ "              a.MATERIAL_ID=temp.id(+) AND           \n"
				+ "              b.DEFAULT_WHS_NO=c.WHS_NO(+) AND           \n"
				+ "              a.IS_USE in ("
				+ isUse
				+ ") AND            \n"
				+ "              b.IS_USE='Y' AND            \n"
				+ "              c.IS_USE(+)='Y' AND            \n"
				+ "              a.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "' AND            \n"
				+ "              b.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'  AND           \n"
				+ "              c.ENTERPRISE_CODE(+) = '"
				+ enterpriseCode
				+ "'            \n"
				+ "          ) g         \n"
				+ "      WHERE           \n"
				+ "          d.IS_USE='Y' AND            \n"
				+ "          e.IS_USE='Y' AND            \n"
				+ "          d.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'   AND          \n"
				+ "          e.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "' AND            \n"
				+ "          d.REQUIREMENT_DETAIL_ID=g.REQUIREMENT_DETAIL_ID AND         \n"
				+ "          d.PUR_ORDER_DETAILS_ID=e.PUR_ORDER_DETAILS_ID           \n"
				+ "      GROUP BY            \n"
				+ "          d.REQUIREMENT_DETAIL_ID         \n"
				+ "      ) tempSum           \n"
				+ "  WHERE           \n"
				+ "      a.REQUIREMENT_HEAD_ID= '"
				+ headId
				+ "' AND          \n"
				+ "      tempSum.id(+)=a.REQUIREMENT_DETAIL_ID AND          \n"
				+ "      a.MATERIAL_ID=b.MATERIAL_ID AND         \n"
				+ "      a.MATERIAL_ID=temp.id(+) AND           \n"
				+ "      b.DEFAULT_WHS_NO=c.WHS_NO(+) AND           \n"
				+ "     a.IS_USE in ("
				+ isUse
				+ ")  AND            \n"
				+ "      b.IS_USE='Y' AND            \n"
				+ "      c.IS_USE(+)='Y' AND            \n"
				+ "      a.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'    AND     \n"
				+ "      b.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'    AND     \n"
				+ "      c.ENTERPRISE_CODE(+) = '"
				+ enterpriseCode
				+ "'            \n"
				+ "  ORDER BY            \n"
				+ "      a.REQUIREMENT_DETAIL_ID         \n";
		List<MrpJPlanRequirementDetailInfo> list = bll.queryByNativeSQL(sql);
		List<MrpJPlanRequirementDetailInfo> arrlist = new ArrayList<MrpJPlanRequirementDetailInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			MrpJPlanRequirementDetailInfo model = new MrpJPlanRequirementDetailInfo();
			Object[] data = (Object[]) it.next();
			model.setRequirementDetailId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setMaterialNo(data[1].toString());
			if (data[2] != null)
				model.setMaterialName(data[2].toString());
			if (data[3] != null)
				model.setMaterSize(data[3].toString());
			if (data[4] != null)
				model.setAppliedQty(Double.parseDouble(data[4].toString()));
			if (data[5] != null)
				model.setApprovedQty(Double.parseDouble(data[5].toString()));
			if (data[6] != null)
				model.setIssQty(Double.parseDouble(data[6].toString()));
			if (data[7] != null)
				model.setEstimatedPrice(Double.parseDouble(data[7].toString()));
			if (data[8] != null)
				model.setEstimatedSum(Double.parseDouble(data[8].toString()));
			if (data[9] != null)
				model.setPurQty(Double.parseDouble(data[9].toString()));
			if (data[10] != null) {
				BpCMeasureUnitFacadeRemote cc = (BpCMeasureUnitFacadeRemote) Ejb3Factory
						.getInstance().getFacadeRemote("BpCMeasureUnitFacade");
				BpCMeasureUnit bp = cc.findById(Long.parseLong(data[10]
						.toString()));
				if(bp != null)
				model.setStockUmName(bp.getUnitName());
			}
			if (data[11] != null)
				model.setUsage(data[11].toString());
			if (data[12] != null)
				model.setMemo(data[12].toString());
			if (data[13] != null)
				model.setNeedDate(data[13].toString());
			if (data[14] != null)
				model.setParameter(data[14].toString());
			if (data[15] != null)
				model.setDocNo(data[15].toString());
			if (data[16] != null)
				model.setWhsName(data[16].toString());
			if (data[17] != null)
				model.setQualityClass(data[17].toString());
			if (data[18] != null)
				model.setLeft(Double.parseDouble(data[18].toString()));
			if (data[19] != null)
				model.setTempNum(Double.parseDouble(data[19].toString()));
			if (data[20] != null)
				model.setItemId(data[20].toString());// modify by ywliu
			// 2009/7/6
			// add by fyyang 090807
			if (data[21] != null)
				model.setCancelReason(data[21].toString());
			if (data[22] != null)
				model.setUseFlag(data[22].toString());
			// add by fyyang 091214
			if (data[23] != null)
				model.setSupplier(data[23].toString());
			if (data[24] != null)
				model.setFactory(data[24].toString());
			if (data[25] != null)
				model.setModifyMemo(data[25].toString());
			arrlist.add(model);
		}
		if (arrlist.size() > 0) {
			result.setList(arrlist);
		}
		return result;

	}
//add by wpzhu 10-04-08
	@SuppressWarnings("unchecked")
	public PageObject getMaterial(String enterpriseCode, Long headId) {
		PageObject result = new PageObject();
		String sql = "   SELECT  \n"
				+ "      a.REQUIREMENT_DETAIL_ID as requirementDetailId, \n"
				+ "      b.MATERIAL_ID as materialId,    \n"
				+ "      b.MATERIAL_NAME as materialName,    \n"
				+ "      b.SPEC_NO as materSize, \n"
				+ "     b.PARAMETER as parameter,   \n"
				+ "     b.STOCK_UM_ID as stockUmName,   \n"
				+ "      a.APPLIED_QTY as appliedQty,    \n"
				+ "      a.ESTIMATED_PRICE as estimatedPrice,    \n"
				+ "     (a.APPLIED_QTY * a.ESTIMATED_PRICE ) as estimatedSum,   \n"
				+ "      temp.stock as left, \n"
				+ "       b.MAX_STOCK as maxStock,    \n"
				+ "       a.USAGE as usage,   \n"
				+ "       b.FACTORY as factory,   \n"
				+ "      to_char(a.DUE_DATE,'YYYY-mm-dd') as needDate,    \n"
				+ "      a.SUPPLIER as supplier ,    \n"
				+ "       a.memo as memo , \n"
				+ "      c.ATTRIBUTE_CODE as equCode, \n"
				+ "       a.ITEM_CODE as itemId, \n"
				+ "       a.PLAN_ORIGINAL_ID, \n"
				+ "      to_char( a.LAST_MODIFIED_DATE,'YYYY-MM-DD hh24:mi:ss'), \n"
				+ "b.material_no ,a.iss_qty, t.rcvqty  as tempNum\n"
				+ "  FROM    \n"
				+ "      MRP_J_PLAN_REQUIREMENT_DETAIL  a,   \n"
				+ "      INV_C_MATERIAL b, " +
				"(\n" +
				"   select cc.requirement_detail_id,nvl(bb.ins_qty,0)+nvl(bb.rcv_qty,0) rcvqty\n" + 
				"from pur_j_plan_order aa,pur_j_order_details bb,mrp_j_plan_requirement_detail cc\n" + 
				"where aa.is_use='Y'\n" + 
				"and aa.enterprise_code='"+enterpriseCode+"'\n" + 
				"and aa.requirement_detail_id=cc.requirement_detail_id\n" + 
				"and bb.pur_no=aa.pur_no\n" + 
				"and bb.material_id=cc.material_id\n" + 
				"      )t,"

				+ "      (SELECT \n"
				+ "          MATERIAL_ID as id,  \n"
				+ "          sum(OPEN_BALANCE + RECEIPT + ADJUST - ISSUE) as stock   \n"
				+ "      FROM    \n" + "          INV_J_WAREHOUSE,    \n"
				+ "          INV_C_WAREHOUSE \n" + "      WHERE   \n"
				+ "          INV_J_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "          INV_J_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND   \n"
				+ "          INV_J_WAREHOUSE.WHS_NO = INV_C_WAREHOUSE.WHS_NO         AND \n"
				+ "          INV_C_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND   \n"
				+ "          INV_C_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "          INV_C_WAREHOUSE.IS_INSPECT = 'N'    \n"
				+ "      GROUP BY    \n"
				+ "          MATERIAL_ID \n"
				+ "      ) temp,  \n"
				+ "      EQU_J_SPAREPART c"
				+ "  WHERE  a.requirement_detail_id=t.requirement_detail_id(+)" 
				+ "   and   a.REQUIREMENT_HEAD_ID= '"
				+ headId
				+ "' AND  \n"
				+ "      a.MATERIAL_ID=b.MATERIAL_ID AND \n"
				+ "      a.MATERIAL_ID=temp.id(+) AND   \n"
				+ "      a.IS_USE='Y' AND    \n"
				+ "      b.IS_USE='Y' AND    \n"
				+ "      c.IS_USE='Y' AND   \n"
				+ "      a.EQU_SPAREPART_ID = c.EQU_SPAREPART_ID  and \n"
				+ "      c.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'    AND   \n"
				+ "      a.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'    AND   \n"
				+ "      b.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'\nORDER BY a.REQUIREMENT_DETAIL_ID ";
//		System.out.println("the sql"+sql);
		List<MrpJPlanRequirementDetailEdit> list = bll.queryByNativeSQL(sql);
		List<MrpJPlanRequirementDetailEdit> arrlist = new ArrayList<MrpJPlanRequirementDetailEdit>();
		Iterator it = list.iterator();
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		while (it.hasNext()) {
			MrpJPlanRequirementDetailEdit model = new MrpJPlanRequirementDetailEdit();
			Object[] data = (Object[]) it.next();
			model.setRequirementDetailId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setMaterialId(Long.parseLong(data[1].toString()));
			if (data[2] != null)
				model.setMaterialName(data[2].toString());
			if (data[3] != null)
				model.setMaterSize(data[3].toString());
			if (data[4] != null)
				model.setParameter(data[4].toString());
			if (data[5] != null) {
				BpCMeasureUnitFacadeRemote cc = (BpCMeasureUnitFacadeRemote) Ejb3Factory
						.getInstance().getFacadeRemote("BpCMeasureUnitFacade");
				BpCMeasureUnit bp = cc.findById(Long.parseLong(data[5]
						.toString()));
				model.setStockUmName(bp.getUnitName());
			}
			if (data[6] != null)
				model.setAppliedQty(Double.parseDouble(data[6].toString()));
			if (data[7] != null)
				model.setEstimatedPrice(Double.parseDouble(data[7].toString()));
			if (data[8] != null)
				model.setEstimatedSum(Double.parseDouble("0"));
			if (data[9] != null)
				model.setLeft(Double.parseDouble(data[9].toString()));
			if (data[10] != null)
				model.setMaxStock(Double.parseDouble(data[10].toString()));
			if (data[11] != null)
				model.setUsage(data[11].toString());
			if (data[12] != null)
				model.setFactory(data[12].toString());
			if (data[13] != null)
				model.setNeedDate(data[13].toString());
			if (data[14] != null)
				model.setSupplier(data[14].toString());
			if (data[15] != null)
				model.setMemo(data[15].toString());
			if (data[16] != null)
				model.setEquCode(data[16].toString());
			if (data[17] != null)
				model.setItemId(data[17].toString());
			if (data[18] != null)
				model.setPlanOriginalId(Long.parseLong(data[18].toString()));
			if (data[19] != null)
				try {
					model.setLastModifiedDate(dateformat.parse(data[19]
							.toString()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (data[20] != null)
				model.setMaterialCode(data[20].toString());
			if (data[21] != null)
				model.setIssQty(Double.parseDouble(data[21].toString()));
			if (data[22] != null)
				model.setInsQty(Double.parseDouble(data[22].toString()));
			arrlist.add(model);
		}
		
		if (arrlist.size() > 0) {
			result.setList(arrlist);
		}
		return result;

	}

	@SuppressWarnings("unchecked")
	public PageObject getMaterialEdit(String enterpriseCode, Long headId) {
		PageObject result = new PageObject();
		String sql = "   SELECT  \n"
				+ "      a.REQUIREMENT_DETAIL_ID as requirementDetailId, \n"
				+ "      b.MATERIAL_ID as materialId,    \n"
				+ "      b.MATERIAL_NAME as materialName,    \n"
				+ "      b.SPEC_NO as materSize, \n"
				+ "      b.PARAMETER as parameter,   \n"
				+ "      b.STOCK_UM_ID as stockUmName,   \n"
				+ "      a.APPLIED_QTY as appliedQty,    \n"
				+ "      a.ESTIMATED_PRICE as estimatedPrice,    \n"
				+ "      (a.APPLIED_QTY * a.ESTIMATED_PRICE ) as estimatedSum,   \n"
				+ "      temp.stock as left, \n"
				+ "      b.MAX_STOCK as maxStock,    \n"
				+ "      a.USAGE as usage,   \n"
				+ "      b.FACTORY as factory,   \n"
				+ "      to_char(a.DUE_DATE,'YYYY-mm-dd') as needDate,    \n"
				+ "      a.SUPPLIER as supplier ,    \n"
				+ "      a.memo as memo , \n"
				+ "      c.ATTRIBUTE_CODE as equCode, \n"
				+ "      a.ITEM_CODE as itemId, \n"// modify by ywliu 2009/7/6
				+ "      a.PLAN_ORIGINAL_ID, \n"
				+ "      to_char(a.LAST_MODIFIED_DATE,'YYYY-MM-DD hh24:mi:ss') \n"
				// add by liuyi 091026 liuyi 增加物料编码
				+ ",b.material_no \n"
				// add by ltong 20100505 物料分类
				+", (select t.class_no from INV_C_MATERIAL_CLASS t where b.maertial_class_id=t.maertial_class_id) class_no \n"
				+ "  FROM    \n"
				+ "      MRP_J_PLAN_REQUIREMENT_DETAIL  a,   \n"
				+ "      INV_C_MATERIAL b,   \n"
				+ "      (SELECT \n"
				+ "          MATERIAL_ID as id,  \n"
				+ "          sum(OPEN_BALANCE + RECEIPT + ADJUST - ISSUE) as stock   \n"
				+ "      FROM    \n" + "          INV_J_WAREHOUSE,    \n"
				+ "          INV_C_WAREHOUSE \n" + "      WHERE   \n"
				+ "          INV_J_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "          INV_J_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND   \n"
				+ "          INV_J_WAREHOUSE.WHS_NO = INV_C_WAREHOUSE.WHS_NO         AND \n"
				+ "          INV_C_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND   \n"
				+ "          INV_C_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "          INV_C_WAREHOUSE.IS_INSPECT = 'N'    \n"
				+ "      GROUP BY    \n"
				+ "          MATERIAL_ID \n"
				+ "      ) temp,  \n"
				+ "      EQU_J_SPAREPART c"
				+ "  WHERE   \n"
				+ "      a.REQUIREMENT_HEAD_ID= '"
				+ headId
				+ "' AND  \n"
				+ "      a.MATERIAL_ID=b.MATERIAL_ID AND \n"
				+ "      a.MATERIAL_ID=temp.id(+) AND   \n"
				+ "      a.IS_USE='Y' AND    \n"
				+ "      b.IS_USE='Y' AND    \n"
				+ "      c.IS_USE='Y' AND    \n"
				+ "      a.EQU_SPAREPART_ID = c.EQU_SPAREPART_ID  and \n"
				+ "      c.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'    AND   \n"
				+ "      a.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'    AND   \n"
				+ "      b.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'   \n   "
				+ "ORDER BY a.REQUIREMENT_DETAIL_ID ";
//		System.out.println("the sql"+sql);
		List<MrpJPlanRequirementDetailEdit> list = bll.queryByNativeSQL(sql);
		List<MrpJPlanRequirementDetailEdit> arrlist = new ArrayList<MrpJPlanRequirementDetailEdit>();
		Iterator it = list.iterator();
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		while (it.hasNext()) {
			MrpJPlanRequirementDetailEdit model = new MrpJPlanRequirementDetailEdit();
			Object[] data = (Object[]) it.next();
			model.setRequirementDetailId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setMaterialId(Long.parseLong(data[1].toString()));
			if (data[2] != null)
				model.setMaterialName(data[2].toString());
			if (data[3] != null)
				model.setMaterSize(data[3].toString());
			if (data[4] != null)
				model.setParameter(data[4].toString());
			if (data[5] != null) {
				BpCMeasureUnitFacadeRemote cc = (BpCMeasureUnitFacadeRemote) Ejb3Factory
						.getInstance().getFacadeRemote("BpCMeasureUnitFacade");
				BpCMeasureUnit bp = cc.findById(Long.parseLong(data[5]
						.toString()));
				if(bp != null)
				model.setStockUmName(bp.getUnitName());
			}
			if (data[6] != null)
				model.setAppliedQty(Double.parseDouble(data[6].toString()));
			if (data[7] != null)
				model.setEstimatedPrice(Double.parseDouble(data[7].toString()));
			if (data[8] != null)
				model.setEstimatedSum(Double.parseDouble("0"));
			if (data[9] != null)
				model.setLeft(Double.parseDouble(data[9].toString()));
			if (data[10] != null)
				model.setMaxStock(Double.parseDouble(data[10].toString()));
			if (data[11] != null)
				model.setUsage(data[11].toString());
			if (data[12] != null)
				model.setFactory(data[12].toString());
			if (data[13] != null)
				model.setNeedDate(data[13].toString());
			if (data[14] != null)
				model.setSupplier(data[14].toString());
			if (data[15] != null)
				model.setMemo(data[15].toString());
			if (data[16] != null)
				model.setEquCode(data[16].toString());
			if (data[17] != null)
				model.setItemId(data[17].toString());
			if (data[18] != null)
				model.setPlanOriginalId(Long.parseLong(data[18].toString()));
			if (data[19] != null)
				try {
					model.setLastModifiedDate(dateformat.parse(data[19]
							.toString()));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			// add by liuyi 091026 增加物料编码
			if (data[20] != null)
				model.setMaterialCode(data[20].toString());
			//add by ltong 20100505 物料分类
			if (data[21] != null)
				model.setClassNo(data[21].toString());
			arrlist.add(model);
		}
		if (arrlist.size() > 0) {
			result.setList(arrlist);
		}
		return result;

	}

	/**
	 * 物料需求计划登记账票信息查询
	 * 
	 * @param enterpriseCode
	 *            企业编码
	 * @param headId
	 *            选中项ID
	 * @return ReceiveGoodsBean 到货单明细
	 */
	@SuppressWarnings("unchecked")
	public MaterialRequestReportBean fillallForMaterialRequestReport(
			String enterpriseCode, String headId) {

		LogUtil.log("finding all InvCMaterial instances", Level.INFO, null);
		MaterialRequestReportBean materialReqiuestReportBean = new MaterialRequestReportBean();
		List materialDetailList = new ArrayList();
		// 数字输出格式化
		String patternNumber = "###,###,###,###,##0.00";
		DecimalFormat dfNumber = new DecimalFormat(patternNumber);
		String patternMoney = "###,###,###,###,##0.0000";
		DecimalFormat dfMoney = new DecimalFormat(patternMoney);
		String nullNumber = "0.00";
		String nullMoney = "0.0000";
		try {
			String sql = "select a.MR_NO,GETDEPTNAME(a.MR_DEPT),a.DUE_DATE,b.PLAN_ORIGINAL_DESC,a.ITEM_ID,GETDEPTNAME(a.COST_DEPT),a.COST_SPECIAL \n"
					+ "from MRP_J_PLAN_REQUIREMENT_HEAD a left join MRP_C_PLAN_ORIGINAL b \n"
					+ "on b.PLAN_ORIGINAL_ID = a.PLAN_ORIGINAL_ID \n"
					+ "and b.IS_USE = 'Y' \n"
					+ "and b.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'"
					+ "where a.REQUIREMENT_HEAD_ID='"
					+ headId
					+ "' \n"
					+ "and a.IS_USE='Y' \n"
					+ "and a.ENTERPRISE_CODE='" + enterpriseCode + "' \n";
			List list = bll.queryByNativeSQL(sql);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				// MaterialRequestReportListBean model = new
				// MaterialRequestReportListBean();
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					materialReqiuestReportBean.setApplicationNo(data[0]
							.toString());
				}
				if (data[1] != null) {
					materialReqiuestReportBean.setApplicationDept(data[1]
							.toString());
				}
				if (data[2] != null) {
					materialReqiuestReportBean.setDueDate(data[2].toString());
				}
				if (data[3] != null) {
					materialReqiuestReportBean.setPlanFrom(data[3].toString());
				}
				if (data[4] != null) {
					materialReqiuestReportBean.setMoneyFrom(data[4].toString());
				}
				if (data[5] != null) {
					materialReqiuestReportBean
							.setBelongDept(data[5].toString());
				}
				if (data[6] != null) {
					materialReqiuestReportBean.setBelongClass(data[6]
							.toString());
				}
			}
		} catch (Exception e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
		}
		try {
			String sql = "  SELECT   distinct       \n"
					+ "      a.REQUIREMENT_DETAIL_ID as requirementDetailId,            \n"
					+ "      b.MATERIAL_NO as materialNo,            \n"
					+ "      b.MATERIAL_NAME as materialName,            \n"
					+ "      b.SPEC_NO as materSize,         \n"
					+ "      a.APPLIED_QTY as appliedQty,            \n"
					+ "      a.APPROVED_QTY as apprpvedQty,          \n"
					+ "      a.ISS_QTY as issQty,            \n"
					+ "      a.ESTIMATED_PRICE as estimatedPrice,            \n"
					+ "      (a.APPLIED_QTY * a.ESTIMATED_PRICE ) as estimatedSum,           \n"
					+ "      tempSum.purQty as purQty,         \n"
					+ "      b.STOCK_UM_ID as stockUmName,           \n"
					+ "      a.USAGE as usage,           \n"
					+ "      a.memo as memo,         \n"
					+ "      to_char(a.DUE_DATE,'YYYY-mm-dd') as needDate,         \n"
					+ "      b.PARAMETER as parameter,           \n"
					+ "      b.DOC_NO as docNo,          \n"
					+ "      c.WHS_NAME as whsName,          \n"
					+ "      b.QUALITY_CLASS as qualityClass,            \n"
					+ "      temp.stock as left,         \n"
					+ "      tempSum.tempNum  as tempNum,      \n"
					+ "      a.ITEM_ID as itemId            \n"
					+ "  FROM            \n"
					+ "      MRP_J_PLAN_REQUIREMENT_DETAIL  a,           \n"
					+ "      INV_C_MATERIAL b,           \n"
					+ "      INV_C_WAREHOUSE c,          \n"
					+ "      (SELECT         \n"
					+ "          MATERIAL_ID as id,          \n"
					+ "          sum(OPEN_BALANCE + RECEIPT + ADJUST - ISSUE) as stock           \n"
					+ "      FROM            \n"
					+ "          INV_J_WAREHOUSE,            \n"
					+ "          INV_C_WAREHOUSE         \n"
					+ "      WHERE           \n"
					+ "          INV_J_WAREHOUSE.IS_USE = 'Y'        AND \n"
					+ "          INV_J_WAREHOUSE.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'      AND \n"
					+ "          INV_J_WAREHOUSE.WHS_NO = INV_C_WAREHOUSE.WHS_NO         AND \n"
					+ "          INV_C_WAREHOUSE.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'      AND \n"
					+ "          INV_C_WAREHOUSE.IS_USE = 'Y'        AND \n"
					+ "          INV_C_WAREHOUSE.IS_INSPECT = 'N'            \n"
					+ "      GROUP BY            \n"
					+ "          MATERIAL_ID         \n"
					+ "      ) temp,         \n"
					+ "      (SELECT         \n"
					+ "          d.REQUIREMENT_DETAIL_ID as id,          \n"
					+ "          sum(d.MR_QTY) as purQty,            \n"
					+ "          sum(e.INS_QTY) as tempNum           \n"
					+ "      FROM            \n"
					+ "          PUR_J_PLAN_ORDER d,         \n"
					+ "          PUR_J_ORDER_DETAILS e,          \n"
					+ "          (SELECT   distinct      \n"
					+ "              a.REQUIREMENT_DETAIL_ID         \n"
					+ "          FROM            \n"
					+ "              MRP_J_PLAN_REQUIREMENT_DETAIL  a,           \n"
					+ "              INV_C_MATERIAL b,           \n"
					+ "              INV_C_WAREHOUSE c,          \n"
					+ "              (SELECT         \n"
					+ "                  MATERIAL_ID as id,          \n"
					+ "                  sum(OPEN_BALANCE + RECEIPT + ADJUST - ISSUE) as stock           \n"
					+ "              FROM            \n"
					+ "                  INV_J_WAREHOUSE,            \n"
					+ "                  INV_C_WAREHOUSE         \n"
					+ "              WHERE           \n"
					+ "                  INV_J_WAREHOUSE.IS_USE = 'Y'        AND \n"
					+ "                  INV_J_WAREHOUSE.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'      AND \n"
					+ "                  INV_J_WAREHOUSE.WHS_NO = INV_C_WAREHOUSE.WHS_NO         AND \n"
					+ "                  INV_C_WAREHOUSE.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'      AND \n"
					+ "                  INV_C_WAREHOUSE.IS_USE = 'Y'        AND \n"
					+ "                  INV_C_WAREHOUSE.IS_INSPECT = 'N'            \n"
					+ "              GROUP BY            \n"
					+ "                  MATERIAL_ID         \n"
					+ "              ) temp          \n"
					+ "          WHERE           \n"
					+ "              a.REQUIREMENT_HEAD_ID= '"
					+ headId
					+ "' AND          \n"
					+ "              a.MATERIAL_ID=b.MATERIAL_ID AND         \n"
					+ "              a.MATERIAL_ID=temp.id(+) AND           \n"
					+ "              b.DEFAULT_WHS_NO=c.WHS_NO(+) AND           \n"
					+ "              a.IS_USE='Y' AND            \n"
					+ "              b.IS_USE='Y' AND            \n"
					+ "              c.IS_USE(+)='Y' AND            \n"
					+ "              a.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "' AND            \n"
					+ "              b.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'  AND           \n"
					+ "              c.ENTERPRISE_CODE(+) = '"
					+ enterpriseCode
					+ "'            \n"
					+ "          ) g         \n"
					+ "      WHERE           \n"
					+ "          d.IS_USE='Y' AND            \n"
					+ "          e.IS_USE='Y' AND            \n"
					+ "          d.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'   AND          \n"
					+ "          e.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "' AND            \n"
					+ "          d.REQUIREMENT_DETAIL_ID=g.REQUIREMENT_DETAIL_ID AND         \n"
					+ "          d.PUR_ORDER_DETAILS_ID=e.PUR_ORDER_DETAILS_ID           \n"
					+ "      GROUP BY            \n"
					+ "          d.REQUIREMENT_DETAIL_ID         \n"
					+ "      ) tempSum           \n"
					+ "  WHERE           \n"
					+ "      a.REQUIREMENT_HEAD_ID= '"
					+ headId
					+ "' AND          \n"
					+ "      tempSum.id(+)=a.REQUIREMENT_DETAIL_ID AND          \n"
					+ "      a.MATERIAL_ID=b.MATERIAL_ID AND         \n"
					+ "      a.MATERIAL_ID=temp.id(+) AND           \n"
					+ "      b.DEFAULT_WHS_NO=c.WHS_NO(+) AND           \n"
					+ "      a.IS_USE='Y' AND            \n"
					+ "      b.IS_USE='Y' AND            \n"
					+ "      c.IS_USE(+)='Y' AND            \n"
					+ "      a.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'    AND     \n"
					+ "      b.ENTERPRISE_CODE = '"
					+ enterpriseCode
					+ "'    AND     \n"
					+ "      c.ENTERPRISE_CODE(+) = '"
					+ enterpriseCode
					+ "'            \n"
					+ "  ORDER BY            \n"
					+ "      a.REQUIREMENT_DETAIL_ID            \n";
			List list = bll.queryByNativeSQL(sql);
			Iterator it = list.iterator();
			while (it.hasNext()) {
				MaterialRequestReportDetailListBean model = new MaterialRequestReportDetailListBean();
				Object[] data = (Object[]) it.next();
				if (data[0] != null) {
					model.setOrderDetailsId(data[0].toString());
				}
				if (data[1] != null) {
					model.setMaterialNo(data[1].toString());
				}
				if (data[2] != null) {
					model.setMaterialName(data[2].toString());
				}
				if (data[3] != null) {
					model.setSpecNo(data[3].toString());
				}
				if (data[4] != null) {
					model.setRequestQuantity(dfNumber.format(data[4]));
				} else {
					model.setRequestQuantity(nullNumber);
				}
				if (data[5] != null) {
					model.setCheckQuantity(dfNumber.format(data[5]));
				} else {
					model.setCheckQuantity(nullNumber);
				}
				if (data[6] != null) {
					model.setGotQuantity(dfNumber.format(data[6]));
				} else {
					model.setGotQuantity(nullNumber);
				}
				if (data[7] != null) {
					model.setEstimatedPrice(dfMoney.format(data[7]));
				} else {
					model.setEstimatedPrice(nullMoney);
				}
				if (data[8] != null) {
					model.setEstimatedMoney(dfMoney.format((data[8])));
					model.setTotalMoney(data[8].toString());
				} else {
					model.setEstimatedMoney(nullMoney);
					model.setTotalMoney(nullMoney);
				}
				if (data[9] != null) {
					model.setPurchaseQuantity(dfNumber.format(data[9]));
				} else {
					model.setPurchaseQuantity(nullNumber);
				}
				if (data[10] != null) {
					model.setStockUmName(data[10].toString());
				}
				if (data[11] != null) {
					model.setUseFor(data[11].toString());
				}
				if (data[12] != null) {
					model.setMeno(data[12].toString());
				}
				if (data[13] != null) {
					model.setDueDate(data[13].toString());
				}
				if (data[14] != null) {
					model.setMaterialPramater(data[14].toString());
				}
				if (data[15] != null) {
					model.setMaterialMapNo(data[15].toString());
				}
				if (data[16] != null) {
					model.setWareHouseNo(data[16].toString());
				}
				if (data[17] != null) {
					model.setQualityLevel(data[17].toString());
				}
				if (data[18] != null) {
					model.setNowStock(dfNumber.format(data[18]));
				} else {
					model.setNowStock(nullNumber);
				}
				if (data[19] != null) {
					model.setInstanceGotQuantity(dfNumber.format(data[19]));
				} else {
					model.setInstanceGotQuantity(nullNumber);
				}
				if (data[20] != null) {
					model.setMoneyFrom(data[20].toString());
				}
				materialDetailList.add(model);
			}
		} catch (Exception e) {
			LogUtil.log("find all failed", Level.SEVERE, e);
		}
		materialReqiuestReportBean
				.setMaterialRequestReportDetailList(materialDetailList);
		return materialReqiuestReportBean;
	}

	/**
	 * 获取当前库存
	 */
	@SuppressWarnings("unchecked")
	public String getMaterialStock(Long materialId, String enterpriseCode) {
		String sql = "select a.stock from \n"
				+ "      (SELECT \n"
				+ "          MATERIAL_ID as id,  \n"
				+ "          sum(OPEN_BALANCE + RECEIPT + ADJUST - ISSUE) as stock   \n"
				+ "      FROM    \n"
				+ "          INV_J_WAREHOUSE,    \n"
				+ "          INV_C_WAREHOUSE \n"
				+ "      WHERE   \n"
				+ "          INV_J_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "          INV_J_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND   \n"
				+ "          INV_J_WAREHOUSE.WHS_NO = INV_C_WAREHOUSE.WHS_NO         AND \n"
				+ "          INV_C_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode + "'      AND   \n"
				+ "          INV_C_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "          INV_C_WAREHOUSE.IS_INSPECT = 'N'    \n"
				+ "      GROUP BY    \n" + "          MATERIAL_ID \n"
				+ "      ) a where a.id='" + materialId + "' \n \n";

		List<Object> listMaterial = bll.queryByNativeSQL(sql);
		if (listMaterial.size() > 0) {
			return listMaterial.get(0).toString();
		} else {
			return "0";
		}
	}

	/**
	 * 
	 * @param initialBookNo
	 *            传入的单号格式 如"DP000000"
	 * @param tabelName
	 *            表名
	 * @param idColumnName
	 *            列名
	 * @return 最大的单号
	 */
	public String getMaxBookNo(String initialBookNo, String tabelName,
			String idColumnName) {
		// 最大单号初始化为初始的单号
		StringBuffer maxBookNo = new StringBuffer(initialBookNo);
		// 数据库中当前的最大单号
		Long bookNo = bll.getMaxId(tabelName, idColumnName);
		String strTemp = "0123456789";
		// j--数字出现的开始位置
		int j = 0;
		// 从字符串后面开始检索，检索不是数字的位置
		for (int i = maxBookNo.length() - 1; i >= 0; i--) {
			j = strTemp.indexOf(maxBookNo.charAt(i));
			// 如果不是数字，返回在该字符在字符串的位置
			if (j == -1) {
				j = i;
				break;
			}
		}
		if (bookNo < Math.pow(10, (maxBookNo.length() - j - 1))) {
			// 最大的数字
			String currentData = "";
			// 获取最大单号的数字部分
			currentData = String.valueOf(bookNo);
			// 如果当前单号的数字部分长度小于初始化时的数字部分长度
			if (currentData.length() < maxBookNo.length() - j - 1) {
				maxBookNo.replace(maxBookNo.length() - currentData.length(),
						maxBookNo.length() + 1, currentData);
			} else {
				// 如果当前单号的数字部分长度小于初始化时的数字部分长度
				maxBookNo.replace(j + 1, maxBookNo.length() + 1, currentData);
			}
		} else {
			maxBookNo = maxBookNo.replace(j + 1, maxBookNo.length() + 1, String
					.valueOf(bookNo));
		}
		
		return maxBookNo.toString();
	}

	// add by fyyang modify by fyyang 090618
	@SuppressWarnings("unchecked")
	public PageObject findApproveListByFuzzy(String enterpriseCode,
			String startDate, String endDate, String dept, String mrBy,
			String maName, String maClassName, String status, String entryIds,
			String planOriginalID, String deptCode, // 登陆人的部门编码
			String workCode,// 登录人的工号
			int... rowStartIdxAndCount) {
		PageObject result = new PageObject();
		// modify by ywliu 2009/7/6 将A.ITEM_ID改为A.ITEM_CODE
		String sql = "select distinct a.  REQUIREMENT_HEAD_ID     ,\n"
				+ "  a.  MR_NO                   ,\n"
				+ "  a.  MR_DATE                 ,\n"
				+ "  a.  WO_NO                   ,\n"
				+ "  a.  PLAN_ORIGINAL_ID        ,\n"
				+ "  a.  ITEM_CODE                     ,\n"
				+ "  a.  MR_BY                       ,\n"
				+ "  GETDEPTNAME(a.MR_DEPT)||'/'||a.MR_DEPT  as MR_DEPT               ,\n"
				+ "  a.  DUE_DATE                ,\n"
				+ "  a.  PLAN_GRADE              ,\n"
				+ "  a.  COST_DEPT               ,\n"
				+ "  a.  COST_SPECIAL            ,\n"
				+ "  a.  MR_REASON               ,\n"
				+ "  a.  MR_TYPE                 ,\n"
				+ "  a.  WF_NO                       ,\n"
				+ "  GETPLANSTATUSNAME(a.MR_STATUS)||a.MR_STATUS   MR_STATUS            ,\n"
				+ "  a.  ENTERPRISE_CODE         ,\n"
				+ "  a.  IS_USE                      ,\n"
				+ "  a.  LAST_MODIFIED_BY        ,\n"
				+ "  a.  LAST_MODIFIED_DATE    ,  \n" + "a.PLAN_DATE_MEMO \n"
				+",a.prj_no \n";

		String strFromStr = "  from MRP_J_PLAN_REQUIREMENT_HEAD a \n";
		String strWhere = " where a.enterprise_code = '" + enterpriseCode
				+ "'\n" + "  and a.is_use = 'Y'\n";

		if (startDate != null && !"".equals(startDate)) {
			strWhere += "  and to_char(a.MR_DATE,'yyyy-MM-dd') >='" + startDate
					+ "'\n";
		}
		if (endDate != null && !endDate.equals("")) {
			strWhere += "  and to_char(a.MR_DATE,'yyyy-MM-dd') <='" + endDate
					+ "'\n";
		}
		if (mrBy != null && !mrBy.equals("")) {
			strWhere += "  and a.MR_BY ='" + mrBy + "'\n";
		}
		if (maName != null && !"".equals(maName)) {
			strFromStr += " , MRP_J_PLAN_REQUIREMENT_DETAIL b,\n"
					+ " INV_C_MATERIAL c \n";
			strWhere += " and c.MATERIAL_NAME like '%" + maName + "%'\n"
					+ " and b.MATERIAL_ID = c.MATERIAL_ID"
					+ " and a.REQUIREMENT_HEAD_ID = b.REQUIREMENT_HEAD_ID"
					+ " and b.is_use = 'Y'\n" + " and c.is_use = 'Y'\n"
					+ " and b.enterprise_code = '" + enterpriseCode + "'\n"
					+ " and c.enterprise_code = '" + enterpriseCode + "'\n";
		}
		if (maClassName != null && !"".equals(maClassName)) {
			if (maName != null && !"".equals(maName)) {
				strFromStr += " ,INV_C_MATERIAL_CLASS d\n";
				// modify by ywliu , 2009/7/7 将类别查询改为模糊查询，根据class_no查询
				strWhere += " and d.class_no like '" + maClassName + "%'\n"
						+ " and d.is_use = 'Y'\n"
						+ " and d.enterprise_code = '" + enterpriseCode + "'\n";
			} else {
				strFromStr += " , MRP_J_PLAN_REQUIREMENT_DETAIL b,\n"
						+ " INV_C_MATERIAL c,\n" + " INV_C_MATERIAL_CLASS d\n";
				strWhere += "and d.class_no like '" + maClassName + "%'\n"
						+ " and c.MAERTIAL_CLASS_ID = d.MAERTIAL_CLASS_ID \n"
						+ " and b.MATERIAL_ID = c.MATERIAL_ID"
						+ " and a.REQUIREMENT_HEAD_ID = b.REQUIREMENT_HEAD_ID"
						+ " and b.is_use = 'Y'\n" + " and c.is_use = 'Y'\n"
						+ " and d.is_use = 'Y'\n"
						+ " and b.enterprise_code = '" + enterpriseCode + "'\n"
						+ " and c.enterprise_code = '" + enterpriseCode + "'\n"
						+ " and d.enterprise_code = '" + enterpriseCode + "'\n";
			}
		}
		if (dept != null && !"".equals(dept)) {
			strWhere += " and a.MR_DEPT='" + dept + "'\n";
		}
		// add 审批状态
		if (status != null && !status.equals("")) {
			strWhere += " and a.mr_status='" + status + "'\n";
		}
		if (entryIds != null && !entryIds.equals("")) {
			strWhere += " and a.wf_no in (" + entryIds + ")\n";
		}
		// add by fyyang 090618
		// add by bjxu 091103 null 为全部计划种类查询
		if (planOriginalID != null && !"".equals(planOriginalID)
				&& !"null".equals(planOriginalID)) {
			strWhere += "  and a.plan_original_id='" + planOriginalID + "'  \n";
		}
		// --------add by fyyang 090722---------部门过滤----------
		// modify by fyyang 091022
		if (!workCode.equals("999999")) {

			if (deptCode != null && deptCode.length() > 1) {

				// 本部门领导审批：审批人与申请人在同一个公司且在同一个部门下
				// 发电、实业、检修综合部审批：审批人与申请人在同一个公司下 去掉
				// strWhere+=" and a.mr_dept like
				// (decode(a.mr_status,'1','"+deptCode+"','3',substr('"+deptCode+"',0,2)||'%',a.mr_dept))
				// \n";

				// modify by fyyang 091103
				// strWhere += " and a.mr_dept like (decode(a.mr_status,'1','"
				// + deptCode
				// +
				// "','3',decode(a.plan_original_id,12,a.mr_dept,13,a.mr_dept,14,a.mr_dept,substr('"
				// + deptCode + "',0,2)||'%'),a.mr_dept)) \n";
				// modify by fyyang 20100319
				strWhere += "  and  a.mr_dept like (decode(a.mr_status,'1','"
						+ deptCode + "',a.mr_dept)) \n";

			}
		}

		// ------------------------------------------------------

		strWhere += " order by a.REQUIREMENT_HEAD_ID desc";
		sql += strFromStr;
		sql += strWhere;
		String sqlCount = "select count(a.REQUIREMENT_HEAD_ID)\n";
		sqlCount += strFromStr;
		sqlCount += strWhere;
		List<MrpJPlanRequirementHead> arrlist = bll.queryByNativeSQL(sql,
				MrpJPlanRequirementHead.class, rowStartIdxAndCount);
		if (arrlist.size() > 0) {
			for (MrpJPlanRequirementHead temp : arrlist) {
				temp.setMrBy(bllEmp.getEmployeeInfo(temp.getMrBy())
						.getWorkerName());
			}
			result.setList(arrlist);
			result.setTotalCount(Long.parseLong(bll.getSingal(sqlCount)
					.toString()));
		}
		return result;

	}

	// modify by fyyang 090630 不需根据采购员查询 modify by fyyang 090703
	@SuppressWarnings("unchecked")
	public PageObject getMRPMaterialDetail(String buyer, String applyDept,
			String enterpriseCode, int... rowStartIdxAndCount) {
		String sql = "select c.material_no ,c.material_name, c.material_id ,c.parameter,c.spec_no,d.class_name,c.qa_control_flag,c.max_stock ,c.pur_um_id,b.approved_qty,b.iss_qty ,b.requirement_detail_id\n"
				+ ",GETWORKERNAME(a.mr_by),GETDEPTNAME(a.mr_dept),a.mr_reason,a.mr_date,a.plan_original_id, b.due_date,b.supplier,b.memo,getworkername(b.last_modified_by), \n"
				+ // modify by ywliu 20091030
				// add by liuyi 110909 增加是否退回 及退回原因
				"  (select distinct e.is_return \n"
				+ " from mrp_j_plan_gather e \n"
				+ "where e.material_id = b.material_id \n"
				+ " and instr(','||E.requirement_detail_ids||',',','||b.requirement_detail_id||',')<>0 \n"
				+ "and e.is_use='N' \n"
				+ "and e.is_return='Y' \n"
				+ " and e.gather_id=(select max(f.gather_id) from mrp_j_plan_gather f where f.material_id=b.material_id \n"
				+ "and instr(','||f.requirement_detail_ids||',',','||b.requirement_detail_id||',')<>0 \n"
				+ "and f.is_use='N' \n"
				+ " and f.is_return='Y')), \n"
				+ " (select distinct e.return_reason \n"
				+ "from mrp_j_plan_gather e  \n"
				+ "where e.material_id = b.material_id \n"
				+ " and instr(','||E.requirement_detail_ids||',',','||b.requirement_detail_id||',')<>0 \n"
				+ "and e.is_use='N' \n"
				+ "and e.is_return='Y' \n"
				+ " and e.gather_id=(select max(f.gather_id) from mrp_j_plan_gather f where f.material_id=b.material_id \n"
				+ "and instr(','||f.requirement_detail_ids||',',','||b.requirement_detail_id||',')<>0 \n"
				+ "and f.is_use='N' \n"
				+ " and f.is_return='Y')) \n"
				+ ",c.factory \n"
				+ "       from mrp_j_plan_requirement_head a ,mrp_j_plan_requirement_detail b ,INV_C_MATERIAL c ,inv_c_material_class d\n"
				+ "       where a.requirement_head_id = b.requirement_head_id and a.mr_status = '2' and b.is_generated = 'N' and b.material_id = c.material_id\n"
				+ "       and c.maertial_class_id = d.maertial_class_id\n"
				+ "       and a.is_use='Y' and b.is_use='Y' and c.is_use='Y' and d.is_use='Y' \n"
				+ "       and a.enterprise_code='"
				+ enterpriseCode
				+ "' and b.enterprise_code='"
				+ enterpriseCode
				+ "' and c.enterprise_code='"
				+ enterpriseCode
				+ "' and d.enterprise_code='"
				+ enterpriseCode
				+ "'   \n"
				+ "   and b.approved_qty<>0  \n"; // add by fyyang 100107

		String sqlCount = "select  count(1)\n"
				+ "       from mrp_j_plan_requirement_head a ,mrp_j_plan_requirement_detail b ,INV_C_MATERIAL c ,inv_c_material_class d\n"
				+ "       where a.requirement_head_id = b.requirement_head_id and a.mr_status = '2' and b.is_generated = 'N' and b.material_id = c.material_id\n"
				+ "       and c.maertial_class_id = d.maertial_class_id\n"
				+ "       and a.is_use='Y' and b.is_use='Y' and c.is_use='Y' and d.is_use='Y' \n"
				+ "       and a.enterprise_code='" + enterpriseCode
				+ "' and b.enterprise_code='" + enterpriseCode
				+ "' and c.enterprise_code='" + enterpriseCode
				+ "' and d.enterprise_code='" + enterpriseCode + "' \n"
				+ "   and b.approved_qty<>0  \n"; // add by fyyang 100107

		if (applyDept != null && !applyDept.equals("")) {
			sql += " and a.mr_dept='" + applyDept + "' \n";
			sqlCount += " and a.mr_dept='" + applyDept + "' \n";
		}
		// if(buyer!=null&&!buyer.equals(""))
		// {
		// sql+=
		// "and c.material_no in\n" +
		// " (\n" +
		// " select t.material_or_class_no\n" +
		// " from PUR_C_BUYER t\n" +
		// " where t.buyer_name like '%"+buyer+"%'\n" +
		// " and t.enterprise_code = 'hfdc'\n" +
		// " and t.is_use = 'Y'\n" +
		// " ) \n";
		//
		// sqlCount+=
		// "and c.material_no in\n" +
		// " (\n" +
		// " select t.material_or_class_no\n" +
		// " from PUR_C_BUYER t\n" +
		// " where t.buyer_name like '%"+buyer+"%'\n" +
		// " and t.enterprise_code = 'hfdc'\n" +
		// " and t.is_use = 'Y'\n" +
		// " ) \n";
		//
		// }
		sql += " order by b.requirement_detail_id desc \n";
		List<MRPGatherDetailInfo> list = bll.queryByNativeSQL(sql,
				rowStartIdxAndCount);
		List<MRPGatherDetailInfo> arrlist = new ArrayList<MRPGatherDetailInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			MRPGatherDetailInfo model = new MRPGatherDetailInfo();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				model.setMaterialNo(data[0].toString());
			if (data[1] != null)
				model.setMaterialName(data[1].toString());
			if (data[2] != null)
				model.setMaterialId(data[2].toString());
			if (data[3] != null)
				model.setParameter(data[3].toString());
			if (data[4] != null)
				model.setSpecNo(data[4].toString());
			if (data[5] != null)
				model.setClassName(data[5].toString());

			if (data[6] != null)
				model.setQaControlFlag(data[6].toString());
			if (data[7] != null)
				model.setMaxStock(Double.parseDouble(data[7].toString()));
			if (data[8] != null) {
				BpCMeasureUnitFacadeRemote cc = (BpCMeasureUnitFacadeRemote) Ejb3Factory
						.getInstance().getFacadeRemote("BpCMeasureUnitFacade");
				BpCMeasureUnit bp = cc.findById(Long.parseLong(data[8]
						.toString()));
				if (bp != null) {
					model.setStockUmName(bp.getUnitName());
				}
			}
			if (data[9] != null)
				model.setApprovedQty(Double.parseDouble(data[9].toString()));
			if (data[10] != null)
				model.setIssQty(Double.parseDouble(data[10].toString()));
			if (data[11] != null)
				model.setRequirementDetailId(data[11].toString());
			if (data[12] != null)
				model.setApplyByName(data[12].toString());
			if (data[13] != null)
				model.setApplyDeptName(data[13].toString());
			if (data[14] != null)
				model.setApplyReason(data[14].toString());
			if (data[15] != null) {
				model.setMrDate(data[15].toString());
			}
			if (data[16] != null) {
				model.setPlanOriginalId(data[16].toString());
			}
			// modify by ywliu 20091030
			if (data[17] != null) {
				model.setDueDate(data[17].toString());
			}
			if (data[18] != null) {
				model.setSupplier(data[18].toString());
			}
			if (data[19] != null) {
				model.setMemo(data[19].toString());
			}
			if (data[20] != null) {
				model.setEntryBy(data[20].toString());
			}
			if (data[21] != null) {
				model.setIsReturn(data[21].toString());
			}
			if (data[22] != null) {
				model.setReturnReason(data[22].toString());
			}
			// modify by ywliu 20091030 End
			if (data[23] != null) {
				model.setFactory(data[23].toString());
			}
			arrlist.add(model);
		}
		PageObject reObject = new PageObject();
		reObject.setList(arrlist);
		reObject.setTotalCount(Long.parseLong(bll.getSingal(sqlCount)
				.toString()));
		return reObject;
	}

	/**
	 * 通过费用编码查出费用名称(物资公用方法)
	 * 
	 * @param itemCode
	 * @return String[]
	 */
	public String getItemNameByItemCode(String itemCode) {
		String str = null;
		if ("zzfy".equals(itemCode)) {
			str = "制造费用";
		} else if ("lwcb".equals(itemCode)) {
			str = "劳务成本";
		}
		return str;
	}

	/**
	 * 删除需求计划申请单 add by fyyang 090728
	 * 
	 * @param entryId
	 *            实例号id
	 * @param headId
	 *            计划单id
	 */
	public void deletePlanRequirement(Long entryId, Long headId) {
		String headSql = "update mrp_j_plan_requirement_head t\n"
				+ "set t.is_use='N'\n" + "where t.requirement_head_id="
				+ headId;
		bll.exeNativeSQL(headSql);
		String detailSql = "update mrp_j_plan_requirement_detail a\n"
				+ "set a.is_use='N'\n" + "where a.requirement_head_id="
				+ headId;
		bll.exeNativeSQL(detailSql);
		if (entryId != null && !entryId.equals("")) {
			BaseDataManager commRemote = (BaseDataManager) Ejb3Factory
					.getInstance().getFacadeRemote("BaseDataManagerImpl");
			commRemote.deleteWf(entryId);
		}
	}

	/**
	 * 获得已经执行过计划作废的物资列表 add by fyyang 090807
	 * 
	 * @param applyDept
	 * @param enterpriseCode
	 * @param rowStartIdxAndCount
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public PageObject getBlankOutMaterialDetail(String applyDept,
			String wzName, String ggSize, String enterpriseCode,
			int... rowStartIdxAndCount) {
		String sql = "select c.material_no ,c.material_name, c.material_id ,c.parameter,c.spec_no,d.class_name,c.qa_control_flag,c.max_stock ,c.pur_um_id,b.approved_qty,b.iss_qty ,b.requirement_detail_id\n"
				+ ",GETWORKERNAME(a.mr_by),GETDEPTNAME(a.mr_dept),a.mr_reason,a.mr_date,a.plan_original_id,b.cancel_reason \n"
				+ "       from mrp_j_plan_requirement_head a ,mrp_j_plan_requirement_detail b ,INV_C_MATERIAL c ,inv_c_material_class d\n"
				+ "       where a.requirement_head_id = b.requirement_head_id and a.mr_status = '2' and b.is_generated = 'N' and b.material_id = c.material_id\n"
				+ "       and c.maertial_class_id = d.maertial_class_id\n"
				+ "       and a.is_use='Y' and b.is_use='C' and c.is_use='Y' and d.is_use='Y' \n"
				+ "       and a.enterprise_code='"
				+ enterpriseCode
				+ "' and b.enterprise_code='"
				+ enterpriseCode
				+ "' and c.enterprise_code='"
				+ enterpriseCode
				+ "' and d.enterprise_code='" + enterpriseCode + "' \n";

		String sqlCount = "select  count(1)\n"
				+ "       from mrp_j_plan_requirement_head a ,mrp_j_plan_requirement_detail b ,INV_C_MATERIAL c ,inv_c_material_class d\n"
				+ "       where a.requirement_head_id = b.requirement_head_id and a.mr_status = '2' and b.is_generated = 'N' and b.material_id = c.material_id\n"
				+ "       and c.maertial_class_id = d.maertial_class_id\n"
				+ "       and a.is_use='Y' and b.is_use='C' and c.is_use='Y' and d.is_use='Y' \n"
				+ "       and a.enterprise_code='" + enterpriseCode
				+ "' and b.enterprise_code='" + enterpriseCode
				+ "' and c.enterprise_code='" + enterpriseCode
				+ "' and d.enterprise_code='" + enterpriseCode + "' \n";

		if (applyDept != null && !applyDept.equals("")) {
			sql += " and a.mr_dept='" + applyDept + "' \n";
			sqlCount += " and a.mr_dept='" + applyDept + "' \n";
		}
		if (wzName != null && !wzName.equals("")) {
			sql += " and c.material_name like '%" + wzName + "%' \n";
			sqlCount += " and c.material_name like '%" + wzName + "%' \n";
		}
		if (ggSize != null && !ggSize.equals("")) {
			sql += " and c.spec_no like '%" + ggSize + "%' \n";
			sqlCount += " and c.spec_no like '%" + ggSize + "%' \n";
		}

		List<MRPGatherDetailInfo> list = bll.queryByNativeSQL(sql,
				rowStartIdxAndCount);
		List<MRPGatherDetailInfo> arrlist = new ArrayList<MRPGatherDetailInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			MRPGatherDetailInfo model = new MRPGatherDetailInfo();
			Object[] data = (Object[]) it.next();
			if (data[0] != null)
				model.setMaterialNo(data[0].toString());
			if (data[1] != null)
				model.setMaterialName(data[1].toString());
			if (data[2] != null)
				model.setMaterialId(data[2].toString());
			if (data[3] != null)
				model.setParameter(data[3].toString());
			if (data[4] != null)
				model.setSpecNo(data[4].toString());
			if (data[5] != null)
				model.setClassName(data[5].toString());

			if (data[6] != null)
				model.setQaControlFlag(data[6].toString());
			if (data[7] != null)
				model.setMaxStock(Double.parseDouble(data[7].toString()));
			if (data[8] != null) {
				BpCMeasureUnitFacadeRemote cc = (BpCMeasureUnitFacadeRemote) Ejb3Factory
						.getInstance().getFacadeRemote("BpCMeasureUnitFacade");
				BpCMeasureUnit bp = cc.findById(Long.parseLong(data[8]
						.toString()));
				if (bp != null) {
					model.setStockUmName(bp.getUnitName());
				}
			}
			if (data[9] != null)
				model.setApprovedQty(Double.parseDouble(data[9].toString()));
			if (data[10] != null)
				model.setIssQty(Double.parseDouble(data[10].toString()));
			if (data[11] != null)
				model.setRequirementDetailId(data[11].toString());
			if (data[12] != null)
				model.setApplyByName(data[12].toString());
			if (data[13] != null)
				model.setApplyDeptName(data[13].toString());
			if (data[14] != null)
				model.setApplyReason(data[14].toString());
			if (data[15] != null) {
				model.setMrDate(data[15].toString());
			}
			if (data[16] != null) {
				model.setPlanOriginalId(data[16].toString());
			}
			if (data[17] != null) {
				model.setCancelReason(data[17].toString());
			}
			arrlist.add(model);
		}
		PageObject reObject = new PageObject();
		reObject.setList(arrlist);
		reObject.setTotalCount(Long.parseLong(bll.getSingal(sqlCount)
				.toString()));
		return reObject;
	}

	// modify by ywliu 20091112 增加 dept 和 queryType 2个参数
	@SuppressWarnings("unchecked")
	public PageObject findMaterialDetailByCond(String fromDate, String toDate,
			String materialNo, String materialName, String specNo,
			String needPlanNo, String mrBy, String buyBy, String dept,
			String queryType, String observer, String status,
			String enterpriseCode, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		// 不传值时表示查询未作废的（is_use='Y'），传值1时表示查询已作废和未作废的(is_use=Y or C)
		String isUse = "";
		// if(flag!=null&&flag.equals("1"))
		// {
		isUse = "'Y','C'";
		// }
		// else
		// {
		// isUse="'Y'";
		//			
		// }
		// -------------------------------
		String sql = "  SELECT          \n"
				+ "      a.REQUIREMENT_DETAIL_ID as requirementDetailId,            \n"
				+ "      b.MATERIAL_NO as materialNo,            \n"
				+ "      b.MATERIAL_NAME as materialName,            \n"
				+ "      b.SPEC_NO as materSize,         \n"
				+ "      a.APPLIED_QTY as appliedQty,            \n"
				+ "      a.APPROVED_QTY as apprpvedQty,          \n"
				+ "      a.ISS_QTY as issQty,            \n"
				+ "      a.ESTIMATED_PRICE as estimatedPrice,            \n"
				+ "      (a.APPLIED_QTY * a.ESTIMATED_PRICE ) as estimatedSum,           \n"
				+ "      tempSum.purQty as purQty,         \n"
				+ "      b.STOCK_UM_ID as stockUmName,           \n"
				+ "      a.USAGE as usage,           \n"
				+ "      a.memo as memo,         \n"
				+ "      to_char(a.DUE_DATE,'YYYY-mm-dd') as needDate,         \n"
				+ "      b.PARAMETER as parameter,           \n"
				+ "      b.DOC_NO as docNo,          \n"
				+ "      c.WHS_NAME as whsName,          \n"
				+ "      b.QUALITY_CLASS as qualityClass,            \n"
				+ "      temp.stock as left,         \n"
				+ "      tempSum.tempNum  as tempNum,      \n"
				+ "      a.ITEM_CODE as itemId,          \n"// modify by ywliu
				// 2009/7/6
				+ "      a.cancel_reason,a.is_use, \n" // add by fyyang 090807
				+ " to_char(tt.mr_date,'yyyy-MM-dd'), \n"
				+ " tt.mr_no,\n"
				+ " tt.wf_no,\n"
				+ " tt.plan_original_id, \n"

				+ "  GETDEPTNAME(tt.MR_DEPT), \n" // add by ywliu 091112
				+ " tt.mr_status \n"
				+",  f.buyer,getworkername(f.buyer) \n" //add by fyyang 20100511采购员
				+ "  FROM            \n"
				+ "      MRP_J_PLAN_REQUIREMENT_DETAIL  a,mrp_j_plan_requirement_head tt,     \n"
				+" mrp_j_plan_gather f, \n" //add by fyyang 20100511
				+ "      INV_C_MATERIAL b,           \n"
				+ "      INV_C_WAREHOUSE c,          \n"
				+ "      (SELECT         \n"
				+ "          MATERIAL_ID as id,          \n"
				+ "          sum(OPEN_BALANCE + RECEIPT + ADJUST - ISSUE) as stock           \n"
				+ "      FROM            \n"
				+ "          INV_J_WAREHOUSE,            \n"
				+ "          INV_C_WAREHOUSE         \n"
				+ "      WHERE           \n"
				+ "          INV_J_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "          INV_J_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND \n"
				+ "          INV_J_WAREHOUSE.WHS_NO = INV_C_WAREHOUSE.WHS_NO         AND \n"
				+ "          INV_C_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND \n"
				+ "          INV_C_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "          INV_C_WAREHOUSE.IS_INSPECT = 'N'            \n"
				+ "      GROUP BY            \n"
				+ "          MATERIAL_ID         \n"
				+ "      ) temp,         \n"
				+ "      (SELECT         \n"
				+ "          d.REQUIREMENT_DETAIL_ID as id,          \n"
				+ "          sum(d.MR_QTY) as purQty,            \n"
				+ "          sum(e.INS_QTY) as tempNum,           \n"
				+ "  ee.buyer as buyer  \n"
				+ "      FROM            \n"
				+ "          PUR_J_PLAN_ORDER d,         \n"
				+ "          PUR_J_ORDER_DETAILS e,pur_j_order ee,    \n"
				+ "          (SELECT         \n"
				+ "              a.REQUIREMENT_DETAIL_ID         \n"
				+ "          FROM            \n"
				+ "              MRP_J_PLAN_REQUIREMENT_DETAIL  a,           \n"
				+ "              INV_C_MATERIAL b,           \n"
				+ "              INV_C_WAREHOUSE c,          \n"
				+ "              (SELECT         \n"
				+ "                  MATERIAL_ID as id,          \n"
				+ "                  sum(OPEN_BALANCE + RECEIPT + ADJUST - ISSUE) as stock           \n"
				+ "              FROM            \n"
				+ "                  INV_J_WAREHOUSE,            \n"
				+ "                  INV_C_WAREHOUSE         \n"
				+ "              WHERE           \n"
				+ "                  INV_J_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "                  INV_J_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND \n"
				+ "                  INV_J_WAREHOUSE.WHS_NO = INV_C_WAREHOUSE.WHS_NO         AND \n"
				+ "                  INV_C_WAREHOUSE.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'      AND \n"
				+ "                  INV_C_WAREHOUSE.IS_USE = 'Y'        AND \n"
				+ "                  INV_C_WAREHOUSE.IS_INSPECT = 'N'            \n"
				+ "              GROUP BY            \n"
				+ "                  MATERIAL_ID         \n"
				+ "              ) temp          \n"
				+ "          WHERE    a.MATERIAL_ID=b.MATERIAL_ID AND         \n"
				+ "              a.MATERIAL_ID=temp.id(+) AND           \n"
				+ "              b.DEFAULT_WHS_NO=c.WHS_NO(+) AND           \n"
				+ "              a.IS_USE in ("
				+ isUse
				+ ") AND            \n"
				+ "              b.IS_USE='Y' AND            \n"
				+ "              c.IS_USE(+)='Y' AND            \n"
				+ "              a.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "' AND            \n"
				+ "              b.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'  AND           \n"
				+ "              c.ENTERPRISE_CODE(+) = '"
				+ enterpriseCode
				+ "'            \n"
				+ "          ) g         \n"
				+ "      WHERE           \n"
				+ "          d.IS_USE='Y' AND            \n"
				+ "          e.IS_USE='Y' AND            \n"
				+ "          d.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'   AND          \n"
				+ "          e.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "' AND            \n"
				+ "          d.REQUIREMENT_DETAIL_ID=g.REQUIREMENT_DETAIL_ID AND         \n"
				+ "          d.PUR_ORDER_DETAILS_ID=e.PUR_ORDER_DETAILS_ID           \n"
				+ " and ee.is_use='Y' \n"
				+ "and ee.enterprise_code='"
				+ enterpriseCode
				+ "' \n"
				+ "and e.pur_no=ee.pur_no \n"
				+ "      GROUP BY            \n"
				+ "          d.REQUIREMENT_DETAIL_ID,ee.buyer         \n"
				+ "      ) tempSum           \n"
				+ "  WHERE   tempSum.id(+)=a.REQUIREMENT_DETAIL_ID AND          \n"
				+ "      a.MATERIAL_ID=b.MATERIAL_ID AND         \n"
				+ "      a.MATERIAL_ID=temp.id(+) AND           \n"
				+ "      b.DEFAULT_WHS_NO=c.WHS_NO(+) AND           \n"
				+ "     a.IS_USE in ("
				+ isUse
				+ ")  AND            \n"
				+ "      b.IS_USE='Y' AND            \n"
				+ "      c.IS_USE(+)='Y' AND            \n"
				+ "      a.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'    AND     \n"
				+ "      b.ENTERPRISE_CODE = '"
				+ enterpriseCode
				+ "'    AND     \n"
				+ "      c.ENTERPRISE_CODE(+) = '"
				+ enterpriseCode
				+ "'            \n"
				+ "and tt.is_use='Y' \n"
				+ "and a.requirement_head_id=tt.requirement_head_id \n"
				+ "and tt.enterprise_code='" + enterpriseCode + "' \n"
				+" and f.requirement_detail_ids(+)=to_char(a.requirement_detail_id) and f.is_use(+)='Y' \n";//add by fyyang 20100511

		if (fromDate != null && !fromDate.equals(""))
			sql += "and to_char(tt.mr_date, 'YYYY-mm-dd') >='" + fromDate
					+ "' \n";
		if (toDate != null && !toDate.equals(""))
			sql += "and to_char(tt.mr_date, 'YYYY-mm-dd') <='" + toDate
					+ "' \n";
		if (materialNo != null && !materialNo.equals(""))
			sql += "and b.MATERIAL_NO like '%" + materialNo + "%' \n";
		if (materialName != null && !materialName.equals(""))
			sql += "and b.MATERIAL_NAME like '%" + materialName + "%' \n";
		if (specNo != null && !specNo.equals(""))
			sql += "and b.SPEC_NO like '%" + specNo + "%' \n";
		if (needPlanNo != null && !needPlanNo.equals(""))
			sql += "and tt.mr_no like '%" + needPlanNo + "%' \n";
		if (mrBy != null && !mrBy.equals(""))// add by ywliu 20091113
			sql += "and tt.mr_by ='" + mrBy + "' \n";
		if (buyBy != null && !buyBy.equals(""))
			sql += "and tempSum.buyer='" + buyBy + "' \n";
		// 
		if (dept != null && !"".equals(dept)) {// add by ywliu 20091113
			sql += " and tt.MR_DEPT='" + dept + "'\n";
		}
		if (status != null && !"".equals(status)) {// add by jling
			sql += " and tt.mr_status='" + status + "'\n";
		}

		if ("2".equals(queryType)) {// add by ywliu 20091113 我审批的
			sql += " and tt.wf_no in(select distinct a.entry_id from wf_c_entry a, wf_j_historyoperation b \n "
					+ " where a.entry_id = b.entry_id \n"
					+ " and (a.flow_type = 'hfResourcePlanXZ-v1.0' or a.flow_type = 'hfResourcePlanGDZC-v1.0' or a.flow_type = 'hfResourcePlanSC-v1.0' or a.flow_type = 'hfResourcePlanJSJ-v1.0')\n"
					+ " and b.caller = '"
					+ observer
					+ "') "
					+ " and tt.MR_STATUS not in ('0','1')";
		} else if ("1".equals(queryType)) {// add by ywliu 20091113 我上报的
			sql += "and tt.mr_by ='" + observer + "' \n" +
			// " and tt.wf_no in(select distinct a.entry_id from wf_c_entry a,
					// wf_j_historyoperation b \n " +
					// " where a.entry_id = b.entry_id \n" +
					// " and (a.flow_type = 'hfResourcePlanXZ-v1.0' or
					// a.flow_type = 'hfResourcePlanGDZC-v1.0' or a.flow_type =
					// 'hfResourcePlanSC-v1.0' or a.flow_type =
					// 'hfResourcePlanJSJ-v1.0')\n" +
					// " and b.caller = '"+observer+"') " +
					" and tt.MR_STATUS not in ('0')";
		} else if ("4".equals(queryType)) {// add by ywliu 20091113 本部门的
			sql += " and tt.MR_DEPT = (select b.dept_code from hr_j_emp_info a,hr_c_dept b  where a.dept_id = b.dept_id and  a.emp_code = '"
					+ observer + "')\n";
		}
		sql += "  ORDER BY            \n"
				+ "      a.REQUIREMENT_DETAIL_ID         \n";
		List<MrpJPlanRequirementDetailInfo> list = bll.queryByNativeSQL(sql,
				rowStartIdxAndCount);
		List<MrpJPlanRequirementDetailInfo> arrlist = new ArrayList<MrpJPlanRequirementDetailInfo>();
		Iterator it = list.iterator();
		while (it.hasNext()) {
			MrpJPlanRequirementDetailInfo model = new MrpJPlanRequirementDetailInfo();
			Object[] data = (Object[]) it.next();
			model.setRequirementDetailId(Long.parseLong(data[0].toString()));
			if (data[1] != null)
				model.setMaterialNo(data[1].toString());
			if (data[2] != null)
				model.setMaterialName(data[2].toString());
			if (data[3] != null)
				model.setMaterSize(data[3].toString());
			if (data[4] != null)
				model.setAppliedQty(Double.parseDouble(data[4].toString()));
			if (data[5] != null)
				model.setApprovedQty(Double.parseDouble(data[5].toString()));
			if (data[6] != null)
				model.setIssQty(Double.parseDouble(data[6].toString()));
			if (data[7] != null)
				model.setEstimatedPrice(Double.parseDouble(data[7].toString()));
			if (data[8] != null)
				model.setEstimatedSum(Double.parseDouble(data[8].toString()));
			if (data[9] != null)
				model.setPurQty(Double.parseDouble(data[9].toString()));
			if (data[10] != null) {
				BpCMeasureUnitFacadeRemote cc = (BpCMeasureUnitFacadeRemote) Ejb3Factory
						.getInstance().getFacadeRemote("BpCMeasureUnitFacade");
				BpCMeasureUnit bp = cc.findById(Long.parseLong(data[10]
						.toString()));
				model.setStockUmName(bp.getUnitName());
			}
			if (data[11] != null)
				model.setUsage(data[11].toString());
			if (data[12] != null)
				model.setMemo(data[12].toString());
			if (data[13] != null)
				model.setNeedDate(data[13].toString());
			if (data[14] != null)
				model.setParameter(data[14].toString());
			if (data[15] != null)
				model.setDocNo(data[15].toString());
			if (data[16] != null)
				model.setWhsName(data[16].toString());
			if (data[17] != null)
				model.setQualityClass(data[17].toString());
			if (data[18] != null)
				model.setLeft(Double.parseDouble(data[18].toString()));
			if (data[19] != null)
				model.setTempNum(Double.parseDouble(data[19].toString()));
			if (data[20] != null)
				model.setItemId(data[20].toString());// modify by ywliu
			// 2009/7/6
			// add by fyyang 090807
			if (data[21] != null)
				model.setCancelReason(data[21].toString());
			if (data[22] != null)
				model.setUseFlag(data[22].toString());
			if (data[23] != null)
				model.setMrDate(data[23].toString());
			if (data[24] != null)
				model.setMrNo(data[24].toString());
			if (data[25] != null)// add by drdu 091106
				model.setWfNo(data[25].toString());
			if (data[26] != null)// add by drdu 091106
				model.setPlanOriginalId(data[26].toString());
			if (data[27] != null)// add by ywliu 091106
				model.setApplyDeptName(data[27].toString());
			if (data[28] != null)// add by jling
				model.setStatus(data[28].toString());
			
			//add by fyyang 20100511
			if (data[29] != null) {
				model.setBuyerBy(data[29].toString());
			}
			if (data[30] != null) {
				model.setBuyerName(data[30].toString());
			}

			// add by liuyi 091104 将要求显示的数据放入model中 采购单 开始
			String sqlCG = "select distinct c.pur_order_details_id,\n"
					+ "  c.pur_no, \n"
					+ " c.pur_qty, \n"
					+ " d.buyer,getworkername(d.buyer), \n"
					+ "  to_char(d.last_modified_date,'yyyy-MM-dd'), \n"
					+ " to_char(d.due_date,'yyyy-MM-dd') \n"
					+ ",GETCLIENTNAME(d.supplier)\n" // add by fyyang 091120
					+ ", c.unit_price \n" // add by fyyang 20100318
					+ "from mrp_j_plan_requirement_detail a , PUR_J_PLAN_ORDER b ,pur_j_order_details c,PUR_J_ORDER d,con_j_clients_info e \n"
					+ "where a.requirement_detail_id =b.requirement_detail_id and c.pur_no = b.pur_no and a.requirement_detail_id = '"
					+ model.getRequirementDetailId()
					+ "'   and  c.pur_no=d.pur_no  and d.supplier=e.cliend_id(+)  \n"
					+ " and a.material_id=c.material_id  and rownum=1 \n";
			List result = bll.queryByNativeSQL(sqlCG);
			Iterator itCG = result.iterator();
			if (itCG.hasNext()) {
				Object[] dataCG = (Object[]) itCG.next();
				if (dataCG[0] != null) {
					model.setOrderDetailsId(dataCG[0].toString());
				}
				if (dataCG[1] != null) {
					model.setPurNo(dataCG[1].toString());
				}
				if (dataCG[2] != null) {
					// modify by ywliu 20091112
					Double purQty = Double.valueOf(dataCG[2].toString());
					if (purQty >= model.getApprovedQty()) {
						model.setPurchaseQuatity(model.getApprovedQty()
								.toString());
					} else {
						model.setPurchaseQuatity(dataCG[2].toString());
					}
				}
				
				if (dataCG[5] != null) {
					model.setBuyTime(dataCG[5].toString());
				}
				if (dataCG[6] != null) {
					model.setDueDate(dataCG[6].toString());
				}
				// add by fyyang 091120
				if (dataCG[7] != null) {
					model.setSupplier(dataCG[7].toString());
				}
				if (dataCG[8] != null) {
					model
							.setUnitPirce(Double.parseDouble(dataCG[8]
									.toString()));
				}
			}
			// 采购单 结束
			// 到货单 开始
			String sqlDH = "select sum(d.rec_qty),sum(d.the_qty) \n"
					+ "from mrp_j_plan_requirement_detail a , PUR_J_PLAN_ORDER b ,pur_j_order_details c ,PUR_J_ARRIVAL_DETAILS d\n"
					+ "where a.requirement_detail_id =b.requirement_detail_id and c.pur_no = b.pur_no and d.pur_no = c.pur_no "
					+ "and a.requirement_detail_id = '"
					+ model.getRequirementDetailId() + "'\n"
					+ " and c.pur_order_details_id=b.pur_order_details_id \n"
					+ " and d.material_id=a.material_id ";
			List listDH = bll.queryByNativeSQL(sqlDH);
			Iterator itDH = listDH.iterator();
			while (itDH.hasNext()) {
				Object[] dataDH = (Object[]) itDH.next();

				if (dataDH[0] != null) {
					// modify by ywliu 20091112
					Double recQty = Double.valueOf(dataDH[0].toString());
					if (recQty >= model.getApprovedQty()) {
						model.setRecQty(model.getApprovedQty().toString());
					} else {
						model.setRecQty(dataDH[0].toString());
					}
				}
				if (dataDH[1] != null) {
					// modify by ywliu 20091112
					Double theQty = Double.valueOf(dataDH[1].toString());
					if (theQty >= model.getApprovedQty()) {
						model.setTheQty(model.getApprovedQty().toString());
					} else {
						model.setTheQty(dataDH[1].toString());
					}

				}
			}
			// 到货单结束
			// 领料单开始
			String sqlLL = "select   sum(i.act_issued_count) \n"
					+ "from mrp_j_plan_requirement_detail a ,INV_J_ISSUE_DETAILS i ,Inv_j_Issue_Head f\n"
					+ "where a.requirement_detail_id = i.requirement_detail_id and f.issue_head_id = i.issue_head_id and a.requirement_detail_id = '"
					+ model.getRequirementDetailId() + "'";
			Object queryObject = bll.getSingal(sqlLL);
			if (queryObject != null) {
				String resultLL = queryObject.toString();
				if (resultLL != null)
					model.setActIssuedCount(resultLL);
			}

			// 领料单结束
			arrlist.add(model);
		}

		pg.setList(arrlist);
		String sqlCount = "select count(*) from (" + sql + ") \n";
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		pg.setTotalCount(totalCount);
		return pg;
	}

	@SuppressWarnings("unchecked")
	public PageObject findByFuzzy(String enterpriseCode, String startDate,
			String endDate, String dept, String mrBy, String maName,
			String maClassName, String status, String planOriginalID,
			int... rowStartIdxAndCount) {
		PageObject result = new PageObject();

		String sql = "select distinct a.  REQUIREMENT_HEAD_ID     ,\n"
				+ "  a.  MR_NO                   ,\n"
				+ "  a.  MR_DATE                 ,\n"
				+ "  a.  WO_NO                   ,\n"
				+ "  a.  PLAN_ORIGINAL_ID        ,\n"
				+ "  a.  ITEM_CODE                     ,\n"// modify by ywliu
				// 2009/7/6
				+ "  a.  MR_BY                       ,\n"
				+ "  GETDEPTNAME(a.MR_DEPT)  as MR_DEPT               ,\n"
				+ "  a.  DUE_DATE                ,\n"
				+ "  a.  PLAN_GRADE              ,\n"
				+ "  a.  COST_DEPT               ,\n"
				+ "  a.  COST_SPECIAL            ,\n"
				+ "  a.  MR_REASON               ,\n"
				+ "  a.  MR_TYPE                 ,\n"
				+ "  a.  WF_NO                       ,\n"
				+ "  a.  MR_STATUS               ,\n"
				+ "  a.  ENTERPRISE_CODE         ,\n"
				+ "  a.  IS_USE                      ,\n"
				+ "  a.  LAST_MODIFIED_BY        ,\n"
				+ "  a.  LAST_MODIFIED_DATE ,     \n"
				+ "   a.PLAN_DATE_MEMO,  \n"
               +" a.prj_no \n";
		String strFromStr = "  from MRP_J_PLAN_REQUIREMENT_HEAD a \n";
		String strWhere = " where a.enterprise_code = '" + enterpriseCode
				+ "'\n" + "  and a.is_use = 'Y'\n";

		if (startDate != null && !"".equals(startDate)) {
			strWhere += "  and to_char(a.MR_DATE,'yyyy-MM-dd') >='" + startDate
					+ "'\n";
		}
		if (endDate != null && !endDate.equals("")) {
			strWhere += "  and to_char(a.MR_DATE,'yyyy-MM-dd') <='" + endDate
					+ "'\n";
		}
		// if (mrBy != null && !mrBy.equals("")) {
		// strWhere += " and a.MR_BY ='" + mrBy + "'\n";
		// } modify by ywliu 20091103
		if (!"999999".equals(mrBy) && mrBy != null && !mrBy.equals("")) {
			strWhere += "  and a.MR_BY ='" + mrBy + "'\n";
		} // modify by ywliu 20091103

		if (maName != null && !"".equals(maName)) {
			strFromStr += " , MRP_J_PLAN_REQUIREMENT_DETAIL b,\n"
					+ " INV_C_MATERIAL c \n";
			strWhere += " and c.MATERIAL_NAME like '%" + maName + "%'\n"
					+ " and b.MATERIAL_ID = c.MATERIAL_ID"
					+ " and a.REQUIREMENT_HEAD_ID = b.REQUIREMENT_HEAD_ID"
					+ " and b.is_use = 'Y'\n" + " and c.is_use = 'Y'\n"
					+ " and b.enterprise_code = '" + enterpriseCode + "'\n"
					+ " and c.enterprise_code = '" + enterpriseCode + "'\n";
		}
		if (maClassName != null && !"".equals(maClassName)) {
			if (maName != null && !"".equals(maName)) { // modify by drdu 090703
				strFromStr += " ,INV_C_MATERIAL_CLASS d\n";
				strWhere += " and d.class_no like '" + maClassName + "%'\n"
						+ " and d.is_use = 'Y'\n"
						+ " and d.enterprise_code = '" + enterpriseCode + "'\n";
			} else {
				strFromStr += " , MRP_J_PLAN_REQUIREMENT_DETAIL b,\n"
						+ " INV_C_MATERIAL c,\n" + " INV_C_MATERIAL_CLASS d\n";
				strWhere += "and d.class_no like '" + maClassName + "%'\n"
						+ " and c.MAERTIAL_CLASS_ID = d.MAERTIAL_CLASS_ID \n"
						+ " and b.MATERIAL_ID = c.MATERIAL_ID"
						+ " and a.REQUIREMENT_HEAD_ID = b.REQUIREMENT_HEAD_ID"
						+ " and b.is_use = 'Y'\n" + " and c.is_use = 'Y'\n"
						+ " and d.is_use = 'Y'\n"
						+ " and b.enterprise_code = '" + enterpriseCode + "'\n"
						+ " and c.enterprise_code = '" + enterpriseCode + "'\n"
						+ " and d.enterprise_code = '" + enterpriseCode + "'\n";
			}
		}
		if (dept != null && !"".equals(dept)) {
			strWhere += " and a.MR_DEPT='" + dept + "'\n";
		}
		// add by fyyang
		if (status != null && !status.equals("")
				&& !"ALL".equalsIgnoreCase(status)) {// modify by ywliu
			// 20091103
			strWhere += "  and a.mr_status='" + status + "'  \n";
		} else if ("ALL".equalsIgnoreCase(status)) {
			// add by ywliu 20091103
			strWhere += "  and a.mr_status in ('0','9')  \n";
		}

		if (planOriginalID != null && !"".equals(planOriginalID)
				&& !"null".equals(planOriginalID)) {
			strWhere += "  and a.plan_original_id='" + planOriginalID + "'  \n";
		}
		// ------
		strWhere += " order by a.REQUIREMENT_HEAD_ID desc";
		sql += strFromStr;
		sql += strWhere;
		String sqlCount = "select count(a.REQUIREMENT_HEAD_ID)\n";
		sqlCount += strFromStr;
		sqlCount += strWhere;
		List<MrpJPlanRequirementHead> arrlist = bll.queryByNativeSQL(sql,
				MrpJPlanRequirementHead.class, rowStartIdxAndCount);
		if (arrlist.size() > 0) {
			for (MrpJPlanRequirementHead temp : arrlist) {

				// modify by fyyang 090511
				if (temp.getMrBy() != null) {
					if (bllEmp.getEmployeeInfo(temp.getMrBy()) != null) {
						temp.setMrBy(bllEmp.getEmployeeInfo(temp.getMrBy())
								.getWorkerName());
					}
				}

			}
			result.setList(arrlist);
			result.setTotalCount(Long.parseLong(bll.getSingal(sqlCount)
					.toString()));
		}
		return result;

	}

}