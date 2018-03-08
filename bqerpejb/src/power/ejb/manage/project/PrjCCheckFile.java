package power.ejb.manage.project;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PrjCCheckFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PRJ_C_CHECK_FILE", schema = "POWER")
public class PrjCCheckFile implements java.io.Serializable {

	// Fields

	private Long checkFileId;
	private Long fileType;
	private String fileNo;
	private String fileName;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String isModel;
	private String enterpriseCode;
	private String isUse;
	private String fileUrl;

	// Constructors

	/** default constructor */
	public PrjCCheckFile() {
	}

	/** minimal constructor */
	public PrjCCheckFile(Long checkFileId) {
		this.checkFileId = checkFileId;
	}

	/** full constructor */
	public PrjCCheckFile(Long checkFileId, Long fileType, String fileNo,
			String fileName, String lastModifiedBy, Date lastModifiedDate,
			String isModel, String enterpriseCode, String isUse, String fileUrl) {
		this.checkFileId = checkFileId;
		this.fileType = fileType;
		this.fileNo = fileNo;
		this.fileName = fileName;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.isModel = isModel;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.fileUrl = fileUrl;
	}

	// Property accessors
	@Id
	@Column(name = "CHECK_FILE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getCheckFileId() {
		return this.checkFileId;
	}

	public void setCheckFileId(Long checkFileId) {
		this.checkFileId = checkFileId;
	}

	@Column(name = "FILE_TYPE", precision = 1, scale = 0)
	public Long getFileType() {
		return this.fileType;
	}

	public void setFileType(Long fileType) {
		this.fileType = fileType;
	}

	@Column(name = "FILE_NO", length = 100)
	public String getFileNo() {
		return this.fileNo;
	}

	public void setFileNo(String fileNo) {
		this.fileNo = fileNo;
	}

	@Column(name = "FILE_NAME", length = 100)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "IS_MODEL", length = 1)
	public String getIsModel() {
		return this.isModel;
	}

	public void setIsModel(String isModel) {
		this.isModel = isModel;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
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

	@Column(name = "FILE_URL", length = 100)
	public String getFileUrl() {
		return this.fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

}