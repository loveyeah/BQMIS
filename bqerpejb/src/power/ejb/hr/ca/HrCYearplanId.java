package power.ejb.hr.ca;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * HrCYearplanId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class HrCYearplanId implements java.io.Serializable {

	// Fields

	private String planYear;
	private Long empId;
	private Long vacationTypeId;

	// Constructors

	/** default constructor */
	public HrCYearplanId() {
	}

	/** full constructor */
	public HrCYearplanId(String planYear, Long empId, Long vacationTypeId) {
		this.planYear = planYear;
		this.empId = empId;
		this.vacationTypeId = vacationTypeId;
	}

	// Property accessors

	@Column(name = "PLAN_YEAR", nullable = false, length = 4)
	public String getPlanYear() {
		return this.planYear;
	}

	public void setPlanYear(String planYear) {
		this.planYear = planYear;
	}

	@Column(name = "EMP_ID", nullable = false, precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "VACATION_TYPE_ID", nullable = false, precision = 10, scale = 0)
	public Long getVacationTypeId() {
		return this.vacationTypeId;
	}

	public void setVacationTypeId(Long vacationTypeId) {
		this.vacationTypeId = vacationTypeId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof HrCYearplanId))
			return false;
		HrCYearplanId castOther = (HrCYearplanId) other;

		return ((this.getPlanYear() == castOther.getPlanYear()) || (this
				.getPlanYear() != null
				&& castOther.getPlanYear() != null && this.getPlanYear()
				.equals(castOther.getPlanYear())))
				&& ((this.getEmpId() == castOther.getEmpId()) || (this
						.getEmpId() != null
						&& castOther.getEmpId() != null && this.getEmpId()
						.equals(castOther.getEmpId())))
				&& ((this.getVacationTypeId() == castOther.getVacationTypeId()) || (this
						.getVacationTypeId() != null
						&& castOther.getVacationTypeId() != null && this
						.getVacationTypeId().equals(
								castOther.getVacationTypeId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getPlanYear() == null ? 0 : this.getPlanYear().hashCode());
		result = 37 * result
				+ (getEmpId() == null ? 0 : this.getEmpId().hashCode());
		result = 37
				* result
				+ (getVacationTypeId() == null ? 0 : this.getVacationTypeId()
						.hashCode());
		return result;
	}

}