package power.ejb.hr.form;

import java.util.Date;

import power.ejb.hr.HrJPosition;


public class PositionForm
{
//	private HrJPosition position;
	private Long positionId;
	private Long empId;
//	private Date rmDate;
	private String positionName;
	private String isPosition;
	private String positionCode;
	private String isNow;
	private Long positionLevel;
	private String approveDept;
	private Long rmMode;
	private String rmReason;
	private String rmView;
	private String memo;
	
	private String rmDateString;
	private String empName;
	private String empDeptName;
	private String positionDescri;
	private String levelDescri;
	private String approveDeptName;
	public String getApproveDeptName() {
		return approveDeptName;
	}
	public void setApproveDeptName(String approveDeptName) {
		this.approveDeptName = approveDeptName;
	}
//	public HrJPosition getPosition() {
//		return position;
//	}
//	public void setPosition(HrJPosition position) {
//		this.position = position;
//	}
	public String getRmDateString() {
		return rmDateString;
	}
	public void setRmDateString(String rmDateString) {
		this.rmDateString = rmDateString;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpDeptName() {
		return empDeptName;
	}
	public void setEmpDeptName(String empDeptName) {
		this.empDeptName = empDeptName;
	}
	public String getPositionDescri() {
		return positionDescri;
	}
	public void setPositionDescri(String positionDescri) {
		this.positionDescri = positionDescri;
	}
	public String getLevelDescri() {
		return levelDescri;
	}
	public void setLevelDescri(String levelDescri) {
		this.levelDescri = levelDescri;
	}
	public Long getPositionId() {
		return positionId;
	}
	public void setPositionId(Long positionId) {
		this.positionId = positionId;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
//	public Date getRmDate() {
//		return rmDate;
//	}
//	public void setRmDate(Date rmDate) {
//		this.rmDate = rmDate;
//	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	public String getIsPosition() {
		return isPosition;
	}
	public void setIsPosition(String isPosition) {
		this.isPosition = isPosition;
	}
	public String getPositionCode() {
		return positionCode;
	}
	public void setPositionCode(String positionCode) {
		this.positionCode = positionCode;
	}
	public String getIsNow() {
		return isNow;
	}
	public void setIsNow(String isNow) {
		this.isNow = isNow;
	}
	public Long getPositionLevel() {
		return positionLevel;
	}
	public void setPositionLevel(Long positionLevel) {
		this.positionLevel = positionLevel;
	}
	public String getApproveDept() {
		return approveDept;
	}
	public void setApproveDept(String approveDept) {
		this.approveDept = approveDept;
	}
	public Long getRmMode() {
		return rmMode;
	}
	public void setRmMode(Long rmMode) {
		this.rmMode = rmMode;
	}
	public String getRmReason() {
		return rmReason;
	}
	public void setRmReason(String rmReason) {
		this.rmReason = rmReason;
	}
	public String getRmView() {
		return rmView;
	}
	public void setRmView(String rmView) {
		this.rmView = rmView;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	
}