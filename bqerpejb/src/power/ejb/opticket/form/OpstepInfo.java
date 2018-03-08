package power.ejb.opticket.form;

public class OpstepInfo implements java.io.Serializable{

	private Long operateStepId;
	private String opticketCode;
	private String operateStepName;
	private String finishTime;
	private String execMan;
	private String execStatus;
	private Long displayNo;
	private String isMain;
	private String memo;
	private String runAddFlag;
	private String proMan;
	private String proName;
	private String execName;
	public Long getOperateStepId() {
		return operateStepId;
	}
	public void setOperateStepId(Long operateStepId) {
		this.operateStepId = operateStepId;
	}
	public String getOpticketCode() {
		return opticketCode;
	}
	public void setOpticketCode(String opticketCode) {
		this.opticketCode = opticketCode;
	}
	public String getOperateStepName() {
		return operateStepName;
	}
	public void setOperateStepName(String operateStepName) {
		this.operateStepName = operateStepName;
	}
	public String getFinishTime() {
		return finishTime;
	}
	public void setFinishTime(String finishTime) {
		this.finishTime = finishTime;
	}
	public String getExecMan() {
		return execMan;
	}
	public void setExecMan(String execMan) {
		this.execMan = execMan;
	}
	public String getExecStatus() {
		return execStatus;
	}
	public void setExecStatus(String execStatus) {
		this.execStatus = execStatus;
	}
	public Long getDisplayNo() {
		return displayNo;
	}
	public void setDisplayNo(Long displayNo) {
		this.displayNo = displayNo;
	}
	public String getIsMain() {
		return isMain;
	}
	public void setIsMain(String isMain) {
		this.isMain = isMain;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getRunAddFlag() {
		return runAddFlag;
	}
	public void setRunAddFlag(String runAddFlag) {
		this.runAddFlag = runAddFlag;
	}
	public String getProMan() {
		return proMan;
	}
	public void setProMan(String proMan) {
		this.proMan = proMan;
	}
	public String getProName() {
		return proName;
	}
	public void setProName(String proName) {
		this.proName = proName;
	}
	public String getExecName() {
		return execName;
	}
	public void setExecName(String execName) {
		this.execName = execName;
	}
}
