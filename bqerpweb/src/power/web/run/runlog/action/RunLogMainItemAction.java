package power.web.run.runlog.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.runlog.RunCMainItem;
import power.ejb.run.runlog.RunCMainItemFacadeRemote;
import power.web.comm.AbstractAction;

public class RunLogMainItemAction extends AbstractAction{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private RunCMainItemFacadeRemote remote;
	private RunCMainItem item;
	/**
	 * 
	 */
	
	public RunLogMainItemAction()
	{
		remote = (RunCMainItemFacadeRemote)factory.getFacadeRemote("RunCMainItemFacade");
	}
	
	public void addRunLogMainItem() {
		item.setEnterpriseCode(employee.getEnterpriseCode());
		try {
			remote.save(item);
			write("{success:true,msg:'增加成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	public void updateRunLogMainItem(){
		RunCMainItem model = remote.findById(item.getItemId());
		model.setMainItemCode(item.getMainItemCode());
		model.setMainItemName(item.getMainItemName());
		model.setDiaplayNo(item.getDiaplayNo()); 
		try {
			remote.update(model);
			write("{success:true,msg:'修改成功！'}");
		} catch (CodeRepeatException e) {
			write("{success:true,msg:'" + e.getMessage() + "'}");
		}
	}
	
	public void deleteRunLogMainItem()
	{
		RunCMainItem model = remote.findById(item.getItemId());
		model.setIsUse("N");
		try {
			remote.update(model);
		} catch (CodeRepeatException e) {
		 
		}
		write("{success:true}");
	}
	
	public void findRunLogMainItem() throws JSONException
	{
		String fuzzy="";
		Object myobj=request.getParameter("fuzzy");
		if(myobj!=null)
		{
			fuzzy=myobj.toString();
		}
	    String enterpriseCode="hfdc";
	    Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			 obj=remote.findMainItemList(fuzzy, enterpriseCode,start,limit);
	    }
	    else
	    {
	    	obj=remote.findMainItemList(fuzzy, enterpriseCode);
	    }
	    
		String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	public RunCMainItem getItem() {
		return item;
	}
	public void setItem(RunCMainItem item) {
		this.item = item;
	}
}
