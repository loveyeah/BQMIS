package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJSecurityPlan entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_SECURITY_PLAN")
public class SpJSecurityPlan implements java.io.Serializable {

	// Fields

	private Long securityPlanId;
	private String planName;
	private String planBasis;
	private Double fee;
	private Long year;
	private Date finishDate;
	private String chargeBy;
	private String chargeDep;
	private String memo;
	private String fillBy;
	private String fillDep;
	private Date fillDate;
	private String finishState;
	private String finishAppraisal;
	private String appraisalBy;
	private Date appraisalDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSecurityPlan() {
	}

	/** minimal constructor */
	public SpJSecurityPlan(Long securityPlanId) {
		this.securityPlanId = securityPlanId;
	}

	/** full constructor */
	public SpJSecurityPlan(Long securityPlanId, String planName,
			String planBasis, Double fee, Long year, Date finishDate,
			String chargeBy, String chargeDep, String memo, String fillBy,
			String fillDep, Date fillDate, String finishState,
			String finishAppraisal, String appraisalBy, Date appraisalDate,
			String enterpriseCode) {
		this.securityPlanId = securityPlanId;
		this.planName = planName;
		this.planBasis = planBasis;
		this.fee = fee;
		this.year = year;
		this.finishDate = finishDate;
		this.chargeBy = chargeBy;
		this.chargeDep = chargeDep;
		this.memo = memo;
		this.fillBy = fillBy;
		this.fillDep = fillDep;
		this.fillDate = fillDate;
		this.finishState = finishState;
		this.finishAppraisal = finishAppraisal;
		this.appraisalBy = appraisalBy;
		this.appraisalDate = appraisalDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SECURITY_PLAN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSecurityPlanId() {
		return this.securityPlanId;
	}

	public void setSecurityPlanId(Long securityPlanId) {
		this.securityPlanId = securityPlanId;
	}

	@Column(name = "PLAN_NAME", length = 100)
	public String getPlanName() {
		return this.planName;
	}

	public void setPlanName(String planName) {
		this.planName = planName;
	}

	@Column(name = "PLAN_BASIS", length = 1000)
	public String getPlanBasis() {
		return this.planBasis;
	}

	public void setPlanBasis(String planBasis) {
		this.planBasis = planBasis;
	}

	@Column(name = "FEE", precision = 15, scale = 4)
	public Double getFee() {
		return this.fee;
	}

	public void setFee(Double fee) {
		this.fee = fee;
	}

	@Column(name = "YEAR", precision = 4, scale = 0)
	public Long getYear() {
		return this.year;
	}

	public void setYear(Long year) {
		this.year = year;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FINISH_DATE", length = 7)
	public Date getFinishDate() {
		return this.finishDate;
	}

	public void setFinishDate(Date finishDate) {
		this.finishDate = finishDate;
	}

	@Column(name = "CHARGE_BY", length = 16)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Column(name = "CHARGE_DEP", length = 20)
	public String getChargeDep() {
		return this.chargeDep;
	}

	public void setChargeDep(String chargeDep) {
		this.chargeDep = chargeDep;
	}

	@Column(name = "MEMO", length = 1000)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "FILL_BY", length = 16)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Column(name = "FILL_DEP", length = 20)
	public String getFillDep() {
		return this.fillDep;
	}

	public void setFillDep(String fillDep) {
		this.fillDep = fillDep;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILL_DATE", length = 7)
	public Date getFillDate() {
		return this.fillDate;
	}

	public void setFillDate(Date fillDate) {
		this.fillDate = fillDate;
	}

	@Column(name = "FINISH_STATE", length = 100)
	public String getFinishState() {
		return this.finishState;
	}

	public void setFinishState(String finishState) {
		this.finishState = finishState;
	}

	@Column(name = "FINISH_APPRAISAL", length = 1000)
	public String getFinishAppraisal() {
		return this.finishAppraisal;
	}

	public void setFinishAppraisal(String finishAppraisal) {
		this.finishAppraisal = finishAppraisal;
	}

	@Column(name = "APPRAISAL_BY", length = 16)
	public String getAppraisalBy() {
		return this.appraisalBy;
	}

	public void setAppraisalBy(String appraisalBy) {
		this.appraisalBy = appraisalBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPRAISAL_DATE", length = 7)
	public Date getAppraisalDate() {
		return this.appraisalDate;
	}

	public void setAppraisalDate(Date appraisalDate) {
		this.appraisalDate = appraisalDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}