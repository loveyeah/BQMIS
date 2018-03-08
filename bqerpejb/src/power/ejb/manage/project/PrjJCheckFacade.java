package power.ejb.manage.project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity PrjJCheck.
 * 
 * @see power.ejb.manage.project.PrjJCheck
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class PrjJCheckFacade implements PrjJCheckFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	private WorkflowService service;
	public PrjJCheckFacade(){
		service = new WorkflowServiceImpl();
	}

	/**
	 * Perform an initial save of a previously unsaved PrjJCheck entity. All
	 * subsequent persist actions of this entity should use the #update()
	 * method.
	 * 
	 * @param entity
	 *            PrjJCheck entity to persist
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	
	//add by ypan 20100819
	public PageObject findReportList(String enterpriseCode,String projectName,int statusId,int... rowStartIdxAndCount){
		PageObject page=new PageObject();
		System.out.println(projectName);
		String sql="select t.check_id,\n"+
       "t.contract_name,\n"+
       "t.con_id,\n"+
       "t.report_code,\n"+
       "t.start_date,\n"+
       "t.end_date,\n"+
       "t.contractor_name,\n"+
       "t.contractor_id,\n"+
       "t.charge_by,\n"+
       "t.dept_charge_by,\n"+
       "t.check_appraise ,\n"+
       "t.entry_by,\n"+
       "t.end_date,\n"+
       "t.enterprise_code\n"+
  "from PRJ_J_CHECK t\n"+
 "where t.contract_name like '%"+projectName+"%'\n"+
   "and t.is_use = 'Y'\n"+
   "and t.status_id = '"+statusId+"'\n";
		String sqlCount=
			"select count(1)\n" +
			"from PRJ_J_CHECK t\n"+
			"where t.contract_name like '%"+projectName+"%'\n"+
			   "and t.is_use = 'Y'\n"+
			   "and t.status_id = '"+statusId+"'\n";
		System.out.println(sql);
		Long totalCount = Long.parseLong(bll.getSingal(sqlCount).toString());
		page.setTotalCount(totalCount);
		List list = bll.queryByNativeSQL(sql);
		page.setList(list);
		return page;
	}
	
	public PrjJCheck save(PrjJCheck entity,String dept) {
		LogUtil.log("saving PrjJCheck instance", Level.INFO, null);
		try {
			entity.setCheckId(bll.getMaxId("PRJ_J_CHECK", "CHECK_ID"));
			String firstLever="select GETDEPTNAME(getfirstlevelbyid("+Long.parseLong(dept)+")) from dual";
			List deptNameList=bll.queryByNativeSQL(firstLever);
			String FirstdeptName=deptNameList.get(0).toString();
			String sql1 = String.format("select fun_spellcode('%s') from dual", FirstdeptName);
			String deptName = bll.getSingal(sql1).toString();
			System.out.println("部门名称首字母拼写为："+deptName);
			SimpleDateFormat format = new SimpleDateFormat("yyyyMM");
			String date  = format.format(new Date());
			date = date.substring(2,date.length());
			String keyword = "Q/CDT-"+deptName+"-"+date;
			int num = Integer.parseInt((bll.getSingal("select count(*) from" +
					" PRJ_J_CHECK" +
					" where REPORT_CODE like '%"+keyword+"%'").toString()));
			System.out.println("编号序号为："+num);
			keyword+="-0"+(num+1);
			entity.setReportCode(keyword);
			entity.setIsUse("Y");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity;
			
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Delete a persistent PrjJCheck entity.
	 * 
	 * @param entity
	 *            PrjJCheck entity to delete
	 * @throws RuntimeException
	 *             when the operation fails
	 */
	public void delete(String ids) {
		String sql = "update PRJ_J_CHECK set is_use = 'N' where CHECK_ID in ("+ids+")";
		bll.exeNativeSQL(sql);
	}

	/**
	 * Persist a previously saved PrjJCheck entity and return it or a copy of it
	 * to the sender. A copy of the PrjJCheck entity parameter is returned when
	 * the JPA persistence mechanism has not previously been tracking the
	 * updated entity.
	 * 
	 * @param entity
	 *            PrjJCheck entity to update
	 * @return PrjJCheck the persisted PrjJCheck entity instance, may not be the
	 *         same
	 * @throws RuntimeException
	 *             if the operation fails
	 */
	public PrjJCheck update(PrjJCheck entity) {
		LogUtil.log("updating PrjJCheck instance", Level.INFO, null);
		try {
			PrjJCheck result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public PrjJCheck findById(Long id) {
		LogUtil.log("finding PrjJCheck instance with id: " + id, Level.INFO,
				null);
		try {
			PrjJCheck instance = entityManager.find(PrjJCheck.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PrjJCheck entities with a specific property value.
	 * 
	 * @param propertyName
	 *            the name of the PrjJCheck property to query
	 * @param value
	 *            the property value to match
	 * @return List<PrjJCheck> found by query
	 */
	@SuppressWarnings("unchecked")
	public List<PrjJCheck> findByProperty(String propertyName,
			final Object value) {
		LogUtil.log("finding PrjJCheck instance with property: " + propertyName
				+ ", value: " + value, Level.INFO, null);
		try {
			final String queryString = "select model from PrjJCheck model where model."
					+ propertyName + "= :propertyValue";
			Query query = entityManager.createQuery(queryString);
			query.setParameter("propertyValue", value);
			return query.getResultList();
		} catch (RuntimeException re) {
			LogUtil.log("find by property name failed", Level.SEVERE, re);
			throw re;
		}
	}

	/**
	 * Find all PrjJCheck entities.
	 * 
	 * @return List<PrjJCheck> all PrjJCheck entities
	 */
	@SuppressWarnings("unchecked")
	public PageObject findAll(String statusId, String workerCode,String projectName,String flag, String startTime, String endTime, int... rowStartIdxAndCount) {
		
		String sql = "select t.check_id,\n" +
				"       t.contract_name,\n" + 
				"       t.con_id,\n" + 
				"       t.report_code,\n" + 
				"       t.start_date,\n" + 
				"       t.end_date,\n" + 
				"       t.contractor_name,\n" + 
				"       t.contractor_id,\n" + 
				"       t.charge_by,\n" + 
				"       t.dept_charge_by,\n" + 
				"       t.check_appraise,\n" + 
				"       getworkername(t.entry_by),\n" + 
				"       t.entry_date,\n" +
				"		t.back_entry_by,\n" +
				"       GETWORKERNAME(t.dept_charge_by),\n" +
				"		t.status_id,\n " +
				"		t.prj_id,\n" +
				"		(select a.prj_name from PRJ_J_REGISTER a where a.prj_id = t.prj_id) as prj_name" + 
				"  from prj_j_check t" + 
				"  where t.is_use='Y'";
		
		if(statusId != null && !"".equals(statusId)) {
			sql += " and t.STATUS_ID ='"+statusId+"'";
		}
		if(projectName != null && !"".equals(projectName)) {
			sql += " and t.CONTRACT_NAME like '%"+projectName+"%'";
		}
		if("fillQuery".equals(flag)){
			if(workerCode != null && !workerCode.equals("") && !workerCode.equals("999999"))
			sql += " and t.entry_by='" + workerCode + "' \n";
		}	
		String sqlCount = "select count(*) from ("+sql+")";
		
		Long countLong = Long.parseLong(bll.getSingal(sqlCount).toString());
		List list = new ArrayList();
		if(countLong>0) {
			list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		}
		PageObject obj = new PageObject();
		obj.setList(list);
		obj.setTotalCount(countLong);
		return obj;
	}
	/**
	 * 竣工验收单上报
	 * @param checkId
	 * @param workercode
	 * @param actionId
	 * @param approveText
	 * @param workFlowCode
	 * update by kzhang 20100821
	 */
	public void endCheckReportTo(String checkId, String workercode,String actionId, String approveText,String workFlowCode){
		WorkflowService service = new WorkflowServiceImpl();
		long entryId = service.doInitialize(workFlowCode,workercode,checkId);
		service.doAction(entryId, workercode,Long.parseLong(actionId),approveText, null); 
		PrjJCheck pjc=findById(Long.parseLong(checkId));
		pjc.setStatusId(1L);
		pjc.setWorkFlowNo(entryId);
		update(pjc);
	}
	/**
	 * 竣工验收单审批
	 * @param entryId
	 * @param workerCode
	 * @param actionId
	 * @param approveText
	 * @param nextRoles
	 * @param identify
	 * @param applyId
	 * @param entryByCode
	 * @return boolean
	 */
	public boolean endCheckApprove(String entryId,String workerCode,String actionId,String approveText,String nextRoles,String identify,String applyId,String entryByCode){
		PrjJCheckApprove pjca=new PrjJCheckApprove();
		PrjJCheck pjc=new PrjJCheck();
		pjc = findById(Long.parseLong(applyId));
		pjca.setCheckId(pjc.getCheckId());
		pjca.setApproveBy(workerCode);
		pjca.setApproveDate(new java.util.Date());
		pjca.setApproveText(approveText);
		pjca.setEnterpriseCode(entryByCode);
		pjca.setIsUse("Y");
		if ("TH".equals(identify)) {
			pjc.setStatusId(0L);
			pjca.setStatusId(0L);
		}
		else if(pjc.getStatusId()==1){
			pjc.setStatusId(2L);
			pjca.setStatusId(2L);
		}
		else if(pjc.getStatusId()==2){
			pjc.setStatusId(3L);
			pjca.setStatusId(3L);
		}
		else if(pjc.getStatusId()==3){
				pjc.setStatusId(4L);
				pjca.setStatusId(4L);
		}else{
			return false;
		}
		update(pjc);
		prjCheckApproveSave(pjca);
		service.doAction(Long.parseLong(entryId), workerCode, Long.parseLong(actionId), approveText, null,
				nextRoles, "");
		return true;
		
	}
	/**
	 * 根据查询条件查询竣工验收单信息
	 * @param projectName
	 * @param statusId
	 * @param enterpriseCode
	 * @param workFlowNos 工作流实例号
	 * @return PageObject
	 * add by kzhang 20100821
	 */
	@SuppressWarnings("unchecked")
	public PageObject checkfindAll(String propertyName,String enterpriseCode,String workFlowNos,Long deptId,
			int... rowStartIdxAndCount){
		PageObject pg = new PageObject();
		String projectName="%"+propertyName+"%";
		String sql = "select t.check_id,\n" +
		"       t.contract_name,\n" + 
		"       t.con_id,\n" + 
		"       t.report_code,\n" + 
		"       t.start_date,\n" + 
		"       t.end_date,\n" + 
		"       t.contractor_name,\n" + 
		"       t.contractor_id,\n" + 
		"       t.charge_by,\n" + 
		"       getworkername(t.dept_charge_by),\n" + 
		"       t.check_appraise,\n" + 
		"       getworkername(t.entry_by),\n" + 
		"       t.entry_date,\n" +
		"		t.back_entry_by," +

		"		t.work_flow_no," +
		"		t.prj_id," +
		"		(select a.prj_name from PRJ_J_REGISTER a where a.prj_id = t.prj_id) as prj_name " +
		"  from prj_j_check t" + 
		"  where t.is_use='Y'"+
		"  and t.enterprise_code='"+enterpriseCode+"'"+
		"  and t.work_flow_no in ("+workFlowNos+")"+
		" and t.CONTRACT_NAME like '"+projectName+"'";
		//add by drdu 20100824 状态为已上报时，按照一级部门过滤
		sql += "and getfirstlevelbyid((select t.dept_id from hr_j_emp_info t where t.emp_code=t.entry_by)) = decode(t.status_id,'1',getfirstlevelbyid("+deptId+"),getfirstlevelbyid((select t.dept_id from hr_j_emp_info t where t.emp_code=t.entry_by)))";
	
		String sqlCount = "select count(1) from (" + sql + ") \n";
		List list=bll.queryByNativeSQL(sql,rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}
	
	public String getCheckList(String workCode,Long deptId)
	{
		String msg="";
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"prjEndChek"}, workCode);
		if(entryIds==null||entryIds.equals("")) return msg;
		else
		{
			String sql = "select count(*)\n" +
				"  from prj_j_check t\n" + 
				" where t.is_use = 'Y'\n" + 
				"   and t.enterprise_code = 'hfdc'\n" + 
				"   and t.work_flow_no in ("+entryIds+")\n" + 
				"   and getfirstlevelbyid((select t.dept_id\n" + 
				"                           from hr_j_emp_info t\n" + 
				"                          where t.emp_code = t.entry_by)) =\n" + 
				"       decode(t.status_id,\n" + 
				"              '1',\n" + 
				"              getfirstlevelbyid("+deptId+"),\n" + 
				"              getfirstlevelbyid((select t.dept_id\n" + 
				"                                  from hr_j_emp_info t\n" + 
				"                                 where t.emp_code = t.entry_by)))\n";
			
//			if(workCode != null && !workCode.equals("999999"))
//			{
//				sql += "   and t.entry_by = '"+workCode+"'";
//			}
			
			 Long count = Long.parseLong(bll.getSingal(sql).toString());
			if (count > 0) {
				msg = "<a target=\"_blank\"  href=\"manage/project/bussiness/projectCheckNew/approve/prjApproveList.js\">"
						+ count + "张工程项目竣工验收单等待审批</a><br/><br/>";
			}
			return msg;

		}
	}
	/**
	 * 保存竣工验收审批信息
	 * @param entity
	 * @throws RuntimeException
	 */
	public void prjCheckApproveSave(PrjJCheckApprove entity) {
		LogUtil.log("saving PrjJCheckApprove instance", Level.INFO, null);
		try {
			Long id=bll.getMaxId("PRJ_J_CHECK_APPROVE", "APPROVE_ID");
			entity.setApproveId(id);
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
}