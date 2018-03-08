package power.web.hr.ca.attendance.yearabsence;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import power.ear.comm.DataChangeException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ca.HrJWorkattendancenewFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;
import com.opensymphony.workflow.service.WorkflowService;

@SuppressWarnings("serial")
public class YearAbsenceStatisticsAction extends AbstractAction {
	protected HrJWorkattendancenewFacadeRemote tRemote;

	


	
	/**
	 * 
	 * 构造方法
	 * 
	 */
	@SuppressWarnings("unchecked")
	public YearAbsenceStatisticsAction() {
		tRemote = (HrJWorkattendancenewFacadeRemote) factory
				.getFacadeRemote("HrJWorkattendancenewFacade");
	}
	
  //  增加年休查询方法  add by wpzhu 20100709
	public void queryYearRest() throws JSONException
	{
		PageObject result = new PageObject();
		int start;
		int limit;
		Long deptID=0L;
		Object sta = request.getParameter("start");
		Object lim = request.getParameter("limit");
		String strYear = request.getParameter("strYear");
		String deptId = request.getParameter("deptId");
		String chsName=request.getParameter("name");
		
		if(sta!=null&&lim!=null)
		{
			start=Integer.parseInt(sta.toString());
			limit=Integer.parseInt(lim.toString());
		
			result=tRemote.findYearRestList((deptId==null||deptId.equals("")||deptId.equals("undefined"))?null:Long.parseLong(deptId),
					strYear, employee.getEnterpriseCode(), chsName,start,limit);
		}else
		{
			result=tRemote.findYearRestList((deptId==null||deptId.equals("")||deptId.equals("undefined"))?null:Long.parseLong(deptId),
					strYear, employee.getEnterpriseCode(), chsName);
		}
		if (result.getTotalCount() > 0) {
			write(JSONUtil.serialize(result));
		} else {
			write("{totalCount : 0,list :[]}");
		}
	}
	//----------------------------

	public void getAattendanceListByYear() throws JSONException {
		
		String start = request.getParameter("start");
		String limit = request.getParameter("limit");
		String strYear = request.getParameter("strYear");
		String deptId = request.getParameter("deptId");

		PageObject pg = new PageObject();
		if (start != null && limit != null)
			pg = tRemote.findAattendanceListByYear((deptId==null||deptId.equals("")||deptId.equals("undefined"))?null:Long.parseLong(deptId),
					strYear,employee.getEnterpriseCode(),Integer.parseInt(start), Integer.parseInt(limit));
		else
			pg = tRemote.findAattendanceListByYear((deptId==null||deptId.equals("")||deptId.equals("undefined"))?null:Long.parseLong(deptId),
                    strYear,employee.getEnterpriseCode());

		if (pg.getTotalCount() > 0) {
			write(JSONUtil.serialize(pg));
		} else {
			write("{totalCount : 0,list :[]}");
		}
	}
}