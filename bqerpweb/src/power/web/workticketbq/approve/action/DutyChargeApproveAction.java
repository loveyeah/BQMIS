package power.web.workticketbq.approve.action;

import java.text.ParseException;

import power.ejb.workticket.business.BqWorkticketApprove;
import power.web.comm.AbstractAction;

public class DutyChargeApproveAction extends AbstractAction{

	private BqWorkticketApprove approve;
	public DutyChargeApproveAction()
	{
		approve=(BqWorkticketApprove)factory.getFacadeRemote("BqWorkticketApproveImpl");
	}
	
	public void dutyChargeApprove() throws NumberFormatException, ParseException
	{
		String  eventIdentify=request.getParameter("eventIdentify");
		String workticketNo=request.getParameter("workticketNo");
		String approveText=request.getParameter("approveText");
		String workerCode=request.getParameter("workerCode");
		String actionId=request.getParameter("actionId");
		String responseDate=request.getParameter("responseDate");
		String nextRoles=request.getParameter("nextRoles");
		String approveFinishDate=request.getParameter("approveFinishDate");
		approve.workticketDutyChargeConfirm(workticketNo, approveText, workerCode, Long.parseLong(actionId), responseDate, nextRoles, eventIdentify, approveFinishDate);
		write("{success:true,msg:'审批成功！'}");
	}
}
