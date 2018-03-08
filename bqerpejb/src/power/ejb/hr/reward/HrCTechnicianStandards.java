package power.ejb.hr.reward;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCTechnicianStandards entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_TECHNICIAN_STANDARDS", schema = "POWER")
public class HrCTechnicianStandards implements java.io.Serializable {

	// Fields

	private Long techId;
	private Double techStandard;
	private String isEmploy;
	private Date effectStartTime;
	private Date effectEndTime;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCTechnicianStandards() {
	}

	/** minimal constructor */
	public HrCTechnicianStandards(Long techId) {
		this.techId = techId;
	}

	/** full constructor */
	public HrCTechnicianStandards(Long techId, Double techStandard,
			String isEmploy, Date effectStartTime, Date effectEndTime,
			String memo, String isUse, String enterpriseCode) {
		this.techId = techId;
		this.techStandard = techStandard;
		this.isEmploy = isEmploy;
		this.effectStartTime = effectStartTime;
		this.effectEndTime = effectEndTime;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TECH_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTechId() {
		return this.techId;
	}

	public void setTechId(Long techId) {
		this.techId = techId;
	}

	@Column(name = "TECH_STANDARD", precision = 15, scale = 4)
	public Double getTechStandard() {
		return this.techStandard;
	}

	public void setTechStandard(Double techStandard) {
		this.techStandard = techStandard;
	}

	@Column(name = "IS_EMPLOY", length = 1)
	public String getIsEmploy() {
		return this.isEmploy;
	}

	public void setIsEmploy(String isEmploy) {
		this.isEmploy = isEmploy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECT_START_TIME", length = 7)
	public Date getEffectStartTime() {
		return this.effectStartTime;
	}

	public void setEffectStartTime(Date effectStartTime) {
		this.effectStartTime = effectStartTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EFFECT_END_TIME", length = 7)
	public Date getEffectEndTime() {
		return this.effectEndTime;
	}

	public void setEffectEndTime(Date effectEndTime) {
		this.effectEndTime = effectEndTime;
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