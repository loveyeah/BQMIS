package power.web.hr.ca.attendance.attendancerights.action;
import power.web.comm.AbstractAction;
import power.ejb.hr.ca.HrJLeftDay;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.googlecode.jsonplugin.JSONException;
import power.ear.comm.ejb.PageObject;
import power.ejb.hr.ca.HrJLeftDayFacadeRemote;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class LeftDayManageAction extends AbstractAction {
	private HrJLeftDayFacadeRemote remote;
	/**
	 * 查询部门下面人员的换休时间
	 */
	public void queryLeftDay () throws JSONException{
		String deptId = request.getParameter("deptId");
		String objStart = request.getParameter("start");
		String objLimit = request.getParameter("limit");
		PageObject object = new PageObject();
		if(objStart != null && objLimit != null){
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findLeftDayList(deptId, start,limit);
		}else{}
		String strOutput = "";
		if(object == null || object.getList() == null){
			strOutput = "{\"list\":[],\"totalCount\":0}";
		}else{
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}
}
