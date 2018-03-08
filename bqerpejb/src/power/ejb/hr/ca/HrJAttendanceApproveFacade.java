package power.ejb.hr.ca;

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

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;

/**
 * Facade for entity HrJAttendanceApprove.
 * 
 * @see power.ejb.hr.ca.HrJAttendanceApprove
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJAttendanceApproveFacade implements
		HrJAttendanceApproveFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;

	
	public void save(HrJAttendanceApprove entity) {
		LogUtil.log("saving HrJAttendanceApprove instance", Level.INFO, null);
		try {
			entity.setApproveId(bll.getMaxId("HR_J_ATTENDANCE_APPROVE", "approve_id"));
			entity.setIsUse("Y");
			entity.setSendState("1");
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public void delete(HrJAttendanceApprove entity) {
		LogUtil.log("deleting HrJAttendanceApprove instance", Level.INFO, null);
		try {
			entity = entityManager.getReference(HrJAttendanceApprove.class,
					entity.getApproveId());
			entityManager.remove(entity);
			LogUtil.log("delete successful", Level.INFO, null);
		} catch (RuntimeException re) {
			LogUtil.log("delete failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	public HrJAttendanceApprove update(HrJAttendanceApprove entity) {
		LogUtil.log("updating HrJAttendanceApprove instance", Level.INFO, null);
		try {
			HrJAttendanceApprove result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJAttendanceApprove findById(Long id) {
		LogUtil.log("finding HrJAttendanceApprove instance with id: " + id,
				Level.INFO, null);
		try {
			HrJAttendanceApprove instance = entityManager.find(
					HrJAttendanceApprove.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}

	
	@SuppressWarnings("unchecked")
	public List<HrJAttendanceApprove> getAttendanceApprove(String month,Long attendanceDeptId) {
		String sql = "select * from HR_J_ATTENDANCE_APPROVE t where t.APPROVE_MONTH ='"
				+ month+ "' \n";
		 if(attendanceDeptId!=null&&!attendanceDeptId.equals(""))
		 {
			 sql+= "and t.ATTENDANCE_DEPT_ID='"+ attendanceDeptId+ "' ";
		 }
//		 if(entryIds!=null&&entryIds.length>0)
//		 {
//			 sql+=" and t.work_flow_no in ("+entryIds[0]+") ";
//		 }
		
		List<HrJAttendanceApprove> list =  bll
				.queryByNativeSQL(sql, HrJAttendanceApprove.class);
		return list;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<HrJAttendanceApprove> getAttendanceListForApprove(String month,String entryIds,Long deptId)
	{
		String sql=
			"select *\n" +
			"  from hr_j_attendance_approve t, hr_j_emp_info b\n" + 
			" where t.report_by = b.emp_code\n" + 
			" and t.approve_month='"+month+"'\n" + 
			"   and t.is_use = 'Y'\n" + 
			"   and t.work_flow_no in ("+entryIds+")\n"+
			"   and  decode(t.send_state,'3',t.report_by, (SELECT a.dept_code\n" + 
			"          FROM hr_c_dept a\n" + 
			"         where a.dept_level = 1\n" + 
			"           and rownum = 1\n" + 
			"         START WITH a.dept_id = b.dept_id\n" + 
			"        CONNECT BY PRIOR a.pdept_id = a.dept_id)) =\n" + 
			"       decode(t.send_state,'3',t.report_by, (SELECT a.dept_code\n" + 
			"          FROM hr_c_dept a\n" + 
			"         where a.dept_level = 1\n" + 
			"           and rownum = 1\n" + 
			"         START WITH a.dept_id = "+deptId+"\n" + 
			"        CONNECT BY PRIOR a.pdept_id = a.dept_id))";


		List<HrJAttendanceApprove> list=bll.queryByNativeSQL(sql, HrJAttendanceApprove.class);
		return list;
	}
	
	public void reportAttendanceDept(Long attendanceDeptId, String workerCode,
			Long actionId, String nextRoles, String remark, String month,String enterpriseCode) {
		service = new WorkflowServiceImpl();
		List<HrJAttendanceApprove> list =this.getAttendanceApprove(month,
				attendanceDeptId);
		Long entryId;
		if(list!=null&&list.size()>0)
		{
			HrJAttendanceApprove model=list.get(0);
			entryId=model.getWorkFlowNo();
			model.setSendState("1");
			model.setReportBy(workerCode);
			model.setReportDate(new Date());
			this.update(model);
		}
		else
		{
			entryId = service.doInitialize("bqWorkAttendance", workerCode,
					attendanceDeptId.toString());
			HrJAttendanceApprove model=new HrJAttendanceApprove();
			model.setApproveMonth(month);
			model.setAttendanceDeptId(attendanceDeptId);
			model.setEnterpriseCode(enterpriseCode);
			model.setReportBy(workerCode);
			model.setReportDate(new Date());
			model.setSendState("1");
			model.setWorkFlowNo(entryId);
			this.save(model);
		}
		service.doAction(entryId, attendanceDeptId.toString(), actionId,
				remark, null,nextRoles, "");
		
	}
	
//	//考勤审批部门管理员汇总审批时授权部门过滤 add by sychen 20100729
//	@SuppressWarnings("unchecked")
//	public String getnextRolesList(String nextRole, Long deptId) {
//
//		String sql= "select r.role_id\n" + 
//		"  from (select *\n" + 
//		"          from sys_c_roles a\n" + 
//		"         where a.is_use = 'Y'\n" + 
//		"           and a.role_id in ("+nextRole+")) r\n" + 
//		" where GETFirstLevelBYID(r.accredit_section) = GETFirstLevelBYID("+deptId+")";
//
//		List list = bll.queryByNativeSQL(sql);
//		String nextRoles = "";
//		for (int i = 0; i < list.size(); i++) {
//			Object data = list.get(i);
//			if (data != null) {
//				nextRoles = data.toString() + "," ;
//				break;
//			}
//
//		}
//		return nextRoles;
//	}
	
	
	public String approveAttendance(Long deptId,String strMonth,String approveText,String workerCode,Long actionId,String nextRoles,String eventIdentify,String entryIds)
	{
		//---状态 1---已上报 2---部门管理员已汇总 3----部门主任已审核  4----审批结束 5---已退回
		service = new WorkflowServiceImpl();
		List<HrJAttendanceApprove> list = this.getAttendanceListForApprove(strMonth, entryIds, deptId);
		HrJAttendanceApprove entity=list.get(0);
		boolean flag=true;
		if(!eventIdentify.equals("TH")&&entity.getSendState().equals("1"))
		{
			flag=this.checkAllReport(deptId,strMonth, entity.getEnterpriseCode());//modify by wpzhu 20100714
		}
		
		if(flag)
		{
		String status="";
		if(eventIdentify.equals("TH"))
		{
			status="5";
		}
		else
		{
		   if(entity.getSendState().equals("1")) status="2";
		   else if(entity.getSendState().equals("2")) status="3";
		   else if(entity.getSendState().equals("3")) status="4";
		   else status="";
		}
		
		for(HrJAttendanceApprove model:list)
		{
			model.setSendState(status);
			this.update(model);
			service.doAction(model.getWorkFlowNo(), workerCode, actionId, approveText, null,nextRoles,""); 
		}
		return "审批成功！";
		}else
		{
			return "存在部门考勤未上报，不能审批！";
		}
	}
	
	/**
	 * 判定考勤部门登记人为相同一级部门的考勤部门是否已全部上报(部门管理员汇总时判断)
	 * modify by fyyang 20100803
	 */
	private boolean checkAllReport(Long deptId,String strMonth,String enterpriseCode)
	{
		String sql=
			"select count(*)\n" +
			"  from HR_C_ATTENDANCEDEP a, HR_J_ATTENDANCE_APPROVE t, hr_j_emp_info b\n" + 
			" where a.attendance_dept_id = t.attendance_dept_id(+)\n" + 
			"   and a.attend_writer_id = b.emp_id\n" + 
			"   and (SELECT c.dept_code\n" + 
			"          FROM hr_c_dept c\n" + 
			"         where c.dept_level = 1\n" + 
			"           and rownum = 1\n" + 
			"         START WITH c.dept_id = b.dept_id\n" + 
			"        CONNECT BY PRIOR c.pdept_id = c.dept_id) =\n" + 
			"       (SELECT c.dept_code\n" + 
			"          FROM hr_c_dept c\n" + 
			"         where c.dept_level = 1\n" + 
			"           and rownum = 1\n" + 
			"         START WITH c.dept_id = "+deptId+"\n" + 
			"        CONNECT BY PRIOR c.pdept_id = c.dept_id)\n" + 
			"   and a.is_use = 'Y'\n" + 
			"   and a.enterprise_code = '"+enterpriseCode+"'\n" + 
			"   and t.is_use(+) = 'Y'\n" + 
			"   and t.approve_month(+) = '"+strMonth+"'\n" + 
			"   and t.approve_id is null \n"+
			"   and ( select count(*) from hr_c_attendancedep e where e.top_check_dep_id=a.attendance_dept_id and e.is_use='Y')=0 ";

        Object obj=bll.getSingal(sql);
        if(obj!=null&& Long.parseLong(obj.toString())>0)
        {
        	return false;
        }
        else
        {
        	return true;
        }
		
	}
	
	public String getMsgList(String workCode)
	{
		String msg="";
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"bqWorkAttendance"}, workCode);
		if(entryIds==null||entryIds.equals("")) return msg;
		else
		{
			String sql=
				"select count(*)\n" +
				"  from hr_j_attendance_approve t, hr_j_emp_info b\n" + 
				" where t.report_by = b.emp_code\n" + 
				"   and t.is_use = 'Y'\n" + 
				"   and t.work_flow_no in ("+entryIds+")\n" + 
				"   and  decode(t.send_state,'3',t.report_by, (SELECT a.dept_code\n" + 
				"          FROM hr_c_dept a\n" + 
				"         where a.dept_level = 1\n" + 
				"           and rownum = 1\n" + 
				"         START WITH a.dept_id = b.dept_id\n" + 
				"        CONNECT BY PRIOR a.pdept_id = a.dept_id)) =\n" + 
				"       decode(t.send_state,'3',t.report_by, (SELECT a.dept_code\n" + 
				"          FROM hr_c_dept a\n" + 
				"         where a.dept_level = 1\n" + 
				"           and rownum = 1\n" + 
				"         START WITH a.dept_id = (select c.dept_id\n" + 
				"                                   from hr_j_emp_info c\n" + 
				"                                  where c.emp_code = '"+workCode+"'\n" + 
				"                                    and rownum = 1)\n" + 
				"        CONNECT BY PRIOR a.pdept_id = a.dept_id))";

			
		   Long count=Long.parseLong(bll.getSingal(sql).toString());
		   if(count>0)
		   {
			   msg="<a target=\"_blank\"  href=\"hr/ca/attendance/attendanceApprove/approveQuery.jsp\">"
				+ count + "张考勤信息等待审批</a><br/><br/>";
			   
		   }
		   return msg;

		}
		
	}
	
	
	
	

}