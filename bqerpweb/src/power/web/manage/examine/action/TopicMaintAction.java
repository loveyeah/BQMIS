package power.web.manage.examine.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.exam.BpCCbmTopic;
import power.ejb.manage.exam.BpCCbmTopicFacadeRemote;
import power.web.comm.AbstractAction;
@SuppressWarnings("serial")
public class TopicMaintAction extends AbstractAction
{
	private BpCCbmTopicFacadeRemote remote;
	
	public TopicMaintAction()
	{
		remote = (BpCCbmTopicFacadeRemote)factory.getFacadeRemote("BpCCbmTopicFacade");
	}
	
	public void findAllTopic() throws JSONException
	{
		PageObject pg = null;
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		if(start != null && limit != null)
			pg = remote.findAllTopic(employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
		else
			pg = remote.findAllTopic(employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));
	}
	
	public void saveModifiedTopicRec() throws JSONException
	{
		String isAdd = request.getParameter("isAdd");
		String isUpdate = request.getParameter("isUpdate");
		String ids = request.getParameter("ids");
		Object addObject = JSONUtil.deserialize(isAdd);
		Object updateObject = JSONUtil.deserialize(isUpdate);
		List<Map> addListMap = (List<Map>)addObject;
		List<Map> updateListMap = (List<Map>)updateObject;
		List<BpCCbmTopic> addList = new ArrayList<BpCCbmTopic>();
		List<BpCCbmTopic> updateList = new ArrayList<BpCCbmTopic>();
		
		for(Map map : addListMap)
		{
			BpCCbmTopic temp = this.parseTopicInstance(map);
			addList.add(temp);
		}
		for(Map map : updateListMap)
		{
			BpCCbmTopic temp = this.parseTopicInstance(map);
			updateList.add(temp);
		}
		
		if(addList.size() > 0 || updateList.size() > 0 || ids != null)
		{
			remote.saveModifiedRec(addList, updateList, ids);
		}
		write("{success:true,msg:'数据保存修改成功！'}");
	}
	
	public BpCCbmTopic parseTopicInstance(Map map)
	{
		BpCCbmTopic temp = new BpCCbmTopic();
		
		Long topicId = null;
		String topicName = null;
		Double coefficient = 0.0;
		Long displayNo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(map.get("topicId") != null)
			topicId = Long.parseLong(map.get("topicId").toString());
		if(map.get("topicName") != null)
			topicName = map.get("topicName").toString();
		if(map.get("coefficient") != null)
			coefficient = Double.parseDouble(map.get("coefficient").toString());
		if(map.get("displayNo") != null && !map.get("displayNo").toString().equals(""))
			displayNo = Long.parseLong(map.get("displayNo").toString());
		
		temp.setTopicId(topicId);
		temp.setTopicName(topicName);
		temp.setCoefficient(coefficient);
		temp.setDisplayNo(displayNo);
		temp.setIsUse(isUse);
		temp.setEnterpriseCode(enterpriseCode);
		
		return temp;
	}
}