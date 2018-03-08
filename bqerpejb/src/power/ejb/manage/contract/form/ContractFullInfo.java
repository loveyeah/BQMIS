package power.ejb.manage.contract.form;

import java.util.Date;
import java.util.List;


public class ContractFullInfo implements java.io.Serializable{
//	合同ID
	private Long conId;
//	合同编号
	private String conttreesNo;
//	合同名称
	private String contractName;
//	合同描叙
	private String conAbstract;
//	供应商ID
	private Long cliendId;
	//供应商name add by bjxu
	private String cliendName;
//	合同类别ID
	private Long conTypeId;
//	合同年份
	private String conYear;
////	项目ID
//	private Long projectId;
//	币别编码
	private Long currencyType;
//	是否有总金额
	private String isSum;
//	总金额
	private Double actAmount;
//	是否要求会签
	private String isSign;
//	要求紧急会签
	private String isInstant;
//	经办人编码
	private String operateBy;
//	经办人部门编码
	private String operateDepCode;
//	负责人编码
	private String operateLeadBy;
//	
	private String manageDepcode;
//	
	private String conBidfNo;
//	
	private String isBid;
//	费用来演ID
	private String itemId;
//	计划开始时间
	private String startDate;
//	计划结束时间
	private String endDate;
//	执行标记
	private Long execFlag;
//	
	private String fileStatus;
//	
	private String fileNo;
//	
	private String fileBy;
//	
	private String fileDate;
//	
	private String fileMemo;
//	
	private String pageCount;
//	
	private Double appliedAmount;
//	
	private Double approvedAmount;
//	已付金额
	private Double payedAmount;
//	付款人编码
	private String entryBy;
//	付款时间
	private String entryDate;
//	会签开始时间
	private String signStartDate;
//	会签结束时间
	private String signEndDate;
//	工作流号
	private Long workflowNo;
//	工作流状态
	private Long workflowStatus;
//	企业编码
	private String enterpriseCode;
//	是否使用
	private String isUse;
	
//	供应商名称
	private String clientName;
//	合同类别名称
	private String conTypeName;
//	费用来源名称
	private String itemName;
//	币别名称
	private String currencyName;
//	经办人名称
	private String operateName;
//	经办人部门名称
	private String operateDepName;
//	负责人名称
	private String operateLeadName;
//	起草人名称
	private String entryName;
//	银行
	private String bankAccount;
//	帐号
	private String account;
//	合同文本附件列表
    private List<ConDocForm> mconlist;
//  合同附件列表
    private List<ConDocForm> mconAttlist;
//  合同凭据列表
    private List<ConDocForm> mconEvilist;
    private List<PaymentPlanForm> paylist;
//   合同文本描叙
    private String filePath;
  //-----add by drdu
    
    //验收终止人
    private String terminateBy;
    //验收终止日期
	private String terminateDate;
    //验收终止说明
	private String terminateMome;
	//验收终止人名称
	private String terminateByName;
	//---ADD by bjxu
	//第三方合作伙伴id
	private Long thirdClientId;
	//第三方合作伙伴名称
	private String thirdClientName;
	//合同对应项目名称
	private String projectName;
	//合同对应项目ID
	private String projectId;
	//第三方负责人
	private String constructionName;
	//合同对应项目显示编号
	private String prjShow;
	// 质量期限（月）
	private String warrantyPeriod;
	/**add by bjxu 灞桥添加字段20091012
	 * 经办人联系电话
	 * 承办部门意见
	 * 承办部门意见日期
	 * 经营计划部意见
	 * 经营计划部意见日期
	 * 
	 */
	private String operateTel;
	private String operateAdvice;
	private String operateDate;
	private String jyjhAdvice;
	private String jyjhDate;
	private String prosy_by; 
	private String prosyStartDate;
	private String prosyEndDate;
	private String prosy_byName;
	private Long   prjtypeId;
	private String prjTypeName;
	
	 /**add by mgxia 灞桥添加字段20100604
     * 是否签订安全协议
     */
    private String ifSecrity;
    
	 private Long workflowNoDg;
	//采购合同类别 add by bjxu091117
	 private String  conCode;
	 
	public String getProsyStartDate() {
		return prosyStartDate;
	}
	public void setProsyStartDate(String prosyStartDate) {
		this.prosyStartDate = prosyStartDate;
	}
	public String getProsyEndDate() {
		return prosyEndDate;
	}
	public void setProsyEndDate(String prosyEndDate) {
		this.prosyEndDate = prosyEndDate;
	}
	public String getProsy_by() {
		return prosy_by;
	}
	public void setProsy_by(String prosy_by) {
		this.prosy_by = prosy_by;
	}
	public String getWarrantyPeriod() {
		return warrantyPeriod;
	}
	public void setWarrantyPeriod(String warrantyPeriod) {
		this.warrantyPeriod = warrantyPeriod;
	}
	public String getPrjShow() {
		return prjShow;
	}
	public void setPrjShow(String prjShow) {
		this.prjShow = prjShow;
	}
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getConTypeName() {
		return conTypeName;
	}
	public void setConTypeName(String conTypeName) {
		this.conTypeName = conTypeName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public String getOperateDepName() {
		return operateDepName;
	}
	public void setOperateDepName(String operateDepName) {
		this.operateDepName = operateDepName;
	}
	public String getOperateLeadName() {
		return operateLeadName;
	}
	public void setOperateLeadName(String operateLeadName) {
		this.operateLeadName = operateLeadName;
	}
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	public String getConttreesNo() {
		return conttreesNo;
	}
	public void setConttreesNo(String conttreesNo) {
		this.conttreesNo = conttreesNo;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getConAbstract() {
		return conAbstract;
	}
	public void setConAbstract(String conAbstract) {
		this.conAbstract = conAbstract;
	}
	public Long getCliendId() {
		return cliendId;
	}
	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}
	public Long getConTypeId() {
		return conTypeId;
	}
	public void setConTypeId(Long conTypeId) {
		this.conTypeId = conTypeId;
	}
	public String getConYear() {
		return conYear;
	}
	public void setConYear(String conYear) {
		this.conYear = conYear;
	}
//	public Long getProjectId() {
//		return projectId;
//	}
//	public void setProjectId(Long projectId) {
//		this.projectId = projectId;
//	}
	public Long getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(Long currencyType) {
		this.currencyType = currencyType;
	}
	public String getIsSum() {
		return isSum;
	}
	public void setIsSum(String isSum) {
		this.isSum = isSum;
	}
	public Double getActAmount() {
		return actAmount;
	}
	public void setActAmount(Double actAmount) {
		this.actAmount = actAmount;
	}
	public String getIsSign() {
		return isSign;
	}
	public void setIsSign(String isSign) {
		this.isSign = isSign;
	}
	public String getIsInstant() {
		return isInstant;
	}
	public void setIsInstant(String isInstant) {
		this.isInstant = isInstant;
	}
	public String getOperateBy() {
		return operateBy;
	}
	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}
	public String getOperateDepCode() {
		return operateDepCode;
	}
	public void setOperateDepCode(String operateDepCode) {
		this.operateDepCode = operateDepCode;
	}
	public String getOperateLeadBy() {
		return operateLeadBy;
	}
	public void setOperateLeadBy(String operateLeadBy) {
		this.operateLeadBy = operateLeadBy;
	}
	public String getManageDepcode() {
		return manageDepcode;
	}
	public void setManageDepcode(String manageDepcode) {
		this.manageDepcode = manageDepcode;
	}
	public String getConBidfNo() {
		return conBidfNo;
	}
	public void setConBidfNo(String conBidfNo) {
		this.conBidfNo = conBidfNo;
	}
	public String getIsBid() {
		return isBid;
	}
	public void setIsBid(String isBid) {
		this.isBid = isBid;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
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
	public Long getExecFlag() {
		return execFlag;
	}
	public void setExecFlag(Long execFlag) {
		this.execFlag = execFlag;
	}
	public String getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	public String getFileNo() {
		return fileNo;
	}
	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}
	public String getFileBy() {
		return fileBy;
	}
	public void setFileBy(String fileBy) {
		this.fileBy = fileBy;
	}
	public String getFileDate() {
		return fileDate;
	}
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}
	public String getFileMemo() {
		return fileMemo;
	}
	public void setFileMemo(String fileMemo) {
		this.fileMemo = fileMemo;
	}
	public String getPageCount() {
		return pageCount;
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	public Double getAppliedAmount() {
		return appliedAmount;
	}
	public void setAppliedAmount(Double appliedAmount) {
		this.appliedAmount = appliedAmount;
	}
	public Double getApprovedAmount() {
		return approvedAmount;
	}
	public void setApprovedAmount(Double approvedAmount) {
		this.approvedAmount = approvedAmount;
	}
	public Double getPayedAmount() {
		return payedAmount;
	}
	public void setPayedAmount(Double payedAmount) {
		this.payedAmount = payedAmount;
	}
	public String getEntryBy() {
		return entryBy;
	}
	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getSignStartDate() {
		return signStartDate;
	}
	public void setSignStartDate(String signStartDate) {
		this.signStartDate = signStartDate;
	}
	public String getSignEndDate() {
		return signEndDate;
	}
	public void setSignEndDate(String signEndDate) {
		this.signEndDate = signEndDate;
	}
	public Long getWorkflowNo() {
		return workflowNo;
	}
	public void setWorkflowNo(Long workflowNo) {
		this.workflowNo = workflowNo;
	}
	public Long getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(Long workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	public String getEnterpriseCode() {
		return enterpriseCode;
	}
	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}
	public String getIsUse() {
		return isUse;
	}
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	public List<ConDocForm> getMconlist() {
		return mconlist;
	}
	public void setMconlist(List<ConDocForm> mconlist) {
		this.mconlist = mconlist;
	}
	public List<ConDocForm> getMconAttlist() {
		return mconAttlist;
	}
	public void setMconAttlist(List<ConDocForm> mconAttlist) {
		this.mconAttlist = mconAttlist;
	}
	public List<ConDocForm> getMconEvilist() {
		return mconEvilist;
	}
	public void setMconEvilist(List<ConDocForm> mconEvilist) {
		this.mconEvilist = mconEvilist;
	}
	public List<PaymentPlanForm> getPaylist() {
		return paylist;
	}
	public void setPaylist(List<PaymentPlanForm> paylist) {
		this.paylist = paylist;
	}
	public String getTerminateBy() {
		return terminateBy;
	}
	public void setTerminateBy(String terminateBy) {
		this.terminateBy = terminateBy;
	}
	public String getTerminateDate() {
		return terminateDate;
	}
	public void setTerminateDate(String terminateDate) {
		this.terminateDate = terminateDate;
	}
	public String getTerminateMome() {
		return terminateMome;
	}
	public void setTerminateMome(String terminateMome) {
		this.terminateMome = terminateMome;
	}
	public String getTerminateByName() {
		return terminateByName;
	}
	public void setTerminateByName(String terminateByName) {
		this.terminateByName = terminateByName;
	}
	public String getBankAccount() {
		return bankAccount;
	}
	public void setBankAccount(String bankAccount) {
		this.bankAccount = bankAccount;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public Long getThirdClientId() {
		return thirdClientId;
	}
	public void setThirdClientId(Long thirdClientId) {
		this.thirdClientId = thirdClientId;
	}
	public String getThirdClientName() {
		return thirdClientName;
	}
	public void setThirdClientName(String thirdClientName) {
		this.thirdClientName = thirdClientName;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getConstructionName() {
		return constructionName;
	}
	public void setConstructionName(String constructionName) {
		this.constructionName = constructionName;
	}
	public String getCliendName() {
		return cliendName;
	}
	public void setCliendName(String cliendName) {
		this.cliendName = cliendName;
	}
	public String getOperateTel() {
		return operateTel;
	}
	public void setOperateTel(String operateTel) {
		this.operateTel = operateTel;
	}
	public String getOperateAdvice() {
		return operateAdvice;
	}
	public void setOperateAdvice(String operateAdvice) {
		this.operateAdvice = operateAdvice;
	}
	public String getOperateDate() {
		return operateDate;
	}
	public void setOperateDate(String operateDate) {
		this.operateDate = operateDate;
	}
	public String getJyjhAdvice() {
		return jyjhAdvice;
	}
	public void setJyjhAdvice(String jyjhAdvice) {
		this.jyjhAdvice = jyjhAdvice;
	}
	public String getJyjhDate() {
		return jyjhDate;
	}
	public void setJyjhDate(String jyjhDate) {
		this.jyjhDate = jyjhDate;
	}
	public String getProsy_byName() {
		return prosy_byName;
	}
	public void setProsy_byName(String prosy_byName) {
		this.prosy_byName = prosy_byName;
	}
	public Long getPrjtypeId() {
		return prjtypeId;
	}
	public void setPrjtypeId(Long prjtypeId) {
		this.prjtypeId = prjtypeId;
	}
	public String getPrjTypeName() {
		return prjTypeName;
	}
	public void setPrjTypeName(String prjTypeName) {
		this.prjTypeName = prjTypeName;
	}
	public Long getWorkflowNoDg() {
		return workflowNoDg;
	}
	public void setWorkflowNoDg(Long workflowNoDg) {
		this.workflowNoDg = workflowNoDg;
	}
	public String getConCode() {
		return conCode;
	}
	public void setConCode(String conCode) {
		this.conCode = conCode;
	}
	public String getIfSecrity() {
		return ifSecrity;
	}
	public void setIfSecrity(String ifSecrity) {
		this.ifSecrity = ifSecrity;
	}
	
}
