/**
* Copyright ustcsoft.com
* All right reserved.
*/
package power.ejb.administration.form;

import java.util.Date;

/**
* 定期工作安排表明细Bean
* @author zhaozhijie
*
*/
public class FixedJobArrangeListBean {

	/** 工作项目名称 */
   private String workitemName;
	/** 周期类别 */
   private String workrangeType;
	/** 开始时间 */
   private String startTime;
	/** 星期 */
   private String startWeek;
	/** 工作说明 */
   private String workExplain;
	/** 节假日是否工作 */
   private String ifWeekend;
	/** 使用标志 */
   private String ifFlag;
   /** 行数计数 */
   private Integer cntRow;

	/**
	 * @return the workitemName
	 */
	public String getWorkitemName() {
		return workitemName;
	}
	/**
	 * @param workitemName the workitemName to set
	 */
	public void setWorkitemName(String workitemName) {
		this.workitemName = workitemName;
	}
	/**
	 * @return the workrangeType
	 */
	public String getWorkrangeType() {
		return workrangeType;
	}
	/**
	 * @param workrangeType the workrangeType to set
	 */
	public void setWorkrangeType(String workrangeType) {
		this.workrangeType = workrangeType;
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
	 * @return the startWeek
	 */
	public String getStartWeek() {
		return startWeek;
	}
	/**
	 * @param startWeek the startWeek to set
	 */
	public void setStartWeek(String startWeek) {
		this.startWeek = startWeek;
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
	 * @return the ifWeekend
	 */
	public String getIfWeekend() {
		return ifWeekend;
	}
	/**
	 * @param ifWeekend the ifWeekend to set
	 */
	public void setIfWeekend(String ifWeekend) {
		this.ifWeekend = ifWeekend;
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
	 * @return the cntRow
	 */
	public Integer getCntRow() {
		return cntRow;
	}
	/**
	 * @param cntRow the cntRow to set
	 */
	public void setCntRow(Integer cntRow) {
		this.cntRow = cntRow;
	}

}
