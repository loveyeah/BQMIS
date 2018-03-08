package power.ejb.productiontec.chemistry.form;

@SuppressWarnings("serial")
public class ThermalEquCheckDetailForm implements java.io.Serializable{
	
	private Long rlsbjcDetailId;
	private Long rlsbjcId;
	private String equCode;
	private Double courseNumber;
	private String repairType;
	private Long repairNumber;
	private String checkHigh;
	private String checkName;
	private String checkPart;
	private String dirtyCapacity;
	private String sedimentQuantity;
	private String memo;
	private String equName;
	private String repairDate;
	
	public String getEquName() {
		return equName;
	}
	public void setEquName(String equName) {
		this.equName = equName;
	}
	public String getRepairDate() {
		return repairDate;
	}
	public void setRepairDate(String repairDate) {
		this.repairDate = repairDate;
	}
	public Long getRlsbjcDetailId() {
		return rlsbjcDetailId;
	}
	public void setRlsbjcDetailId(Long rlsbjcDetailId) {
		this.rlsbjcDetailId = rlsbjcDetailId;
	}
	public Long getRlsbjcId() {
		return rlsbjcId;
	}
	public void setRlsbjcId(Long rlsbjcId) {
		this.rlsbjcId = rlsbjcId;
	}
	public String getEquCode() {
		return equCode;
	}
	public void setEquCode(String equCode) {
		this.equCode = equCode;
	}
	public Double getCourseNumber() {
		return courseNumber;
	}
	public void setCourseNumber(Double courseNumber) {
		this.courseNumber = courseNumber;
	}
	public String getRepairType() {
		return repairType;
	}
	public void setRepairType(String repairType) {
		this.repairType = repairType;
	}
	public Long getRepairNumber() {
		return repairNumber;
	}
	public void setRepairNumber(Long repairNumber) {
		this.repairNumber = repairNumber;
	}
	public String getCheckHigh() {
		return checkHigh;
	}
	public void setCheckHigh(String checkHigh) {
		this.checkHigh = checkHigh;
	}
	public String getCheckName() {
		return checkName;
	}
	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}
	public String getCheckPart() {
		return checkPart;
	}
	public void setCheckPart(String checkPart) {
		this.checkPart = checkPart;
	}
	public String getDirtyCapacity() {
		return dirtyCapacity;
	}
	public void setDirtyCapacity(String dirtyCapacity) {
		this.dirtyCapacity = dirtyCapacity;
	}
	public String getSedimentQuantity() {
		return sedimentQuantity;
	}
	public void setSedimentQuantity(String sedimentQuantity) {
		this.sedimentQuantity = sedimentQuantity;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
