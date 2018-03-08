package power.ejb.productiontec.castBackProtect;

import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.Ejb3Factory;
import power.ear.comm.ejb.PageObject;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.plan.maintWeekPlan.BpJPlanRepairGather;

import java.util.Date;
import java.util.Calendar ;
@Stateless
public class protectMangeImpl  implements  protectMange
{
	@PersistenceContext
	private EntityManager entityManager;
	WorkflowService service;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;

	public protectMangeImpl() {
		bll = (NativeSqlHelperRemote) Ejb3Factory.getInstance()
				.getFacadeRemote("NativeSqlHelper");
		service = new WorkflowServiceImpl();
	}

	//by ghzhou 2010-07-31 获得投退保护单流水号
	public int getMaxId(String type,String enterpriseCode){
		String inOrOut = type;
		int maxId=1;
		if("1".equals(type) || "I".equals(type)){
			type = "I";
		}
		else type ="O";
		Calendar time =  Calendar.getInstance();
		//得到月份
		String formatMonth = null;
		int intMonth = time.get(Calendar.MONTH) + 1;
		if(intMonth < 10){ 	
			formatMonth = "0" + intMonth;
		}else{
			formatMonth = intMonth + "";
		}
		String strTime = time.get(Calendar.YEAR) + "-" + formatMonth;
//		String sql = 
//			"select nvl(max(pa.apply_id)-min(pa.apply_id) + 2,1) from pt_j_protect_apply pa where to_char(pa.apply_time,\n" +
//			"'YYYY-MM') = '" + strTime + "' and pa.in_out = '" + type + "' and is_use='Y' and enterprise_code='"+enterpriseCode+"'";
		String sql="select max(pa.apply_code) from pt_j_protect_apply pa where to_char(pa.apply_time,\n" +
			"'YYYY-MM') = '" + strTime + "' and pa.in_out = '" + type + "' and is_use='Y' and enterprise_code='"+enterpriseCode+"'";
		List list=bll.queryByNativeSQL(sql);
		if (list!=null&&list.size()>0&&list.get(0)!=null) {
			String applyCode=list.get(0).toString();
			applyCode=applyCode.substring(applyCode.length()-4, applyCode.length()-1);
			maxId=Integer.parseInt(applyCode)+1;
		}
		return maxId;

	}
	
	@SuppressWarnings("unchecked")
	public  PageObject  getProtectApply(String protectType,String approve,String status,String inOut,String enterpriseCode,String entryIds,String applyBy,int... rowStartIdxAndCount)
	{
		PageObject result=  new PageObject() ;
		String sql=
			"select distinct t.apply_id," +
			"t.apply_code," +
			"t.in_out," +
			"t.protection_type," +
			"t.apply_dep," +
			"(select distinct d.dept_name from hr_c_dept  d \n" +
			"where d.dept_id=t.apply_dep)deptName," +
			"t.apply_by,"+
			" getworkername(t.apply_by)," +
			"to_char(t.apply_time,'yyyy-mm-dd  hh24:mi') ,\n" +
			"t.protection_name ," +
			"t.protection_reason," +
			"t.measures," +
			"t.workflow_no," +
			"t.status," +
			"execute_time," +
			"t.memo ," +
			"t.block_id,"+			
			" getworkername( t.class_leader)," +
			"getworkername( t.executor)," +
			"getworkername( t.guardian)  from pt_j_protect_apply t\n" +
			"where t.is_use='Y' " +
			"and t.enterprise_code='"+enterpriseCode+"'\n"+
			"and t.apply_by='"+applyBy+"'\n";
		
	
		if(protectType != null &&!protectType.equals("")&& protectType.length() > 0)
		{
			sql += " and t.protection_type='"+protectType+"' ";	
		}
		if(approve != null &&approve.equals("approve"))
		{
			sql += " and t.workflow_no  in ("+entryIds+") ";
		}
		if(status != null &&!status.equals(""))
		{
			sql += " and t.status  in ("+status+") ";
		}
		if(inOut != null &&!inOut.equals("")&& inOut.length() > 0)
		{
			sql += " and t.in_out='"+inOut+"' ";
		}
		String 	sqlcount="select count(1) from ("+sql+")";
//	System.out.println("the sql"+sql);
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
//		System.out.println("the list"+list);
		Long count = Long.parseLong(bll.getSingal(sqlcount).toString());
		
		result.setList(list);
		result.setTotalCount(count);
		
		
		return result;
		
	}
	public boolean save(PtJProtectApply entity) {
		try {
			entity.setApplyId(bll.getMaxId("pt_j_protect_apply", "apply_id"));
			entityManager.persist(entity);
			return true;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public PtJProtectApply update(PtJProtectApply entity) {
		LogUtil.log("updating PtJProtectApply instance", Level.INFO, null);
		try {
			PtJProtectApply result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}
	public PtJProtectApply findById(Long  id) {
		LogUtil.log("finding PtJProtectApply instance with id: " + id, Level.INFO,
				null);
		try {
			PtJProtectApply instance = entityManager.find(PtJProtectApply.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}
	public void deleteProtect(String ids)
	{
		String sql="update pt_j_protect_apply a\n" +
		"   set a.is_use = 'N'\n" + 
		" where a.apply_id  in ("+ids+")";
		 bll.exeNativeSQL(sql);


	}
	public void ProtectReport(String applyID,String flowCode,String workerCode,Long actionId,String approveText,
			String nextRoles, String eventIdentify)
	{
		WorkflowService service = new WorkflowServiceImpl();
		long entryId = service.doInitialize(flowCode,workerCode,applyID);
		
		service.doAction(entryId, workerCode, actionId, "上报", null,nextRoles,null); 
		String sql=" update pt_j_protect_apply  set status=1,workflow_no="+entryId+"  where apply_id='"+applyID+"'";
		bll.exeNativeSQL(sql);
	}
	
	/*public void ProtectReport(Long applyID, Long actionId,
			String workerCode, String approveText, String nextRoles, 
			String workflowType) {
		PtJProtectApply model = this.findById(applyID);
		Long entryId;
		if (model.getWorkflowNo() == null||model.getWorkflowNo().equals("")) {
			entryId = service.doInitialize(workflowType, workerCode, applyID
					.toString());
			System.out.println("the entryId"+entryId);
			
		} else {
			entryId = model.getWorkflowNo();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		model.setStatus("1");
		model.setWorkflowNo(entryId);
		update(model);
	}*/
	
	}