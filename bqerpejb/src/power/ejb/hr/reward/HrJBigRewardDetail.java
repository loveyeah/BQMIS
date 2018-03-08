package power.ejb.hr.reward;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrJBigRewardDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_BIG_REWARD_DETAIL")
public class HrJBigRewardDetail implements java.io.Serializable {

	// Fields

	private Long bigDetailId;
	private Long bigRewardId;
	private Long deptId;
	private Long empCount;
	private Double bigRewardNum;
	private String memo;
	private Long workFlowNo;
	private String workFlowState;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJBigRewardDetail() {
	}

	/** minimal constructor */
	public HrJBigRewardDetail(Long bigDetailId) {
		this.bigDetailId = bigDetailId;
	}

	/** full constructor */
	public HrJBigRewardDetail(Long bigDetailId, Long bigRewardId, Long deptId,
			Long empCount, Double bigRewardNum, String memo, Long workFlowNo,
			String workFlowState, String isUse, String enterpriseCode) {
		this.bigDetailId = bigDetailId;
		this.bigRewardId = bigRewardId;
		this.deptId = deptId;
		this.empCount = empCount;
		this.bigRewardNum = bigRewardNum;
		this.memo = memo;
		this.workFlowNo = workFlowNo;
		this.workFlowState = workFlowState;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BIG_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBigDetailId() {
		return this.bigDetailId;
	}

	public void setBigDetailId(Long bigDetailId) {
		this.bigDetailId = bigDetailId;
	}

	@Column(name = "BIG_REWARD_ID", precision = 10, scale = 0)
	public Long getBigRewardId() {
		return this.bigRewardId;
	}

	public void setBigRewardId(Long bigRewardId) {
		this.bigRewardId = bigRewardId;
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

	@Column(name = "BIG_REWARD_NUM", precision = 15, scale = 4)
	public Double getBigRewardNum() {
		return this.bigRewardNum;
	}

	public void setBigRewardNum(Double bigRewardNum) {
		this.bigRewardNum = bigRewardNum;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "WORK_FLOW_NO", precision = 10, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "WORK_FLOW_STATE", length = 2)
	public String getWorkFlowState() {
		return this.workFlowState;
	}

	public void setWorkFlowState(String workFlowState) {
		this.workFlowState = workFlowState;
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