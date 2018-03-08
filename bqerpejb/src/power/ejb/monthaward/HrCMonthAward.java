package power.ejb.monthaward;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCMonthAward entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_MONTH_AWARD", schema = "POWER")
public class HrCMonthAward implements java.io.Serializable {

	// Fields

	private Long monthAwardId;
	private Double monthAward;
	private Date effectStartTime;
	private Date effectEndTime;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCMonthAward() {
	}

	/** minimal constructor */
	public HrCMonthAward(Long monthAwardId) {
		this.monthAwardId = monthAwardId;
	}

	/** full constructor */
	public HrCMonthAward(Long monthAwardId, Double monthAward,
			Date effectStartTime, Date effectEndTime, String memo,
			String isUse, String enterpriseCode) {
		this.monthAwardId = monthAwardId;
		this.monthAward = monthAward;
		this.effectStartTime = effectStartTime;
		this.effectEndTime = effectEndTime;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "MONTH_AWARD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getMonthAwardId() {
		return this.monthAwardId;
	}

	public void setMonthAwardId(Long monthAwardId) {
		this.monthAwardId = monthAwardId;
	}

	@Column(name = "MONTH_AWARD", precision = 10)
	public Double getMonthAward() {
		return this.monthAward;
	}

	public void setMonthAward(Double monthAward) {
		this.monthAward = monthAward;
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