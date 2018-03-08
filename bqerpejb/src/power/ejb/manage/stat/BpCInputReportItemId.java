package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BpCInputReportItemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class BpCInputReportItemId implements java.io.Serializable {

	// Fields

	private Long reportCode;
	private String itemCode;

	// Constructors

	/** default constructor */
	public BpCInputReportItemId() {
	}

	/** full constructor */
	public BpCInputReportItemId(Long reportCode, String itemCode) {
		this.reportCode = reportCode;
		this.itemCode = itemCode;
	}

	// Property accessors

	@Column(name = "REPORT_CODE", nullable = false, precision = 10, scale = 0)
	public Long getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(Long reportCode) {
		this.reportCode = reportCode;
	}

	@Column(name = "ITEM_CODE", nullable = false, length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BpCInputReportItemId))
			return false;
		BpCInputReportItemId castOther = (BpCInputReportItemId) other;

		return ((this.getReportCode() == castOther.getReportCode()) || (this
				.getReportCode() != null
				&& castOther.getReportCode() != null && this.getReportCode()
				.equals(castOther.getReportCode())))
				&& ((this.getItemCode() == castOther.getItemCode()) || (this
						.getItemCode() != null
						&& castOther.getItemCode() != null && this
						.getItemCode().equals(castOther.getItemCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getReportCode() == null ? 0 : this.getReportCode()
						.hashCode());
		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		return result;
	}

}