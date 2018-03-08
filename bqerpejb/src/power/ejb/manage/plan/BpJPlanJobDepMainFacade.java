package power.ejb.manage.plan;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import power.ear.comm.LogUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.plan.form.BpJPlanJobDepMainForm;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

/**
 * Facade for entity BpJPlanJobDepMain.
 * 
 * @see power.ejb.manage.plan.BpJPlanJobDepMain
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class BpJPlanJobDepMainFacade implements BpJPlanJobDepMainFacadeRemote {
	// property constants
	public static final String EDIT_BY = "editBy";
	public static final String EDIT_DEPCODE = "editDepcode";
	public static final String PLAN_STATUS = "planStatus";
	public static final String WORK_FLOW_NO = "workFlowNo";
	public static final String SIGN_STATUS = "signStatus";
	public static final String ENTERPRISE_CODE = "enterpriseCode";

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote dll;
	// @EJB(beanName = "WorkflowServiceImpl")
	// protected WorkflowService ItemRemote;

	WorkflowService service;

	public BpJPlanJobDepMainFacade() {
		service = new WorkflowServiceImpl();
	}

	/**
	 * Perform an initial save of a previously unsaved BpJPlanJobDepMain entity.
	 * All subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            BpJPlanJobDepMain entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	// public BpJPlanJobDepMain save(BpJPlanJobDepMain entity, String deptCode1)
	// {
	// update by sychen 20100408
	public BpJPlanJobDepMain save(BpJPlanJobDepMain entity, String deptCode1,
			String flag, String workflowType, String WorkerCode,
			String signStatus) {
		LogUtil.log("saving BpJPlanJobDepMain instance", Level.INFO, null);
		try {

			if (this.checkDept(deptCode1)) {
				entity.setIfDiversify("Y");
			} else {
				entity.setIfDiversify("N");
			}

			if (this.checkDepthot(deptCode1)) {
				entity.setIfHeating("Y");
			} else {
				entity.setIfHeating("N");
			}
			String id = dll.getMaxId("bp_j_plan_job_dep_main ", "dep_main_id")
					.toString();
			entity.setDepMainId(Long.parseLong(id));
			// add by sychen 20100408
			if (flag != null && flag.equals("approve")) {
				long entryId = service.doInitialize(workflowType, WorkerCode,
						id);
				// update by sychen 20100428
				if (signStatus != null && signStatus.equals("1"))
					service.doAction(entryId, WorkerCode, 24l, "SB", null,
							null, WorkerCode);
				else if (signStatus != null && signStatus.equals("2"))
					service.doAction(entryId, WorkerCode, 25l, "SB2", null,
							null, WorkerCode);
				else if (signStatus != null && signStatus.equals("3"))
					service.doAction(entryId, WorkerCode, 26l, "SB3", null,
							null, WorkerCode);
				else if (signStatus != null && signStatus.equals("4"))
					service.doAction(entryId, WorkerCode, 27l, "SB4", null,
							null, WorkerCode);
				else if (signStatus != null && signStatus.equals("5"))
					service.doAction(entryId, WorkerCode, 28l, "SB5", null,
							null, WorkerCode);
				else if (signStatus != null && signStatus.equals("6"))
					service.doAction(entryId, WorkerCode, 29l, "SB6", null,
							null, WorkerCode);
				else if (signStatus != null && signStatus.equals("7"))
					service.doAction(entryId, WorkerCode, 210l, "SB7", null,
							null, WorkerCode);
				else if (signStatus != null && signStatus.equals("8"))
					service.doAction(entryId, WorkerCode, 211l, "SB8", null,
							null, WorkerCode);
				else if (signStatus != null && signStatus.equals("9"))
					service.doAction(entryId, WorkerCode, 212l, "SB9", null,
							null, WorkerCode);
				else if (signStatus != null && signStatus.equals("10"))
					service.doAction(entryId, WorkerCode, 213l, "SB10", null,
							null, WorkerCode);
				entity.setWorkFlowNo(entryId);
			} else {
				;
			}
			// --------------------//

			entityManager.persist(entity);

			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent BpJPlanJobDepMain entity.
	 * 
	 * @param entity
	 *            BpJPlanJobDepMain entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(BpJPlanJobDepMain entity) {
		LogUtil.log("deleting BpJPlanJobDepMain instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(BpJPlanJobDepMain.class, entity
					.getDepMainId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Persist a previously saved BpJPlanJobDepMain entity and return it or a
	 * copy of it to the sender. A copy of the BpJPlanJobDepMain entity
	 * parameter is returned when the JPA persistence mechanism has not
	 * previously been tracking the updated entity.
	 * 
	 * @param entity
	 *            BpJPlanJobDepMain entity to update
	 * @return BpJPlanJobDepMain the persisted BpJPlanJobDepMain entity
	 *         instance, may not be the same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public BpJPlanJobDepMain update(BpJPlanJobDepMain entity) {
		LogUtil.log("updating BpJPlanJobDepMain instance", Level.INFO, null);
		try {
			BpJPlanJobDepMain result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public BpJPlanJobDepMain findById(Long id) {
		LogUtil.log("finding BpJPlanJobDepMain instance with id: " + id,
				Level.INFO, null);
		try {
			BpJPlanJobDepMain instance = entityManager.find(
					BpJPlanJobDepMain.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all BpJPlanJobDepMain entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the BpJPlanJobDepMain property to query
	 * @param value
	 *            the property value to match
	 * @return List<BpJPlanJobDepMain> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanJobDepMain> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding BpJPlanJobDepMain instance with property: "
				+ propertyName + ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from BpJPlanJobDepMain model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	public List<BpJPlanJobDepMain> findByEditBy(Object editBy) {
		return findByProperty(EDIT_BY, editBy);
	}

	public List<BpJPlanJobDepMain> findByEditDepcode(Object editDepcode) {
		return findByProperty(EDIT_DEPCODE, editDepcode);
	}

	public List<BpJPlanJobDepMain> findByPlanStatus(Object planStatus) {
		return findByProperty(PLAN_STATUS, planStatus);
	}

	public List<BpJPlanJobDepMain> findByWorkFlowNo(Object workFlowNo) {
		return findByProperty(WORK_FLOW_NO, workFlowNo);
	}

	public List<BpJPlanJobDepMain> findBySignStatus(Object signStatus) {
		return findByProperty(SIGN_STATUS, signStatus);
	}

	public List<BpJPlanJobDepMain> findByEnterpriseCode(Object enterpriseCode) {
		return findByProperty(ENTERPRISE_CODE, enterpriseCode);
	}

	/**
	 * Find all BpJPlanJobDepMain entities.
	 * 
	 * @return List<BpJPlanJobDepMain> all BpJPlanJobDepMain entities
	 */
	@SuppressWarnings("unchecked")
	public List<BpJPlanJobDepMain> findAll() {
		LogUtil
				.log("finding all BpJPlanJobDepMain instances", Level.INFO,
						null);
		try {
			final String queryString = "select model from BpJPlanJobDepMain model";
			Query query = entityManager.createQuery(queryString);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find all failed", Level.SEVERE, re);
			throw re;
		}
	}

	// add by sychen 20100423
	@SuppressWarnings("unchecked")
	public PageObject getBpJPlanJobDepMainQuery(String planTime,
			String editDeptcode, String enterpriseCode) throws Exception {
		PageObject pg = new PageObject();
		planTime = planTime.equals("") ? "9000-10" : planTime;
		String strWhere = "";

		if (editDeptcode != null && !editDeptcode.equals("")) {
			strWhere += " and substr(a.edit_depcode,1," + editDeptcode.length()
					+ ")='" + editDeptcode + "'";
		}

		String sql = "SELECT a.dep_main_id,\n"
				+ "       a.plan_time,\n"
				+ "       a.edit_by,\n"
				+ "       getworkername(a.edit_by) editName,\n"
				+ "       to_char(a.edit_date, 'yyyy-mm-dd') edit_date,\n"
				+ "       a.work_flow_no,\n"
				+ "       a.sign_status,\n"
				+ "       b.job_id,\n"
				+ "       b.job_content,\n"
				+ "       b.complete_data,\n"
				+ "       a.edit_depcode,\n"
				+ "       getdeptname(a.edit_depcode) deptName,\n"
				+ "       b.charge_by,\n"
				+ "       getworkername(b.charge_by) chargeName,\n"
				+ "       b.order_by,\n"
				+ "       c.dept_id,\n"
				+ "       (SELECT getdeptname(t.dept_code)\n"
				+ "          FROM hr_c_dept t\n"
				+ "         WHERE t.dept_level = 1\n"
				+ "        AND rownum = 1\n"
				+ "         START WITH t.dept_id = c.dept_id\n"
				+ "        CONNECT BY PRIOR t.pdept_id = t.dept_id)level1DeptName\n"
				+ "  FROM bp_j_plan_job_dep_main   a,\n"
				+ "       bp_j_plan_job_dep_detail b,\n"
				+ "       hr_c_dept                c\n"
				+ " WHERE a.dep_main_id = b.dep_main_id\n"
				+ "   AND a.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   AND b.enterprise_code = '" + enterpriseCode + "'\n"
				+ "   AND c.dept_code = a.edit_depcode\n"
				+ "   AND a.plan_time = to_date('" + planTime
				+ "' || '-01', 'yyyy-MM-dd ')\n";

		if (strWhere != null && !strWhere.equals("")) {
			sql += strWhere;
		}
		sql += "  order by a.edit_depcode,b.job_id";
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = dll.queryByNativeSQL(sql);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(dll.getSingal(sqlCount).toString()));
		return pg;
	}

	@SuppressWarnings("unchecked")
	// public BpJPlanJobDepMainForm getBpJPlanJobDepMain(String approve,
	// String entryIds, String planDate, String editBy,
	// String enterpriseCode) {
	// update by sychen 20100414
	public BpJPlanJobDepMainForm getBpJPlanJobDepMain(String approve,
			String flag, String entryIds, String planDate, String editBy,
			String editDeptcode, String enterpriseCode) {
		planDate = planDate.equals("") ? "9000-10" : planDate;
		String sql = "select s.*,getworkername(s.edit_by) editbyname,getdeptname(s.edit_depcode) "
				+ "deptname "
				+ " from bp_j_plan_job_dep_main s"
				+ " where s.plan_time="
				+ "to_date('"
				+ planDate
				+ "'||'-01','yyyy-MM-dd ') "
				// + "and s.sign_status <>2"
				+ " and s.enterprise_code='" + enterpriseCode + "'";
		if (approve != null && approve.equals("approve")) {
			sql += " and s.work_flow_no in (" + entryIds + ")";
		} else if (approve != null && approve.equals("init")) {
			// 初始化
		} // add by sychen 20100414
		else if (flag != null && flag.equals("query")) {
			if (editDeptcode != null && !editDeptcode.equals("")) {
				sql += " and substr(s.edit_depcode,1," + editDeptcode.length()
						+ ")='" + editDeptcode + "'";
			}
			// modified by liuyi 20100423
			// if (editBy != null && !editBy.equals("")) {
			// sql += " and s.edit_by='" + editBy + "'";
			// }
		} else {
			sql += " and s.edit_by='" + editBy + "'";
		}

		List list = dll.queryByNativeSQL(sql);
		BpJPlanJobDepMainForm model = new BpJPlanJobDepMainForm();
		BpJPlanJobDepMain baseInfo = new BpJPlanJobDepMain();
		if (list.size() > 0) {
			Object[] data = (Object[]) list.get(0);

			if (data[0] != null) {
				baseInfo.setDepMainId(Long.parseLong(data[0].toString()));
			}
			// if(data[1]!=null){
			// baseInfo.setDepMainId(data[0].toString());
			// }
			// if(data[2]!=null){
			// model.setEditDate(data[0].toString());
			// }
			if (data[3] != null) {
				model.setEditDate(data[3].toString());
			}
			// if(data[4]!=null){
			// baseInfo.setDepMainId(data[0].toString());
			// }
			// if (data[5] != null) {
			// baseInfo.setPlanStatus(Long.parseLong(data[5].toString()));
			// }
			if (data[7] != null) {
				baseInfo.setWorkFlowNo(Long.parseLong(data[7].toString()));
			}
			if (data[8] != null) {
				baseInfo.setSignStatus(Long.parseLong(data[8].toString()));
			}
			// --------update by sychen 20100328------------//
			// if (data[11] != null) {
			// baseInfo.setIfEquApproval(data[11].toString());
			// }
			if (data[13] != null) {
				baseInfo.setIfDiversify(data[13].toString());
			}
			if (data[14] != null) {
				baseInfo.setIfHeating(data[14].toString());
			}
			// ---------------------------------------------//
			if (data[18] != null) {
				model.setEditByName(data[18].toString());
			}
			if (data[19] != null) {
				model.setEditDepName(data[19].toString());
			}

			model.setBaseInfo(baseInfo);
		}
		return model;

	}

	// add by sychen 20100406
	@SuppressWarnings("unchecked")
	public PageObject getBpJPlanJobDepMainApprove(String approve,
			String entryIds, String planDate, String editBy,
			String enterpriseCode) throws Exception {
		PageObject pg = new PageObject();
		planDate = planDate.equals("") ? "9000-10" : planDate;

		String sql = "select s.dep_main_id,\n" + "s.plan_time,\n"
				+ "s.edit_by,\n"
				+ "to_char(s.edit_date,'yyyy-mm-dd')edit_date,\n"
				+ "s.edit_depcode,\n" + "s.finish_edit_by,\n"
				+ "to_char(s.finish_edit_date,'yyyy-mm-dd')finish_edit_date,\n"
				+ "s.work_flow_no,\n" + "s.sign_status,\n"
				+ "s.finish_sign_status,\n" + "s.finish_sign_status,\n"
				+ "s.if_diversify,\n" + "s.if_heating,\n"
				+ "getworkername(s.edit_by) editbyname,\n"
				+ "getdeptname(s.edit_depcode)deptname\n"
				+ " from bp_j_plan_job_dep_main s\n" + " where s.plan_time="
				+ "to_date('" + planDate + "'||'-01','yyyy-MM-dd ') \n"
				+ " and s.enterprise_code='" + enterpriseCode + "'\n";
		if (approve != null && approve.equals("approve")) {
			if (entryIds != null) {
				String inEntriyId = dll.subStr(entryIds, ",", 500,
						"s.work_flow_no");
				sql += " and " + inEntriyId;
			} else
				sql += " and s.work_flow_no =null";
			sql += "  order by s.edit_depcode";
		} else {
			sql += " and s.edit_by='" + editBy + "'"
					+ "  order by s.dep_main_id desc";
		}

		List list = dll.queryByNativeSQL(sql);
		pg.setList(list);
		return pg;
	}

	// add by sychen 20100407
	@SuppressWarnings("unchecked")
	public List getBpJPlanJobDept() {
		String sql = "SELECT DISTINCT t.edit_depcode,\n"
				+ "                getdeptname(t.edit_depcode) deptName\n"
				+ "  FROM Bp_j_Plan_Job_Dep_Main t\n"
				+ " ORDER BY t.edit_depcode";

		List list = dll.queryByNativeSQL(sql);
		return list;
	}

	// 获得部门汇总审批人 add by sychen 20100504
	@SuppressWarnings("unchecked")
	// public List getBpJPlanJobDepMainCaller(String entryId){
	//		 
	// String sql ="SELECT t.caller,\n" +
	// " getworkername(t.caller) callerName\n" +
	// " FROM wf_j_historyoperation t\n" +
	// " WHERE t.entry_id = '"+entryId+"'\n" +
	// " AND t.step_id IN (4)";
	// List list = dll.queryByNativeSQL(sql);
	//			
	// return list;
	// }
	public String getBpJPlanJobDepMainCaller(String entryId) {

		String sql = "SELECT t.caller,\n"
				+ "       getworkername(t.caller) callerName\n"
				+ "  FROM wf_j_historyoperation t\n" + " WHERE t.entry_id = '"
				+ entryId + "'\n" + "   AND t.step_id IN (4)";

		List list = dll.queryByNativeSQL(sql);
		String caller = "";
		for (int i = 0; i < list.size(); i++) {
			Object[] data = (Object[]) list.get(i);
			if (data[0] != null && data[1] != null) {
				caller = data[0].toString() + "," + data[1].toString();
				break;
			}

		}
		return caller;
	}

	public String getPlanJobDeptMainId(String planTime, String editDepcode,
			String workerCode, String enterpriseCode, String method,
			String depMainId) {

		String sql = "select t.dep_main_id from bp_j_plan_job_dep_main t\n"
				+ " where t.plan_time=to_date('" + planTime
				+ "'||'-01','yyyy-MM-dd ') \n" + " and t.edit_by='"
				+ workerCode + "'\n" + " and t.edit_depcode='" + editDepcode
				+ "'\n" + " and t.enterprise_code='" + enterpriseCode + "'";

		Object maxNoObj = dll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}

	public String getPlanJobDeptNewMainId(String planTime, String workerCode,
			String enterpriseCode) {

		String sql = "select t.dep_main_id from bp_j_plan_job_dep_main t\n"
				+ " where t.plan_time=to_date('" + planTime
				+ "'||'-01','yyyy-MM-dd ') \n" + " and t.edit_by='"
				+ workerCode + "'\n" + " and t.sign_status in(0,12)\n"
				+ " and t.enterprise_code='" + enterpriseCode + "'";

		Object maxNoObj = dll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}

	public String getBpJPlanJobDeptMainId(String editBy, String planTime,
			String enterpriseCode) {
		String sql = "SELECT t.dep_main_id\n"
				+ "  FROM Bp_j_Plan_Job_Dep_Main t\n" + " WHERE t.edit_by = '"
				+ editBy + "'\n" + "   AND t.plan_time=" + "to_date('"
				+ planTime + "'||'-01','yyyy-MM-dd ') \n"
				+ "   AND t.enterprise_code = '" + enterpriseCode + "'";

		Object maxNoObj = dll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}

	@SuppressWarnings("unchecked")
	public PageObject getBpJPlanJobDepMainList(String finish, String planDate,
			String enterpriseCode,int... rowStartIdxAndCount) {//modify by wpzhu 20100602  按照一级部门中排序号进行排序
		String sql = "select s.*,getworkername(s.edit_by) editbyname,\n"
				+"  getdeptname(s.edit_depcode) deptname ,\n " 
				+"  getdeptname(GETFirstLevelBYID(c.dept_id))," 
				+	"d.order_by "
				+ " from bp_j_plan_job_dep_main s,\n"
				+ "       hr_c_dept                c, \n" 
				+ "       bp_c_plan_job_dept d"
				+ " where c.dept_code = s.edit_depcode\n" 
				+" and s.plan_time="
				+ "to_date('"
				+ planDate
				+ "'||'-01','yyyy-MM-dd ') "
				+ " and s.enterprise_code='" + enterpriseCode + "'\n " 
				+"  and GETFirstLevelBYID(c.dept_id)=d.dept_code(+)";
		//update by sychen 20100628
		
//		String sqlCount = "select count(*) " + " from bp_j_plan_job_dep_main s"
//				+ " where s.plan_time=" + "to_date('" + planDate
//				+ "'||'-01','yyyy-MM-dd ') " + " and s.enterprise_code='"
//				+ enterpriseCode + "'  ";
		
		String sqlCount = "select count(*) " + " from bp_j_plan_job_dep_main s,\n"
		+ "       hr_c_dept                c, \n" 
		+ "       bp_c_plan_job_dept d"
		+ " where c.dept_code = s.edit_depcode\n" 
		+" and s.plan_time="
		+ "to_date('"
		+ planDate
		+ "'||'-01','yyyy-MM-dd ') "
		+ " and s.enterprise_code='" + enterpriseCode + "'\n " 
		+"  and GETFirstLevelBYID(c.dept_id)=d.dept_code(+)";
		//update by sychen 20100628 end
		if (finish != null && finish.equals("finish")) {
			sql += "and s.SIGN_STATUS= 11";
			sqlCount += "and s.SIGN_STATUS= 11";
		}
		if (finish != null && finish.equals("finish1")) {
			sql += "and s.FINISH_SIGN_STATUS= 11";
			sqlCount += "and s.FINISH_SIGN_STATUS= 11";
		}
		sql+="order by d.order_by";
//		System.out.println("the sql"+sql);
		List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
		BpJPlanJobDepMainForm model = new BpJPlanJobDepMainForm();
		BpJPlanJobDepMain baseInfo = new BpJPlanJobDepMain();
		Iterator it = list.iterator();
		PageObject object = new PageObject();
		List<BpJPlanJobDepMainForm> arraylist = new ArrayList<BpJPlanJobDepMainForm>();
		while (it.hasNext()) {
			model = new BpJPlanJobDepMainForm();
			baseInfo = new BpJPlanJobDepMain();

			Object[] data = (Object[]) it.next();
			if (data[0] != null) {
				baseInfo.setDepMainId(Long.parseLong(data[0].toString()));
			}
			// if(data[1]!=null){
			// baseInfo.setDepMainId(data[0].toString());
			// }
			// if(data[2]!=null){
			// model.setEditDate(data[0].toString());
			// }
			if (data[3] != null) {
				model.setEditDate(data[3].toString());
			}
			// if(data[4]!=null){
			// baseInfo.setDepMainId(data[0].toString());
			// }
			// if(data[5]!=null){
			// baseInfo.setPlanStatus(Long.parseLong(data[5].toString()));
			// }
			// if(data[6]!=null){
			// baseInfo.setDepMainId(data[0].toString());
			// }
			if (data[7] != null) {
				baseInfo.setWorkFlowNo(Long.parseLong(data[7].toString()));
			}
			if (data[8] != null) {
				baseInfo.setSignStatus(Long.parseLong(data[8].toString()));
			}
			if (data[10] != null) {
				baseInfo.setFinishSignStatus(Long
						.parseLong(data[10].toString()));
			}
			// update by sychen 20100408
			if (data[16] != null)
				baseInfo.setIfRead(data[16].toString());
			if (data[17] != null)
				baseInfo.setFinishIfRead(data[17].toString());

			if (data[18] != null) {
				model.setEditByName(data[18].toString());
			}
			if (data[19] != null) {
				model.setEditDepName(data[19].toString());
			}
			if (data[20] != null) {
				model.setLevel1DeptName(data[20].toString());
			}

			model.setBaseInfo(baseInfo);
			arraylist.add(model);
		}

		Long totalCount = Long.parseLong(dll.getSingal(sqlCount).toString());
		object.setList(arraylist);
		object.setTotalCount(totalCount);
		return object;

	}

	// 审批 nextRoles 代表下步审批人
	@SuppressWarnings("unchecked")
	private void changeWfInfo(Long entryId, String workerCode, Long actionId,
			String approveText, String nextRoles, String isEqu) {
		boolean SB = false;
		if (isEqu != null && isEqu.equals("Y")) {
			SB = true;
		}
		Map m = new java.util.HashMap();
		m.put("SB", SB);
		service.doAction(entryId, workerCode, actionId, approveText, m, "",
				nextRoles);
	}

	// 审批步骤：立项 上报
	// 对应信息：编辑页面点“上报”→4
	// nextRoles 代表下步审批人（角色）
	// nextRolePs 代表下步审批人（人员）
	// public BpJPlanJobDepMain reportTo(String prjNo, String workflowType,
	// String workerCode, String actionId, String approveText,
	// String nextRoles, String enterpriseCode, String isEqu) {
	// update by sychen 20100327
	public BpJPlanJobDepMain reportTo(String prjNo, String workflowType,
			String workerCode, String actionId, String approveText,
			String nextRolePs, String nextRoles, String enterpriseCode) {
		BpJPlanJobDepMain entity = findById(Long.parseLong(prjNo));
		try {

			if (entity.getWorkFlowNo() == null) {
				// 处理未上报
				if (!workflowType.equals("")) {
					long entryId = service.doInitialize(workflowType,
							workerCode, prjNo);
					long actionIdl = Long.parseLong(actionId);
					// service.doAction(entryId, workerCode, actionIdl,
					// approveText, null, "", nextRoles);
					// update by sychen 20100327
					service.doAction(entryId, workerCode, actionIdl,
							approveText, null, nextRoles, nextRolePs);
					entity.setWorkFlowNo(entryId);
					entity.setSignStatus(1l);
					// entity.setIfEquApproval(isEqu);
					// entity.setPlanStatus(1L);
				}
			} else {
				// 处理已退回
				long entryId = service.doInitialize(workflowType, workerCode,
						entity.getDepMainId().toString());
				long actionIdl = Long.parseLong(actionId);
				// service.doAction(entryId, workerCode, actionIdl, approveText,
				// null, "", nextRoles);
				// update by sychen 20100327
				service.doAction(entryId, workerCode, actionIdl, approveText,
						null, nextRoles, nextRolePs);
				entity.setWorkFlowNo(entryId);
				entity.setSignStatus(1l);
				// entity.setIfEquApproval(isEqu);
				// entity.setPlanStatus(1L);
			}
			// entity.setAccSignStartDate(new Date());
			update(entity);

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
	 * 
	 * 
	 * 
	 */

	// 立项审批
	@SuppressWarnings("unchecked")
	public BpJPlanJobDepMain approveStep(String prjNo, String workflowType,
			String workerCode, String actionId, String eventIdentify,
			String approveText, String nextRoles, String stepId,
			String enterpriseCode) {
		// update by sychen 20100327
		BpJPlanJobDepMain entity = findById(Long.parseLong(prjNo));
		try {
			if (eventIdentify.equals("TH")) {
				entity.setSignStatus(12l);
			} else {
				if (entity.getSignStatus() == 1) {
					if (eventIdentify.equals("TY")) {
						entity.setSignStatus(2l);
					} else if (eventIdentify.equals("TY（XSBBSP）")) {
						entity.setSignStatus(6l);
					} else if (eventIdentify.equals("TY（ZZGCLD）")) {
						entity.setSignStatus(10l);
					}
				} else if (entity.getSignStatus() == 2) {
					entity.setSignStatus(3l);
				} else if (entity.getSignStatus() == 3) {
					entity.setSignStatus(4l);
				} else if (entity.getSignStatus() == 4) {
					entity.setSignStatus(5l);
				} else if (entity.getSignStatus() == 5) {
					// if(eventIdentify.equals("TY（ZSCJHHZ）"))
					// {
					// entity.setSignStatus(7l);
					// }
					// else if(eventIdentify.equals("TY")){
					// entity.setSignStatus(11l);
					// }
					// if(eventIdentify.equals("TY（ZSCJHHZ）"))
					// {
					// entity.setSignStatus(7l);
					// }

					// modify by fyyang 20100525
					if (eventIdentify.equals("TY")) {
						if (entity.getIfHeating().equals("Y")) {
							entity.setSignStatus(7l);
							actionId = "89";
						} else {
							entity.setSignStatus(11l);
							actionId = "83";
						}
					}
				} else if (entity.getSignStatus() == 6) {
					entity.setSignStatus(7l);
				} else if (entity.getSignStatus() == 7) {
					entity.setSignStatus(8l);
				} else if (entity.getSignStatus() == 8) {
					if (eventIdentify.equals("TY（XSCCZSP）")) {
						entity.setSignStatus(9l);
					} else if (eventIdentify.equals("TY（SPJS）")) {
						entity.setSignStatus(11l);
					}
				} else if (entity.getSignStatus() == 9) {
					entity.setSignStatus(11l);
				} else if (entity.getSignStatus() == 10) {
					entity.setSignStatus(11l);
				}
			}
			boolean checkD = true;
			boolean checkH = true;
			if (entity.getIfDiversify() != null
					&& entity.getIfDiversify().equals("N"))
				checkD = false;
			if (entity.getIfHeating() != null
					&& entity.getIfHeating().equals("N"))
				checkH = false;
			Map m = new java.util.HashMap();
			m.put("IF_DIVERSIFY", checkD);
			m.put("IF_HEATING", checkH);
			WorkflowService service = new WorkflowServiceImpl();
			service.doAction(entity.getWorkFlowNo(), workerCode, Long
					.parseLong(actionId), approveText, m, "", nextRoles);
			entityManager.merge(entity);

			// BpJPlanJobDepMain model = findById(Long.parseLong(prjNo));
			// long actionIdl = Long.parseLong(actionId);
			// this.changeWfInfo(model.getWorkFlowNo(), workerCode, actionIdl,
			// approveText, nextRoles, model.getIfEquApproval());
			// model.setSignStatus(getapproveStatus(stepId, actionId));
			// // model.setPlanStatus(getplanStatus(actionId));
			// update(model);

		} catch (Exception e) {

		}
		return entity;
	}

	private Long getapproveStatus(String stepID, String actionId) {

		switch (Integer.parseInt(actionId)) {
		case 42:
			return 3l;
		case 52:
			return 3l;
		case 62:
			return 3l;

		case 63:
			return 2l;

		default:
			return 1l;
		}

	}

	private Long getplanStatus(String actionId) {

		switch (Integer.parseInt(actionId)) {
		case 42:
			return 1l;

		case 43:
			return 2l;

		default:
			return 0l;
		}

	}

	@SuppressWarnings("unchecked")
	public PageObject getBpPlanDeptModList(String approve, String entryIds,
			String planDate, String enterpriseCode, int... rowIndexAndCount) {
		PageObject pg = new PageObject();
		String sql = "select to_char(a.plan_time,'yyyy-mm'),\n"
				+ " getworkername(a.edit_by),\n"
				+ " to_char(a.edit_date,'yyyy-mm-dd'),\n"
				+ " b.dep_main_id,\n"
				+ " getdeptname(b.edit_depcode),\n"
				+ " c.job_id,\n"
				+ " c.job_content,\n"
				+ " c.complete_data,\n"
				+ " b.work_flow_no,\n"
				+ " decode(c.complete_data,0,'当月',1,'跨越','2','长期',3,'全年')\n"
				+ " from BP_J_PLAN_JOB_MAIN a,BP_J_PLAN_JOB_DEP_MAIN b,BP_J_PLAN_JOB_DEP_DETAIL c\n"
				+ " where a.plan_time=b.plan_time\n"
				+ " and c.dep_main_id=b.dep_main_id\n"
				+ " and a.enterprise_code='" + enterpriseCode + "'\n"
				+ " and b.enterprise_code='" + enterpriseCode + "'\n"
				+ " and c.enterprise_code='" + enterpriseCode + "'";

		if (planDate != null && !planDate.equals(""))
			sql += " and to_char(a.plan_time,'yyyy-mm')='" + planDate + "' \n";

		if (approve != null && approve.equals("Y"))
			sql += "  and a.work_flow_no in (" + entryIds + ") \n";

		// 部门计划查审批结束
		if (approve != null && approve.equals("signStatus")) {
			sql += "  and b.sign_status='2' \n";
		} else {

		}
		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = dll.queryByNativeSQL(sql, rowIndexAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(dll.getSingal(sqlCount).toString()));
		return pg;
	}

	@SuppressWarnings("unchecked")
	// 应该是显示当前月份下责任人下多个主表记录 add by sychen 20100505
	public PageObject getBpPlanStatusByChargeBy(String deptH, String planTime,
			String entryIds) throws Exception {
		PageObject pg = new PageObject();

		String sql = "SELECT distinct t.dep_main_id,\n"
				+ "       t.edit_by,\n"
				+ "       t.edit_depcode,\n"
				+ "       getdeptname(t.edit_depcode),\n"
				+ "       getworkername(t.edit_by),\n"
				+ "       t.finish_edit_by,\n"
				+ "       to_char(t.finish_edit_date, 'yyyy-mm-dd'),\n"
				+ "       to_char(t.EDIT_DATE, 'yyyy-mm'),\n"
				+ "       t.sign_status,\n"
				+ "       t.finish_work_flow_no,\n"
				+ "       t.finish_sign_status,\n"
				+ "       t.if_equ_approval,\n"
				+ "       t.if_diversify,\n"
				+ "       t.if_heating,\n"
				+ "       t.if_read,\n"
				+ "       t.finish_if_read\n"
				+ "  FROM BP_J_PLAN_JOB_DEP_MAIN t,bp_j_plan_job_dep_detail b\n"
				+ " WHERE to_char(t.plan_time,'yyyy-mm')='" + planTime + "' \n"
				+ "   AND t.dep_main_id=b.dep_main_id"
				// update by sychen 20100613
				+ "   AND (t.sign_status=11 or t.sign_status is null)";
//				+ "   AND t.sign_status=11";

		if (deptH != null && !deptH.equals("")) {
			sql += " and (t.finish_sign_status is null or t.finish_sign_status=0 or t.finish_sign_status=12) and b.charge_by='"
					+ deptH + "'";
		} else {
			sql += " and t.finish_work_flow_no in (" + entryIds + ")";
		}

		List list = dll.queryByNativeSQL(sql);
		pg.setList(list);
		return pg;
	}

	public List getBpPlanStatusByDeptCode(String deptH, String planTime,
			String entryIds) {
		String sql =
		// "select to_char(a.plan_time,'yyyy-mm'),\n" +
		// " b.dep_main_id,\n" +
		// " b.edit_depcode,\n" +
		// " getdeptname(b.edit_depcode),\n" +
		// " b.finish_edit_by,\n" +
		// " getworkername(b.finish_edit_by),\n" +
		// " to_char(b.finish_edit_date,'yyyy-mm-dd'),\n" +
		// " b.sign_status,\n" +
		// " b.finish_work_flow_no,\n" +
		// " b.finish_sign_status,b.if_equ_approval \n" +
		// " from BP_J_PLAN_JOB_MAIN a,BP_J_PLAN_JOB_DEP_MAIN b\n" +
		// " where a.plan_time=b.plan_time\n" +
		//			
		// " and to_char(a.plan_time,'yyyy-mm')='" +planTime+"'\n";
		// " and b\n";
		// +
		"select t.dep_main_id,t.edit_by, t.edit_depcode,getdeptname(t.edit_depcode),"
				+ " getworkername(t.edit_by)," + "	t.finish_edit_by,"
				+ " to_char(t.finish_edit_date,'yyyy-mm-dd'),"
				+ "  to_char(t.EDIT_DATE,'yyyy-mm'),\n "
				+ " t.sign_status, "
				+ " t.finish_work_flow_no,"
				+ " t.finish_sign_status,t.if_equ_approval,"
				+ " t.if_diversify,t.if_heating, "
				+ " t.if_read,t.finish_if_read "// add by sychen 20100408
				+ "  from BP_J_PLAN_JOB_DEP_MAIN  t " + " where  "
				// + " t.edit_by='" + deptH + "'\n"
				+ "  to_char(t.plan_time,'yyyy-mm')='" + planTime + "' \n"
				// update by sychen 20100613
				+ " and (t.sign_status='11' or t.sign_status is null)";
//				+ " and t.sign_status='11'";
		if (deptH != null && !deptH.equals("")) {
			sql += " and (t.finish_sign_status is null or t.finish_sign_status=0 or t.finish_sign_status=12) and t.edit_by='"
					+ deptH + "'";
		} else {
			sql += " and t.finish_work_flow_no in (" + entryIds + ")";
		}
		return dll.queryByNativeSQL(sql);

	}

	@SuppressWarnings("unchecked")
	public PageObject getPlanJobCompleteDetialList(String isApprove,
			String entryIds, String deptMainId, String enterpriseCode,
			String chargeBy, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select a.job_id,\n"
				+ "       a.dep_main_id,\n"
				+ "       a.job_content,\n"
				+ "       a.if_complete,\n"
				+ "       a.complete_desc,\n"
				+ "       a.complete_data,\n"
				+ "       b.edit_depcode,\n"
				+
				// modified by liuyi 20100525
				// " getdeptname(b.edit_depcode) deptName,\n" + //add by sychen
				// 20100408
				" (select  t.dept_name from hr_c_dept t where t.dept_level=1 and rownum = 1 start with t.dept_code=b.edit_depcode connect by prior t.pdept_id=t.dept_id) deptName, "
				+ "       getworkername(b.finish_edit_by) finishEditbyname,\n"
				+ "       to_char(b.finish_edit_date, 'yyyy-mm-dd')finish_edit_date,\n"
				+ "       getworkername(b.edit_by) editbyname,\n"
				+ // add by sychen 20100505
				"       to_char(b.edit_date, 'yyyy-mm-dd')edit_date,\n"
				+ // add by sychen 20100505
				"       b.finish_work_flow_no,\n"
				+ // add by sychen 20100505
				"       b.finish_sign_status\n"
				+ // add by sychen 20100505
				" ,a.order_by \n"
				+ // add by sychen 20100613
				" ,b.sign_status \n"
				+ // add by liuyi 2010525
				" from BP_J_PLAN_JOB_DEP_DETAIL a,BP_J_PLAN_JOB_DEP_MAIN b\n"
				+ " where a.enterprise_code='" + enterpriseCode + "'\n"
				+ " and a.dep_main_id=b.dep_main_id";

		if (deptMainId != null && !deptMainId.equals(""))
			sql += " and b.dep_main_id in (" + deptMainId + ")\n";
		if (isApprove != null && isApprove.equals("Y"))
			sql += " and b.finish_work_flow_no in (" + entryIds + ")";
		// add by liuyi 20100525
		else if (isApprove != null && isApprove.equals("gather")) {
			;
		}
		// update by sychen 20100525
		else {
			if (chargeBy != null && !chargeBy.equals("")) {
				sql += " and a.charge_by = '" + chargeBy + "'";
			}
		}
        sql += "order by a.order_by";
		String sqlCount = "select count(*) from (" + sql + ")";
		List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(dll.getSingal(sqlCount).toString()));
		return pg;

	}

	public void reportDeptPlanComplete(Long deptMainId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String nextRolePs, String workflowType) {
		BpJPlanJobDepMain model = entityManager.find(BpJPlanJobDepMain.class,
				deptMainId);
		Long entryId;
		if (model.getFinishWorkFlowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, deptMainId
					.toString());
			model.setFinishWorkFlowNo(entryId);
		} else {
			entryId = model.getFinishWorkFlowNo();
		}
		// update by sychen 20100329
		// service.doAction(entryId, workerCode, actionId, approveText, null,
		// "",
		// nextRoles);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, nextRolePs);
		model.setFinishSignStatus(1l);
		entityManager.merge(model);

	}

	@SuppressWarnings("unchecked")
	public BpJPlanJobDepMain deptPlanCompleteApprove(String deptMainId,
			String workflowType, String workerCode, String actionId,
			String eventIdentify, String approveText, String nextRoles,
			String stepId, String enterpriseCode) {

		// update by sychen 20100329
		BpJPlanJobDepMain entity = findById(Long.parseLong(deptMainId));
		try {

			if (eventIdentify.equals("TH")) {
				entity.setFinishSignStatus(12l);
			} else {
				if (entity.getFinishSignStatus() == 1) {
					if (eventIdentify.equals("TY")) {
						entity.setFinishSignStatus(2l);
					} else if (eventIdentify.equals("TY（XSBBSP）")) {
						entity.setFinishSignStatus(6l);
					} else if (eventIdentify.equals("TY（ZZGCLD）")) {
						entity.setFinishSignStatus(10l);
					}
				} else if (entity.getFinishSignStatus() == 2) {
					entity.setFinishSignStatus(3l);
				} else if (entity.getFinishSignStatus() == 3) {
					entity.setFinishSignStatus(4l);
				} else if (entity.getFinishSignStatus() == 4) {
					entity.setFinishSignStatus(5l);
				} else if (entity.getFinishSignStatus() == 5) {
//					if (eventIdentify.equals("TY（ZSCJHHZ）")) {
//						entity.setFinishSignStatus(7l);
//					} else if (eventIdentify.equals("TY")) {
//						entity.setFinishSignStatus(11l);
//					}
					//modify by fyyang 20100525
					if(eventIdentify.equals("TY")){
					if(entity.getIfHeating().equals("Y"))
					{
						entity.setFinishSignStatus(7l);
						actionId="89";
					}
					else
					{
					   entity.setFinishSignStatus(11l);
					   actionId="83";
					}
					}
				} else if (entity.getFinishSignStatus() == 6) {
					entity.setFinishSignStatus(7l);
				} else if (entity.getFinishSignStatus() == 7) {
					entity.setFinishSignStatus(8l);
				} else if (entity.getFinishSignStatus() == 8) {
					if (eventIdentify.equals("TY（XSCCZSP）")) {
						entity.setFinishSignStatus(9l);
					} else if (eventIdentify.equals("TY（SPJS）")) {
						entity.setFinishSignStatus(11l);
					}
				} else if (entity.getFinishSignStatus() == 9) {
					entity.setFinishSignStatus(11l);
				} else if (entity.getFinishSignStatus() == 10) {
					entity.setFinishSignStatus(11l);
				}
			}
			boolean checkD = true;
			boolean checkH = true;
			if (entity.getIfDiversify() != null
					&& entity.getIfDiversify().equals("N"))
				checkD = false;
			if (entity.getIfHeating() != null
					&& entity.getIfHeating().equals("N"))
				checkH = false;
			Map m = new java.util.HashMap();
			m.put("IF_DIVERSIFY", checkD);
			m.put("IF_HEATING", checkH);
			WorkflowService service = new WorkflowServiceImpl();
			service.doAction(entity.getFinishWorkFlowNo(), workerCode, Long
					.parseLong(actionId), approveText, m, "", nextRoles);
			entityManager.merge(entity);
		} catch (Exception e) {

		}
		return entity;

		// BpJPlanJobDepMain model = findById(Long.parseLong(deptMainId));
		// try {
		// long actionIdl = Long.parseLong(actionId);
		// this
		// .changeWfInfo(model.getFinishWorkFlowNo(), workerCode,
		// actionIdl, approveText, nextRoles, model
		// .getIfEquApproval());
		// model.setFinishSignStatus(getapproveStatus(stepId, actionId));
		// // model.setPlanStatus(getplanStatus(actionId));
		// update(model);
		//
		// } catch (Exception e) {
		//
		// }
		// return model;
	}

	public void updateMainreportTo(Long depMainId) {
		String sql = "update bp_j_plan_job_dep_main a set a.sign_status = '13' where a.dep_main_id = "
				+ depMainId + "";
		dll.exeNativeSQL(sql);
	}

	public void updateMainIfReadRecord(Long depMainId) {
		String sql = "update bp_j_plan_job_dep_main a set a.if_read = 'Y' where a.dep_main_id = "
				+ depMainId + "";
		dll.exeNativeSQL(sql);
	}

	public void updateMainFinishIfReadRecord(Long depMainId) {
		String sql = "update bp_j_plan_job_dep_main a set a.finish_if_read = 'Y' where a.dep_main_id = "
				+ depMainId + "";
		dll.exeNativeSQL(sql);
	}

	@SuppressWarnings("unchecked")
	public PageObject getPlanCompleteAllQuery(String isApprove,
			String entryIds, String planTime, String enterpriseCode,
			int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql = "select to_char(a.plan_time,'yyyy-mm'),\n"
				+ " a.finish_work_flow_no,\n"
				+ " a.finish_sign_status,\n"
				+ " b.edit_depcode,\n"
				+ " getdeptname(b.edit_depcode),\n"
				+ " c.job_id,\n"
				+ " c.job_content,\n"
				+ " decode(c.if_complete,0,'未完成',1,'进行中','2','已完成'),\n"
				+ // modify by drdu 20100104
				" c.complete_desc,\n"
				+ " decode(c.complete_data,0,'当月',1,'跨越','2','长期',3,'全年')\n"
				+ // modify by drdu 20100104
				" from BP_J_PLAN_JOB_MAIN a,BP_J_PLAN_JOB_DEP_MAIN b,BP_J_PLAN_JOB_DEP_DETAIL c\n"
				+ " where a.plan_time=b.plan_time\n" +
				// " and a.finish_sign_status is not null\n" +
				" and b.dep_main_id=c.dep_main_id" + "  and b.sign_status ='2'";

		if (planTime != null && !planTime.equals(""))
			sql += " and to_char(a.plan_time,'yyyy-mm')='" + planTime + "'\n";

		if (isApprove != null && isApprove.equals("Y"))
			sql += "and a.finish_sign_status is not null and a.finish_work_flow_no in ("
					+ entryIds + ")";

		String sqlCount = "select count(*) from (" + sql + ")";
		List list = dll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(dll.getSingal(sqlCount).toString()));
		return pg;
	}

	@SuppressWarnings("unchecked")
	public PageObject getBpPlanDeptShowAllList(String planDate,
			String enterpriseCode, int... rowIndexAndCount) {
		PageObject pg = new PageObject();
		String sql = "select to_char(b.plan_time,'yyyy-mm'),\n"//modify by wpzhu 20100602
				+ " getworkername(b.edit_by),\n"
				+ " to_char(b.edit_date,'yyyy-mm-dd'),\n"
				+ " b.dep_main_id,\n"
				+ " getdeptname(b.edit_depcode),\n"
				+ " c.job_id,\n"
				+ " c.job_content,\n"
				+ " c.complete_data,\n"
				+ " b.work_flow_no,\n"
				+ " decode(c.complete_data,0,'当月',1,'跨月','2','长期',3,'全年'),\n"
				+ " c.order_by,\n" // add by sychen 20100506
				+	"getdeptname( GETFirstLevelBYID(d.dept_id))level1DeptName"
//				+ " (SELECT getdeptname(t.dept_code)\n"
//				+ "  FROM hr_c_dept t\n"
//				+ "  WHERE t.dept_level = 1\n"
//				+ "        AND rownum = 1\n"
//				+ "         START WITH t.dept_id = d.dept_id\n"
//				+ "        CONNECT BY PRIOR t.pdept_id = t.dept_id)level1DeptName\n"
				+ " from BP_J_PLAN_JOB_DEP_MAIN b,BP_J_PLAN_JOB_DEP_DETAIL c,hr_c_dept d ,bp_c_plan_job_dept t \n"
				+ " where c.dep_main_id=b.dep_main_id\n"
				+ " and d.dept_code = b.edit_depcode\n"
				+ " and b.enterprise_code='" + enterpriseCode + "'\n"
				+
				// 部门计划审批结束
				" and b.sign_status ='11'\n" + " and c.enterprise_code='"
				+ enterpriseCode + "' \n " 
				+"and  GETFirstLevelBYID(d.dept_id)=t.dept_code(+) ";

		if (planDate != null && !planDate.equals(""))
			sql += " and to_char(b.plan_time,'yyyy-mm')='" + planDate + "' \n";
            sql += "order by  t.order_by ,level1DeptName,c.job_id \n";
		String sqlCount = "select count(*) from (" + sql + ") \n";
//		System.out.println("the sql"+sql);
		List list = dll.queryByNativeSQL(sql, rowIndexAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(dll.getSingal(sqlCount).toString()));
		return pg;
	}

	@SuppressWarnings("unchecked")
	public PageObject getBpPlanDeptCompleteShowAllList(String planDate,
			String enterpriseCode, int... rowIndexAndCount) {
		PageObject pg = new PageObject();
		String sql = "select to_char(b.plan_time,'yyyy-mm'),\n"
				+ " b.finish_work_flow_no,\n"
				+ " b.finish_sign_status,\n"
				+ " b.edit_depcode,\n"
				+ " getdeptname(b.edit_depcode),\n"
				+ " c.job_id,\n"
				+ " c.job_content,\n"
				+ " decode(c.if_complete,0,'未完成',1,'进行中','2','已完成'),\n"
				+ " c.complete_desc,\n"
				+ " decode(c.complete_data,0,'当月完成',1,'跨越完成','2','长期',3,'全年完成')\n"
				// add by liuyi 20100524 
				+ " ,c.order_by \n"
				+ " from BP_J_PLAN_JOB_DEP_MAIN b,BP_J_PLAN_JOB_DEP_DETAIL c\n"
				+ " where c.dep_main_id=b.dep_main_id\n"
				+ " and b.enterprise_code='" + enterpriseCode + "'\n"
				+ " and b.finish_sign_status ='11'\n"
				+ " and c.enterprise_code='" + enterpriseCode + "'";

		if (planDate != null && !planDate.equals(""))
			sql += " and to_char(b.plan_time,'yyyy-mm')='" + planDate + "' \n";

		String sqlCount = "select count(*) from (" + sql + ") \n";
		List list = dll.queryByNativeSQL(sql, rowIndexAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(dll.getSingal(sqlCount).toString()));
		return pg;
	}

	@SuppressWarnings("unchecked")
	public String getManagerDept(Long id) {

		String sql = "select t.dept_level,t.dept_code,t.dept_name, level\n"
				+ "    from hr_c_dept t\n" + "   start with t.dept_id = '" + id
				+ "'\n" + "  connect by prior t.pdept_id = t.dept_id";

		List list = dll.queryByNativeSQL(sql);
		String dept = "";
		// Iterator it = list.iterator();
		// while (it.hasNext()) {
		// Object[] data = (Object[]) it.next();
		// }
		//		
		for (int i = 0; i < list.size(); i++) {
			Object[] data = (Object[]) list.get(i);
			if (data[0] != null && data[0].toString().equals("1")) {
				dept = data[1].toString() + "," + data[2].toString();
				break;
			}

		}
		return dept;
	}

	private boolean checkDept(String deptcode1) {
		String sql = "select count(1)\n" + "  from hr_c_dept t\n"
				+ "where t.dept_code='" + deptcode1 + "'\n"
				+ " start with t.dept_id = 191\n"
				+ "connect by prior t.dept_id = t.pdept_id";
		Long count = Long.parseLong(dll.getSingal(sql).toString());
		if (count > 0) {
			return true;
		} else
			return false;
	}

	private boolean checkDepthot(String deptcode1) {
		String sql = "select count(1)\n" + " from hr_c_dept t\n"
				+ "where t.dept_code='" + deptcode1 + "'\n"
				+ " start with t.dept_id = 204 or t.dept_id =205 \n"
				+ "connect by prior t.dept_id = t.pdept_id";
		Long count = Long.parseLong(dll.getSingal(sql).toString());
		if (count > 0)
			return true;
		else
			return false;
	}
	
	/**
	 * 查询部门月度计划完成情况填写页面新增主表的主键
	 * add by sychen 20100613
	 * @param planTime
	 * @param workerCode
	 * @param enterpriseCode
	 * @return
	 */
	public String getPlanCompleteNewMainId(String planTime, String workerCode,
			String enterpriseCode) {

		String sql = "select t.dep_main_id from bp_j_plan_job_dep_main t\n"
				+ " where t.plan_time=to_date('" + planTime
				+ "'||'-01','yyyy-MM-dd ') \n" + " and t.edit_by='"
				+ workerCode + "'\n" + " and (t.sign_status is null or t.sign_status=0)\n"
				+ " and t.enterprise_code='" + enterpriseCode + "'";

		Object maxNoObj = dll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}

}