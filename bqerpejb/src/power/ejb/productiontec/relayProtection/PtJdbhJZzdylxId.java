package power.ejb.productiontec.relayProtection;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * PtJdbhJZzdylxId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class PtJdbhJZzdylxId implements java.io.Serializable {

	// Fields

	private Long deviceId;
	private Long protectTypeId;

	// Constructors

	/** default constructor */
	public PtJdbhJZzdylxId() {
	}

	/** full constructor */
	public PtJdbhJZzdylxId(Long deviceId, Long protectTypeId) {
		this.deviceId = deviceId;
		this.protectTypeId = protectTypeId;
	}

	// Property accessors

	@Column(name = "DEVICE_ID", nullable = false, precision = 10, scale = 0)
	public Long getDeviceId() {
		return this.deviceId;
	}

	public void setDeviceId(Long deviceId) {
		this.deviceId = deviceId;
	}

	@Column(name = "PROTECT_TYPE_ID", nullable = false, precision = 10, scale = 0)
	public Long getProtectTypeId() {
		return this.protectTypeId;
	}

	public void setProtectTypeId(Long protectTypeId) {
		this.protectTypeId = protectTypeId;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof PtJdbhJZzdylxId))
			return false;
		PtJdbhJZzdylxId castOther = (PtJdbhJZzdylxId) other;

		return ((this.getDeviceId() == castOther.getDeviceId()) || (this
				.getDeviceId() != null
				&& castOther.getDeviceId() != null && this.getDeviceId()
				.equals(castOther.getDeviceId())))
				&& ((this.getProtectTypeId() == castOther.getProtectTypeId()) || (this
						.getProtectTypeId() != null
						&& castOther.getProtectTypeId() != null && this
						.getProtectTypeId()
						.equals(castOther.getProtectTypeId())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getDeviceId() == null ? 0 : this.getDeviceId().hashCode());
		result = 37
				* result
				+ (getProtectTypeId() == null ? 0 : this.getProtectTypeId()
						.hashCode());
		return result;
	}

}