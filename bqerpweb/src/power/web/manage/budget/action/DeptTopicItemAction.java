package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import power.ejb.manage.budget.*;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmCCenterItem;
import power.ejb.manage.budget.CbmCCenterItemFacadeRemote;
import power.web.comm.AbstractAction;



public class DeptTopicItemAction extends AbstractAction
{
	private CbmCCenterItemFacadeRemote remote;
	private CbmCCenterUseFacadeRemote  remotee;
	
	public DeptTopicItemAction()
	{
		remote = (CbmCCenterItemFacadeRemote)factory.getFacadeRemote("CbmCCenterItemFacade");
		remotee = (CbmCCenterUseFacadeRemote)factory.getFacadeRemote("CbmCCenterUseFacade");
	}
	
	public void findDeptTopicItemList() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String centerTopicId = request.getParameter("centerTopicId");
		if (start == null || limit == null) {
			PageObject pg = remote.findDeptTopicItemList(centerTopicId,
					employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		} else {
			PageObject pg = remote.findDeptTopicItemList(centerTopicId,
					employee.getEnterpriseCode(), Integer.parseInt(start),
					Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
		
			
	}
	
	public void saveDeptTopicItem() throws JSONException
	{
		String addString = request.getParameter("isAdd");
		String updateString = request.getParameter("isUpdate");
		String deleteIds = request.getParameter("isDelete");
		Object addObject = JSONUtil.deserialize(addString);
		Object updateObject = JSONUtil.deserialize(updateString);
		List<Map> alist = (List<Map>)addObject; 
		List<Map> ulist = (List<Map>)updateObject;
		List<CbmCCenterItem> addList = new ArrayList<CbmCCenterItem>();
		List<CbmCCenterItem> updateList = new  ArrayList<CbmCCenterItem>();
		//modify by ypan 20100903
		for(Map data : alist)
		{
			Long centerItemId = null;
			Long centerTopicId = null;
			Long itemId = null;
			String itemAlias = null;
			String dataSource = null;
			Long displayNo = null;
			String dataType=null;
			String masterMode=null;
			String isUse = "Y";
			String enterpriseCode = employee.getEnterpriseCode();
			
			if(data.get("ccc.dataType") != null)
				dataType = data.get("ccc.dataType").toString();
			if(data.get("ccc.masterMode") != null)
				masterMode = data.get("ccc.masterMode").toString();
			if(data.get("ccc.centerItemId") != null)
				centerItemId = Long.parseLong(data.get("ccc.centerItemId").toString());
			if(data.get("ccc.centerTopicId") != null)
				centerTopicId = Long.parseLong(data.get("ccc.centerTopicId").toString());
			if(data.get("ccc.itemId") != null)
				itemId = Long.parseLong(data.get("ccc.itemId").toString());
			if(data.get("ccc.itemAlias") != null)
				itemAlias = data.get("ccc.itemAlias").toString();
			if(data.get("ccc.dataSource") != null)
				dataSource = data.get("ccc.dataSource").toString();
			if(data.get("ccc.displayNo") != null)
				displayNo = Long.parseLong(data.get("ccc.displayNo").toString());
			CbmCCenterItem temp = new CbmCCenterItem();
			temp.setCenterItemId(centerItemId);
			temp.setCenterTopicId(centerTopicId);
			temp.setItemId(itemId);
			temp.setItemAlias(itemAlias);
			temp.setDataSource(dataSource);
			temp.setDisplayNo(displayNo);
			temp.setDataType(dataType);
			temp.setMasterMode(masterMode);
			temp.setIsUse(isUse);
			temp.setEnterpriseCode(enterpriseCode);
			
			
			addList.add(temp);			
		}
		
		//modify by ypan 20100903
		for(Map data : ulist)
		{
			Long centerItemId = null;
			Long centerTopicId = null;
			Long itemId = null;
			String itemAlias = null;
			String dataSource = null;
			String dataType = null;
			String masterMode = null;
			Long displayNo = null;
			String isUse = "Y";
			String enterpriseCode = employee.getEnterpriseCode();
			
			
			if(data.get("ccc.dataType") != null)
				dataType = data.get("ccc.dataType").toString();
			if(data.get("ccc.masterMode") != null)
				masterMode = data.get("ccc.masterMode").toString();
			if(data.get("ccc.centerItemId") != null)
				centerItemId = Long.parseLong(data.get("ccc.centerItemId").toString());
			if(data.get("ccc.centerTopicId") != null)
				centerTopicId = Long.parseLong(data.get("ccc.centerTopicId").toString());
			if(data.get("ccc.itemId") != null)
				itemId = Long.parseLong(data.get("ccc.itemId").toString());
			if(data.get("ccc.itemAlias") != null)
				itemAlias = data.get("ccc.itemAlias").toString();
			if(data.get("ccc.dataSource") != null)
				dataSource = data.get("ccc.dataSource").toString();
			if(data.get("ccc.displayNo") != null)
				displayNo = Long.parseLong(data.get("ccc.displayNo").toString());
			CbmCCenterItem temp = new CbmCCenterItem();
			temp.setCenterItemId(centerItemId);
			temp.setCenterTopicId(centerTopicId);
			temp.setItemId(itemId);
			temp.setItemAlias(itemAlias);
			temp.setDataSource(dataSource);
			temp.setDataType(dataType);
			temp.setMasterMode(masterMode);
			temp.setDisplayNo(displayNo);
			temp.setIsUse(isUse);
			temp.setEnterpriseCode(enterpriseCode);
			
			updateList.add(temp);			
		}
		
		if(addList != null || updateList != null || deleteIds != null)
		{
			if(addList.size() > 0 || updateList.size() > 0 || deleteIds.length() > 0)
			{
				remote.saveDeptTopicItem(addList, updateList, deleteIds);
			}
		}		
	}
	
	public void judgeDeptItem()
	{
		String center = request.getParameter("centerId");
		String itemIds = request.getParameter("itemIds");
		String dciIds = request.getParameter("dciIds");
		Long centerId = Long.parseLong(center);
		boolean isExist = remote.judgeIsExist(centerId,itemIds,dciIds,employee.getEnterpriseCode());
		if(isExist)
		{
			write("{success: true,msg:'同一部门下不允许有多个相同指标！'}");
		}
		else {
			write("{success: false,msg:'所选指标都具有唯一性！'}");
		}
	}
	
	//add by ypan 20100903
	public void queryCenterUse() throws JSONException {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String itemIds = request.getParameter("itemId");
		if (start == null || limit == null) {
			PageObject pg = remotee.findCenterList(itemIds,
					employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		} else {
			PageObject pg = remotee.findCenterList(itemIds,
					employee.getEnterpriseCode(), Integer.parseInt(start),
					Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
		
	}
	
    public void saveCenterUse() throws JSONException {
    	String addString = request.getParameter("isAdd");
		String updateString = request.getParameter("isUpdate");
		String deleteIds = request.getParameter("isDelete");
		String itemIds = request.getParameter("itemId");
		long itemId = Long.parseLong(itemIds);
		Object addObject = JSONUtil.deserialize(addString);
		Object updateObject = JSONUtil.deserialize(updateString);
		List<Map> alist = (List<Map>)addObject; 
		List<Map> ulist = (List<Map>)updateObject;
		List<CbmCCenterUse> addList = new ArrayList<CbmCCenterUse>();
		List<CbmCCenterUse> updateList = new  ArrayList<CbmCCenterUse>();
		for(Map data : alist)
		{   String depName=null;
			Long useId = 0l;
			Long centerId = 0l;
			String isUse = "Y";
			String enterpriseCode = employee.getEnterpriseCode();
			if(data.get("centerId") != null)
			centerId = Long.parseLong(data.get("centerId").toString());
			if(data.get("depName") != null)
				depName = data.get("depName").toString();
			boolean existFlag = false;
			existFlag = remotee.checkDepName(itemId,depName,enterpriseCode,useId);
			if (existFlag == true) {
				write("{success:true,existFlag:" + existFlag + "}");
			}else{
			CbmCCenterUse temp = new CbmCCenterUse();
			temp.setIsUse(isUse);
			temp.setItemId(itemId);
			temp.setCenterId(centerId);
			temp.setEnterpriseCode(enterpriseCode);
			addList.add(temp);
			}	
		}
		//modify by ypan 20100903
		for(Map data : ulist)
		{   String depName=null;
			Long useId = null;
			Long centerId = null;
			String isUse = "Y";
			String enterpriseCode = employee.getEnterpriseCode();
			if(data.get("centerId") != null)
			 centerId = Long.parseLong(data.get("centerId").toString());
			if(data.get("useId")!=null)
				useId=Long.parseLong(data.get("useId").toString());
			if(data.get("depName") != null)
				depName = data.get("depName").toString();
			boolean existFlag = false;
			existFlag = remotee.checkDepName(itemId,depName,enterpriseCode,useId);
			if (existFlag == true) {
				write("{success:true,existFlag:" + existFlag + "}");
			}else{
			CbmCCenterUse temp = new CbmCCenterUse();
			temp.setIsUse(isUse);
			temp.setItemId(itemId);
			temp.setCenterId(centerId);
			temp.setEnterpriseCode(enterpriseCode);
			temp.setUseId(useId);
			updateList.add(temp);
			}
		}
		if(addList != null || updateList != null || deleteIds != null)
		{
			if(addList.size() > 0 || updateList.size() > 0 || deleteIds.length() > 0)
			{
				remotee.saveCenter(addList, updateList, deleteIds);
				write("{success:true,existFlag:false}");
			}
		}
	}

}