/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr.ca;

/**
 * 人员及其附带的信息bean
 * 
 * @author chen shoujiang
 */
public class EmpFollowBaseInfo implements java.io.Serializable{
	private static final long serialVersionUID = 1L;
	
	private String empId;
	private String deptId;
	private String deptCode;
	private String deptName;
	private String chsName;
	private String enName;
	private String empCode;
	private String brithday;
	private String sex;
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
	 * @return the deptCode
	 */
	public String getDeptCode() {
		return deptCode;
	}
	/**
	 * @param deptCode the deptCode to set
	 */
	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
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
	 * @return the enName
	 */
	public String getEnName() {
		return enName;
	}
	/**
	 * @param enName the enName to set
	 */
	public void setEnName(String enName) {
		this.enName = enName;
	}
	/**
	 * @return the empCode
	 */
	public String getEmpCode() {
		return empCode;
	}
	/**
	 * @param empCode the empCode to set
	 */
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	/**
	 * @return the brithday
	 */
	public String getBrithday() {
		return brithday;
	}
	/**
	 * @param brithday the brithday to set
	 */
	public void setBrithday(String brithday) {
		this.brithday = brithday;
	}
	/**
	 * @return the sex
	 */
	public String getSex() {
		return sex;
	}
	/**
	 * @param sex the sex to set
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}
	
}
