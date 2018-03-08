/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;
/**
 * 派遣人员信息
 * @author zhaomingjian
 *
 */
public class BorrowEmployeeInfo implements java.io.Serializable{
	/**员工编码**/
    private String empCode;
    /**中文名**/
    private String chsName;
    /**部门名称**/
    private String deptName;
    /**岗位名称**/
    private String stationName;
    /**员工外借ID**/
    private String employeeBorrowId;
    /**开始日期**/
    private String startDate;
    /**结束日期**/
    private String endDate;
    /**停薪日期**/
    private String stopPayDate;
    /**起薪日期**/
    private String startPayDate;
    /**是否回来**/
    private String ifBack;
    /**备注**/
    private String memo;
    /**人员ID**/
    private String empId;
    /**部门ID**/
    private String deptId;
    /**岗位ID**/
    private String stationId;
    /**flag**/
    private String flag;
    /**修改时间**/
    private String updateTime;
    /**
     * 员工编码
     * @return
     */
	public String getEmpCode() {
		return empCode;
	}
	public void setEmpCode(String empCode) {
		this.empCode = empCode;
	}
	/**
	 * 中文名
	 * @return
	 */
	public String getChsName() {
		return chsName;
	}
	public void setChsName(String chsName) {
		this.chsName = chsName;
	}
	/**
	 * 部门名称
	 * @return
	 */
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	/**
	 * 岗位名称
	 * @return
	 */
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	/**
	 * 开始日期
	 * @return
	 */
	public String getEmployeeBorrowId() {
		return employeeBorrowId;
	}
	public void setEmployeeBorrowId(String employeeBorrowId) {
		this.employeeBorrowId = employeeBorrowId;
	}
	/**
	 * 结束日期
	 * @return
	 */
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	/**
	 * 停薪日期
	 * @return
	 */
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	/**
	 * 起薪日期
	 * @return
	 */
	public String getStopPayDate() {
		return stopPayDate;
	}
	public void setStopPayDate(String stopPayDate) {
		this.stopPayDate = stopPayDate;
	}
	/**
	 * 是否回来
	 * @return
	 */
	public String getStartPayDate() {
		return startPayDate;
	}
	public void setStartPayDate(String startPayDate) {
		this.startPayDate = startPayDate;
	}
	/**
	 * 是否回来
	 * @return
	 */
	public String getIfBack() {
		return ifBack;
	}
	public void setIfBack(String ifBack) {
		this.ifBack = ifBack;
	}
	/**
	 * 备注
	 * @return
	 */
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	/**
	 * 员工ID
	 * 
	 * @return
	 */
	public String getEmpId() {
		return empId;
	}
	public void setEmpId(String empId) {
		this.empId = empId;
	}
	/**
	 * 部门id
	 * @return
	 */
	public String getDeptId() {
		return deptId;
	}
	public void setDeptId(String deptId) {
		this.deptId = deptId;
	}
	/**
	 * 岗位ID
	 * @return
	 */
	public String getStationId() {
		return stationId;
	}
	public void setStationId(String stationId) {
		this.stationId = stationId;
	}
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
