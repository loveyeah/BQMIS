package power.ejb.manage.contract.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJContractInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "CON_J_CONTRACT_INFO")
public class ConJContractInfo implements java.io.Serializable {

	// Fields

	private Long conId;
	private String conttreesNo;
	private String contractName;
	private String conAbstract;
	private Long cliendId;
	private Long conTypeId;
	private String conYear;
	private String projectId;
	private Long currencyType;
	private String isSum;
	private Double actAmount;
	private String isSign;
	private String isInstant;
	private String operateBy;
	private String operateDepCode;
	private String operateLeadBy;
	private String manageDepcode;
	private String conBidfNo;
	private String isBid;
	private String itemId;
	private Date startDate;
	private Date endDate;
	private Long execFlag;
	private String fileStatus;
	private String fileNo;
	private String fileBy;
	private Date fileDate;
	private String fileMemo;
	private Long fileCount;
	private String pageCount;
	private Double appliedAmount;
	private Double approvedAmount;
	private Double payedAmount;
	private String entryBy;
	private Date entryDate;
	private Date signStartDate;
	private Date signEndDate;
	private Long workflowNo;
	private Long workflowStatus;
	private String enterpriseCode;
	private String isUse;
	private String terminateBy;
	private Date terminateDate;
	private String terminateMome;
	private Long thirdClientId;
	private Long warrantyPeriod; 

	/**add by bjxu 灞桥添加字段20091012
	 * 经办人联系电话
	 * 承办部门意见
	 * 承办部门意见日期
	 * 经营计划部意见
	 * 经营计划部意见日期
	 *本项目合同代理人
	 *委托开始日期
	 *委托结束日期
	 *项目类别*/ 
	private String operateTel;
	private String operateAdvice;
	private Date operateDate;
	private String jyjhAdvice;
	private Date jyjhDate;
	private String prosy_by; 
	private Date prosyStartDate;
	private Date prosyEndDate;
    private Long prjtypeId;
    /**add by mgxia 灞桥添加字段20100604
     * 是否签订安全协议
     */
    private String ifSecrity;
    
	// Constructors
  //委托工作流号 bjxu 091109WORKFLOW_DG_STATUS
    private Long workflowNoDg;
    // 采购合同类别
    private String conCode;
    //委托审批状态 
   private Long workflowDgStatus;
   //委托人
   private String proxyCode;
   
	/** default constructor */
	public ConJContractInfo() {
	}

	/** minimal constructor */
	public ConJContractInfo(Long conId, String conttreesNo,
			String contractName, Long cliendId, Long conTypeId, String conYear,
			Long currencyType, String isSum, String isSign, String isInstant,
			String itemId, Long execFlag, String fileStatus, String entryBy,
			Date entryDate, String enterpriseCode, String isUse , Long prjtypeId) {
		this.conId = conId;
		this.conttreesNo = conttreesNo;
		this.contractName = contractName;
		this.cliendId = cliendId;
		this.conTypeId = conTypeId;
		this.conYear = conYear;
		this.currencyType = currencyType;
		this.isSum = isSum;
		this.isSign = isSign;
		this.isInstant = isInstant;
		this.itemId = itemId;
		this.execFlag = execFlag;
		this.fileStatus = fileStatus;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
        this.prjtypeId = prjtypeId;
      
	}

	/** full constructor */
	public ConJContractInfo(Long conId, String conttreesNo,
			String contractName, String conAbstract, Long cliendId,
			Long conTypeId, String conYear, String projectId,
			Long currencyType, String isSum, Double actAmount, String isSign,
			String isInstant, String operateBy, String operateDepCode,
			String operateLeadBy, String manageDepcode, String conBidfNo,
			String isBid, String itemId, Date startDate, Date endDate,
			Long execFlag, String fileStatus, String fileNo, String fileBy,
			Date fileDate, String fileMemo, Long fileCount, String pageCount,
			Double appliedAmount, Double approvedAmount, Double payedAmount,
			String entryBy, Date entryDate, Date signStartDate,
			Date signEndDate, Long workflowNo, Long workflowStatus,
			String enterpriseCode, String isUse, String terminateBy,
			Date terminateDate, String terminateMome, Long thirdClientId,Long warrantyPeriod, String operateTel,
			String operateAdvice, Date operateDate, 
			String jyjhAdvice,Date jyjhDate,String prosy_by,
			Date  prosyStartDate, Date prosyEndDate , Long prjtypeId,Long workflowNoDg ,String conCode,Long workflowDgStatus,String proxyCode,
			String ifSecrity) {
		this.conId = conId;
		this.conttreesNo = conttreesNo;
		this.contractName = contractName;
		this.conAbstract = conAbstract;
		this.cliendId = cliendId;
		this.conTypeId = conTypeId;
		this.conYear = conYear;
		this.projectId = projectId;
		this.currencyType = currencyType;
		this.isSum = isSum;
		this.actAmount = actAmount;
		this.isSign = isSign;
		this.isInstant = isInstant;
		this.operateBy = operateBy;
		this.operateDepCode = operateDepCode;
		this.operateLeadBy = operateLeadBy;
		this.manageDepcode = manageDepcode;
		this.conBidfNo = conBidfNo;
		this.isBid = isBid;
		this.itemId = itemId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.execFlag = execFlag;
		this.fileStatus = fileStatus;
		this.fileNo = fileNo;
		this.fileBy = fileBy;
		this.fileDate = fileDate;
		this.fileMemo = fileMemo;
		this.fileCount = fileCount;
		this.pageCount = pageCount;
		this.appliedAmount = appliedAmount;
		this.approvedAmount = approvedAmount;
		this.payedAmount = payedAmount;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.signStartDate = signStartDate;
		this.signEndDate = signEndDate;
		this.workflowNo = workflowNo;
		this.workflowStatus = workflowStatus;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.terminateBy = terminateBy;
		this.terminateDate = terminateDate;
		this.terminateMome = terminateMome;
		this.thirdClientId = thirdClientId;
		this.warrantyPeriod=warrantyPeriod;
		this.operateTel = operateTel;
		this.operateAdvice = operateAdvice;
		this.operateDate = operateDate;
		this.jyjhAdvice = jyjhAdvice;
		this.jyjhDate = jyjhDate;
		this.prosy_by = prosy_by;
		this.prosyStartDate = prosyStartDate;
		this.prosyEndDate = prosyEndDate;
		this.prjtypeId = prjtypeId;
		this.workflowNoDg=workflowNoDg;
		this.conCode = conCode;
		this.workflowDgStatus = workflowDgStatus;
		this.proxyCode=proxyCode;
		this.ifSecrity = ifSecrity;
	
	}

	// Property accessors
	@Id
	@Column(name = "CON_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getConId() {
		return this.conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	@Column(name = "CONTTREES_NO", nullable = false, length = 50)
	public String getConttreesNo() {
		return this.conttreesNo;
	}

	public void setConttreesNo(String conttreesNo) {
		this.conttreesNo = conttreesNo;
	}

	@Column(name = "CONTRACT_NAME", nullable = false, length = 100)
	public String getContractName() {
		return this.contractName;
	}

	public void setContractName(String contractName) {
		this.contractName = contractName;
	}

	@Column(name = "CON_ABSTRACT", length = 1000)
	public String getConAbstract() {
		return this.conAbstract;
	}

	public void setConAbstract(String conAbstract) {
		this.conAbstract = conAbstract;
	}

	@Column(name = "CLIEND_ID", nullable = false, precision = 10, scale = 0)
	public Long getCliendId() {
		return this.cliendId;
	}

	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}

	@Column(name = "CON_TYPE_ID", nullable = false, precision = 10, scale = 0)
	public Long getConTypeId() {
		return this.conTypeId;
	}

	public void setConTypeId(Long conTypeId) {
		this.conTypeId = conTypeId;
	}

	@Column(name = "CON_YEAR", nullable = false, length = 4)
	public String getConYear() {
		return this.conYear;
	}

	public void setConYear(String conYear) {
		this.conYear = conYear;
	}

	@Column(name = "PROJECT_ID", length = 30)
	public String getProjectId() {
		return this.projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Column(name = "CURRENCY_TYPE", nullable = false, precision = 10, scale = 0)
	public Long getCurrencyType() {
		return this.currencyType;
	}

	public void setCurrencyType(Long currencyType) {
		this.currencyType = currencyType;
	}

	@Column(name = "IS_SUM", nullable = false, length = 1)
	public String getIsSum() {
		return this.isSum;
	}

	public void setIsSum(String isSum) {
		this.isSum = isSum;
	}

	@Column(name = "ACT_AMOUNT", precision = 15, scale = 4)
	public Double getActAmount() {
		return this.actAmount;
	}

	public void setActAmount(Double actAmount) {
		this.actAmount = actAmount;
	}

	@Column(name = "IS_SIGN", nullable = false, length = 1)
	public String getIsSign() {
		return this.isSign;
	}

	public void setIsSign(String isSign) {
		this.isSign = isSign;
	}

	@Column(name = "IS_INSTANT", nullable = false, length = 1)
	public String getIsInstant() {
		return this.isInstant;
	}

	public void setIsInstant(String isInstant) {
		this.isInstant = isInstant;
	}

	@Column(name = "OPERATE_BY", length = 16)
	public String getOperateBy() {
		return this.operateBy;
	}

	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}

	@Column(name = "OPERATE_DEP_CODE", length = 20)
	public String getOperateDepCode() {
		return this.operateDepCode;
	}

	public void setOperateDepCode(String operateDepCode) {
		this.operateDepCode = operateDepCode;
	}

	@Column(name = "OPERATE_LEAD_BY", length = 16)
	public String getOperateLeadBy() {
		return this.operateLeadBy;
	}

	public void setOperateLeadBy(String operateLeadBy) {
		this.operateLeadBy = operateLeadBy;
	}

	@Column(name = "MANAGE_DEPCODE", length = 20)
	public String getManageDepcode() {
		return this.manageDepcode;
	}

	public void setManageDepcode(String manageDepcode) {
		this.manageDepcode = manageDepcode;
	}

	@Column(name = "CON_BIDF_NO", length = 50)
	public String getConBidfNo() {
		return this.conBidfNo;
	}

	public void setConBidfNo(String conBidfNo) {
		this.conBidfNo = conBidfNo;
	}

	@Column(name = "IS_BID", length = 1)
	public String getIsBid() {
		return this.isBid;
	}

	public void setIsBid(String isBid) {
		this.isBid = isBid;
	}

	@Column(name = "ITEM_ID", nullable = false, precision = 10, scale = 0)
	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "EXEC_FLAG", nullable = false, precision = 1, scale = 0)
	public Long getExecFlag() {
		return this.execFlag;
	}

	public void setExecFlag(Long execFlag) {
		this.execFlag = execFlag;
	}

	@Column(name = "FILE_STATUS", nullable = false, length = 3)
	public String getFileStatus() {
		return this.fileStatus;
	}

	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}

	@Column(name = "FILE_NO", length = 30)
	public String getFileNo() {
		return this.fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	@Column(name = "FILE_BY", length = 16)
	public String getFileBy() {
		return this.fileBy;
	}

	public void setFileBy(String fileBy) {
		this.fileBy = fileBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "FILE_DATE", length = 7)
	public Date getFileDate() {
		return this.fileDate;
	}

	public void setFileDate(Date fileDate) {
		this.fileDate = fileDate;
	}

	@Column(name = "FILE_MEMO", length = 500)
	public String getFileMemo() {
		return this.fileMemo;
	}

	public void setFileMemo(String fileMemo) {
		this.fileMemo = fileMemo;
	}

	@Column(name = "FILE_COUNT", precision = 4)
	public Long getFileCount() {
		return fileCount;
	}

	public void setFileCount(Long fileCount) {
		this.fileCount = fileCount;
	}

	@Column(name = "PAGE_COUNT", length = 10)
	public String getPageCount() {
		return this.pageCount;
	}

	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}

	@Column(name = "APPLIED_AMOUNT", precision = 15, scale = 4)
	public Double getAppliedAmount() {
		return this.appliedAmount;
	}

	public void setAppliedAmount(Double appliedAmount) {
		this.appliedAmount = appliedAmount;
	}

	@Column(name = "APPROVED_AMOUNT", precision = 15, scale = 4)
	public Double getApprovedAmount() {
		return this.approvedAmount;
	}

	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}

	@Column(name = "PAYED_AMOUNT", precision = 15)
	public Double getPayedAmount() {
		return this.payedAmount;
	}

	public void setPayedAmount(Double payedAmount) {
		this.payedAmount = payedAmount;
	}

	@Column(name = "ENTRY_BY", nullable = false, length = 16)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE", nullable = false, length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SIGN_START_DATE", length = 7)
	public Date getSignStartDate() {
		return this.signStartDate;
	}

	public void setSignStartDate(Date signStartDate) {
		this.signStartDate = signStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SIGN_END_DATE", length = 7)
	public Date getSignEndDate() {
		return this.signEndDate;
	}

	public void setSignEndDate(Date signEndDate) {
		this.signEndDate = signEndDate;
	}

	@Column(name = "WORKFLOW_NO", precision = 22, scale = 0)
	public Long getWorkflowNo() {
		return this.workflowNo;
	}

	public void setWorkflowNo(Long workflowNo) {
		this.workflowNo = workflowNo;
	}

	@Column(name = "WORKFLOW_STATUS", precision = 11, scale = 0)
	public Long getWorkflowStatus() {
		return this.workflowStatus;
	}

	public void setWorkflowStatus(Long workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", nullable = false, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "TERMINATE_BY")
	public String getTerminateBy() {
		return terminateBy;
	}

	public void setTerminateBy(String terminateBy) {
		this.terminateBy = terminateBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "TERMINATE_DATE")
	public Date getTerminateDate() {
		return terminateDate;
	}

	public void setTerminateDate(Date terminateDate) {
		this.terminateDate = terminateDate;
	}

	@Column(name = "TERMINATE_MEMO")
	public String getTerminateMome() {
		return terminateMome;
	}

	public void setTerminateMome(String terminateMome) {
		this.terminateMome = terminateMome;
	}

	@Column(name = "THIRD_CLIEND_ID")
	public Long getThirdClientId() {
		return thirdClientId;
	}

	public void setThirdClientId(Long thirdClientId) {
		this.thirdClientId = thirdClientId;
	}

	@Column(name = "WARRANTY_PERIOD", precision = 4)
	public Long getWarrantyPeriod() {
		return warrantyPeriod;
	}

	public void setWarrantyPeriod(Long warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}
	@Column(name = "OPERATE_TEL", length = 13)
	public String getOperateTel() {
		return this.operateTel;
	}

	public void setOperateTel(String operateTel) {
		this.operateTel = operateTel;
	}

	@Column(name = "OPERATE_ADVICE", length = 500)
	public String getOperateAdvice() {
		return this.operateAdvice;
	}

	public void setOperateAdvice(String operateAdvice) {
		this.operateAdvice = operateAdvice;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "OPERATE_DATE", length = 7)
	public Date getOperateDate() {
		return this.operateDate;
	}

	public void setOperateDate(Date operateDate) {
		this.operateDate = operateDate;
	}

	@Column(name = "JYJH_ADVICE", length = 500)
	public String getJyjhAdvice() {
		return this.jyjhAdvice;
	}

	public void setJyjhAdvice(String jyjhAdvice) {
		this.jyjhAdvice = jyjhAdvice;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "JYJH_DATE", length = 7)
	public Date getJyjhDate() {
		return this.jyjhDate;
	}

	public void setJyjhDate(Date jyjhDate) {
		this.jyjhDate = jyjhDate;
	}
	@Column(name = "PROSY_BY", length = 20)
	public String getProsy_by() {
		return prosy_by;
	}

	public void setProsy_by(String prosy_by) {
		this.prosy_by = prosy_by;
	}
	

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROSY_START_DATE", length = 7)
	public Date getProsyStartDate() {
		return prosyStartDate;
	}
	
	public void setProsyStartDate(Date prosyStartDate) {
		this.prosyStartDate = prosyStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PROSY_END_DATE", length = 7)
	public Date getProsyEndDate() {
		return prosyEndDate;
	}

	public void setProsyEndDate(Date prosyEndDate) {
		this.prosyEndDate = prosyEndDate;
	}
    
	
	@Column(name = "PRJTYPE_ID", precision = 10, scale = 0)
	public Long getPrjtypeId() {
		return prjtypeId;
	}

	public void setPrjtypeId(Long prjtypeId) {
		this.prjtypeId = prjtypeId;
	}
	
	@Column(name = "WORKFLOW_NO_DG", precision = 22, scale = 0)
	public Long getWorkflowNoDg() {
		return workflowNoDg;
	}

	public void setWorkflowNoDg(Long workflowNoDg) {
		this.workflowNoDg = workflowNoDg;
	}

	@Column(name = "CON_CODE", precision = 10, scale = 0)
	public String getConCode() {
		return conCode;
	}
	
	public void setConCode(String conCode) {
		this.conCode = conCode;
	}
	@Column(name = "WORKFLOW_DG_STATUS", precision = 11, scale = 0)
	public Long getWorkflowDgStatus() {
		return workflowDgStatus;
	}

	public void setWorkflowDgStatus(Long workflowDgStatus) {
		this.workflowDgStatus = workflowDgStatus;
	}
	
	@Column(name = "PROXY_CODE", length = 20)
	public String getProxyCode() {
		return proxyCode;
	}

	public void setProxyCode(String proxyCode) {
		this.proxyCode = proxyCode;
	}

	@Column(name = "IF_SECRITY", length = 1)
	public String getIfSecrity() {
		return ifSecrity;
	}

	public void setIfSecrity(String ifSecrity) {
		this.ifSecrity = ifSecrity;
	}
	

	}
	
	