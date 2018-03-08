package power.web.equ.base.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCBug;
import power.ejb.equ.base.EquCBugFacadeRemote;
import power.ejb.equ.base.EquCBugreason;
import power.ejb.equ.base.EquCBugreasonFacadeRemote;
import power.ejb.equ.base.EquCBugsolutionFacadeRemote;
import power.web.comm.AbstractAction;

public class EquBugReasonAction extends AbstractAction{
	private EquCBugreasonFacadeRemote remote;
	private EquCBugreason bugReason;
	private String bugReasonId;
	public EquCBugreason getBugReason() {
		return bugReason;
	}
	public void setBugReason(EquCBugreason bugReason) {
		this.bugReason = bugReason;
	}
	public String getBugReasonId() {
		return bugReasonId;
	}
	public void setBugReasonId(String bugReasonId) {
		this.bugReasonId = bugReasonId;
	}

	/**
	 * 构造函数
	 */
	public EquBugReasonAction()
	{
		remote=(EquCBugreasonFacadeRemote) factory.getFacadeRemote("EquCBugreasonFacade");
	}
	
	/**
	 * 查询某个故障对应的所有原因
	 * @throws JSONException
	 */
	public void findBugReasonList() throws JSONException
	{
		String fuzzy="";
		String bugCode="";
		Object myobj=request.getParameter("fuzzy");
		Object	objBug=request.getParameter("bugId");
		if(myobj!=null)
		{
			fuzzy=myobj.toString();
		}
		if(objBug!=null)
		{
			bugCode=objBug.toString();
			EquCBugFacadeRemote bugremote=(EquCBugFacadeRemote) factory.getFacadeRemote("EquCBugFacade");
			EquCBug bugmodel=  bugremote.findById(Long.parseLong(bugCode));
			if(bugmodel!=null)
			{
				bugCode=bugmodel.getBugCode();
			}
		}
	    String enterpriseCode=employee.getEnterpriseCode();
	    Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			 obj=remote.findBugReasonList(fuzzy, bugCode, enterpriseCode,start,limit);
	    }
	    else
	    {
	    	obj=remote.findBugReasonList(fuzzy, bugCode, enterpriseCode);
	    }
	
//		 List<EquCBlock> list=obj.getList();
//		 Long count=obj.getTotalCount();
		 String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	public void findBugReaListByCode() throws JSONException
	{
		String fuzzy="";
		String bugCode="";
		Object myobj=request.getParameter("fuzzy");
		Object	objBug=request.getParameter("bugCode");
		if(myobj!=null)
		{
			fuzzy=myobj.toString();
		}
		if(objBug!=null)
		{
			bugCode=objBug.toString();
		}
	    String enterpriseCode=employee.getEnterpriseCode();
	    Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			 obj=remote.findBugReasonList(fuzzy, bugCode, enterpriseCode,start,limit);
	    }
	    else
	    {
	    	obj=remote.findBugReasonList(fuzzy, bugCode, enterpriseCode);
	    }
		 String str=JSONUtil.serialize(obj);
		write(str);
	}
	
	/**
	 * 增加某故障的故障原因
	 */
	public void addBugReason()
	{
		String bugCode=request.getParameter("bugCode");
		
		EquCBugFacadeRemote bugremote=(EquCBugFacadeRemote) factory.getFacadeRemote("EquCBugFacade");
		EquCBug bugmodel=  bugremote.findById(Long.parseLong(bugCode));
		if(bugmodel!=null)
		{
			bugCode=bugmodel.getBugCode();
		}
		bugReason.setBugCode(bugCode);
		bugReason.setEnterpriseCode(employee.getEnterpriseCode());
		bugReason.setEntryBy(employee.getWorkerCode());
		bugReason.setEntryDate(new java.util.Date());
	    int id=remote.save(bugReason);
		if(id != -1)
		{
			write("{success:true,id:'"+id+"',msg:'增加成功！'}");
		}
		else
		{
			write("{success:true,id:'-1',msg:'增加失败:原因重复！'}");
		}
		
	}
	
	/**
	 * 修改某故障的故障原因
	 */
	public void updateBugReason()
	{
		EquCBugreason model=new EquCBugreason();
		model=remote.findById(Long.parseLong(bugReasonId));
		model.setBugReasonDesc(bugReason.getBugReasonDesc());
		model.setEntryBy(employee.getWorkerCode());
		model.setEntryDate(new java.util.Date());
		if(remote.update(model))
		{
			 write("{success:true,msg:'修改成功！'}");
		}
		else
		{
			 write("{success:true,msg:'修改失败:原因重复！'}");
		}
		
	}
	
	/**
	 * 删除故障的故障原因
	 */
	public void deleteBugReason()
	{
		EquCBugsolutionFacadeRemote solutionRemote=(EquCBugsolutionFacadeRemote) factory.getFacadeRemote("EquCBugsolutionFacade");
		 String ids= request.getParameter("ids");
	     String [] reasonids= ids.split(",");
			for(int i=0;i<reasonids.length;i++)
			{
			  remote.delete(Long.parseLong(reasonids[i]));	
			  //删除原因对应的解决方案
			  solutionRemote.deleteAllByReasonId(Long.parseLong(reasonids[i]), employee.getEnterpriseCode());
			}
			String	str = "{success: true,msg:\'ok\'}";
		    write(str);
	}
	
	/**
	 * 删除故障的故障原因（ids:原因id）
	 * @param ids
	 */
	public void deleteReasonForBug(String ids)
	{
		EquCBugsolutionFacadeRemote solutionRemote=(EquCBugsolutionFacadeRemote) factory.getFacadeRemote("EquCBugsolutionFacade");
		
	     String [] reasonids= ids.split(",");
			for(int i=0;i<reasonids.length;i++)
			{
			  remote.delete(Long.parseLong(reasonids[i]));	
			  solutionRemote.deleteAllByReasonId(Long.parseLong(reasonids[i]), employee.getEnterpriseCode());
			}
			
	}
	
	public void getReasonStringByCode()
	{
		String bugCode="";
		Object	objBug=request.getParameter("bugCode");
		if(objBug!=null)
		{
			bugCode=objBug.toString();
		}
		 PageObject obj=new  PageObject();
		 obj=remote.findBugReasonList("%", bugCode, employee.getEnterpriseCode());
		 List<EquCBugreason> list=obj.getList();
		 String reasons="";
		 if(list!=null)
		 {
			 for(int i=0;i<list.size();i++)
			 {
				 EquCBugreason model=list.get(i);
				 if(reasons.equals(""))
				 {
					 reasons="("+(i+1)+")"+model.getBugReasonDesc();
				 }
				 else
				 {
					 reasons= reasons+"\n("+(i+1)+")"+model.getBugReasonDesc();
				 }
				
			 }
		 }
		 write("{success: true,reasons:'"+reasons+"'}");
		 
	}

	
	
}
