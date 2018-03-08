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
import power.ejb.productiontec.report.PtJQuickProtectCount;
import power.ejb.productiontec.report.PtJQuickProtectCountFacadeRemote;
import power.web.comm.AbstractAction;
/**
 * 快速保护（线路高频保护及元件差动保护）统计报表
 * @author liuyi 0901016
 *
 */
@SuppressWarnings("serial")
public class QuickProtectCountAction extends AbstractAction
{
	private PtJQuickProtectCountFacadeRemote remote;
	public QuickProtectCountAction()
	{
		remote = (PtJQuickProtectCountFacadeRemote)factory.getFacadeRemote("PtJQuickProtectCountFacade");
	}
	
	public void getQuickProtectCountList() throws JSONException
	{
		String month = request.getParameter("month");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		
		PageObject pg = new PageObject();
		
		if(start != null && limit != null)
			pg = remote.findAllByMonth(month, employee.getEnterpriseCode(),
					Integer.parseInt(start),Integer.parseInt(limit));
		else 
			pg = remote.findAllByMonth(month, employee.getEnterpriseCode());
		write(JSONUtil.serialize(pg));	
	}
	
	public void saveQuickProtectCountMod() throws JSONException
	{
		String addStr = request.getParameter("add");
		String updateStr = request.getParameter("update");
		String ids = request.getParameter("ids");
		String month = request.getParameter("month");
		String approveBy = request.getParameter("approveBy");
		String checkBy = request.getParameter("checkBy");
		
		Object addObj = JSONUtil.deserialize(addStr);
		Object updateObj = JSONUtil.deserialize(updateStr);
		
		List<Map> addMapList = (List<Map>)addObj;
		List<Map> updateMapList = (List<Map>)updateObj;
		 
		List<PtJQuickProtectCount> addList = new ArrayList<PtJQuickProtectCount>();
		List<PtJQuickProtectCount> updateList = new ArrayList<PtJQuickProtectCount>();
		
		for(Map map : addMapList)
		{
			PtJQuickProtectCount temp = this.parseInstanceOfProtectCount(map,month,approveBy,checkBy);
			addList.add(temp);
		}
		for(Map map : updateMapList)
		{
			PtJQuickProtectCount temp = this.parseInstanceOfProtectCount(map,month,approveBy,checkBy);
			updateList.add(temp);
		}
		
		if(addList != null || updateList != null)
		{
			if(addList.size() > 0 || updateList.size() > 0)
			{
				remote.saveMod(addList, updateList, ids);
			}
		}
		
		write("{success : true,msg : '数据保存成功！'}");
	}
	
	public PtJQuickProtectCount parseInstanceOfProtectCount(Map map,String month,
			String approveByStr,String checkByStr)
	{
		PtJQuickProtectCount temp = new PtJQuickProtectCount();
		
		Long chekupId = null;
		String strMonth = null;
		String protectEqu = null;
		String protectDevice = null;
		String protectType = null;
		Date exitDate = null;
		Date resumeDate = null;
		String exitReason = null;
		String checkBy = checkByStr;
		String approveBy = approveByStr;
		String entryBy = employee.getWorkerCode();
		Date entryDate = new Date();
		String enterpriseCode = employee.getEnterpriseCode();
		
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		if(map.get("chekupId") != null)
			chekupId = Long.parseLong(map.get("chekupId").toString());
		if(map.get("strMonth") != null)
			strMonth = map.get("strMonth").toString();
		if(map.get("protectEqu") != null)
			protectEqu = map.get("protectEqu").toString();
		if(map.get("protectDevice") != null)
			protectDevice = map.get("protectDevice").toString();
		if(map.get("protectType") != null)
			protectType = map.get("protectType").toString();
		if (map.get("exitDateString") != null && !map.get("exitDateString").toString().equals(""))
			try {
				exitDate = sdf.parse(map.get("exitDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (map.get("resumeDateString") != null && !map.get("resumeDateString").toString().equals(""))
			try {
				resumeDate = sdf.parse(map.get("resumeDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (map.get("entryDateString") != null && !map.get("entryDateString").toString().equals(""))
			try {
				entryDate = sdf.parse(map.get("entryDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (map.get("exitReason") != null)
			exitReason = map.get("exitReason").toString();		
		if(map.get("entryBy") != null && !map.get("entryBy").toString().equals(""))
			entryBy = map.get("entryBy").toString();
		
		temp.setChekupId(chekupId);		
		temp.setStrMonth(strMonth);
		temp.setProtectEqu(protectEqu);
		temp.setProtectDevice(protectDevice);
		temp.setProtectType(protectType);
		temp.setExitDate(exitDate);
		temp.setResumeDate(resumeDate);
		temp.setExitReason(exitReason);
		temp.setApproveBy(approveBy);
		temp.setCheckBy(checkBy);
		temp.setEntryBy(entryBy);
		temp.setEntryDate(entryDate);
		temp.setEnterpriseCode(enterpriseCode);
		
		return temp;
	}
}