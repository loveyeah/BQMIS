package power.web.run.protectapply.action;

import power.ear.comm.ejb.PageObject;
import power.ejb.run.protectinoutapply.RunJProtectinoutapplyFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;


public class ProtectApplyQueryAction extends AbstractAction{
	protected RunJProtectinoutapplyFacadeRemote protectRemote; 
	public ProtectApplyQueryAction()
	{
		protectRemote=(RunJProtectinoutapplyFacadeRemote)factory.getFacadeRemote("RunJProtectinoutapplyFacade");
	}
	
	
	public void findProtectQueryList() throws JSONException
	{
		Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    String startDate=request.getParameter("startDate");
	    String endDate=request.getParameter("endDate");
	    String applyDept=request.getParameter("applyDept");
	    String status=request.getParameter("status");
	    PageObject obj=new  PageObject();
	
		
		if(objstart!=null&&objlimit!=null)
		{
			obj=protectRemote.findListForQuery(startDate, endDate, applyDept, status,  employee.getEnterpriseCode(),Integer.parseInt(objstart.toString()),Integer.parseInt(objlimit.toString()));
		}
		else
		{
			obj=protectRemote.findListForQuery(startDate, endDate, applyDept, status,employee.getEnterpriseCode());
		}
		if(obj==null||obj.getList()==null)
		{
			write("{\"list\":[],\"totalCount\":0}");
		}
		else
		{
		 String str=JSONUtil.serialize(obj);
			write(str);
		}
  }
}
