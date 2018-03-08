package power.ejb.administration.form;

public class AdJCarwhInvoiceBean implements java.io.Serializable{
	private Long id;
	private String whId;
	private String fileType;
	private String fileKind;
	private String fileName;
	private String fileText;
	private String isUse;
	private String updateUser;
	private String updateTime;
	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}
	/**
	 * @return the whId
	 */
	public String getWhId() {
		return whId;
	}
	/**
	 * @param whId the whId to set
	 */
	public void setWhId(String whId) {
		this.whId = whId;
	}
	/**
	 * @return the fileType
	 */
	public String getFileType() {
		return fileType;
	}
	/**
	 * @param fileType the fileType to set
	 */
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	/**
	 * @return the fileKind
	 */
	public String getFileKind() {
		return fileKind;
	}
	/**
	 * @param fileKind the fileKind to set
	 */
	public void setFileKind(String fileKind) {
		this.fileKind = fileKind;
	}
	/**
	 * @return the fileName
	 */
	public String getFileName() {
		return fileName;
	}
	/**
	 * @param fileName the fileName to set
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	/**
	 * @return the fileText
	 */
	public String getFileText() {
		return fileText;
	}
	/**
	 * @param fileText the fileText to set
	 */
	public void setFileText(String fileText) {
		this.fileText = fileText;
	}
	/**
	 * @return the isUse
	 */
	public String getIsUse() {
		return isUse;
	}
	/**
	 * @param isUse the isUse to set
	 */
	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}
	/**
	 * @return the updateUser
	 */
	public String getUpdateUser() {
		return updateUser;
	}
	/**
	 * @param updateUser the updateUser to set
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}
	/**
	 * @return the updateTime
	 */
	public String getUpdateTime() {
		return updateTime;
	}
	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
}
