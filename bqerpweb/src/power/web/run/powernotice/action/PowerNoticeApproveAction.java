package power.web.run.powernotice.action;

import java.text.ParseException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.powernotice.RunCPowerNoticeFacadeRemote;
import power.ejb.run.powernotice.RunJPowerNoticeApprove;
import power.web.comm.AbstractAction;

public class PowerNoticeApproveAction extends AbstractAction{
	protected RunCPowerNoticeFacadeRemote remote;  
	private RunJPowerNoticeApprove power;
	public PowerNoticeApproveAction()
	{
		remote=(RunCPowerNoticeFacadeRemote)factory.getFacadeRemote("RunCPowerNoticeFacade");
	}
	
	public void findApproveList() throws JSONException
	{
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
		WorkflowService workflowService = new com.opensymphony.workflow.service.WorkflowServiceImpl();
		String entryIds = workflowService.getAvailableWorkflow(new String[] {
				"bqStopSendElec"}, employee.getWorkerCode());
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findApproveList(employee.getEnterpriseCode(), null, entryIds,start,limit);
	    }
	    else
	    {
	    	obj=remote.findApproveList(employee.getEnterpriseCode(), null,entryIds);
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	 /**
	   * 上报
	   */
	  public void reportTo()
	  {
		  String actionId=request.getParameter("actionId");
		  String busitNo=request.getParameter("busitNo");
		  String flowCode=request.getParameter("flowCode");
		  String workerCode=request.getParameter("workerCode");
		  remote.reportTo(busitNo, flowCode, workerCode,Long.parseLong(actionId));
		  write("{success:true,msg:'&nbsp&nbsp&nbsp上报成功&nbsp&nbsp&nbsp'}");
	  }
	
	public void approveSign() throws NumberFormatException, ParseException
	{
		String  eventIdentify=request.getParameter("eventIdentify");
		String noticeNo=request.getParameter("noticeNo");
		String workflowNo=request.getParameter("workflowNo");
		String workerCode=request.getParameter("workerCode");
		String actionId=request.getParameter("actionId");
		String responseDate=request.getParameter("responseDate");
		String nextRoles=request.getParameter("nextRoles");
		power.setEnterpriseCode(employee.getEnterpriseCode());
		remote.approveSign(power, noticeNo, workflowNo, workerCode,Long.parseLong(actionId), responseDate, nextRoles,eventIdentify);
		  write("{success:true,msg:'审批成功！'}");
	}
	
	public void findQueryList() throws JSONException
	{
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    String sdate=request.getParameter("sdate");
	    String edate=request.getParameter("edate");
	    String busiState=request.getParameter("busiState");
	    
	    String apply=request.getParameter("apply");
	    String teamValue=request.getParameter("teamValue");

	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findQueryList(employee.getEnterpriseCode(), sdate, edate, null, busiState,teamValue,apply, start,limit);
	    }
	    else
	    {
	    	obj=remote.findQueryList(employee.getEnterpriseCode(), sdate, edate, null, busiState,teamValue,apply);
	    }
	    String str=JSONUtil.serialize(obj);
		write(str);
	}

	public void queryData()throws JSONException{
	
	    
	    PageObject obj=new  PageObject();
	    obj=remote.findData();
	    String str=JSONUtil.serialize(obj);
		write(str);
		
	}
	public RunJPowerNoticeApprove getPower() {
		return power;
	}

	public void setPower(RunJPowerNoticeApprove power) {
		this.power = power;
	}
}
