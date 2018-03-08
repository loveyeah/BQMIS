package power.ejb.productiontec.castBackProtect;

import java.util.Date;
import java.util.List;
import java.util.Map;
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


@Stateless
public class PtJProtectApplyFacade implements PtJProtectApplyFacadeRemote {
	

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;


	public void saveCastBackProtect(PtJProtectApply entity,String applyId,String status,String entryId) {
		entityManager.merge(entity);
		
		PtJProtectApply entity2 = this.findByCastBackProtectId(Long.parseLong(applyId));
		
         if(Integer.parseInt(status)==7){
        	entity2.setStatus("8");
    		WorkflowService service = new WorkflowServiceImpl();
    		service.doAction(Long.valueOf(entryId),"",103l,"",null,"","");
    		entityManager.merge(entity2);
        }
		
	}
	
	public PtJProtectApply findByCastBackProtectId(Long applyId) {
		PtJProtectApply instance = entityManager.find(PtJProtectApply.class, applyId);
		return instance;
	}
	
	@SuppressWarnings("unchecked")
	public PageObject getCastBackProtectApproveList(String inOut,String protectionType,String applyId, 
			String status, String entryIds,String enterpriseCode,int... rowStartIdxAndCount) {

		PageObject pg = new PageObject();
		String strWhere = " ";
        if(inOut!=null && !inOut.equals(""))
		   strWhere += " and t.in_out='"+inOut+"'\n";
       
        if(protectionType!=null && !protectionType.equals(""))
 		   strWhere += " and t.protection_type='"+protectionType+"'\n";
        
        if(applyId!=null && !applyId.equals(""))
  		   strWhere += " and t.apply_id='"+applyId+"'\n";
        
		if(status != null && status.equals("approve"))
		{
			strWhere += " and  t.status not in(9)\n" +
					" and  t.workflow_no in (" + entryIds + ") \n";
		}

		String sql = "select t.apply_id,\n" +
			"t.apply_code,\n" + 
			"t.in_out,\n" + 
			"t.protection_type,\n" + 
			"t.apply_dep,\n" + 
			"(select a.dept_name from hr_c_dept a where a.dept_id=t.apply_dep)deptName,\n" + 
			"t.apply_by,\n" + 
			"getworkername(t.apply_by)applyName,\n" + 
			"to_char(t.apply_time,'yyyy-mm-dd hh24:mi:ss')apply_time,\n" + 
			"t.protection_name,\n" + 
			"t.protection_reason,\n" + 
			"t.measures,\n" + 
			"t.workflow_no,\n" + 
			"t.status,\n" + 
			"decode(t.status,'0','未上报','1','已上报','2','检修公司高管已审批','3','设备部高管已审批','4','值长已审批','5','总工已审批','6','值长已二次复核','7','机组长、运行班长/程控班班长指定执行人已审批','8','审批结束','9','审批退回') statusName,\n" + 
			"t.class_leader,\n" + 
			"getworkername(t.class_leader)classLeaderName,\n" + 
			"t.executor,\n" + 
			"getworkername(t.executor)executorName,\n" + 
			"t.guardian,\n" + 
			"getworkername(t.guardian)guardianName,\n" + 
			"to_char(t.execute_time,'yyyy-mm-dd hh24:mi:ss')execute_time,\n" + 
			"t.memo,\n" + 
			"t.block_id\n" + 
			"from PT_J_PROTECT_APPLY t\n" + 
			"where t.is_use='Y'\n" + 
			"and t.enterprise_code='"+enterpriseCode+"'\n";

		sql += strWhere;
		String sqlCount = "select count(1) from PT_J_PROTECT_APPLY t where t.is_use='Y' and t.enterprise_code='"+enterpriseCode+"'" + strWhere;
		sql += "order by t.apply_id desc";
		List list = bll.queryByNativeSQL(sql, rowStartIdxAndCount);
		pg.setList(list);
		pg.setTotalCount(Long.parseLong(bll.getSingal(sqlCount).toString()));
		return pg;
	}
	
	@SuppressWarnings("unchecked")
	public void approveCastBackProtect(Long applyId,String workerCode, String entryId, String approveText, Long actionId,
			String responseDate, String nextRoles,String nextRolepPs,String eventIdentify) {
		PtJProtectApply entity = this.findByCastBackProtectId(applyId);
		if(actionId != null)
		{
			if(actionId == 42||actionId == 52||actionId == 62||actionId == 72||actionId == 82||actionId == 92||actionId == 102
				||actionId == 112||actionId == 122||actionId ==132||actionId == 142||actionId == 152||actionId == 162)
			{
				entity.setStatus("9");
			}else if(actionId == 45||actionId == 1112)
			{
				entity.setStatus("2");
			}else if(actionId == 56||actionId ==513 ||actionId == 126||actionId == 1213)
			{
				entity.setStatus("3");
			}else if(actionId == 67||actionId == 137)
			{
				entity.setStatus("4");
			}else if(actionId == 78||actionId == 714)
			{
				entity.setStatus("5");
			}else if(actionId == 89||actionId ==815 ||actionId ==1416||actionId == 1415)
			{
				entity.setStatus("6");
			}else if(actionId == 910||actionId == 1510||actionId == 1610)
			{
				entity.setStatus("7");
			}else if(actionId == 103)
			{
				entity.setStatus("8");
			}
		}
		
		boolean check1 = true;
		boolean check2 = true;
		boolean check3 = true;
		boolean check4 = false;
		boolean check5 = false;
		boolean check6 = false;
		boolean check7 = false;
		if(entity.getBlockId() != null && entity.getBlockId().equals("4")){
			check1 = false;	
			check2 = false;	
			check3 = false;	
		}
		if((entity.getProtectionType() != null && (entity.getProtectionType().equals("1")||entity.getProtectionType().equals("2")))&&
			(entity.getBlockId() != null && entity.getBlockId().equals("1"))){
			check4 = true;	
		}
		if((entity.getProtectionType() != null && (entity.getProtectionType().equals("3")||entity.getProtectionType().equals("4")))&&
				(entity.getBlockId() != null && entity.getBlockId().equals("1"))){
				check5 = true;	
			}
		if((entity.getProtectionType() != null && (entity.getProtectionType().equals("1")||entity.getProtectionType().equals("2")))&&
				(entity.getBlockId() != null && entity.getBlockId().equals("4"))){
				check6 = true;	
			}
		if((entity.getProtectionType() != null && (entity.getProtectionType().equals("3")||entity.getProtectionType().equals("4")))&&
					(entity.getBlockId() != null && entity.getBlockId().equals("4"))){
				check7 = true;	
				}
		Map m = new java.util.HashMap();
		m.put("DQZZSP", check1);
		m.put("RKZZSP", check2);
		m.put("ZZECFH", check3);
		m.put("300DQGGJZZ", check4);
		m.put("300RKGGCKBBZ", check5);
		m.put("125DQGGYXBZ", check6);
		m.put("125RKGGCKBBZ", check7);
		WorkflowService service = new WorkflowServiceImpl();
		service.doAction(Long.valueOf(entryId),workerCode,actionId,approveText,m,nextRoles,nextRolepPs);
		entityManager.merge(entity);
	}
}