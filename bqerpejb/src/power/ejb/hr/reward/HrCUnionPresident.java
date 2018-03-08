package power.ejb.hr.reward;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCUnionPresident entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_UNION_PRESIDENT")
public class HrCUnionPresident implements java.io.Serializable {

	// Fields

	private Long unionPerId;
	private Double unionPerStandard;
	private Date effectStartTime;
	private Date effectEndTime;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCUnionPresident() {
	}

	/** minimal constructor */
	public HrCUnionPresident(Long unionPerId) {
		this.unionPerId = unionPerId;
	}

	/** full constructor */
	public HrCUnionPresident(Long unionPerId, Double unionPerStandard,
			Date effectStartTime, Date effectEndTime, String memo,
			String isUse, String enterpriseCode) {
		this.unionPerId = unionPerId;
		this.unionPerStandard = unionPerStandard;
		this.effectStartTime = effectStartTime;
		this.effectEndTime = effectEndTime;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "UNION_PER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getUnionPerId() {
		return this.unionPerId;
	}

	public void setUnionPerId(Long unionPerId) {
		this.unionPerId = unionPerId;
	}

	@Column(name = "UNION_PER_STANDARD", precision = 15, scale = 4)
	public Double getUnionPerStandard() {
		return this.unionPerStandard;
	}

	public void setUnionPerStandard(Double unionPerStandard) {
		this.unionPerStandard = unionPerStandard;
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