/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;
/**
 * 离职员工打印详细信息
 * @author wangpeng
 */
public class DismissionStaffListBean {

    /** 行号 */
    private String rowCount="";
    /** 员工编号 */
    private String workerCode="";
    /** 员工姓名 */
    private String workerName="";
    /** 员工类别 */
    private String workerType="";
    /** 离职日期 */
    private String dismissionDate="";
    /** 离职类别 */
    private String dismissionType="";
    /** 所属部门 */
    private String department="";
    /** 岗位名称 */
    private String jobName="";
    /** 离职原因 */
    private String dismissionReason="";
    /** 离职后去向 */
    private String whither="";
    /** 备注 */
    private String memo="";
    /** 行数计数 */
    private Integer cntRow;
    
    /** 新工号 */
    private String newEmpCode = "";

    public String getNewEmpCode() {
		return newEmpCode;
	}

	public void setNewEmpCode(String newEmpCode) {
		this.newEmpCode = newEmpCode;
	}

	/**
     * 构造方法
     */
    public DismissionStaffListBean() {

    }

    /**
     * @return the rowCount
     */
    public String getRowCount() {
        return rowCount;
    }

    /**
     * @param rowCount the rowCount to set
     */
    public void setRowCount(String rowCount) {
        this.rowCount = rowCount;
    }

    /**
     * @return the workerCode
     */
    public String getWorkerCode() {
        return workerCode;
    }

    /**
     * @param workerCode the workerCode to set
     */
    public void setWorkerCode(String workerCode) {
        this.workerCode = workerCode;
    }

    /**
     * @return the workerName
     */
    public String getWorkerName() {
        return workerName;
    }

    /**
     * @param workerName the workerName to set
     */
    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    /**
     * @return the workerType
     */
    public String getWorkerType() {
        return workerType;
    }

    /**
     * @param workerType the workerType to set
     */
    public void setWorkerType(String workerType) {
        this.workerType = workerType;
    }

    /**
     * @return the dismissionDate
     */
    public String getDismissionDate() {
        return dismissionDate;
    }

    /**
     * @param dismissionDate the dismissionDate to set
     */
    public void setDismissionDate(String dismissionDate) {
        this.dismissionDate = dismissionDate;
    }

    /**
     * @return the dismissionType
     */
    public String getDismissionType() {
        return dismissionType;
    }

    /**
     * @param dismissionType the dismissionType to set
     */
    public void setDismissionType(String dismissionType) {
        this.dismissionType = dismissionType;
    }

    /**
     * @return the department
     */
    public String getDepartment() {
        return department;
    }

    /**
     * @param department the department to set
     */
    public void setDepartment(String department) {
        this.department = department;
    }

    /**
     * @return the jobName
     */
    public String getJobName() {
        return jobName;
    }

    /**
     * @param jobName the jobName to set
     */
    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    /**
     * @return the dismissionReason
     */
    public String getDismissionReason() {
        return dismissionReason;
    }

    /**
     * @param dismissionReason the dismissionReason to set
     */
    public void setDismissionReason(String dismissionReason) {
        this.dismissionReason = dismissionReason;
    }

    /**
     * @return the whither
     */
    public String getWhither() {
        return whither;
    }

    /**
     * @param whither the whither to set
     */
    public void setWhither(String whither) {
        this.whither = whither;
    }

    /**
     * @return the memo
     */
    public String getMemo() {
        return memo;
    }

    /**
     * @param memo the memo to set
     */
    public void setMemo(String memo) {
        this.memo = memo;
    }

    /**
     * @return the cntRow
     */
    public Integer getCntRow() {
        return cntRow;
    }

    /**
     * @param cntRow the cntRow to set
     */
    public void setCntRow(Integer cntRow) {
        this.cntRow = cntRow;
    }
}
