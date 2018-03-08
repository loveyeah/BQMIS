package power.ejb.hr.reward;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJMonthReward entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_MONTH_REWARD")
public class HrJMonthReward implements java.io.Serializable {

	// Fields

	private Long rewardId;
	private String rewardMonth;
	private Double monthBase;
	private Date handedDate;
	private String fillBy;
	private Date fillDate;
	private String workFlowState;
	private Long workFlowNo;
	private String isUse;
	private String enterpriseCode;
    private Double coefficient;// 全厂系数 add by fyyang 20100722
	// Constructors

	/** default constructor */
	public HrJMonthReward() {
	}

	/** minimal constructor */
	public HrJMonthReward(Long rewardId) {
		this.rewardId = rewardId;
	}

	/** full constructor */
	public HrJMonthReward(Long rewardId, String rewardMonth, Double monthBase,
			Date handedDate, String fillBy, Date fillDate,
			String workFlowState, Long workFlowNo, String isUse,
			String enterpriseCode,Double coefficient) {
		this.rewardId = rewardId;
		this.rewardMonth = rewardMonth;
		this.monthBase = monthBase;
		this.handedDate = handedDate;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.workFlowState = workFlowState;
		this.workFlowNo = workFlowNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.coefficient=coefficient;
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

	@Column(name = "REWARD_MONTH", length = 10)
	public String getRewardMonth() {
		return this.rewardMonth;
	}

	public void setRewardMonth(String rewardMonth) {
		this.rewardMonth = rewardMonth;
	}

	@Column(name = "MONTH_BASE", precision = 15, scale = 4)
	public Double getMonthBase() {
		return this.monthBase;
	}

	public void setMonthBase(Double monthBase) {
		this.monthBase = monthBase;
	}

	@Temporal(TemporalType.TIMESTAMP)
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

	@Temporal(TemporalType.TIMESTAMP)
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

	@Column(name = "COEFFICIENT", precision = 15, scale = 4)
	public Double getCoefficient() {
		return coefficient;
	}

	public void setCoefficient(Double coefficient) {
		this.coefficient = coefficient;
	}

}