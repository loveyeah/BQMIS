package power.ejb.manage.stat.form;

import power.ejb.manage.stat.BpCStatReportItem;

@SuppressWarnings("serial")
public class StatReportItemForm implements java.io.Serializable {

	private BpCStatReportItem model;
	private String itemName;

	public BpCStatReportItem getModel() {
		return model;
	}

	public void setModel(BpCStatReportItem model) {
		this.model = model;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}
