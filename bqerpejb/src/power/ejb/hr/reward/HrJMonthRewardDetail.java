package power.ejb.hr.reward;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrJMonthRewardDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_MONTH_REWARD_DETAIL", schema = "POWER")
public class HrJMonthRewardDetail implements java.io.Serializable {

	// Fields

	private Long detailId;
	private Long rewardId;
	private Long deptId;
	private Long empCount;
	private Double lastMonthNum;
	private Double monthRewardNum;
	private Double quantifyCash;
	private Double extraAddNum;
	private Double monthAssessNum;
	private Double otherNum;
	private Double totalNum;
	private String memo;
	private String workFlowState;
	private String workFlowNo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJMonthRewardDetail() {
	}

	/** minimal constructor */
	public HrJMonthRewardDetail(Long detailId) {
		this.detailId = detailId;
	}

	/** full constructor */
	public HrJMonthRewardDetail(Long detailId, Long rewardId, Long deptId,
			Long empCount, Double lastMonthNum, Double monthRewardNum,
			Double quantifyCash, Double extraAddNum, Double monthAssessNum,
			Double otherNum, Double totalNum, String memo,
			String workFlowState, String workFlowNo, String isUse,
			String enterpriseCode) {
		this.detailId = detailId;
		this.rewardId = rewardId;
		this.deptId = deptId;
		this.empCount = empCount;
		this.lastMonthNum = lastMonthNum;
		this.monthRewardNum = monthRewardNum;
		this.quantifyCash = quantifyCash;
		this.extraAddNum = extraAddNum;
		this.monthAssessNum = monthAssessNum;
		this.otherNum = otherNum;
		this.totalNum = totalNum;
		this.memo = memo;
		this.workFlowState = workFlowState;
		this.workFlowNo = workFlowNo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDetailId() {
		return this.detailId;
	}

	public void setDetailId(Long detailId) {
		this.detailId = detailId;
	}

	@Column(name = "REWARD_ID", precision = 10, scale = 0)
	public Long getRewardId() {
		return this.rewardId;
	}

	public void setRewardId(Long rewardId) {
		this.rewardId = rewardId;
	}

	@Column(name = "DEPT_ID", precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "EMP_COUNT", precision = 10, scale = 0)
	public Long getEmpCount() {
		return this.empCount;
	}

	public void setEmpCount(Long empCount) {
		this.empCount = empCount;
	}

	@Column(name = "LAST_MONTH_NUM", precision = 15, scale = 4)
	public Double getLastMonthNum() {
		return this.lastMonthNum;
	}

	public void setLastMonthNum(Double lastMonthNum) {
		this.lastMonthNum = lastMonthNum;
	}

	@Column(name = "MONTH_REWARD_NUM", precision = 15, scale = 4)
	public Double getMonthRewardNum() {
		return this.monthRewardNum;
	}

	public void setMonthRewardNum(Double monthRewardNum) {
		this.monthRewardNum = monthRewardNum;
	}

	@Column(name = "QUANTIFY_CASH", precision = 15, scale = 4)
	public Double getQuantifyCash() {
		return this.quantifyCash;
	}

	public void setQuantifyCash(Double quantifyCash) {
		this.quantifyCash = quantifyCash;
	}

	@Column(name = "EXTRA_ADD_NUM", precision = 15, scale = 4)
	public Double getExtraAddNum() {
		return this.extraAddNum;
	}

	public void setExtraAddNum(Double extraAddNum) {
		this.extraAddNum = extraAddNum;
	}

	@Column(name = "MONTH_ASSESS_NUM", precision = 15, scale = 4)
	public Double getMonthAssessNum() {
		return this.monthAssessNum;
	}

	public void setMonthAssessNum(Double monthAssessNum) {
		this.monthAssessNum = monthAssessNum;
	}

	@Column(name = "OTHER_NUM", precision = 15, scale = 4)
	public Double getOtherNum() {
		return this.otherNum;
	}

	public void setOtherNum(Double otherNum) {
		this.otherNum = otherNum;
	}

	@Column(name = "TOTAL_NUM", precision = 15, scale = 4)
	public Double getTotalNum() {
		return this.totalNum;
	}

	public void setTotalNum(Double totalNum) {
		this.totalNum = totalNum;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "WORK_FLOW_STATE", length = 20)
	public String getWorkFlowState() {
		return this.workFlowState;
	}

	public void setWorkFlowState(String workFlowState) {
		this.workFlowState = workFlowState;
	}

	@Column(name = "WORK_FLOW_NO", length = 20)
	public String getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(String workFlowNo) {
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