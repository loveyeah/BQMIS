package power.web.equ.change.action;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.change.EquCChangesource;
import power.ejb.equ.change.EquCChangesourceFacadeRemote;
import power.web.comm.AbstractAction;

public class EquChangeSourceAction extends AbstractAction{

	private EquCChangesourceFacadeRemote remote;
	private EquCChangesource source;
	private String sourceId;
	
	public EquChangeSourceAction()
	{
		remote=(EquCChangesourceFacadeRemote)factory.getFacadeRemote("EquCChangesourceFacade");
	}
	
	public void findChangeSourceList() throws JSONException
	{
		String fuzzy="";
		Object myobj=request.getParameter("fuzzy");
		if(myobj!=null)
		{
			fuzzy=myobj.toString();
		}
	    String enterpriseCode=employee.getEnterpriseCode();
	    Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			 obj=remote.findChangeSourceList(fuzzy, enterpriseCode,start,limit);
	    }
	    else
	    {
	    	obj=remote.findChangeSourceList(fuzzy, enterpriseCode);
	    }

		 String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	public void addChangeSource()
	{
		source.setEnterpriseCode(employee.getEnterpriseCode());
	    int i=remote.save(source);
		if(i != -1)
		{
			write("{success:true,msg:'增加成功！'}");
		}
		else
		{
			write("{success:true,msg:'增加失败:编码重复！'}");
		}
		
	}
	
	public void updateChangeSource()
	{
		EquCChangesource model=remote.findById(Long.parseLong(sourceId));
		model.setSourceCode(source.getSourceCode());
		model.setSourceName(source.getSourceName());
		if(remote.update(model))
		{
			write("{success:true,msg:'修改成功！'}");
		}
		else
		{
			write("{success:true,msg:'修改失败:编码重复！'}");
		}
	}
	
	public void deleteChangeSource()
	{
		
		 String ids= request.getParameter("ids");
	     String [] sourceids= ids.split(",");
			for(int i=0;i<sourceids.length;i++)
			{
				if(!sourceids[i].equals(""))
				{
					remote.delete(Long.parseLong(sourceids[i]));
				}
			}
			
			write("{success:true,msg:'删除成功！'}");
		
	}

	public EquCChangesource getSource() {
		return source;
	}

	public void setSource(EquCChangesource source) {
		this.source = source;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

}
