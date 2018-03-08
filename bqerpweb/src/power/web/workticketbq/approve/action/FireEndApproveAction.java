package power.web.workticketbq.approve.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import power.ejb.webservice.run.ticketmanage.WsJTaskFacadeRemote;
import power.ejb.workticket.business.BqWorkticketApprove;
import power.ejb.workticket.business.RunJWorktickethis;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.form.WorkticketInfo;
import power.web.comm.AbstractAction;
import service.ProductServiceManager;
import service.ProductServiceManagerImpl;

@SuppressWarnings("serial")
public class FireEndApproveAction extends AbstractAction{

	private BqWorkticketApprove approve;
	private RunJWorktickethis hisModel;
	private ProductServiceManager productManager;
	private RunJWorkticketsFacadeRemote workticketRemote;
	private WsJTaskFacadeRemote taskRemote;// add by ywliu 090513
	
	public FireEndApproveAction()
	{
		approve=(BqWorkticketApprove)factory.getFacadeRemote("BqWorkticketApproveImpl");
		workticketRemote=(RunJWorkticketsFacadeRemote)factory.getFacadeRemote("RunJWorkticketsFacade");
		taskRemote = (WsJTaskFacadeRemote) factory.getFacadeRemote("WsJTaskFacade");// add by ywliu 090513
	}
	public void fireEndApprove() throws NumberFormatException, ParseException
	{
		String  eventIdentify=request.getParameter("eventIdentify");
		String workticketNo=request.getParameter("workticketNo");
		String actionId=request.getParameter("actionId");
		String responseDate=request.getParameter("responseDate");
		String nextRoles=request.getParameter("nextRoles");
		String workerCode=request.getParameter("workerCode");
		String approveText=request.getParameter("approveText");
		hisModel.setWorkticketNo(workticketNo);
		hisModel.setApproveBy(workerCode);
		hisModel.setApproveText(approveText);
		approve.fireEndApprove(hisModel, Long.parseLong(actionId), responseDate, nextRoles, eventIdentify);
		write("{success:true,msg:'审批成功！'}");
		WorkticketInfo model=	workticketRemote.queryWorkticket(employee.getEnterpriseCode(), workticketNo);
		if(eventIdentify.equals("ZJ"))
		{
			
			productManager = new ProductServiceManagerImpl();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String wsAddr = request.getSession().getServletContext().getInitParameter("productWebServiceAddr");
			productManager.DisuseTicket(wsAddr, employee.getEnterpriseCode(), employee.getWorkerCode(), model.getModel().getApplyNo(), sdf.format(hisModel.getOldApprovedFinishDate()));
		} else {
			productManager = new ProductServiceManagerImpl();
			String wsAddr = request.getSession().getServletContext().getInitParameter("productWebServiceAddr");
			productManager.BeforeDestroyTask(wsAddr, employee.getEnterpriseCode(), employee.getWorkerCode(), model.getModel().getApplyNo(), "1", approveText);
			taskRemote.delete(model.getModel().getApplyNo());// add by ywliu 090513
		}
	}
	
	public void createTicket() {
		String workticketNo = request.getParameter("workticketNo");
		WorkticketInfo model=	workticketRemote.queryWorkticket(employee.getEnterpriseCode(), workticketNo);
		if(model.getModel().getApplyNo() == null || model.getModel().getApplyNo().trim().equals(""))
			return;
		productManager = new ProductServiceManagerImpl();
		String wsAddr = request.getSession().getServletContext().getInitParameter("productWebServiceAddr");
		//两票系统创建（开票或重开票）一张票，修改系统中所对应的任务信息
		productManager.CreateTicket(wsAddr, employee.getEnterpriseCode(),
		employee.getWorkerCode(), model.getModel().getApplyNo(),
		workticketNo, model.getModel().getIsCreatebyStand(), "1", model.getModel().getWorkticketTypeCode(), model.getWorkticketTypeName(), "WatchTypeID",
		"WatchTypeName", model.getModel().getEquAttributeCode(), model.getBlockName());
		
	}
	
	public RunJWorktickethis getHisModel() {
		return hisModel;
	}
	public void setHisModel(RunJWorktickethis hisModel) {
		this.hisModel = hisModel;
	}
}
