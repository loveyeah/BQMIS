package power.web.run.repair;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.repair.RunJRepairTasklist;
import power.ejb.run.repair.RunJRepairTasklistFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class  RepairTaskAction  extends  AbstractAction{
	private RunJRepairTasklistFacadeRemote remote;
	public RepairTaskAction()
	{
		remote=(RunJRepairTasklistFacadeRemote) factory.getFacadeRemote("RunJRepairTasklistFacade");
	}
	public void getRepairTask() throws JSONException
	{
		 String year=request.getParameter("year");
		PageObject result =new PageObject ();
		result=remote.getRepairTask(year,employee.getEnterpriseCode());
		write(JSONUtil.serialize(result));
	}
	
	public void getRepairTaskSelectList() throws JSONException
	{
		 String year=request.getParameter("year");
		 String sepeciality = request.getParameter("speciality");
		PageObject result =new PageObject ();
		result=remote.getRepairTaskSelect(year, sepeciality, employee.getEnterpriseCode());
		write(JSONUtil.serialize(result));
	}
	
	public  void saveRepairTask()throws JSONException, ParseException, java.text.ParseException, CodeRepeatException
	 {
		 String year=request.getParameter("year");
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<RunJRepairTasklist> addList =null;
		List<RunJRepairTasklist> updateList = null;
		if(list != null && list.size()>0)
		{
		addList = new ArrayList<RunJRepairTasklist>();
		updateList = new ArrayList<RunJRepairTasklist>();
		try {
		for (Map data : list) {
			
			String tasklistId = null;
			String tasklistName = null;
			if (data.get("tasklistId") != null)
				tasklistId = data.get("tasklistId").toString();
			if (data.get("tasklistName") != null)
				tasklistName = data.get("tasklistName").toString();
			
			RunJRepairTasklist model = new RunJRepairTasklist();
			
			if (tasklistId == null) {
				
				if (tasklistName != null && !tasklistName.equals(""))
					model.setTasklistName(tasklistName);
				if(year!=null&&!year.equals(""))
				{
				DateFormat sdf = new SimpleDateFormat("yyyy");
				model.setTasklistYear(sdf.parse(year));
				}
				
				//填写人和填写时间
					model.setEntryDate(new Date());
					model.setIsUse("Y");
					model.setEntryBy(employee.getWorkerCode());
			     	model.setEnterpriseCode(employee.getEnterpriseCode());
				   addList.add(model);
			} else  if(tasklistId!=null) {
				model = remote.findById(Long.parseLong(tasklistId));
				if (tasklistName != null && !tasklistName.equals(""))
					model.setTasklistName(tasklistName);
				//年度
				if(year!=null&&!year.equals(""))
				{
				DateFormat sdf = new SimpleDateFormat("yyyy");
				model.setTasklistYear(sdf.parse(year));
				}
				
				
				//填写人和填写时间
					model.setEntryDate(new Date());
					model.setEntryBy(employee.getWorkerCode());
					model.setIsUse("Y");
			     	model.setEnterpriseCode(employee.getEnterpriseCode());
				    updateList.add(model);
			}
		}
	  
		remote.saveRepairTask(addList, updateList);
		write("{success:true,msg:'操作成功！'}");
		}
		catch (CodeRepeatException e) {
			String out = "{success:true,msg :'" + e.getMessage() + "'}";
			write(out);
		}
		}
	 }
	
	

	public  void delRepairTask()
	{
		 String ids=request.getParameter("ids");
		 remote.delRepairTask(ids);
	}
	
}