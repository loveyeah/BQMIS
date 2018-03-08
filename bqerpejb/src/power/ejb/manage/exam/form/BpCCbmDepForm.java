package power.ejb.manage.exam.form;

import power.ejb.manage.exam.BpCCbmDep;

public class BpCCbmDepForm {
	private BpCCbmDep cdInfo;
	private String deptname;

	public BpCCbmDep getCdInfo() {
		return cdInfo;
	}

	public void setCdInfo(BpCCbmDep cdInfo) {
		this.cdInfo = cdInfo;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

}
