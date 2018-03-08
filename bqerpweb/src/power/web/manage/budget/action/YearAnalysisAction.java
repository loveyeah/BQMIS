package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmJAnalysisYear;
import power.ejb.manage.budget.CbmJAnalysisYearFacadeRemote;
import power.web.comm.AbstractAction;

public class YearAnalysisAction extends AbstractAction
{
	private CbmJAnalysisYearFacadeRemote remote;
	public YearAnalysisAction()
	{
		remote = (CbmJAnalysisYearFacadeRemote)factory.getFacadeRemote("CbmJAnalysisYearFacade");
	}
	
	public void getYearAnalysisList() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String centerIdStr = request.getParameter("centerId");
		String dataTime = request.getParameter("dataTime");
		Long centerId = null;
		if(centerIdStr != null)
			centerId = Long.parseLong(centerIdStr);
		if(start == null || limit == null)
		{
			PageObject pg = remote.findAnalysisYearList(centerId, dataTime, employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		}
		else
		{
			PageObject pg = remote.findAnalysisYearList(centerId, dataTime, employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
	}
	
	public void deleteYearAnalysis()
	{
		String ids = request.getParameter("ids");
		remote.delete(ids);
		write("{success: true,msg:'数据清空成功！'}");
	}
	
	public void saveYearAnalysisModified() throws JSONException
	{
		String addString = request.getParameter("isAdd");
		String updateString = request.getParameter("isUpdate");
		Object addObject = JSONUtil.deserialize(addString);
		Object updateObject = JSONUtil.deserialize(updateString);
		List<Map> alist  = (List<Map>)addObject;
		List<Map> ulist = (List<Map>)updateObject;
		List<CbmJAnalysisYear> addList = new ArrayList<CbmJAnalysisYear>();
		List<CbmJAnalysisYear> updateList = new ArrayList<CbmJAnalysisYear>();
		
		for(Map data : alist)
		{
			CbmJAnalysisYear temp = this.parseAnalysisYear(data);
			addList.add(temp);
		}
		for(Map data : ulist)
		{
			CbmJAnalysisYear temp = this.parseAnalysisYear(data);
			updateList.add(temp);
		}
		
		if(addList != null || updateList != null)
		{
			if(addList.size() > 0 || updateList.size() > 0)
			{
				remote.saveAnalysisYearModified(addList, updateList);
				write("{success: true,msg:'数据保存成功！'}");
			}
		}
	}
	
	/**
	 * 将一映射转化为CbmJAnalysisYear对象
	 */
	public CbmJAnalysisYear parseAnalysisYear(Map data)
	{
		CbmJAnalysisYear temp = new CbmJAnalysisYear();
		Long analysisYearId = null;
		Long itemId = null;
		Long centerId = null;
		String dataTime = null;
		Double totalFact = null;
		Double yearBudget = null;
		Double percentValue = null;
		String itemContent = null;
		String itemExplain = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(data.get("analysisYearId") != null)
			temp.setAnalysisYearId(Long.parseLong(data.get("analysisYearId").toString()));
		if(data.get("itemId") != null)
			temp.setItemId(Long.parseLong(data.get("itemId").toString()));
		if(data.get("centerId") != null)
			temp.setCenterId(Long.parseLong(data.get("centerId").toString()));
		if(data.get("dataTime") != null)
			temp.setDataTime(data.get("dataTime").toString());
		if(data.get("totalFact") != null)
			temp.setTotalFact(Double.parseDouble(data.get("totalFact").toString()));
		if(data.get("yearBudget") != null)
			temp.setYearBudget(Double.parseDouble(data.get("yearBudget").toString()));
		if(data.get("percentValue") != null)
			temp.setPercentValue(Double.parseDouble(data.get("percentValue").toString()));
		if(data.get("itemContent") != null)
			temp.setItemContent(data.get("itemContent").toString());
		if(data.get("itemExplain") != null)
			temp.setItemExplain(data.get("itemExplain").toString());
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		
		return temp;
		
			
			
			
			
			
			
	}
}