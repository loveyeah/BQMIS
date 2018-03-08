package power.ejb.workticket.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.opensymphony.engineassistant.po.WorkflowEvent;
import com.opensymphony.workflow.service.WorkflowService;
import com.opensymphony.workflow.service.WorkflowServiceImpl;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.comm.NativeSqlHelperRemote;
import power.ejb.workticket.form.WorkticketBusiStatus;

@Stateless
public class StandardTicketApproveImpl implements StandardTicketApprove{
	@SuppressWarnings("unused")
	@PersistenceContext
	private EntityManager entityManager;
	@EJB(beanName = "NativeSqlHelper")
	protected NativeSqlHelperRemote bll;
	protected RunJWorkticketsFacadeRemote baseRemote;
	protected RunJWorktickethisFacadeRemote hisRemote;
	WorkflowService service;
	
	public StandardTicketApproveImpl()
	{
		baseRemote = (RunJWorkticketsFacadeRemote) Ejb3Factory.getInstance()
		.getFacadeRemote("RunJWorkticketsFacade");
		hisRemote = (RunJWorktickethisFacadeRemote) Ejb3Factory.getInstance()
		.getFacadeRemote("RunJWorktickethisFacade");
		service = new WorkflowServiceImpl();
	}
	
	@SuppressWarnings("unchecked")
	public List<WorkticketBusiStatus> findAllBusiStatusList()
	{
		String sql=
			"select t.workticket_staus_id,t.workticket_status_name  from RUN_C_WORKTICKET_STANDSTATUS t order by t.workticket_staus_id";
		List list=bll.queryByNativeSQL(sql);
		List arraylist = new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			Object[] data=(Object[])it.next();
			WorkticketBusiStatus model=new WorkticketBusiStatus();
			if(data[0]!=null)
			{
				model.setWorkticketStausId(Long.parseLong(data[0].toString()));
			}
			if(data[1]!=null)
			{
				model.setWorkticketStatusName(data[1].toString());
			}
			arraylist.add(model);
			
		}
		return arraylist;

	}
	
	/**
	 * 上报列表的状态
	 * @return
	 */
	public List<WorkticketBusiStatus> findBusiStatuForReport()
	{
		return this.findBusiStatus("1,5");
	}
	/**
	 * 审批列表的状态
	 * @return
	 */
	public List<WorkticketBusiStatus> findBusiStatuForApprove()
	{
		return this.findBusiStatus("2,3,6");
	}
	
	
	@SuppressWarnings("unchecked")
	private List<WorkticketBusiStatus> findBusiStatus(String statuIds)
	{
		String sql=
			"select t.workticket_staus_id,t.workticket_status_name   from RUN_C_WORKTICKET_STANDSTATUS t  where t.workticket_staus_id in  ("+statuIds+")  order by t.workticket_staus_id";
		List list=bll.queryByNativeSQL(sql);
		List arraylist = new ArrayList();
		Iterator it=list.iterator();
		while(it.hasNext())
		{
			Object[] data=(Object[])it.next();
			WorkticketBusiStatus model=new WorkticketBusiStatus();
			if(data[0]!=null)
			{
				model.setWorkticketStausId(Long.parseLong(data[0].toString()));
			}
			if(data[1]!=null)
			{
				model.setWorkticketStatusName(data[1].toString());
			}
			arraylist.add(model);
			
		}
		return arraylist;
	}
	
	/**
	 * 获得审批方式列表
	 */
	@SuppressWarnings("unchecked")
	public List<WorkflowEvent> findActionList(String workticketNo)
	{
		RunJWorktickets entity=baseRemote.findById(workticketNo);
		if(entity!=null)
		{
			List<WorkflowEvent> actionList=new ArrayList();
			if(entity.getWorkFlowNo()!=null&&!entity.getWorkFlowNo().equals(""))
			{
		       actionList = service.getActions(entity.getWorkFlowNo()) ;
			}
			else
			{
				actionList=  service.findFirstStep("bqStandWorkticket",null).getActions();
			}
		 return actionList;
		}
		return null;
	}
	
	/**
	 * 上报
	 * @param workticketNo
	 * @param workerCode
	 * @param actionId
	 */
	public void reportTo(String workticketNo,String workerCode,Long actionId,String approveText,String eventIdentify,String nextRoles)
	{
		
		RunJWorktickets entity=baseRemote.findById(workticketNo);
		if(entity.getWorkFlowNo()==null)
		{
			long entryId = service.doInitialize("bqStandWorkticket",workerCode,workticketNo );
			service.doAction(entryId, workerCode, actionId, approveText, null,nextRoles); 
			entity.setWorkFlowNo(entryId);
		}
		else
		{
			this.changeWfInfo(entity.getWorkFlowNo(), workerCode, actionId, approveText, nextRoles);
		}
		
		entity.setWorkticketStausId(2l);
		baseRemote.update(entity);
	}
	
	/**
	 * 执行
	 * @param entryId 实例编号
	 * @param workerCode 审批人
	 * @param actionId   动作
	 * @param approveText 意见
	 */
	private void changeWfInfo(Long entryId,String workerCode,Long actionId,String approveText,String nextRoles)
	{
		service.doAction(entryId, workerCode, actionId, approveText, null,nextRoles,""); 
	}
	
	private Long getBusiStatus(String step, Long actionId)
	{
		if(step.equals("ahDept"))
		{
			if(actionId==45l) return 3l; //安环部已审批
			if(actionId==42l) return 5l; //退票
			if(actionId==64l) return 6l; //设备部已审批
			if(actionId==62l) return 5l; //退票
		}
		else if(step.equals("engineer"))
		{
			if(actionId==53l) return 4l; //已结束
			if(actionId==52l) return 5l; //退票
		}
		return null;
	}
	
	//安环部和设备部审批
	public void workticketAhDept(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles ) 
	{
		RunJWorktickets model=baseRemote.findById(workticketNo);
		if(model!=null)
		{
		Long entryId=model.getWorkFlowNo();
		Long status=getBusiStatus("ahDept", actionId);
		
		model.setWorkticketStausId(status);
		baseRemote.update(model);
		RunJWorktickethis hismodel=new RunJWorktickethis();
		hismodel.setWorkticketNo(workticketNo);
		hismodel.setApproveBy(workerCode);
		hismodel.setApproveText(approveText);
		hismodel.setApproveDate(new java.util.Date());
		hismodel.setApproveStatus(status.toString());
		hisRemote.save(hismodel);
		this.changeWfInfo(entryId, workerCode, actionId, approveText,nextRoles);
		}
	}
	
	//总工程师审批
	public void workticketEngineer(String workticketNo,String approveText,String workerCode,Long actionId,String responseDate,String nextRoles ) 
	{
		RunJWorktickets model=baseRemote.findById(workticketNo);
		if(model!=null)
		{
		Long entryId=model.getWorkFlowNo();
		Long status=getBusiStatus("engineer", actionId);
		
		model.setWorkticketStausId(status);
		baseRemote.update(model);
		RunJWorktickethis hismodel=new RunJWorktickethis();
		hismodel.setWorkticketNo(workticketNo);
		hismodel.setApproveBy(workerCode);
		hismodel.setApproveText(approveText);
		hismodel.setApproveDate(new java.util.Date());
		hismodel.setApproveStatus(status.toString());
		hisRemote.save(hismodel);
		this.changeWfInfo(entryId, workerCode, actionId, approveText,nextRoles);
		}
	}
}
