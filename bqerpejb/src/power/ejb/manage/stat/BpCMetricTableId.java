package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BpCMetricTableId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class BpCMetricTableId implements java.io.Serializable {

	// Fields

	private String itemCode;
	private String tableCode;

	// Constructors

	/** default constructor */
	public BpCMetricTableId() {
	}

	/** full constructor */
	public BpCMetricTableId(String itemCode, String tableCode) {
		this.itemCode = itemCode;
		this.tableCode = tableCode;
	}

	// Property accessors

	@Column(name = "ITEM_CODE", nullable = false, length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "TABLE_CODE", nullable = false, length = 10)
	public String getTableCode() {
		return this.tableCode;
	}

	public void setTableCode(String tableCode) {
		this.tableCode = tableCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BpCMetricTableId))
			return false;
		BpCMetricTableId castOther = (BpCMetricTableId) other;

		return ((this.getItemCode() == castOther.getItemCode()) || (this
				.getItemCode() != null
				&& castOther.getItemCode() != null && this.getItemCode()
				.equals(castOther.getItemCode())))
				&& ((this.getTableCode() == castOther.getTableCode()) || (this
						.getTableCode() != null
						&& castOther.getTableCode() != null && this
						.getTableCode().equals(castOther.getTableCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		result = 37 * result
				+ (getTableCode() == null ? 0 : this.getTableCode().hashCode());
		return result;
	}

}