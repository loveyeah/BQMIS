package power.ejb.manage.contract.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJBalance entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_BALANCE")
public class ConJBalance implements java.io.Serializable {

	// Fields

	private Long balanceId;
	private Long conId;
	private Long paymentId;
	private Long cliendId;
	private String operateBy;
	private String operateDepCode;
	private String balanceBy;
	private String balaFlag;
	private String balaBatch;
	private String balaCause;
	private String memo;
	private Long workflowNo;
	private Long workflowStatus;
	private Double applicatPrice;
	private Double passPrice;
	private Double balancePrice;
	private Double elsePrice;
	private String balaMethod;
	private String itemId;
	private Date applicatDate;
	private Date passDate;
	private Date balaDate;
	private String chequeNo;
	private String receiptNo;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;
	private String isUse;
	
	//采购申请id bq  
	private Long appId;
	//采购金额单位 bq 
	private Long moneyUnit;
	//本合同 第几次结算
	private Long conBalanceDegreeLong;
	// Constructors

	/** default constructor */
	public ConJBalance() {
	}

	/** minimal constructor */
	public ConJBalance(Long balanceId, Long conId, Long paymentId,
			Long cliendId, String balaFlag, Double applicatPrice,
			String balaMethod, String itemId, String entryBy, Date entryDate,
			String enterpriseCode, String isUse) {
		this.balanceId = balanceId;
		this.conId = conId;
		this.paymentId = paymentId;
		this.cliendId = cliendId;
		this.balaFlag = balaFlag;
		this.applicatPrice = applicatPrice;
		this.balaMethod = balaMethod;
		this.itemId = itemId;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public ConJBalance(Long balanceId, Long conId, Long paymentId,
			Long cliendId, String operateBy, String operateDepCode,
			String balanceBy, String balaFlag, String balaBatch,
			String balaCause, String memo, Long workflowNo,
			Long workflowStatus, Double applicatPrice, Double passPrice,
			Double balancePrice, Double elsePrice, String balaMethod,
			String itemId, Date applicatDate, Date passDate, Date balaDate,
			String chequeNo, String receiptNo, String entryBy, Date entryDate,
			String enterpriseCode, String isUse,Long appId,Long moneyUnit) {
		this.balanceId = balanceId;
		this.conId = conId;
		this.paymentId = paymentId;
		this.cliendId = cliendId;
		this.operateBy = operateBy;
		this.operateDepCode = operateDepCode;
		this.balanceBy = balanceBy;
		this.balaFlag = balaFlag;
		this.balaBatch = balaBatch;
		this.balaCause = balaCause;
		this.memo = memo;
		this.workflowNo = workflowNo;
		this.workflowStatus = workflowStatus;
		this.applicatPrice = applicatPrice;
		this.passPrice = passPrice;
		this.balancePrice = balancePrice;
		this.elsePrice = elsePrice;
		this.balaMethod = balaMethod;
		this.itemId = itemId;
		this.applicatDate = applicatDate;
		this.passDate = passDate;
		this.balaDate = balaDate;
		this.chequeNo = chequeNo;
		this.receiptNo = receiptNo;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.appId = appId; 
		this.moneyUnit = moneyUnit;
	}

	// Property accessors
	@Id
	@Column(name = "BALANCE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getBalanceId() {
		return this.balanceId;
	}

	public void setBalanceId(Long balanceId) {
		this.balanceId = balanceId;
	}

	@Column(name = "CON_ID", unique = false, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getConId() {
		return this.conId;
	}

	public void setConId(Long conId) {
		this.conId = conId;
	}

	@Column(name = "PAYMENT_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getPaymentId() {
		return this.paymentId;
	}

	public void setPaymentId(Long paymentId) {
		this.paymentId = paymentId;
	}

	@Column(name = "CLIEND_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getCliendId() {
		return this.cliendId;
	}

	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}

	@Column(name = "OPERATE_BY", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	public String getOperateBy() {
		return this.operateBy;
	}

	public void setOperateBy(String operateBy) {
		this.operateBy = operateBy;
	}

	@Column(name = "OPERATE_DEP_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getOperateDepCode() {
		return this.operateDepCode;
	}

	public void setOperateDepCode(String operateDepCode) {
		this.operateDepCode = operateDepCode;
	}

	@Column(name = "BALANCE_BY", unique = false, nullable = true, insertable = true, updatable = true, length = 16)
	public String getBalanceBy() {
		return this.balanceBy;
	}

	public void setBalanceBy(String balanceBy) {
		this.balanceBy = balanceBy;
	}

	@Column(name = "BALA_FLAG", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getBalaFlag() {
		return this.balaFlag;
	}

	public void setBalaFlag(String balaFlag) {
		this.balaFlag = balaFlag;
	}

	@Column(name = "BALA_BATCH", unique = false, nullable = true, insertable = true, updatable = true, length = 500)
	public String getBalaBatch() {
		return this.balaBatch;
	}

	public void setBalaBatch(String balaBatch) {
		this.balaBatch = balaBatch;
	}

	@Column(name = "BALA_CAUSE", unique = false, nullable = true, insertable = true, updatable = true, length = 500)
	public String getBalaCause() {
		return this.balaCause;
	}

	public void setBalaCause(String balaCause) {
		this.balaCause = balaCause;
	}

	@Column(name = "MEMO", unique = false, nullable = true, insertable = true, updatable = true, length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "WORKFLOW_NO", unique = false, nullable = true, insertable = true, updatable = true, precision = 22, scale = 0)
	public Long getWorkflowNo() {
		return this.workflowNo;
	}

	public void setWorkflowNo(Long workflowNo) {
		this.workflowNo = workflowNo;
	}

	@Column(name = "WORKFLOW_STATUS", unique = false, nullable = true, insertable = true, updatable = true, precision = 11, scale = 0)
	public Long getWorkflowStatus() {
		return this.workflowStatus;
	}

	public void setWorkflowStatus(Long workflowStatus) {
		this.workflowStatus = workflowStatus;
	}

	@Column(name = "APPLICAT_PRICE", unique = false, nullable = false, insertable = true, updatable = true, precision = 15, scale = 4)
	public Double getApplicatPrice() {
		return this.applicatPrice;
	}

	public void setApplicatPrice(Double applicatPrice) {
		this.applicatPrice = applicatPrice;
	}

	@Column(name = "PASS_PRICE", unique = false, nullable = true, insertable = true, updatable = true, precision = 15, scale = 4)
	public Double getPassPrice() {
		return this.passPrice;
	}

	public void setPassPrice(Double passPrice) {
		this.passPrice = passPrice;
	}

	@Column(name = "BALANCE_PRICE", unique = false, nullable = true, insertable = true, updatable = true, precision = 15, scale = 4)
	public Double getBalancePrice() {
		return this.balancePrice;
	}

	public void setBalancePrice(Double balancePrice) {
		this.balancePrice = balancePrice;
	}

	@Column(name = "ELSE_PRICE", unique = false, nullable = true, insertable = true, updatable = true, precision = 15, scale = 4)
	public Double getElsePrice() {
		return this.elsePrice;
	}

	public void setElsePrice(Double elsePrice) {
		this.elsePrice = elsePrice;
	}

	@Column(name = "BALA_METHOD", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getBalaMethod() {
		return this.balaMethod;
	}

	public void setBalaMethod(String balaMethod) {
		this.balaMethod = balaMethod;
	}

	@Column(name = "ITEM_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public String getItemId() {
		return this.itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "APPLICAT_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getApplicatDate() {
		return this.applicatDate;
	}

	public void setApplicatDate(Date applicatDate) {
		this.applicatDate = applicatDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "PASS_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getPassDate() {
		return this.passDate;
	}

	public void setPassDate(Date passDate) {
		this.passDate = passDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "BALA_DATE", unique = false, nullable = true, insertable = true, updatable = true, length = 7)
	public Date getBalaDate() {
		return this.balaDate;
	}

	public void setBalaDate(Date balaDate) {
		this.balaDate = balaDate;
	}

	@Column(name = "CHEQUE_NO", unique = false, nullable = true, insertable = true, updatable = true, length = 500)
	public String getChequeNo() {
		return this.chequeNo;
	}

	public void setChequeNo(String chequeNo) {
		this.chequeNo = chequeNo;
	}

	@Column(name = "RECEIPT_NO", unique = false, nullable = true, insertable = true, updatable = true, length = 500)
	public String getReceiptNo() {
		return this.receiptNo;
	}

	public void setReceiptNo(String receiptNo) {
		this.receiptNo = receiptNo;
	}

	@Column(name = "ENTRY_BY", unique = false, nullable = false, insertable = true, updatable = true, length = 16)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "ENTRY_DATE", unique = false, nullable = false, insertable = true, updatable = true, length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = false, insertable = true, updatable = true, length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", unique = false, nullable = false, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	
	@Column(name = "APP_ID", precision = 10, scale = 0)
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}
	
	@Column(name = "MONEY_UNIT", precision = 1, scale = 0)
	public Long getMoneyUnit() {
		return moneyUnit;
	}

	public void setMoneyUnit(Long moneyUnit) {
		this.moneyUnit = moneyUnit;
	}

	@Column(name = "CON_BALANCE_DEGREE", precision = 1, scale = 0)
	public Long getConBalanceDegreeLong() {
		return conBalanceDegreeLong;
	}
	
	public void setConBalanceDegreeLong(Long conBalanceDegreeLong) {
		this.conBalanceDegreeLong = conBalanceDegreeLong;
	}

}