package power.ejb.hr.ca;

public class AttendanceStatisticsQueryInfo implements java.io.Serializable{
	
	/** 父部门id  */
	private String pdeptId;
	/** 部门id */
	private String deptId;
	/** 员工id */
	private String empId;
	/** 父部门名称 */
	private String pdeptName;
	/** 部门名称 */
	private String deptName;
	/** 姓名 */
	private String chsName;
	/** 类型id */
	private String typeId;
	/** 天数 */
	private Double days;
	/** 类型 */
	private String type;
	
	/**
	 * @return the pdeptId
	 */
	public String getPdeptId() {
		return pdeptId;
	}
	/**
	 * @param pdeptId the pdeptId to set
	 */
	public void setPdeptId(String pdeptId) {
		this.pdeptId = pdeptId;
	}
	/**
	 * @return the deptId
	 */
	public String getDeptId() {
		return deptId;
	}
	/**
	 * @param deptId the deptId to set
	 */
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * @return the empId
	 */
	public String getEmpId() {
		return empId;
	}
	/**
	 * @param empId the empId to set
	 */
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	/**
	 * @return the pdeptName
	 */
	public String getPdeptName() {
		return pdeptName;
	}
	/**
	 * @param pdeptName the pdeptName to set
	 */
	public void setPdeptName(String pdeptName) {
		this.pdeptName = pdeptName;
	}
	/**
	 * @return the deptName
	 */
	public String getDeptName() {
		return deptName;
	}
	/**
	 * @param deptName the deptName to set
	 */
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * @return the chsName
	 */
	public String getChsName() {
		return chsName;
	}
	/**
	 * @param chsName the chsName to set
	 */
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	/**
	 * @return the typeId
	 */
	public String getTypeId() {
		return typeId;
	}
	/**
	 * @param typeId the typeId to set
	 */
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	/**
	 * @return the days
	 */
	public Double getDays() {
		return days;
	}
	/**
	 * @param days the days to set
	 */
	public void setDays(Double days) {
		this.days = days;
	}
	/**
	 * @return the type
	 */
	public String getType() {
		return type;
	}
	/**
	 * @param type the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
	
}
