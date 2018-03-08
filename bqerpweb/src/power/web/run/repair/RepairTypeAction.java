package power.web.run.repair;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.run.repair.RunCRepairType;
import power.ejb.run.repair.RunCRepairTypeFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class   RepairTypeAction  extends  AbstractAction 
{
	private RunCRepairTypeFacadeRemote  remote;
	public RepairTypeAction()
	{
		remote=(RunCRepairTypeFacadeRemote) factory.getFacadeRemote("RunCRepairTypeFacade");
	}
	public void getRepairType () throws JSONException
	{
		PageObject result =new PageObject ();
		result=remote.getRepairType(employee.getEnterpriseCode());
		write(JSONUtil.serialize(result));
	}
	public  void saveRepairType()throws JSONException, ParseException, java.text.ParseException
	 {
		String str = request.getParameter("isUpdate");
		Object object = JSONUtil.deserialize(str);
		List<Map> list = (List<Map>) object;
		List<RunCRepairType> addList =null;
		List<RunCRepairType> updateList = null;
		if(list != null && list.size()>0)
		{
		addList = new ArrayList<RunCRepairType>();
		updateList = new ArrayList<RunCRepairType>();
		try {
		for (Map data : list) {
			String repairTypeId = null;
			String repairTypeName = null;
			String memo = null;
			String isUse = null;
			if (data.get("repairTypeId") != null)
				repairTypeId = data.get("repairTypeId").toString();
			if (data.get("repairTypeName") != null)
				repairTypeName = data.get("repairTypeName").toString();
			if (data.get("isUse") != null)
				isUse = data.get("isUse").toString();
			if (data.get("memo") != null)
				memo = data.get("memo").toString();
			RunCRepairType model = new RunCRepairType();
			if (repairTypeId == null) {
				if (repairTypeName != null && !repairTypeName.equals(""))
					model.setRepairTypeName(repairTypeName);
				if (memo != null&&!memo.equals(""))
					model.setMemo(memo);
				if(isUse!=null&&!isUse.equals(""))
					model.setIsUse(isUse);
				//填写人和填写时间
					model.setEntryDate(new Date());
					model.setEntryBy(employee.getWorkerCode());
			     	model.setEnterpriseCode(employee.getEnterpriseCode());
				   addList.add(model);
			} else {
				model = remote.findById(Long.parseLong(repairTypeId));
				if (repairTypeName != null && !repairTypeName.equals(""))
					model.setRepairTypeName(repairTypeName);
				if (memo != null&&!memo.equals(""))
					model.setMemo(memo);
				if(isUse!=null&&!isUse.equals(""))
					model.setIsUse(isUse);
				//填写人和填写时间
					model.setEntryDate(new Date());
					model.setEntryBy(employee.getWorkerCode());
			     	model.setEnterpriseCode(employee.getEnterpriseCode());
				    updateList.add(model);
			}
		}
	 
		remote.saveRepairType(addList, updateList);
		write("{success:true,msg:'操作成功！'}");
		}
		catch (CodeRepeatException e) {
			String out = "{success:true,msg :'" + e.getMessage() + "'}";
			write(out);
		}
	
	
		
	
		}
	 }
}
	 

	
	