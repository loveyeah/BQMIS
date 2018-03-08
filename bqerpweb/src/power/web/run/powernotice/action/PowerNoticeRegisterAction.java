package power.web.run.powernotice.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.powernotice.RunCPowerNotice;
import power.ejb.run.powernotice.RunCPowerNoticeFacadeRemote;
import power.web.comm.AbstractAction;

public class PowerNoticeRegisterAction extends AbstractAction{
	private RunCPowerNotice power;
	protected RunCPowerNoticeFacadeRemote remote;  
	public PowerNoticeRegisterAction(){
		remote=(RunCPowerNoticeFacadeRemote)factory.getFacadeRemote("RunCPowerNoticeFacade");
	    
	}
	
	public void findPowerNoticeList() throws JSONException
	{
		//String contactDept=request.getParameter("contactDept");
		String  busiState=request.getParameter("busiState");
		if(busiState==null||busiState.equals(""))
		{
			busiState="1,4";
		}
		 Object objstart=request.getParameter("start");
		    Object objlimit=request.getParameter("limit");
		    PageObject obj=new  PageObject();
		    if(objstart!=null&&objlimit!=null)
		    {
		        int start = Integer.parseInt(request.getParameter("start"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				obj=remote.findNoReportList(employee.getEnterpriseCode(), null, busiState, start,limit);
		    }
		    else
		    {
		    	obj=remote.findNoReportList(employee.getEnterpriseCode(), null, busiState);
		    }
		    String str=JSONUtil.serialize(obj);
			write(str);
	}
	
	public void addPowerNotice()
	{  //modify by ypan 20100811
		power.setEnterpriseCode(employee.getEnterpriseCode());
		power.setContactMonitor(employee.getWorkerCode());
		power.setContactDept(employee.getDeptCode());
		power=remote.save(power);
		write("{success:true,no:'"+power.getNoticeNo()+"',id:'"+power.getNoticeId()+"',msg:'增加成功！'}");
		
	}
	public void updatePowerNotice()
	{
		RunCPowerNotice model=remote.findById(power.getNoticeId());
		model.setContactContent(power.getContactContent());
		model.setMemo(power.getMemo());
		model.setContactDate(power.getContactDate());
//		model.setContactDept(power.getContactDept());
//		model.setContactMonitor(power.getContactMonitor());
		model.setModifyBy(employee.getWorkerCode());
		model.setNoticeSort(power.getNoticeSort());
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	public void deletePowerNotice()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	

	public RunCPowerNotice getPower() {
		return power;
	}

	public void setPower(RunCPowerNotice power) {
		this.power = power;
	}
}
