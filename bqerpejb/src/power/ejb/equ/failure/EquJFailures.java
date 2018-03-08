package power.ejb.equ.failure;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquJFailures entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_FAILURES")
public class EquJFailures implements java.io.Serializable {

	// Fields

	private Long id;
	private String failureCode;
	private String woCode;
	private String bugCode;
	private String failuretypeCode;
	private String failureLevel;
	private String woStatus;
	private String preContent;
	private String failureContent;
	private String ifStopRun;
	private String runProfession;
	private String dominationProfession;
	private String repairDep;
	private String attributeCode;
	private String equName;
	private String installationCode;
	private String installationDesc;
	private String belongSystem;
	private String locationCode;
	private String locationDesc;
	private String likelyReason;
	private String findBy;
	private Date findDate;
	private String findDept;
	private Long woPriority;
	private String writeBy;
	private String writeDept;
	private Date writeDate;
	private String repairDept;
	private String realrepairDept;
	private String ifOpenWorkorder;
	private String ifRepeat;
	private String supervisor;
	private String workFlowNo;
	private String wfState;
	private String unqualifiedFailureCode;
	private String isTel;
	private String telMan;
	private Date telTime;
	private String isMessage;
	private String isCheck;
	private Date cliamDate;
	private String cliamBy;
	private String entrepriseCode;
	private String isuse;
	
	private String isSend;

	// Constructors

	

	/** default constructor */
	public EquJFailures() {
	}

	/** minimal constructor */
	public EquJFailures(Long id) {
		this.id = id;
	}

	/** full constructor */
	public EquJFailures(Long id, String failureCode, String woCode,
			String bugCode, String failuretypeCode, String failureLevel,
			String woStatus, String preContent, String failureContent,
			String ifStopRun, String runProfession,
			String dominationProfession, String repairDep,
			String attributeCode, String equName, String installationCode,
			String installationDesc, String belongSystem, String locationCode,
			String locationDesc, String likelyReason, String findBy,
			Date findDate, String findDept, Long woPriority, String writeBy,
			String writeDept, Date writeDate, String repairDept,
			String realrepairDept, String ifOpenWorkorder, String ifRepeat,
			String supervisor, String workFlowNo, String wfState,
			String unqualifiedFailureCode, String isTel, String telMan,
			Date telTime, String isMessage, String isCheck, Date cliamDate,
			String entrepriseCode, String isuse,String cliamBy) {
		this.id = id;
		this.failureCode = failureCode;
		this.woCode = woCode;
		this.bugCode = bugCode;
		this.failuretypeCode = failuretypeCode;
		this.failureLevel = failureLevel;
		this.woStatus = woStatus;
		this.preContent = preContent;
		this.failureContent = failureContent;
		this.ifStopRun = ifStopRun;
		this.runProfession = runProfession;
		this.dominationProfession = dominationProfession;
		this.repairDep = repairDep;
		this.attributeCode = attributeCode;
		this.equName = equName;
		this.installationCode = installationCode;
		this.installationDesc = installationDesc;
		this.belongSystem = belongSystem;
		this.locationCode = locationCode;
		this.locationDesc = locationDesc;
		this.likelyReason = likelyReason;
		this.findBy = findBy;
		this.findDate = findDate;
		this.findDept = findDept;
		this.woPriority = woPriority;
		this.writeBy = writeBy;
		this.writeDept = writeDept;
		this.writeDate = writeDate;
		this.repairDept = repairDept;
		this.realrepairDept = realrepairDept;
		this.ifOpenWorkorder = ifOpenWorkorder;
		this.ifRepeat = ifRepeat;
		this.supervisor = supervisor;
		this.workFlowNo = workFlowNo;
		this.wfState = wfState;
		this.unqualifiedFailureCode = unqualifiedFailureCode;
		this.isTel = isTel;
		this.telMan = telMan;
		this.telTime = telTime;
		this.isMessage = isMessage;
		this.isCheck = isCheck;
		this.cliamDate = cliamDate;
		this.cliamBy=cliamBy;
		this.entrepriseCode = entrepriseCode;
		this.isuse = isuse;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "FAILURE_CODE", length = 15)
	public String getFailureCode() {
		return this.failureCode;
	}

	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}

	@Column(name = "WO_CODE", length = 20)
	public String getWoCode() {
		return this.woCode;
	}

	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}

	@Column(name = "BUG_CODE", length = 30)
	public String getBugCode() {
		return this.bugCode;
	}

	public void setBugCode(String bugCode) {
		this.bugCode = bugCode;
	}

	@Column(name = "FAILURETYPE_CODE", length = 20)
	public String getFailuretypeCode() {
		return this.failuretypeCode;
	}

	public void setFailuretypeCode(String failuretypeCode) {
		this.failuretypeCode = failuretypeCode;
	}

	@Column(name = "FAILURE_LEVEL", length = 20)
	public String getFailureLevel() {
		return this.failureLevel;
	}

	public void setFailureLevel(String failureLevel) {
		this.failureLevel = failureLevel;
	}

	@Column(name = "WO_STATUS", length = 20)
	public String getWoStatus() {
		return this.woStatus;
	}

	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}

	@Column(name = "PRE_CONTENT", length = 1000)
	public String getPreContent() {
		return this.preContent;
	}

	public void setPreContent(String preContent) {
		this.preContent = preContent;
	}

	@Column(name = "FAILURE_CONTENT", length = 1000)
	public String getFailureContent() {
		return this.failureContent;
	}

	public void setFailureContent(String failureContent) {
		this.failureContent = failureContent;
	}

	@Column(name = "IF_STOP_RUN", length = 1)
	public String getIfStopRun() {
		return this.ifStopRun;
	}

	public void setIfStopRun(String ifStopRun) {
		this.ifStopRun = ifStopRun;
	}

	@Column(name = "RUN_PROFESSION", length = 10)
	public String getRunProfession() {
		return this.runProfession;
	}

	public void setRunProfession(String runProfession) {
		this.runProfession = runProfession;
	}

	@Column(name = "DOMINATION_PROFESSION", length = 10)
	public String getDominationProfession() {
		return this.dominationProfession;
	}

	public void setDominationProfession(String dominationProfession) {
		this.dominationProfession = dominationProfession;
	}

	@Column(name = "REPAIR_DEP", length = 10)
	public String getRepairDep() {
		return this.repairDep;
	}

	public void setRepairDep(String repairDep) {
		this.repairDep = repairDep;
	}

	@Column(name = "ATTRIBUTE_CODE", length = 30)
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@Column(name = "EQU_NAME", length = 100)
	public String getEquName() {
		return this.equName;
	}

	public void setEquName(String equName) {
		this.equName = equName;
	}

	@Column(name = "INSTALLATION_CODE", length = 30)
	public String getInstallationCode() {
		return this.installationCode;
	}

	public void setInstallationCode(String installationCode) {
		this.installationCode = installationCode;
	}

	@Column(name = "INSTALLATION_DESC", length = 100)
	public String getInstallationDesc() {
		return this.installationDesc;
	}

	public void setInstallationDesc(String installationDesc) {
		this.installationDesc = installationDesc;
	}

	@Column(name = "BELONG_SYSTEM", length = 20)
	public String getBelongSystem() {
		return this.belongSystem;
	}

	public void setBelongSystem(String belongSystem) {
		this.belongSystem = belongSystem;
	}

	@Column(name = "LOCATION_CODE", length = 30)
	public String getLocationCode() {
		return this.locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	@Column(name = "LOCATION_DESC", length = 100)
	public String getLocationDesc() {
		return this.locationDesc;
	}

	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}

	@Column(name = "LIKELY_REASON", length = 200)
	public String getLikelyReason() {
		return this.likelyReason;
	}

	public void setLikelyReason(String likelyReason) {
		this.likelyReason = likelyReason;
	}

	@Column(name = "FIND_BY", length = 16)
	public String getFindBy() {
		return this.findBy;
	}

	public void setFindBy(String findBy) {
		this.findBy = findBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FIND_DATE", length = 7)
	public Date getFindDate() {
		return this.findDate;
	}

	public void setFindDate(Date findDate) {
		this.findDate = findDate;
	}

	@Column(name = "FIND_DEPT", length = 10)
	public String getFindDept() {
		return this.findDept;
	}

	public void setFindDept(String findDept) {
		this.findDept = findDept;
	}

	@Column(name = "WO_PRIORITY", precision = 4, scale = 0)
	public Long getWoPriority() {
		return this.woPriority;
	}

	public void setWoPriority(Long woPriority) {
		this.woPriority = woPriority;
	}

	@Column(name = "WRITE_BY", length = 16)
	public String getWriteBy() {
		return this.writeBy;
	}

	public void setWriteBy(String writeBy) {
		this.writeBy = writeBy;
	}

	@Column(name = "WRITE_DEPT", length = 10)
	public String getWriteDept() {
		return this.writeDept;
	}

	public void setWriteDept(String writeDept) {
		this.writeDept = writeDept;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "WRITE_DATE", length = 7)
	public Date getWriteDate() {
		return this.writeDate;
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

	@Column(name = "REPAIR_DEPT", length = 20)
	public String getRepairDept() {
		return this.repairDept;
	}

	public void setRepairDept(String repairDept) {
		this.repairDept = repairDept;
	}

	@Column(name = "REALREPAIR_DEPT", length = 20)
	public String getRealrepairDept() {
		return this.realrepairDept;
	}

	public void setRealrepairDept(String realrepairDept) {
		this.realrepairDept = realrepairDept;
	}

	@Column(name = "IF_OPEN_WORKORDER", length = 1)
	public String getIfOpenWorkorder() {
		return this.ifOpenWorkorder;
	}

	public void setIfOpenWorkorder(String ifOpenWorkorder) {
		this.ifOpenWorkorder = ifOpenWorkorder;
	}

	@Column(name = "IF_REPEAT", length = 1)
	public String getIfRepeat() {
		return this.ifRepeat;
	}

	public void setIfRepeat(String ifRepeat) {
		this.ifRepeat = ifRepeat;
	}

	@Column(name = "SUPERVISOR", length = 16)
	public String getSupervisor() {
		return this.supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	@Column(name = "WORK_FLOW_NO", length = 36)
	public String getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "WF_STATE", length = 30)
	public String getWfState() {
		return this.wfState;
	}

	public void setWfState(String wfState) {
		this.wfState = wfState;
	}

	@Column(name = "UNQUALIFIED_FAILURE_CODE", length = 15)
	public String getUnqualifiedFailureCode() {
		return this.unqualifiedFailureCode;
	}

	public void setUnqualifiedFailureCode(String unqualifiedFailureCode) {
		this.unqualifiedFailureCode = unqualifiedFailureCode;
	}

	@Column(name = "IS_TEL", length = 1)
	public String getIsTel() {
		return this.isTel;
	}

	public void setIsTel(String isTel) {
		this.isTel = isTel;
	}

	@Column(name = "TEL_MAN", length = 16)
	public String getTelMan() {
		return this.telMan;
	}

	public void setTelMan(String telMan) {
		this.telMan = telMan;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TEL_TIME", length = 7)
	public Date getTelTime() {
		return this.telTime;
	}

	public void setTelTime(Date telTime) {
		this.telTime = telTime;
	}

	@Column(name = "IS_MESSAGE", length = 1)
	public String getIsMessage() {
		return this.isMessage;
	}

	public void setIsMessage(String isMessage) {
		this.isMessage = isMessage;
	}

	@Column(name = "IS_CHECK", length = 1)
	public String getIsCheck() {
		return this.isCheck;
	}

	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CLIAM_DATE", length = 7)
	public Date getCliamDate() {
		return this.cliamDate;
	}

	public void setCliamDate(Date cliamDate) {
		this.cliamDate = cliamDate;
	}

	@Column(name = "ENTREPRISE_CODE", length = 20)
	public String getEntrepriseCode() {
		return this.entrepriseCode;
	}

	public void setEntrepriseCode(String entrepriseCode) {
		this.entrepriseCode = entrepriseCode;
	}

	@Column(name = "ISUSE", length = 1)
	public String getIsuse() {
		return this.isuse;
	}

	public void setIsuse(String isuse) {
		this.isuse = isuse;
	}
	@Column(name = "CLIAM_BY", length = 16)
	public String getCliamBy() {
		return cliamBy;
	}

	public void setCliamBy(String cliamBy) {
		this.cliamBy = cliamBy;
	}

	@Column(name = "IS_SEND", length = 1)
	public String getIsSend() {
		return isSend;
	}

	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}
}