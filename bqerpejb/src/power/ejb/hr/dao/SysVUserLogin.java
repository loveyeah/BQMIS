package power.ejb.hr.dao;

import java.util.List;

/**
 * SysVUserLoginId entity.
 * 
 * @author MyEclipse Persistence Tools
 */

public class SysVUserLogin implements java.io.Serializable {

	// Fields

	private Long empId;
	private String userLoginAccount;
	private String userPassword;
	private String dbAccount;
	private String userTheme;
	private String chsName;
	private String enName;
	private String empCode;
	private Long deptId;
	private String deptCode;
	private String deptName;
	private Long powerPlantId;
	private List<SysVUserRole> luserRole;

	// Constructors
	/** default constructor */
	public SysVUserLogin() {
	}

	/** full constructor */

	/**
	 * @param empId
	 * @param userLoginAccount
	 * @param userPassword
	 * @param dbAccount
	 * @param userTheme
	 * @param chsName
	 * @param enName
	 * @param empCode
	 * @param deptId
	 * @param deptCode
	 * @param deptName
	 * @param powerPlantId
	 * @param luserRole
	 */
	public SysVUserLogin(Long empId, String userLoginAccount,
			String userPassword, String dbAccount, String userTheme,
			String chsName, String enName, String empCode, Long deptId,
			String deptCode, String deptName, Long powerPlantId,
			List<SysVUserRole> luserRole) {
		this.empId = empId;
		this.userLoginAccount = userLoginAccount;
		this.userPassword = userPassword;
		this.dbAccount = dbAccount;
		this.userTheme = userTheme;
		this.chsName = chsName;
		this.enName = enName;
		this.empCode = empCode;
		this.deptId = deptId;
		this.deptCode = deptCode;
		this.deptName = deptName;
		this.powerPlantId = powerPlantId;
		this.luserRole = luserRole;
	}

	// Property accessors
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public String getUserLoginAccount() {
		return this.userLoginAccount;
	}

	public void setUserLoginAccount(String userLoginAccount) {
		this.userLoginAccount = userLoginAccount;
	}

	public String getUserPassword() {
		return this.userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getDbAccount() {
		return this.dbAccount;
	}

	public void setDbAccount(String dbAccount) {
		this.dbAccount = dbAccount;
	}

	public String getUserTheme() {
		return this.userTheme;
	}

	public void setUserTheme(String userTheme) {
		this.userTheme = userTheme;
	}

	/**
	 * @return the chsName
	 */
	public String getChsName() {
		return chsName;
	}

	/**
	 * @param chsName
	 *            the chsName to set
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
	 * @param enName
	 *            the enName to set
	 */
	public void setEnName(String enName) {
		this.enName = enName;
	}

	public String getEmpCode() {
		return this.empCode;
	}

	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}

	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public String getDeptCode() {
		return this.deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public String getDeptName() {
		return this.deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	/**
	 * @return the powerPlantId
	 */
	public Long getPowerPlantId() {
		return powerPlantId;
	}

	/**
	 * @param powerPlantId
	 *            the powerPlantId to set
	 */
	public void setPowerPlantId(Long powerPlantId) {
		this.powerPlantId = powerPlantId;
	}

	/**
	 * @return the luserRole
	 */
	public List<SysVUserRole> getLuserRole() {
		return luserRole;
	}

	/**
	 * @param luserRole
	 *            the luserRole to set
	 */
	public void setLuserRole(List<SysVUserRole> luserRole) {
		this.luserRole = luserRole;
	}

}