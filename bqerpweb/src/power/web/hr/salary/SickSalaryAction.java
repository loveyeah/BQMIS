package power.web.hr.salary;

import java.text.ParseException;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCSickSalary;
import power.ejb.hr.salary.HrCSickSalaryFacadeRemote;
import power.web.comm.AbstractAction;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

@SuppressWarnings("serial")
public class SickSalaryAction extends AbstractAction {

	private HrCSickSalaryFacadeRemote remote;
	private HrCSickSalary sickSalary;

	public SickSalaryAction() {
		remote = (HrCSickSalaryFacadeRemote) factory
				.getFacadeRemote("HrCSickSalaryFacade");
	}

	/**
	 * 增加一条月出勤天数记录
	 * 
	 * @throws ParseException
	 */
	public void addSickSalary() throws ParseException {
		sickSalary.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(sickSalary);
		write("{success:true,msg:'增加成功！'}");
	}

	/**
	 * 修改月出勤天数记录
	 * 
	 * @throws ParseException
	 */
	public void updateSickSalary() throws ParseException {
		HrCSickSalary model = remote.findById(sickSalary
				.getSickSalaryId());
		model.setFactWorkyearBottom(sickSalary.getFactWorkyearBottom());
		model.setFactWorkyearTop(sickSalary.getFactWorkyearTop());
		model.setLocalWorkageBottom(sickSalary.getLocalWorkageBottom());
		model.setLocalWorkageTop(sickSalary.getLocalWorkageTop());
		model.setSalaryScale(sickSalary.getSalaryScale());
		model.setMemo(sickSalary.getMemo());

		remote.update(model);
		write("{success:true,msg:'修改成功！'}");
	}

	/**
	 * 查询所有月出勤天数记录
	 * 
	 * @throws JSONException
	 */
	public void findSickSalaryList() throws JSONException {
		Object objstart = request.getParameter("start");
		Object objlimit = request.getParameter("limit");
		PageObject object = null;
		if (objstart != null && objlimit != null) {
			int start = Integer.parseInt(request.getParameter("start"));
			int limit = Integer.parseInt(request.getParameter("limit"));
			object = remote.findAll(employee
					.getEnterpriseCode(), start, limit);
		} else {
			object = remote.findAll(employee
					.getEnterpriseCode());
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
	 * 删除一条或多条月出勤天数记录
	 */
	public void deleteSickSalary() {
		String ids = request.getParameter("ids");
		remote.delete(ids);
		write("{success:true,msg:'删除成功！'}");
	}

	public HrCSickSalary getSickSalary() {
		return sickSalary;
	}

	public void setSickSalary(HrCSickSalary sickSalary) {
		this.sickSalary = sickSalary;
	}


}
