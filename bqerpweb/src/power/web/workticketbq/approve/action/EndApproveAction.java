package power.web.workticketbq.approve.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Map;

import power.ear.comm.ejb.Ejb3Factory;
import power.ejb.webservice.run.ticketmanage.WsJTask;
import power.ejb.webservice.run.ticketmanage.WsJTaskFacadeRemote;
import power.ejb.workticket.business.BqWorkticketApprove;
import power.ejb.workticket.business.RunJWorktickethis;
import power.ejb.workticket.business.RunJWorktickethisFacadeRemote;
import power.ejb.workticket.business.RunJWorkticketsFacadeRemote;
import power.ejb.workticket.form.WorkticketHisForPrint;
import power.ejb.workticket.form.WorkticketInfo;
import power.web.comm.AbstractAction;
import service.ProductServiceManager;
import service.ProductServiceManagerImpl;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

public class EndApproveAction extends AbstractAction{

	private BqWorkticketApprove approve;
	private RunJWorktickethisFacadeRemote hisRemote;
	private  RunJWorktickethis endHisModel;
	private  RunJWorktickethis pemitHisModel;
	private  RunJWorktickethis changeChargeModel;
	private  RunJWorktickethis delayHisModel;
	private  RunJWorktickethis safetyExeHisModel;
	private ProductServiceManager productManager;
	private RunJWorkticketsFacadeRemote workticketRemote;
	private WsJTaskFacadeRemote taskRemote;// add by ywliu 090513
	
	public EndApproveAction()
	{
		approve=(BqWorkticketApprove)factory.getFacadeRemote("BqWorkticketApproveImpl");
		hisRemote = (RunJWorktickethisFacadeRemote) Ejb3Factory.getInstance()
		.getFacadeRemote("RunJWorktickethisFacade");
		workticketRemote=(RunJWorkticketsFacadeRemote)factory.getFacadeRemote("RunJWorkticketsFacade");
		taskRemote = (WsJTaskFacadeRemote) factory.getFacadeRemote("WsJTaskFacade");// add by ywliu 090513
	}
	public void endApprove()
	{

		// add by fyyang 090310
		String changeLabelStatus=request.getParameter("changeLabelStatus");
		String delayLabelStatus=request.getParameter("delayLabelStatus");
		String  eventIdentify=request.getParameter("eventIdentify");
		String workticketNo=request.getParameter("workticketNo");
		String actionId=request.getParameter("actionId");
		String responseDate=request.getParameter("responseDate");
		String nextRoles=request.getParameter("nextRoles");
		String workticketMemo=request.getParameter("workticketMemo");
		String workerCode=request.getParameter("workerCode");
		endHisModel.setWorkticketNo(workticketNo);
		endHisModel.setApproveBy(workerCode);
		pemitHisModel.setWorkticketNo(workticketNo);
		changeChargeModel.setWorkticketNo(workticketNo);
		delayHisModel.setWorkticketNo(workticketNo);
		safetyExeHisModel.setWorkticketNo(workticketNo);
		if(changeLabelStatus.equals("false"))
		{
			changeChargeModel=null;
		}
		if(delayLabelStatus.equals("false"))
		{
			delayHisModel=null;
		}
		
		String msg=approve.workticketEndApprove(endHisModel, pemitHisModel, changeChargeModel, delayHisModel,safetyExeHisModel, Long.parseLong(actionId), nextRoles, eventIdentify, workticketMemo, responseDate);
		write("{success:true,msg:'"+msg+"'}");
		WorkticketInfo model=	workticketRemote.queryWorkticket(employee.getEnterpriseCode(), workticketNo);
		if(eventIdentify.equals("ZJ"))
		{
			
			productManager = new ProductServiceManagerImpl();
			String wsAddr = request.getSession().getServletContext().getInitParameter("productWebServiceAddr");
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			productManager.DisuseTicket(wsAddr, employee.getEnterpriseCode(), employee.getWorkerCode(), model.getModel().getApplyNo(), sdf.format(endHisModel.getOldApprovedFinishDate()));
		} else {
			productManager = new ProductServiceManagerImpl();
			String wsAddr = request.getSession().getServletContext().getInitParameter("productWebServiceAddr");
			productManager.BeforeDestroyTask(wsAddr, employee.getEnterpriseCode(), employee.getWorkerCode(), model.getModel().getApplyNo(), "1", workticketMemo);
			taskRemote.delete(model.getModel().getApplyNo());// add by ywliu 090513
		}
	}
	
	public void findReturnBackList() throws ParseException, JSONException
	{
		String workticketNo=request.getParameter("workticketNo");
		List<WorkticketHisForPrint> list=approve.findReturnBackList(workticketNo);
		String str = JSONUtil.serialize(list);
		write(str);
	}
	
	@SuppressWarnings("unchecked")
	public void saveReturnBackInfo() throws JSONException, ParseException
	{
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String str=request.getParameter("data");
		   Object obj = JSONUtil.deserialize(str);
		   List<Map> list = (List<Map>)obj;
		   for(Map data : list)
		   {
			   String strId = ((Map) ((Map) data)).get("id").toString();
			   String ApprovedDate=((Map) ((Map) data)).get("oldApprovedFinishDate").toString();
			   String newChargeBy=((Map) ((Map) data)).get("newChargeBy").toString();
			   String oldChargeBy=((Map) ((Map) data)).get("oldChargeBy").toString();
			   String workticketNo=((Map) ((Map) data)).get("workticketNo").toString();
			   String changeStatus=((Map) ((Map) data)).get("changeStatus").toString();
			   if(strId!=null&&!strId.equals(""))
			   {
			   RunJWorktickethis model=hisRemote.findById(Long.parseLong(strId));
			   if(ApprovedDate!=null&&!ApprovedDate.equals(""))
			   {
				   model.setOldApprovedFinishDate(df.parse(ApprovedDate));
			   }
			   if(workticketNo!=null&&!workticketNo.equals(""))
			   {
				   model.setWorkticketNo(workticketNo);
			   }
			   if(oldChargeBy!=null&&!oldChargeBy.equals(""))
			   {
			    model.setOldChargeBy(oldChargeBy);
			   }
			   if(newChargeBy!=null&&!newChargeBy.equals(""))
			   {
				   model.setNewChargeBy(newChargeBy);
			   }
			    if(changeStatus!=null&&!changeStatus.equals(""))
			    {
			    model.setChangeStatus(changeStatus);
			    }
			    hisRemote.update(model);
			   }
			   else
			   {
				   RunJWorktickethis model=new RunJWorktickethis();
				   if(ApprovedDate!=null&&!ApprovedDate.equals(""))
				   {
					   model.setOldApprovedFinishDate(df.parse(ApprovedDate));
				   }
				   if(workticketNo!=null&&!workticketNo.equals(""))
				   {
					   model.setWorkticketNo(workticketNo);
				   }
				   if(oldChargeBy!=null&&!oldChargeBy.equals(""))
				   {
				    model.setOldChargeBy(oldChargeBy);
				   }
				   if(newChargeBy!=null&&!newChargeBy.equals(""))
				   {
					   model.setNewChargeBy(newChargeBy);
				   }
				    if(changeStatus!=null&&!changeStatus.equals(""))
				    {
				    model.setChangeStatus(changeStatus);
				    }
				    hisRemote.save(model);
			   }
		   }
		   write("{success:true,msg:'保存成功！'}");
	}
	
	public void deleteReturnBackInfo()
	{
		String ids= request.getParameter("ids");
	    String [] hisIds= ids.split(",");
			for(int i=0;i<hisIds.length;i++)
			{
			RunJWorktickethis model=hisRemote.findById(Long.parseLong(hisIds[i]));
			hisRemote.delete(model);
			}
			 write("{success:true,msg:'删除成功！'}");
	}
	
	
	
	public RunJWorktickethis getEndHisModel() {
		return endHisModel;
	}
	public void setEndHisModel(RunJWorktickethis endHisModel) {
		this.endHisModel = endHisModel;
	}
	public RunJWorktickethis getPemitHisModel() {
		return pemitHisModel;
	}
	public void setPemitHisModel(RunJWorktickethis pemitHisModel) {
		this.pemitHisModel = pemitHisModel;
	}
	public RunJWorktickethis getChangeChargeModel() {
		return changeChargeModel;
	}
	public void setChangeChargeModel(RunJWorktickethis changeChargeModel) {
		this.changeChargeModel = changeChargeModel;
	}
	public RunJWorktickethis getDelayHisModel() {
		return delayHisModel;
	}
	public void setDelayHisModel(RunJWorktickethis delayHisModel) {
		this.delayHisModel = delayHisModel;
	}
	public RunJWorktickethis getSafetyExeHisModel() {
		return safetyExeHisModel;
	}
	public void setSafetyExeHisModel(RunJWorktickethis safetyExeHisModel) {
		this.safetyExeHisModel = safetyExeHisModel;
	}
}
