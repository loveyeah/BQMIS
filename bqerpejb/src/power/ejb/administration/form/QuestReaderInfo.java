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
public class QuestReaderInfo implements Serializable {
	// Fields

	private Long idReader;
	private String applyIdReader;
	private String readManReader;
	private String readManNameReader;
	private String updateUserReader;
	private String updateTimeReader;
	private String isUseReader;
	/**
	 * @return the idReader
	 */
	public Long getIdReader() {
		return idReader;
	}
	/**
	 * @param idReader the idReader to set
	 */
	public void setIdReader(Long idReader) {
		this.idReader = idReader;
	}
	/**
	 * @return the applyIdReader
	 */
	public String getApplyIdReader() {
		return applyIdReader;
	}
	/**
	 * @param applyIdReader the applyIdReader to set
	 */
	public void setApplyIdReader(String applyIdReader) {
		this.applyIdReader = applyIdReader;
	}
	/**
	 * @return the readManReader
	 */
	public String getReadManReader() {
		return readManReader;
	}
	/**
	 * @param readManReader the readManReader to set
	 */
	public void setReadManReader(String readManReader) {
		this.readManReader = readManReader;
	}
	/**
	 * @return the readManNameReader
	 */
	public String getReadManNameReader() {
		return readManNameReader;
	}
	/**
	 * @param readManNameReader the readManNameReader to set
	 */
	public void setReadManNameReader(String readManNameReader) {
		this.readManNameReader = readManNameReader;
	}
	/**
	 * @return the updateUserReader
	 */
	public String getUpdateUserReader() {
		return updateUserReader;
	}
	/**
	 * @param updateUserReader the updateUserReader to set
	 */
	public void setUpdateUserReader(String updateUserReader) {
		this.updateUserReader = updateUserReader;
	}
	/**
	 * @return the updateTimeReader
	 */
	public String getUpdateTimeReader() {
		return updateTimeReader;
	}
	/**
	 * @param updateTimeReader the updateTimeReader to set
	 */
	public void setUpdateTimeReader(String updateTimeReader) {
		this.updateTimeReader = updateTimeReader;
	}
	/**
	 * @return the isUseReader
	 */
	public String getIsUseReader() {
		return isUseReader;
	}
	/**
	 * @param isUseReader the isUseReader to set
	 */
	public void setIsUseReader(String isUseReader) {
		this.isUseReader = isUseReader;
	}

}
