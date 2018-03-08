package power.ejb.hr.reward;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJBigReward entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_BIG_REWARD")
public class HrJBigReward implements java.io.Serializable {

	// Fields

	private Long bigRewardId;
	private String bigRewardMonth;
	private Long bigAwardId;
	private String bigAwardName;
	private Double bigRewardBase;
	private Date handedDate;
	private String fillBy;
	private Date fillDate;
	private String workFlowState;
	private Long workFlowNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJBigReward() {
	}

	/** minimal constructor */
	public HrJBigReward(Long bigRewardId) {
		this.bigRewardId = bigRewardId;
	}

	/** full constructor */
	public HrJBigReward(Long bigRewardId, String bigRewardMonth,
			Long bigAwardId, String bigAwardName, Double bigRewardBase,
			Date handedDate, String fillBy, Date fillDate,
			String workFlowState, Long workFlowNo, String isUse,
			String enterpriseCode) {
		this.bigRewardId = bigRewardId;
		this.bigRewardMonth = bigRewardMonth;
		this.bigAwardId = bigAwardId;
		this.bigAwardName = bigAwardName;
		this.bigRewardBase = bigRewardBase;
		this.handedDate = handedDate;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.workFlowState = workFlowState;
		this.workFlowNo = workFlowNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BIG_REWARD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBigRewardId() {
		return this.bigRewardId;
	}

	public void setBigRewardId(Long bigRewardId) {
		this.bigRewardId = bigRewardId;
	}

	@Column(name = "BIG_REWARD_MONTH", length = 10)
	public String getBigRewardMonth() {
		return this.bigRewardMonth;
	}

	public void setBigRewardMonth(String bigRewardMonth) {
		this.bigRewardMonth = bigRewardMonth;
	}

	@Column(name = "BIG_AWARD_ID", precision = 10, scale = 0)
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

	@Column(name = "BIG_REWARD_BASE", precision = 15, scale = 4)
	public Double getBigRewardBase() {
		return this.bigRewardBase;
	}

	public void setBigRewardBase(Double bigRewardBase) {
		this.bigRewardBase = bigRewardBase;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "HANDED_DATE", length = 7)
	public Date getHandedDate() {
		return this.handedDate;
	}

	public void setHandedDate(Date handedDate) {
		this.handedDate = handedDate;
	}

	@Column(name = "FILL_BY", length = 20)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FILL_DATE", length = 7)
	public Date getFillDate() {
		return this.fillDate;
	}

	public void setFillDate(Date fillDate) {
		this.fillDate = fillDate;
	}

	@Column(name = "WORK_FLOW_STATE", length = 2)
	public String getWorkFlowState() {
		return this.workFlowState;
	}

	public void setWorkFlowState(String workFlowState) {
		this.workFlowState = workFlowState;
	}

	@Column(name = "WORK_FLOW_NO", precision = 10, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
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