package power.ejb.manage.plan.trainplan;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "BP_J_TRAINING_MAIN")
public class BpJTrainingMain implements java.io.Serializable {

	// Fields

	private Long trainingMainId;
	private Long trainingGatherId;
	private Date trainingMonth;
	private String trainingDep;
	private Long workflowNo;
	private Long workflowStatus;
	private String reportBy;
	private Date reportTime;
	private String isUse;
	private String enterpriseCode;

	//add by drdu 091210
	private Long backfillWorkflowNo;
	private Long backfillWorkflowStatus;
	private String backfillBy;
	private Date backfillDate;
	
	private String fillBy;
	
	// Constructors

	

	/** default constructor */
	public BpJTrainingMain() {
	}

	/** minimal constructor */
	public BpJTrainingMain(Long trainingMainId, Date trainingMonth,
			String trainingDep) {
		this.trainingMainId = trainingMainId;
		this.trainingMonth = trainingMonth;
		this.trainingDep = trainingDep;
	}

	// Property accessors
	@Id
	@Column(name = "TRAINING_MAIN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTrainingMainId() {
		return this.trainingMainId;
	}

	public void setTrainingMainId(Long trainingMainId) {
		this.trainingMainId = trainingMainId;
	}

	@Column(name = "TRAINING_GATHER_ID", precision = 10, scale = 0)
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

	@Column(name = "TRAINING_DEP", nullable = false, length = 20)
	public String getTrainingDep() {
		return this.trainingDep;
	}

	public void setTrainingDep(String trainingDep) {
		this.trainingDep = trainingDep;
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

	@Column(name = "REPORT_BY", length = 16)
	public String getReportBy() {
		return this.reportBy;
	}

	public void setReportBy(String reportBy) {
		this.reportBy = reportBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REPORT_TIME", length = 7)
	public Date getReportTime() {
		return this.reportTime;
	}

	public void setReportTime(Date reportTime) {
		this.reportTime = reportTime;
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
	
	@Column(name = "BACKFILL_WORKFLOW_NO", precision = 22, scale = 0)
	public Long getBackfillWorkflowNo() {
		return this.backfillWorkflowNo;
	}

	public void setBackfillWorkflowNo(Long backfillWorkflowNo) {
		this.backfillWorkflowNo = backfillWorkflowNo;
	}

	@Column(name = "BACKFILL_WORKFLOW_STATUS", precision = 1, scale = 0)
	public Long getBackfillWorkflowStatus() {
		return this.backfillWorkflowStatus;
	}

	public void setBackfillWorkflowStatus(Long backfillWorkflowStatus) {
		this.backfillWorkflowStatus = backfillWorkflowStatus;
	}

	@Column(name = "BACKFILL_BY", length = 16)
	public String getBackfillBy() {
		return this.backfillBy;
	}

	public void setBackfillBy(String backfillBy) {
		this.backfillBy = backfillBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BACKFILL_DATE", length = 7)
	public Date getBackfillDate() {
		return this.backfillDate;
	}

	public void setBackfillDate(Date backfillDate) {
		this.backfillDate = backfillDate;
	}

	@Column(name = "FILL_BY", length = 16)
	public String getFillBy() {
		return fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}
}