package power.ejb.administration.form;

import java.io.Serializable;
import java.util.Date;
/**
 * 定期工作安排bean
 * @author liugonglei
 */
public class RegularWorkArrangeInfo implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**定期工作维护表*/
	/**序号*/
	private String id;
	/**工作项目编码*/
	private String workItemCode;
	/**工作项目名称*/
	private String workItemName;
	/**子类别编码*/
	private String subWorkTypeCode;
	/**节假日是否工作*/
	private String ifWeekEnd;
	/**开始时间*/
	private String startTime;
	/**new开始时间*/
	private Date newStartTime;
	/**周期类别*/
	private String workRangeType;
	/**使用标志*/
	private String ifFlag;
	/**工作说明*/	
	private String workExplain;
	/**修改时间*/	
	private Long updateTime;
	
	
	/**工作类别维护*/
	/**子类别名称*/
	private String subWorkTypeName;

	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the workItemCode
	 */
	public String getWorkItemCode() {
		return workItemCode;
	}


	/**
	 * @param workItemCode the workItemCode to set
	 */
	public void setWorkItemCode(String workItemCode) {
		this.workItemCode = workItemCode;
	}


	/**
	 * @return the workItemName
	 */
	public String getWorkItemName() {
		return workItemName;
	}


	/**
	 * @param workItemName the workItemName to set
	 */
	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}


	/**
	 * @return the subWorkTypeCode
	 */
	public String getSubWorkTypeCode() {
		return subWorkTypeCode;
	}


	/**
	 * @param subWorkTypeCode the subWorkTypeCode to set
	 */
	public void setSubWorkTypeCode(String subWorkTypeCode) {
		this.subWorkTypeCode = subWorkTypeCode;
	}


	/**
	 * @return the ifWeekEnd
	 */
	public String getIfWeekEnd() {
		return ifWeekEnd;
	}


	/**
	 * @param ifWeekEnd the ifWeekEnd to set
	 */
	public void setIfWeekEnd(String ifWeekEnd) {
		this.ifWeekEnd = ifWeekEnd;
	}



	/**
	 * @return the workRangeType
	 */
	public String getWorkRangeType() {
		return workRangeType;
	}


	/**
	 * @param workRangeType the workRangeType to set
	 */
	public void setWorkRangeType(String workRangeType) {
		this.workRangeType = workRangeType;
	}


	/**
	 * @return the ifFlag
	 */
	public String getIfFlag() {
		return ifFlag;
	}


	/**
	 * @param ifFlag the ifFlag to set
	 */
	public void setIfFlag(String ifFlag) {
		this.ifFlag = ifFlag;
	}


	/**
	 * @return the workExplain
	 */
	public String getWorkExplain() {
		return workExplain;
	}


	/**
	 * @param workExplain the workExplain to set
	 */
	public void setWorkExplain(String workExplain) {
		this.workExplain = workExplain;
	}


	/**
	 * @return the subWorkTypeName
	 */
	public String getSubWorkTypeName() {
		return subWorkTypeName;
	}


	/**
	 * @param subWorkTypeName the subWorkTypeName to set
	 */
	public void setSubWorkTypeName(String subWorkTypeName) {
		this.subWorkTypeName = subWorkTypeName;
	}


	/**
	 * @return the serialVersionUID
	 */
	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	/**
	 * @return the newStartTime
	 */
	public Date getNewStartTime() {
		return newStartTime;
	}


	/**
	 * @param newStartTime the newStartTime to set
	 */
	public void setNewStartTime(Date newStartTime) {
		this.newStartTime = newStartTime;
	}


	/**
	 * @return the startTime
	 */
	public String getStartTime() {
		return startTime;
	}


	/**
	 * @param startTime the startTime to set
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}


	/**
	 * @return the updateTime
	 */
	public Long getUpdateTime() {
		return updateTime;
	}


	/**
	 * @param updateTime the updateTime to set
	 */
	public void setUpdateTime(Long updateTime) {
		this.updateTime = updateTime;
	}
}
