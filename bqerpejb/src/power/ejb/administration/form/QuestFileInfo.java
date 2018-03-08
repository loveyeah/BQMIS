/**
　* Copyright ustcsoft.com
　* All right reserved.
*/
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * OndutyInfo entity.
 * 
 * @author sufeiyu
 */
@SuppressWarnings("serial")
public class QuestFileInfo implements Serializable {
	// Fields

	private Long idFile;
	private String applyIdFile;
	private String fileTypeFile;
	private String fileKindFile;
	private String fileNameFile;
	private byte[] fileTextFile;
	private String isUseFile;
	private String updateUserFile;
	private String updateTimeFile;
	/**
	 * @return the idFile
	 */
	public Long getIdFile() {
		return idFile;
	}
	/**
	 * @param idFile the idFile to set
	 */
	public void setIdFile(Long idFile) {
		this.idFile = idFile;
	}
	/**
	 * @return the applyIdFile
	 */
	public String getApplyIdFile() {
		return applyIdFile;
	}
	/**
	 * @param applyIdFile the applyIdFile to set
	 */
	public void setApplyIdFile(String applyIdFile) {
		this.applyIdFile = applyIdFile;
	}
	/**
	 * @return the fileTypeFile
	 */
	public String getFileTypeFile() {
		return fileTypeFile;
	}
	/**
	 * @param fileTypeFile the fileTypeFile to set
	 */
	public void setFileTypeFile(String fileTypeFile) {
		this.fileTypeFile = fileTypeFile;
	}
	/**
	 * @return the fileKindFile
	 */
	public String getFileKindFile() {
		return fileKindFile;
	}
	/**
	 * @param fileKindFile the fileKindFile to set
	 */
	public void setFileKindFile(String fileKindFile) {
		this.fileKindFile = fileKindFile;
	}
	/**
	 * @return the fileNameFile
	 */
	public String getFileNameFile() {
		return fileNameFile;
	}
	/**
	 * @param fileNameFile the fileNameFile to set
	 */
	public void setFileNameFile(String fileNameFile) {
		this.fileNameFile = fileNameFile;
	}
	
	/**
	 * @return the isUseFile
	 */
	public String getIsUseFile() {
		return isUseFile;
	}
	/**
	 * @param isUseFile the isUseFile to set
	 */
	public void setIsUseFile(String isUseFile) {
		this.isUseFile = isUseFile;
	}
	/**
	 * @return the updateUserFile
	 */
	public String getUpdateUserFile() {
		return updateUserFile;
	}
	/**
	 * @param updateUserFile the updateUserFile to set
	 */
	public void setUpdateUserFile(String updateUserFile) {
		this.updateUserFile = updateUserFile;
	}
	/**
	 * @return the updateTimeFile
	 */
	public String getUpdateTimeFile() {
		return updateTimeFile;
	}
	/**
	 * @param updateTimeFile the updateTimeFile to set
	 */
	public void setUpdateTimeFile(String updateTimeFile) {
		this.updateTimeFile = updateTimeFile;
	}
	/**
	 * @param fileTextFile the fileTextFile to set
	 */
	public void setFileTextFile(byte[] fileTextFile) {
		this.fileTextFile = fileTextFile;
	}
	/**
	 * @return the fileTextFile
	 */
	public byte[] getFileTextFile() {
		return fileTextFile;
	}
	
}
