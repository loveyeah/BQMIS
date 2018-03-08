package power.ejb.manage.plan.trainplan;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpJTrainingSumApproval entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "BP_J_TRAINING_SUM_APPROVAL")
public class BpJTrainingSumApproval implements java.io.Serializable {

	// Fields

	private Long approvalId;
	private Long workflowNo;
	private Long workflowStatus;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJTrainingSumApproval() {
	}

	/** minimal constructor */
	public BpJTrainingSumApproval(Long approvalId) {
		this.approvalId = approvalId;
	}

	/** full constructor */
	public BpJTrainingSumApproval(Long approvalId, Long workflowNo,
			Long workflowStatus, String isUse, String enterpriseCode) {
		this.approvalId = approvalId;
		this.workflowNo = workflowNo;
		this.workflowStatus = workflowStatus;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "APPROVAL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getApprovalId() {
		return this.approvalId;
	}

	public void setApprovalId(Long approvalId) {
		this.approvalId = approvalId;
	}

	@Column(name = "WORKFLOW_NO", precision = 22, scale = 0)
	public Long getWorkflowNo() {
		return this.workflowNo;
	}

	public void setWorkflowNo(Long workflowNo) {
		this.workflowNo = workflowNo;
	}

	@Column(name = "WORKFLOW_STATUS", precision = 1, scale = 0)
	public Long getWorkflowStatus() {
		return this.workflowStatus;
	}

	public void setWorkflowStatus(Long workflowStatus) {
		this.workflowStatus = workflowStatus;
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