package power.web.hr.salary;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCSalaryPoint;
import power.ejb.hr.salary.HrCSalaryPointFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SalaryPointAction extends AbstractAction {
	private HrCSalaryPointFacadeRemote remote;
	private HrCSalaryPoint spoint;
	private int start;
	private int limit;

	public SalaryPointAction() {
		remote = (HrCSalaryPointFacadeRemote) factory
				.getFacadeRemote("HrCSalaryPointFacade");
	}

	public void saveSalaryPoint() throws ParseException {
		String startTime=request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM"); 
		spoint.setEffectStartTime(df.parse(startTime));
		spoint.setEffectEndTime(df.parse(endTime));
		spoint.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(spoint);
	}

	public void updateSalaryPoint() throws ParseException {
		String startTime=request.getParameter("startTime");
		String endTime = request.getParameter("endTime");
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM"); 
		spoint.setEffectStartTime(df.parse(startTime));
		spoint.setEffectEndTime(df.parse(endTime));
		HrCSalaryPoint entity = remote.findById(spoint.getSalaryPointId());
		spoint.setIsUse(entity.getIsUse());
		spoint.setEnterpriseCode(entity.getEnterpriseCode());
		remote.update(spoint);
	}

	public void getSalaryPointList() throws JSONException {
		String sDate = request.getParameter("sDate");
		PageObject object = remote.findAll(sDate, employee.getEnterpriseCode(), start,
				limit);
//		System.out.println(JSONUtil.serialize(object));
		write(JSONUtil.serialize(object));
	}
	
	/**
	 * 删除一条或多条薪点点值记录
	 * 
	 * add by drdu 090929
	 */
	public void deleteSalaryPoint()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public HrCSalaryPoint getSpoint() {
		return spoint;
	}

	public void setSpoint(HrCSalaryPoint spoint) {
		this.spoint = spoint;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}
}
