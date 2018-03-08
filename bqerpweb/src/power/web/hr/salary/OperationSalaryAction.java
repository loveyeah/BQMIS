package power.web.hr.salary;

import com.googlecode.jsonplugin.JSONException;
import com.googlecode.jsonplugin.JSONUtil;

import power.ear.comm.ejb.PageObject;
import power.ejb.hr.salary.HrCOperationSalary;
import power.ejb.hr.salary.HrCOperationSalaryFacadeRemote;
import power.web.comm.AbstractAction;

@SuppressWarnings("serial")
public class OperationSalaryAction extends AbstractAction {
	private HrCOperationSalaryFacadeRemote remote;
	private HrCOperationSalary osalary;
	private int start;
	private int limit;

	public OperationSalaryAction() {
		remote = (HrCOperationSalaryFacadeRemote) factory
				.getFacadeRemote("HrCOperationSalaryFacade");
	}

	public void saveOperationSalary() {
		osalary.setEnterpriseCode(employee.getEnterpriseCode());
		remote.save(osalary);
	}

	public void updateOperationSalary() {
		HrCOperationSalary entity = remote.findById(osalary
				.getOperationSalaryId());
		osalary.setIsUse(entity.getIsUse());
		osalary.setEnterpriseCode(entity.getEnterpriseCode());
		remote.update(osalary);
	}

	public void delOperationSalary() {
		String ids = request.getParameter("ids");
		remote.delete(ids);
	}

	public void getOperationSalaryList() throws JSONException {
		PageObject object = remote.findAll(employee.getEnterpriseCode(), start,
				limit);
		write(JSONUtil.serialize(object));
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

	public HrCOperationSalary getOsalary() {
		return osalary;
	}

	public void setOsalary(HrCOperationSalary osalary) {
		this.osalary = osalary;
	}
}
