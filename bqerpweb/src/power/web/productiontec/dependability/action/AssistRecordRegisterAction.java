package power.web.productiontec.dependability.action;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxJFjregister;
import power.ejb.productiontec.dependabilityAnalysis.PtKkxJFjregisterFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class AssistRecordRegisterAction extends AbstractAction
{
	private PtKkxJFjregisterFacadeRemote remote;
	
	public AssistRecordRegisterAction()
	{
		remote = (PtKkxJFjregisterFacadeRemote)factory.getFacadeRemote("PtKkxJFjregisterFacade");
	}
	
	public void findAssistRecList() throws JSONException {
		String month = request.getParameter("month");
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.getRecordList(month, employee.getEnterpriseCode(),
					start,limit);		
		} else {

			object = remote.getRecordList(month, employee.getEnterpriseCode());
		}

		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
		
	
	public void modifyAssistRec() throws JSONException
	{
		String addStr = request.getParameter("add");
		String updateStr = request.getParameter("update");
		String ids = request.getParameter("ids");
		
		Object addObj = JSONUtil.deserialize(addStr);
		Object updateObj = JSONUtil.deserialize(updateStr);
		
		List<Map> addMapList = (List<Map>)addObj;
		List<Map> updateMapList = (List<Map>)updateObj;
		
		List<PtKkxJFjregister> addList = new ArrayList<PtKkxJFjregister>();
		List<PtKkxJFjregister> updateList = new ArrayList<PtKkxJFjregister>();
		
		for(Map map : addMapList)
		{
			PtKkxJFjregister temp = this.parseInstanceOfAssistRec(map);
			addList.add(temp);
		}
		for(Map map : updateMapList)
		{
			PtKkxJFjregister temp = this.parseInstanceOfAssistRec(map);
			updateList.add(temp);
		}
		
		if(addList != null || updateList != null || ids != null)
			remote.modifyRec(addList,updateList,ids);
		write("{success : true,msg : '数据保存修改成功！'}");
		
	}
	
	public PtKkxJFjregister parseInstanceOfAssistRec(Map map)
	{
		PtKkxJFjregister reg = new PtKkxJFjregister();
		
		Long fjId = null;
		String strMonth = null;
		String fjCode = null;
		Date startDate = null;
		Date endDate = null;
		Long jzztId = null;
		Double keepTime = 0.0;
		Long standbyNum = 0L;
		Double repairMandays = 0.0;
		Double repairCost = 0.0;
		String eventCode = null;
		String eventReason = null;
		String eventOtherReason = null;
		String entryBy = employee.getWorkerCode();
		Date entryDate = new Date();
		String enterpriseCode = employee.getEnterpriseCode();
		
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat  sdf2 = new SimpleDateFormat("yyyy-MM-dd");
		if(map.get("fjId") != null)
			fjId = Long.parseLong(map.get("fjId").toString());
		if(map.get("strMonth") != null)
			strMonth = map.get("strMonth").toString();
		if(map.get("fjCode") != null)
			fjCode = map.get("fjCode").toString();
		if(map.get("startDateString") != null)
			try {
				startDate = sdf1.parse(map.get("startDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if(map.get("endDateString") != null)
			try {
				endDate = sdf1.parse(map.get("endDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if(map.get("jzztId") != null)
			jzztId = Long.parseLong(map.get("jzztId").toString());
		if(map.get("keepTime") != null)
			keepTime = Double.parseDouble(map.get("keepTime").toString());
		if(map.get("standbyNum") != null)
			standbyNum = Long.parseLong(map.get("standbyNum").toString());
		if(map.get("repairMandays") != null)
			repairMandays = Double.parseDouble(map.get("repairMandays").toString());
		if(map.get("repairCost") != null)
			repairCost = Double.parseDouble(map.get("repairCost").toString());
		if(map.get("eventCode") != null)
			eventCode = map.get("eventCode").toString();
		if(map.get("eventReason") != null)
			eventReason = map.get("eventReason").toString();
		if(map.get("eventOtherReason") != null)
			eventOtherReason = map.get("eventOtherReason").toString();
		if(map.get("entryBy") != null)
			entryBy = map.get("entryBy").toString();
		if(map.get("entryDateString") != null)
			try {
				entryDate = sdf2.parse(map.get("entryDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		
		reg.setFjId(fjId);
		reg.setStrMonth(strMonth);
		reg.setFjCode(fjCode);
		reg.setStartDate(startDate);
		reg.setEndDate(endDate);
		reg.setJzztId(jzztId);
		reg.setKeepTime(keepTime);
		reg.setStandbyNum(standbyNum);
		reg.setRepairMandays(repairMandays);
		reg.setRepairCost(repairCost);
		reg.setEventCode(eventCode);
		reg.setEventReason(eventReason);
		reg.setEventOtherReason(eventOtherReason);
		reg.setEntryBy(entryBy);
		reg.setEntryDate(entryDate);
		reg.setEnterpriseCode(enterpriseCode);
		return reg;
	}
}