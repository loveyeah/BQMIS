package power.web.workticketbq.approve.action;

import power.ejb.workticket.business.BqWorkticketApprove;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.form.WorkticketInfo;
import power.web.comm.AbstractAction;
import service.ProductServiceManager;
import service.ProductServiceManagerImpl;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class BqCommApproveAction extends AbstractAction{
	
	private BqWorkticketApprove approve;
	private RunJWorkticketsFacadeRemote workticketRemote;
	private ProductServiceManager productManager;
	
	public BqCommApproveAction(){
		approve=(BqWorkticketApprove)factory.getFacadeRemote("BqWorkticketApproveImpl");
		workticketRemote=(RunJWorkticketsFacadeRemote)factory.getFacadeRemote("RunJWorkticketsFacade");
		 
	}
	
	
	public void workticketCommApprove()
	{
		String  eventIdentify=request.getParameter("eventIdentify");
		String workticketNo=request.getParameter("workticketNo");
		String approveText=request.getParameter("approveText");
		String workerCode=request.getParameter("workerCode");
		String actionId=request.getParameter("actionId");
		String responseDate=request.getParameter("responseDate");
		String nextRoles=request.getParameter("nextRoles");
		
		// 选择操作票 add by bjxu 20100113
		String workticketMemo=request.getParameter("workticketMemo");
		String msg=approve.workticketSign(workticketNo, approveText, workerCode,Long.parseLong(actionId), responseDate, nextRoles, eventIdentify,workticketMemo);
		write("{success:true,msg:'"+msg+"'}");
		WorkticketInfo model=	workticketRemote.queryWorkticket(employee.getEnterpriseCode(), workticketNo);
		if(actionId.equals("910")&&!model.getModel().getWorkticketTypeCode().equals("4"))
		{
		if( model.getModel().getApplyNo() == null || model.getModel().getApplyNo().trim().equals(""))
			return ;
		productManager = new ProductServiceManagerImpl();
		String wsAddr = request.getSession().getServletContext().getInitParameter("productWebServiceAddr");
		//两票系统创建（开票或重开票）一张票，修改系统中所对应的任务信息
		productManager.CreateTicket(wsAddr, employee.getEnterpriseCode(),
		employee.getWorkerCode(), model.getModel().getApplyNo(),
		workticketNo, model.getModel().getIsCreatebyStand(), "1", model.getModel().getWorkticketTypeCode(), model.getWorkticketTypeName(), "WatchTypeID",
		"WatchTypeName", model.getModel().getEquAttributeCode(), model.getBlockName());
		}
	}
	
	   /**
     * 通过工作票编号取得所有工作票基本信息
     * 
     * @throws JSONException
     */
    public void getWorkticketBaseInfoByNo() throws JSONException {
       
    
    	 // 工作票信息远程处理对象
    	RunJWorkticketsFacadeRemote  remoteWorktickets = (RunJWorkticketsFacadeRemote) factory
                .getFacadeRemote("RunJWorkticketsFacade");
        // 工作票编号
        String workticketNo = request.getParameter("workticketNo");
        // 工作票信息部分汉字名
        WorkticketInfo workticketInfo = remoteWorktickets.queryWorkticket(
                employee.getEnterpriseCode(), workticketNo);
        String strBaseInfo = JSONUtil.serialize(workticketInfo);
        write("{success: true,data:" + strBaseInfo + "}");
    }

}
