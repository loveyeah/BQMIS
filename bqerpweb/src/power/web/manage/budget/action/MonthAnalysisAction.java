package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmJAnalysisMonth;
import power.ejb.manage.budget.CbmJAnalysisMonthFacadeRemote;
import power.web.comm.AbstractAction;

public class MonthAnalysisAction extends AbstractAction
{
	private CbmJAnalysisMonthFacadeRemote remote;
	public MonthAnalysisAction()
	{
		remote = (CbmJAnalysisMonthFacadeRemote)factory.getFacadeRemote("CbmJAnalysisMonthFacade");
	}
	
	
	/**
	 * 删除数据
	 */
	public void deleteMonthAnlysis()
	{
		String ids = request.getParameter("ids");
		remote.delete(ids);
		write("{success: true,msg:'数据清空成功！'}");
	}
	/**
	 * 查询预算月份列表
	 * @throws JSONException
	 */
	public void getMonthAnlysisList() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String centerIdStr = request.getParameter("centerId");
		Long centerId = null;
		if(centerIdStr != null)
			centerId = Long.parseLong(centerIdStr);
		String dataTime = request.getParameter("dataTime");
		if(start == null || limit == null)
		{
			PageObject pg = remote.findAnalysisMonthList(centerId, dataTime, employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		}
		else
		{
			PageObject pg = remote.findAnalysisMonthList(centerId, dataTime, employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
	}
	
	/**
	 * 修改预算月份数据
	 * @throws JSONException
	 */
	public void saveMonthModified() throws JSONException
	{
		String addString = request.getParameter("isAdd");
		String updateString = request.getParameter("isUpdate");
		Object addObject = JSONUtil.deserialize(addString);
		Object updateObject = JSONUtil.deserialize(updateString);
		List<Map> alist  = (List<Map>)addObject;
		List<Map> ulist = (List<Map>)updateObject;
		List<CbmJAnalysisMonth> addList = new ArrayList<CbmJAnalysisMonth>();
		List<CbmJAnalysisMonth> updateList = new ArrayList<CbmJAnalysisMonth>();
		
		for(Map data : alist)
		{
			CbmJAnalysisMonth temp = this.parseMonthAna(data);			
			addList.add(temp);
		}
		for(Map data : ulist)
		{
			CbmJAnalysisMonth temp = this.parseMonthAna(data);
			updateList.add(temp);
		}
		
		if(addList != null || updateList != null)
		{
			if(addList.size() >0 || updateList.size() > 0)
			{
				remote.saveAnalysisMonthModified(addList, updateList);
				write("{success: true,msg:'数据保存成功！'}");
			}
		}		
	}
	
	/**
	 * 将一映射转为CbmJAnalysisMonth对象
	 * @param data
	 * @return
	 */
	public CbmJAnalysisMonth parseMonthAna(Map data)
	{
		CbmJAnalysisMonth temp = new CbmJAnalysisMonth();
		Long analysisMonthId = null;
		Long itemId = null;
		Long centerId = null;
		String dataTime = null;
		Double budgetValue = null;
		Double factValue = null;
		Double addReduce = null;
		String itemContent = null;
		String itemExplain = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(data.get("analysisMonthId") != null)
			analysisMonthId = Long.parseLong(data.get("analysisMonthId").toString());
		if(data.get("itemId") != null)
			itemId = Long.parseLong(data.get("itemId").toString());
		if(data.get("centerId") != null)
			centerId = Long.parseLong(data.get("centerId").toString());
		if(data.get("dataTime") != null)
			dataTime = data.get("dataTime").toString();
		if(data.get("budgetValue") != null)
			budgetValue = Double.parseDouble(data.get("budgetValue").toString());
		if(data.get("factValue") != null)
			factValue = Double.parseDouble(data.get("factValue").toString());
		if(data.get("addReduce") != null)
			addReduce = Double.parseDouble(data.get("addReduce").toString());
		if(data.get("itemContent") != null)
			itemContent = data.get("itemContent").toString();
		if(data.get("itemExplain") != null)
			itemExplain = data.get("itemExplain").toString();
		
		temp.setAnalysisMonthId(analysisMonthId);
		temp.setItemId(itemId);
		temp.setCenterId(centerId);
		temp.setDataTime(dataTime);
		temp.setBudgetValue(budgetValue);
		temp.setFactValue(factValue);
		temp.setAddReduce(addReduce);
		temp.setItemContent(itemContent);
		temp.setItemExplain(itemExplain);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		
		return temp;
	}
}