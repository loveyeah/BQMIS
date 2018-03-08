package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BpCAnalyseAccountSetupId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class BpCAnalyseAccountSetupId implements java.io.Serializable {

	// Fields

	private Long accountCode;
	private Long dataTimeDot;

	// Constructors

	/** default constructor */
	public BpCAnalyseAccountSetupId() {
	}

	/** full constructor */
	public BpCAnalyseAccountSetupId(Long accountCode, Long dataTimeDot) {
		this.accountCode = accountCode;
		this.dataTimeDot = dataTimeDot;
	}

	// Property accessors

	@Column(name = "ACCOUNT_CODE", nullable = false, precision = 10, scale = 0)
	public Long getAccountCode() {
		return this.accountCode;
	}

	public void setAccountCode(Long accountCode) {
		this.accountCode = accountCode;
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
		if (!(other instanceof BpCAnalyseAccountSetupId))
			return false;
		BpCAnalyseAccountSetupId castOther = (BpCAnalyseAccountSetupId) other;

		return ((this.getAccountCode() == castOther.getAccountCode()) || (this
				.getAccountCode() != null
				&& castOther.getAccountCode() != null && this.getAccountCode()
				.equals(castOther.getAccountCode())))
				&& ((this.getDataTimeDot() == castOther.getDataTimeDot()) || (this
						.getDataTimeDot() != null
						&& castOther.getDataTimeDot() != null && this
						.getDataTimeDot().equals(castOther.getDataTimeDot())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getAccountCode() == null ? 0 : this.getAccountCode()
						.hashCode());
		result = 37
				* result
				+ (getDataTimeDot() == null ? 0 : this.getDataTimeDot()
						.hashCode());
		return result;
	}

}