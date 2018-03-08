/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;



/**
 * 定期工作登记bean
 * 
 * @author daichunlin
 */
public class RegularWorkRegisterInfo  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** 定期工作登记表 */
	/** 序号 */	
	private Long id;
	/** 共作日期 */
	private String workDate;
	/** 共作星期 */
	private String workWeek;
	/** 工作说明 */
	private String workExplain;
	/** 工作结果 */
	private String result;
	/** 标志 */
	private String mark;
	/** 操作人 */
	private String operator;
	/** 备注 */
	private String memo;
	/** 更新时间 */
	private String updateTime;
	/** 更新者 */
	private String updateUser;
	/** 是否可用 */
	private String isUse;
	/** 定期工作维护表 */
	/** 工作项目名称 */
	private String workItemName;	
	/** 工作类别维护表 */
	/** 子类别名称 */
	private String subWorkTypeName;
	/** 定期工作明细表 */
	/** 周期号 */
	private String rangeNumber;
	
	
	/**
	 * @return the rangeNumber
	 */
	public String getRangeNumber() {
		return rangeNumber;
	}
	/**
	 * @param rangeNumber the rangeNumber to set
	 */
	public void setRangeNumber(String rangeNumber) {
		this.rangeNumber = rangeNumber;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getWorkDate() {
		return workDate;
	}
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}
	public String getWorkExplain() {
		return workExplain;
	}
	public void setWorkExplain(String workExplain) {
		this.workExplain = workExplain;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getMark() {
		return mark;
	}
	public void setMark(String mark) {
		this.mark = mark;
	}

	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getWorkItemName() {
		return workItemName;
	}
	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
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
	 * @return the workWeek
	 */
	public String getWorkWeek() {
		return workWeek;
	}
	/**
	 * @param workWeek the workWeek to set
	 */
	public void setWorkWeek(String workWeek) {
		this.workWeek = workWeek;
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
	

}

