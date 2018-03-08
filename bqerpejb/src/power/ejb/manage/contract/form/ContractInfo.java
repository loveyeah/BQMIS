package power.ejb.manage.contract.form;

import java.util.Date;

public class ContractInfo implements java.io.Serializable{
	//合同ID
	private Long conId;
	//供应商ID
	private Long cliendId;
	//币别ID
	private Long currencyType;
	//工作流状态(long)
	private Long workflowStatus;
	//工作流状态(string)
	private String workflowSta;
	//付款总金额
	private Double actAmount;
	//合同编号
	private String conttreesNo;
	//合同名称
	private String contractName;
	//经办人编码
	private String operateBy;
	//经办人部门编码
	private String operateDepCode;
	//经办人部门id
	private Long deptId;
	//经办人姓名
	private String operateName;
	//经办人部门名称
	private String operateDeptName;
	//会签开始时间
	private String signStartDate;
	//会签结束时间
	private String signEndDate;
	//供应商名称
	private String clientName;

	//归档状态
	private String fileStatue;
	//-----add by fyyang
	//经办部门负责人 
	private String operateLeadBy;
	private String operateLeadName;
	//合同开始时间
	private String startDate;
	//合同结束时间
	private String endDate;
	
	//-----add by drdu
	
	//合同变更id
	private String conModify;
	//执行标记(String)
	private String exeFlag;
	//归档日期
	private String fileDate;
	//合同起草日期
	private String entryDate;
	//有总金额
	private String isSum;
	//已结算金额
	private Double payedAmount;
	//未结算金额
	private Double unliquidate;
	//计划付款日期
	private String payDate;
	//计划付款金额
	private Double payPrice;
	//备注
	private String memo;
	//归档备注
	private String fileMemo;
	//费用来源ID
	private String itemId;
	//费用来源
	private String itemName;
	private String enterpriseCode;
	private String isUse;
	//-----add by lyu
//	工作流号
	private Long workflowNo;
	//合同执行标记
	private Long execFlag;
	//合同变更id
	private Long conModifyId;
	//合同页号
	private String pageCount;
	//合同份数
	private Long fileCount;
//	合同类别ID
	private Long conTypeId;
//	合同类别名称
	private String conTypeName;
//  合同档号
	private String fileNo;
	
	//-----add by bjxu
	//币别名称
	private String currencyName;
	//负责人
	private String constructionName;
	//归档页面区分变更
	private String typeChoose;
	//变更去原合同id
	private String typeConId;
	
	//add by drdu 091110
	//委托人
	private String prosyBy; 
	private String prosyName;
	//委托开始时间
	private String prosyStartDate;
	//委托结束时间
	private String prosyEndDate;
	//委托工作流号
    private Long workflowNoDg;
    
    //委托审批状态 
    private Long workflowDgStatus;
    
  //判断是否是计划经营部 add by bjxu 
	private String jhFlg;
	
	
	public String getTypeConId() {
		return typeConId;
	}
	public void setTypeConId(String typeConId) {
		this.typeConId = typeConId;
	}
	public String getTypeChoose() {
		return typeChoose;
	}
	public void setTypeChoose(String typeChoose) {
		this.typeChoose = typeChoose;
	}
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	public Long getCliendId() {
		return cliendId;
	}
	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}
	public Long getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(Long currencyType) {
		this.currencyType = currencyType;
	}
	public Long getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(Long workflowStatus) {
		this.workflowStatus = workflowStatus;
	}
	public Double getActAmount() {
		return actAmount;
	}
	public void setActAmount(Double actAmount) {
		this.actAmount = actAmount;
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
	public String getOperateName() {
		return operateName;
	}
	public void setOperateName(String operateName) {
		this.operateName = operateName;
	}
	public String getOperateDeptName() {
		return operateDeptName;
	}
	public void setOperateDeptName(String operateDeptName) {
		this.operateDeptName = operateDeptName;
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
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getFileStatue() {
		return fileStatue;
	}
	public void setFileStatue(String fileStatue) {
		this.fileStatue = fileStatue;
	}
	public String getOperateLeadBy() {
		return operateLeadBy;
	}
	public void setOperateLeadBy(String operateLeadBy) {
		this.operateLeadBy = operateLeadBy;
	}
	public String getOperateLeadName() {
		return operateLeadName;
	}
	public void setOperateLeadName(String operateLeadName) {
		this.operateLeadName = operateLeadName;
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
	public String getIsSum() {
		return isSum;
	}
	public void setIsSum(String isSum) {
		this.isSum = isSum;
	}
	public Double getPayedAmount() {
		return payedAmount;
	}
	public void setPayedAmount(Double payedAmount) {
		this.payedAmount = payedAmount;
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
	public Double getUnliquidate() {
		return unliquidate;
	}
	public void setUnliquidate(Double unliquidate) {
		this.unliquidate = unliquidate;
	}
	public Long getExecFlag() {
		return execFlag;
	}
	public void setExecFlag(Long execFlag) {
		this.execFlag = execFlag;
	}
	public Long getConModifyId() {
		return conModifyId;
	}
	public void setConModifyId(Long conModifyId) {
		this.conModifyId = conModifyId;
	}
	public String getPageCount() {
		return pageCount;
	}
	public void setPageCount(String pageCount) {
		this.pageCount = pageCount;
	}
	public Long getFileCount() {
		return fileCount;
	}
	public void setFileCount(Long fileCount) {
		this.fileCount = fileCount;
	}
	public Long getConTypeId() {
		return conTypeId;
	}
	public void setConTypeId(Long conTypeId) {
		this.conTypeId = conTypeId;
	}
	public String getConTypeName() {
		return conTypeName;
	}
	public void setConTypeName(String conTypeName) {
		this.conTypeName = conTypeName;
	}
	public String getFileNo() {
		return fileNo;
	}
	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
	}
	public Double getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getFileDate() {
		return fileDate;
	}
	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getExeFlag() {
		return exeFlag;
	}
	public void setExeFlag(String exeFlag) {
		this.exeFlag = exeFlag;
	}
	public String getConModify() {
		return conModify;
	}
	public void setConModify(String conModify) {
		this.conModify = conModify;
	}
	public String getFileMemo() {
		return fileMemo;
	}
	public void setFileMemo(String fileMemo) {
		this.fileMemo = fileMemo;
	}
	public Long getWorkflowNo() {
		return workflowNo;
	}
	public void setWorkflowNo(Long workflowNo) {
		this.workflowNo = workflowNo;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getWorkflowSta() {
		return workflowSta;
	}
	public void setWorkflowSta(String workflowSta) {
		this.workflowSta = workflowSta;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public String getConstructionName() {
		return constructionName;
	}
	public void setConstructionName(String constructionName) {
		this.constructionName = constructionName;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
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
	public Long getWorkflowNoDg() {
		return workflowNoDg;
	}
	public void setWorkflowNoDg(Long workflowNoDg) {
		this.workflowNoDg = workflowNoDg;
	}
	public String getProsyBy() {
		return prosyBy;
	}
	public void setProsyBy(String prosyBy) {
		this.prosyBy = prosyBy;
	}
	public String getProsyName() {
		return prosyName;
	}
	public void setProsyName(String prosyName) {
		this.prosyName = prosyName;
	}
	public String getJhFlg() {
		return jhFlg;
	}
	public void setJhFlg(String jhFlg) {
		this.jhFlg = jhFlg;
	}
	public Long getWorkflowDgStatus() {
		return workflowDgStatus;
	}
	public void setWorkflowDgStatus(Long workflowDgStatus) {
		this.workflowDgStatus = workflowDgStatus;
	}
	
}
