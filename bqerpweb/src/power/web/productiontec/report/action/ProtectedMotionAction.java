package power.web.productiontec.report.action;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.Employee;
import power.ear.comm.ejb.PageObject;
import power.ejb.basedata.BaseDataManager;
import power.ejb.productiontec.report.PtJJdyb;
import power.ejb.productiontec.report.PtJJdybDetail;
import power.ejb.productiontec.report.PtJJdybDetailFacadeRemote;
import power.ejb.productiontec.report.PtJJdybFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class ProtectedMotionAction extends AbstractAction
{
	private PtJJdybFacadeRemote remote;
	private PtJJdybDetailFacadeRemote detailRemote;
	private BaseDataManager baseRemote;
	public ProtectedMotionAction()
	{
		remote = (PtJJdybFacadeRemote)factory.getFacadeRemote("PtJJdybFacade");
		detailRemote = (PtJJdybDetailFacadeRemote)factory.getFacadeRemote("PtJJdybDetailFacade");
		baseRemote = (BaseDataManager)factory.getFacadeRemote("BaseDataManagerImpl");
	}
	
	
	
	public void findAllDetails() throws JSONException
	{
		String jdybId = request.getParameter("jdybId");
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		List<PtJJdybDetail> list = null;
		PageObject pg = new PageObject();
		if(start == null || limit == null)
		{
			if(jdybId == null)
				list = detailRemote.findByProperty("jdybId", null);
			else
				list = detailRemote.findByProperty("jdybId", Long.parseLong(jdybId));
		}
				
		else 
		{
			if(jdybId == null)
				list = detailRemote.findByProperty("jdybId", null, Integer.parseInt(start),Integer.parseInt(limit));
			else
				list = detailRemote.findByProperty("jdybId", Long.parseLong(jdybId), Integer.parseInt(start),Integer.parseInt(limit));
		}
			
		pg.setList(list);
		pg.setTotalCount((long)list.size());
		write(JSONUtil.serialize(pg));
	}
	
	public void getMainRecord() throws JSONException
	{
		String month = request.getParameter("month");
		List<PtJJdyb> list = remote.findByProperty("month", month);
		if (list != null && list.size() > 0) {
			PtJJdyb flag = list.get(0);
			flag.setMonth(month);
			flag.setEntryBy(employee.getWorkerCode());
			String outStr = JSONUtil.serialize(flag);
			String manager = "";
			String protector = "";
			String approveName = "";
			String entryName = "";
			if(flag.getManagerBy() != null)
			{
				Employee emp = baseRemote.getEmployeeInfo(flag.getManagerBy());
				manager = emp.getWorkerName();
			}
			if(flag.getProtectedBy() != null)
			{
				Employee emp = baseRemote.getEmployeeInfo(flag.getProtectedBy());
				protector = emp.getWorkerName();
			}
			if(flag.getApproveBy() != null)
			{
				Employee emp = baseRemote.getEmployeeInfo(flag.getApproveBy());
				approveName = emp.getWorkerName();
			}
			if(flag.getEntryBy() != null)
			{
				Employee emp = baseRemote.getEmployeeInfo(flag.getEntryBy());
				entryName = emp.getWorkerName();
			}
			write(outStr.substring(0,outStr.length() - 1) + ",manager:'" + manager + "',protector:'" + protector+ "',approveName:'" + approveName + "',entryName:'" + entryName + "'}");
		}
		else 
		{
			PtJJdyb temp = new PtJJdyb();
			temp.setMonth(month);
			temp.setEntryBy(employee.getWorkerCode());
			temp.setTfMotionSum(0L);
			temp.setTfMotionRight(0L);
			temp.setOoMotionSum(0L);
			temp.setOoMotionRight(0L);
			temp.setTtMotionSum(0L);
			temp.setTtMotionRight(0L);
			temp.setOochzMotionSum(0L);
			temp.setOochzMotionRight(0L);
			temp.setTtchzMotionSum(0L);
			temp.setTtchzMotionRight(0L);
			temp.setErrorMotionSum(0L);
			temp.setErrorMotionRight(0L);
			String outStr = JSONUtil.serialize(temp);
			String manager = "";
			String protector = "";
			String approveName = "";
			String entryName = "";
			if(temp.getManagerBy() != null)
			{
				Employee emp = baseRemote.getEmployeeInfo(temp.getManagerBy());
				manager = emp.getWorkerName();
			}
			if(temp.getProtectedBy() != null)
			{
				Employee emp = baseRemote.getEmployeeInfo(temp.getProtectedBy());
				protector = emp.getWorkerName();
			}
			if(temp.getApproveBy() != null)
			{
				Employee emp = baseRemote.getEmployeeInfo(temp.getApproveBy());
				approveName = emp.getWorkerName();
			}
			if(temp.getEntryBy() != null)
			{
				Employee emp = baseRemote.getEmployeeInfo(temp.getEntryBy());
				entryName = emp.getWorkerName();
			}
			write(outStr.substring(0,outStr.length() - 1) + ",manager:'" + manager + "',protector:'" + protector+ "',approveName:'" + approveName + "',entryName:'" + entryName + "'}");
		}
			
	}
	
	public void getSeasonRecord() throws JSONException
	{
		String year = request.getParameter("year");
		String seaStr = request.getParameter("sea");
		
		Integer sea = Integer.parseInt(seaStr);
		int from = sea * 3 - 2;
		int to = sea * 3;
		String fromMonth = year + "-" + (from > 9 ? String.valueOf(from) : "0" + String.valueOf(from));
		String toMonth = year + "-" + (to > 9 ? String.valueOf(to) : "0" + String.valueOf(to));
		
		List<PtJJdyb> list = remote.findSeasonRec(fromMonth,toMonth);
		PtJJdyb temp = new PtJJdyb();
		Long tfMotionSum = 0L;
		Long tfMotionRight = 0L;
		Long ooMotionSum = 0L;
		Long ooMotionRight = 0L;
		Long ttMotionSum = 0L;
		Long ttMotionRight = 0L;
		Long oochzMotionSum = 0L;
		Long oochzMotionRight = 0L;
		Long ttchzMotionSum = 0L;
		Long ttchzMotionRight = 0L;
		Long errorMotionSum = 0L;
		Long errorMotionRight = 0L;
		
		for(PtJJdyb pt : list)
		{
			if(pt.getTfMotionSum() != null)
				tfMotionSum += pt.getTfMotionSum();
			if(pt.getTfMotionRight() != null)
				tfMotionRight += pt.getTfMotionRight();
			if(pt.getOoMotionSum() != null)
				ooMotionSum += pt.getOoMotionSum();
			if(pt.getOoMotionRight() != null)
				ooMotionRight += pt.getOoMotionRight();
			if(pt.getTtMotionSum() != null)
				ttMotionSum += pt.getTtMotionSum();
			if(pt.getTtMotionRight() != null)
				ttMotionRight += pt.getTtMotionRight();
			if(pt.getOochzMotionSum() != null)
				oochzMotionSum += pt.getOochzMotionSum();
			if(pt.getOochzMotionRight() != null)
				oochzMotionRight += pt.getOochzMotionRight();
			if(pt.getTtchzMotionSum() != null)
				ttchzMotionSum += pt.getTtchzMotionSum();
			if(pt.getTtchzMotionRight() != null)
				ttchzMotionRight += pt.getTtchzMotionRight();
			if(pt.getErrorMotionSum() != null)
				errorMotionSum += pt.getErrorMotionSum();
			if(pt.getErrorMotionRight() != null)
				errorMotionRight += pt.getErrorMotionRight();
		}
		temp.setTfMotionSum(tfMotionSum);
		temp.setTfMotionRight(tfMotionRight);
		temp.setOoMotionSum(ooMotionSum);
		temp.setOoMotionRight(ooMotionRight);
		temp.setTtMotionSum(ttMotionSum);
		temp.setTtMotionRight(ttMotionRight);
		temp.setOochzMotionSum(oochzMotionSum);
		temp.setOochzMotionRight(oochzMotionRight);
		temp.setTtchzMotionSum(ttchzMotionSum);
		temp.setTtchzMotionRight(ttchzMotionRight);
		temp.setErrorMotionSum(errorMotionSum);
		temp.setErrorMotionRight(errorMotionRight);
		
		write(JSONUtil.serialize(temp));
	}
	
	public void getYearRecord() throws JSONException
	{
		String year = request.getParameter("year");
		String fromMonth = year + "-01";
		String toMonth = year + "-12";
		
		List<PtJJdyb> list = remote.findSeasonRec(fromMonth,toMonth);
		PtJJdyb temp = new PtJJdyb();
		Long tfMotionSum = 0L;
		Long tfMotionRight = 0L;
		Long ooMotionSum = 0L;
		Long ooMotionRight = 0L;
		Long ttMotionSum = 0L;
		Long ttMotionRight = 0L;
		Long oochzMotionSum = 0L;
		Long oochzMotionRight = 0L;
		Long ttchzMotionSum = 0L;
		Long ttchzMotionRight = 0L;
		Long errorMotionSum = 0L;
		Long errorMotionRight = 0L;
		
		for(PtJJdyb pt : list)
		{
			if(pt.getTfMotionSum() != null)
				tfMotionSum += pt.getTfMotionSum();
			if(pt.getTfMotionRight() != null)
				tfMotionRight += pt.getTfMotionRight();
			if(pt.getOoMotionSum() != null)
				ooMotionSum += pt.getOoMotionSum();
			if(pt.getOoMotionRight() != null)
				ooMotionRight += pt.getOoMotionRight();
			if(pt.getTtMotionSum() != null)
				ttMotionSum += pt.getTtMotionSum();
			if(pt.getTtMotionRight() != null)
				ttMotionRight += pt.getTtMotionRight();
			if(pt.getOochzMotionSum() != null)
				oochzMotionSum += pt.getOochzMotionSum();
			if(pt.getOochzMotionRight() != null)
				oochzMotionRight += pt.getOochzMotionRight();
			if(pt.getTtchzMotionSum() != null)
				ttchzMotionSum += pt.getTtchzMotionSum();
			if(pt.getTtchzMotionRight() != null)
				ttchzMotionRight += pt.getTtchzMotionRight();
			if(pt.getErrorMotionSum() != null)
				errorMotionSum += pt.getErrorMotionSum();
			if(pt.getErrorMotionRight() != null)
				errorMotionRight += pt.getErrorMotionRight();
		}
		temp.setTfMotionSum(tfMotionSum);
		temp.setTfMotionRight(tfMotionRight);
		temp.setOoMotionSum(ooMotionSum);
		temp.setOoMotionRight(ooMotionRight);
		temp.setTtMotionSum(ttMotionSum);
		temp.setTtMotionRight(ttMotionRight);
		temp.setOochzMotionSum(oochzMotionSum);
		temp.setOochzMotionRight(oochzMotionRight);
		temp.setTtchzMotionSum(ttchzMotionSum);
		temp.setTtchzMotionRight(ttchzMotionRight);
		temp.setErrorMotionSum(errorMotionSum);
		temp.setErrorMotionRight(errorMotionRight);
		
		write(JSONUtil.serialize(temp));
	}
	public void saveModDetails() throws JSONException
	{
		String jdybId = request.getParameter("jdybId");
		String month = request.getParameter("month");
		String tfMotionSum = request.getParameter("tfMotionSum");
		String tfMotionRight = request.getParameter("tfMotionRight");
		String ooMotionSum = request.getParameter("ooMotionSum");
		String ooMotionRight = request.getParameter("ooMotionRight");
		String ttMotionSum = request.getParameter("ttMotionSum");
		String ttMotionRight = request.getParameter("ttMotionRight");
		String oochzMotionSum = request.getParameter("oochzMotionSum");
		String oochzMotionRight = request.getParameter("oochzMotionRight");
		String ttchzMotionSum = request.getParameter("ttchzMotionSum");
		String ttchzMotionRight = request.getParameter("ttchzMotionRight");
		String errorMotionSum = request.getParameter("errorMotionSum");
		String errorMotionRight = request.getParameter("errorMotionRight");
		String managerBy = request.getParameter("managerBy");
		String protectedBy = request.getParameter("protectedBy");
		String approveBy = request.getParameter("approveBy");
		String entryBy = request.getParameter("entryBy");
		
		PtJJdyb jdyb = new PtJJdyb();
		if(jdybId != null && !jdybId.equals(""))
			jdyb.setJdybId(Long.parseLong(jdybId));
		if(month != null && !month.equals(""))
			jdyb.setMonth(month);
		if(tfMotionSum != null && !tfMotionSum.equals(""))
			jdyb.setTfMotionSum(Long.parseLong(tfMotionSum));
		if(tfMotionRight != null && !tfMotionRight.equals(""))
			jdyb.setTfMotionRight(Long.parseLong(tfMotionRight));
		if(ooMotionSum != null && !ooMotionSum.equals(""))
			jdyb.setOoMotionSum(Long.parseLong(ooMotionSum));
		if(ooMotionRight != null && !ooMotionRight.equals(""))
			jdyb.setOoMotionRight(Long.parseLong(ooMotionRight));
		if(ttMotionSum != null && !ttMotionSum.equals(""))
			jdyb.setTtMotionSum(Long.parseLong(ttMotionSum));
		if(ttMotionRight != null && !ttMotionRight.equals(""))
			jdyb.setTtMotionRight(Long.parseLong(ttMotionRight));
		if(oochzMotionSum != null && !oochzMotionSum.equals(""))
			jdyb.setOochzMotionSum(Long.parseLong(oochzMotionSum));
		if(oochzMotionRight != null && !oochzMotionRight.equals(""))
			jdyb.setOochzMotionRight(Long.parseLong(oochzMotionRight));
		if(ttchzMotionSum != null && !ttchzMotionSum.equals(""))
			jdyb.setTtchzMotionSum(Long.parseLong(ttchzMotionSum));
		if(ttchzMotionRight != null && !ttchzMotionRight.equals(""))
			jdyb.setTtchzMotionRight(Long.parseLong(ttchzMotionRight));
		if(errorMotionSum != null && !errorMotionSum.equals(""))
			jdyb.setErrorMotionSum(Long.parseLong(errorMotionSum));
		if(errorMotionRight != null && !errorMotionRight.equals(""))
			jdyb.setErrorMotionRight(Long.parseLong(errorMotionRight));
		if(managerBy != null &&!managerBy.equals(""))
			jdyb.setManagerBy(managerBy);
		if(protectedBy != null && !protectedBy.equals(""))
			jdyb.setProtectedBy(protectedBy);
		if(approveBy != null && !approveBy.equals(""))
			jdyb.setApproveBy(approveBy);
		if(entryBy != null && !entryBy.equals(""))
			jdyb.setEntryBy(entryBy);
		
		jdyb.setEnterpriseCode(employee.getEnterpriseCode());
		PtJJdyb result = new PtJJdyb();
		if(jdyb.getJdybId() == null)
		{
			 result = remote.save(jdyb);
		}
		else 
		{
			result = remote.update(jdyb);
		}
		
		String addStr = request.getParameter("add");
		String updateStr  = request.getParameter("update");
		String ids = request.getParameter("ids");
		
		Object addObj = JSONUtil.deserialize(addStr);
		Object updateObj = JSONUtil.deserialize(updateStr);
		
		List<Map> addMapList = (List<Map>)addObj;
		List<Map> updateMapList = (List<Map>)updateObj;
		
		List<PtJJdybDetail> addList = new ArrayList<PtJJdybDetail>();
		List<PtJJdybDetail> updateList = new ArrayList<PtJJdybDetail>();
		
		for(Map map : addMapList)
		{
			PtJJdybDetail temp = this.parsePtDetail(result,map);
			addList.add(temp);
		}
		
		for(Map map : updateMapList)
		{
			PtJJdybDetail temp = this.parsePtDetail(result,map);
			updateList.add(temp);
		}
		
		if(addList != null || updateList != null || ids != null)
		{
			if(addList.size() > 0 || updateList.size() > 0 || ids.length() > 0)
			{
				detailRemote.treatRecords(addList,updateList,ids);				
			}
		}
		write("{ success : true,msg : '数据保存修改成功！'}");
	}
	
	public PtJJdybDetail parsePtDetail(PtJJdyb result,Map map)
	{
		PtJJdybDetail temp = new PtJJdybDetail();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		
		Long jdybDetailId = null;
		Long jdybId = result.getJdybId();
		Date time = null;
		String address = null;
		String equSummary = null;
		String equName = null;
		String equType = null;
		Long rightNum = null;
		Long wrongNum = null;
		String errorReason = null;
		String memo = null;
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(map.get("jdybDetailId") != null)
			jdybDetailId = Long.parseLong(map.get("jdybDetailId").toString());
		if(map.get("time") != null)
			try {
				time = sdf.parse(map.get("time").toString());
			} catch (ParseException e) {
				e.printStackTrace();
			}
		if(map.get("address") != null)
			address = map.get("address").toString();
		if(map.get("equSummary") != null)
			equSummary = map.get("equSummary").toString();
		if(map.get("equName") != null)
			equName = map.get("equName").toString();
		if(map.get("equType") != null)
			equType = map.get("equType").toString();
		if(map.get("rightNum") != null)
			rightNum = Long.parseLong(map.get("rightNum").toString());
		if(map.get("wrongNum") != null)
			wrongNum = Long.parseLong(map.get("wrongNum").toString());
		if(map.get("errorReason") != null)
			errorReason = map.get("errorReason").toString();
		if(map.get("memo") != null)
			memo = map.get("memo").toString();
		
		temp.setJdybDetailId(jdybDetailId);
		temp.setJdybId(jdybId);
		temp.setTime(time);
		temp.setAddress(address);
		temp.setEquSummary(equSummary);
		temp.setEquName(equName);
		temp.setEquType(equType);
		temp.setRightNum(rightNum);
		temp.setWrongNum(wrongNum);
		temp.setErrorReason(errorReason);
		temp.setMemo(memo);
		temp.setEnterpriseCode(enterpriseCode);
		
		return temp;
	}
}