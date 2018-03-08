package power.ejb.administration.form;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * AdJOutQuestFile entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "AD_J_OUT_QUEST_FILE")
public class AdJOutQuestFileInfo implements java.io.Serializable {

	// Fields

	private Long id;
	private String applyId;
	private String fileType;
	private String fileKind;
	private String fileName;
	private byte[] fileText;
	private String isUse;
	private String updateUser;
	private Date updateTime;

	// Constructors

	/** default constructor */
	public AdJOutQuestFileInfo() {
	}

	/** minimal constructor */
	public AdJOutQuestFileInfo(Long id) {
		this.id = id;
	}

	/** full constructor */
	public AdJOutQuestFileInfo(Long id, String applyId, String fileType,
			String fileKind, String fileName, byte[] fileText,
			String isUse, String updateUser, Date updateTime) {
		this.id = id;
		this.applyId = applyId;
		this.fileType = fileType;
		this.fileKind = fileKind;
		this.fileName = fileName;
		this.fileText = fileText;
		this.isUse = isUse;
		this.updateUser = updateUser;
		this.updateTime = updateTime;
	}

	// Property accessors
	@Id
	@Column(name = "ID", nullable = false, precision = 10, scale = 0)
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Column(name = "APPLY_ID", length = 12)
	public String getApplyId() {
		return this.applyId;
	}

	public void setApplyId(String applyId) {
		this.applyId = applyId;
	}

	@Column(name = "FILE_TYPE", length = 40)
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

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "UPDATE_USER", length = 10)
	public String getUpdateUser() {
		return this.updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "UPDATE_TIME", length = 7)
	public Date getUpdateTime() {
		return this.updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
