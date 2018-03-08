package power.ejb.equ.failure;

import java.util.Date;

@SuppressWarnings("serial")
public class EquFailuresHisInfo implements java.io.Serializable{
	//id
	private Long id;
	//缺陷编号
	private String failureCode;
	//审批类型
	private String approveType;
	//审批类型
	private String approveTypeName;
	//分组名称
	private String groupName;
	//审批时间
	private String approveTime;
	//审批意见
	private String approveOpinion;
	//审批人
	private String approvePeople;
	//审批人名称
	private String approvePeopleName;
	//仲裁类型
	private String arbitrateType;
	//仲裁后检修部门
	private String arbitrateDept;
	//仲裁后检修部门名称
	private String arbitrateDeptName;
	//仲裁后管辖专业
	private String arbitrateProfession;
	//仲裁后管辖专业名称
	private String arbitrateProfessionName;
	//验收仲裁类别
	private String checkArbitrateType;
	//仲裁后类别
	private String arbitrateKind;
	//待处理类型
	private String awaitType;
	//申请待处理延长日期
	private String delayDate;
	//待处理审批日期
	private String awaitappoDate;
	//消缺方式
	private String eliminateType;
	//消缺班组
	private String eliminateClass;
	//消缺班组名称
	private String eliminateClassName;
	//处理结果
	private String tackleResult;
	//负责人
	private String chargeMan;
	//负责人名称
	private String chargeManName;
	//备注
	private String approveRemark;
	//值长
	private String chargerLeader;
	//值长名称
	private String chargerLeaderName;
	//现场验收人
	private String checkMan;
	//现场验收人名称
	private String checkManName;
	//验收质量
	private String checkQuality;
	//企业编码
	private String enterpriseCode;
	//是否使用
	private String isuse;
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
	public String getApproveType() {
		return approveType;
	}
	public void setApproveType(String approveType) {
		this.approveType = approveType;
	}
	public String getApproveTime() {
		return approveTime;
	}
	public void setApproveTime(String approveTime) {
		this.approveTime = approveTime;
	}
	public String getApproveOpinion() {
		return approveOpinion;
	}
	public void setApproveOpinion(String approveOpinion) {
		this.approveOpinion = approveOpinion;
	}
	public String getApprovePeople() {
		return approvePeople;
	}
	public void setApprovePeople(String approvePeople) {
		this.approvePeople = approvePeople;
	}
	public String getApprovePeopleName() {
		return approvePeopleName;
	}
	public void setApprovePeopleName(String approvePeopleName) {
		this.approvePeopleName = approvePeopleName;
	}
	public String getArbitrateType() {
		return arbitrateType;
	}
	public void setArbitrateType(String arbitrateType) {
		this.arbitrateType = arbitrateType;
	}
	public String getArbitrateDept() {
		return arbitrateDept;
	}
	public void setArbitrateDept(String arbitrateDept) {
		this.arbitrateDept = arbitrateDept;
	}
	public String getArbitrateDeptName() {
		return arbitrateDeptName;
	}
	public void setArbitrateDeptName(String arbitrateDeptName) {
		this.arbitrateDeptName = arbitrateDeptName;
	}
	public String getArbitrateProfession() {
		return arbitrateProfession;
	}
	public void setArbitrateProfession(String arbitrateProfession) {
		this.arbitrateProfession = arbitrateProfession;
	}
	public String getArbitrateProfessionName() {
		return arbitrateProfessionName;
	}
	public void setArbitrateProfessionName(String arbitrateProfessionName) {
		this.arbitrateProfessionName = arbitrateProfessionName;
	}
	public String getCheckArbitrateType() {
		return checkArbitrateType;
	}
	public void setCheckArbitrateType(String checkArbitrateType) {
		this.checkArbitrateType = checkArbitrateType;
	}
	public String getArbitrateKind() {
		return arbitrateKind;
	}
	public void setArbitrateKind(String arbitrateKind) {
		this.arbitrateKind = arbitrateKind;
	}
	public String getAwaitType() {
		return awaitType;
	}
	public void setAwaitType(String awaitType) {
		this.awaitType = awaitType;
	}
	public String getDelayDate() {
		return delayDate;
	}
	public void setDelayDate(String delayDate) {
		this.delayDate = delayDate;
	}
	public String getAwaitappoDate() {
		return awaitappoDate;
	}
	public void setAwaitappoDate(String awaitappoDate) {
		this.awaitappoDate = awaitappoDate;
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
	public String getTackleResult() {
		return tackleResult;
	}
	public void setTackleResult(String tackleResult) {
		this.tackleResult = tackleResult;
	}
	public String getChargeMan() {
		return chargeMan;
	}
	public void setChargeMan(String chargeMan) {
		this.chargeMan = chargeMan;
	}
	public String getChargeManName() {
		return chargeManName;
	}
	public void setChargeManName(String chargeManName) {
		this.chargeManName = chargeManName;
	}
	public String getApproveRemark() {
		return approveRemark;
	}
	public void setApproveRemark(String approveRemark) {
		this.approveRemark = approveRemark;
	}
	public String getChargerLeader() {
		return chargerLeader;
	}
	public void setChargerLeader(String chargerLeader) {
		this.chargerLeader = chargerLeader;
	}
	public String getChargerLeaderName() {
		return chargerLeaderName;
	}
	public void setChargerLeaderName(String chargerLeaderName) {
		this.chargerLeaderName = chargerLeaderName;
	}
	public String getCheckMan() {
		return checkMan;
	}
	public void setCheckMan(String checkMan) {
		this.checkMan = checkMan;
	}
	public String getCheckManName() {
		return checkManName;
	}
	public void setCheckManName(String checkManName) {
		this.checkManName = checkManName;
	}
	public String getCheckQuality() {
		return checkQuality;
	}
	public void setCheckQuality(String checkQuality) {
		this.checkQuality = checkQuality;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getIsuse() {
		return isuse;
	}
	public void setIsuse(String isuse) {
		this.isuse = isuse;
	}
	public String getApproveTypeName() {
		return approveTypeName;
	}
	public void setApproveTypeName(String approveTypeName) {
		this.approveTypeName = approveTypeName;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
}
