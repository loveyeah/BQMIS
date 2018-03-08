package power.ejb.hr;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJCarwhInvoice entity.
 *
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_CARWH_INVOICE")
public class HrJCarwhInvoice implements java.io.Serializable {

	// Fields
	private static final long serialVersionUID = 1L;
	private Long fileId;
	private Long workcontractid;
	private String fileType;
	private String fileKind;
	private String fileName;
	private byte[] fileText;
	private String fileOriger;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Date insertdate;
	private String insertby;

	// Constructors

	/** default constructor */
	public HrJCarwhInvoice() {
	}

	/** minimal constructor */
	public HrJCarwhInvoice(Long fileId, Long workcontractid) {
		this.fileId = fileId;
		this.workcontractid = workcontractid;
	}

	/** full constructor */
	public HrJCarwhInvoice(Long fileId, Long workcontractid, String fileType,
			String fileKind, String fileName, byte[] fileText,
			String fileOriger, String enterpriseCode, String isUse,
			String lastModifiedBy, Date lastModifiedDate, Date insertdate,
			String insertby) {
		this.fileId = fileId;
		this.workcontractid = workcontractid;
		this.fileType = fileType;
		this.fileKind = fileKind;
		this.fileName = fileName;
		this.fileText = fileText;
		this.fileOriger = fileOriger;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.insertdate = insertdate;
		this.insertby = insertby;
	}

	// Property accessors
	@Id
	@Column(name = "FILE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFileId() {
		return this.fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	@Column(name = "WORKCONTRACTID", nullable = false, precision = 10, scale = 0)
	public Long getWorkcontractid() {
		return this.workcontractid;
	}

	public void setWorkcontractid(Long workcontractid) {
		this.workcontractid = workcontractid;
	}

	@Column(name = "FILE_TYPE", length = 2)
	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Column(name = "FILE_KIND", length = 2)
	public String getFileKind() {
		return this.fileKind;
	}

	public void setFileKind(String fileKind) {
		this.fileKind = fileKind;
	}

	@Column(name = "FILE_NAME", length = 120)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "FILE_TEXT")
	public byte[] getFileText() {
		return this.fileText;
	}

	public void setFileText(byte[] fileText) {
		this.fileText = fileText;
	}

	@Column(name = "FILE_ORIGER", length = 1)
	public String getFileOriger() {
		return this.fileOriger;
	}

	public void setFileOriger(String fileOriger) {
		this.fileOriger = fileOriger;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

}