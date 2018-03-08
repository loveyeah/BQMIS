package power.ejb.run.securityproduction.form;

@SuppressWarnings("serial")
public class EmployeeInfo implements java.io.Serializable{
	
	/** 员工编码*/
	private String empCode;
	/** 员工名称*/
	private String empName;
	/** 部门编码*/
	private String deptCode;
	/** 部门*/
	private String deptName;
	/** 性别*/
	private String sex;
	/** 岗位*/
	private Long stationId;
	/** 员工类别*/
	private String empType;
	/** 社保卡号*/
	private String socialInsuranceId;
	/**员工履历*/
	private String curriculumVitae;
	/** 工作岗位*/
	private String workStationName;
	
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public String getSocialInsuranceId() {
		return socialInsuranceId;
	}
	public void setSocialInsuranceId(String socialInsuranceId) {
		this.socialInsuranceId = socialInsuranceId;
	}
	public String getCurriculumVitae() {
		return curriculumVitae;
	}
	public void setCurriculumVitae(String curriculumVitae) {
		this.curriculumVitae = curriculumVitae;
	}
	public String getWorkStationName() {
		return workStationName;
	}
	public void setWorkStationName(String workStationName) {
		this.workStationName = workStationName;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getEmpType() {
		return empType;
	}
	public void setEmpType(String empType) {
		this.empType = empType;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getDeptCode() {
		return deptCode;
	}
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}
}
