package power.web.workticket.query.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ejb.workticket.business.RunJWorktickethis;
import power.ejb.workticket.business.RunJWorktickethisFacadeRemote;
import power.ejb.workticket.form.workticketHisInfo;
import power.web.comm.AbstractAction;

public class WorkticketApproveUpdate extends AbstractAction{

	private RunJWorktickethisFacadeRemote remote;
	private  RunJWorktickethis endHisModel;
	private  RunJWorktickethis pemitHisModel;
	private  RunJWorktickethis changeChargeModel;
	private  RunJWorktickethis delayHisModel;
	private  RunJWorktickethis safetyExeHisModel;
	private RunJWorktickethis dhModel;
	public  WorkticketApproveUpdate()
	{
		remote=(RunJWorktickethisFacadeRemote)factory.getFacadeRemote("RunJWorktickethisFacade");
	}
	
	public void findApproveInfoForUpdate() throws JSONException
	{
		String workticketNo =request.getParameter("workticketNo");
		List<workticketHisInfo> list=remote.findHisInfo(workticketNo);
		write("{list:"+JSONUtil.serialize(list)+"}");
	}
	
	public void updateApproveInfo()
	{
		String workticketNo =request.getParameter("workticketNo");
		String workticketTypeCode=request.getParameter("workticketTypeCode");
		String changeLabelStatus=request.getParameter("changeLabelStatus");
		String delayLabelStatus=request.getParameter("delayLabelStatus");
		if(workticketTypeCode.equals("4"))
		{
			dhModel.setWorkticketNo(workticketNo);
			endHisModel=null;
			pemitHisModel=null;
			changeChargeModel=null;
			delayHisModel=null;
			safetyExeHisModel=null;
		}
		else if(workticketTypeCode.equals("1"))
		{
			dhModel=null;
			endHisModel.setWorkticketNo(workticketNo);
			pemitHisModel.setWorkticketNo(workticketNo);
			if(changeLabelStatus.equals("true"))
			{
			 changeChargeModel.setWorkticketNo(workticketNo);
			}
			else
			{
				changeChargeModel=null;
			}
			if(delayLabelStatus.equals("true"))
			{
			delayHisModel.setWorkticketNo(workticketNo);
			}
			else
			{
				delayHisModel=null;
			}
			safetyExeHisModel.setWorkticketNo(workticketNo);
			
		}
		else if(workticketTypeCode.equals("2")||workticketTypeCode.equals("7")||workticketTypeCode.equals("8"))
		{
			endHisModel.setWorkticketNo(workticketNo);
			pemitHisModel.setWorkticketNo(workticketNo);
			dhModel=null;
			changeChargeModel=null;
			delayHisModel=null;
			safetyExeHisModel=null;
		}
		else if(workticketTypeCode.equals("3")||workticketTypeCode.equals("5"))
		{
			dhModel=null;
			endHisModel.setWorkticketNo(workticketNo);
			pemitHisModel.setWorkticketNo(workticketNo);
			if(changeLabelStatus.equals("true"))
			{
			 changeChargeModel.setWorkticketNo(workticketNo);
			}
			else
			{
				changeChargeModel=null;
			}
			if(delayLabelStatus.equals("true"))
			{
			delayHisModel.setWorkticketNo(workticketNo);
			}
			else
			{
				delayHisModel=null;
			}
			safetyExeHisModel=null;
		}
		
		remote.updateApproveInfo(endHisModel, pemitHisModel, changeChargeModel, delayHisModel, safetyExeHisModel, dhModel);
		
		 write("{success:true,msg:'修改成功！'}");
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

	public RunJWorktickethis getDhModel() {
		return dhModel;
	}

	public void setDhModel(RunJWorktickethis dhModel) {
		this.dhModel = dhModel;
	}
}
