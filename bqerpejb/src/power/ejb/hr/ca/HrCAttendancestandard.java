package power.ejb.hr.ca;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCAttendancestandard entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_ATTENDANCESTANDARD")
public class HrCAttendancestandard implements java.io.Serializable {

	// Fields

	private Long attendancestandardid;
	private String attendanceYear;
	private String attendanceMonth;
	private Long attendanceDeptId;
	private Date startDate;
	private Date endDate;
	private Double standardDay;
	private String amBegingTime;
	private String amEndTime;
	private String pmBegingTime;
	private String pmEndTime;
	private Double standardTime;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCAttendancestandard() {
	}

	/** minimal constructor */
	public HrCAttendancestandard(Long attendancestandardid) {
		this.attendancestandardid = attendancestandardid;
	}

	/** full constructor */
	public HrCAttendancestandard(Long attendancestandardid,
			String attendanceYear, String attendanceMonth,
			Long attendanceDeptId, Date startDate, Date endDate,
			Double standardDay, String amBegingTime, String amEndTime,
			String pmBegingTime, String pmEndTime, Double standardTime,
			String lastModifiyBy, Date lastModifiyDate, String isUse,
			String enterpriseCode) {
		this.attendancestandardid = attendancestandardid;
		this.attendanceYear = attendanceYear;
		this.attendanceMonth = attendanceMonth;
		this.attendanceDeptId = attendanceDeptId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.standardDay = standardDay;
		this.amBegingTime = amBegingTime;
		this.amEndTime = amEndTime;
		this.pmBegingTime = pmBegingTime;
		this.pmEndTime = pmEndTime;
		this.standardTime = standardTime;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ATTENDANCESTANDARDID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAttendancestandardid() {
		return this.attendancestandardid;
	}

	public void setAttendancestandardid(Long attendancestandardid) {
		this.attendancestandardid = attendancestandardid;
	}

	@Column(name = "ATTENDANCE_YEAR", length = 4)
	public String getAttendanceYear() {
		return this.attendanceYear;
	}

	public void setAttendanceYear(String attendanceYear) {
		this.attendanceYear = attendanceYear;
	}

	@Column(name = "ATTENDANCE_MONTH", length = 2)
	public String getAttendanceMonth() {
		return this.attendanceMonth;
	}

	public void setAttendanceMonth(String attendanceMonth) {
		this.attendanceMonth = attendanceMonth;
	}

	@Column(name = "ATTENDANCE_DEPT_ID", precision = 10, scale = 0)
	public Long getAttendanceDeptId() {
		return this.attendanceDeptId;
	}

	public void setAttendanceDeptId(Long attendanceDeptId) {
		this.attendanceDeptId = attendanceDeptId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "STANDARD_DAY", precision = 15, scale = 4)
	public Double getStandardDay() {
		return this.standardDay;
	}

	public void setStandardDay(Double standardDay) {
		this.standardDay = standardDay;
	}

	@Column(name = "AM_BEGING_TIME", length = 10)
	public String getAmBegingTime() {
		return this.amBegingTime;
	}

	public void setAmBegingTime(String amBegingTime) {
		this.amBegingTime = amBegingTime;
	}

	@Column(name = "AM_END_TIME", length = 10)
	public String getAmEndTime() {
		return this.amEndTime;
	}

	public void setAmEndTime(String amEndTime) {
		this.amEndTime = amEndTime;
	}

	@Column(name = "PM_BEGING_TIME", length = 10)
	public String getPmBegingTime() {
		return this.pmBegingTime;
	}

	public void setPmBegingTime(String pmBegingTime) {
		this.pmBegingTime = pmBegingTime;
	}

	@Column(name = "PM_END_TIME", length = 10)
	public String getPmEndTime() {
		return this.pmEndTime;
	}

	public void setPmEndTime(String pmEndTime) {
		this.pmEndTime = pmEndTime;
	}

	@Column(name = "STANDARD_TIME", precision = 15, scale = 4)
	public Double getStandardTime() {
		return this.standardTime;
	}

	public void setStandardTime(Double standardTime) {
		this.standardTime = standardTime;
	}

	@Column(name = "LAST_MODIFIY_BY", length = 16)
	public String getLastModifiyBy() {
		return this.lastModifiyBy;
	}

	public void setLastModifiyBy(String lastModifiyBy) {
		this.lastModifiyBy = lastModifiyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIY_DATE", length = 7)
	public Date getLastModifiyDate() {
		return this.lastModifiyDate;
	}

	public void setLastModifiyDate(Date lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}