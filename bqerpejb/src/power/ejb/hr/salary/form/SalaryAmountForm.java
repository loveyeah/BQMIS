package power.ejb.hr.salary.form;

import java.util.List;


import power.ejb.hr.salary.HrJSalary;
import power.ejb.hr.salary.HrJSalaryDetail;


public class SalaryAmountForm implements java.io.Serializable
{
	private static final long serialVersionUID = 126942669856252L;
	// 主表
	private HrJSalary salary;
	
	// 明细列表
	private List<HrJSalaryDetail> detailList;

	public HrJSalary getSalary() {
		return salary;
	}

	public void setSalary(HrJSalary salary) {
		this.salary = salary;
	}

	public List<HrJSalaryDetail> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<HrJSalaryDetail> detailList) {
		this.detailList = detailList;
	}
}