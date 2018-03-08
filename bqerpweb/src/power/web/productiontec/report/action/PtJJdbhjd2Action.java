package power.web.productiontec.report.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.report.PtJJdbhjd2;
import power.ejb.productiontec.report.PtJJdbhjd2FacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class PtJJdbhjd2Action extends AbstractAction
{
	private PtJJdbhjd2FacadeRemote remote;
	
	public PtJJdbhjd2Action()
	{
		remote = (PtJJdbhjd2FacadeRemote)factory.getFacadeRemote("PtJJdbhjd2Facade");
	}
	
	public void getPtJJdbhjd2List() throws JSONException
	{
		String month = request.getParameter("month");
		String tableFlag = request.getParameter("tableFlag");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		PageObject pg = new PageObject();
		
		if(start != null && limit != null)
			pg = remote.findAllByMonthAndFlag(month, tableFlag, employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
		else 
			pg = remote.findAllByMonthAndFlag(month, tableFlag, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));		
	}
	
	public void savePtJjdbhjd2Mod() throws JSONException
	{
		String addStr = request.getParameter("add");
		String updateStr = request.getParameter("update");
		String month = request.getParameter("month");
		String tableFlag = request.getParameter("tableFlag");
		String engineer = request.getParameter("engineer");
		String equDept = request.getParameter("equDept");
		
		Object addObj = JSONUtil.deserialize(addStr);
		Object updateObj = JSONUtil.deserialize(updateStr);
		
		List<Map> addMapList = (List<Map>)addObj;
		List<Map> updateMapList = (List<Map>)updateObj;
		 
		List<PtJJdbhjd2> addList = new ArrayList<PtJJdbhjd2>();
		List<PtJJdbhjd2> updateList = new ArrayList<PtJJdbhjd2>();
		
		for(Map map : addMapList)
		{
			PtJJdbhjd2 temp = this.parseInstanceOfPtjj(map,month,tableFlag,engineer,equDept);
			addList.add(temp);
		}
		for(Map map : updateMapList)
		{
			PtJJdbhjd2 temp = this.parseInstanceOfPtjj(map,month,tableFlag,engineer,equDept);
			updateList.add(temp);
		}
		
		if(addList != null || updateList != null)
		{
			if(addList.size() > 0 || updateList.size() > 0)
			{
				remote.saveModiRec(addList, updateList);
			}
		}
		
		write("{success : true,msg : '数据保存成功！'}");
	}
	
	public PtJJdbhjd2 parseInstanceOfPtjj(Map map,String month,String tableFlagStr,
			String engineerStr,String equDeptStr)
	{
		PtJJdbhjd2 temp = new PtJJdbhjd2();
		
		Long jdbhjd2Id = null;
		String strMonth = month;
		String countType = null;
		Double kv35Num = null;
		Double kv110Num = null;
		Double kv220Num = null;
		Double kv330Num = null;
		Double mcProtectNum = null;
		Double safeDeviceNum = null;
		Double otherNum = null;
		String engineer = engineerStr;
		String equDept = equDeptStr;
		String entryBy = employee.getWorkerCode();
		Date entryDate = new Date();
		String enterpriseCode = employee.getEnterpriseCode();
		String tableFlag = tableFlagStr;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if(map.get("jdbhjd2Id") != null)
			jdbhjd2Id = Long.parseLong(map.get("jdbhjd2Id").toString());
		if(map.get("countType") != null)
			countType = map.get("countType").toString();
		if(map.get("kv35Num") != null)
			kv35Num = Double.parseDouble(map.get("kv35Num").toString());
		if(map.get("kv110Num") != null)
			kv110Num = Double.parseDouble(map.get("kv110Num").toString());
		if(map.get("kv220Num") != null)
			kv220Num = Double.parseDouble(map.get("kv220Num").toString());
		if(map.get("kv330Num") != null)
			kv330Num = Double.parseDouble(map.get("kv330Num").toString());
		if(map.get("mcProtectNum") != null)
			mcProtectNum = Double.parseDouble(map.get("mcProtectNum").toString());
		if(map.get("safeDeviceNum") != null)
			safeDeviceNum = Double.parseDouble(map.get("safeDeviceNum").toString());
		if(map.get("otherNum") != null)
			otherNum = Double.parseDouble(map.get("otherNum").toString());
		if(map.get("entryDateString") != null)
			try {
				entryDate = sdf.parse(map.get("entryDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if(map.get("entryBy") != null && !map.get("entryBy").toString().equals(""))
			entryBy = map.get("entryBy").toString();
			
		temp.setJdbhjd2Id(jdbhjd2Id);
		temp.setStrMonth(strMonth);
		temp.setCountType(countType);
		temp.setKv35Num(kv35Num);
		temp.setKv110Num(kv110Num);
		temp.setKv220Num(kv220Num);
		temp.setKv330Num(kv330Num);
		temp.setMcProtectNum(mcProtectNum);
		temp.setSafeDeviceNum(safeDeviceNum);
		temp.setOtherNum(otherNum);
		temp.setEngineer(engineer);
		temp.setEquDept(equDept);
		temp.setEntryBy(entryBy);
		temp.setEntryDate(entryDate);
		temp.setEnterpriseCode(enterpriseCode);
		temp.setTableFlag(tableFlag);
		
		return temp;
	}
}