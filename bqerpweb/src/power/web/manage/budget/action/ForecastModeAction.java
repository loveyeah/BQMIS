package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmJModelForecastFacadeRemote;
import power.ejb.manage.budget.form.CapitalDetailForm;
import power.ejb.manage.budget.form.ForecastItemForm;
import power.web.comm.AbstractAction;

public class ForecastModeAction extends AbstractAction
{
	private CbmJModelForecastFacadeRemote remote;
	public ForecastModeAction()
	{
		remote = (CbmJModelForecastFacadeRemote)factory.getFacadeRemote("CbmJModelForecastFacade");
	}
	
	public void getForcastRec() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String forecastTime = request.getParameter("forecastTime");
		String modelType = request.getParameter("modelType");
		if(start == null || limit == null)
		{
			PageObject pg = remote.findAllModelData(forecastTime, modelType, employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		}
		else {
			PageObject pg = remote.findAllModelData(forecastTime, modelType, employee.getEnterpriseCode()
					,Integer.parseInt(start),Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
	}
		
	/**
	 * 批量保存修改预测指标数据表记录
	 * @throws JSONException 
	 */
	public void saveForecastItemMod() throws JSONException
	{
		String modString = request.getParameter("isMod");
		Object modObject = JSONUtil.deserialize(modString);
		List<Map> mlist = (List<Map>)modObject;
		List<ForecastItemForm> modList = new ArrayList<ForecastItemForm>();

		for(Map data : mlist)
		{
			ForecastItemForm temp = this.parseForecastItem(data);
			modList.add(temp);
		}	
		if(modList != null)
		{
			if(modList.size() >0 )
			{
				remote.saveForecastItem(modList);
				write("{success: true,msg:'数据保存修改成功！'}");
			}
		}	
	}
	
	public ForecastItemForm parseForecastItem(Map data)
	{
		ForecastItemForm temp = new ForecastItemForm();
		Long modelItemId = null;
		String modelItemCode = null;
		String modelItemName = null;
		String modelType = null;
		String isItem = null;
		String comeFrom = null;
		Long modelOrder = null;
		Long displayNo = null;
		String modelItemExplain = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		Long forecastId = null;
		String forecastTime = null;
		Double forecastValue = 0.0;
		
		if(data.get("modelItemId") != null)
			modelItemId = Long.parseLong(data.get("modelItemId").toString());
		if(data.get("modelItemCode") != null)
			modelItemCode = data.get("modelItemCode").toString();
		if(data.get("modelItemName") != null)
			modelItemName = data.get("modelItemName").toString();
		if(data.get("modelType") != null)
			modelType = data.get("modelType").toString();
		if(data.get("isItem") != null)
			isItem = data.get("isItem").toString();
		if(data.get("comeFrom") != null)
			comeFrom = data.get("comeFrom").toString();
		if(data.get("modelOrder") != null)
			modelOrder = Long.parseLong(data.get("modelOrder").toString());
		if(data.get("modelItemExplain") != null)
			modelItemExplain = data.get("modelItemExplain").toString();		
		if(data.get("forecastId") != null)
			forecastId = Long.parseLong(data.get("forecastId").toString());
		if(data.get("forecastTime") != null)
			forecastTime = data.get("forecastTime").toString();
		if(data.get("forecastValue") != null)
			forecastValue = Double.parseDouble(data.get("forecastValue").toString());
		
		temp.setModelItemId(modelItemId);
		temp.setModelItemCode(modelItemCode);
		temp.setModelItemName(modelItemName);
		temp.setModelType(modelType);
		temp.setIsItem(isItem);
		temp.setComeFrom(comeFrom);
		temp.setModelOrder(modelOrder);
		temp.setDisplayNo(displayNo);
		temp.setModelItemExplain(modelItemExplain);
		temp.setForecastId(forecastId);
		temp.setForecastTime(forecastTime);
		temp.setForecastValue(forecastValue);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		
		return temp;
	}
	
	public void getForcastModelRecords() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String forecastTime = request.getParameter("forecastTime");
		if(start == null || limit == null)
		{
			PageObject pg = remote.findModelAnalysisData(forecastTime, employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		}
		else {
			PageObject pg = remote.findModelAnalysisData(forecastTime, employee.getEnterpriseCode()
					,Integer.parseInt(start),Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
	}
}