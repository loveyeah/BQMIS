package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvCTransaction entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_C_TRANSACTION")
public class InvCTransaction implements java.io.Serializable {

	// Fields

	private Long transId;
	private String transCode;
	private String transName;
	private String transDesc;
	private String isOpenBalance;
	private String isReceive;
	private String isAdjust;
	private String isIssues;
	private String isReserved;
	private String isInspection;
	private String isSaleAmount;
	private String isEntryCost;
	private String isPoCost;
	private String isAjustCost;
	private String isActualCost;
	private String isCheckPo;
	private String isPoQuantity;
	private String isShopOrder;
	private String isCheckShopOrder;
	private String isShopOrderIssue;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public InvCTransaction() {
	}

	/** minimal constructor */
	public InvCTransaction(Long transId, String transCode, String transName,
			String isOpenBalance, String isReceive, String isAdjust,
			String isIssues, String isReserved, String isInspection,
			String isSaleAmount, String isEntryCost, String isPoCost,
			String isAjustCost, String isActualCost, String isCheckPo,
			String isPoQuantity, String isShopOrder, String isCheckShopOrder,
			String isShopOrderIssue, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.transId = transId;
		this.transCode = transCode;
		this.transName = transName;
		this.isOpenBalance = isOpenBalance;
		this.isReceive = isReceive;
		this.isAdjust = isAdjust;
		this.isIssues = isIssues;
		this.isReserved = isReserved;
		this.isInspection = isInspection;
		this.isSaleAmount = isSaleAmount;
		this.isEntryCost = isEntryCost;
		this.isPoCost = isPoCost;
		this.isAjustCost = isAjustCost;
		this.isActualCost = isActualCost;
		this.isCheckPo = isCheckPo;
		this.isPoQuantity = isPoQuantity;
		this.isShopOrder = isShopOrder;
		this.isCheckShopOrder = isCheckShopOrder;
		this.isShopOrderIssue = isShopOrderIssue;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public InvCTransaction(Long transId, String transCode, String transName,
			String transDesc, String isOpenBalance, String isReceive,
			String isAdjust, String isIssues, String isReserved,
			String isInspection, String isSaleAmount, String isEntryCost,
			String isPoCost, String isAjustCost, String isActualCost,
			String isCheckPo, String isPoQuantity, String isShopOrder,
			String isCheckShopOrder, String isShopOrderIssue,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.transId = transId;
		this.transCode = transCode;
		this.transName = transName;
		this.transDesc = transDesc;
		this.isOpenBalance = isOpenBalance;
		this.isReceive = isReceive;
		this.isAdjust = isAdjust;
		this.isIssues = isIssues;
		this.isReserved = isReserved;
		this.isInspection = isInspection;
		this.isSaleAmount = isSaleAmount;
		this.isEntryCost = isEntryCost;
		this.isPoCost = isPoCost;
		this.isAjustCost = isAjustCost;
		this.isActualCost = isActualCost;
		this.isCheckPo = isCheckPo;
		this.isPoQuantity = isPoQuantity;
		this.isShopOrder = isShopOrder;
		this.isCheckShopOrder = isCheckShopOrder;
		this.isShopOrderIssue = isShopOrderIssue;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "TRANS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTransId() {
		return this.transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}

	@Column(name = "TRANS_CODE", nullable = false, length = 6)
	public String getTransCode() {
		return this.transCode;
	}

	public void setTransCode(String transCode) {
		this.transCode = transCode;
	}

	@Column(name = "TRANS_NAME", nullable = false, length = 100)
	public String getTransName() {
		return this.transName;
	}

	public void setTransName(String transName) {
		this.transName = transName;
	}

	@Column(name = "TRANS_DESC", length = 200)
	public String getTransDesc() {
		return this.transDesc;
	}

	public void setTransDesc(String transDesc) {
		this.transDesc = transDesc;
	}

	@Column(name = "IS_OPEN_BALANCE", nullable = false, length = 1)
	public String getIsOpenBalance() {
		return this.isOpenBalance;
	}

	public void setIsOpenBalance(String isOpenBalance) {
		this.isOpenBalance = isOpenBalance;
	}

	@Column(name = "IS_RECEIVE", nullable = false, length = 1)
	public String getIsReceive() {
		return this.isReceive;
	}

	public void setIsReceive(String isReceive) {
		this.isReceive = isReceive;
	}

	@Column(name = "IS_ADJUST", nullable = false, length = 1)
	public String getIsAdjust() {
		return this.isAdjust;
	}

	public void setIsAdjust(String isAdjust) {
		this.isAdjust = isAdjust;
	}

	@Column(name = "IS_ISSUES", nullable = false, length = 1)
	public String getIsIssues() {
		return this.isIssues;
	}

	public void setIsIssues(String isIssues) {
		this.isIssues = isIssues;
	}

	@Column(name = "IS_RESERVED", nullable = false, length = 1)
	public String getIsReserved() {
		return this.isReserved;
	}

	public void setIsReserved(String isReserved) {
		this.isReserved = isReserved;
	}

	@Column(name = "IS_INSPECTION", nullable = false, length = 1)
	public String getIsInspection() {
		return this.isInspection;
	}

	public void setIsInspection(String isInspection) {
		this.isInspection = isInspection;
	}

	@Column(name = "IS_SALE_AMOUNT", nullable = false, length = 1)
	public String getIsSaleAmount() {
		return this.isSaleAmount;
	}

	public void setIsSaleAmount(String isSaleAmount) {
		this.isSaleAmount = isSaleAmount;
	}

	@Column(name = "IS_ENTRY_COST", nullable = false, length = 1)
	public String getIsEntryCost() {
		return this.isEntryCost;
	}

	public void setIsEntryCost(String isEntryCost) {
		this.isEntryCost = isEntryCost;
	}

	@Column(name = "IS_PO_COST", nullable = false, length = 1)
	public String getIsPoCost() {
		return this.isPoCost;
	}

	public void setIsPoCost(String isPoCost) {
		this.isPoCost = isPoCost;
	}

	@Column(name = "IS_AJUST_COST", nullable = false, length = 1)
	public String getIsAjustCost() {
		return this.isAjustCost;
	}

	public void setIsAjustCost(String isAjustCost) {
		this.isAjustCost = isAjustCost;
	}

	@Column(name = "IS_ACTUAL_COST", nullable = false, length = 1)
	public String getIsActualCost() {
		return this.isActualCost;
	}

	public void setIsActualCost(String isActualCost) {
		this.isActualCost = isActualCost;
	}

	@Column(name = "IS_CHECK_PO", nullable = false, length = 1)
	public String getIsCheckPo() {
		return this.isCheckPo;
	}

	public void setIsCheckPo(String isCheckPo) {
		this.isCheckPo = isCheckPo;
	}

	@Column(name = "IS_PO_QUANTITY", nullable = false, length = 1)
	public String getIsPoQuantity() {
		return this.isPoQuantity;
	}

	public void setIsPoQuantity(String isPoQuantity) {
		this.isPoQuantity = isPoQuantity;
	}

	@Column(name = "IS_SHOP_ORDER", nullable = false, length = 1)
	public String getIsShopOrder() {
		return this.isShopOrder;
	}

	public void setIsShopOrder(String isShopOrder) {
		this.isShopOrder = isShopOrder;
	}

	@Column(name = "IS_CHECK_SHOP_ORDER", nullable = false, length = 1)
	public String getIsCheckShopOrder() {
		return this.isCheckShopOrder;
	}

	public void setIsCheckShopOrder(String isCheckShopOrder) {
		this.isCheckShopOrder = isCheckShopOrder;
	}

	@Column(name = "IS_SHOP_ORDER_ISSUE", nullable = false, length = 1)
	public String getIsShopOrderIssue() {
		return this.isShopOrderIssue;
	}

	public void setIsShopOrderIssue(String isShopOrderIssue) {
		this.isShopOrderIssue = isShopOrderIssue;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", nullable = false, length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ENTERPRISE_CODE", nullable = false, length = 10)
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

}