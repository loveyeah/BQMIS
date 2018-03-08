package power.ejb.hr.ca;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * HrJAttendancecheckId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class HrJAttendancecheckId implements java.io.Serializable {

	// Fields

	private String attendanceYear;
	private String attendanceMonth;
	private Long attendanceDep;

	// Constructors

	/** default constructor */
	public HrJAttendancecheckId() {
	}

	/** full constructor */
	public HrJAttendancecheckId(String attendanceYear, String attendanceMonth,
			Long attendanceDep) {
		this.attendanceYear = attendanceYear;
		this.attendanceMonth = attendanceMonth;
		this.attendanceDep = attendanceDep;
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

	@Column(name = "ATTENDANCE_DEP", nullable = false, precision = 10, scale = 0)
	public Long getAttendanceDep() {
		return this.attendanceDep;
	}

	public void setAttendanceDep(Long attendanceDep) {
		this.attendanceDep = attendanceDep;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof HrJAttendancecheckId))
			return false;
		HrJAttendancecheckId castOther = (HrJAttendancecheckId) other;

		return ((this.getAttendanceYear() == castOther.getAttendanceYear()) || (this
				.getAttendanceYear() != null
				&& castOther.getAttendanceYear() != null && this
				.getAttendanceYear().equals(castOther.getAttendanceYear())))
				&& ((this.getAttendanceMonth() == castOther
						.getAttendanceMonth()) || (this.getAttendanceMonth() != null
						&& castOther.getAttendanceMonth() != null && this
						.getAttendanceMonth().equals(
								castOther.getAttendanceMonth())))
				&& ((this.getAttendanceDep() == castOther.getAttendanceDep()) || (this
						.getAttendanceDep() != null
						&& castOther.getAttendanceDep() != null && this
						.getAttendanceDep()
						.equals(castOther.getAttendanceDep())));
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
		result = 37
				* result
				+ (getAttendanceDep() == null ? 0 : this.getAttendanceDep()
						.hashCode());
		return result;
	}

}