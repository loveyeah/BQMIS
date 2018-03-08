package power.ejb.hr.ca;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * HrCExchangetorestId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class HrCExchangetorestId implements java.io.Serializable {

	// Fields

	private String attendanceYear;
	private String attendanceMonth;
	private Long empId;

	// Constructors

	/** default constructor */
	public HrCExchangetorestId() {
	}

	/** full constructor */
	public HrCExchangetorestId(String attendanceYear, String attendanceMonth,
			Long empId) {
		this.attendanceYear = attendanceYear;
		this.attendanceMonth = attendanceMonth;
		this.empId = empId;
	}

	// Property accessors

	@Column(name = "ATTENDANCE_YEAR", nullable = false, length = 4)
	public String getAttendanceYear() {
		return this.attendanceYear;
	}

	public void setAttendanceYear(String attendanceYear) {
		this.attendanceYear = attendanceYear;
	}

	@Column(name = "ATTENDANCE_MONTH", nullable = false, length = 2)
	public String getAttendanceMonth() {
		return this.attendanceMonth;
	}

	public void setAttendanceMonth(String attendanceMonth) {
		this.attendanceMonth = attendanceMonth;
	}

	@Column(name = "EMP_ID", nullable = false, precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof HrCExchangetorestId))
			return false;
		HrCExchangetorestId castOther = (HrCExchangetorestId) other;

		return ((this.getAttendanceYear() == castOther.getAttendanceYear()) || (this
				.getAttendanceYear() != null
				&& castOther.getAttendanceYear() != null && this
				.getAttendanceYear().equals(castOther.getAttendanceYear())))
				&& ((this.getAttendanceMonth() == castOther
						.getAttendanceMonth()) || (this.getAttendanceMonth() != null
						&& castOther.getAttendanceMonth() != null && this
						.getAttendanceMonth().equals(
								castOther.getAttendanceMonth())))
				&& ((this.getEmpId() == castOther.getEmpId()) || (this
						.getEmpId() != null
						&& castOther.getEmpId() != null && this.getEmpId()
						.equals(castOther.getEmpId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getAttendanceYear() == null ? 0 : this.getAttendanceYear()
						.hashCode());
		result = 37
				* result
				+ (getAttendanceMonth() == null ? 0 : this.getAttendanceMonth()
						.hashCode());
		result = 37 * result
				+ (getEmpId() == null ? 0 : this.getEmpId().hashCode());
		return result;
	}

}