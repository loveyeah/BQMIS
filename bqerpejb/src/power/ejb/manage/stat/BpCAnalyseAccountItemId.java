package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BpCAnalyseAccountItemId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class BpCAnalyseAccountItemId implements java.io.Serializable {

	// Fields

	private Long accountCode;
	private String itemCode;

	// Constructors

	/** default constructor */
	public BpCAnalyseAccountItemId() {
	}

	/** full constructor */
	public BpCAnalyseAccountItemId(Long accountCode, String itemCode) {
		this.accountCode = accountCode;
		this.itemCode = itemCode;
	}

	// Property accessors

	@Column(name = "ACCOUNT_CODE", nullable = false, precision = 10, scale = 0)
	public Long getAccountCode() {
		return this.accountCode;
	}

	public void setAccountCode(Long accountCode) {
		this.accountCode = accountCode;
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
		if (!(other instanceof BpCAnalyseAccountItemId))
			return false;
		BpCAnalyseAccountItemId castOther = (BpCAnalyseAccountItemId) other;

		return ((this.getAccountCode() == castOther.getAccountCode()) || (this
				.getAccountCode() != null
				&& castOther.getAccountCode() != null && this.getAccountCode()
				.equals(castOther.getAccountCode())))
				&& ((this.getItemCode() == castOther.getItemCode()) || (this
						.getItemCode() != null
						&& castOther.getItemCode() != null && this
						.getItemCode().equals(castOther.getItemCode())));
	}

	public int hashCode() {
		int result = 17;

		result = 37
				* result
				+ (getAccountCode() == null ? 0 : this.getAccountCode()
						.hashCode());
		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		return result;
	}

}