package power.ejb.manage.plan;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * BpCPlanFormulaId entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Embeddable
public class BpCPlanFormulaId implements java.io.Serializable {

	// Fields

	private String itemCode;
	private Long formulaNo;

	// Constructors

	/** default constructor */
	public BpCPlanFormulaId() {
	}

	/** full constructor */
	public BpCPlanFormulaId(String itemCode, Long formulaNo) {
		this.itemCode = itemCode;
		this.formulaNo = formulaNo;
	}

	// Property accessors

	@Column(name = "ITEM_CODE", nullable = false, length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "FORMULA_NO", nullable = false, precision = 10, scale = 0)
	public Long getFormulaNo() {
		return this.formulaNo;
	}

	public void setFormulaNo(Long formulaNo) {
		this.formulaNo = formulaNo;
	}

	public boolean equals(Object other) {
		if ((this == other))
			return true;
		if ((other == null))
			return false;
		if (!(other instanceof BpCPlanFormulaId))
			return false;
		BpCPlanFormulaId castOther = (BpCPlanFormulaId) other;

		return ((this.getItemCode() == castOther.getItemCode()) || (this
				.getItemCode() != null
				&& castOther.getItemCode() != null && this.getItemCode()
				.equals(castOther.getItemCode())))
				&& ((this.getFormulaNo() == castOther.getFormulaNo()) || (this
						.getFormulaNo() != null
						&& castOther.getFormulaNo() != null && this
						.getFormulaNo().equals(castOther.getFormulaNo())));
	}

	public int hashCode() {
		int result = 17;

		result = 37 * result
				+ (getItemCode() == null ? 0 : this.getItemCode().hashCode());
		result = 37 * result
				+ (getFormulaNo() == null ? 0 : this.getFormulaNo().hashCode());
		return result;
	}

}