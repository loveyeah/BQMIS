package power.ejb.manage.stat.form;

import power.ejb.manage.stat.BpCItemReportNew;

@SuppressWarnings("serial")
public class ItemReprotNewForm implements java.io.Serializable {
	private BpCItemReportNew model;
	private String itemName;

	public BpCItemReportNew getModel() {
		return model;
	}

	public void setModel(BpCItemReportNew model) {
		this.model = model;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
}
