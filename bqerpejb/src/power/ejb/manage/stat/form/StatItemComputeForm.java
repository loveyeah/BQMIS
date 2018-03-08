package power.ejb.manage.stat.form;

@SuppressWarnings("serial")
public class StatItemComputeForm implements java.io.Serializable {
	private String isCollect;
	private String isCompute;
	private String isTime;
	private String isDate;
	private String isMonth;
	private String isYear;
	private String isQuarter;
	private String startTime;
	private String endTime;
	private String startDate;
	private String endDate;
	private String startMonth;
	private String endMonth;
	private String startQuarterYear;
	private String endQuarterYear;
	private String startQuarter;
	private String endQuarter;
	private String startYear;
	private String endYear;
	//add by bjxu 分组区间
	private String isGroup;
	private String groupStartDate;
	private String groupEndDate;

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
	}

	public String getIsCompute() {
		return isCompute;
	}

	public void setIsCompute(String isCompute) {
		this.isCompute = isCompute;
	}

	public String getIsTime() {
		return isTime;
	}

	public void setIsTime(String isTime) {
		this.isTime = isTime;
	}

	public String getIsDate() {
		return isDate;
	}

	public void setIsDate(String isDate) {
		this.isDate = isDate;
	}

	public String getIsMonth() {
		return isMonth;
	}

	public void setIsMonth(String isMonth) {
		this.isMonth = isMonth;
	}

	public String getIsYear() {
		return isYear;
	}

	public void setIsYear(String isYear) {
		this.isYear = isYear;
	}

	public String getIsQuarter() {
		return isQuarter;
	}

	public void setIsQuarter(String isQuarter) {
		this.isQuarter = isQuarter;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getStartMonth() {
		return startMonth;
	}

	public void setStartMonth(String startMonth) {
		this.startMonth = startMonth;
	}

	public String getEndMonth() {
		return endMonth;
	}

	public void setEndMonth(String endMonth) {
		this.endMonth = endMonth;
	}

	public String getStartQuarterYear() {
		return startQuarterYear;
	}

	public void setStartQuarterYear(String startQuarterYear) {
		this.startQuarterYear = startQuarterYear;
	}

	public String getEndQuarterYear() {
		return endQuarterYear;
	}

	public void setEndQuarterYear(String endQuarterYear) {
		this.endQuarterYear = endQuarterYear;
	}

	public String getStartQuarter() {
		return startQuarter;
	}

	public void setStartQuarter(String startQuarter) {
		this.startQuarter = startQuarter;
	}

	public String getEndQuarter() {
		return endQuarter;
	}

	public void setEndQuarter(String endQuarter) {
		this.endQuarter = endQuarter;
	}

	public String getStartYear() {
		return startYear;
	}

	public void setStartYear(String startYear) {
		this.startYear = startYear;
	}

	public String getEndYear() {
		return endYear;
	}

	public void setEndYear(String endYear) {
		this.endYear = endYear;
	}

	public String getGroupEndDate() {
		return groupEndDate;
	}

	public void setGroupEndDate(String groupEndDate) {
		this.groupEndDate = groupEndDate;
	}

	public String getGroupStartDate() {
		return groupStartDate;
	}

	public void setGroupStartDate(String groupStartDate) {
		this.groupStartDate = groupStartDate;
	}

	public String getIsGroup() {
		return isGroup;
	}

	public void setIsGroup(String isGroup) {
		this.isGroup = isGroup;
	}

}
