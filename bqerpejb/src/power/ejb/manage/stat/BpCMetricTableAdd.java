package power.ejb.manage.stat;

public class BpCMetricTableAdd implements java.io.Serializable {

	private BpCMetricTable baseInfo;
	private String itemCodeAdd;
	private String tableCodeAdd;

	public BpCMetricTable getBaseInfo() {
		return baseInfo;
	}

	public void setBaseInfo(BpCMetricTable baseInfo) {
		this.baseInfo = baseInfo;
	}

	public String getItemCodeAdd() {
		return itemCodeAdd;
	}

	public void setItemCodeAdd(String itemCodeAdd) {
		this.itemCodeAdd = itemCodeAdd;
	}

	public String getTableCodeAdd() {
		return tableCodeAdd;
	}

	public void setTableCodeAdd(String tableCodeAdd) {
		this.tableCodeAdd = tableCodeAdd;
	}

}
