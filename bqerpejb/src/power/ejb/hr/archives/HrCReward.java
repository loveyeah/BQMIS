package power.ejb.hr.archives;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCReward entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_REWARD")
public class HrCReward implements java.io.Serializable {

	// Fields

	private Long rewardId;
	private Long empId;
	private String rewardName;
	private Date rewardTime;
	private String rewardType;
	private String rewardLeveal;
	private String rewardUnit;
	private String rewardReason;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCReward() {
	}

	/** minimal constructor */
	public HrCReward(Long rewardId) {
		this.rewardId = rewardId;
	}

	/** full constructor */
	public HrCReward(Long rewardId, Long empId, String rewardName,
			Date rewardTime, String rewardType, String rewardLeveal,
			String rewardUnit, String rewardReason, String isUse,
			String enterpriseCode) {
		this.rewardId = rewardId;
		this.empId = empId;
		this.rewardName = rewardName;
		this.rewardTime = rewardTime;
		this.rewardType = rewardType;
		this.rewardLeveal = rewardLeveal;
		this.rewardUnit = rewardUnit;
		this.rewardReason = rewardReason;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "REWARD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRewardId() {
		return this.rewardId;
	}

	public void setRewardId(Long rewardId) {
		this.rewardId = rewardId;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "REWARD_NAME", length = 50)
	public String getRewardName() {
		return this.rewardName;
	}

	public void setRewardName(String rewardName) {
		this.rewardName = rewardName;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REWARD_TIME", length = 7)
	public Date getRewardTime() {
		return this.rewardTime;
	}

	public void setRewardTime(Date rewardTime) {
		this.rewardTime = rewardTime;
	}

	@Column(name = "REWARD_TYPE", length = 20)
	public String getRewardType() {
		return this.rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	@Column(name = "REWARD_LEVEAL", length = 20)
	public String getRewardLeveal() {
		return this.rewardLeveal;
	}

	public void setRewardLeveal(String rewardLeveal) {
		this.rewardLeveal = rewardLeveal;
	}

	@Column(name = "REWARD_UNIT", length = 30)
	public String getRewardUnit() {
		return this.rewardUnit;
	}

	public void setRewardUnit(String rewardUnit) {
		this.rewardUnit = rewardUnit;
	}

	@Column(name = "REWARD_REASON", length = 100)
	public String getRewardReason() {
		return this.rewardReason;
	}

	public void setRewardReason(String rewardReason) {
		this.rewardReason = rewardReason;
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