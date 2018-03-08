package power.ejb.manage.stat;

public class BpCAnalyseAccountItemAdd implements java.io.Serializable {
	private BpCAnalyseAccountItem baseInfo;
	private String itemName;

	public BpCAnalyseAccountItem getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(BpCAnalyseAccountItem baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
