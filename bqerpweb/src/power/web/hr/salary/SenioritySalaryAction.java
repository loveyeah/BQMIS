package power.web.hr.salary;

import java.text.ParseException;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCSenioritySalary;
import power.ejb.hr.salary.HrCSenioritySalaryFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class SenioritySalaryAction extends AbstractAction{

	private HrCSenioritySalaryFacadeRemote remote;
	private HrCSenioritySalary salary;
	
	
	public SenioritySalaryAction()
	{
		remote = (HrCSenioritySalaryFacadeRemote)factory.getFacadeRemote("HrCSenioritySalaryFacade");		
	}

	/**
	 * 增加一条工龄工资记录
	 * @throws ParseException
	 */
	public void addSenioritySalary() throws ParseException
	{
		String effectStartTime = request.getParameter("effectStartTime");
		String effectEndTime = request.getParameter("effectEndTime");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
		salary.setEffectStartTime(formatter.parse(effectStartTime));
		salary.setEffectEndTime(formatter.parse(effectEndTime));
		salary.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(salary);
		write("{success:true,msg:'增加成功！'}");
	}
	
	/**
	 * 修改工龄工资记录
	 * @throws ParseException
	 */
	public void updateSenioritySalary() throws ParseException
	{
		String effectStartTime = request.getParameter("effectStartTime");
		String effectEndTime = request.getParameter("effectEndTime");
		java.text.SimpleDateFormat formatter= new java.text.SimpleDateFormat("yyyy-MM");
		
		HrCSenioritySalary model = remote.findById(salary.getSenioritySalaryId());
		model.setSenioritySalary(salary.getSenioritySalary());
		model.setEffectStartTime(formatter.parse(effectStartTime));
		model.setEffectEndTime(formatter.parse(effectEndTime));
		model.setMemo(salary.getMemo());
		
		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}
	
	/**
	 * 查询所有工龄工资列表记录
	 * @throws JSONException
	 */
	public void findSenioritySalaryList() throws JSONException
	{
		String sDate = request.getParameter("sDate");
		
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findSenioritySalaryList(sDate, employee.getEnterpriseCode(),start,limit);
		} else {
			object = remote.findSenioritySalaryList(sDate, employee.getEnterpriseCode());
		}
		String strOutput = "";
		if (object == null || object.getList() == null) {
			strOutput = "{\"list\":[],\"totalCount\":0}";
		} else {
			strOutput = JSONUtil.serialize(object);
		}
		write(strOutput);
	}

	/**
	 * 删除一条或多条工龄工资记录
	 */
	public void deleteSenioritySalary()
	{
		String ids=request.getParameter("ids");
		remote.deleteMulti(ids);
		write("{success:true,msg:'删除成功！'}");
	}
	
	public HrCSenioritySalary getSalary() {
		return salary;
	}


	public void setSalary(HrCSenioritySalary salary) {
		this.salary = salary;
	}
}
