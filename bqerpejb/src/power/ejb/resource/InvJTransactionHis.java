package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvJTransactionHis entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_J_TRANSACTION_HIS")
public class InvJTransactionHis implements java.io.Serializable {

	// Fields

	private Long transHisId;
	private String orderNo;
	private Long sequenceId;
	private Long materialId;
	private Long transId;
	private Long reasonId;
	private String lotNo;
	private Double transQty;
	private Long transUmId;
	private Double price;
	private Double actCost;
	private Double stdCost;
	private Long oriCurrencyId;
	private Long currencyId;
	private Double exchangeRate;
	private Double taxRate;
	private String fromWhsNo;
	private String fromLocationNo;
	private String toWhsNo;
	private String toLocationNo;
	private String customerNo;
	private Long supplier;
	private String manufacturerNo;
	private String receiveMan;
	private String receiveDept;
	private String costMan;
	private String costDept;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String memo;
	private String enterpriseCode;
	private String isUse;
	private String arrivalNo;
	private Long rollBackId;
	
	private Double taxCount;

	// Constructors

	/** default constructor */
	public InvJTransactionHis() {
	}

	/** minimal constructor */
	public InvJTransactionHis(Long transHisId, String orderNo, Long sequenceId,
			Long materialId, Double transQty, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.transHisId = transHisId;
		this.orderNo = orderNo;
		this.sequenceId = sequenceId;
		this.materialId = materialId;
		this.transQty = transQty;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public InvJTransactionHis(Long transHisId, String orderNo, Long sequenceId,
			Long materialId, Long transId, Long reasonId, String lotNo,
			Double transQty, Long transUmId, Double price, Double actCost,
			Double stdCost, Long oriCurrencyId, Long currencyId,
			Double exchangeRate, Double taxRate, String fromWhsNo,
			String fromLocationNo, String toWhsNo, String toLocationNo,
			String customerNo, Long supplier, String manufacturerNo,
			String receiveMan, String receiveDept, String costMan,
			String costDept, String lastModifiedBy, Date lastModifiedDate,
			String memo, String enterpriseCode, String isUse,String arrivalNo) {
		this.transHisId = transHisId;
		this.orderNo = orderNo;
		this.sequenceId = sequenceId;
		this.materialId = materialId;
		this.transId = transId;
		this.reasonId = reasonId;
		this.lotNo = lotNo;
		this.transQty = transQty;
		this.transUmId = transUmId;
		this.price = price;
		this.actCost = actCost;
		this.stdCost = stdCost;
		this.oriCurrencyId = oriCurrencyId;
		this.currencyId = currencyId;
		this.exchangeRate = exchangeRate;
		this.taxRate = taxRate;
		this.fromWhsNo = fromWhsNo;
		this.fromLocationNo = fromLocationNo;
		this.toWhsNo = toWhsNo;
		this.toLocationNo = toLocationNo;
		this.customerNo = customerNo;
		this.supplier = supplier;
		this.manufacturerNo = manufacturerNo;
		this.receiveMan = receiveMan;
		this.receiveDept = receiveDept;
		this.costMan = costMan;
		this.costDept = costDept;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.arrivalNo=arrivalNo;
	}

	// Property accessors
	@Id
	@Column(name = "TRANS_HIS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTransHisId() {
		return this.transHisId;
	}

	public void setTransHisId(Long transHisId) {
		this.transHisId = transHisId;
	}

	@Column(name = "ORDER_NO", nullable = false, length = 30)
	public String getOrderNo() {
		return this.orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	@Column(name = "SEQUENCE_ID", nullable = false, precision = 10, scale = 0)
	public Long getSequenceId() {
		return this.sequenceId;
	}

	public void setSequenceId(Long sequenceId) {
		this.sequenceId = sequenceId;
	}

	@Column(name = "MATERIAL_ID", nullable = false, precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "TRANS_ID", precision = 10, scale = 0)
	public Long getTransId() {
		return this.transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}

	@Column(name = "REASON_ID", precision = 10, scale = 0)
	public Long getReasonId() {
		return this.reasonId;
	}

	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}

	@Column(name = "LOT_NO", length = 30)
	public String getLotNo() {
		return this.lotNo;
	}

	public void setLotNo(String lotNo) {
		this.lotNo = lotNo;
	}

	@Column(name = "TRANS_QTY", nullable = false, precision = 15, scale = 4)
	public Double getTransQty() {
		return this.transQty;
	}

	public void setTransQty(Double transQty) {
		this.transQty = transQty;
	}

	@Column(name = "TRANS_UM_ID", precision = 10, scale = 0)
	public Long getTransUmId() {
		return this.transUmId;
	}

	public void setTransUmId(Long transUmId) {
		this.transUmId = transUmId;
	}

	@Column(name = "PRICE", precision = 18, scale = 5)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "ACT_COST", precision = 18, scale = 5)
	public Double getActCost() {
		return this.actCost;
	}

	public void setActCost(Double actCost) {
		this.actCost = actCost;
	}

	@Column(name = "STD_COST", precision = 18, scale = 5)
	public Double getStdCost() {
		return this.stdCost;
	}

	public void setStdCost(Double stdCost) {
		this.stdCost = stdCost;
	}

	@Column(name = "ORI_CURRENCY_ID", precision = 10, scale = 0)
	public Long getOriCurrencyId() {
		return this.oriCurrencyId;
	}

	public void setOriCurrencyId(Long oriCurrencyId) {
		this.oriCurrencyId = oriCurrencyId;
	}

	@Column(name = "CURRENCY_ID", precision = 10, scale = 0)
	public Long getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name = "EXCHANGE_RATE", precision = 18, scale = 5)
	public Double getExchangeRate() {
		return this.exchangeRate;
	}

	public void setExchangeRate(Double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	@Column(name = "TAX_RATE", precision = 18, scale = 5)
	public Double getTaxRate() {
		return this.taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	@Column(name = "FROM_WHS_NO", length = 10)
	public String getFromWhsNo() {
		return this.fromWhsNo;
	}

	public void setFromWhsNo(String fromWhsNo) {
		this.fromWhsNo = fromWhsNo;
	}

	@Column(name = "FROM_LOCATION_NO", length = 10)
	public String getFromLocationNo() {
		return this.fromLocationNo;
	}

	public void setFromLocationNo(String fromLocationNo) {
		this.fromLocationNo = fromLocationNo;
	}

	@Column(name = "TO_WHS_NO", length = 10)
	public String getToWhsNo() {
		return this.toWhsNo;
	}

	public void setToWhsNo(String toWhsNo) {
		this.toWhsNo = toWhsNo;
	}

	@Column(name = "TO_LOCATION_NO", length = 10)
	public String getToLocationNo() {
		return this.toLocationNo;
	}

	public void setToLocationNo(String toLocationNo) {
		this.toLocationNo = toLocationNo;
	}

	@Column(name = "CUSTOMER_NO", length = 100)
	public String getCustomerNo() {
		return this.customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	@Column(name = "SUPPLIER", precision = 10, scale = 0)
	public Long getSupplier() {
		return this.supplier;
	}

	public void setSupplier(Long supplier) {
		this.supplier = supplier;
	}

	@Column(name = "MANUFACTURER_NO", length = 100)
	public String getManufacturerNo() {
		return this.manufacturerNo;
	}

	public void setManufacturerNo(String manufacturerNo) {
		this.manufacturerNo = manufacturerNo;
	}

	@Column(name = "RECEIVE_MAN", length = 16)
	public String getReceiveMan() {
		return this.receiveMan;
	}

	public void setReceiveMan(String receiveMan) {
		this.receiveMan = receiveMan;
	}

	@Column(name = "RECEIVE_DEPT", length = 10)
	public String getReceiveDept() {
		return this.receiveDept;
	}

	public void setReceiveDept(String receiveDept) {
		this.receiveDept = receiveDept;
	}

	@Column(name = "COST_MAN", length = 16)
	public String getCostMan() {
		return this.costMan;
	}

	public void setCostMan(String costMan) {
		this.costMan = costMan;
	}

	@Column(name = "COST_DEPT", length = 10)
	public String getCostDept() {
		return this.costDept;
	}

	public void setCostDept(String costDept) {
		this.costDept = costDept;
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

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	@Column(name = "ARRIVAL_NO",length = 30)
	public String getArrivalNo() {
		return arrivalNo;
	}
	
	public void setArrivalNo(String arrivalNo) {
		this.arrivalNo = arrivalNo;
	}

	@Column(name = "ROLL_BACK_ID", precision = 10, scale = 0)
	public Long getRollBackId() {
		return rollBackId;
	}

	public void setRollBackId(Long rollBackId) {
		this.rollBackId = rollBackId;
	}
	
	@Column(name = "TAX_COUNT", precision = 18, scale = 5)
	public Double getTaxCount() {
		return taxCount;
	}

	public void setTaxCount(Double taxCount) {
		this.taxCount = taxCount;
	}

}