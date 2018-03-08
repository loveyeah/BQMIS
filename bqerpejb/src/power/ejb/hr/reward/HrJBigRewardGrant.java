package power.ejb.hr.reward;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJBigRewardGrant entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_BIG_REWARD_GRANT")
public class HrJBigRewardGrant implements java.io.Serializable {

	// Fields

	private Long bigGrantId;
	private Long bigAwardId;
	private String bigGrantMonth;
	private Long deptId;
	private Long groupId;
	private String fillBy;
	private Date fillDate;
	private Long workFlowNo;
	private String workFlowState;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJBigRewardGrant() {
	}

	/** minimal constructor */
	public HrJBigRewardGrant(Long bigGrantId) {
		this.bigGrantId = bigGrantId;
	}

	/** full constructor */
	public HrJBigRewardGrant(Long bigGrantId, Long bigAwardId,
			String bigGrantMonth, Long deptId, Long groupId, String fillBy,
			Date fillDate, Long workFlowNo, String workFlowState, String isUse,
			String enterpriseCode) {
		this.bigGrantId = bigGrantId;
		this.bigAwardId = bigAwardId;
		this.bigGrantMonth = bigGrantMonth;
		this.deptId = deptId;
		this.groupId = groupId;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.workFlowNo = workFlowNo;
		this.workFlowState = workFlowState;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BIG_GRANT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBigGrantId() {
		return this.bigGrantId;
	}

	public void setBigGrantId(Long bigGrantId) {
		this.bigGrantId = bigGrantId;
	}

	@Column(name = "BIG_AWARD_ID", precision = 10, scale = 0)
	public Long getBigAwardId() {
		return this.bigAwardId;
	}

	public void setBigAwardId(Long bigAwardId) {
		this.bigAwardId = bigAwardId;
	}

	@Column(name = "BIG_GRANT_MONTH", length = 10)
	public String getBigGrantMonth() {
		return this.bigGrantMonth;
	}

	public void setBigGrantMonth(String bigGrantMonth) {
		this.bigGrantMonth = bigGrantMonth;
	}

	@Column(name = "DEPT_ID", precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "GROUP_ID", precision = 10, scale = 0)
	public Long getGroupId() {
		return this.groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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