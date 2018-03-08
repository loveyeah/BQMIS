package power.ejb.manage.contract.form;


@SuppressWarnings("serial")
public class ConBalanceForm implements java.io.Serializable{
//结算ID
	private Long balanceId;
//	合同ID
	private Long conId;
//	付款计划ID
	private Long paymentId;
//	合作伙伴ID
	private Long cliendId;
//	经办人
	private String operateBy;
//	经办人部门
	private String operateDepCode;
//	财务经办人
	private String balanceBy;
//	付款表识
	private String balaFlag;
//	付款说明
	private String balaBatch;
//	付款原因
	private String balaCause;
//	备注
	private String memo;
//	工作流序号
	private Long workflowNo;
//	工作流状态
	private Long workflowStatus;
//	本次申请付款
	private Double applicatPrice;
//	本次审批付款
	private Double passPrice;
//	财务结算金额
	private Double balancePrice;
//	其他费用
	private Double elsePrice;
//	结算方式
	private String balaMethod;
//	费用来源
	private String itemId;
//	申请日期
	private String applicatDate;
//	批准日期
	private String passDate;
//	结算日期
	private String balaDate;
//	支票号
	private String chequeNo;
//	票号
	private String receiptNo;
//	起草人
	private String entryBy;
//	起草时间
	private String entryDate;
//	企业编码
	private String enterpriseCode;
//	是否使用
	private String isUse;
	
//	合同名称
	private String contractName;
//	合作伙伴名称
	private String clientName;
//	费用来源名称
	private String itemName;
//	经办人名称
	private String operateName;
//	经办部门名称
	private String operateDeptName;
//	起草人名称
	private String entryName;
//	财务经办人名称
	private String balanceName;
//	合同总额
	private Double actAmount;
	//-----add by bjxu
	//合同币别
	private String currencyName;
// add by bjxu 剩余金额 灞桥用
	private Double leaveAmount;
	public Long getBalanceId() {
		return balanceId;
	}
	public void setBalanceId(Long balanceId) {
		this.balanceId = balanceId;
	}
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	public Long getPaymentId() {
		return paymentId;
	}
	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}
	public Long getCliendId() {
		return cliendId;
	}
	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
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
	public String getBalanceBy() {
		return balanceBy;
	}
	public void setBalanceBy(String balanceBy) {
		this.balanceBy = balanceBy;
	}
	public String getBalaFlag() {
		return balaFlag;
	}
	public void setBalaFlag(String balaFlag) {
		this.balaFlag = balaFlag;
	}
	public String getBalaBatch() {
		return balaBatch;
	}
	public void setBalaBatch(String balaBatch) {
		this.balaBatch = balaBatch;
	}
	public String getBalaCause() {
		return balaCause;
	}
	public void setBalaCause(String balaCause) {
		this.balaCause = balaCause;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	public Double getApplicatPrice() {
		return applicatPrice;
	}
	public void setApplicatPrice(Double applicatPrice) {
		this.applicatPrice = applicatPrice;
	}
	public Double getPassPrice() {
		return passPrice;
	}
	public void setPassPrice(Double passPrice) {
		this.passPrice = passPrice;
	}
	public Double getBalancePrice() {
		return balancePrice;
	}
	public void setBalancePrice(Double balancePrice) {
		this.balancePrice = balancePrice;
	}
	public Double getElsePrice() {
		return elsePrice;
	}
	public void setElsePrice(Double elsePrice) {
		this.elsePrice = elsePrice;
	}
	public String getBalaMethod() {
		return balaMethod;
	}
	public void setBalaMethod(String balaMethod) {
		this.balaMethod = balaMethod;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getApplicatDate() {
		return applicatDate;
	}
	public void setApplicatDate(String applicatDate) {
		this.applicatDate = applicatDate;
	}
	public String getPassDate() {
		return passDate;
	}
	public void setPassDate(String passDate) {
		this.passDate = passDate;
	}
	public String getBalaDate() {
		return balaDate;
	}
	public void setBalaDate(String balaDate) {
		this.balaDate = balaDate;
	}
	public String getChequeNo() {
		return chequeNo;
	}
	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}
	public String getReceiptNo() {
		return receiptNo;
	}
	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
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
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
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
	public String getEntryName() {
		return entryName;
	}
	public void setEntryName(String entryName) {
		this.entryName = entryName;
	}
	public String getBalanceName() {
		return balanceName;
	}
	public void setBalanceName(String balanceName) {
		this.balanceName = balanceName;
	}
	public Double getActAccount() {
		return actAmount;
	}
	public void setActAccount(Double actAmount) {
		this.actAmount = actAmount;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public Double getLeaveAmount() {
		return leaveAmount;
	}
	public void setLeaveAmount(Double leaveAmount) {
		this.leaveAmount = leaveAmount;
	}
}
