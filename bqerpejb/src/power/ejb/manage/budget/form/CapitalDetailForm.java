package power.ejb.manage.budget.form;

public class CapitalDetailForm implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2416545341213L;
	private Long capitalId;
	private String budgetTime;
	private Long workFlowNo;
	private String workFlowStatus;

	private Long capitalDetailId;
	private String project;
	private Double materialCost;
	private Double workingCost;
	private Double otherCost;
	private Double deviceCost;
	private Double totalCost;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	public Long getCapitalId() {
		return capitalId;
	}

	public void setCapitalId(Long capitalId) {
		this.capitalId = capitalId;
	}

	public String getBudgetTime() {
		return budgetTime;
	}

	public void setBudgetTime(String budgetTime) {
		this.budgetTime = budgetTime;
	}

	public Long getWorkFlowNo() {
		return workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	public String getWorkFlowStatus() {
		return workFlowStatus;
	}

	public void setWorkFlowStatus(String workFlowStatus) {
		this.workFlowStatus = workFlowStatus;
	}

	public Long getCapitalDetailId() {
		return capitalDetailId;
	}

	public void setCapitalDetailId(Long capitalDetailId) {
		this.capitalDetailId = capitalDetailId;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public Double getMaterialCost() {
		return materialCost;
	}

	public void setMaterialCost(Double materialCost) {
		this.materialCost = materialCost;
	}

	public Double getWorkingCost() {
		return workingCost;
	}

	public void setWorkingCost(Double workingCost) {
		this.workingCost = workingCost;
	}

	public Double getOtherCost() {
		return otherCost;
	}

	public void setOtherCost(Double otherCost) {
		this.otherCost = otherCost;
	}

	public Double getDeviceCost() {
		return deviceCost;
	}

	public void setDeviceCost(Double deviceCost) {
		this.deviceCost = deviceCost;
	}

	public Double getTotalCost() {
		return totalCost;
	}

	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getIsUse() {
		return isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
}