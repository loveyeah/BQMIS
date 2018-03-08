package power.ejb.system;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

 
/**
 * 功能模块实体
 * @author wzhyan  
 */
@Entity
@Table(name = "SYS_C_FLS") 
public class SysCFls implements java.io.Serializable {
 
	/**
	 * 
	 */
	private static final long serialVersionUID = 554920388846348284L;
	private Long fileId;
	private String fileName;
	private Long parentFileId;
	private String fileType;
	private String isFile;
	private String memo;
	private String isUse;
	private String openType;
	private String fileAddr;
	private Long line;
	private String isDisp;
	private String isPersonly;
	private String enterpriseCode;
	private String modifyBy;
	private Date modifyDate;

	// Constructors

	/** default constructor */
	public SysCFls() {
	}

	/** minimal constructor */
	public SysCFls(Long fileId) {
		this.fileId = fileId;
	}

	/** full constructor */
	public SysCFls(Long fileId, String fileName, Long parentFileId,
			String fileType, String isFile, String memo, String isUse,
			String openType, String fileAddr, Long line, String isDisp,
			String isPersonly, String enterpriseCode, String modifyBy,
			Date modifyDate) {
		this.fileId = fileId;
		this.fileName = fileName;
		this.parentFileId = parentFileId;
		this.fileType = fileType;
		this.isFile = isFile;
		this.memo = memo;
		this.isUse = isUse;
		this.openType = openType;
		this.fileAddr = fileAddr;
		this.line = line;
		this.isDisp = isDisp;
		this.isPersonly = isPersonly;
		this.enterpriseCode = enterpriseCode;
		this.modifyBy = modifyBy;
		this.modifyDate = modifyDate;
	}

	// Property accessors
	@Id
	@Column(name = "FILE_ID",  nullable = false, precision = 10, scale = 0) 
	public Long getFileId() {
		return this.fileId;
	}

	public void setFileId(Long fileId) {
		this.fileId = fileId;
	}

	@Column(name = "FILE_NAME", length = 50)
	public String getFileName() {
		return this.fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Column(name = "PARENT_FILE_ID", precision = 10, scale = 0)
	public Long getParentFileId() {
		return this.parentFileId;
	}

	public void setParentFileId(Long parentFileId) {
		this.parentFileId = parentFileId;
	}

	@Column(name = "FILE_TYPE", length = 1)
	public String getFileType() {
		return this.fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Column(name = "IS_FILE", length = 1)
	public String getIsFile() {
		return this.isFile;
	}

	public void setIsFile(String isFile) {
		this.isFile = isFile;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "OPEN_TYPE", length = 1)
	public String getOpenType() {
		return this.openType;
	}

	public void setOpenType(String openType) {
		this.openType = openType;
	}

	@Column(name = "FILE_ADDR", length = 100)
	public String getFileAddr() {
		return this.fileAddr;
	}

	public void setFileAddr(String fileAddr) {
		this.fileAddr = fileAddr;
	}

	@Column(name = "LINE", precision = 10, scale = 0)
	public Long getLine() {
		return this.line;
	}

	public void setLine(Long line) {
		this.line = line;
	}

	@Column(name = "IS_DISP", length = 1)
	public String getIsDisp() {
		return this.isDisp;
	}

	public void setIsDisp(String isDisp) {
		this.isDisp = isDisp;
	}

	@Column(name = "IS_PERSONLY", length = 1)
	public String getIsPersonly() {
		return this.isPersonly;
	}

	public void setIsPersonly(String isPersonly) {
		this.isPersonly = isPersonly;
	}

	@Column(name = "ENTERPRISE_CODE", length = 30)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "MODIFY_BY", length = 30)
	public String getModifyBy() {
		return this.modifyBy;
	}

	public void setModifyBy(String modifyBy) {
		this.modifyBy = modifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MODIFY_DATE", length = 7)
	public Date getModifyDate() {
		return this.modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

}