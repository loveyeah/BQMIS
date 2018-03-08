package power.ejb.hr.salary.form;

import java.util.Date;

@SuppressWarnings("serial")
public class AttendanceDaysForm implements java.io.Serializable{

	private Long attendanceDaysId;
	private Double attendanceDays;
	private String month;
	private String memo;
	private String startDate;//add by sychen 20100806
	private String endDate;//add by sychen 20100806


	public Long getAttendanceDaysId() {
		return attendanceDaysId;
	}
	public void setAttendanceDaysId(Long attendanceDaysId) {
		this.attendanceDaysId = attendanceDaysId;
	}
	public Double getAttendanceDays() {
		return attendanceDays;
	}
	public void setAttendanceDays(Double attendanceDays) {
		this.attendanceDays = attendanceDays;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
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
	
}
