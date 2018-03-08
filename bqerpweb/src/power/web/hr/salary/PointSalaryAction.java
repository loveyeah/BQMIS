package power.web.hr.salary;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCPointSalary;
import power.ejb.hr.salary.HrCPointSalaryFacadeRemote;
import power.web.comm.AbstractAction;

public class PointSalaryAction extends AbstractAction
{
	private HrCPointSalaryFacadeRemote remote;
	public PointSalaryAction()
	{
		remote = (HrCPointSalaryFacadeRemote)factory.getFacadeRemote("HrCPointSalaryFacade");
	}
	
	public void fingAllPointSalary() throws JSONException
	{
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		String checkGradeStr = request.getParameter("checkGrade");
		Long checkGrade = null;
		if(checkGradeStr != null && !checkGradeStr.equals(""))
			checkGrade = Long.parseLong(checkGradeStr);
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findAll(checkGrade,employee.getEnterpriseCode(),start,limit);
		} else {
			object = remote.findAll(checkGrade,employee.getEnterpriseCode());
		}
		
		String strOutput = "";
		strOutput = JSONUtil.serialize(object);
		write(strOutput);
	}
	
	public void savePointSalaryRec() throws JSONException
	{
		String addStr = request.getParameter("add");
		String updateStr = request.getParameter("update");
		Object addObj = JSONUtil.deserialize(addStr);
		Object updateObj = JSONUtil.deserialize(updateStr);
		List<Map> alist = (List<Map>)addObj;
		List<Map> ulist = (List<Map>)updateObj;
		
		List<HrCPointSalary> addList = new ArrayList<HrCPointSalary>();
		List<HrCPointSalary> updateList = new ArrayList<HrCPointSalary>();
		
		for(Map temp : alist)
		{
			HrCPointSalary flag = this.parseToPointSalary(temp);
			addList.add(flag);
		}
		for(Map temp : ulist)
		{
			HrCPointSalary flag = this.parseToPointSalary(temp);
			updateList.add(flag);
		}
		
		if(addList.size() > 0 || updateList.size() > 0)
		{
			remote.savePointSalaryRec(addList, updateList);
		}
	}
	
	public HrCPointSalary parseToPointSalary(Map map)
	{
		HrCPointSalary ps = new HrCPointSalary();
		Long pointSalaryId = null;
		Long checkStationGrade = null;
		Long salaryLevel = null;
		Double jobPoint = null;
		String memo = null;
		String isUse = "Y";
		String enterpriseCode = employee.getEnterpriseCode();
		
		if(map.get("pointSalaryId") != null)
			pointSalaryId = Long.parseLong(map.get("pointSalaryId").toString());
		if(map.get("checkStationGrade") != null)
			checkStationGrade = Long.parseLong(map.get("checkStationGrade").toString());
		if(map.get("salaryLevel") != null)
			salaryLevel = Long.parseLong(map.get("salaryLevel").toString());
		if(map.get("jobPoint") != null)
			jobPoint = Double.parseDouble(map.get("jobPoint").toString());
		if(map.get("memo") != null)
			memo = map.get("memo").toString();
		
		ps.setPointSalaryId(pointSalaryId);
		ps.setCheckStationGrade(checkStationGrade);
		ps.setSalaryLevel(salaryLevel);
		ps.setJobPoint(jobPoint);
		ps.setMemo(memo);
		ps.setIsUse(isUse);
		ps.setEnterpriseCode(enterpriseCode);
		
		return ps;
	}
}