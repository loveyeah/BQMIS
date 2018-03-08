package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BpCInputReportSetupId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class BpCInputReportSetupId implements java.io.Serializable {

	// Fields

	private Long reportCode;
	private Long dataTimeDot;

	// Constructors

	/** default constructor */
	public BpCInputReportSetupId() {
	}

	/** full constructor */
	public BpCInputReportSetupId(Long reportCode, Long dataTimeDot) {
		this.reportCode = reportCode;
		this.dataTimeDot = dataTimeDot;
	}

	// Property accessors

	@Column(name = "REPORT_CODE", nullable = false, precision = 10, scale = 0)
	public Long getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(Long reportCode) {
		this.reportCode = reportCode;
	}

	@Column(name = "DATA_TIME_DOT", nullable = false, precision = 22, scale = 0)
	public Long getDataTimeDot() {
		return this.dataTimeDot;
	}

	public void setDataTimeDot(Long dataTimeDot) {
		this.dataTimeDot = dataTimeDot;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BpCInputReportSetupId))
			return false;
		BpCInputReportSetupId castOther = (BpCInputReportSetupId) other;

		return ((this.getReportCode() == castOther.getReportCode()) || (this
				.getReportCode() != null
				&& castOther.getReportCode() != null && this.getReportCode()
				.equals(castOther.getReportCode())))
				&& ((this.getDataTimeDot() == castOther.getDataTimeDot()) || (this
						.getDataTimeDot() != null
						&& castOther.getDataTimeDot() != null && this
						.getDataTimeDot().equals(castOther.getDataTimeDot())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getReportCode() == null ? 0 : this.getReportCode()
						.hashCode());
		result = 37
				* result
				+ (getDataTimeDot() == null ? 0 : this.getDataTimeDot()
						.hashCode());
		return result;
	}

}