/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;



/**
 * 全部定期工作bean
 * 
 * @author daichunlin
 */
public class AllRegularWorkInfo  implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** 定期工作维护表 */
	/** 工作项目编码 */	
	private String workItemCode;
	/** 类别编码 */
	private String workTypeCode;
	/** 子类别编码 */
	private String subWorkTypeCode;
	/** 周期类别 */
	private String workRangeType;
	/** 开始时间 */
	private String startTime;
	/** 工作说明 */
	private String workExplain;		
	/** 定期工作明细表 */
	/** 周期号 */
	private String rangeNumber;
	
	public String getWorkItemCode() {
		return workItemCode;
	}
	public void setWorkItemCode(String workItemCode) {
		this.workItemCode = workItemCode;
	}
	public String getWorkTypeCode() {
		return workTypeCode;
	}
	public void setWorkTypeCode(String workTypeCode) {
		this.workTypeCode = workTypeCode;
	}
	public String getSubWorkTypeCode() {
		return subWorkTypeCode;
	}
	public void setSubWorkTypeCode(String subWorkTypeCode) {
		this.subWorkTypeCode = subWorkTypeCode;
	}
	public String getWorkRangeType() {
		return workRangeType;
	}
	public void setWorkRangeType(String workRangeType) {
		this.workRangeType = workRangeType;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getWorkExplain() {
		return workExplain;
	}
	public void setWorkExplain(String workExplain) {
		this.workExplain = workExplain;
	}
	public String getRangeNumber() {
		return rangeNumber;
	}
	public void setRangeNumber(String rangeNumber) {
		this.rangeNumber = rangeNumber;
	}
	
}

