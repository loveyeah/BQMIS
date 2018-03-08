package power.ejb.manage.contract.form;

public class BpAppHeaderForm implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7126354015756708644L;
	private String appId;
	private String operateDepDode;
	private String operateDepName;
	private String workflowNO;
	private String workflowStatus;
	private String balaFlag;
	private String applicatDate;
	private String entryBy;
	private String entryDate;
	private String moneyUnit;
	private String entryByName;
	private String wzFlag;

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getOperateDepDode() {
		return operateDepDode;
	}

	public void setOperateDepDode(String operateDepDode) {
		this.operateDepDode = operateDepDode;
	}

	public String getWorkflowNO() {
		return workflowNO;
	}

	public void setWorkflowNO(String workflowNO) {
		this.workflowNO = workflowNO;
	}

	public String getWorkflowStatus() {
		return workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	public String getBalaFlag() {
		return balaFlag;
	}

	public void setBalaFlag(String balaFlag) {
		this.balaFlag = balaFlag;
	}

	public String getApplicatDate() {
		return applicatDate;
	}

	public void setApplicatDate(String applicatDate) {
		this.applicatDate = applicatDate;
	}

	public String getEntryByName() {
		return entryByName;
	}

	public void setEntryByName(String entryByName) {
		this.entryByName = entryByName;
	}

	public String getEntryDate() {
		return entryDate;
	}

	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}

	public String getMoneyUnit() {
		return moneyUnit;
	}

	public void setMoneyUnit(String moneyUnit) {
		this.moneyUnit = moneyUnit;
	}

	public String getEntryBy() {
		return entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	public String getOperateDepName() {
		return operateDepName;
	}

	public void setOperateDepName(String operateDepName) {
		this.operateDepName = operateDepName;
	}

	public String getWzFlag() {
		return wzFlag;
	}

	public void setWzFlag(String wzFlag) {
		this.wzFlag = wzFlag;
	}

}
