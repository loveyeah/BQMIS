package power.ejb.productiontec.relayProtection;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PtJdbhCLbyxmdyId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class PtJdbhCLbyxmdyId implements java.io.Serializable {

	// Fields

	private Long sylbId;
	private Long syxmId;

	// Constructors

	/** default constructor */
	public PtJdbhCLbyxmdyId() {
	}

	/** full constructor */
	public PtJdbhCLbyxmdyId(Long sylbId, Long syxmId) {
		this.sylbId = sylbId;
		this.syxmId = syxmId;
	}

	// Property accessors

	@Column(name = "SYLB_ID", nullable = false, precision = 10, scale = 0)
	public Long getSylbId() {
		return this.sylbId;
	}

	public void setSylbId(Long sylbId) {
		this.sylbId = sylbId;
	}

	@Column(name = "SYXM_ID", nullable = false, precision = 10, scale = 0)
	public Long getSyxmId() {
		return this.syxmId;
	}

	public void setSyxmId(Long syxmId) {
		this.syxmId = syxmId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PtJdbhCLbyxmdyId))
			return false;
		PtJdbhCLbyxmdyId castOther = (PtJdbhCLbyxmdyId) other;

		return ((this.getSylbId() == castOther.getSylbId()) || (this
				.getSylbId() != null
				&& castOther.getSylbId() != null && this.getSylbId().equals(
				castOther.getSylbId())))
				&& ((this.getSyxmId() == castOther.getSyxmId()) || (this
						.getSyxmId() != null
						&& castOther.getSyxmId() != null && this.getSyxmId()
						.equals(castOther.getSyxmId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getSylbId() == null ? 0 : this.getSylbId().hashCode());
		result = 37 * result
				+ (getSyxmId() == null ? 0 : this.getSyxmId().hashCode());
		return result;
	}

}