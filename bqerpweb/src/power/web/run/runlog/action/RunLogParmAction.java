package power.web.run.runlog.action;

import java.util.List;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.system.BpCMeasureUnit;
import power.ejb.manage.system.BpCMeasureUnitFacadeRemote;
import power.ejb.run.runlog.RunCRunlogParm;
import power.ejb.run.runlog.RunCRunlogParmFacadeRemote;
import power.ejb.run.runlog.RunJRunlogMain;
import power.ejb.run.runlog.RunJRunlogMainFacadeRemote;
import power.ejb.run.runlog.RunJShiftParm;
import power.ejb.run.runlog.RunJShiftParmFacadeRemote;
import power.web.comm.AbstractAction;

public class RunLogParmAction extends AbstractAction{
   private RunCRunlogParmFacadeRemote remote;
   private RunJRunlogMainFacadeRemote mainremote;
	private RunCRunlogParm parm;
	private String runlogParmId;
	private String diaplayNo;
	private RunJShiftParmFacadeRemote parmremote;
	public String getDiaplayNo() {
		return diaplayNo;
	}
	public void setDiaplayNo(String diaplayNo) {
		this.diaplayNo = diaplayNo;
	}
	public RunCRunlogParm getParm() {
		return parm;
	}
	public void setParm(RunCRunlogParm parm) {
		this.parm = parm;
	}
	public String getRunlogParmId() {
		return runlogParmId;
	}
	public void setRunlogParmId(String runlogParmId) {
		this.runlogParmId = runlogParmId;
	}
	
	/**
	 * 构造函数
	 */
	public RunLogParmAction()
	{
		remote=(RunCRunlogParmFacadeRemote)factory.getFacadeRemote("RunCRunlogParmFacade");
		parmremote=(RunJShiftParmFacadeRemote)factory.getFacadeRemote("RunJShiftParmFacade");
		mainremote=(RunJRunlogMainFacadeRemote)factory.getFacadeRemote("RunJRunlogMainFacade");
	}
	
	/**
	 * 获得某个专业的运行参数列表
	 * @throws JSONException
	 */
	public void getRunLogParmList() throws JSONException
	{
		String fuzzy="";
		String specialCode="%";
		Object myobj=request.getParameter("fuzzy");
		Object objspecial=request.getParameter("special");
		if(myobj!=null)
		{
			fuzzy=myobj.toString();
		}
		if(objspecial!=null)
		{
			specialCode=objspecial.toString();
		}
	    String enterpriseCode=employee.getEnterpriseCode();
	    Object objstart=request.getParameter("start");
	    Object objlimit=request.getParameter("limit");
	    PageObject obj=new  PageObject();
	    if(objstart!=null&&objlimit!=null)
	    {
	        int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			 obj=remote.findParmList(fuzzy, specialCode, enterpriseCode, start,limit);
	    }
	    else
	    {
	    	obj=remote.findParmList(fuzzy, specialCode, enterpriseCode);
	    }
	    List<RunCRunlogParm> list=obj.getList();
	    StringBuffer JSONStr = new StringBuffer(); 
	    BpCMeasureUnitFacadeRemote unit=(BpCMeasureUnitFacadeRemote)factory.getFacadeRemote("BpCMeasureUnitFacade");
	    JSONStr.append("{list:[");
	    
	    for(int i=0;i<list.size();i++)
	    {
	    	RunCRunlogParm model=list.get(i);
	    	String statTypeName=model.getStatType();
	    	if(statTypeName.equals("1"))
	    	{
	    		statTypeName="累加值";
	    	}
	    	if(statTypeName.equals("2"))
	    	{
	    		statTypeName="最新值";
	    	}
	    	if(statTypeName.equals("3"))
	    	{
	    		statTypeName="平均值";
	    	}
	    	String unitName="";
	    	if(model.getRunlogParmId()!=null)
	    	{
	    	 unitName=model.getUnitMessureId().toString();
	    	BpCMeasureUnit unitmodel=unit.findById(model.getUnitMessureId());
			if(unitmodel!=null)
			{
				unitName=unitmodel.getUnitName();
			}
	    	}
	    	
	    	JSONStr.append("{runlogParmId:"+model.getRunlogParmId()+",itemId:"+model.getItemId()+",itemCode:'"+model.getItemCode()+"',");
	    	JSONStr.append("itemName:'"+model.getItemName()+"',specialityCode:'"+model.getSpecialityCode()+"',unitMessureId:"+model.getUnitMessureId()+",");
	    	JSONStr.append("unitName:'"+unitName+"',statType:'"+model.getStatType()+"',diaplayNo:"+model.getDiaplayNo()+",statTypeName:'"+statTypeName+"'},");
	    }
	    if (JSONStr.length() > 7) {
			JSONStr.deleteCharAt(JSONStr.lastIndexOf(","));
		}
	   
	    JSONStr.append("],totalCount:"+obj.getTotalCount()+"}");

       write(JSONStr.toString());
	}
	
	/**
	 * 增加一条运行参数
	 */
	public void addParm()
	{
		String specialCode=request.getParameter("special");
		if(diaplayNo!=null&&!diaplayNo.equals(""))
		{
			parm.setDiaplayNo(Long.parseLong(diaplayNo));
		}
	    parm.setSpecialityCode(specialCode);
		parm.setEnterpriseCode(employee.getEnterpriseCode());
		//parm.setItemCode("*");
		Long id=remote.save(parm);
//		if(parmremote.findMaxLogid(specialCode, employee.getEnterpriseCode()) !=null)
//		{
//			Long logid=parmremote.findMaxLogid(specialCode, employee.getEnterpriseCode());
//			RunJShiftParm newmodel=new RunJShiftParm();
//			newmodel.setRunLogid(logid);
//			newmodel.setRunlogParmId(id);
//			newmodel.setEnterpriseCode(employee.getEnterpriseCode());
//			newmodel.setIsUse("Y");
//			parmremote.save(newmodel);
//		}
		if(mainremote.findLatestModelBySpecial(specialCode, employee.getEnterpriseCode()) != null){
			RunJRunlogMain mainmodel=mainremote.findLatestModelBySpecial(specialCode,  employee.getEnterpriseCode());
			Long logid=parmremote.findMaxLogid(specialCode, employee.getEnterpriseCode());
			RunJShiftParm newmodel=new RunJShiftParm();
			newmodel.setRunLogid(mainmodel.getRunLogid());
			newmodel.setRunlogParmId(id);
			newmodel.setEnterpriseCode(employee.getEnterpriseCode());
			newmodel.setIsUse("Y");
			parmremote.save(newmodel);
		}
		write("{success:true,msg:'增加成功！'}");
	}
	/**
	 * 修改一条运行参数
	 */
	public void updateParm()
	{
		RunCRunlogParm model=remote.findById(Long.parseLong(runlogParmId));
		//model.setItemCode("*");
		if(diaplayNo!=null&&!diaplayNo.equals(""))
		{
			model.setDiaplayNo(Long.parseLong(diaplayNo));
		}
		//model.setSpecialityCode(parm.getSpecialityCode());
		model.setUnitMessureId(parm.getUnitMessureId());
		model.setItemName(parm.getItemName());
		model.setStatType(parm.getStatType());
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	/**
	 * 删除一条运行参数
	 */
	public void deleteparm()
	{
		 String ids= request.getParameter("ids");
	     String [] parmIds= ids.split(",");
			for(int i=0;i<parmIds.length;i++)
			{
				if(!parmIds[i].equals(""))
				{
					RunCRunlogParm pmodel=remote.findById(Long.parseLong(parmIds[i]));
					remote.delete(Long.parseLong(parmIds[i]));
					
					Long logid=parmremote.findMaxLogid(pmodel.getSpecialityCode(), employee.getEnterpriseCode());
					Long id=parmremote.findIdByparmid(logid, Long.parseLong(parmIds[i]), employee.getEnterpriseCode());
					if(id != null)
					{
						RunJShiftParm model=parmremote.findById(id);
						model.setIsUse("N");
						parmremote.update(model);
					}
				}
			}
			write("{success:true,msg:'删除成功！'}");
		
	}

	
	
}
