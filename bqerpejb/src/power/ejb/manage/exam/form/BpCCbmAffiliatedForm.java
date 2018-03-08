package power.ejb.manage.exam.form;

import power.ejb.manage.exam.BpCCbmAffiliated;

@SuppressWarnings("serial")
public class BpCCbmAffiliatedForm implements java.io.Serializable {

	private BpCCbmAffiliated afInfo;
	private String itemname;
	private String deptname;
	//add by drdu 091201
	private String ifBranchItem;

	public BpCCbmAffiliated getAfInfo() {
		return afInfo;
	}

	public void setAfInfo(BpCCbmAffiliated afInfo) {
		this.afInfo = afInfo;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getDeptname() {
		return deptname;
	}

	public void setDeptname(String deptname) {
		this.deptname = deptname;
	}

	public String getIfBranchItem() {
		return ifBranchItem;
	}

	public void setIfBranchItem(String ifBranchItem) {
		this.ifBranchItem = ifBranchItem;
	}
}