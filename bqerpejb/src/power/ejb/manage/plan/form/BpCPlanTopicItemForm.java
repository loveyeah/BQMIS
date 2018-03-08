package power.ejb.manage.plan.form;

import power.ejb.manage.plan.BpCPlanTopicItem;

public class BpCPlanTopicItemForm implements java.io.Serializable {
	private BpCPlanTopicItem baseInfo;
	private String itemName;

	public BpCPlanTopicItem getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(BpCPlanTopicItem baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
