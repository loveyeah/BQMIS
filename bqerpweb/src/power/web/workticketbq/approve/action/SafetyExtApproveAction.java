package power.web.workticketbq.approve.action;

import java.text.ParseException;

import power.ejb.opticket.RunJOpticketFacadeRemote;
import power.ejb.workticket.business.BqWorkticketApprove;
import power.web.comm.AbstractAction;

public class SafetyExtApproveAction extends AbstractAction{

	private BqWorkticketApprove approve;
	public SafetyExtApproveAction()
	{
		approve=(BqWorkticketApprove)factory.getFacadeRemote("BqWorkticketApproveImpl");
	}
	
	public void safetyExeApprove() throws NumberFormatException, ParseException
	{
		String  eventIdentify=request.getParameter("eventIdentify");
		String workticketNo=request.getParameter("workticketNo");
		String approveText=request.getParameter("approveText");
		String workerCode=request.getParameter("workerCode");
		String actionId=request.getParameter("actionId");
		String responseDate=request.getParameter("responseDate");
		String nextRoles=request.getParameter("nextRoles");
		String workticketMemo=request.getParameter("workticketMemo");
		String msg=approve.workticketSafetyExe(workticketNo, approveText, workerCode, Long.parseLong(actionId), responseDate, nextRoles, eventIdentify, workticketMemo);
		write("{success:true,msg:'"+msg+"'}");
	}
	
	public void getRefOpticketNoForWorkticket()
	{
		String workticketNo=request.getParameter("workticketNo");
		RunJOpticketFacadeRemote opticketRemote=(RunJOpticketFacadeRemote)factory.getFacadeRemote("RunJOpticketFacade");
		String opticketNos= opticketRemote.findByWorktickectNo(workticketNo);
		write("{success:true,opticketNos:'"+opticketNos+"'}");
		
	}
}
