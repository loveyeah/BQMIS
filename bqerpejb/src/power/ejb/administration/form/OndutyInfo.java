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
public class OndutyInfo implements Serializable {
/**值班日志登记表**/
	
	/**序号**/
	private Long noteId;
	/**值班记事时间**/
	private String regTime;
	/**值班记事内容**/
	private String regText;
	/**工作类别**/
	private String worktypeCode;
	/**子工作类别**/
	private String subWorktypeCode;
	/**子工作类别**/
	private String subWorktypeName;
	/**值别**/
	private String dutyType;
	/**值别**/
	private String dutyTypeName;
	/**值班人**/ 
	private String dutyman;
	/**登记人**/
	private String crtUser;
	/**修改人**/
	private String updateUser;
	/**修改时间**/
	private String updateTime;
	
	
	/**
	 * @return the regText
	 */
	public String getRegText() {
		return regText;
	}
	/**
	 * @param regText the regText to set
	 */
	public void setRegText(String regText) {
		this.regText = regText;
	}
	/**
	 * @return the subWorktypeCode
	 */
	public String getSubWorktypeCode() {
		return subWorktypeCode;
	}
	/**
	 * @param subWorktypeCode the subWorktypeCode to set
	 */
	public void setSubWorktypeCode(String subWorktypeCode) {
		this.subWorktypeCode = subWorktypeCode;
	}
	/**
	 * @return the subWorktypeName
	 */
	public String getSubWorktypeName() {
		return subWorktypeName;
	}
	/**
	 * @param subWorktypeName the subWorktypeName to set
	 */
	public void setSubWorktypeName(String subWorktypeName) {
		this.subWorktypeName = subWorktypeName;
	}
	/**
	 * @return the dutyType
	 */
	public String getDutyType() {
		return dutyType;
	}
	/**
	 * @param dutyType the dutyType to set
	 */
	public void setDutyType(String dutyType) {
		this.dutyType = dutyType;
	}
	/**
	 * @return the dutyTypeName
	 */
	public String getDutyTypeName() {
		return dutyTypeName;
	}
	/**
	 * @param dutyTypeName the dutyTypeName to set
	 */
	public void setDutyTypeName(String dutyTypeName) {
		this.dutyTypeName = dutyTypeName;
	}
	/**
	 * @return the dutyman
	 */
	public String getDutyman() {
		return dutyman;
	}
	/**
	 * @param dutyman the dutyman to set
	 */
	public void setDutyman(String dutyman) {
		this.dutyman = dutyman;
	}
	/**
	 * @return the crtUser
	 */
	public String getCrtUser() {
		return crtUser;
	}
	/**
	 * @param crtUser the crtUser to set
	 */
	public void setCrtUser(String crtUser) {
		this.crtUser = crtUser;
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
	 * @return the noteId
	 */
	public Long getNoteId() {
		return noteId;
	}
	/**
	 * @param noteId the noteId to set
	 */
	public void setNoteId(Long noteId) {
		this.noteId = noteId;
	}
	/**
	 * @return the regTime
	 */
	public String getRegTime() {
		return regTime;
	}
	/**
	 * @param regTime the regTime to set
	 */
	public void setRegTime(String regTime) {
		this.regTime = regTime;
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
	/**
	 * @return the worktypeCode
	 */
	public String getWorktypeCode() {
		return worktypeCode;
	}
	/**
	 * @param worktypeCode the worktypeCode to set
	 */
	public void setWorktypeCode(String worktypeCode) {
		this.worktypeCode = worktypeCode;
	}
}



	

