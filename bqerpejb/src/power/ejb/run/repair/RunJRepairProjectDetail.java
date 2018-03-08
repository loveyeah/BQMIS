package power.ejb.run.repair;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJRepairProjectDetail entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_J_REPAIR_PROJECT_DETAIL")
public class RunJRepairProjectDetail implements java.io.Serializable {

	// Fields

	private Long projectDetailId;
	private Long projectMainId;
	private Long repairProjectId;
	private String standard;
	private String material;
	private String isUse;
	private String enterpriseCode;
	private String situation;
	private String reason;
	private String workingCharge;
	private String workingMenbers;
	private String workingTime;
	private Date startDate;
	private Date endDate;
	private String repairProjectName;
	private String acceptanceSecond;
	private String acceptanceThree;

	// Constructors

	/** default constructor */
	public RunJRepairProjectDetail() {
	}

	/** minimal constructor */
	public RunJRepairProjectDetail(Long projectDetailId, Long projectMainId) {
		this.projectDetailId = projectDetailId;
		this.projectMainId = projectMainId;
	}

	/** full constructor */
	public RunJRepairProjectDetail(Long projectDetailId, Long projectMainId,
			Long repairProjectId, String standard, String material,
			String isUse, String enterpriseCode, String situation,
			String reason, String workingCharge, String workingMenbers,
			String workingTime, Date startDate, Date endDate,
			String repairProjectName,String acceptanceSecond,String acceptanceThree ) {
		this.projectDetailId = projectDetailId;
		this.projectMainId = projectMainId;
		this.repairProjectId = repairProjectId;
		this.standard = standard;
		this.material = material;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.situation = situation;
		this.reason = reason;
		this.workingCharge = workingCharge;
		this.workingMenbers = workingMenbers;
		this.workingTime = workingTime;
		this.startDate = startDate;
		this.endDate = endDate;
		this.repairProjectName = repairProjectName;
		this.acceptanceSecond=acceptanceSecond;
		this.acceptanceThree=acceptanceThree;
	}

	// Property accessors
	@Id
	@Column(name = "PROJECT_DETAIL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getProjectDetailId() {
		return this.projectDetailId;
	}

	public void setProjectDetailId(Long projectDetailId) {
		this.projectDetailId = projectDetailId;
	}

	@Column(name = "PROJECT_MAIN_ID", nullable = false, precision = 10, scale = 0)
	public Long getProjectMainId() {
		return this.projectMainId;
	}

	public void setProjectMainId(Long projectMainId) {
		this.projectMainId = projectMainId;
	}

	@Column(name = "REPAIR_PROJECT_ID", precision = 10, scale = 0)
	public Long getRepairProjectId() {
		return this.repairProjectId;
	}

	public void setRepairProjectId(Long repairProjectId) {
		this.repairProjectId = repairProjectId;
	}

	@Column(name = "STANDARD", length = 100)
	public String getStandard() {
		return this.standard;
	}

	public void setStandard(String standard) {
		this.standard = standard;
	}

	@Column(name = "MATERIAL", length = 1)
	public String getMaterial() {
		return this.material;
	}

	public void setMaterial(String material) {
		this.material = material;
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

	@Column(name = "SITUATION", length = 1)
	public String getSituation() {
		return this.situation;
	}

	public void setSituation(String situation) {
		this.situation = situation;
	}

	@Column(name = "REASON", length = 200)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "REPAIR_PROJECT_NAME", length = 100)
	public String getRepairProjectName() {
		return this.repairProjectName;
	}

	public void setRepairProjectName(String repairProjectName) {
		this.repairProjectName = repairProjectName;
	}
	@Column(name = "ACCEPTANCE_SECOND", length = 20)
	public String getAcceptanceSecond() {
		return acceptanceSecond;
	}

	public void setAcceptanceSecond(String acceptanceSecond) {
		this.acceptanceSecond = acceptanceSecond;
	}
	@Column(name = "ACCEPTANCE_THREE", length = 20)
	public String getAcceptanceThree() {
		return acceptanceThree;
	}

	public void setAcceptanceThree(String acceptanceThree) {
		this.acceptanceThree = acceptanceThree;
	}

}