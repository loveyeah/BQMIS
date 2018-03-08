package power.ejb.run.repair;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCRepairProject entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_C_REPAIR_PROJECT")
public class RunCRepairProject implements java.io.Serializable {

	// Fields

	private Long repairProjectId;
	private Long FProjectId;
	private String repairProjectName;
	private String workingCharge;
	private String workingMenbers;
	private String workingTime;
	private String acceptanceFirst;
	private String acceptanceSecond;
	private String acceptanceThird;
	private String acceptanceLevel;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCRepairProject() {
	}

	/** minimal constructor */
	public RunCRepairProject(Long repairProjectId) {
		this.repairProjectId = repairProjectId;
	}

	/** full constructor */
	public RunCRepairProject(Long repairProjectId, Long FProjectId,
			String repairProjectName, String workingCharge,
			String workingMenbers, String workingTime, String acceptanceFirst,
			String acceptanceSecond, String acceptanceThird,
			String acceptanceLevel, String isUse, String enterpriseCode) {
		this.repairProjectId = repairProjectId;
		this.FProjectId = FProjectId;
		this.repairProjectName = repairProjectName;
		this.workingCharge = workingCharge;
		this.workingMenbers = workingMenbers;
		this.workingTime = workingTime;
		this.acceptanceFirst = acceptanceFirst;
		this.acceptanceSecond = acceptanceSecond;
		this.acceptanceThird = acceptanceThird;
		this.acceptanceLevel = acceptanceLevel;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "REPAIR_PROJECT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRepairProjectId() {
		return this.repairProjectId;
	}

	public void setRepairProjectId(Long repairProjectId) {
		this.repairProjectId = repairProjectId;
	}

	@Column(name = "F_PROJECT_ID", precision = 10, scale = 0)
	public Long getFProjectId() {
		return this.FProjectId;
	}

	public void setFProjectId(Long FProjectId) {
		this.FProjectId = FProjectId;
	}

	@Column(name = "REPAIR_PROJECT_NAME", length = 100)
	public String getRepairProjectName() {
		return this.repairProjectName;
	}

	public void setRepairProjectName(String repairProjectName) {
		this.repairProjectName = repairProjectName;
	}

	@Column(name = "WORKING_CHARGE", length = 20)
	public String getWorkingCharge() {
		return this.workingCharge;
	}

	public void setWorkingCharge(String workingCharge) {
		this.workingCharge = workingCharge;
	}

	@Column(name = "WORKING_MENBERS", length = 100)
	public String getWorkingMenbers() {
		return this.workingMenbers;
	}

	public void setWorkingMenbers(String workingMenbers) {
		this.workingMenbers = workingMenbers;
	}

	@Column(name = "WORKING_TIME", length = 100)
	public String getWorkingTime() {
		return this.workingTime;
	}

	public void setWorkingTime(String workingTime) {
		this.workingTime = workingTime;
	}

	@Column(name = "ACCEPTANCE_FIRST", length = 20)
	public String getAcceptanceFirst() {
		return this.acceptanceFirst;
	}

	public void setAcceptanceFirst(String acceptanceFirst) {
		this.acceptanceFirst = acceptanceFirst;
	}

	@Column(name = "ACCEPTANCE_SECOND", length = 20)
	public String getAcceptanceSecond() {
		return this.acceptanceSecond;
	}

	public void setAcceptanceSecond(String acceptanceSecond) {
		this.acceptanceSecond = acceptanceSecond;
	}

	@Column(name = "ACCEPTANCE_THIRD", length = 20)
	public String getAcceptanceThird() {
		return this.acceptanceThird;
	}

	public void setAcceptanceThird(String acceptanceThird) {
		this.acceptanceThird = acceptanceThird;
	}

	@Column(name = "ACCEPTANCE_LEVEL", length = 1)
	public String getAcceptanceLevel() {
		return this.acceptanceLevel;
	}

	public void setAcceptanceLevel(String acceptanceLevel) {
		this.acceptanceLevel = acceptanceLevel;
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