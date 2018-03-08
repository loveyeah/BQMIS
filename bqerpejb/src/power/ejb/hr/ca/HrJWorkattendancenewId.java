package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJWorkattendancenewId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class HrJWorkattendancenewId implements java.io.Serializable {

	// Fields

	private Long empId;
	private Date attendanceDate;

	// Constructors

	/** default constructor */
	public HrJWorkattendancenewId() {
	}

	/** full constructor */
	public HrJWorkattendancenewId(Long empId, Date attendanceDate) {
		this.empId = empId;
		this.attendanceDate = attendanceDate;
	}

	// Property accessors

	@Column(name = "EMP_ID", nullable = false, precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ATTENDANCE_DATE", nullable = false, length = 7)
	public Date getAttendanceDate() {
		return this.attendanceDate;
	}

	public void setAttendanceDate(Date attendanceDate) {
		this.attendanceDate = attendanceDate;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof HrJWorkattendancenewId))
			return false;
		HrJWorkattendancenewId castOther = (HrJWorkattendancenewId) other;

		return ((this.getEmpId() == castOther.getEmpId()) || (this.getEmpId() != null
				&& castOther.getEmpId() != null && this.getEmpId().equals(
				castOther.getEmpId())))
				&& ((this.getAttendanceDate() == castOther.getAttendanceDate()) || (this
						.getAttendanceDate() != null
						&& castOther.getAttendanceDate() != null && this
						.getAttendanceDate().equals(
								castOther.getAttendanceDate())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getEmpId() == null ? 0 : this.getEmpId().hashCode());
		result = 37
				* result
				+ (getAttendanceDate() == null ? 0 : this.getAttendanceDate()
						.hashCode());
		return result;
	}

}