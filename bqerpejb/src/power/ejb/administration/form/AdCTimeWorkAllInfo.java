/**
 * Copyright ustcsoft.com
 * All right reserved.
 */
package power.ejb.administration.form;

// default package

/**
 * 定期工作登记 entity.
 * 
 * @author daichunlin
 */
public class AdCTimeWorkAllInfo implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	/** 定期工作登记表 */
	/** 工作名称 */
	private String workitemName;
	/** 周期类别 */
	private String workrangeType;
	/** 计划开始时间 */
	private String startTime;
	/** 星期 */
	private String startWeek;
	/** 具体工作内容 */
	private String workExplain;
	/** 节假日是否工作 */
	private String ifWeekend;
	/** 工作是否进行 */
	private String ifFlag;	

	/**
	 * @return 工作名称
	 */
	public String getWorkitemName() {
		return workitemName;
	}

	/**
	 * @param 工作名称
	 */
	public void setWorkitemName(String workitemName) {
		this.workitemName = workitemName;
	}

	/**
	 * @return 周期类别
	 */
	public String getWorkrangeType() {
		return workrangeType;
	}

	/**
	 * @param 周期类别
	 */
	public void setWorkrangeType(String workrangeType) {
		this.workrangeType = workrangeType;
	}

	/**
	 * @return 计划开始时间
	 */
	public String getStartTime() {
		return startTime;
	}

	/**
	 * @param 计划开始时间
	 */
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	/**
	 * @return 星期
	 */
	public String getStartWeek() {
		return startWeek;
	}

	/**
	 * @param 星期
	 */
	public void setStartWeek(String startWeek) {
		this.startWeek = startWeek;
	}

	/**
	 * @return 具体工作内容
	 */
	public String getWorkExplain() {
		return workExplain;
	}

	/**
	 * @param 具体工作内容
	 */
	public void setWorkExplain(String workExplain) {
		this.workExplain = workExplain;
	}

	/**
	 * @return 节假日是否工作
	 */
	public String getIfWeekend() {
		return ifWeekend;
	}

	/**
	 * @param 节假日是否工作
	 */
	public void setIfWeekend(String ifWeekend) {
		this.ifWeekend = ifWeekend;
	}

	/**
	 * @return 工作是否进行
	 */
	public String getIfFlag() {
		return ifFlag;
	}

	/**
	 * @param 工作是否进行
	 */
	public void setIfFlag(String ifFlag) {
		this.ifFlag = ifFlag;
	}	
	
}