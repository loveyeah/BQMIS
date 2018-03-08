package power.ejb.hr.reward;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJRewardGrant entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_J_REWARD_GRANT")
public class HrJRewardGrant implements java.io.Serializable {

	// Fields

	private Long grantId;
	private String grantMonth;
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
	public HrJRewardGrant() {
	}

	/** minimal constructor */
	public HrJRewardGrant(Long grantId) {
		this.grantId = grantId;
	}

	/** full constructor */
	public HrJRewardGrant(Long grantId, String grantMonth, Long deptId,
			Long groupId, String fillBy, Date fillDate, Long workFlowNo,
			String workFlowState, String isUse, String enterpriseCode) {
		this.grantId = grantId;
		this.grantMonth = grantMonth;
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
	@Column(name = "GRANT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getGrantId() {
		return this.grantId;
	}

	public void setGrantId(Long grantId) {
		this.grantId = grantId;
	}

	@Column(name = "GRANT_MONTH", length = 10)
	public String getGrantMonth() {
		return this.grantMonth;
	}

	public void setGrantMonth(String grantMonth) {
		this.grantMonth = grantMonth;
	}

	@Column(name = "DEPT_ID", precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
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
	
	@Column(name = "GROUP_ID", precision = 10, scale = 0)
	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

}