package power.ejb.hr.salary;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCBigAward entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_BIG_AWARD")
public class HrCBigAward implements java.io.Serializable {

	// Fields

	private Long bigAwardId;
	private String bigAwardName;
	private Date awardMonth;
	private Date assessmentFrom;
	private Date assessmentTo;
	private Double halfStandarddays;
	private  Double allStandarddays;
	private Double bigAwardBase;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCBigAward() {
	}

	/** minimal constructor */
	public HrCBigAward(Long bigAwardId) {
		this.bigAwardId = bigAwardId;
	}

	/** full constructor */
	public HrCBigAward(Long bigAwardId, String bigAwardName, Date awardMonth,
			Date assessmentFrom, Date assessmentTo, Double halfStandarddays,
			Double bigAwardBase, String memo, String isUse,
			String enterpriseCode) {
		this.bigAwardId = bigAwardId;
		this.bigAwardName = bigAwardName;
		this.awardMonth = awardMonth;
		this.assessmentFrom = assessmentFrom;
		this.assessmentTo = assessmentTo;
		this.halfStandarddays = halfStandarddays;
		this.bigAwardBase = bigAwardBase;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BIG_AWARD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBigAwardId() {
		return this.bigAwardId;
	}

	public void setBigAwardId(Long bigAwardId) {
		this.bigAwardId = bigAwardId;
	}

	@Column(name = "BIG_AWARD_NAME", length = 50)
	public String getBigAwardName() {
		return this.bigAwardName;
	}

	public void setBigAwardName(String bigAwardName) {
		this.bigAwardName = bigAwardName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "AWARD_MONTH", length = 7)
	public Date getAwardMonth() {
		return this.awardMonth;
	}

	public void setAwardMonth(Date awardMonth) {
		this.awardMonth = awardMonth;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ASSESSMENT_FROM", length = 7)
	public Date getAssessmentFrom() {
		return this.assessmentFrom;
	}

	public void setAssessmentFrom(Date assessmentFrom) {
		this.assessmentFrom = assessmentFrom;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ASSESSMENT_TO", length = 7)
	public Date getAssessmentTo() {
		return this.assessmentTo;
	}

	public void setAssessmentTo(Date assessmentTo) {
		this.assessmentTo = assessmentTo;
	}

	@Column(name = "HALF_STANDARDDAYS", precision = 3)
	public Double getHalfStandarddays() {
		return this.halfStandarddays;
	}

	public void setHalfStandarddays(Double halfStandarddays) {
		this.halfStandarddays = halfStandarddays;
	}

	@Column(name = "BIG_AWARD_BASE", precision = 10)
	public Double getBigAwardBase() {
		return this.bigAwardBase;
	}

	public void setBigAwardBase(Double bigAwardBase) {
		this.bigAwardBase = bigAwardBase;
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
	@Column(name = "ALL_STANDARDDAYS", precision = 3)
	public Double getAllStandarddays() {
		return allStandarddays;
	}

	public void setAllStandarddays(Double allStandarddays) {
		this.allStandarddays = allStandarddays;
	}

}