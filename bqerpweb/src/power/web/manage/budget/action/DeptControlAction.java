package power.web.manage.budget.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.manage.budget.CbmCMasterItem;
import power.ejb.manage.budget.CbmCMasterItemFacadeRemote;
import power.web.comm.AbstractAction;

public class DeptControlAction extends AbstractAction
{
	private CbmCMasterItemFacadeRemote remote;
	public DeptControlAction()
	{
		remote = (CbmCMasterItemFacadeRemote)factory.getFacadeRemote("CbmCMasterItemFacade");
	}
	
	public void getDeptConList() throws JSONException
	{
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String centerId = request.getParameter("centerId");
		if(start == null || limit == null)
		{
			PageObject pg = remote.findAll(centerId,employee.getEnterpriseCode());
			write(JSONUtil.serialize(pg));
		}
		else {
			PageObject pg = remote.findAll(centerId,employee.getEnterpriseCode(),Integer.parseInt(start),Integer.parseInt(limit));
			write(JSONUtil.serialize(pg));
		}
		
		
	}
	
	public void saveDeptControlInput() throws JSONException
	{
		String addString = request.getParameter("isAdd");
		String updateString = request.getParameter("isUpdate");
		String deleteIds = request.getParameter("isDelete");
		Object addObject = JSONUtil.deserialize(addString);
		Object updateObject = JSONUtil.deserialize(updateString);
		List<Map> alist = (List<Map>)addObject; 
		List<Map> ulist = (List<Map>)updateObject;
		List<CbmCMasterItem> addList = new ArrayList<CbmCMasterItem>();
		List<CbmCMasterItem> updateList = new  ArrayList<CbmCMasterItem>();
		
		
		for(Map data : alist)
		{
			Long masterId = null;
			Long centerId = null;
			Long itemId = null;
			String itemAlias = null;
			String masterMode = null;
			String dataType = null;
			String sysSource = null;
			String comeFrom = null;
			Long displayNo = null;
			String isUse = "Y";
			String enterpriseCode = employee.getEnterpriseCode();
			
			if(data.get("ccm.masterId") != null)
				masterId = Long.parseLong(data.get("ccm.masterId").toString());
			if(data.get("ccm.centerId") != null)
				centerId = Long.parseLong(data.get("ccm.centerId").toString());
			if(data.get("ccm.itemId") != null)
				itemId = Long.parseLong(data.get("ccm.itemId").toString());
			if(data.get("ccm.itemAlias") != null)
				itemAlias = data.get("ccm.itemAlias").toString();
			if(data.get("ccm.masterMode") != null)
				masterMode = data.get("ccm.masterMode").toString();
			if(data.get("ccm.dataType") != null)
				dataType = data.get("ccm.dataType").toString();
			if(data.get("ccm.sysSource") != null)
				sysSource = data.get("ccm.sysSource").toString();
			if(data.get("ccm.comeFrom") != null)
				comeFrom = data.get("ccm.comeFrom").toString();
			if(data.get("ccm.displayNo") != null)
				displayNo = Long.parseLong(data.get("ccm.displayNo").toString());
			
			CbmCMasterItem temp = new CbmCMasterItem();
			temp.setMasterId(masterId);
			temp.setCenterId(centerId);
			temp.setItemId(itemId);
			temp.setItemAlias(itemAlias);
			temp.setMasterMode(masterMode);
			temp.setDataType(dataType);
			temp.setSysSource(sysSource);
			temp.setComeFrom(comeFrom);
			temp.setDisplayNo(displayNo);
			temp.setIsUse(isUse);
			temp.setEnterpriseCode(enterpriseCode);
			addList.add(temp);
		}
		
		for(Map data : ulist)
		{
			Long masterId = null;
			Long centerId = null;
			Long itemId = null;
			String itemAlias = null;
			String masterMode = null;
			String dataType = null;
			String sysSource = null;
			String comeFrom = null;
			Long displayNo = null;
			String isUse = "Y";
			String enterpriseCode = employee.getEnterpriseCode();
			
			if(data.get("ccm.masterId") != null)
				masterId = Long.parseLong(data.get("ccm.masterId").toString());
			if(data.get("ccm.centerId") != null)
				centerId = Long.parseLong(data.get("ccm.centerId").toString());
			if(data.get("ccm.itemId") != null)
				itemId = Long.parseLong(data.get("ccm.itemId").toString());
			if(data.get("ccm.itemAlias") != null)
				itemAlias = data.get("ccm.itemAlias").toString();
			if(data.get("ccm.masterMode") != null)
				masterMode = data.get("ccm.masterMode").toString();
			if(data.get("ccm.dataType") != null)
				dataType = data.get("ccm.dataType").toString();
			if(data.get("ccm.sysSource") != null)
				sysSource = data.get("ccm.sysSource").toString();
			if(data.get("ccm.comeFrom") != null)
				comeFrom = data.get("ccm.comeFrom").toString();
			if(data.get("ccm.displayNo") != null)
				displayNo = Long.parseLong(data.get("ccm.displayNo").toString());
			
			CbmCMasterItem temp = new CbmCMasterItem();
			temp.setMasterId(masterId);
			temp.setCenterId(centerId);
			temp.setItemId(itemId);
			temp.setItemAlias(itemAlias);
			temp.setMasterMode(masterMode);
			temp.setDataType(dataType);
			temp.setSysSource(sysSource);
			temp.setComeFrom(comeFrom);
			temp.setDisplayNo(displayNo);
			temp.setIsUse(isUse);
			temp.setEnterpriseCode(enterpriseCode);
			updateList.add(temp);
		}
		if(addList != null || updateList != null || deleteIds != null)
		{
			if(addList.size() > 0 || updateList.size() > 0 || deleteIds.length() > 0)
			{
				remote.saveDeptControlInput(addList,updateList,deleteIds);
			}
		}
		
	}
}