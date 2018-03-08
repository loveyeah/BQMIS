/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

/**
 * 新进员工详细信息
 * @author wangpeng
 */
public class NewStaffListBean {

    /** 行号 */
    private String rowCount = "";
    /** 员工编号 */
    private String workerCode = "";
    /** 员工姓名 */
    private String workerName = "";
    /** 性别 */
    private String sex = "";
    /** 员工类别 */
    private String workerType = "";
    /** 进厂类别 */
    private String missionType = "";
    /** 进厂日期 */
    private String missionDate = "";
    /** 身份证号 */
    private String idCard = "";
    /** 试用期开始 */
    private String tryStartDate = "";
    /** 试用期结束 */
    private String tryEndDate = "";
    /** 所属部门 */
    private String department = "";
    /** 岗位名称 */
    private String jobName = "";
    /** 备注 */
    private String memo = "";
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
    public NewStaffListBean() {

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
	 * @return the missionType
	 */
	public String getMissionType() {
		return missionType;
	}

	/**
	 * @param missionType the missionType to set
	 */
	public void setMissionType(String missionType) {
		this.missionType = missionType;
	}

	/**
	 * @return the missionDate
	 */
	public String getMissionDate() {
		return missionDate;
	}

	/**
	 * @param missionDate the missionDate to set
	 */
	public void setMissionDate(String missionDate) {
		this.missionDate = missionDate;
	}

	/**
	 * @return the idCard
	 */
	public String getIdCard() {
		return idCard;
	}

	/**
	 * @param idCard the idCard to set
	 */
	public void setIdCard(String idCard) {
		this.idCard = idCard;
	}

	/**
	 * @return the tryStartDate
	 */
	public String getTryStartDate() {
		return tryStartDate;
	}

	/**
	 * @param tryStartDate the tryStartDate to set
	 */
	public void setTryStartDate(String tryStartDate) {
		this.tryStartDate = tryStartDate;
	}

	/**
	 * @return the tryEndDate
	 */
	public String getTryEndDate() {
		return tryEndDate;
	}

	/**
	 * @param tryEndDate the tryEndDate to set
	 */
	public void setTryEndDate(String tryEndDate) {
		this.tryEndDate = tryEndDate;
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
