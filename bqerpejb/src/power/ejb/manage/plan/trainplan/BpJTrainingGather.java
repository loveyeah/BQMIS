package power.ejb.manage.plan.trainplan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * BpJTrainingGather entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_J_TRAINING_GATHER")
public class BpJTrainingGather implements java.io.Serializable {

	// Fields

	private Long trainingGatherId;
	private Date trainingMonth;
	private Long workflowNo;
	private Long workflowStatus;
	private String gatherBy;
	private Date gatherTime;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpJTrainingGather() {
	}

	/** minimal constructor */
	public BpJTrainingGather(Long trainingGatherId, Date trainingMonth) {
		this.trainingGatherId = trainingGatherId;
		this.trainingMonth = trainingMonth;
	}



	// Property accessors
	@Id
	@Column(name = "TRAINING_GATHER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTrainingGatherId() {
		return this.trainingGatherId;
	}

	public void setTrainingGatherId(Long trainingGatherId) {
		this.trainingGatherId = trainingGatherId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TRAINING_MONTH", nullable = false, length = 7)
	public Date getTrainingMonth() {
		return this.trainingMonth;
	}

	public void setTrainingMonth(Date trainingMonth) {
		this.trainingMonth = trainingMonth;
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

	@Column(name = "GATHER_BY", length = 16)
	public String getGatherBy() {
		return this.gatherBy;
	}

	public void setGatherBy(String gatherBy) {
		this.gatherBy = gatherBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "GATHER_TIME", length = 7)
	public Date getGatherTime() {
		return this.gatherTime;
	}

	public void setGatherTime(Date gatherTime) {
		this.gatherTime = gatherTime;
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