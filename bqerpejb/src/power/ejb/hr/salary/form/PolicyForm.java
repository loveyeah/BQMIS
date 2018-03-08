package power.ejb.hr.salary.form;

public class PolicyForm
{
	private Long policyId;
	private Long stationId;
	private Double increaseRange;
	private String memo;
	private String isUse;
	private String enterpriseCode;
	
	// 岗位名称
	private String stationName;

	public Long getPolicyId() {
		return policyId;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Double getIncreaseRange() {
		return increaseRange;
	}

	public void setIncreaseRange(Double increaseRange) {
		this.increaseRange = increaseRange;
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

	public String getStationName() {
		return stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
}