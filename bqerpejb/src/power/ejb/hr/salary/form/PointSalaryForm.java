package power.ejb.hr.salary.form;

public class PointSalaryForm 
{
	private Long pointSalaryId;
	private Long checkStationGrade;
	private Long salaryLevel;
	private Double jobPoint;
	private String memo;
	
	// 执行岗级名
	private String checkStationName;
	// 薪级名
	private String levelName;
	
	public Long getPointSalaryId() {
		return pointSalaryId;
	}
	public void setPointSalaryId(Long pointSalaryId) {
		this.pointSalaryId = pointSalaryId;
	}
	public Long getCheckStationGrade() {
		return checkStationGrade;
	}
	public void setCheckStationGrade(Long checkStationGrade) {
		this.checkStationGrade = checkStationGrade;
	}
	public Long getSalaryLevel() {
		return salaryLevel;
	}
	public void setSalaryLevel(Long salaryLevel) {
		this.salaryLevel = salaryLevel;
	}
	public Double getJobPoint() {
		return jobPoint;
	}
	public void setJobPoint(Double jobPoint) {
		this.jobPoint = jobPoint;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getCheckStationName() {
		return checkStationName;
	}
	public void setCheckStationName(String checkStationName) {
		this.checkStationName = checkStationName;
	}
	public String getLevelName() {
		return levelName;
	}
	public void setLevelName(String levelName) {
		this.levelName = levelName;
	}
	
	
}