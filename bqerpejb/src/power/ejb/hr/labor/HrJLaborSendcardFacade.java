package power.ejb.hr.labor;

import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.hr.LogUtil;
import power.ejb.manage.plan.BpJPlanRepairDep;

/**
 * Facade for entity HrJLaborSendcard.
 * 
 * @see power.ejb.hr.labor.HrJLaborSendcard
 * @author MyEclipse Persistence Tools
 */
@Stateless
public class HrJLaborSendcardFacade implements HrJLaborSendcardFacadeRemote {

	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	private NativeSqlHelperRemote bll;
	WorkflowService service;
	public  HrJLaborSendcardFacade(){
		service = new WorkflowServiceImpl();
		
	}
	public List  getApprovelist(String sendYear,String sendKind,String entryIds,String enterpriseCode)
	{
		String sql=
			"select  a.sendcard_id," +
			"a.work_flow_no\n" +
			"from  hr_j_labor_sendcard  a\n" + 
			"where a.enterprise_code ='"+enterpriseCode+"'\n" +
			"and  a.is_use='Y'\n " +
			"and  a.send_year='"+sendYear+"'\n" + 
			"and  a.send_kind='"+sendKind+"'\n" + 
			"and a.work_flow_no  in ("+entryIds+")";
//		System.out.println("the sql"+sql);
       List list=bll.queryByNativeSQL(sql);
		return list;
		
	}
	public String  getSendCardStatus(String sendYear,String sendKind,String entryBy,String enterpriseCode)
	{
		String sql=
			"select " +
			"a.send_state  from hr_j_labor_sendcard  a\n" +
			"where a.send_year='"+sendYear+"'\n" + 
			"and a.send_kind='"+sendKind+"'\n" + 
			"and a.entry_by='"+entryBy+"'\n" + 
			"and a.enterprise_code='"+enterpriseCode+"'";

		Object obj  = bll.getSingal(sql);
		String status = (obj == null) ? "" : obj.toString();
		return status;
		
		
	}
	public HrJLaborSendcard save(HrJLaborSendcard entity)
	{
		LogUtil.log("saving HrJLaborSendcard instance", Level.INFO, null);
		try {
			Long mainID=bll.getMaxId("HR_J_LABOR_SENDCARD", "sendcard_id");
			entity.setSendcardId(bll.getMaxId("HR_J_LABOR_SENDCARD", "sendcard_id"));
			entity.setIsUse("Y");
			entity.setEntryDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			return entity ;
		} catch (RuntimeException re) {
			LogUtil.log("save failed", Level.SEVERE, re);
			throw re;
		}
	}
	public  Long  saveDetail(HrJLaborSendcard entity,String enterpriseCode,String workCode) {
		LogUtil.log("saving HrJLaborSendcard instance", Level.INFO, null);
		
			Long mainID=0L;
			HrJLaborSendcard  mod=	this.findMainInfo(entity.getSendYear(), entity.getSendKind(), enterpriseCode);
			if(mod==null)
			{
				 mainID=bll.getMaxId("HR_J_LABOR_SENDCARD", "sendcard_id");
			entity.setSendcardId(bll.getMaxId("HR_J_LABOR_SENDCARD", "sendcard_id"));
			entity.setIsUse("Y");
			entity.setEntryDate(new Date());
			entityManager.persist(entity);
			LogUtil.log("save successful", Level.INFO, null);
			
		} else
		{
			mainID=mod.getSendcardId();
			mod.setEntryBy(workCode);
			mod.setEntryDate(new Date());
			entityManager.merge(mod);
			
		}
			return mainID;
		
	}

	public void  sendCardReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType) 
	{
	
		HrJLaborSendcard model = this.findById(mainId);
		Long entryId;
		if (model.getWorkFlowNo() == null) {
			entryId = service.doInitialize(workflowType, workerCode, mainId
					.toString());
			model.setWorkFlowNo(entryId);
		} else {
			entryId = model.getWorkFlowNo();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,nextRoles, "");
		model.setSendState("1");
		this.update(model);
	}
	public void sendCardApprove(Long mainId,Long actionId,Long entryId, String workerCode,
			String approveText, String nextRoles)
	{
		HrJLaborSendcard model = this.findById(mainId);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 42l) {
			model.setSendState("3");
		} else if (actionId == 43l) {
			model.setSendState("2");
		}
		this.update(model);
	}
	
	public void delete(String ids) {
	
		String sql=
			"\n" +
			"update HR_J_LABOR_SENDCARD t\n" + 
			"set t.is_use='N'\n" + 
			"where t.sendcard_id in ("+ids+")";
		bll.exeNativeSQL(sql);

	}
	public HrJLaborSendcard findMainInfo(String  sendYear ,String sendKind,String enterpriseCode)
	{
		String sql=
			"select *\n" +
			"from HR_J_LABOR_SENDCARD t\n" + 
			" where t.is_use = 'Y'\n" +
			"and t.enterprise_code='"+enterpriseCode+"'" + 
			"and t.send_year = '"+sendYear+"'\n" + 
			"and t.send_kind = '"+sendKind+"'\n" ;

		List<HrJLaborSendcard> list=bll.queryByNativeSQL(sql, HrJLaborSendcard.class);
		if(list!=null&&list.size()>0)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}
	
	public HrJLaborSendcard update(HrJLaborSendcard entity) {
		LogUtil.log("updating HrJLaborSendcard instance", Level.INFO, null);
		try {
			HrJLaborSendcard result = entityManager.merge(entity);
			LogUtil.log("update successful", Level.INFO, null);
			return result;
		} catch (RuntimeException re) {
			LogUtil.log("update failed", Level.SEVERE, re);
			throw re;
		}
	}

	public HrJLaborSendcard findById(Long id) {
		LogUtil.log("finding HrJLaborSendcard instance with id: " + id,
				Level.INFO, null);
		try {
			HrJLaborSendcard instance = entityManager.find(
					HrJLaborSendcard.class, id);
			return instance;
		} catch (RuntimeException re) {
			LogUtil.log("find failed", Level.SEVERE, re);
			throw re;
		}
	}



	
	

}