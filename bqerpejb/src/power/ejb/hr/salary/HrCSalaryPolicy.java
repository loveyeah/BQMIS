package power.ejb.hr.salary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCSalaryPolicy entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_SALARY_POLICY")
public class HrCSalaryPolicy implements java.io.Serializable {

	// Fields

	private Long policyId;
	private Long stationId;
	private Double increaseRange;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCSalaryPolicy() {
	}

	/** minimal constructor */
	public HrCSalaryPolicy(Long policyId) {
		this.policyId = policyId;
	}

	/** full constructor */
	public HrCSalaryPolicy(Long policyId, Long stationId, Double increaseRange,
			String memo, String isUse, String enterpriseCode) {
		this.policyId = policyId;
		this.stationId = stationId;
		this.increaseRange = increaseRange;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "POLICY_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPolicyId() {
		return this.policyId;
	}

	public void setPolicyId(Long policyId) {
		this.policyId = policyId;
	}

	@Column(name = "STATION_ID", precision = 10, scale = 0)
	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	@Column(name = "INCREASE_RANGE", precision = 10, scale = 4)
	public Double getIncreaseRange() {
		return this.increaseRange;
	}

	public void setIncreaseRange(Double increaseRange) {
		this.increaseRange = increaseRange;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}