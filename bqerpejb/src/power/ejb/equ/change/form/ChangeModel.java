package power.ejb.equ.change.form;

import java.util.Date;

import power.ejb.equ.change.EquJEquchang;

public class ChangeModel {

	private EquJEquchang change;
	private String specialityName;
	private String sourceName;
	private String changePlanDate;
	private String applyDate;
	private String deptName;
	public EquJEquchang getChange() {
		return change;
	}
	public void setChange(EquJEquchang change) {
		this.change = change;
	}
	public String getSpecialityName() {
		return specialityName;
	}
	public void setSpecialityName(String specialityName) {
		this.specialityName = specialityName;
	}
	public String getSourceName() {
		return sourceName;
	}
	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	public String getChangePlanDate() {
		return changePlanDate;
	}
	public void setChangePlanDate(String changePlanDate) {
		this.changePlanDate = changePlanDate;
	}
	public String getApplyDate() {
		return applyDate;
	}
	public void setApplyDate(String applyDate) {
		this.applyDate = applyDate;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	
}
