package power.ejb.equ.failure;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;

import com.opensymphony.engineassistant.po.WorkflowActivity;
import com.opensymphony.engineassistant.po.WorkflowEvent;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

@Stateless
public class BqEquCFailureApproveImpl implements BqEquCFailureApprove {
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;
	protected EquJFailuresFacadeRemote fremote;
	protected EquJFailureHistoryFacadeRemote hisremote;
	public BqEquCFailureApproveImpl() {
		service = new WorkflowServiceImpl();
		fremote = (EquJFailuresFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("EquJFailuresFacade");
		hisremote = (EquJFailureHistoryFacadeRemote) Ejb3Factory.getInstance().getFacadeRemote("EquJFailureHistoryFacade");
	}
	
	/**
	 * 缺陷上报
	 * add by lyu @20090119
	 */
	public void failureReport(EquJFailures entity,String workflowType, Long actionId, String approveText, String nextRoles,String eventIdentify)
	{
		Long entryId = -1l;
		 EquJFailureHistory failurehis=new EquJFailureHistory();
		 failurehis.setApproveOpinion(approveText);
		 failurehis.setApproveTime(new Date());
		 failurehis.setApprovePeople(entity.getWriteBy());
		 failurehis.setFailureCode(entity.getFailureCode());
		 failurehis.setIsuse("Y");
		 failurehis.setEnterpriseCode(entity.getEntrepriseCode());
		if (entity.getWorkFlowNo() == null) {
			entryId = service.doInitialize(workflowType, entity.getWriteBy(), null);
			entity.setWorkFlowNo(entryId.toString());
			if(eventIdentify.equals("SB(DJQR)"))
			{
				entity.setWoStatus("2");// 状态修改为待确认
			}
			else
			{
				entity.setWoStatus("1");// 状态修改为待消缺
			}
			 failurehis.setApproveType("0");
		} else {
			entryId = Long.parseLong(entity.getWorkFlowNo());
			if(eventIdentify.equals("SB(DJQR)"))
			{
				entity.setWoStatus("2");// 状态修改为待确认
			}
			else
			{
				entity.setWoStatus("1");// 状态修改为待消缺
			}
			 failurehis.setApproveType("25");
		}
		entityManager.merge(entity);
		hisremote.save(failurehis);
		service.doAction(entryId, entity.getWriteBy(), actionId, approveText, null,nextRoles,"");
	}
	/**
	 * 缺陷审批
	 * @param failure
	 * @param failurehis
	 * @param actionId
	 * @param m
	 * @param nextRoles
	 */
	public void failureApprove(EquJFailures failure,EquJFailureHistory failurehis,Long actionId,Map m,String nextRoles)
	{
		hisremote.save(failurehis);
		fremote.update(failure);
		service.doAction(Long.parseLong(failure.getWorkFlowNo()), failurehis.getApprovePeople(), actionId, failurehis.getApproveOpinion(), m, nextRoles,"");
	}
	/**
	 * 缺陷运行验收
	 * @param failure
	 * @param failurehis
	 * @param actionId
	 * @param nextRoles
	 */
	public void failureRunAcceptance(EquJFailures entity,EquJFailureHistory failurehis,Long actionId,String nextRoles)
	{
		if(failurehis.getCheckQuality().equals("2"))
		{
			entity.setWoStatus("9");//状态修改为验收退回
			failurehis.setApproveType("9");
			EquJFailures model=new EquJFailures();
		}
		else
		{
			entity.setWoStatus("4");// 状态修改为运行已验收
			failurehis.setApproveType("4");
			if(entity.getUnqualifiedFailureCode() != null)
			{
				EquJFailures oldFailure=fremote.findFailureByCode(entity.getUnqualifiedFailureCode(), entity.getEntrepriseCode());
				oldFailure.setWoStatus("4");
				fremote.update(oldFailure);
			}
		}
		failurehis.setFailureCode(entity.getFailureCode());
		failurehis.setApproveTime(new Date());
		failurehis.setEnterpriseCode(entity.getEntrepriseCode());
		failurehis.setIsuse("Y");
		hisremote.save(failurehis);
		fremote.update(entity);
		service.doAction(Long.parseLong(entity.getWorkFlowNo()), failurehis.getApprovePeople(), actionId, failurehis.getApproveOpinion(), null, nextRoles,"");
	}
	/**
	 * 缺陷点检验收
	 * @param entity
	 * @param failurehis
	 * @param actionId
	 * @param nextRoles
	 */
	public void failureAcceptance(EquJFailures entity,EquJFailureHistory failurehis,Long actionId,String nextRoles)
	{
		if(failurehis.getCheckQuality().equals("2"))
		{
			entity.setWoStatus("9");//状态修改为验收退回
			failurehis.setApproveType("9");
		}
		else
		{
			entity.setWoStatus("14");// 生技部已验收状态修改为运行待验收
			failurehis.setApproveType("14");
		}
		failurehis.setFailureCode(entity.getFailureCode());
		failurehis.setApproveTime(new Date());
		failurehis.setEnterpriseCode(entity.getEntrepriseCode());
		failurehis.setIsuse("Y");
		hisremote.save(failurehis);
		fremote.update(entity);
		service.doAction(Long.parseLong(entity.getWorkFlowNo()), failurehis.getApprovePeople(), actionId, failurehis.getApproveOpinion(), null, nextRoles,"");
	}
	/**
	 * 获得审批方式列表
	 * 
	 * @param entityId
	 *            缺陷单
	 * @return List<WorkflowEvent>
	 */
	public List<WorkflowEvent> findActionList(Long entityId) {
		List<WorkflowEvent> actionList = service.getActions(entityId);
		return actionList;
	}
	
	/**
	 * 获得需要审批的相关URL
	 * @param entryId
	 * @param workerCode
	 * @return
	 */
	public List<WorkflowActivity> getCurrentStepsInfo(Long entryId,String workerCode){
		List<WorkflowActivity> list = service.getCurrentStepsInfo(entryId, workerCode);
		return list;
	}

}
