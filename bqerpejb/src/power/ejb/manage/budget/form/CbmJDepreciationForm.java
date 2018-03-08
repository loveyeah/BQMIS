package power.ejb.manage.budget.form;

@SuppressWarnings("serial")
public class CbmJDepreciationForm implements java.io.Serializable {

	private Long depreciationDetailId;
	private Long depreciationId;
	private String assetName;
	private Double lastAsset;
	private Double addAsset;
	private Double reduceAsset;
	private Double newAsset;
	private Double depreciationRate;
	private Double depreciationNumber;
	private Double depreciationSum;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	private String budgetTime;
	private Long workFlowNo;
	private String workFlowStatus;

	private Double newAssetCount;

	public Long getDepreciationDetailId() {
		return depreciationDetailId;
	}

	public void setDepreciationDetailId(Long depreciationDetailId) {
		this.depreciationDetailId = depreciationDetailId;
	}

	public Long getDepreciationId() {
		return depreciationId;
	}

	public void setDepreciationId(Long depreciationId) {
		this.depreciationId = depreciationId;
	}

	public String getAssetName() {
		return assetName;
	}

	public void setAssetName(String assetName) {
		this.assetName = assetName;
	}

	public Double getLastAsset() {
		return lastAsset;
	}

	public void setLastAsset(Double lastAsset) {
		this.lastAsset = lastAsset;
	}

	public Double getAddAsset() {
		return addAsset;
	}

	public void setAddAsset(Double addAsset) {
		this.addAsset = addAsset;
	}

	public Double getReduceAsset() {
		return reduceAsset;
	}

	public void setReduceAsset(Double reduceAsset) {
		this.reduceAsset = reduceAsset;
	}

	public Double getNewAsset() {
		return newAsset;
	}

	public void setNewAsset(Double newAsset) {
		this.newAsset = newAsset;
	}

	public Double getDepreciationRate() {
		return depreciationRate;
	}

	public void setDepreciationRate(Double depreciationRate) {
		this.depreciationRate = depreciationRate;
	}

	public Double getDepreciationNumber() {
		return depreciationNumber;
	}

	public void setDepreciationNumber(Double depreciationNumber) {
		this.depreciationNumber = depreciationNumber;
	}

	public Double getDepreciationSum() {
		return depreciationSum;
	}

	public void setDepreciationSum(Double depreciationSum) {
		this.depreciationSum = depreciationSum;
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

	public Double getNewAssetCount() {
		return newAssetCount;
	}

	public void setNewAssetCount(Double newAssetCount) {
		this.newAssetCount = newAssetCount;
	}
}
