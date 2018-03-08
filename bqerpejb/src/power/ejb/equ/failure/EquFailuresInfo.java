package power.ejb.equ.failure;

import java.util.Date;

@SuppressWarnings("serial")
public class EquFailuresInfo implements java.io.Serializable {
	//id
	private Long id;
	//缺陷编号
	private String failureCode;
	//工单编号
	private String woCode;
	//故障代码
	private String bugCode;
	//故障名称
	private String bugName;
	//缺陷类别编码
	private String failuretypeCode;
	//缺陷类别名称
	private String failuretypeName;
	//缺陷级别
	private String failureLevel;
	//缺陷优先级
	private String failurePri;
	//缺陷状态
	private String woStatus;
	//缺陷状态名称
	private String woStatusName;
	//未消除前措施
	private String preContent;
	//缺陷内容
	private String failureContent;
	//是否限制运行
	private String ifStopRun;
	//是否限制运行说明
	private String ifStopRunName;
	//运行专业
	private String runProfession;
	//运行专业名称
	private String runProfessionName;
	//管辖专业
	private String dominationProfession;
	//管辖专业名称
	private String dominationProfessionName;
	//检修部门
	private String repairDep;
	//检修部门名称
	private String repairDepName;
	//设备功能码
	private String attributeCode;
	//设备名称
	private String equName;
	//安装点标识
	private String installationCode;
	//安装点描述
	private String installationDesc;
	//所属系统
	private String belongSystem;
	//所属系统名称
	private String belongSystemName;
	//位置编码
	private String locationCode;
	//位置描述
	private String locationDesc;
	//可能原因
	private String likelyReason;
	//发现人
	private String findBy;
	//发现人名称
	private String findByName;
	//发现日期
	private String findDate;
	//发现部门
	private String findDept;
	//发现部门名称
	private String findDeptName;
	//工单优先级
	private Long woPriority;
	//填写人
	private String writeBy;
	//填写人名称
	private String writeByName;
	//填写人所在班组
	private String writeDept;
	//填写人所在班组名称
	private String writeDeptName;
	//填写日期
	private String writeDate;
	//班组
	private String repairDept;
	//班组名称
	private String repairDeptName;
	//实际检修班组
	private String realrepairDept;
	//实际检修班组名称
	private String realrepairDeptName;
	//是否需开工单
	private String ifOpenWorkorder;
	private String ifOpenWorkorderName;
	//是否重复发生的缺陷
	private String ifRepeat;
	private String ifRepeatName;
	//工作负责人
	private String supervisor;
	//工作负责人名称
	private String supervisorName;
	//工作流编号
	private String workFlowNo;
	//工作流状态
	private String wfState;
	//不合格缺陷编号
	private String unqualifiedFailureCode;
	//是否电话通知
	private String isTel;
	//电话通知人
	private String telMan;
	//电话通知人名称
	private String telManName;
	//电话通知时间
	private String telTime;
	//是否短信通知
	private String isMessage;
	//是否点检产生
	private String isCheck;
	//缺陷认领日期
	private String cliamDate;
	//缺陷认领人
	private String cliamBy;
	//缺陷认领人名称
	private String cliamByName;
	//企业编码
	private String entrepriseCode;
	//是否使用
	private String isuse;
	//消缺时限
	private String toEliminDate;
	private String isOverTime;
	
	//分组名称
	private String groupName;
	private String delayDate;
	
	
	// 是否上报
	private String isSend;
	
	public String getIsSend() {
		return isSend;
	}
	public void setIsSend(String isSend) {
		this.isSend = isSend;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFailureCode() {
		return failureCode;
	}
	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}
	public String getBugCode() {
		return bugCode;
	}
	public void setBugCode(String bugCode) {
		this.bugCode = bugCode;
	}
	public String getBugName() {
		return bugName;
	}
	public void setBugName(String bugName) {
		this.bugName = bugName;
	}
	public String getFailuretypeCode() {
		return failuretypeCode;
	}
	public void setFailuretypeCode(String failuretypeCode) {
		this.failuretypeCode = failuretypeCode;
	}
	public String getFailuretypeName() {
		return failuretypeName;
	}
	public void setFailuretypeName(String failuretypeName) {
		this.failuretypeName = failuretypeName;
	}
	public String getFailureLevel() {
		return failureLevel;
	}
	public void setFailureLevel(String failureLevel) {
		this.failureLevel = failureLevel;
	}
	public String getFailurePri() {
		return failurePri;
	}
	public void setFailurePri(String failurePri) {
		this.failurePri = failurePri;
	}
	public String getWoStatus() {
		return woStatus;
	}
	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}
	public String getPreContent() {
		return preContent;
	}
	public void setPreContent(String preContent) {
		this.preContent = preContent;
	}
	public String getFailureContent() {
		return failureContent;
	}
	public void setFailureContent(String failureContent) {
		this.failureContent = failureContent;
	}
	public String getIfStopRun() {
		return ifStopRun;
	}
	public void setIfStopRun(String ifStopRun) {
		this.ifStopRun = ifStopRun;
	}
	public String getRunProfession() {
		return runProfession;
	}
	public void setRunProfession(String runProfession) {
		this.runProfession = runProfession;
	}
	public String getRunProfessionName() {
		return runProfessionName;
	}
	public void setRunProfessionName(String runProfessionName) {
		this.runProfessionName = runProfessionName;
	}
	public String getDominationProfession() {
		return dominationProfession;
	}
	public void setDominationProfession(String dominationProfession) {
		this.dominationProfession = dominationProfession;
	}
	public String getDominationProfessionName() {
		return dominationProfessionName;
	}
	public void setDominationProfessionName(String dominationProfessionName) {
		this.dominationProfessionName = dominationProfessionName;
	}
	public String getRepairDep() {
		return repairDep;
	}
	public void setRepairDep(String repairDep) {
		this.repairDep = repairDep;
	}
	public String getRepairDepName() {
		return repairDepName;
	}
	public void setRepairDepName(String repairDepName) {
		this.repairDepName = repairDepName;
	}
	public String getAttributeCode() {
		return attributeCode;
	}
	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public String getBelongSystem() {
		return belongSystem;
	}
	public void setBelongSystem(String belongSystem) {
		this.belongSystem = belongSystem;
	}
	public String getBelongSystemName() {
		return belongSystemName;
	}
	public void setBelongSystemName(String belongSystemName) {
		this.belongSystemName = belongSystemName;
	}
	public String getLikelyReason() {
		return likelyReason;
	}
	public void setLikelyReason(String likelyReason) {
		this.likelyReason = likelyReason;
	}
	public String getFindBy() {
		return findBy;
	}
	public void setFindBy(String findBy) {
		this.findBy = findBy;
	}
	public String getFindByName() {
		return findByName;
	}
	public void setFindByName(String findByName) {
		this.findByName = findByName;
	}
	public String getFindDate() {
		return findDate;
	}
	public void setFindDate(String findDate) {
		this.findDate = findDate;
	}
	public String getFindDept() {
		return findDept;
	}
	public void setFindDept(String findDept) {
		this.findDept = findDept;
	}
	public String getFindDeptName() {
		return findDeptName;
	}
	public void setFindDeptName(String findDeptName) {
		this.findDeptName = findDeptName;
	}
	public String getWriteBy() {
		return writeBy;
	}
	public void setWriteBy(String writeBy) {
		this.writeBy = writeBy;
	}
	public String getWriteByName() {
		return writeByName;
	}
	public void setWriteByName(String writeByName) {
		this.writeByName = writeByName;
	}
	public String getWriteDept() {
		return writeDept;
	}
	public void setWriteDept(String writeDept) {
		this.writeDept = writeDept;
	}
	public String getWriteDeptName() {
		return writeDeptName;
	}
	public void setWriteDeptName(String writeDeptName) {
		this.writeDeptName = writeDeptName;
	}
	public String getWriteDate() {
		return writeDate;
	}
	public void setWriteDate(String writeDate) {
		this.writeDate = writeDate;
	}
	public String getRepairDept() {
		return repairDept;
	}
	public void setRepairDept(String repairDept) {
		this.repairDept = repairDept;
	}
	public String getRepairDeptName() {
		return repairDeptName;
	}
	public void setRepairDeptName(String repairDeptName) {
		this.repairDeptName = repairDeptName;
	}
	public String getRealrepairDept() {
		return realrepairDept;
	}
	public void setRealrepairDept(String realrepairDept) {
		this.realrepairDept = realrepairDept;
	}
	public String getRealrepairDeptName() {
		return realrepairDeptName;
	}
	public void setRealrepairDeptName(String realrepairDeptName) {
		this.realrepairDeptName = realrepairDeptName;
	}
	public String getIfOpenWorkorder() {
		return ifOpenWorkorder;
	}
	public void setIfOpenWorkorder(String ifOpenWorkorder) {
		this.ifOpenWorkorder = ifOpenWorkorder;
	}
	public String getIfRepeat() {
		return ifRepeat;
	}
	public void setIfRepeat(String ifRepeat) {
		this.ifRepeat = ifRepeat;
	}
	public String getSupervisor() {
		return supervisor;
	}
	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}
	public String getSupervisorName() {
		return supervisorName;
	}
	public void setSupervisorName(String supervisorName) {
		this.supervisorName = supervisorName;
	}
	public String getWorkFlowNo() {
		return workFlowNo;
	}
	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
	}
	public String getWfState() {
		return wfState;
	}
	public void setWfState(String wfState) {
		this.wfState = wfState;
	}
	public String getUnqualifiedFailureCode() {
		return unqualifiedFailureCode;
	}
	public void setUnqualifiedFailureCode(String unqualifiedFailureCode) {
		this.unqualifiedFailureCode = unqualifiedFailureCode;
	}
	public String getIsTel() {
		return isTel;
	}
	public void setIsTel(String isTel) {
		this.isTel = isTel;
	}
	public String getTelMan() {
		return telMan;
	}
	public void setTelMan(String telMan) {
		this.telMan = telMan;
	}
	public String getTelManName() {
		return telManName;
	}
	public void setTelManName(String telManName) {
		this.telManName = telManName;
	}
	public String getTelTime() {
		return telTime;
	}
	public void setTelTime(String telTime) {
		this.telTime = telTime;
	}
	public String getIsMessage() {
		return isMessage;
	}
	public void setIsMessage(String isMessage) {
		this.isMessage = isMessage;
	}
	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	public String getCliamDate() {
		return cliamDate;
	}
	public void setCliamDate(String cliamDate) {
		this.cliamDate = cliamDate;
	}
	public String getEntrepriseCode() {
		return entrepriseCode;
	}
	public void setEntrepriseCode(String entrepriseCode) {
		this.entrepriseCode = entrepriseCode;
	}
	public String getIsuse() {
		return isuse;
	}
	public void setIsuse(String isuse) {
		this.isuse = isuse;
	}
	public String getWoStatusName() {
		return woStatusName;
	}
	public void setWoStatusName(String woStatusName) {
		this.woStatusName = woStatusName;
	}
	public String getLocationCode() {
		return locationCode;
	}
	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}
	public String getLocationDesc() {
		return locationDesc;
	}
	public void setLocationDesc(String locationDesc) {
		this.locationDesc = locationDesc;
	}
	public String getWoCode() {
		return woCode;
	}
	public void setWoCode(String woCode) {
		this.woCode = woCode;
	}
	public String getIfStopRunName() {
		return ifStopRunName;
	}
	public void setIfStopRunName(String ifStopRunName) {
		this.ifStopRunName = ifStopRunName;
	}
	public String getInstallationCode() {
		return installationCode;
	}
	public void setInstallationCode(String installationCode) {
		this.installationCode = installationCode;
	}
	public String getInstallationDesc() {
		return installationDesc;
	}
	public void setInstallationDesc(String installationDesc) {
		this.installationDesc = installationDesc;
	}
	public Long getWoPriority() {
		return woPriority;
	}
	public void setWoPriority(Long woPriority) {
		this.woPriority = woPriority;
	}
	public String getIfOpenWorkorderName() {
		return ifOpenWorkorderName;
	}
	public void setIfOpenWorkorderName(String ifOpenWorkorderName) {
		this.ifOpenWorkorderName = ifOpenWorkorderName;
	}
	public String getIfRepeatName() {
		return ifRepeatName;
	}
	public void setIfRepeatName(String ifRepeatName) {
		this.ifRepeatName = ifRepeatName;
	}
	public String getCliamBy() {
		return cliamBy;
	}
	public void setCliamBy(String cliamBy) {
		this.cliamBy = cliamBy;
	}
	public String getCliamByName() {
		return cliamByName;
	}
	public void setCliamByName(String cliamByName) {
		this.cliamByName = cliamByName;
	}
	public String getToEliminDate() {
		return toEliminDate;
	}
	public void setToEliminDate(String toEliminDate) {
		this.toEliminDate = toEliminDate;
	}
	public String getIsOverTime() {
		return isOverTime;
	}
	public void setIsOverTime(String isOverTime) {
		this.isOverTime = isOverTime;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getDelayDate() {
		return delayDate;
	}
	public void setDelayDate(String delayDate) {
		this.delayDate = delayDate;
	}


}
