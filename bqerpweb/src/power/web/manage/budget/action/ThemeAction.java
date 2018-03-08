package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.CodeRepeatException;
import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmCTopic;
import power.ejb.manage.budget.CbmCTopicFacadeRemote;
import power.web.comm.AbstractAction;

public class ThemeAction extends AbstractAction 
{
	private CbmCTopicFacadeRemote remote;
	
	public ThemeAction()
	{
		remote = (CbmCTopicFacadeRemote)factory.getFacadeRemote("CbmCTopicFacade");
	}
	
	public void saveDependThemeInput() throws JSONException
	{
		String addString = request.getParameter("isAdd");
		String str = request.getParameter("isUpdate");
		String deleteId = request.getParameter("isDelete");
		String[] ss = deleteId.split(",");
		String ids = deleteId.replaceAll(",", "','");
		ids = "'" + ids + "'";
		Object addObject = JSONUtil.deserialize(addString);
		Object object = JSONUtil.deserialize(str);
		List<Map> alist = (List<Map>)addObject;
		List<Map> ulist = (List<Map>)object;
			List<CbmCTopic> addList = new ArrayList<CbmCTopic>();
			List<CbmCTopic> updateList = new ArrayList<CbmCTopic>();
//		List<CbmCTopic> editList = new ArrayList<CbmCTopic>();
		for(Map data : alist)
		{
			Long  topicId = null;
			String topicCode = null;
			String topicName = null;
			String budgetType = null;
			String dataType = null;
			String timeType = null;
			String isUse = "Y";
			String enterpriseCode = employee.getEnterpriseCode();
			if(data.get("topicId") != null)
				topicId = Long.parseLong(data.get("topicId").toString());
			if(data.get("topicCode") != null)
				topicCode = data.get("topicCode").toString();
			if(data.get("topicName") != null)
				topicName = data.get("topicName").toString();
			if(data.get("budgetType") != null)
				budgetType = data.get("budgetType").toString();
			if(data.get("dataType") != null)
				dataType = data.get("dataType").toString();
			
			CbmCTopic temp = new CbmCTopic();
			temp.setTopicId(topicId);
			temp.setTopicCode(topicCode);
			temp.setTopicName(topicName);
			temp.setBudgetType(budgetType);
			temp.setDataType(dataType);
			temp.setTimeType(timeType);
			temp.setIsUse(isUse);
			temp.setEnterpriseCode(enterpriseCode);
			
			addList.add(temp);
		}
		
		for(Map data : ulist)
		{
			Long  topicId = null;
			String topicCode = null;
			String topicName = null;
			String budgetType = null;
			String dataType = null;
			String timeType = null;
			String isUse = "Y";
			String enterpriseCode = employee.getEnterpriseCode();
			if(data.get("topicId") != null)
				topicId = Long.parseLong(data.get("topicId").toString());
			if(data.get("topicCode") != null)
				topicCode = data.get("topicCode").toString();
			if(data.get("topicName") != null)
				topicName = data.get("topicName").toString();
			if(data.get("budgetType") != null)
				budgetType = data.get("budgetType").toString();
			if(data.get("dataType") != null)
				dataType = data.get("dataType").toString();
			
			CbmCTopic temp = new CbmCTopic();
			temp.setTopicId(topicId);
			temp.setTopicCode(topicCode);
			temp.setTopicName(topicName);
			temp.setBudgetType(budgetType);
			temp.setDataType(dataType);
			temp.setTimeType(timeType);
			temp.setIsUse(isUse);
			temp.setEnterpriseCode(enterpriseCode);
			
			updateList.add(temp);
		}
		
		if(addList != null || updateList != null || ids != null)
		{
			if(addList.size() > 0 || updateList.size() > 0 || deleteId.length() > 0)
			{
				String flagString = remote.save(addList,updateList,ids);
				if(!flagString.equals(""))
				{
					write("{failure:true,msg:'" + flagString + "'}");
				}
				else
				{
					write("{success:true,msg:'数据保存修改成功！'}");
				}
			}
		}		
		
	}
	
	public void getThemeList() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start == null || limit == null)
		{
			PageObject pg = remote.findAll(employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		}
		else {
			PageObject pg = remote.findAll(employee.getEnterpriseCode(), Integer.parseInt(start),Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));	
		}
		
	}
}
