package power.ejb.resource;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * InvCReason entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "INV_C_REASON")
public class InvCReason implements java.io.Serializable {

	// Fields

	private Long reasonId;
	private Long transId;
	private String reasonCode;
	private String reasonName;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public InvCReason() {
	}

	/** minimal constructor */
	public InvCReason(Long reasonId, Long transId, String reasonCode,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.reasonId = reasonId;
		this.transId = transId;
		this.reasonCode = reasonCode;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public InvCReason(Long reasonId, Long transId, String reasonCode,
			String reasonName, String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.reasonId = reasonId;
		this.transId = transId;
		this.reasonCode = reasonCode;
		this.reasonName = reasonName;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "REASON_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getReasonId() {
		return this.reasonId;
	}

	public void setReasonId(Long reasonId) {
		this.reasonId = reasonId;
	}

	@Column(name = "TRANS_ID", nullable = false, precision = 10, scale = 0)
	public Long getTransId() {
		return this.transId;
	}

	public void setTransId(Long transId) {
		this.transId = transId;
	}

	@Column(name = "REASON_CODE", nullable = false, length = 10)
	public String getReasonCode() {
		return this.reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	@Column(name = "REASON_NAME", length = 100)
	public String getReasonName() {
		return this.reasonName;
	}

	public void setReasonName(String reasonName) {
		this.reasonName = reasonName;
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