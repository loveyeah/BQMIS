package power.ejb.manage.plan.itemplan;

import java.util.Date;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.manage.plan.trainplan.BpJTrainingMain;


@Stateless
public class ItemPlanSignManagerImpl implements ItemPlanSignManager{
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	WorkflowService service;
	
	public ItemPlanSignManagerImpl()
	{
		service = new WorkflowServiceImpl();
	}
	
	
	public void wholeItemPlanReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType) {
		
		BpJItemplanPlantMain model = entityManager.find(BpJItemplanPlantMain.class,mainId);
		Long entryId;
		if (model.getWorkflowNoPlan() == null) {
			entryId = service.doInitialize(workflowType, workerCode, mainId
					.toString());
			model.setWorkflowNoPlan(entryId);
		} else {
			entryId = model.getWorkflowNoPlan();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		model.setWorkflowStatusPlan(1L);
		entityManager.merge(model);
	}
	
	
	public void wholeItemPlanApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles) {
		BpJItemplanPlantMain model = entityManager.find(BpJItemplanPlantMain.class, mainId);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 42l) {
			model.setWorkflowStatusPlan(3l);
		} else if (actionId == 43l) {
			model.setWorkflowStatusPlan(2l);
		}
		entityManager.merge(model);
	}
	
	
	public void partItemPlanReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType) {
		
		BpJItemplanDepMain model = entityManager.find(BpJItemplanDepMain.class,mainId);
		Long entryId;
		if (model.getWorkflowNoPlan() == null) {
			entryId = service.doInitialize(workflowType, workerCode, mainId
					.toString());
			model.setWorkflowNoPlan(entryId);
		} else {
			entryId = model.getWorkflowNoPlan();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		model.setWorkflowStatusPlan(1L);
		entityManager.merge(model);
	}
	
	public void partItemPlanApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles) {
		BpJItemplanDepMain model = entityManager.find(BpJItemplanDepMain.class, mainId);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 42l||actionId == 62l) {
			model.setWorkflowStatusPlan(3l);
		} else if (actionId == 63l) {
			model.setWorkflowStatusPlan(2l);
		}
		entityManager.merge(model);
	}
	
	public void wholeItemFactReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType) {
		
		BpJItemplanPlantMain model = entityManager.find(BpJItemplanPlantMain.class,mainId);
		Long entryId;
		if (model.getWorkflowNoFact() == null) {
			entryId = service.doInitialize(workflowType, workerCode, mainId
					.toString());
			model.setWorkflowNoFact(entryId);
		} else {
			entryId = model.getWorkflowNoFact();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		model.setWorkflowStatusFact(1l);
		entityManager.merge(model);
	}
	
	
	public void wholeItemFactApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles) {
		BpJItemplanPlantMain model = entityManager.find(BpJItemplanPlantMain.class, mainId);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 42l) {
			model.setWorkflowStatusFact(3l);
		} else if (actionId == 43l) {
			model.setWorkflowStatusFact(2l);
		}
		entityManager.merge(model);
	}
	
	public void partItemFactReport(Long mainId, Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType) {
		
		BpJItemplanDepMain model = entityManager.find(BpJItemplanDepMain.class,mainId);
		Long entryId;
		if (model.getWorkflowNoFact() == null) {
			entryId = service.doInitialize(workflowType, workerCode, mainId
					.toString());
			model.setWorkflowNoFact(entryId);
		} else {
			entryId = model.getWorkflowNoFact();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		model.setWorkflowStatusFact(1L);
		entityManager.merge(model);
	}
	
	public void partItemFactApprove(Long mainId, Long actionId, Long entryId,
			String workerCode, String approveText, String nextRoles) {
		BpJItemplanDepMain model = entityManager.find(BpJItemplanDepMain.class, mainId);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 42l || actionId == 52l) {
			model.setWorkflowStatusFact(3l);
		} else if (actionId == 53l) {
			model.setWorkflowStatusFact(2l);
		}
		entityManager.merge(model);
	}
	//技术
	public void fillTecItemReport(Long TecmainId,Long actionId, String workerCode,
			String approveText, String nextRoles, String workflowType){
		BpJItemplanTecMain model = entityManager.find(BpJItemplanTecMain.class,TecmainId);
		Long entryId;
		if (model.getWorkflowNoPlan() == null) {
			entryId = service.doInitialize(workflowType, workerCode, TecmainId
					.toString());
			model.setWorkflowNoPlan(entryId);
		} else {
			entryId = model.getWorkflowNoPlan();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		model.setWorkflowStatusPlan(1l);
		entityManager.merge(model);
	}
	
	public void fillTecItemApprove(Long TecmainId,Long actionId,Long entryId, String workerCode,
			String approveText, String nextRoles){
		BpJItemplanTecMain entity = entityManager.find(BpJItemplanTecMain.class,TecmainId);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 42l||actionId == 52l) {
			entity.setWorkflowStatusPlan(3l);
		} else if (actionId == 53l) {
			entity.setWorkflowStatusPlan(2l);
			entity.setWorkflowStatusFact(0l);
		}
		entityManager.merge(entity);
	}
	public void TecItemFinishReport(Long TecmainId, Long actionId,
			String workerCode, String approveText, String nextRoles,
			String workflowType){
		BpJItemplanTecMain entity = entityManager.find(BpJItemplanTecMain.class, TecmainId);
		Long entryId;
		if(entity.getWorkflowNoFact() == null){
			entryId = service.doInitialize(workflowType, workerCode, TecmainId.toString());
			entity.setWorkflowNoFact(entryId);
		}else{
			entryId = entity.getWorkflowNoFact();
		}
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		entity.setWorkflowStatusFact(1l);
		entityManager.merge(entity);
		
	}
	public void TecItemFinishApprove(Long TecmainId,Long actionId,Long entryId, String workerCode,
			String approveText, String nextRoles){
		BpJItemplanTecMain entity = entityManager.find(BpJItemplanTecMain.class,TecmainId);
		service.doAction(entryId, workerCode, actionId, approveText, null,
				nextRoles, "");
		if (actionId == 42l||actionId == 52l) {
			entity.setWorkflowStatusFact(3l);
		} else if (actionId == 53l) {
			entity.setWorkflowStatusFact(2l);
		}
		entityManager.merge(entity);
	}
}
