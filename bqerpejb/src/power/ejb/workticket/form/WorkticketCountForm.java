package power.ejb.workticket.form;

public class WorkticketCountForm {
//	专业编码
	private String repairSpecailCode;
//	专业名称
	private String repairSpecailName;
//	起始号
	private String startWorkticketNo;
//	结束号
	private String endWorkticketNo;
//	工作票类别编码
	private String workticketTypeCode;
//	工作票类别名称
	private String workticketTypeName;
//	未签发数量
	private Long notIssuedNum;
//	已签发数量
	private Long issuedNum;
//	已接收数量
	private Long receivedNum;
//	已批准数量
	private Long approvaledNum;
//	已许可数量
	private Long licensedNum;
//	已完成数量
	private Long achievedNum;
//	未执行数量
	private Long notExecedNum;
//	作废数量
	private Long invalidNum;
//  总计
	private Long totlaNum;
//	工作票数量
	private Long workticketNum;
//	工作票合格数量
	private Long workticketQualifiedNum;
//	使用标准票数量
	private Long usedStanderworkticketNum;
//	使用标准票合格数量
	private Long usedStaworkQualifiedNum;
	public String getRepairSpecailCode() {
		return repairSpecailCode;
	}
	public void setRepairSpecailCode(String repairSpecailCode) {
		this.repairSpecailCode = repairSpecailCode;
	}
	public String getRepairSpecailName() {
		return repairSpecailName;
	}
	public void setRepairSpecailName(String repairSpecailName) {
		this.repairSpecailName = repairSpecailName;
	}
	public String getStartWorkticketNo() {
		return startWorkticketNo;
	}
	public void setStartWorkticketNo(String startWorkticketNo) {
		this.startWorkticketNo = startWorkticketNo;
	}
	public String getEndWorkticketNo() {
		return endWorkticketNo;
	}
	public void setEndWorkticketNo(String endWorkticketNo) {
		this.endWorkticketNo = endWorkticketNo;
	}
	public String getWorkticketTypeCode() {
		return workticketTypeCode;
	}
	public void setWorkticketTypeCode(String workticketTypeCode) {
		this.workticketTypeCode = workticketTypeCode;
	}
	public String getWorkticketTypeName() {
		return workticketTypeName;
	}
	public void setWorkticketTypeName(String workticketTypeName) {
		this.workticketTypeName = workticketTypeName;
	}
	public Long getNotIssuedNum() {
		return notIssuedNum;
	}
	public void setNotIssuedNum(Long notIssuedNum) {
		this.notIssuedNum = notIssuedNum;
	}
	public Long getIssuedNum() {
		return issuedNum;
	}
	public void setIssuedNum(Long issuedNum) {
		this.issuedNum = issuedNum;
	}
	public Long getReceivedNum() {
		return receivedNum;
	}
	public void setReceivedNum(Long receivedNum) {
		this.receivedNum = receivedNum;
	}
	public Long getApprovaledNum() {
		return approvaledNum;
	}
	public void setApprovaledNum(Long approvaledNum) {
		this.approvaledNum = approvaledNum;
	}
	public Long getLicensedNum() {
		return licensedNum;
	}
	public void setLicensedNum(Long licensedNum) {
		this.licensedNum = licensedNum;
	}
	public Long getAchievedNum() {
		return achievedNum;
	}
	public void setAchievedNum(Long achievedNum) {
		this.achievedNum = achievedNum;
	}
	public Long getNotExecedNum() {
		return notExecedNum;
	}
	public void setNotExecedNum(Long notExecedNum) {
		this.notExecedNum = notExecedNum;
	}
	public Long getInvalidNum() {
		return invalidNum;
	}
	public void setInvalidNum(Long invalidNum) {
		this.invalidNum = invalidNum;
	}
	public Long getWorkticketNum() {
		return workticketNum;
	}
	public void setWorkticketNum(Long workticketNum) {
		this.workticketNum = workticketNum;
	}
	public Long getWorkticketQualifiedNum() {
		return workticketQualifiedNum;
	}
	public void setWorkticketQualifiedNum(Long workticketQualifiedNum) {
		this.workticketQualifiedNum = workticketQualifiedNum;
	}
	public Long getUsedStanderworkticketNum() {
		return usedStanderworkticketNum;
	}
	public void setUsedStanderworkticketNum(Long usedStanderworkticketNum) {
		this.usedStanderworkticketNum = usedStanderworkticketNum;
	}
	public Long getUsedStaworkQualifiedNum() {
		return usedStaworkQualifiedNum;
	}
	public void setUsedStaworkQualifiedNum(Long usedStaworkQualifiedNum) {
		this.usedStaworkQualifiedNum = usedStaworkQualifiedNum;
	}
	public Long getTotlaNum() {
		return totlaNum;
	}
	public void setTotlaNum(Long totlaNum) {
		this.totlaNum = totlaNum;
	}

}
