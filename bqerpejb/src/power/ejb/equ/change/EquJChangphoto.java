package power.ejb.equ.change;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * EquJChangphoto entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_CHANGPHOTO")
public class EquJChangphoto implements java.io.Serializable {

	// Fields

	private Long changePhotoId;
	private String equChangeNo;
	private String oldFileName;
	private byte[] oldPhoto;
	private String newFileName;
	private byte[] newPhoto;
	private String enterpriseCode;
	private String isUse;

	// Constructors

	/** default constructor */
	public EquJChangphoto() {
	}

	/** minimal constructor */
	public EquJChangphoto(Long changePhotoId) {
		this.changePhotoId = changePhotoId;
	}

	/** full constructor */
	public EquJChangphoto(Long changePhotoId, String equChangeNo,
			String oldFileName, byte[] oldPhoto, String newFileName,
			byte[] newPhoto, String enterpriseCode, String isUse) {
		this.changePhotoId = changePhotoId;
		this.equChangeNo = equChangeNo;
		this.oldFileName = oldFileName;
		this.oldPhoto = oldPhoto;
		this.newFileName = newFileName;
		this.newPhoto = newPhoto;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
	}

	// Property accessors
	@Id
	@Column(name = "CHANGE_PHOTO_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getChangePhotoId() {
		return this.changePhotoId;
	}

	public void setChangePhotoId(Long changePhotoId) {
		this.changePhotoId = changePhotoId;
	}

	@Column(name = "EQU_CHANGE_NO", length = 20)
	public String getEquChangeNo() {
		return this.equChangeNo;
	}

	public void setEquChangeNo(String equChangeNo) {
		this.equChangeNo = equChangeNo;
	}

	@Column(name = "OLD_FILE_NAME", length = 100)
	public String getOldFileName() {
		return this.oldFileName;
	}

	public void setOldFileName(String oldFileName) {
		this.oldFileName = oldFileName;
	}

	@Column(name = "OLD_PHOTO")
	public byte[] getOldPhoto() {
		return this.oldPhoto;
	}

	public void setOldPhoto(byte[] oldPhoto) {
		this.oldPhoto = oldPhoto;
	}

	@Column(name = "NEW_FILE_NAME", length = 100)
	public String getNewFileName() {
		return this.newFileName;
	}

	public void setNewFileName(String newFileName) {
		this.newFileName = newFileName;
	}

	@Column(name = "NEW_PHOTO")
	public byte[] getNewPhoto() {
		return this.newPhoto;
	}

	public void setNewPhoto(byte[] newPhoto) {
		this.newPhoto = newPhoto;
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

}