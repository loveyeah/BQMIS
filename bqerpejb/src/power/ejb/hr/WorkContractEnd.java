/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.hr;
/**
 * 终止劳动合同登记bean
 * 
 * @author liuxin
 */
public class WorkContractEnd implements java.io.Serializable{
/**劳动合同签定ID*/
	private Long workcontractid;
	/** 劳动合同编号 */
    private String workContractNo;
    /**劳动合同有效期*/
    private String contractTerm;
    /**甲方部门*/
    private String fristDept;
    /**甲方地址*/
    private String fristAddrest;
    /**中文名*/
    private String ChsName;
    /**劳动合同续签标志*/
    private String contractContinueMark;
    /**所属部门*/
    private String suoDept;
    /**岗位名称*/
    private String stationName;
    /**劳动合同签字日期*/
    private String signDate;
    /**开始日期*/
    private String startDate;
    /**结束日期*/
    private String endDate;
    /**是否劳动合同正在执行*/
    private String ifExecute;
    /**人员ID*/
    private Long empId;
    /**部门ID*/
    private Long deptId;
    /**岗位ID*/
    private Long stationId;
    /**是否使用*/
    private String isUse;
    /**人员基本信息表里的岗位ID*/
    private Long empInfoStation;
    /**人员基本信息表里的部门ID*/
    private Long empInfoDept;
    
    private Long contractTermId;
    
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public Long getWorkcontractid() {
		return workcontractid;
	}
	public void setWorkcontractid(Long workcontractid) {
		this.workcontractid = workcontractid;
	}
	public String getWorkContractNo() {
		return workContractNo;
	}
	public void setWorkContractNo(String workContractNo) {
		this.workContractNo = workContractNo;
	}
	public String getContractTerm() {
		return contractTerm;
	}
	public void setContractTerm(String contractTerm) {
		this.contractTerm = contractTerm;
	}
	public String getFristDept() {
		return fristDept;
	}
	public void setFristDept(String fristDept) {
		this.fristDept = fristDept;
	}
	public String getFristAddrest() {
		return fristAddrest;
	}
	public void setFristAddrest(String fristAddrest) {
		this.fristAddrest = fristAddrest;
	}
	public String getChsName() {
		return ChsName;
	}
	public void setChsName(String chsName) {
		ChsName = chsName;
	}
	public String getContractContinueMark() {
		return contractContinueMark;
	}
	public void setContractContinueMark(String contractContinueMark) {
		this.contractContinueMark = contractContinueMark;
	}
	public String getSuoDept() {
		return suoDept;
	}
	public void setSuoDept(String suoDept) {
		this.suoDept = suoDept;
	}
	public String getStationName() {
		return stationName;
	}
	public void setStationName(String stationName) {
		this.stationName = stationName;
	}
	public String getSignDate() {
		return signDate;
	}
	public void setSignDate(String signDate) {
		this.signDate = signDate;
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
	public String getIfExecute() {
		return ifExecute;
	}
	public void setIfExecute(String ifExecute) {
		this.ifExecute = ifExecute;
	}
	public Long getEmpId() {
		return empId;
	}
	public void setEmpId(Long empId) {
		this.empId = empId;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public Long getStationId() {
		return stationId;
	}
	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}
	public Long getEmpInfoStation() {
		return empInfoStation;
	}
	public void setEmpInfoStation(Long empInfoStation) {
		this.empInfoStation = empInfoStation;
	}
	public Long getEmpInfoDept() {
		return empInfoDept;
	}
	public void setEmpInfoDept(Long empInfoDept) {
		this.empInfoDept = empInfoDept;
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
}