package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BpCItemCollectSetupId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class BpCItemCollectSetupId implements java.io.Serializable {

	// Fields

	private String itemCode;
	private Long dataTimeDot;

	// Constructors

	/** default constructor */
	public BpCItemCollectSetupId() {
	}

	/** full constructor */
	public BpCItemCollectSetupId(String itemCode, Long dataTimeDot) {
		this.itemCode = itemCode;
		this.dataTimeDot = dataTimeDot;
	}

	// Property accessors

	@Column(name = "ITEM_CODE", nullable = false, length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
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
		if (!(other instanceof BpCItemCollectSetupId))
			return false;
		BpCItemCollectSetupId castOther = (BpCItemCollectSetupId) other;

		return ((this.getItemCode() == castOther.getItemCode()) || (this
				.getItemCode() != null
				&& castOther.getItemCode() != null && this.getItemCode()
				.equals(castOther.getItemCode())))
				&& ((this.getDataTimeDot() == castOther.getDataTimeDot()) || (this
						.getDataTimeDot() != null
						&& castOther.getDataTimeDot() != null && this
						.getDataTimeDot().equals(castOther.getDataTimeDot())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		result = 37
				* result
				+ (getDataTimeDot() == null ? 0 : this.getDataTimeDot()
						.hashCode());
		return result;
	}

}