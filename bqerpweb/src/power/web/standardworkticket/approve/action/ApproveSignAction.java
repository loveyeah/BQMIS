package power.web.standardworkticket.approve.action;

import java.util.Iterator;
import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.engineassistant.po.WorkflowEvent;

import power.ear.comm.ejb.PageObject;
import power.ejb.workticket.business.RunJWorkticketDangerFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketSafety;
import power.ejb.workticket.business.RunJWorktickets;
import power.ejb.workticket.business.StandardTicketApprove;
import power.ejb.workticket.business.WorkticketManager;
import power.web.comm.AbstractAction;
import power.web.comm.Constants;

public class ApproveSignAction extends AbstractAction {

	private StandardTicketApprove approve;
	 private WorkticketManager workticketManageRemote;
	private RunJWorkticketDangerFacadeRemote dangerRemote;
	 /** 工作票号 */
    private String workticketNo;

    /** 备注 */
    private String approveText;

    /** 签字人的工号 */
    private String workerCode;

    /** radio的value值 */
    private String actionId;

    /** 响应时间 */
    private String responseDate;

    /** 下步角色 */
    private String nextRoles;
	public ApproveSignAction()
	{
		approve=(StandardTicketApprove)factory.getFacadeRemote("StandardTicketApproveImpl");
	    workticketManageRemote = (WorkticketManager) factory
        .getFacadeRemote("WorkticketManagerImpl");
	    dangerRemote=(RunJWorkticketDangerFacadeRemote)factory.getFacadeRemote("RunJWorkticketDangerFacade");
	}
	public String getWorkticketNo() {
		return workticketNo;
	}
	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}
	public String getApproveText() {
		return approveText;
	}
	public void setApproveText(String approveText) {
		this.approveText = approveText;
	}
	public String getWorkerCode() {
		return workerCode;
	}
	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}
	public String getActionId() {
		return actionId;
	}
	public void setActionId(String actionId) {
		this.actionId = actionId;
	}
	public String getResponseDate() {
		return responseDate;
	}
	public void setResponseDate(String responseDate) {
		this.responseDate = responseDate;
	}
	public String getNextRoles() {
		return nextRoles;
	}
	public void setNextRoles(String nextRoles) {
		this.nextRoles = nextRoles;
	}
	
	/**
	 * 获得审批方式列表
	 * @throws JSONException
	 */
	  public void getApproveMethod() throws JSONException {
	        List<WorkflowEvent> list =approve.findActionList(workticketNo);
	        write(JSONUtil.serialize(list));
	    }
	  
//	    /**
//	     * 安措明细列表查询
//	     * 
//	     * @throws JSONException
//	     */
//	    public void getReceiveSafetyDetails() throws JSONException {
//	    	String safetyCode=request.getParameter("safetyCode");
//	        PageObject result = workticketManageRemote.findSafetyDetailList(
//	                Constants.ENTERPRISE_CODE, workticketNo, safetyCode);
//	        List list = result.getList();
//	        Iterator it = list.iterator();
//	        while (it.hasNext()) {
//	            RunJWorkticketSafety data = (RunJWorkticketSafety) it.next();
//	            if (data.getIsReturn() != null) {
//	                if (Constants.IS_RETURN_Y.equals(data.getIsReturn().toString())) {
//	                    data.setIsReturn("是换行");
//	                } else {
//	                    data.setIsReturn("不换行");
//	                }
//	            }
//	        }
//	        write(JSONUtil.serialize(result));
//	    }
	    
//	    /**
//		 * 危险点列表
//		 * @throws JSONException
//		 */
//		public void findDangerList() throws JSONException
//		{
//			String workticketNo=request.getParameter("workticketNo");
//			 PageObject obj=new  PageObject();
//			 obj=dangerRemote.findDangerList(workticketNo);
//			 String str=JSONUtil.serialize(obj);
//			 write(str);
//			
//		}
		
		public void report()
		{
		try{
			String eventIdentify=request.getParameter("eventIdentify");
			approve.reportTo(workticketNo, workerCode, Long.parseLong(actionId), approveText, eventIdentify, nextRoles);
			//approve.reportTo(workticketNo, workerCode, Long.parseLong(actionId));
			write("{success:true,msg:'上报成功！'}");
			
		} catch (Exception e) {
			write("{success:true,msg:'上报失败！'}");
		}
		}
		
		/**
		 * 安环部门审批
		 */
		public void ahDeptApprove()
		{
			try{
			approve.workticketAhDept(workticketNo, approveText, workerCode, Long.parseLong(actionId), responseDate, nextRoles);
		  } catch (Exception e) {
			  write("{success:true,msg:'审批失败！'}");
		   }
		}
		
		/**
		 * 总工程师审批
		 */
		public void engineerApprove()
		{
			try{
			approve.workticketEngineer(workticketNo, approveText, workerCode, Long.parseLong(actionId), responseDate, nextRoles);
			} catch (Exception e) {
				 write("{success:true,msg:'审批失败！'}");
			}
		}
		
		/**
		 * 批量审批 add by fyyang 090904
		 */
		public void multiApprove()
		{
			String [] nos=workticketNo.split(",");
			for(int i=0;i<nos.length;i++)
			{
				RunJWorktickets model=workticketManageRemote.findWorkticketByNo(nos[i]);
				if(model.getWorkticketStausId()==2||model.getWorkticketStausId()==6)
				{
					approve.workticketAhDept(nos[i], approveText, workerCode,Long.parseLong(actionId), responseDate, nextRoles);
				}
				else if(model.getWorkticketStausId()==3)
				{
					approve.workticketEngineer(nos[i], approveText, workerCode, Long.parseLong(actionId), responseDate, nextRoles);
				}
			}
			 write("{success:true,msg:'审批成功！'}");
		}
	  
	  
	  
	
}
