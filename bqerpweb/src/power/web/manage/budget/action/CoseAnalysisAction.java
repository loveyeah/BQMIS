package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.poi.poifs.storage.ListManagedBlock;
import org.apache.struts2.components.Else;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmJCost;
import power.ejb.manage.budget.CbmJCostFacadeRemote;
import power.web.comm.AbstractAction;
@SuppressWarnings("serial")
public class CoseAnalysisAction extends AbstractAction
{
	private CbmJCostFacadeRemote remote;
	public CoseAnalysisAction()
	{
		remote = (CbmJCostFacadeRemote)factory.getFacadeRemote("CbmJCostFacade");
	}
	
	public void getCostAnalysisList() throws JSONException
	{
		String time = request.getParameter("time");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		PageObject pg = new PageObject();
		if(start != null && limit != null)
			pg = remote.findAllCostRec(time, employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
		else 
			pg = remote.findAllCostRec(time, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
	
	public void saveAllCostMod() throws JSONException
	{
		String addStr = request.getParameter("add");
		String updateStr = request.getParameter("update");
		Object addObject = JSONUtil.deserialize(addStr);
		Object updateObject = JSONUtil.deserialize(updateStr);
		List<Map> addMapList = (List<Map>)addObject;
		List<Map> updateMapList = (List<Map>)updateObject;
		List<CbmJCost> addList = new ArrayList<CbmJCost>();
		List<CbmJCost> updateList = new ArrayList<CbmJCost>();
		if(addMapList != null && addMapList.size() > 0)
		{
			for(Map map : addMapList)
			{
				CbmJCost temp = this.parseInstanceOfCost(map);
				addList.add(temp);
			}
		}
		if(updateMapList != null && updateMapList.size() > 0)
		{
			for(Map map : updateMapList)
			{
				CbmJCost temp = this.parseInstanceOfCost(map);
				updateList.add(temp);
			}
		}
		if(addList.size() > 0 || updateList.size() > 0)
			remote.saveAllModRec(addList, updateList);
		write("{success : true,msg : '数据保存修改成功！'}");
	}
	
	public CbmJCost parseInstanceOfCost(Map map)
	{
		CbmJCost temp = new CbmJCost();
		Long costId = null;
		String analyseDate = null;
		Long itemId = null;
		Double factValue = 0.0;
		String memo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(map.get("costId") != null)
			costId = Long.parseLong(map.get("costId").toString());
		if(map.get("analyseDate") != null)
			analyseDate = map.get("analyseDate").toString();
		if(map.get("itemId") != null)
			itemId = Long.parseLong(map.get("itemId").toString());
		if(map.get("factValue") != null)
			factValue = Double.parseDouble(map.get("factValue").toString());
		if(map.get("memo") != null)
			memo = map.get("memo").toString();
		
		temp.setCostId(costId);
		temp.setAnalyseDate(analyseDate);
		temp.setItemId(itemId);
		temp.setFactValue(factValue);
		temp.setMemo(memo);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		
		return temp;
	}
}