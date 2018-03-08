package power.ejb.resource.form;

public class MRPIssueMaterialDetailInfo {
	
	/** 领料单号 */
	private String issueNo="";
	/** 申请数量 */
	private String appliedCount="";
	/** 核准数量 */
	private String approvedCount="";
	/**　实际发货数量　*/
	private String actIssuedCount="";

	public String getIssueNo() {
		return issueNo;
	}

	public void setIssueNo(String issueNo) {
		this.issueNo = issueNo;
	}

	public String getAppliedCount() {
		return appliedCount;
	}

	public void setAppliedCount(String appliedCount) {
		this.appliedCount = appliedCount;
	}

	public String getApprovedCount() {
		return approvedCount;
	}

	public void setApprovedCount(String approvedCount) {
		this.approvedCount = approvedCount;
	}

	public String getActIssuedCount() {
		return actIssuedCount;
	}

	public void setActIssuedCount(String actIssuedCount) {
		this.actIssuedCount = actIssuedCount;
	}

}
