package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCRewardspunish entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_REWARDSPUNISH")
public class HrCRewardspunish implements java.io.Serializable {

	// Fields

	private Long rewardsPunishId;
	private String rewardsPunish;
	private String rewardsPunishType;
	private Long orderBy;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String insertby;
	private Date insertdate;

	// Constructors

	/** default constructor */
	public HrCRewardspunish() {
	}

	/** minimal constructor */
	public HrCRewardspunish(Long rewardsPunishId) {
		this.rewardsPunishId = rewardsPunishId;
	}

	/** full constructor */
	public HrCRewardspunish(Long rewardsPunishId, String rewardsPunish,
			String rewardsPunishType, Long orderBy, String enterpriseCode,
			String isUse, String lastModifiedBy, Date lastModifiedDate,
			String insertby, Date insertdate) {
		this.rewardsPunishId = rewardsPunishId;
		this.rewardsPunish = rewardsPunish;
		this.rewardsPunishType = rewardsPunishType;
		this.orderBy = orderBy;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.insertby = insertby;
		this.insertdate = insertdate;
	}

	// Property accessors
	@Id
	@Column(name = "REWARDS_PUNISH_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRewardsPunishId() {
		return this.rewardsPunishId;
	}

	public void setRewardsPunishId(Long rewardsPunishId) {
		this.rewardsPunishId = rewardsPunishId;
	}

	@Column(name = "REWARDS_PUNISH", length = 50)
	public String getRewardsPunish() {
		return this.rewardsPunish;
	}

	public void setRewardsPunish(String rewardsPunish) {
		this.rewardsPunish = rewardsPunish;
	}

	@Column(name = "REWARDS_PUNISH_TYPE", length = 1)
	public String getRewardsPunishType() {
		return this.rewardsPunishType;
	}

	public void setRewardsPunishType(String rewardsPunishType) {
		this.rewardsPunishType = rewardsPunishType;
	}

	@Column(name = "ORDER_BY", precision = 10, scale = 0)
	public Long getOrderBy() {
		return this.orderBy;
	}

	public void setOrderBy(Long orderBy) {
		this.orderBy = orderBy;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

}