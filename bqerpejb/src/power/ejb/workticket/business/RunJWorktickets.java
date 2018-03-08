package power.ejb.workticket.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * RunJWorktickets entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "RUN_J_WORKTICKETS")
public class RunJWorktickets implements java.io.Serializable {

	private String workticketNo;
	private String chargeBy;
	private String chargeDept;
	private Long memberCount;
	private String members;
	private String workticketTypeCode;
	private Long sourceId;
	private String conditionName;// 修改
	private Long firelevelId;
	private String workticketContent;
	private String repairSpecailCode;
	private String equAttributeCode;
	private String watcher;
	private String isEmergency;
	private Date planStartDate;
	private Date planEndDate;
	private String entryBy;
	private String entryDept;
	private Date entryDate;
	private Date actualStartDate;
	private Date actualEndDate;
	private Date approvedFinishDate;
	private String refWorkticketNo;
	private String fireticketExecuteBy;
	private String fireticketFireman;
	private String permissionDept;
	private String workticketMemo;
	private String printFlag;
	private Long dangerType;
	private String dangerCondition;
	private Long workFlowNo;
	private Long wfState;
	private Long saveExeStatusId;
	private Long workticketStausId;
	private String enterpriseCode;
	private String isUse;
	private String isStandard;
	private String isCreatebyStand;
	private String locationName;// 位置名称
	private String isNeedMeasure;// 是否需要测量
	private String failureCode;// 缺陷单号
	private String woCode;// 工单号
	private String autoDeviceName;// 需要退出热工保护或自动装置名称
	private String applyNo;// 申请单号
	private Date approvedStartDate;// 批准开始时间
	private String mainEquCode;//主设备编码
	private String mainEquName;//主设备名称

	// Constructors

	/** default constructor */
	public RunJWorktickets() {
	}

	/** minimal constructor */
	public RunJWorktickets(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	/** full constructor */
	public RunJWorktickets(String workticketNo, String chargeBy,
			String chargeDept, Long memberCount, String members,
			String workticketTypeCode, Long sourceId, String conditionName,
			Long firelevelId, String workticketContent,
			String repairSpecailCode, String equAttributeCode, String watcher,
			String isEmergency, Date planStartDate, Date planEndDate,
			String entryBy, String entryDept, Date entryDate,
			Date actualStartDate, Date actualEndDate, Date approvedFinishDate,
			String refWorkticketNo, String fireticketExecuteBy,
			String fireticketFireman, String permissionDept,
			String workticketMemo, String printFlag, Long dangerType,
			String dangerCondition, Long workFlowNo, Long wfState,
			Long saveExeStatusId, Long workticketStausId,
			String enterpriseCode, String isUse, String isStandard,
			String isCreatebyStand, String locationName, String isNeedMeasure,
			String failureCode, String woCode, String autoDeviceName,
			String applyNo, Date approvedStartDate) {
		this.workticketNo = workticketNo;
		this.chargeBy = chargeBy;
		this.chargeDept = chargeDept;
		this.memberCount = memberCount;
		this.members = members;
		this.workticketTypeCode = workticketTypeCode;
		this.sourceId = sourceId;
		this.conditionName = conditionName;
		this.firelevelId = firelevelId;
		this.workticketContent = workticketContent;
		this.repairSpecailCode = repairSpecailCode;
		this.equAttributeCode = equAttributeCode;
		this.watcher = watcher;
		this.isEmergency = isEmergency;
		this.planStartDate = planStartDate;
		this.planEndDate = planEndDate;
		this.entryBy = entryBy;
		this.entryDept = entryDept;
		this.entryDate = entryDate;
		this.actualStartDate = actualStartDate;
		this.actualEndDate = actualEndDate;
		this.approvedFinishDate = approvedFinishDate;
		this.refWorkticketNo = refWorkticketNo;
		this.fireticketExecuteBy = fireticketExecuteBy;
		this.fireticketFireman = fireticketFireman;
		this.permissionDept = permissionDept;
		this.workticketMemo = workticketMemo;
		this.printFlag = printFlag;
		this.dangerType = dangerType;
		this.dangerCondition = dangerCondition;
		this.workFlowNo = workFlowNo;
		this.wfState = wfState;
		this.saveExeStatusId = saveExeStatusId;
		this.workticketStausId = workticketStausId;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.isStandard = isStandard;
		this.isCreatebyStand = isCreatebyStand;
		this.locationName = locationName;
		this.isNeedMeasure = isNeedMeasure;
		this.failureCode = failureCode;
		this.woCode = woCode;
		this.autoDeviceName = autoDeviceName;
		this.applyNo = applyNo;
		this.approvedStartDate = approvedStartDate;
	}

	// Property accessors
	@Id
	@Column(name = "WORKTICKET_NO", unique = true, nullable = false, length = 22)
	public String getWorkticketNo() {
		return this.workticketNo;
	}

	public void setWorkticketNo(String workticketNo) {
		this.workticketNo = workticketNo;
	}

	@Column(name = "CHARGE_BY", length = 30)
	public String getChargeBy() {
		return this.chargeBy;
	}

	public void setChargeBy(String chargeBy) {
		this.chargeBy = chargeBy;
	}

	@Column(name = "CHARGE_DEPT", length = 20)
	public String getChargeDept() {
		return this.chargeDept;
	}

	public void setChargeDept(String chargeDept) {
		this.chargeDept = chargeDept;
	}

	@Column(name = "MEMBER_COUNT", precision = 10, scale = 0)
	public Long getMemberCount() {
		return this.memberCount;
	}

	public void setMemberCount(Long memberCount) {
		this.memberCount = memberCount;
	}

	@Column(name = "MEMBERS", length = 300)
	public String getMembers() {
		return this.members;
	}

	public void setMembers(String members) {
		this.members = members;
	}

	@Column(name = "WORKTICKET_TYPE_CODE", length = 1)
	public String getWorkticketTypeCode() {
		return this.workticketTypeCode;
	}

	public void setWorkticketTypeCode(String workticketTypeCode) {
		this.workticketTypeCode = workticketTypeCode;
	}

	@Column(name = "SOURCE_ID", precision = 10, scale = 0)
	public Long getSourceId() {
		return this.sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	@Column(name = "CONDITION_NAME", length = 100)
	public String getConditionName() {
		return this.conditionName;
	}

	public void setConditionName(String conditionName) {
		this.conditionName = conditionName;
	}

	@Column(name = "FIRELEVEL_ID", precision = 10, scale = 0)
	public Long getFirelevelId() {
		return this.firelevelId;
	}

	public void setFirelevelId(Long firelevelId) {
		this.firelevelId = firelevelId;
	}

	@Column(name = "WORKTICKET_CONTENT", length = 4000)
	public String getWorkticketContent() {
		return this.workticketContent;
	}

	public void setWorkticketContent(String workticketContent) {
		this.workticketContent = workticketContent;
	}

	@Column(name = "REPAIR_SPECAIL_CODE", length = 10)
	public String getRepairSpecailCode() {
		return this.repairSpecailCode;
	}

	public void setRepairSpecailCode(String repairSpecailCode) {
		this.repairSpecailCode = repairSpecailCode;
	}

	@Column(name = "EQU_ATTRIBUTE_CODE", length = 30)
	public String getEquAttributeCode() {
		return this.equAttributeCode;
	}

	public void setEquAttributeCode(String equAttributeCode) {
		this.equAttributeCode = equAttributeCode;
	}

	@Column(name = "WATCHER", length = 30)
	public String getWatcher() {
		return this.watcher;
	}

	public void setWatcher(String watcher) {
		this.watcher = watcher;
	}

	@Column(name = "IS_EMERGENCY", length = 1)
	public String getIsEmergency() {
		return this.isEmergency;
	}

	public void setIsEmergency(String isEmergency) {
		this.isEmergency = isEmergency;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PLAN_START_DATE", length = 7)
	public Date getPlanStartDate() {
		return this.planStartDate;
	}

	public void setPlanStartDate(Date planStartDate) {
		this.planStartDate = planStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PLAN_END_DATE", length = 7)
	public Date getPlanEndDate() {
		return this.planEndDate;
	}

	public void setPlanEndDate(Date planEndDate) {
		this.planEndDate = planEndDate;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Column(name = "ENTRY_DEPT", length = 20)
	public String getEntryDept() {
		return this.entryDept;
	}

	public void setEntryDept(String entryDept) {
		this.entryDept = entryDept;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTUAL_START_DATE", length = 7)
	public Date getActualStartDate() {
		return this.actualStartDate;
	}

	public void setActualStartDate(Date actualStartDate) {
		this.actualStartDate = actualStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ACTUAL_END_DATE", length = 7)
	public Date getActualEndDate() {
		return this.actualEndDate;
	}

	public void setActualEndDate(Date actualEndDate) {
		this.actualEndDate = actualEndDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVED_FINISH_DATE", length = 7)
	public Date getApprovedFinishDate() {
		return this.approvedFinishDate;
	}

	public void setApprovedFinishDate(Date approvedFinishDate) {
		this.approvedFinishDate = approvedFinishDate;
	}

	@Column(name = "REF_WORKTICKET_NO", length = 22)
	public String getRefWorkticketNo() {
		return this.refWorkticketNo;
	}

	public void setRefWorkticketNo(String refWorkticketNo) {
		this.refWorkticketNo = refWorkticketNo;
	}

	@Column(name = "FIRETICKET_EXECUTE_BY", length = 30)
	public String getFireticketExecuteBy() {
		return this.fireticketExecuteBy;
	}

	public void setFireticketExecuteBy(String fireticketExecuteBy) {
		this.fireticketExecuteBy = fireticketExecuteBy;
	}

	@Column(name = "FIRETICKET_FIREMAN", length = 30)
	public String getFireticketFireman() {
		return this.fireticketFireman;
	}

	public void setFireticketFireman(String fireticketFireman) {
		this.fireticketFireman = fireticketFireman;
	}

	@Column(name = "PERMISSION_DEPT", length = 20)
	public String getPermissionDept() {
		return this.permissionDept;
	}

	public void setPermissionDept(String permissionDept) {
		this.permissionDept = permissionDept;
	}

	@Column(name = "WORKTICKET_MEMO", length = 300)
	public String getWorkticketMemo() {
		return this.workticketMemo;
	}

	public void setWorkticketMemo(String workticketMemo) {
		this.workticketMemo = workticketMemo;
	}

	@Column(name = "PRINT_FLAG", length = 1)
	public String getPrintFlag() {
		return this.printFlag;
	}

	public void setPrintFlag(String printFlag) {
		this.printFlag = printFlag;
	}

	@Column(name = "DANGER_TYPE", precision = 10, scale = 0)
	public Long getDangerType() {
		return this.dangerType;
	}

	public void setDangerType(Long dangerType) {
		this.dangerType = dangerType;
	}

	@Column(name = "DANGER_CONDITION")
	public String getDangerCondition() {
		return this.dangerCondition;
	}

	public void setDangerCondition(String dangerCondition) {
		this.dangerCondition = dangerCondition;
	}

	@Column(name = "WORK_FLOW_NO", precision = 22, scale = 0)
	public Long getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "WF_STATE", precision = 11, scale = 0)
	public Long getWfState() {
		return this.wfState;
	}

	public void setWfState(Long wfState) {
		this.wfState = wfState;
	}

	@Column(name = "SAVE_EXE_STATUS_ID", precision = 10, scale = 0)
	public Long getSaveExeStatusId() {
		return this.saveExeStatusId;
	}

	public void setSaveExeStatusId(Long saveExeStatusId) {
		this.saveExeStatusId = saveExeStatusId;
	}

	@Column(name = "WORKTICKET_STAUS_ID", precision = 10, scale = 0)
	public Long getWorkticketStausId() {
		return this.workticketStausId;
	}

	public void setWorkticketStausId(Long workticketStausId) {
		this.workticketStausId = workticketStausId;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "IS_STANDARD", length = 1)
	public String getIsStandard() {
		return this.isStandard;
	}

	public void setIsStandard(String isStandard) {
		this.isStandard = isStandard;
	}

	@Column(name = "IS_CREATEBY_STAND", length = 1)
	public String getIsCreatebyStand() {
		return this.isCreatebyStand;
	}

	public void setIsCreatebyStand(String isCreatebyStand) {
		this.isCreatebyStand = isCreatebyStand;
	}

	@Column(name = "LOCATION_NAME", length = 200)
	public String getLocationName() {
		return this.locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	@Column(name = "IS_NEED_MEASURE", length = 1)
	public String getIsNeedMeasure() {
		return this.isNeedMeasure;
	}

	public void setIsNeedMeasure(String isNeedMeasure) {
		this.isNeedMeasure = isNeedMeasure;
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

	@Column(name = "AUTO_DEVICE_NAME", length = 100)
	public String getAutoDeviceName() {
		return this.autoDeviceName;
	}

	public void setAutoDeviceName(String autoDeviceName) {
		this.autoDeviceName = autoDeviceName;
	}

	@Column(name = "APPLY_NO", length = 15)
	public String getApplyNo() {
		return this.applyNo;
	}

	public void setApplyNo(String applyNo) {
		this.applyNo = applyNo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPROVED_START_DATE", length = 7)
	public Date getApprovedStartDate() {
		return this.approvedStartDate;
	}

	public void setApprovedStartDate(Date approvedStartDate) {
		this.approvedStartDate = approvedStartDate;
	}
	@Column(name = "main_equ_code", length = 30)
	public String getMainEquCode() {
		return mainEquCode;
	}

	public void setMainEquCode(String mainEquCode) {
		this.mainEquCode = mainEquCode;
	}
	@Column(name = "main_equ_name", length = 100)
	public String getMainEquName() {
		return mainEquName;
	}

	public void setMainEquName(String mainEquName) {
		this.mainEquName = mainEquName;
	}

}