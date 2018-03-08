package power.web.hr.salary;

import java.text.ParseException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCOvertimeSalaryBase;
import power.ejb.hr.salary.HrCOvertimeSalaryBaseFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class OvertimeSalaryBaseAction extends AbstractAction{

	private HrCOvertimeSalaryBaseFacadeRemote remote;
	private HrCOvertimeSalaryBase base;
	
	public OvertimeSalaryBaseAction()
	{
		remote = (HrCOvertimeSalaryBaseFacadeRemote)factory.getFacadeRemote("HrCOvertimeSalaryBaseFacade");
	}
	
	/**
	 * 增加一条加班工资基数记录
	 * @throws ParseException
	 */
	public void addOvertimeSalaryBase() throws ParseException
	{
		String effectStartTime = request.getParameter("effectStartTime");
		String effectEndTime = request.getParameter("effectEndTime");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
		base.setEffectStartTime(formatter.parse(effectStartTime));
		base.setEffectEndTime(formatter.parse(effectEndTime));
		base.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(base);
		write("{success:true,msg:'增加成功！'}");
	}
	
	/**
	 * 修改一条加班工资基数记录
	 * @throws ParseException
	 */
	public void updateOvertimeSalaryBase() throws ParseException
	{
		String effectStartTime = request.getParameter("effectStartTime");
		String effectEndTime = request.getParameter("effectEndTime");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
		
		HrCOvertimeSalaryBase model = remote.findById(base.getOvertimeSalaryBaseId());
		model.setOvertimeSalaryBase(base.getOvertimeSalaryBase());
		model.setEffectStartTime(formatter.parse(effectStartTime));
		model.setEffectEndTime(formatter.parse(effectEndTime));
		model.setMemo(base.getMemo());
		
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	
	/**
	 * 删除一条或多条加班工资基数记录
	 */
	public void deleteOvertimeSalaryBase()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	/**
	 * 根据条件查找加班工资基数记录列表
	 * @throws JSONException
	 */
	public void findOverTimeSalaryBaseList() throws JSONException
	{
		String sDate = request.getParameter("sDate");
		
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findOverTimeSalaryBaseList(sDate, employee.getEnterpriseCode(), start,limit);
		} else {
			object = remote.findOverTimeSalaryBaseList(sDate, employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	public HrCOvertimeSalaryBase getBase() {
		return base;
	}

	public void setBase(HrCOvertimeSalaryBase base) {
		this.base = base;
	}
	
}
