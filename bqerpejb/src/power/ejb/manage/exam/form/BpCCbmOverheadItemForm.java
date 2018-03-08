package power.ejb.manage.exam.form;

import power.ejb.manage.exam.BpCCbmOverheadItem;

public class BpCCbmOverheadItemForm {
	private BpCCbmOverheadItem ohinfo;
	private String itemname;
	private String unitname;

	public BpCCbmOverheadItem getOhinfo() {
		return ohinfo;
	}

	public void setOhinfo(BpCCbmOverheadItem ohinfo) {
		this.ohinfo = ohinfo;
	}

	public String getItemname() {
		return itemname;
	}

	public void setItemname(String itemname) {
		this.itemname = itemname;
	}

	public String getUnitname() {
		return unitname;
	}

	public void setUnitname(String unitname) {
		this.unitname = unitname;
	}
}
