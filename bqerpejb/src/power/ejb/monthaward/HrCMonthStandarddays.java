package power.ejb.monthaward;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCMonthStandarddays entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_MONTH_STANDARDDAYS", schema = "POWER")
public class HrCMonthStandarddays implements java.io.Serializable {

	// Fields

	private Long standarddaysId;
	private Double standarddays;
	private Date effectStartTime;
	private Date effectEndTime;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCMonthStandarddays() {
	}

	/** minimal constructor */
	public HrCMonthStandarddays(Long standarddaysId) {
		this.standarddaysId = standarddaysId;
	}

	/** full constructor */
	public HrCMonthStandarddays(Long standarddaysId, Double standarddays,
			Date effectStartTime, Date effectEndTime, String memo,
			String isUse, String enterpriseCode) {
		this.standarddaysId = standarddaysId;
		this.standarddays = standarddays;
		this.effectStartTime = effectStartTime;
		this.effectEndTime = effectEndTime;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "STANDARDDAYS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getStandarddaysId() {
		return this.standarddaysId;
	}

	public void setStandarddaysId(Long standarddaysId) {
		this.standarddaysId = standarddaysId;
	}

	@Column(name = "STANDARDDAYS", precision = 10)
	public Double getStandarddays() {
		return this.standarddays;
	}

	public void setStandarddays(Double standarddays) {
		this.standarddays = standarddays;
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