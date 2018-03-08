package power.ejb.run.securityproduction.form;

@SuppressWarnings("serial")
public class SpDiseasesInfo implements java.io.Serializable{

	/** 体检编号 */
	private Long medicalId;
	/** 人员编号 */
	private String workerCode;
	/** 人员名称 */
	private String workerName;
	/** 接触危害 */
	private String contactHarm;
	/** 接触年数 */
	private Long contactYear;
	/** 检查时间 */
	private String checkDate;
	/** 医院 */
	private String hospital;
	/** 检查内容 */
	private String content;
	/** 检查结果 */
	private String checkResult;
	/** 备注 */
	private String memo;
	/** 人员部门 */
	private String deptName;
	
	public Long getMedicalId() {
		return medicalId;
	}
	public void setMedicalId(Long medicalId) {
		this.medicalId = medicalId;
	}
	public String getWorkerCode() {
		return workerCode;
	}
	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}
	public String getContactHarm() {
		return contactHarm;
	}
	public void setContactHarm(String contactHarm) {
		this.contactHarm = contactHarm;
	}
	public Long getContactYear() {
		return contactYear;
	}
	public void setContactYear(Long contactYear) {
		this.contactYear = contactYear;
	}
	public String getCheckDate() {
		return checkDate;
	}
	public void setCheckDate(String checkDate) {
		this.checkDate = checkDate;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getWorkerName() {
		return workerName;
	}
	public void setWorkerName(String workerName) {
		this.workerName = workerName;
	}
}
