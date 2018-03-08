package power.web.run.runlog.action;

import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.run.runlog.RunCRunlogParm;
import power.ejb.run.runlog.RunCRunlogParmFacadeRemote;
import power.ejb.run.runlog.RunJShiftParm;
import power.ejb.run.runlog.RunJShiftParmFacadeRemote;
import power.web.comm.AbstractAction;

public class RunShiftParmAction extends AbstractAction{

	private RunJShiftParmFacadeRemote remote;
	private BpCMeasureUnitFacadeRemote unitremote;
	private RunJShiftParm shiftParm;
	private String shiftParmId;
	public RunJShiftParm getShiftParm() {
		return shiftParm;
	}
	public void setShiftParm(RunJShiftParm shiftParm) {
		this.shiftParm = shiftParm;
	}
	public String getShiftParmId() {
		return shiftParmId;
	}
	public void setShiftParmId(String shiftParmId) {
		this.shiftParmId = shiftParmId;
	}
	
	/**
	 * 构造函数
	 */
	public RunShiftParmAction()
	{
		remote=(RunJShiftParmFacadeRemote) factory.getFacadeRemote("RunJShiftParmFacade");
		unitremote=(BpCMeasureUnitFacadeRemote) factory.getFacadeRemote("BpCMeasureUnitFacade");
	}
	
	/**
	 * 根据运行日志id查询对应的交接班参数
	 * @throws JSONException
	 */
	public void findShifParmList() throws JSONException
	{
	    Long runLogid=1l;
	    runLogid=Long.parseLong(request.getParameter("runlogId"));
	    
	    String enterpriseCode=employee.getEnterpriseCode();
	    Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			obj=remote.findParmList(runLogid, start,limit);
	    }
	    else
	    {
	    	obj=remote.findParmList(runLogid);
	    }
	    
	    List<RunJShiftParm> list=obj.getList();
	    RunCRunlogParmFacadeRemote  parmRemote=(RunCRunlogParmFacadeRemote)factory.getFacadeRemote("RunCRunlogParmFacade");
	    StringBuffer JSONStr = new StringBuffer(); 
	    JSONStr.append("{list:[");
	    String parmName="";
	    for(int i=0;i<list.size();i++)
	    {
	    	parmName="";
	    	String unitName="";
	    	RunJShiftParm model=list.get(i);
	    	if(model.getRunlogParmId()!=null)
	    	{
	    		parmName=model.getRunlogParmId().toString();
	    		RunCRunlogParm parmmodel=parmRemote.findById(model.getRunlogParmId());
	    		if(parmmodel!=null)
	    		{
	    			parmName=parmmodel.getItemName();
	    			if(parmmodel.getUnitMessureId() !=null)
	    			unitName=unitremote.findById(parmmodel.getUnitMessureId()).getUnitName();
	    		}
	    	}
	    	
	    	 JSONStr.append("{shiftParmId:"+model.getShiftParmId()+",runlogParmId:"+model.getRunlogParmId()+",parmName:'"+parmName+"',itemNumberValue:"+model.getItemNumberValue()+",unitName:'"+unitName+"'},");
	   
	    }
	    
	    if (JSONStr.length() > 7) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
	   
	    JSONStr.append("],totalCount:"+obj.getTotalCount()+"}");
	    write(JSONStr.toString());
	    
//		String str=JSONUtil.serialize(obj);
//		write(str);
		
	}
	
	/**
	 * 保存（即修改）交接班参数
	 * @throws JSONException
	 */
	@SuppressWarnings("unchecked")
	public void addShifParm() throws JSONException
	{
		String str=request.getParameter("data");
		   Object obj = JSONUtil.deserialize(str);
		   List<Map> list = (List<Map>)obj;
		   for(Map data : list)
		   {
		       String id = ((Map) ((Map) data)).get("shiftParmId").toString();
		       String value = ((Map) ((Map)data)).get("itemNumberValue").toString(); 
		       RunJShiftParm model=remote.findById(Long.parseLong(id));
		       if(!"".equals(value))
		       {
		    	   model.setItemNumberValue(Double.parseDouble(value));
		       }
		       else
		       {
		    	   model.setItemNumberValue(null);
		       }
		       remote.update(model);
		   } 
		  write("{success:true,id:'-1',msg:'保存成功！'}");
	}
	
}
