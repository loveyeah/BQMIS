package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJRewardspunish entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_REWARDSPUNISH")
public class HrJRewardspunish implements java.io.Serializable {

	// Fields

	private Long rewardspunishid;
	private Long rewardsPunishId;
	private Date rewardspunishDate;
	private String rewardsPunishReason;
	private String memo;
	private Date insertdate;
	private String insertby;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long empId;

	// Constructors

	/** default constructor */
	public HrJRewardspunish() {
	}

	/** minimal constructor */
	public HrJRewardspunish(Long rewardspunishid) {
		this.rewardspunishid = rewardspunishid;
	}

	/** full constructor */
	public HrJRewardspunish(Long rewardspunishid, Long rewardsPunishId,
			Date rewardspunishDate, String rewardsPunishReason, String memo,
			Date insertdate, String insertby, String enterpriseCode,
			String isUse, String lastModifiedBy, Date lastModifiedDate,
			Long empId) {
		this.rewardspunishid = rewardspunishid;
		this.rewardsPunishId = rewardsPunishId;
		this.rewardspunishDate = rewardspunishDate;
		this.rewardsPunishReason = rewardsPunishReason;
		this.memo = memo;
		this.insertdate = insertdate;
		this.insertby = insertby;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.empId = empId;
	}

	// Property accessors
	@Id
	@Column(name = "REWARDSPUNISHID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRewardspunishid() {
		return this.rewardspunishid;
	}

	public void setRewardspunishid(Long rewardspunishid) {
		this.rewardspunishid = rewardspunishid;
	}

	@Column(name = "REWARDS_PUNISH_ID", precision = 10, scale = 0)
	public Long getRewardsPunishId() {
		return this.rewardsPunishId;
	}

	public void setRewardsPunishId(Long rewardsPunishId) {
		this.rewardsPunishId = rewardsPunishId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REWARDSPUNISH_DATE", length = 7)
	public Date getRewardspunishDate() {
		return this.rewardspunishDate;
	}

	public void setRewardspunishDate(Date rewardspunishDate) {
		this.rewardspunishDate = rewardspunishDate;
	}

	@Column(name = "REWARDS_PUNISH_REASON", length = 500)
	public String getRewardsPunishReason() {
		return this.rewardsPunishReason;
	}

	public void setRewardsPunishReason(String rewardsPunishReason) {
		this.rewardsPunishReason = rewardsPunishReason;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
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

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

}