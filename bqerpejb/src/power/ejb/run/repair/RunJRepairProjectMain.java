package power.ejb.run.repair;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJRepairProjectMain entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_J_REPAIR_PROJECT_MAIN")
public class RunJRepairProjectMain implements java.io.Serializable {

	// Fields

	private Long projectMainId;
	private String projectMainYear;
	private Long repairTypeId;
	private String fillBy;
	private Date fillTime;
	private Long specialityId;
	private Long tasklistId;
	private String memo;
	private Long version;
	private Long workflowNo;
	private Long workflowStatus;
	private String isUse;
	private String enterpriseCode;
	private String finalVersion;
	private String situationProject;

	// Constructors

	/** default constructor */
	public RunJRepairProjectMain() {
	}

	/** minimal constructor */
	public RunJRepairProjectMain(Long projectMainId) {
		this.projectMainId = projectMainId;
	}

	/** full constructor */
	public RunJRepairProjectMain(Long projectMainId, String projectMainYear,
			Long repairTypeId, String fillBy, Date fillTime, Long specialityId,
			Long tasklistId, String memo, Long version, Long workflowNo,
			Long workflowStatus, String isUse, String enterpriseCode,
			String finalVersion, String situationProject) {
		this.projectMainId = projectMainId;
		this.projectMainYear = projectMainYear;
		this.repairTypeId = repairTypeId;
		this.fillBy = fillBy;
		this.fillTime = fillTime;
		this.specialityId = specialityId;
		this.tasklistId = tasklistId;
		this.memo = memo;
		this.version = version;
		this.workflowNo = workflowNo;
		this.workflowStatus = workflowStatus;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.finalVersion = finalVersion;
		this.situationProject = situationProject;
	}

	// Property accessors
	@Id
	@Column(name = "PROJECT_MAIN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getProjectMainId() {
		return this.projectMainId;
	}

	public void setProjectMainId(Long projectMainId) {
		this.projectMainId = projectMainId;
	}

	@Column(name = "PROJECT_MAIN_YEAR", length = 4)
	public String getProjectMainYear() {
		return this.projectMainYear;
	}

	public void setProjectMainYear(String projectMainYear) {
		this.projectMainYear = projectMainYear;
	}

	@Column(name = "REPAIR_TYPE_ID", precision = 10, scale = 0)
	public Long getRepairTypeId() {
		return this.repairTypeId;
	}

	public void setRepairTypeId(Long repairTypeId) {
		this.repairTypeId = repairTypeId;
	}

	@Column(name = "FILL_BY", length = 20)
	public String getFillBy() {
		return this.fillBy;
	}

	public void setFillBy(String fillBy) {
		this.fillBy = fillBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FILL_TIME", length = 7)
	public Date getFillTime() {
		return this.fillTime;
	}

	public void setFillTime(Date fillTime) {
		this.fillTime = fillTime;
	}

	@Column(name = "SPECIALITY_ID", precision = 10, scale = 0)
	public Long getSpecialityId() {
		return this.specialityId;
	}

	public void setSpecialityId(Long specialityId) {
		this.specialityId = specialityId;
	}

	@Column(name = "TASKLIST_ID", precision = 10, scale = 0)
	public Long getTasklistId() {
		return this.tasklistId;
	}

	public void setTasklistId(Long tasklistId) {
		this.tasklistId = tasklistId;
	}

	@Column(name = "MEMO", length = 100)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "VERSION", precision = 10, scale = 0)
	public Long getVersion() {
		return this.version;
	}

	public void setVersion(Long version) {
		this.version = version;
	}

	@Column(name = "WORKFLOW_NO", precision = 22, scale = 0)
	public Long getWorkflowNo() {
		return this.workflowNo;
	}

	public void setWorkflowNo(Long workflowNo) {
		this.workflowNo = workflowNo;
	}

	@Column(name = "WORKFLOW_STATUS", precision = 2, scale = 0)
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

	@Column(name = "FINAL_VERSION", length = 1)
	public String getFinalVersion() {
		return this.finalVersion;
	}

	public void setFinalVersion(String finalVersion) {
		this.finalVersion = finalVersion;
	}

	@Column(name = "SITUATION_PROJECT", length = 1)
	public String getSituationProject() {
		return this.situationProject;
	}

	public void setSituationProject(String situationProject) {
		this.situationProject = situationProject;
	}

}