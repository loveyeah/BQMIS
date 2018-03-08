package power.web.equ.base.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.equ.base.EquCEquipments;
import power.ejb.equ.base.EquCEquipmentsFacadeRemote;
import power.ejb.equ.base.EquRBug;
import power.ejb.equ.base.EquRBugFacadeRemote;
import power.web.comm.AbstractAction;

public class EquRBugAction extends AbstractAction{
	private EquRBugFacadeRemote remote;
	
	/**
	 * 构造函数
	 */
	public EquRBugAction()
	{
		remote=(EquRBugFacadeRemote)factory.getFacadeRemote("EquRBugFacade");
	}
	
	/**
	 * 查询某故障对应的设备
	 * @throws JSONException
	 */
	public void findEquRBugList() throws JSONException
	{
		String bugCode="";
		Object myobj=request.getParameter("bugCode");
		
		if(myobj!=null)
		{
			bugCode=myobj.toString();
		}
		   String enterpriseCode=employee.getEnterpriseCode();
		    Object objstart=request.getParameter("start");
		    Object objlimit=request.getParameter("limit");
		    PageObject obj=new  PageObject();
		    if(objstart!=null&&objlimit!=null)
		    {
		        int start = Integer.parseInt(request.getParameter("start"));
				int limit = Integer.parseInt(request.getParameter("limit"));
				 obj=remote.findEquRBugList(bugCode, enterpriseCode,start,limit);
		    }
		    else
		    {
		    	obj=remote.findEquRBugList(bugCode, enterpriseCode);
		    }
		    List<EquRBug> list=obj.getList();
		    EquCEquipmentsFacadeRemote  equremote=(EquCEquipmentsFacadeRemote) factory.getFacadeRemote("EquCEquipmentsFacade");
		    StringBuffer JSONStr = new StringBuffer(); 
		    JSONStr.append("{list:[");
		    String equName="";
		    for(int i=0;i<list.size();i++)
		    {
		    	equName="";
		    	EquRBug model=list.get(i);
		    	if(model.getAttributeCode()!=null)
		    	{
		    	  EquCEquipments equmodel=equremote.findByCode(model.getAttributeCode(), enterpriseCode);
		    	  if(equmodel!=null)
		    	  {
		    		  equName=equmodel.getEquName();
		    	  }
		    	  
		    	}
		    	JSONStr.append("{id:"+model.getId()+",bugCode:'"+model.getBugCode()+"',attributeCode:'"+model.getAttributeCode()+"',equName:'"+equName+"'},");
		    	
		    }
		    if (JSONStr.length() > 7) {
				JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
			}
		   
		    JSONStr.append("],totalCount:"+obj.getTotalCount()+"}");
		    write(JSONStr.toString());

	}
	
	/**
	 * 增加故障对应设备
	 */
	public  void addEquRBug()
	{
		 String codes= request.getParameter("codes");
		 String bugCode=request.getParameter("bugCode");
		 String [] equcodes= codes.split(",");
		 EquRBug model=new EquRBug();
		 model.setEnterpriseCode(employee.getEnterpriseCode());
		 model.setBugCode(bugCode);
		  for(int i=0;i<equcodes.length;i++)
			{
			  model.setAttributeCode(equcodes[i]);
			  remote.save(model);
			}
		  write("{success:true,msg:'增加成功！'}");
	}
	
	/**
	 * 删除故障对应设备
	 */
	public void deleteEquRBug()
	{
		String ids= request.getParameter("ids");
		String [] bugRBugIds= ids.split(",");
		 for(int i=0;i<bugRBugIds.length;i++)
		 {
			remote.delete(Long.parseLong(bugRBugIds[i]));
		 }
		 write("{success:true,msg:'删除成功！'}");
	}

}
