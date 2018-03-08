package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PurJOrderDetails entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PUR_J_ORDER_DETAILS")
public class PurJOrderDetails implements java.io.Serializable {

	// Fields

	private Long purOrderDetailsId;
	private String purNo;
	private Long purLine;
	private Long materialId;
	private String receiptWhs;
	private String receiptLocation;
	private Double purQty;
	private Double rcvQty;
	private Double insQty;
	private Double unitPrice;
	private Long currencyId;
	private Long purUmId;
	private Double taxRate;
	private Double exchageRate;
	private Date dueDate;
	private String qaControlFlag;
	private String memo;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;
	private Double taxCount;
	
	// Constructors

	/** default constructor */
	public PurJOrderDetails() {
	}

	/** minimal constructor */
	public PurJOrderDetails(Long purOrderDetailsId, String purNo,
			Long materialId, Double purQty, Double rcvQty, Double insQty,
			Double unitPrice, Long currencyId, Long purUmId, Double taxRate,
			Double exchageRate, Date dueDate, String qaControlFlag,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse,Double taxCount) {
		this.purOrderDetailsId = purOrderDetailsId;
		this.purNo = purNo;
		this.materialId = materialId;
		this.purQty = purQty;
		this.rcvQty = rcvQty;
		this.insQty = insQty;
		this.unitPrice = unitPrice;
		this.currencyId = currencyId;
		this.purUmId = purUmId;
		this.taxRate = taxRate;
		this.exchageRate = exchageRate;
		this.dueDate = dueDate;
		this.qaControlFlag = qaControlFlag;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.taxCount = taxCount;
	}

	/** full constructor */
	public PurJOrderDetails(Long purOrderDetailsId, String purNo, Long purLine,
			Long materialId, String receiptWhs, String receiptLocation,
			Double purQty, Double rcvQty, Double insQty, Double unitPrice,
			Long currencyId, Long purUmId, Double taxRate, Double exchageRate,
			Date dueDate, String qaControlFlag, String memo,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse,Double taxCount) {
		this.purOrderDetailsId = purOrderDetailsId;
		this.purNo = purNo;
		this.purLine = purLine;
		this.materialId = materialId;
		this.receiptWhs = receiptWhs;
		this.receiptLocation = receiptLocation;
		this.purQty = purQty;
		this.rcvQty = rcvQty;
		this.insQty = insQty;
		this.unitPrice = unitPrice;
		this.currencyId = currencyId;
		this.purUmId = purUmId;
		this.taxRate = taxRate;
		this.exchageRate = exchageRate;
		this.dueDate = dueDate;
		this.qaControlFlag = qaControlFlag;
		this.memo = memo;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.taxCount = taxCount;
	}

	// Property accessors
	@Id
	@Column(name = "PUR_ORDER_DETAILS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPurOrderDetailsId() {
		return this.purOrderDetailsId;
	}

	public void setPurOrderDetailsId(Long purOrderDetailsId) {
		this.purOrderDetailsId = purOrderDetailsId;
	}

	@Column(name = "PUR_NO", nullable = false, length = 20)
	public String getPurNo() {
		return this.purNo;
	}

	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}

	@Column(name = "PUR_LINE", precision = 10, scale = 0)
	public Long getPurLine() {
		return this.purLine;
	}

	public void setPurLine(Long purLine) {
		this.purLine = purLine;
	}

	@Column(name = "MATERIAL_ID", nullable = false, precision = 10, scale = 0)
	public Long getMaterialId() {
		return this.materialId;
	}

	public void setMaterialId(Long materialId) {
		this.materialId = materialId;
	}

	@Column(name = "RECEIPT_WHS", length = 10)
	public String getReceiptWhs() {
		return this.receiptWhs;
	}

	public void setReceiptWhs(String receiptWhs) {
		this.receiptWhs = receiptWhs;
	}

	@Column(name = "RECEIPT_LOCATION", length = 10)
	public String getReceiptLocation() {
		return this.receiptLocation;
	}

	public void setReceiptLocation(String receiptLocation) {
		this.receiptLocation = receiptLocation;
	}

	@Column(name = "PUR_QTY", nullable = false, precision = 15, scale = 4)
	public Double getPurQty() {
		return this.purQty;
	}

	public void setPurQty(Double purQty) {
		this.purQty = purQty;
	}

	@Column(name = "RCV_QTY", nullable = false, precision = 15, scale = 4)
	public Double getRcvQty() {
		return this.rcvQty;
	}

	public void setRcvQty(Double rcvQty) {
		this.rcvQty = rcvQty;
	}

	@Column(name = "INS_QTY", nullable = false, precision = 15, scale = 4)
	public Double getInsQty() {
		return this.insQty;
	}

	public void setInsQty(Double insQty) {
		this.insQty = insQty;
	}

	@Column(name = "UNIT_PRICE", nullable = false, precision = 18, scale = 5)
	public Double getUnitPrice() {
		return this.unitPrice;
	}

	public void setUnitPrice(Double unitPrice) {
		this.unitPrice = unitPrice;
	}

	@Column(name = "CURRENCY_ID", nullable = false, precision = 10, scale = 0)
	public Long getCurrencyId() {
		return this.currencyId;
	}

	public void setCurrencyId(Long currencyId) {
		this.currencyId = currencyId;
	}

	@Column(name = "PUR_UM_ID", nullable = false, precision = 10, scale = 0)
	public Long getPurUmId() {
		return this.purUmId;
	}

	public void setPurUmId(Long purUmId) {
		this.purUmId = purUmId;
	}

	@Column(name = "TAX_RATE", nullable = false, precision = 18, scale = 5)
	public Double getTaxRate() {
		return this.taxRate;
	}

	public void setTaxRate(Double taxRate) {
		this.taxRate = taxRate;
	}

	@Column(name = "EXCHAGE_RATE", nullable = false, precision = 18, scale = 5)
	public Double getExchageRate() {
		return this.exchageRate;
	}

	public void setExchageRate(Double exchageRate) {
		this.exchageRate = exchageRate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DUE_DATE", nullable = false, length = 7)
	public Date getDueDate() {
		return this.dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	@Column(name = "QA_CONTROL_FLAG", nullable = false, length = 1)
	public String getQaControlFlag() {
		return this.qaControlFlag;
	}

	public void setQaControlFlag(String qaControlFlag) {
		this.qaControlFlag = qaControlFlag;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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

	@Column(name = "TAX_COUNT", precision = 18, scale = 5)
	public Double getTaxCount() {
		return taxCount;
	}

	public void setTaxCount(Double taxCount) {
		this.taxCount = taxCount;
	}

}