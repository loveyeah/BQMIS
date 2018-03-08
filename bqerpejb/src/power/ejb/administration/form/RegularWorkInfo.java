/**
 * 
 */
package power.ejb.administration.form;

import java.io.Serializable;

/**
 * @author chaihao
 * 
 */
public class RegularWorkInfo implements Serializable {

	private static final long serialVersionUID = 1L;
	/** 工作项目名称 */
	private String workItemName;
	/** 工作日期 */
	private String workDate;
	/** 星期 */
	private String week;
	/** 具体工作内容 */
	private String workExplain;
	/** 工作结果 */
	private String result;
	/** 完成标志 */
	private String mark;
	/** 操作人 */
	private String operator;
	/** 备注 */
	private String memo;

	/**
	 * @return 工作项目名称
	 */
	public String getWorkItemName() {
		return workItemName;
	}

	/**
	 * @param 工作项目名称
	 *            设置工作项目名称
	 */
	public void setWorkItemName(String workItemName) {
		this.workItemName = workItemName;
	}

	/**
	 * @return 工作日期
	 */
	public String getWorkDate() {
		return workDate;
	}

	/**
	 * @param 工作日期
	 *            设置工作日期
	 */
	public void setWorkDate(String workDate) {
		this.workDate = workDate;
	}

	/**
	 * @return 星期
	 */
	public String getWeek() {
		return week;
	}

	/**
	 * @param 星期
	 *            设置星期
	 */
	public void setWeek(String week) {
		this.week = week;
	}

	/**
	 * @return 具体工作内容
	 */
	public String getWorkExplain() {
		return workExplain;
	}

	/**
	 * @param 具体工作内容
	 *            设置具体工作内容
	 */
	public void setWorkExplain(String workExplain) {
		this.workExplain = workExplain;
	}

	/**
	 * @return 工作结果
	 */
	public String getResult() {
		return result;
	}

	/**
	 * @param 工作结果
	 *            设置工作结果
	 */
	public void setResult(String result) {
		this.result = result;
	}

	/**
	 * @return 标记
	 */
	public String getMark() {
		return mark;
	}

	/**
	 * @param 标记
	 *            设置标记
	 */
	public void setMark(String mark) {
		this.mark = mark;
	}

	/**
	 * @return 操作人
	 */
	public String getOperator() {
		return operator;
	}

	/**
	 * @param 操作人
	 *            设置操作人
	 */
	public void setOperator(String operator) {
		this.operator = operator;
	}

	/**
	 * @return 备注
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param 备注
	 *            设置备注
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}
}
