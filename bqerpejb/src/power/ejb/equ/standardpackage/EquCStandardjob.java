package power.ejb.equ.standardpackage;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquCStandardjob entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "EQU_C_STANDARDJOB")
public class EquCStandardjob implements java.io.Serializable {

	// Fields

	private Long jobId;
	private String jobCode;
	private String code;
	private String description;
	private Long priority;
	private Double jopDuration;
	private String maintDep;
	private String speciality;
	private String crewId;
	private Long interruptable;
	private Long downTime;
	private String supervisor;
	private String laborCode;
	private String calnum;
	private String status;
	private String enterprisecode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquCStandardjob() {
	}

	/** minimal constructor */
	public EquCStandardjob(Long jobId) {
		this.jobId = jobId;
	}

	/** full constructor */
	public EquCStandardjob(Long jobId, String jobCode, String code,
			String description, Long priority, Double jopDuration,
			String maintDep, String speciality, String crewId,
			Long interruptable, Long downTime, String supervisor,
			String laborCode, String calnum, String status,
			String enterprisecode, String isUse) {
		this.jobId = jobId;
		this.jobCode = jobCode;
		this.code = code;
		this.description = description;
		this.priority = priority;
		this.jopDuration = jopDuration;
		this.maintDep = maintDep;
		this.speciality = speciality;
		this.crewId = crewId;
		this.interruptable = interruptable;
		this.downTime = downTime;
		this.supervisor = supervisor;
		this.laborCode = laborCode;
		this.calnum = calnum;
		this.status = status;
		this.enterprisecode = enterprisecode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "JOB_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getJobId() {
		return this.jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	@Column(name = "JOB_CODE", length = 10)
	public String getJobCode() {
		return this.jobCode;
	}

	public void setJobCode(String jobCode) {
		this.jobCode = jobCode;
	}

	@Column(name = "CODE", length = 20)
	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	@Column(name = "DESCRIPTION", length = 100)
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "PRIORITY", precision = 10, scale = 0)
	public Long getPriority() {
		return this.priority;
	}

	public void setPriority(Long priority) {
		this.priority = priority;
	}

	@Column(name = "JOP_DURATION", precision = 15, scale = 4)
	public Double getJopDuration() {
		return this.jopDuration;
	}

	public void setJopDuration(Double jopDuration) {
		this.jopDuration = jopDuration;
	}

	@Column(name = "MAINT_DEP", length = 20)
	public String getMaintDep() {
		return this.maintDep;
	}

	public void setMaintDep(String maintDep) {
		this.maintDep = maintDep;
	}

	@Column(name = "SPECIALITY", length = 10)
	public String getSpeciality() {
		return this.speciality;
	}

	public void setSpeciality(String speciality) {
		this.speciality = speciality;
	}

	@Column(name = "CREW_ID", length = 10)
	public String getCrewId() {
		return this.crewId;
	}

	public void setCrewId(String crewId) {
		this.crewId = crewId;
	}

	@Column(name = "INTERRUPTABLE", precision = 1, scale = 0)
	public Long getInterruptable() {
		return this.interruptable;
	}

	public void setInterruptable(Long interruptable) {
		this.interruptable = interruptable;
	}

	@Column(name = "DOWN_TIME", precision = 1, scale = 0)
	public Long getDownTime() {
		return this.downTime;
	}

	public void setDownTime(Long downTime) {
		this.downTime = downTime;
	}

	@Column(name = "SUPERVISOR", length = 30)
	public String getSupervisor() {
		return this.supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	@Column(name = "LABOR_CODE", length = 10)
	public String getLaborCode() {
		return this.laborCode;
	}

	public void setLaborCode(String laborCode) {
		this.laborCode = laborCode;
	}

	@Column(name = "CALNUM", length = 10)
	public String getCalnum() {
		return this.calnum;
	}

	public void setCalnum(String calnum) {
		this.calnum = calnum;
	}

	@Column(name = "STATUS", length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "ENTERPRISECODE", length = 20)
	public String getEnterprisecode() {
		return this.enterprisecode;
	}

	public void setEnterprisecode(String enterprisecode) {
		this.enterprisecode = enterprisecode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

}