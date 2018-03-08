/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;

import java.util.Date;

/**
 * 劳动合同bean
 * 
 * @author zhouxu
 */
public class WorkContract implements java.io.Serializable{
    /** 劳动合同签定ID */
    private Long workcontractid;
    /** 人员ID */
    private Long empId;
    /** 人员姓名 */
    private String empName;
    /** 部门ID */
    private Long deptId;
    /** 部门名字 */
    private String deptName;
    /** 岗位ID */
    private Long stationId;
    /** 岗位名字 */
    private String stationName;
    /** 劳动合同编号 */
    private String wrokContractNo;
    /** 甲方部门ID */
    private Long fristDepId;
    /** 甲方部门名字 */
    private String fristDepName;
    /** 甲方地址 */
    private String fristAddrest;
    /** 劳动合同有效期ID */
    private Long contractTermId;
    /** 劳动合同签字日期 */
    private String workSignDate;
    /** 开始日期 */
    private String startDate;
    /** 结束日期 */
    private String endDate;
    /** 是否劳动合同正在执行 */
    private String ifExecute;
    /** 劳动合同续签标志 */
    private String contractContinueMark;
    /** 备注 */
    private String memo;
    /** 记录人 */
    private String insertby;
    /** 记录日期 */
    private String insertdate;
    /** 企业编码 */
    private String enterpriseCode;
    /** 是否使用 */
    private String isUse;
    /** 上次修改人 */
    private String lastModifiedBy;
    /** 上次修改日期 */
    private Date lastModifiedDate;
    /** 甲方 */
    private String owner;
    /** 签订机构 */
	private String signedInstitutions;
	/** 合同期限 */
	private String contractPeriod;
	/** 用工形式 */
	private String laborType;
	/** 合同类别 */
	private String contractType;
	
	/** 合同解除文号 */
	private String contractTerminatedCode; //add by sychen 20100714
    /**
     * @return the workcontractid
     */
    public Long getWorkcontractid() {
        return workcontractid;
    }
    /**
     * @param workcontractid the workcontractid to set
     */
    public void setWorkcontractid(Long workcontractid) {
        this.workcontractid = workcontractid;
    }
    /**
     * @return the empId
     */
    public Long getEmpId() {
        return empId;
    }
    /**
     * @param empId the empId to set
     */
    public void setEmpId(Long empId) {
        this.empId = empId;
    }
    /**
     * @return the empName
     */
    public String getEmpName() {
        return empName;
    }
    /**
     * @param empName the empName to set
     */
    public void setEmpName(String empName) {
        this.empName = empName;
    }
    /**
     * @return the deptId
     */
    public Long getDeptId() {
        return deptId;
    }
    /**
     * @param deptId the deptId to set
     */
    public void setDeptId(Long deptId) {
        this.deptId = deptId;
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
     * @return the stationId
     */
    public Long getStationId() {
        return stationId;
    }
    /**
     * @param stationId the stationId to set
     */
    public void setStationId(Long stationId) {
        this.stationId = stationId;
    }
    /**
     * @return the stationName
     */
    public String getStationName() {
        return stationName;
    }
    /**
     * @param stationName the stationName to set
     */
    public void setStationName(String stationName) {
        this.stationName = stationName;
    }
    /**
     * @return the wrokContractNo
     */
    public String getWrokContractNo() {
        return wrokContractNo;
    }
    /**
     * @param wrokContractNo the wrokContractNo to set
     */
    public void setWrokContractNo(String wrokContractNo) {
        this.wrokContractNo = wrokContractNo;
    }
    /**
     * @return the fristDepId
     */
    public Long getFristDepId() {
        return fristDepId;
    }
    /**
     * @param fristDepId the fristDepId to set
     */
    public void setFristDepId(Long fristDepId) {
        this.fristDepId = fristDepId;
    }
    /**
     * @return the fristDepName
     */
    public String getFristDepName() {
        return fristDepName;
    }
    /**
     * @param fristDepName the fristDepName to set
     */
    public void setFristDepName(String fristDepName) {
        this.fristDepName = fristDepName;
    }
    /**
     * @return the fristAddrest
     */
    public String getFristAddrest() {
        return fristAddrest;
    }
    /**
     * @param fristAddrest the fristAddrest to set
     */
    public void setFristAddrest(String fristAddrest) {
        this.fristAddrest = fristAddrest;
    }
    /**
     * @return the contractTermId
     */
    public Long getContractTermId() {
        return contractTermId;
    }
    /**
     * @param contractTermId the contractTermId to set
     */
    public void setContractTermId(Long contractTermId) {
        this.contractTermId = contractTermId;
    }
    /**
     * @return the workSignDate
     */
    public String getWorkSignDate() {
        return workSignDate;
    }
    /**
     * @param workSignDate the workSignDate to set
     */
    public void setWorkSignDate(String workSignDate) {
        this.workSignDate = workSignDate;
    }
    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }
    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }
    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }
    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
    /**
     * @return the ifExecute
     */
    public String getIfExecute() {
        return ifExecute;
    }
    /**
     * @param ifExecute the ifExecute to set
     */
    public void setIfExecute(String ifExecute) {
        this.ifExecute = ifExecute;
    }
    /**
     * @return the contractContinueMark
     */
    public String getContractContinueMark() {
        return contractContinueMark;
    }
    /**
     * @param contractContinueMark the contractContinueMark to set
     */
    public void setContractContinueMark(String contractContinueMark) {
        this.contractContinueMark = contractContinueMark;
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
     * @return the insertby
     */
    public String getInsertby() {
        return insertby;
    }
    /**
     * @param insertby the insertby to set
     */
    public void setInsertby(String insertby) {
        this.insertby = insertby;
    }
    /**
     * @return the insertdate
     */
    public String getInsertdate() {
        return insertdate;
    }
    /**
     * @param insertdate the insertdate to set
     */
    public void setInsertdate(String insertdate) {
        this.insertdate = insertdate;
    }
    /**
     * @return the enterpriseCode
     */
    public String getEnterpriseCode() {
        return enterpriseCode;
    }
    /**
     * @param enterpriseCode the enterpriseCode to set
     */
    public void setEnterpriseCode(String enterpriseCode) {
        this.enterpriseCode = enterpriseCode;
    }
    /**
     * @return the isUse
     */
    public String getIsUse() {
        return isUse;
    }
    /**
     * @param isUse the isUse to set
     */
    public void setIsUse(String isUse) {
        this.isUse = isUse;
    }
    /**
     * @return the lastModifiedBy
     */
    public String getLastModifiedBy() {
        return lastModifiedBy;
    }
    /**
     * @param lastModifiedBy the lastModifiedBy to set
     */
    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }
    /**
     * @return the lastModifiedDate
     */
    public Date getLastModifiedDate() {
        return lastModifiedDate;
    }
    /**
     * @param lastModifiedDate the lastModifiedDate to set
     */
    public void setLastModifiedDate(Date lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }
	public String getOwner() {
		return owner;
	}
	public void setOwner(String owner) {
		this.owner = owner;
	}
	public String getSignedInstitutions() {
		return signedInstitutions;
	}
	public void setSignedInstitutions(String signedInstitutions) {
		this.signedInstitutions = signedInstitutions;
	}
	public String getContractPeriod() {
		return contractPeriod;
	}
	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}
	public String getLaborType() {
		return laborType;
	}
	public void setLaborType(String laborType) {
		this.laborType = laborType;
	}
	public String getContractType() {
		return contractType;
	}
	public void setContractType(String contractType) {
		this.contractType = contractType;
	}
	public String getContractTerminatedCode() {
		return contractTerminatedCode;
	}
	public void setContractTerminatedCode(String contractTerminatedCode) {
		this.contractTerminatedCode = contractTerminatedCode;
	}
        
 }
