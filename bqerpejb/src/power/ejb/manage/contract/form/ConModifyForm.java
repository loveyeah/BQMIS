package power.ejb.manage.contract.form;

import java.util.Date;
import java.util.List;

public class ConModifyForm implements java.io.Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//合同ID
	private Long conId;
	private Long conModifyId;     	//变更合同ID
	private String conttreesNo;    //合同编号
	private String conModifyNo;		//合同变更编号
	private String contractName;	//合同名称
	private Long cliendId;			//供应商ID
	private Long conomodifyType;	//变更类型
	private String conomodifyTypeName;//变更类型名称
	private Long workflowStatus;	//工作流状态
	private String operateBy;		//经办人编号
	private String operateDepCode;	//申请部门编号
	private Long deptId;			//申请部门ID
	private String signStartDate;	//会签开始时间
	private String signEndDate;		//会签结束时间
	private String enterpriseCode;
	private String isUse;
	private String clientName;		//供应商名称
	
	private String operateName;		//经办人名称
	private String operateDeptName;	//申请部门名称
	private String operateLeadBy;  //部门负责人编号
	private String operateLeadName;//部门负责人名称
	private Long currencyType;     //币别
	private Double actAmount; 		//原合同金额
	private Double modiyActAmount;	//现合同金额
	private String startDate;		//合同履行开始时间
	private String endDate;			//合同履行结束时间
	private String conomodifyName;	//变更原因
	private String entryBy;			//起草人
	private String entryName;
	private String entryDate;			//起草时间
	private String lastModifiedBy;	
	private String lastModifiedDate;	
	
	private String balaFlag;		//付款状态
	private String paymentMoment;	//付款阶段
	private String memo;			//付款说明
	private Double payPrice;		//付款金额
	private String payDate;			//计划付款日期

	private String fileStatus;		//归档状态
	private Long workFlowNo; //工作流实例号
	
	private String execFlag; //合同执行状态 add bjxu
	
	//当前部门审批判断 
	private String deptFlg;
	
	private String filePath;		// 附件名称
	
	private List<ConDocForm> mconlist; //合同正本list
    private List<ConDocForm> mconAttlist;//合同附件list
    private List<ConDocForm> mconEvilist;//合同凭据list
    private List<PaymentPlanForm> conpaylist;//合同付款list
    private String currencyName;     //币种名称 
    
    
    
	public Long getConModifyId() {
		return conModifyId;
	}
	public void setConModifyId(Long conModifyId) {
		this.conModifyId = conModifyId;
	}
	public String getConModifyNo() {
		return conModifyNo;
	}
	public void setConModifyNo(String conModifyNo) {
		this.conModifyNo = conModifyNo;
	}
	public String getContractName() {
		return contractName;
	}
	public void setContractName(String contractName) {
		this.contractName = contractName;
	}
	public Long getCliendId() {
		return cliendId;
	}
	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}
	public Long getConomodifyType() {
		return conomodifyType;
	}
	public void setConomodifyType(Long conomodifyType) {
		this.conomodifyType = conomodifyType;
	}
	public Long getWorkflowStatus() {
		return workflowStatus;
	}
	public void setWorkflowStatus(Long workflowStatus) {
		this.workflowStatus = workflowStatus;
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
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
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
	public String getConttreesNo() {
		return conttreesNo;
	}
	public void setConttreesNo(String conttreesNo) {
		this.conttreesNo = conttreesNo;
	}
	public String getOperateLeadBy() {
		return operateLeadBy;
	}
	public void setOperateLeadBy(String operateLeadBy) {
		this.operateLeadBy = operateLeadBy;
	}
	public Long getCurrencyType() {
		return currencyType;
	}
	public void setCurrencyType(Long currencyType) {
		this.currencyType = currencyType;
	}
	public Double getActAmount() {
		return actAmount;
	}
	public void setActAmount(Double actAmount) {
		this.actAmount = actAmount;
	}
	public Double getModiyActAmount() {
		return modiyActAmount;
	}
	public void setModiyActAmount(Double modiyActAmount) {
		this.modiyActAmount = modiyActAmount;
	}	
	public String getConomodifyName() {
		return conomodifyName;
	}
	public void setConomodifyName(String conomodifyName) {
		this.conomodifyName = conomodifyName;
	}
	public String getEntryBy() {
		return entryBy;
	}
	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}
	public String getLastModifiedBy() {
		return lastModifiedBy;
	}
	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
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
	public String getEntryDate() {
		return entryDate;
	}
	public void setEntryDate(String entryDate) {
		this.entryDate = entryDate;
	}
	public String getLastModifiedDate() {
		return lastModifiedDate;
	}
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}
	public String getBalaFlag() {
		return balaFlag;
	}
	public void setBalaFlag(String balaFlag) {
		this.balaFlag = balaFlag;
	}
	public String getPaymentMoment() {
		return paymentMoment;
	}
	public void setPaymentMoment(String paymentMoment) {
		this.paymentMoment = paymentMoment;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public Double getPayPrice() {
		return payPrice;
	}
	public void setPayPrice(Double payPrice) {
		this.payPrice = payPrice;
	}
	public String getPayDate() {
		return payDate;
	}
	public void setPayDate(String payDate) {
		this.payDate = payDate;
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
	public String getFileStatus() {
		return fileStatus;
	}
	public void setFileStatus(String fileStatus) {
		this.fileStatus = fileStatus;
	}
	public String getConomodifyTypeName() {
		return conomodifyTypeName;
	}
	public void setConomodifyTypeName(String conomodifyTypeName) {
		this.conomodifyTypeName = conomodifyTypeName;
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
	public List<PaymentPlanForm> getConpaylist() {
		return conpaylist;
	}
	public void setConpaylist(List<PaymentPlanForm> conpaylist) {
		this.conpaylist = conpaylist;
	}
	public Long getConId() {
		return conId;
	}
	public void setConId(Long conId) {
		this.conId = conId;
	}
	public Long getWorkFlowNo() {
		return workFlowNo;
	}
	public void setWorkFlowNo(Long workFlowNo) {
		this.workFlowNo = workFlowNo;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getCurrencyName() {
		return currencyName;
	}
	public void setCurrencyName(String currencyName) {
		this.currencyName = currencyName;
	}
	public Long getDeptId() {
		return deptId;
	}
	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}
	public String getExecFlag() {
		return execFlag;
	}
	public void setExecFlag(String execFlag) {
		this.execFlag = execFlag;
	}
	public String getDeptFlg() {
		return deptFlg;
	}
	public void setDeptFlg(String deptFlg) {
		this.deptFlg = deptFlg;
	}
}
