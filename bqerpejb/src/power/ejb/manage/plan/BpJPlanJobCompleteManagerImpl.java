package power.ejb.manage.plan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

@Stateless
public class BpJPlanJobCompleteManagerImpl implements BpJPlanJobCompleteManager {
	@PersistenceContext
	private EntityManager entityManager;
	WorkflowService service;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public BpJPlanJobCompleteManagerImpl() {
		bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("NativeSqlHelper");
		service = new WorkflowServiceImpl();
	}
	

	public BpJPlanJobCompleteDetail findByDetailId (Long id) {
		LogUtil.log("finding BpJPlanJobCompleteDetail instance with id: " + id,
				Level.INFO, null);
		try {
			BpJPlanJobCompleteDetail instance = entityManager.find(
					BpJPlanJobCompleteDetail.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public BpJPlanJobCompleteMain findByMainId(Long id) {
		LogUtil.log("finding BpJPlanJobCompleteMain instance with id: " + id,
				Level.INFO, null);
		try {
			BpJPlanJobCompleteMain instance = entityManager.find(
					BpJPlanJobCompleteMain.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject getPlanJobCompleteList(String planTime,String workerCode,String enterpriseCode,String editDeptCode) throws Exception {
			 PageObject pg = new PageObject();
			 String sql= "SELECT m.dep_main_id\n" +
				         " FROM bp_j_plan_job_complete_main m\n" + 
				         " WHERE m.enterprise_code = '"+enterpriseCode+"'\n" + 
				         " AND m.plan_time = to_date('" + planTime+ "' || '-01', 'yyyy-MM-dd ')\n" + 
				         " AND m.finish_edit_by = '"+workerCode+"'";
			 Object mainId = bll.getSingal(sql);
			 if(mainId == null) {
					
					String detailSql ="SELECT '' as job_id,\n" +
						"       '' as dep_main_id,\n" + 
						"       d.job_content,\n" + 
						"       d.if_complete,\n" + 
						"       d.complete_desc,\n" + 
						"       d.complete_data,\n" + 
						"       d.charge_by,\n" + 
						"       d.order_by,\n" + 
						"       '' as edit_depcode,\n" + 
						"       getdeptname('"+editDeptCode+"') deptName,\n" + 
						"       '' as finish_edit_by,\n" + 
						"       '' as editName,\n" + 
						"       '' as finish_sign_status,\n" + 
						"       '' as finish_work_flow_no,\n" + 
						"       d.job_id as link_job_id,\n"+
						"       '' as if_diversify,\n" + 
						"       '' as if_heating,\n" + 
						"       '' as finish_if_read,\n" + 
						"       (SELECT getdeptname(t.dept_code)\n" + 
						"          FROM hr_c_dept t\n" + 
						"         WHERE t.dept_level = 1\n" + 
						"           AND rownum = 1\n" + 
						"         START WITH t.dept_id = c.dept_id\n" + 
						"        CONNECT BY PRIOR t.pdept_id = t.dept_id) level1DeptName\n" + 
						"  FROM bp_j_plan_job_dep_main   m,\n" + 
						"       bp_j_plan_job_dep_detail d,\n" + 
						"       hr_c_dept                     c\n" + 
						" WHERE m.dep_main_id = d.dep_main_id\n" + 
						"   AND c.dept_code = m.edit_depcode\n"+
						"   AND m.enterprise_code = '"+enterpriseCode+"'\n" + 
						"   AND d.enterprise_code = '"+enterpriseCode+"'\n" + 
						"   AND m.plan_time = to_date('" + planTime+ "' || '-01', 'yyyy-mm-dd')\n" + 
						"   AND m.sign_status = 11\n" + 
						"   AND (d.charge_by = '"+workerCode+"' or m.edit_by ='"+workerCode+"') \n";
				detailSql += " order by d.dep_main_id,to_number(d.order_by) ";
				List list = bll.queryByNativeSQL(detailSql);
				pg.setList(list);
				
			 }
			 else {
				String detailSql ="SELECT d.job_id,\n" +
					"       d.dep_main_id,\n" + 
					"       d.job_content,\n" + 
					"       d.if_complete,\n" + 
					"       d.complete_desc,\n" + 
					"       d.complete_data,\n" + 
					"       d.charge_by,\n" + 
					"       d.order_by,\n" + 
					"       m.edit_depcode,\n" + 
					"       getdeptname(m.edit_depcode) deptName,\n" + 
					"       m.finish_edit_by,\n" + 
					"       getworkername(m.finish_edit_by) editName,\n" + 
					"       m.finish_sign_status,\n" + 
					"       m.finish_work_flow_no,\n" + 
					"       d.link_job_id,\n" + 
					"       m.if_diversify,\n" + 
					"       m.if_heating,\n" + 
					"       m.finish_if_read,\n"+
					"       (SELECT getdeptname(t.dept_code)\n" + 
					"          FROM hr_c_dept t\n" + 
					"         WHERE t.dept_level = 1\n" + 
					"           AND rownum = 1\n" + 
					"         START WITH t.dept_id = c.dept_id\n" + 
					"        CONNECT BY PRIOR t.pdept_id = t.dept_id) level1DeptName\n" + 
					"  FROM bp_j_plan_job_complete_detail d,\n" + 
					"       bp_j_plan_job_complete_main   m,\n" + 
					"       hr_c_dept                     c\n" + 
					" WHERE m.dep_main_id = d.dep_main_id\n" + 
					"   AND c.dept_code = m.edit_depcode\n"+
					"   AND m.enterprise_code = '"+enterpriseCode+"'\n" + 
					"   AND d.enterprise_code = '"+enterpriseCode+"'\n" + 
					"   AND m.plan_time = to_date('" + planTime+ "' || '-01', 'yyyy-mm-dd')\n" + 
					"   AND m.finish_edit_by = '"+workerCode+"'\n";

				detailSql += " order by to_number(d.order_by) ";
				List list = bll.queryByNativeSQL(detailSql);
				pg.setList(list);
			}
			 return pg;	
	}
	
	public String getPlanCompleteNewMainId(String planTime, String workerCode,
			String enterpriseCode) {

		String sql= "SELECT m.dep_main_id\n" +
                " FROM bp_j_plan_job_complete_main m\n" + 
                " WHERE m.enterprise_code = '"+enterpriseCode+"'\n" + 
                " AND m.plan_time = to_date('" + planTime+ "' || '-01', 'yyyy-MM-dd ')\n" + 
                " AND m.finish_edit_by = '"+workerCode+"'";

		Object maxNoObj = bll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}
	
	public String getPlanCompleteStatus(String planTime, String workerCode,
			String enterpriseCode) {

		String sql= "SELECT m.finish_sign_status\n" +
                " FROM bp_j_plan_job_complete_main m\n" + 
                " WHERE m.enterprise_code = '"+enterpriseCode+"'\n" + 
                " AND m.plan_time = to_date('" + planTime+ "' || '-01', 'yyyy-MM-dd ')\n" + 
                " AND m.finish_edit_by = '"+workerCode+"'";

		Object maxNoObj = bll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}
	
	public BpJPlanJobCompleteMain save(BpJPlanJobCompleteMain entity, String deptCode1, String WorkerCode ) {
		LogUtil.log("saving BpJPlanJobCompleteMain instance", Level.INFO, null);
		try {

			if (this.checkDiversifyDept(deptCode1)) {
				entity.setIfDiversify("Y");
			} else {
				entity.setIfDiversify("N");
			}

			if (this.checkHeatingDept(deptCode1)) {
				entity.setIfHeating("Y");
			} else {
				entity.setIfHeating("N");
			}
			String id = bll.getMaxId("bp_j_plan_job_complete_main ", "dep_main_id")
					.toString();
			entity.setDepMainId(Long.parseLong(id));

			entityManager.persist(entity);

			LogUtil.log("save successful", Level.INFO, null);
			return entity;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public BpJPlanJobCompleteMain update(BpJPlanJobCompleteMain entity) {
		LogUtil.log("updating BpJPlanJobCompleteMain instance", Level.INFO,
				null);
		try {
			BpJPlanJobCompleteMain result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void saveDetail(List<BpJPlanJobCompleteDetail> addList) {
		if (addList != null && addList.size() > 0) {
			int i = 0;
			Long idtemp = bll.getMaxId("bp_j_plan_job_complete_detail", "job_id");
			for (BpJPlanJobCompleteDetail entity : addList) {
				Long id = idtemp + i++;

				entity.setJobId(id);
				this.saveD(entity);
			}
		}
	}

	public boolean deleteDetail(String ids) {
		try {
			String[] temp1 = ids.split(",");

			for (String i : temp1) {
				BpJPlanJobCompleteDetail entity = new BpJPlanJobCompleteDetail();
				entity = this.findByDetailId(Long.parseLong(i));
				this.deleteD(entity);

			}

			return true;
		} catch (RuntimeException re) {
			return false;
		}
	}
	
	public void updateDetail(List<BpJPlanJobCompleteDetail> updateList) {
		if (updateList != null && updateList.size() > 0) {

			for (BpJPlanJobCompleteDetail entity : updateList) {

				this.updateD(entity);
			}
		}
	}
	
	public void saveD(BpJPlanJobCompleteDetail entity) {
		LogUtil.log("saving BpJPlanJobCompleteDetail instance", Level.INFO,
				null);
		try {
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	public void deleteD(BpJPlanJobCompleteDetail entity) {
		LogUtil.log("deleting BpJPlanJobCompleteDetail instance", Level.INFO,
				null);
		try {
			entity = entityManager.getReference(BpJPlanJobCompleteDetail.class,
					entity.getJobId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public BpJPlanJobCompleteDetail updateD(BpJPlanJobCompleteDetail entity) {
		LogUtil.log("updating BpJPlanJobCompleteDetail instance", Level.INFO,
				null);
		try {
			BpJPlanJobCompleteDetail result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	
	private boolean checkDiversifyDept(String deptcode1) {
		String sql = "select count(1)\n" + "  from hr_c_dept t\n"
				+ "where t.dept_code='" + deptcode1 + "'\n"
				+ " start with t.dept_id = 191\n"
				+ "connect by prior t.dept_id = t.pdept_id";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0) {
			return true;
		} else
			return false;
	}

	private boolean checkHeatingDept(String deptcode1) {
		String sql = "select count(1)\n" + " from hr_c_dept t\n"
				+ "where t.dept_code='" + deptcode1 + "'\n"
				+ " start with t.dept_id = 204 or t.dept_id =205 \n"
				+ "connect by prior t.dept_id = t.pdept_id";
		Long count = Long.parseLong(bll.getSingal(sql).toString());
		if (count > 0)
			return true;
		else
			return false;
	}
	
	public void reportPlanJobComplete(Long deptMainId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String nextRolePs, String workflowType) {
		BpJPlanJobCompleteMain model = entityManager.find(BpJPlanJobCompleteMain.class,
				deptMainId);
		Long entryId;
		if (model.getFinishWorkFlowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, deptMainId
					.toString());
			model.setFinishWorkFlowNo(entryId);
		} else {
			entryId = model.getFinishWorkFlowNo();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, nextRolePs);
		model.setFinishSignStatus(1l);
		entityManager.merge(model);

	}
	
	
	@SuppressWarnings("unchecked")
	public PageObject getPlanJobCompleteApproveList(String flag,String planTime,String entryIds,String enterpriseCode,
		String txtIfComplete, int... rowStartIdxAndCount) {
		PageObject pg = new PageObject();
		String sql ="SELECT d.job_id,\n" +
		"       d.dep_main_id,\n" + 
		"       d.job_content,\n" + 
		"       d.if_complete,\n" + 
		"       d.complete_desc,\n" + 
		"       d.complete_data,\n" + 
		"       d.charge_by,\n" + 
		"       d.order_by,\n" + 
		"       m.edit_depcode,\n" + 
		"       getdeptname(m.edit_depcode) deptName,\n" + 
		"       m.finish_edit_by,\n" + 
		"       getworkername(m.finish_edit_by) editName,\n" + 
		"       to_char(m.finish_edit_date, 'yyyy-mm-dd') finish_edit_date,\n" +
		"       m.finish_sign_status,\n" + 
		"       m.finish_work_flow_no,\n" + 
		"       d.link_job_id,\n"+
		"       (SELECT getdeptname(t.dept_code)\n" + 
		"          FROM hr_c_dept t\n" + 
		"         WHERE t.dept_level = 1\n" + 
		"           AND rownum = 1\n" + 
		"         START WITH t.dept_id = c.dept_id\n" + 
		"        CONNECT BY PRIOR t.pdept_id = t.dept_id) level1DeptName,\n" + 
		"       decode(d.complete_data,'0','当月','1','跨月','2','长期','')completeDataName,\n"+
		"       decode(d.if_complete,'0','未完成','1','进行中','2','已完成','')ifCompleteName\n"+
		"  FROM bp_j_plan_job_complete_detail d,\n" + 
		"       bp_j_plan_job_complete_main   m,\n" + 
		"       hr_c_dept                     c,\n" + 
		"       bp_c_plan_job_dept          a\n" + 
		" WHERE m.dep_main_id = d.dep_main_id\n" + 
		"   AND c.dept_code = m.edit_depcode\n"+
		"   AND m.enterprise_code = '"+enterpriseCode+"'\n" + 
		"   AND d.enterprise_code = '"+enterpriseCode+"'\n" + 
		"   AND GETFirstLevelBYID(c.dept_id) = a.dept_code(+)\n" + 
		"   AND m.plan_time = to_date('" + planTime+ "' || '-01', 'yyyy-mm-dd')\n"; 
		
		if (flag != null && flag.equals("approve")) {
			if (entryIds != null) {
				String inEntriyId = bll.subStr(entryIds, ",", 500,
						"m.finish_work_flow_no");
				sql += " and " + inEntriyId;
			} else
				sql += " and m.finish_work_flow_no =null";
			sql += "\n order by d.dep_main_id,d.order_by";
		} 
		if (flag != null && flag.equals("show")) {
			if(txtIfComplete!=null && !txtIfComplete.equals("")){
				sql+=" and d.if_complete='"+txtIfComplete+"'\n";
			}
			sql += " order by a.order_by,d.dep_main_id,to_number(d.order_by)";
		}
		
		String sqlCount = "select count(*) from (" + sql + ")";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;

	}
	
	@SuppressWarnings("unchecked")
	public List getPlanJobCompleteInfo(String planTime,String entryIds,String enterpriseCode) {
		String sql ="SELECT m.dep_main_id,\n" +
			"       m.edit_depcode,\n" + 
			"       m.finish_edit_by,\n" + 
			"       m.finish_work_flow_no,\n" + 
			"       m.finish_sign_status,\n" + 
			"       m.if_diversify,\n" + 
			"       m.if_heating,\n" + 
			"       m.finish_if_read\n" + 
			"  FROM bp_j_plan_job_complete_main m\n"+ 
			"  WHERE m.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   AND m.plan_time = to_date('" + planTime+ "' || '-01', 'yyyy-mm-dd')\n";
		if (entryIds != null) {
			String inEntriyId = bll.subStr(entryIds, ",", 500,
					"m.finish_work_flow_no");
			sql += " and " + inEntriyId;
		} else
			sql += " and m.finish_work_flow_no =null";
		sql += "\n order by m.dep_main_id";
		return bll.queryByNativeSQL(sql);

	}
	
	@SuppressWarnings("unchecked")
	public BpJPlanJobCompleteMain planJobCompleteApprove(String deptMainId,
			String workflowType, String workerCode, String actionId,
			String eventIdentify, String approveText, String nextRoles,
			String stepId, String enterpriseCode) {

	
		BpJPlanJobCompleteMain entity = findByMainId(Long.parseLong(deptMainId));
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
	}
	
	@SuppressWarnings("unchecked")
	public PageObject getPlanJobCompleteMainList(String flag,String planTime,String enterpriseCode,
			  int... rowStartIdxAndCount) {
			PageObject pg = new PageObject();
			String sql ="SELECT m.dep_main_id,\n" +
				"       m.plan_time,\n" + 
				"       m.edit_depcode,\n" + 
				"       getdeptname(m.edit_depcode) deptName,\n" + 
				"       m.finish_edit_by,\n" + 
				"       getworkername(m.finish_edit_by) editName,\n" + 
				"       to_char(m.finish_edit_date, 'yyyy-mm-dd') finish_edit_date,\n" + 
				"       m.finish_work_flow_no,\n" + 
				"       m.finish_sign_status,\n" + 
				"       m.finish_if_read,\n" + 
				"       getdeptname(GETFirstLevelBYID(c.dept_id)),\n" + 
				"       d.order_by\n" + 
				"  FROM bp_j_plan_job_complete_main m,\n" + 
				"       hr_c_dept                   c,\n" + 
				"       bp_c_plan_job_dept          d\n" + 
				" WHERE c.dept_code = m.edit_depcode\n" + 
				"   AND m.plan_time = to_date('"+planTime+"' || '-01', 'yyyy-MM-dd ')\n" + 
				"   AND m.enterprise_code = '"+enterpriseCode+"'\n" + 
				"   AND GETFirstLevelBYID(c.dept_id) = d.dept_code(+)\n";
 
			
			if (flag != null && flag.equals("finish")) {
				sql += " and m.finish_sign_status =11\n";
				sql += "order by d.order_by,m.dep_main_id";
			} 
			String sqlCount = "select count(*) from (" + sql + ")";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			pg.setList(list);
			pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
			return pg;

		}
	
	@SuppressWarnings("unchecked")
	public PageObject getPlanJobCompleteDetailList(String deptMainId,String enterpriseCode,
			  int... rowStartIdxAndCount) {
			PageObject pg = new PageObject();
			String sql ="SELECT d.job_id,\n" +
			"       d.dep_main_id,\n" + 
			"       d.job_content,\n" + 
			"       d.if_complete,\n" + 
			"       d.complete_desc,\n" + 
			"       d.complete_data,\n" + 
			"       d.charge_by,\n" + 
			"       d.order_by\n" + 
			"  FROM bp_j_plan_job_complete_detail d\n" +  
			"   WHERE d.enterprise_code = '"+enterpriseCode+"'\n"+
			"   AND d.dep_main_id ='"+deptMainId+"'\n"+
			"   order by d.dep_main_id,d.order_by";

			String sqlCount = "select count(*) from (" + sql + ")";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			pg.setList(list);
			pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
			return pg;

		}
	public void modifyFinishIfRead(Long deptMainId) {
		String sql = "update bp_j_plan_job_complete_main a set a.finish_if_read = 'Y' where a.dep_main_id = "
				+ deptMainId + "";
		bll.exeNativeSQL(sql);
	}
	
	@SuppressWarnings("unchecked")
	public PageObject getPlanJobCompleteQuestList(String planTime,String enterpriseCode,String editDepcode,
			int... rowStartIdxAndCount) {
			PageObject pg = new PageObject();
			String sql ="SELECT d.job_id,\n" +
			"       d.dep_main_id,\n" + 
			"       d.job_content,\n" + 
			"       d.if_complete,\n" + 
			"       d.complete_desc,\n" + 
			"       d.complete_data,\n" + 
			"       d.charge_by,\n" + 
			"       d.order_by,\n" + 
			"       m.edit_depcode,\n" + 
			"       getdeptname(m.edit_depcode) deptName,\n" + 
			"       m.finish_edit_by,\n" + 
			"       getworkername(m.finish_edit_by) editName,\n" + 
			"       to_char(m.finish_edit_date, 'yyyy-mm-dd') finish_edit_date,\n" +
			"       m.finish_sign_status,\n" + 
			"       m.finish_work_flow_no,\n" + 
			"       d.link_job_id,\n"+
			"       (SELECT getdeptname(t.dept_code)\n" + 
			"          FROM hr_c_dept t\n" + 
			"         WHERE t.dept_level = 1\n" + 
			"           AND rownum = 1\n" + 
			"         START WITH t.dept_id = c.dept_id\n" + 
			"        CONNECT BY PRIOR t.pdept_id = t.dept_id) level1DeptName\n" + 
			"  FROM bp_j_plan_job_complete_detail d,\n" + 
			"       bp_j_plan_job_complete_main   m,\n" + 
			"       hr_c_dept                     c\n" + 
			" WHERE m.dep_main_id = d.dep_main_id\n" + 
			"   AND c.dept_code = m.edit_depcode\n" + 
			"   AND m.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   AND d.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   AND m.plan_time = to_date('" + planTime+ "' || '-01', 'yyyy-mm-dd')\n"; 
			
			
          if (editDepcode != null && !editDepcode.equals("")) {
					sql += " and substr(m.edit_depcode,1,"+editDepcode.length()+")='" + editDepcode + "'\n";
				}
			
				sql += "order by d.dep_main_id,to_number(d.order_by)";
			
			String sqlCount = "select count(*) from (" + sql + ")";
			List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
			pg.setList(list);
			pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
			return pg;

		}
	
	public String getPlanJobCompleteMaxPlanTime(String enterpriseCode,String entryIds) {
		String sql = "SELECT MAX(m.plan_time)\n" +
			"  FROM bp_j_plan_job_complete_main m\n" + 
			" WHERE m.enterprise_code = '"+enterpriseCode+"'\n" ;
		if (entryIds != null) {
			String inEntriyId = bll.subStr(entryIds, ",", 500,
					"m.finish_work_flow_no");
			sql += " and " + inEntriyId;
		} else
			sql += " and m.finish_work_flow_no =null";


		Object maxNoObj = bll.getSingal(sql);
		String maxNo = (maxNoObj == null) ? "" : maxNoObj.toString();
		return maxNo;
	}
	
}