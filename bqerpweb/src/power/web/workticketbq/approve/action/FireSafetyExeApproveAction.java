package power.web.workticketbq.approve.action;

import power.ejb.workticket.business.BqWorkticketApprove;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class FireSafetyExeApproveAction extends AbstractAction{
	private BqWorkticketApprove approve;
	public FireSafetyExeApproveAction()
	{
		approve=(BqWorkticketApprove)factory.getFacadeRemote("BqWorkticketApproveImpl");
	}
	
	public void safetyFireExeApprove()
	{
		String  eventIdentify=request.getParameter("eventIdentify");
		String workticketNo=request.getParameter("workticketNo");
		String approveText=request.getParameter("approveText");
		String workerCode=request.getParameter("workerCode");
		String actionId=request.getParameter("actionId");
		String responseDate=request.getParameter("responseDate");
		String nextRoles=request.getParameter("nextRoles");
		String safetyType=request.getParameter("safetyType");
		String msg= approve.fireSafetyExeApprove(safetyType, workticketNo, approveText, workerCode, Long.parseLong(actionId), responseDate, nextRoles, eventIdentify);
    	write("{success:true,msg:'"+msg+"'}");
	}
	
}
