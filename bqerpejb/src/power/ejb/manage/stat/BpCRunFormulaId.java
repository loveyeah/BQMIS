package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BpCRunFormulaId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class BpCRunFormulaId implements java.io.Serializable {

	// Fields

	private String itemCode;
	private String runDataCode;

	// Constructors

	/** default constructor */
	public BpCRunFormulaId() {
	}

	/** full constructor */
	public BpCRunFormulaId(String itemCode, String runDataCode) {
		this.itemCode = itemCode;
		this.runDataCode = runDataCode;
	}

	// Property accessors

	@Column(name = "ITEM_CODE", nullable = false, length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "RUN_DATA_CODE", nullable = false, length = 40)
	public String getRunDataCode() {
		return this.runDataCode;
	}

	public void setRunDataCode(String runDataCode) {
		this.runDataCode = runDataCode;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BpCRunFormulaId))
			return false;
		BpCRunFormulaId castOther = (BpCRunFormulaId) other;

		return ((this.getItemCode() == castOther.getItemCode()) || (this
				.getItemCode() != null
				&& castOther.getItemCode() != null && this.getItemCode()
				.equals(castOther.getItemCode())))
				&& ((this.getRunDataCode() == castOther.getRunDataCode()) || (this
						.getRunDataCode() != null
						&& castOther.getRunDataCode() != null && this
						.getRunDataCode().equals(castOther.getRunDataCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		result = 37
				* result
				+ (getRunDataCode() == null ? 0 : this.getRunDataCode()
						.hashCode());
		return result;
	}

}