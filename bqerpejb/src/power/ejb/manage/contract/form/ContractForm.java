package power.ejb.manage.contract.form;

@SuppressWarnings("serial")
public class ContractForm implements java.io.Serializable{
	private Long conId;
	private String conttreesNo;
	private String contractName;
	private Long cliendId;
	private String clientName;
	private String bankAccount;
	private String account;
	private String itemId;
	private String itemName;
	private Double actAccount;
	private Double appliedAccount;
	private Double approvedAccount;
	private Double payedAccount;
	// add by bjxu 20091009
	private String deptName;
	private String budgetName;
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
	public Long getCliendId() {
		return cliendId;
	}
	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
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
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public Double getActAccount() {
		return actAccount;
	}
	public void setActAccount(Double actAccount) {
		this.actAccount = actAccount;
	}
	public Double getAppliedAccount() {
		return appliedAccount;
	}
	public void setAppliedAccount(Double appliedAccount) {
		this.appliedAccount = appliedAccount;
	}
	public Double getApprovedAccount() {
		return approvedAccount;
	}
	public void setApprovedAccount(Double approvedAccount) {
		this.approvedAccount = approvedAccount;
	}
	public Double getPayedAccount() {
		return payedAccount;
	}
	public void setPayedAccount(Double payedAccount) {
		this.payedAccount = payedAccount;
	}
	public String getDeptName() {
		return deptName;
	}
	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}
	public String getBudgetName() {
		return budgetName;
	}
	public void setBudgetName(String budgetName) {
		this.budgetName = budgetName;
	}
}
