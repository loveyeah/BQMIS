package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PurJArrival entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PUR_J_ARRIVAL", schema = "POWER")
public class PurJArrival implements java.io.Serializable {

	// Fields

	private Long id;
	private String arrivalNo;
	private String purNo;
	private Long supplier;
	private String contractNo;
	private String invoiceNo;
	private String receiveBy;
	private Date arrivalDate;
	private String arrivalState;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String memo;
	private String enterpriseCode;
	private String isUse;
	private String checkState;
	private String checkBy;
	private Date checkDate;
	private String relationArrivalNo;

	// Constructors

	/** default constructor */
	public PurJArrival() {
	}

	/** minimal constructor */
	public PurJArrival(Long id, String arrivalNo, String purNo,
			String arrivalState, String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.id = id;
		this.arrivalNo = arrivalNo;
		this.purNo = purNo;
		this.arrivalState = arrivalState;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public PurJArrival(Long id, String arrivalNo, String purNo, Long supplier,
			String contractNo, String invoiceNo, String receiveBy,
			Date arrivalDate, String arrivalState, String lastModifiedBy,
			Date lastModifiedDate, String memo, String enterpriseCode,
			String isUse, String checkState, String checkBy, Date checkDate,
			String relationArrivalNo) {
		this.id = id;
		this.arrivalNo = arrivalNo;
		this.purNo = purNo;
		this.supplier = supplier;
		this.contractNo = contractNo;
		this.invoiceNo = invoiceNo;
		this.receiveBy = receiveBy;
		this.arrivalDate = arrivalDate;
		this.arrivalState = arrivalState;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.memo = memo;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.checkState = checkState;
		this.checkBy = checkBy;
		this.checkDate = checkDate;
		this.relationArrivalNo = relationArrivalNo;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "ARRIVAL_NO", nullable = false, length = 30)
	public String getArrivalNo() {
		return this.arrivalNo;
	}

	public void setArrivalNo(String arrivalNo) {
		this.arrivalNo = arrivalNo;
	}

	@Column(name = "PUR_NO", nullable = false, length = 30)
	public String getPurNo() {
		return this.purNo;
	}

	public void setPurNo(String purNo) {
		this.purNo = purNo;
	}

	@Column(name = "SUPPLIER", precision = 10, scale = 0)
	public Long getSupplier() {
		return this.supplier;
	}

	public void setSupplier(Long supplier) {
		this.supplier = supplier;
	}

	@Column(name = "CONTRACT_NO", length = 30)
	public String getContractNo() {
		return this.contractNo;
	}

	public void setContractNo(String contractNo) {
		this.contractNo = contractNo;
	}

	@Column(name = "INVOICE_NO", length = 30)
	public String getInvoiceNo() {
		return this.invoiceNo;
	}

	public void setInvoiceNo(String invoiceNo) {
		this.invoiceNo = invoiceNo;
	}

	@Column(name = "RECEIVE_BY", length = 16)
	public String getReceiveBy() {
		return this.receiveBy;
	}

	public void setReceiveBy(String receiveBy) {
		this.receiveBy = receiveBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ARRIVAL_DATE", length = 7)
	public Date getArrivalDate() {
		return this.arrivalDate;
	}

	public void setArrivalDate(Date arrivalDate) {
		this.arrivalDate = arrivalDate;
	}

	@Column(name = "ARRIVAL_STATE", nullable = false, length = 1)
	public String getArrivalState() {
		return this.arrivalState;
	}

	public void setArrivalState(String arrivalState) {
		this.arrivalState = arrivalState;
	}

	@Column(name = "LAST_MODIFIED_BY", nullable = false, length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.DATE)
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

	@Column(name = "CHECK_STATE", length = 1)
	public String getCheckState() {
		return this.checkState;
	}

	public void setCheckState(String checkState) {
		this.checkState = checkState;
	}

	@Column(name = "CHECK_BY", length = 16)
	public String getCheckBy() {
		return this.checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

	// modified by liuyi 091126 为与月结表中的时间进行比较
//	@Temporal(TemporalType.DATE)
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CHECK_DATE", length = 7)
	public Date getCheckDate() {
		return this.checkDate;
	}

	public void setCheckDate(Date checkDate) {
		this.checkDate = checkDate;
	}

	@Column(name = "RELATION_ARRIVAL_NO", length = 30)
	public String getRelationArrivalNo() {
		return this.relationArrivalNo;
	}

	public void setRelationArrivalNo(String relationArrivalNo) {
		this.relationArrivalNo = relationArrivalNo;
	}

}