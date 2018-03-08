package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJSafetyAnnualplan entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_SAFETY_ANNUALPLAN", schema = "POWER")
public class SpJSafetyAnnualplan implements java.io.Serializable {

	// Fields

	private Long annualplanId;
	private String planYear;
	private String depCode;
	private String planContent;
	private String memo;
	private String fillBy;
	private Date fillTime;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafetyAnnualplan() {
	}

	/** minimal constructor */
	public SpJSafetyAnnualplan(Long annualplanId) {
		this.annualplanId = annualplanId;
	}

	/** full constructor */
	public SpJSafetyAnnualplan(Long annualplanId, String planYear,
			String depCode, String planContent, String memo, String fillBy,
			Date fillTime, String enterpriseCode) {
		this.annualplanId = annualplanId;
		this.planYear = planYear;
		this.depCode = depCode;
		this.planContent = planContent;
		this.memo = memo;
		this.fillBy = fillBy;
		this.fillTime = fillTime;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ANNUALPLAN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAnnualplanId() {
		return this.annualplanId;
	}

	public void setAnnualplanId(Long annualplanId) {
		this.annualplanId = annualplanId;
	}

	@Column(name = "PLAN_YEAR", length = 4)
	public String getPlanYear() {
		return this.planYear;
	}

	public void setPlanYear(String planYear) {
		this.planYear = planYear;
	}

	@Column(name = "DEP_CODE", length = 20)
	public String getDepCode() {
		return this.depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	@Column(name = "PLAN_CONTENT", length = 4000)
	public String getPlanContent() {
		return this.planContent;
	}

	public void setPlanContent(String planContent) {
		this.planContent = planContent;
	}

	@Column(name = "MEMO", length = 500)
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

	@Temporal(TemporalType.DATE)
	@Column(name = "FILL_TIME", length = 7)
	public Date getFillTime() {
		return this.fillTime;
	}

	public void setFillTime(Date fillTime) {
		this.fillTime = fillTime;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}