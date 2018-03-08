package power.ejb.hr.reward;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrJRewardGrantDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_REWARD_GRANT_DETAIL")
public class HrJRewardGrantDetail implements java.io.Serializable {

	// Fields

	private Long detailId;
	private Long grantId;
	private Long empId;
	private Double coefficientNum;
	private Double baseNum;
	private Double amountNum;
	private Double awardNum;
	private Double totalNum;
	private String signBy;
	private String memo;
	private String isUse;
	private String enterpriseCode;
	private Double monthRewardNum;
	private Double quantifyCash;
	private Double monthAssessNum;
	private Double quantifyAssessNum;

	private Double addValue;//add by sychen 20100903

	// Constructors

	/** default constructor */
	public HrJRewardGrantDetail() {
	}

	/** minimal constructor */
	public HrJRewardGrantDetail(Long detailId) {
		this.detailId = detailId;
	}

	/** full constructor */
	public HrJRewardGrantDetail(Long detailId, Long grantId, Long empId,
			Double coefficientNum, Double baseNum, Double amountNum,
			Double awardNum, Double totalNum, String signBy, String memo,
			String isUse, String enterpriseCode, Double monthRewardNum,
			Double quantifyCash, Double monthAssessNum, Double quantifyAssessNum) {
		this.detailId = detailId;
		this.grantId = grantId;
		this.empId = empId;
		this.coefficientNum = coefficientNum;
		this.baseNum = baseNum;
		this.amountNum = amountNum;
		this.awardNum = awardNum;
		this.totalNum = totalNum;
		this.signBy = signBy;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.monthRewardNum = monthRewardNum;
		this.quantifyCash = quantifyCash;
		this.monthAssessNum = monthAssessNum;
		this.quantifyAssessNum = quantifyAssessNum;
	}

	// Property accessors
	@Id
	@Column(name = "DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDetailId() {
		return this.detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	@Column(name = "GRANT_ID", precision = 10, scale = 0)
	public Long getGrantId() {
		return this.grantId;
	}

	public void setGrantId(Long grantId) {
		this.grantId = grantId;
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

	@Column(name = "AWARD_NUM", precision = 15, scale = 4)
	public Double getAwardNum() {
		return this.awardNum;
	}

	public void setAwardNum(Double awardNum) {
		this.awardNum = awardNum;
	}

	@Column(name = "TOTAL_NUM", precision = 15, scale = 4)
	public Double getTotalNum() {
		return this.totalNum;
	}

	public void setTotalNum(Double totalNum) {
		this.totalNum = totalNum;
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

	@Column(name = "MONTH_REWARD_NUM", precision = 15, scale = 4)
	public Double getMonthRewardNum() {
		return this.monthRewardNum;
	}

	public void setMonthRewardNum(Double monthRewardNum) {
		this.monthRewardNum = monthRewardNum;
	}

	@Column(name = "QUANTIFY_CASH", precision = 15, scale = 4)
	public Double getQuantifyCash() {
		return this.quantifyCash;
	}

	public void setQuantifyCash(Double quantifyCash) {
		this.quantifyCash = quantifyCash;
	}

	@Column(name = "MONTH_ASSESS_NUM", precision = 15, scale = 4)
	public Double getMonthAssessNum() {
		return this.monthAssessNum;
	}

	public void setMonthAssessNum(Double monthAssessNum) {
		this.monthAssessNum = monthAssessNum;
	}

	@Column(name = "QUANTIFY_ASSESS_NUM", precision = 15, scale = 4)
	public Double getQuantifyAssessNum() {
		return this.quantifyAssessNum;
	}

	public void setQuantifyAssessNum(Double quantifyAssessNum) {
		this.quantifyAssessNum = quantifyAssessNum;
	}

	@Column(name = "ADD_VALUE", precision = 15, scale = 4)
	public Double getAddValue() {
		return addValue;
	}

	public void setAddValue(Double addValue) {
		this.addValue = addValue;
	}

}