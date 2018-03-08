package power.ejb.hr.salary;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrCAttendanceDays entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_ATTENDANCE_DAYS")
public class HrCAttendanceDays implements java.io.Serializable {

	// Fields

	private Long attendanceDaysId;
	private Date month;
	private Double attendanceDays;
	private String memo;
	private String isUse;
	private String enterpriseCode;
	private Date startDate;
	private Date endDate;

	// Constructors

	/** default constructor */
	public HrCAttendanceDays() {
	}

	/** minimal constructor */
	public HrCAttendanceDays(Long attendanceDaysId) {
		this.attendanceDaysId = attendanceDaysId;
	}

	/** full constructor */
	public HrCAttendanceDays(Long attendanceDaysId, Date month,
			Double attendanceDays, String memo, String isUse,
			String enterpriseCode) {
		this.attendanceDaysId = attendanceDaysId;
		this.month = month;
		this.attendanceDays = attendanceDays;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ATTENDANCE_DAYS_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAttendanceDaysId() {
		return this.attendanceDaysId;
	}

	public void setAttendanceDaysId(Long attendanceDaysId) {
		this.attendanceDaysId = attendanceDaysId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MONTH", length = 7)
	public Date getMonth() {
		return this.month;
	}

	public void setMonth(Date month) {
		this.month = month;
	}

	@Column(name = "ATTENDANCE_DAYS", precision = 10)
	public Double getAttendanceDays() {
		return this.attendanceDays;
	}

	public void setAttendanceDays(Double attendanceDays) {
		this.attendanceDays = attendanceDays;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}