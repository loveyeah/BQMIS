package power.ejb.run.repair;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJRepairProjectAccept entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_REPAIR_PROJECT_ACCEPT")
public class RunJRepairProjectAccept implements java.io.Serializable {

	// Fields

	private Long acceptId;
	private String repairProjectName;
	private String workingMenbers;
	private String workingCharge;
	private Date startTime;
	private Date endTime;
	private Long specialityId;
	private String fillBy;
	private Date fillDate;
	private String completion;
	private String memo;
	private String acceptanceLevel;
	private Long workflowNo;
	private String workflowStatus;
	private String isUse;
	private String enterpriseCode;
	private Date planStartDate;
	private Date planEndDate;

	// Constructors

	/** default constructor */
	public RunJRepairProjectAccept() {
	}

	/** minimal constructor */
	public RunJRepairProjectAccept(Long acceptId) {
		this.acceptId = acceptId;
	}

	/** full constructor */
	public RunJRepairProjectAccept(Long acceptId, String repairProjectName,
			String workingMenbers, String workingCharge, Date startTime,
			Date endTime, Long specialityId, String fillBy, Date fillDate,
			String completion, String memo, String acceptanceLevel,
			Long workflowNo, String workflowStatus, String isUse,
			String enterpriseCode, Date planStartDate, Date planEndDate) {
		this.acceptId = acceptId;
		this.repairProjectName = repairProjectName;
		this.workingMenbers = workingMenbers;
		this.workingCharge = workingCharge;
		this.startTime = startTime;
		this.endTime = endTime;
		this.specialityId = specialityId;
		this.fillBy = fillBy;
		this.fillDate = fillDate;
		this.completion = completion;
		this.memo = memo;
		this.acceptanceLevel = acceptanceLevel;
		this.workflowNo = workflowNo;
		this.workflowStatus = workflowStatus;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.planStartDate = planStartDate;
		this.planEndDate = planEndDate;
	}

	// Property accessors
	@Id
	@Column(name = "ACCEPT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAcceptId() {
		return this.acceptId;
	}

	public void setAcceptId(Long acceptId) {
		this.acceptId = acceptId;
	}

	@Column(name = "REPAIR_PROJECT_NAME", length = 100)
	public String getRepairProjectName() {
		return this.repairProjectName;
	}

	public void setRepairProjectName(String repairProjectName) {
		this.repairProjectName = repairProjectName;
	}

	@Column(name = "WORKING_MENBERS", length = 100)
	public String getWorkingMenbers() {
		return this.workingMenbers;
	}

	public void setWorkingMenbers(String workingMenbers) {
		this.workingMenbers = workingMenbers;
	}

	@Column(name = "WORKING_CHARGE", length = 20)
	public String getWorkingCharge() {
		return this.workingCharge;
	}

	public void setWorkingCharge(String workingCharge) {
		this.workingCharge = workingCharge;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_TIME", length = 7)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_TIME", length = 7)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "SPECIALITY_ID", precision = 10, scale = 0)
	public Long getSpecialityId() {
		return this.specialityId;
	}

	public void setSpecialityId(Long specialityId) {
		this.specialityId = specialityId;
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

	@Column(name = "COMPLETION", length = 500)
	public String getCompletion() {
		return this.completion;
	}

	public void setCompletion(String completion) {
		this.completion = completion;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ACCEPTANCE_LEVEL", length = 1)
	public String getAcceptanceLevel() {
		return this.acceptanceLevel;
	}

	public void setAcceptanceLevel(String acceptanceLevel) {
		this.acceptanceLevel = acceptanceLevel;
	}

	@Column(name = "WORKFLOW_NO", precision = 22, scale = 0)
	public Long getWorkflowNo() {
		return this.workflowNo;
	}

	public void setWorkflowNo(Long workflowNo) {
		this.workflowNo = workflowNo;
	}

	@Column(name = "WORKFLOW_STATUS", length = 1)
	public String getWorkflowStatus() {
		return this.workflowStatus;
	}

	public void setWorkflowStatus(String workflowStatus) {
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

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_START_DATE", length = 7)
	public Date getPlanStartDate() {
		return planStartDate;
	}

	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_END_DATE", length = 7)
	public Date getPlanEndDate() {
		return planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}
}