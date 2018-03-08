package power.ejb.manage.contract.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConJConDoc entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_CON_DOC")
public class ConJConDoc implements java.io.Serializable {

	// Fields

	private Long conDocId;
	private Long keyId;
	private String docType;
	private String docName;
	private byte[] docContent;
	private String docMemo;
	private String oriFileName;
	private String oriFileExt;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public ConJConDoc() {
	}

	/** minimal constructor */
	public ConJConDoc(Long conDocId, Long keyId, String docType,
			String docName, String oriFileName, String oriFileExt,
			String lastModifiedBy, Date lastModifiedDate,
			String enterpriseCode, String isUse) {
		this.conDocId = conDocId;
		this.keyId = keyId;
		this.docType = docType;
		this.docName = docName;
		this.oriFileName = oriFileName;
		this.oriFileExt = oriFileExt;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	/** full constructor */
	public ConJConDoc(Long conDocId, Long keyId, String docType,
			String docName, byte[] docContent, String docMemo,
			String oriFileName, String oriFileExt, String lastModifiedBy,
			Date lastModifiedDate, String enterpriseCode, String isUse) {
		this.conDocId = conDocId;
		this.keyId = keyId;
		this.docType = docType;
		this.docName = docName;
		this.docContent = docContent;
		this.docMemo = docMemo;
		this.oriFileName = oriFileName;
		this.oriFileExt = oriFileExt;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "CON_DOC_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getConDocId() {
		return this.conDocId;
	}

	public void setConDocId(Long conDocId) {
		this.conDocId = conDocId;
	}

	@Column(name = "KEY_ID", nullable = false, precision = 10, scale = 0)
	public Long getKeyId() {
		return this.keyId;
	}

	public void setKeyId(Long keyId) {
		this.keyId = keyId;
	}

	@Column(name = "DOC_TYPE", nullable = false, length = 8)
	public String getDocType() {
		return this.docType;
	}

	public void setDocType(String docType) {
		this.docType = docType;
	}

	@Column(name = "DOC_NAME", nullable = false, length = 200)
	public String getDocName() {
		return this.docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	@Column(name = "DOC_CONTENT")
	public byte[] getDocContent() {
		return this.docContent;
	}

	public void setDocContent(byte[] docContent) {
		this.docContent = docContent;
	}

	@Column(name = "DOC_MEMO", length = 256)
	public String getDocMemo() {
		return this.docMemo;
	}

	public void setDocMemo(String docMemo) {
		this.docMemo = docMemo;
	}

	@Column(name = "ORI_FILE_NAME", nullable = false, length = 200)
	public String getOriFileName() {
		return this.oriFileName;
	}

	public void setOriFileName(String oriFileName) {
		this.oriFileName = oriFileName;
	}

	@Column(name = "ORI_FILE_EXT", nullable = false, length = 8)
	public String getOriFileExt() {
		return this.oriFileExt;
	}

	public void setOriFileExt(String oriFileExt) {
		this.oriFileExt = oriFileExt;
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