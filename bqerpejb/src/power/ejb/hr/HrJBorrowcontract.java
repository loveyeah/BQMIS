package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJBorrowcontract entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_BORROWCONTRACT")
public class HrJBorrowcontract implements java.io.Serializable {

	// Fields

	private Long borrowcontractid;
	private Long cooperateUnitId;
	private String wrokContractNo;
	private Date signatureDate;
	private Date startDate;
	private Date endDate;
	private String contractContent;
	private String dcmStatus;
	private String note;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	
	private String transferType;

	// Constructors

	/** default constructor */
	public HrJBorrowcontract() {
	}

	/** minimal constructor */
	public HrJBorrowcontract(Long borrowcontractid) {
		this.borrowcontractid = borrowcontractid;
	}

	/** full constructor */
	public HrJBorrowcontract(Long borrowcontractid, Long cooperateUnitId,
			String wrokContractNo, Date signatureDate, Date startDate,
			Date endDate, String contractContent, String dcmStatus,
			String note, String isUse, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode,String transferType) {
		this.borrowcontractid = borrowcontractid;
		this.cooperateUnitId = cooperateUnitId;
		this.wrokContractNo = wrokContractNo;
		this.signatureDate = signatureDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.contractContent = contractContent;
		this.dcmStatus = dcmStatus;
		this.note = note;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.transferType = transferType;
	}

	// Property accessors
	@Id
	@Column(name = "BORROWCONTRACTID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBorrowcontractid() {
		return this.borrowcontractid;
	}

	public void setBorrowcontractid(Long borrowcontractid) {
		this.borrowcontractid = borrowcontractid;
	}

	@Column(name = "COOPERATE_UNIT_ID", precision = 10, scale = 0)
	public Long getCooperateUnitId() {
		return this.cooperateUnitId;
	}

	public void setCooperateUnitId(Long cooperateUnitId) {
		this.cooperateUnitId = cooperateUnitId;
	}

	@Column(name = "WROK_CONTRACT_NO", length = 6)
	public String getWrokContractNo() {
		return this.wrokContractNo;
	}

	public void setWrokContractNo(String wrokContractNo) {
		this.wrokContractNo = wrokContractNo;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "SIGNATURE_DATE", length = 7)
	public Date getSignatureDate() {
		return this.signatureDate;
	}

	public void setSignatureDate(Date signatureDate) {
		this.signatureDate = signatureDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "CONTRACT_CONTENT", length = 256)
	public String getContractContent() {
		return this.contractContent;
	}

	public void setContractContent(String contractContent) {
		this.contractContent = contractContent;
	}

	@Column(name = "DCM_STATUS", length = 1)
	public String getDcmStatus() {
		return this.dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

	@Column(name = "NOTE", length = 256)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "TRANSFER_TYPE", length = 1)
	public String getTransferType() {
		return transferType;
	}

	public void setTransferType(String transferType) {
		this.transferType = transferType;
	}

}