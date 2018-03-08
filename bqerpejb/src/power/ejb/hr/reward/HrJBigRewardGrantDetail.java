package power.ejb.hr.reward;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrJBigRewardGrantDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_BIG_REWARD_GRANT_DETAIL")
public class HrJBigRewardGrantDetail implements java.io.Serializable {

	// Fields

	private Long bigDetailId;
	private Long bigGrantId;
	private Long empId;
	private Double coefficientNum;
	private Double baseNum;
	private Double amountNum;
	private String signBy;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJBigRewardGrantDetail() {
	}

	/** minimal constructor */
	public HrJBigRewardGrantDetail(Long bigDetailId) {
		this.bigDetailId = bigDetailId;
	}

	/** full constructor */
	public HrJBigRewardGrantDetail(Long bigDetailId, Long bigGrantId,
			Long empId, Double coefficientNum, Double baseNum,
			Double amountNum, String signBy, String memo, String isUse,
			String enterpriseCode) {
		this.bigDetailId = bigDetailId;
		this.bigGrantId = bigGrantId;
		this.empId = empId;
		this.coefficientNum = coefficientNum;
		this.baseNum = baseNum;
		this.amountNum = amountNum;
		this.signBy = signBy;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BIG_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBigDetailId() {
		return this.bigDetailId;
	}

	public void setBigDetailId(Long bigDetailId) {
		this.bigDetailId = bigDetailId;
	}

	@Column(name = "BIG_GRANT_ID", precision = 10, scale = 0)
	public Long getBigGrantId() {
		return this.bigGrantId;
	}

	public void setBigGrantId(Long bigGrantId) {
		this.bigGrantId = bigGrantId;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "COEFFICIENT_NUM", precision = 15, scale = 4)
	public Double getCoefficientNum() {
		return this.coefficientNum;
	}

	public void setCoefficientNum(Double coefficientNum) {
		this.coefficientNum = coefficientNum;
	}

	@Column(name = "BASE_NUM", precision = 15, scale = 4)
	public Double getBaseNum() {
		return this.baseNum;
	}

	public void setBaseNum(Double baseNum) {
		this.baseNum = baseNum;
	}

	@Column(name = "AMOUNT_NUM", precision = 15, scale = 4)
	public Double getAmountNum() {
		return this.amountNum;
	}

	public void setAmountNum(Double amountNum) {
		this.amountNum = amountNum;
	}

	@Column(name = "SIGN_BY", length = 20)
	public String getSignBy() {
		return this.signBy;
	}

	public void setSignBy(String signBy) {
		this.signBy = signBy;
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