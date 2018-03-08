package power.ejb.equ.failure;

import java.util.Date;

@SuppressWarnings("serial")
public class EquFailuresQueryForm implements java.io.Serializable{
//缺陷综合统计
	private String queryType;
	private String queryTypeName;
	private String count;

	//缺陷编号
	private String failureCode;
	//缺陷状态
	private String woStatus;
	//缺陷状态名称
	private String woStatusName;
	//缺陷内容
	private String failureContent;
	//故障名称
	private String bugName;
	//设备功能码
	private String attributeCode;
	//设备名称
	private String equName;
	//检修部门
	private String repairDep;
	//检修部门名称
	private String repairDepName;
	//发现人
	private String findBy;
	//发现人名称
	private String findByName;
	//发现日期
	private String findDate;
	//消缺方式
	private String eliminateType;
	//缺陷类别名称
	private String failuretypeName;
	//消缺时间
	private String overtime;
	//消缺方式名称
	private String elininateTypeName;
	//消缺班组
	private String eliminateClass;
	//消缺班组名称
	private String eliminateClassName;
	//审批时间
	private String approveTime;
	//审批人
	private String approvePeople;
	//审批人姓名
	private String approvePeopleName;
	
	//填写人
	private String writeBy;
	//填写人名称
	private String writeByName;
	//填写人所在班组
	private String writeDept;
	//填写人所在班组名称
	private String writeDeptName;
	
	//消缺数
	private String eliminateCount;
	//消缺率
	private String eliminateRate;
	//待处理数
	private String awaitCount;
	//未消缺数
	private String noeliminateCount;
	//缺陷重复发生数
	private String repeatCount;
	//缺陷退回数
	private String backCount;
	
	//所属系统
	private String belongSystem;
	//所属系统名称
	private String belongSystemName;
	
	//审批意见
	private String approveOpinion;
	//管辖专业
	private String dominationProfession;
	//管辖专业名称
	private String dominationProfessionName;
	//待处理类型
	private String awaitType;
	//检修部门主管待处理意见
	private String repairOpinion;
	//生技部待处理意见
	private String equOpinion;
	//值长待处理意见
	private String leaderOpinion;
	//超时缺陷数ltong
	private String overTime;
	//缺陷及时率ltong
	private String failureInTime;
	//缺陷及总数ltong
	private String totalNum;
	//年度统计报表
	private String m1count;
	private String m2count;
	private String m3count;
	private String m4count;
	private String m5count;
	private String m6count;
	private String m7count;
	private String m8count;
	private String m9count;
	private String m10count;
	private String m11count;
	private String m12count;
	private String type;
	
	private Long id;
	private String workFlowNo;
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
	public String getAwaitType() {
		return awaitType;
	}
	public void setAwaitType(String awaitType) {
		this.awaitType = awaitType;
	}
	public String getRepairOpinion() {
		return repairOpinion;
	}
	public void setRepairOpinion(String repairOpinion) {
		this.repairOpinion = repairOpinion;
	}
	public String getEquOpinion() {
		return equOpinion;
	}
	public void setEquOpinion(String equOpinion) {
		this.equOpinion = equOpinion;
	}
	public String getLeaderOpinion() {
		return leaderOpinion;
	}
	public void setLeaderOpinion(String leaderOpinion) {
		this.leaderOpinion = leaderOpinion;
	}
	public String getEliminateCount() {
		return eliminateCount;
	}
	public void setEliminateCount(String eliminateCount) {
		this.eliminateCount = eliminateCount;
	}
	public String getAwaitCount() {
		return awaitCount;
	}
	public void setAwaitCount(String awaitCount) {
		this.awaitCount = awaitCount;
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
	public String getQueryType() {
		return queryType;
	}
	public void setQueryType(String queryType) {
		this.queryType = queryType;
	}
	public String getQueryTypeName() {
		return queryTypeName;
	}
	public void setQueryTypeName(String queryTypeName) {
		this.queryTypeName = queryTypeName;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getFailureCode() {
		return failureCode;
	}
	public void setFailureCode(String failureCode) {
		this.failureCode = failureCode;
	}
	public String getWoStatus() {
		return woStatus;
	}
	public void setWoStatus(String woStatus) {
		this.woStatus = woStatus;
	}
	public String getWoStatusName() {
		return woStatusName;
	}
	public void setWoStatusName(String woStatusName) {
		this.woStatusName = woStatusName;
	}
	public String getFailureContent() {
		return failureContent;
	}
	public void setFailureContent(String failureContent) {
		this.failureContent = failureContent;
	}
	public String getBugName() {
		return bugName;
	}
	public void setBugName(String bugName) {
		this.bugName = bugName;
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
	public String getEliminateType() {
		return eliminateType;
	}
	public void setEliminateType(String eliminateType) {
		this.eliminateType = eliminateType;
	}
	public String getEliminateClass() {
		return eliminateClass;
	}
	public void setEliminateClass(String eliminateClass) {
		this.eliminateClass = eliminateClass;
	}
	public String getEliminateClassName() {
		return eliminateClassName;
	}
	public void setEliminateClassName(String eliminateClassName) {
		this.eliminateClassName = eliminateClassName;
	}
	public String getApprovePeople() {
		return approvePeople;
	}
	public void setApprovePeople(String approvePeople) {
		this.approvePeople = approvePeople;
	}
	public String getElininateTypeName() {
		return elininateTypeName;
	}
	public void setElininateTypeName(String elininateTypeName) {
		this.elininateTypeName = elininateTypeName;
	}
	public String getApproveTime() {
		return approveTime;
	}
	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}
	public String getApprovePeopleName() {
		return approvePeopleName;
	}
	public void setApprovePeopleName(String approvePeopleName) {
		this.approvePeopleName = approvePeopleName;
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
	public String getEliminateRate() {
		return eliminateRate;
	}
	public void setEliminateRate(String eliminateRate) {
		this.eliminateRate = eliminateRate;
	}
	public String getApproveOpinion() {
		return approveOpinion;
	}
	public void setApproveOpinion(String approveOpinion) {
		this.approveOpinion = approveOpinion;
	}
	public String getNoeliminateCount() {
		return noeliminateCount;
	}
	public void setNoeliminateCount(String noeliminateCount) {
		this.noeliminateCount = noeliminateCount;
	}
	public String getRepeatCount() {
		return repeatCount;
	}
	public void setRepeatCount(String repeatCount) {
		this.repeatCount = repeatCount;
	}
	public String getBackCount() {
		return backCount;
	}
	public void setBackCount(String backCount) {
		this.backCount = backCount;
	}
	public String getM1count() {
		return m1count;
	}
	public void setM1count(String m1count) {
		this.m1count = m1count;
	}
	public String getM2count() {
		return m2count;
	}
	public void setM2count(String m2count) {
		this.m2count = m2count;
	}
	public String getM3count() {
		return m3count;
	}
	public void setM3count(String m3count) {
		this.m3count = m3count;
	}
	public String getM4count() {
		return m4count;
	}
	public void setM4count(String m4count) {
		this.m4count = m4count;
	}
	public String getM5count() {
		return m5count;
	}
	public void setM5count(String m5count) {
		this.m5count = m5count;
	}
	public String getM6count() {
		return m6count;
	}
	public void setM6count(String m6count) {
		this.m6count = m6count;
	}
	public String getM7count() {
		return m7count;
	}
	public void setM7count(String m7count) {
		this.m7count = m7count;
	}
	public String getM8count() {
		return m8count;
	}
	public void setM8count(String m8count) {
		this.m8count = m8count;
	}
	public String getM9count() {
		return m9count;
	}
	public void setM9count(String m9count) {
		this.m9count = m9count;
	}
	public String getM10count() {
		return m10count;
	}
	public void setM10count(String m10count) {
		this.m10count = m10count;
	}
	public String getM11count() {
		return m11count;
	}
	public void setM11count(String m11count) {
		this.m11count = m11count;
	}
	public String getM12count() {
		return m12count;
	}
	public void setM12count(String m12count) {
		this.m12count = m12count;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWorkFlowNo() {
		return workFlowNo;
	}
	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
	}
	public String getOverTime() {
		return overTime;
	}
	public void setOverTime(String overTime) {
		this.overTime = overTime;
	}
	public String getFailureInTime() {
		return failureInTime;
	}
	public void setFailureInTime(String failureInTime) {
		this.failureInTime = failureInTime;
	}
	public String getTotalNum() {
		return totalNum;
	}
	public void setTotalNum(String totalNum) {
		this.totalNum = totalNum;
	}
	public String getFailuretypeName() {
		return failuretypeName;
	}
	public void setFailuretypeName(String failuretypeName) {
		this.failuretypeName = failuretypeName;
	}
	public String getOvertime() {
		return overtime;
	}
	public void setOvertime(String overtime) {
		this.overtime = overtime;
	}
}
