package power.ejb.resource.form;

@SuppressWarnings("serial")
public class IssueInfoForReport implements java.io.Serializable{
	/**
	 * 领用部门
	 */
	private String issueDeptName;
	/**
	 * 本月领用
	 */
	private String receiptCount;
	/**
	 * 应分配差异
	 */
	private String assignDis;
	public String getIssueDeptName() {
		return issueDeptName;
	}
	public void setIssueDeptName(String issueDeptName) {
		this.issueDeptName = issueDeptName;
	}
	public String getReceiptCount() {
		return receiptCount;
	}
	public void setReceiptCount(String receiptCount) {
		this.receiptCount = receiptCount;
	}
	public String getAssignDis() {
		return assignDis;
	}
	public void setAssignDis(String assignDis) {
		this.assignDis = assignDis;
	}

}
