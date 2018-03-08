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
import power.ejb.productiontec.report.PtJJdbhjd1;
import power.ejb.productiontec.report.PtJJdbhjd1FacadeRemote;
import power.ejb.productiontec.report.PtJJdbhjd2;
import power.ejb.productiontec.report.PtJJdbhjd2FacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class PtJJdbhjd1Action extends AbstractAction
{
	private PtJJdbhjd1FacadeRemote remote;
	
	public PtJJdbhjd1Action()
	{
		remote = (PtJJdbhjd1FacadeRemote)factory.getFacadeRemote("PtJJdbhjd1Facade");
	}
	
	public void getPtJJdbhjd1List() throws JSONException
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
	
	public void savePtJjdbhjd1Mod() throws JSONException
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
		 
		List<PtJJdbhjd1> addList = new ArrayList<PtJJdbhjd1>();
		List<PtJJdbhjd1> updateList = new ArrayList<PtJJdbhjd1>();
		
		for(Map map : addMapList)
		{
			PtJJdbhjd1 temp = this.parseInstanceOfPtjj(map,month,tableFlag,engineer,equDept);
			addList.add(temp);
		}
		for(Map map : updateMapList)
		{
			PtJJdbhjd1 temp = this.parseInstanceOfPtjj(map,month,tableFlag,engineer,equDept);
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
	
	public PtJJdbhjd1 parseInstanceOfPtjj(Map map,String month,String tableFlagStr,
			String engineerStr,String equDeptStr)
	{
		PtJJdbhjd1 temp = new PtJJdbhjd1();
		
		Long jdbhjd1Id = null;
		String strMonth = month;
		String countType = null;
		Double dynamoNum = null;
		Double transformerNum = null;
		Double fbzProtectNum = null;
		Double factoryProctectNum = null;
		Double blockNum = null;
		String engineer = engineerStr;
		String equDept = equDeptStr;
		String entryBy = employee.getWorkerCode();
		Date entryDate = new Date();
		String enterpriseCode = employee.getEnterpriseCode();
		String tableFlag = tableFlagStr;
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if(map.get("jdbhjd1Id") != null)
			jdbhjd1Id = Long.parseLong(map.get("jdbhjd1Id").toString());
		if(map.get("countType") != null)
			countType = map.get("countType").toString();
		if(map.get("dynamoNum") != null)
			dynamoNum = Double.parseDouble(map.get("dynamoNum").toString());
		if(map.get("transformerNum") != null)
			transformerNum = Double.parseDouble(map.get("transformerNum").toString());
		if(map.get("fbzProtectNum") != null)
			fbzProtectNum = Double.parseDouble(map.get("fbzProtectNum").toString());
		if(map.get("factoryProctectNum") != null)
			factoryProctectNum = Double.parseDouble(map.get("factoryProctectNum").toString());
		if(map.get("blockNum") != null)
			blockNum = Double.parseDouble(map.get("blockNum").toString());
		if(map.get("entryDateString") != null)
			try {
				entryDate = sdf.parse(map.get("entryDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if(map.get("entryBy") != null && !map.get("entryBy").toString().equals(""))
			entryBy = map.get("entryBy").toString();
			
		temp.setJdbhjd1Id(jdbhjd1Id);
		temp.setStrMonth(strMonth);
		temp.setCountType(countType);
		temp.setDynamoNum(dynamoNum);
		temp.setTransformerNum(transformerNum);
		temp.setFbzProtectNum(fbzProtectNum);
		temp.setFactoryProctectNum(factoryProctectNum);
		temp.setBlockNum(blockNum);
		temp.setEngineer(engineer);
		temp.setEquDept(equDept);
		temp.setEntryBy(entryBy);
		temp.setEntryDate(entryDate);
		temp.setEnterpriseCode(enterpriseCode);
		temp.setTableFlag(tableFlag);
		
		return temp;
	}
}