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
import power.ejb.productiontec.report.PtJProtectChekup;
import power.ejb.productiontec.report.PtJProtectChekupFacadeRemote;
import power.web.comm.AbstractAction;
/**
 * 继电保护装置定检完成情况报表
 * @author liuyi 091016
 *
 */
@SuppressWarnings("serial")
public class PtJProtectCheckupAction extends AbstractAction
{
	private PtJProtectChekupFacadeRemote remote;
	
	public PtJProtectCheckupAction()
	{
		remote = (PtJProtectChekupFacadeRemote)factory.getFacadeRemote("PtJProtectChekupFacade");
	}
	
	public void getProtectCheckupRecAll() throws JSONException
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
	
	public void saveProtectCheckupRecMod() throws JSONException
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
		 
		List<PtJProtectChekup> addList = new ArrayList<PtJProtectChekup>();
		List<PtJProtectChekup> updateList = new ArrayList<PtJProtectChekup>();
		
		for(Map map : addMapList)
		{
			PtJProtectChekup temp = this.parseInstanceOfProtectCheckup(map,month,approveBy,checkBy);
			addList.add(temp);
		}
		for(Map map : updateMapList)
		{
			PtJProtectChekup temp = this.parseInstanceOfProtectCheckup(map,month,approveBy,checkBy);
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
	
	public PtJProtectChekup parseInstanceOfProtectCheckup(Map map,String month,
			String approveByStr,String checkByStr)
	{
		PtJProtectChekup temp = new PtJProtectChekup();
		
		Long chekupId = null;
		String strMonth = month;
		String protectEqu = null;
		String protectDevice = null;
		Date lastCheckDate = null;
		Date planFinishDate = null;
		Date factFinishDate = null;
		String finishThing = null;
		String notFinishReason = null;
		String hasProblem = null;
		String approveBy = approveByStr;
		String checkBy = checkByStr;
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
		if (map.get("lastCheckDateString") != null && !map.get("lastCheckDateString").toString().equals(""))
			try {
				lastCheckDate = sdf.parse(map.get("lastCheckDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (map.get("planFinishDateString") != null && !map.get("planFinishDateString").toString().equals(""))
			try {
				planFinishDate = sdf.parse(map.get("planFinishDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (map.get("factFinishDateString") != null && !map.get("factFinishDateString").toString().equals(""))
			try {
				factFinishDate = sdf.parse(map.get("factFinishDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (map.get("entryDateString") != null && !map.get("entryDateString").toString().equals(""))
			try {
				entryDate = sdf.parse(map.get("entryDateString").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if (map.get("finishThing") != null)
			finishThing = map.get("finishThing").toString();
		if(map.get("notFinishReason") != null)
			notFinishReason = map.get("notFinishReason").toString();
		if(map.get("hasProblem") != null)
			hasProblem = map.get("hasProblem").toString();
//		if(map.get("approveBy") != null && !map.get("approveBy").toString().equals(""))
//			approveBy = map.get("approveBy").toString();
//		if(map.get("checkBy") != null && !map.get("checkBy").toString().equals(""))
//			checkBy = map.get("checkBy").toString();
		if(map.get("entryBy") != null && !map.get("entryBy").toString().equals(""))
			entryBy = map.get("entryBy").toString();
		
		temp.setChekupId(chekupId);		
		temp.setStrMonth(strMonth);
		temp.setProtectEqu(protectEqu);
		temp.setProtectDevice(protectDevice);
		temp.setLastCheckDate(lastCheckDate);
		temp.setPlanFinishDate(planFinishDate);
		temp.setFactFinishDate(factFinishDate);
		temp.setFinishThing(finishThing);
		temp.setNotFinishReason(notFinishReason);
		temp.setHasProblem(hasProblem);
		temp.setApproveBy(approveBy);
		temp.setCheckBy(checkBy);
		temp.setEntryBy(entryBy);
		temp.setEntryDate(entryDate);
		temp.setEnterpriseCode(enterpriseCode);
		
		return temp;
	}
}