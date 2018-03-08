package power.ejb.manage.contract;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConCSignatory entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_C_SIGNATORY")
public class ConCSignatory implements java.io.Serializable {

	// Fields

	private Long signId;
	private String signatoryBy;
	private byte[] signatoryFile;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public ConCSignatory() {
	}

	/** minimal constructor */
	public ConCSignatory(Long signId, String signatoryBy,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.signId = signId;
		this.signatoryBy = signatoryBy;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public ConCSignatory(Long signId, String signatoryBy, byte[] signatoryFile,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.signId = signId;
		this.signatoryBy = signatoryBy;
		this.signatoryFile = signatoryFile;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "SIGN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSignId() {
		return this.signId;
	}

	public void setSignId(Long signId) {
		this.signId = signId;
	}

	@Column(name = "SIGNATORY_BY", nullable = false, length = 16)
	public String getSignatoryBy() {
		return this.signatoryBy;
	}

	public void setSignatoryBy(String signatoryBy) {
		this.signatoryBy = signatoryBy;
	}

	@Column(name = "SIGNATORY_FILE")
	public byte[] getSignatoryFile() {
		return this.signatoryFile;
	}

	public void setSignatoryFile(byte[] signatoryFile) {
		this.signatoryFile = signatoryFile;
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

}