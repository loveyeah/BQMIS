/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;
/**
 * 员工借调登记bean
 * 
 * @author chen shoujiang
 */
public class EmpMoveRegister implements java.io.Serializable{
	
	private static final long serialVersionUID = 1L;
	/** 员工借调ID */
	private String hrJEmployeeborrowInId;
	/** 员工ID */
	private String empId;
	/** 员工Code*/
	private String empCode;
	/**中文名称 */
	private String chsName;
	/** 所属部门ID */
	private String ssDeptId;
	/** 所属部门名称 */
	private String ssDeptName;
	/** 岗位ID */
	private String stationId;
	/** 岗位名称 */
	private String stationName;
	/** 借调部门ID */
	private String jdDeptId;
	/** 借调部门名称*/
	private String jdDeptName;;
	/** 开始日期 */
	private String startDate;
	/** 结束日期 */
	private String endDate;
	/** 单据状态 */
	private String dcmStatus;
	/** 备注 */
	private String memo;
	/** 上次修改时间*/
	private String lastModifiedDate;
	/** 是否已回 */
	private String ifBack;
	public String getHrJEmployeeborrowInId() {
		return hrJEmployeeborrowInId;
	}
	public void setHrJEmployeeborrowInId(String hrJEmployeeborrowInId) {
		this.hrJEmployeeborrowInId = hrJEmployeeborrowInId;
	}
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	public String getChsName() {
		return chsName;
	}
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	public String getSsDeptId() {
		return ssDeptId;
	}
	public void setSsDeptId(String ssDeptId) {
		this.ssDeptId = ssDeptId;
	}
	public String getSsDeptName() {
		return ssDeptName;
	}
	public void setSsDeptName(String ssDeptName) {
		this.ssDeptName = ssDeptName;
	}
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getJdDeptId() {
		return jdDeptId;
	}
	public void setJdDeptId(String jdDeptId) {
		this.jdDeptId = jdDeptId;
	}
	public String getJdDeptName() {
		return jdDeptName;
	}
	public void setJdDeptName(String jdDeptName) {
		this.jdDeptName = jdDeptName;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getDcmStatus() {
		return dcmStatus;
	}
	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getIfBack() {
		return ifBack;
	}
	public void setIfBack(String ifBack) {
		this.ifBack = ifBack;
	}
}
