package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCValidFormula entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_VALID_FORMULA", schema = "POWER")
public class BpCValidFormula implements java.io.Serializable {

	// Fields

	private String itemCode;
	private String connItemCode;
	private Double min;
	private Double max;

	// Constructors

	/** default constructor */
	public BpCValidFormula() {
	}

	/** minimal constructor */
	public BpCValidFormula(String itemCode) {
		this.itemCode = itemCode;
	}

	/** full constructor */
	public BpCValidFormula(String itemCode, String connItemCode, Double min,
			Double max) {
		this.itemCode = itemCode;
		this.connItemCode = connItemCode;
		this.min = min;
		this.max = max;
	}

	// Property accessors
	@Id
	@Column(name = "ITEM_CODE", unique = true, nullable = false, length = 40)
	public String getItemCode() {
		return this.itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	@Column(name = "CONN_ITEM_CODE", length = 40)
	public String getConnItemCode() {
		return this.connItemCode;
	}

	public void setConnItemCode(String connItemCode) {
		this.connItemCode = connItemCode;
	}

	@Column(name = "MIN", precision = 15, scale = 4)
	public Double getMin() {
		return this.min;
	}

	public void setMin(Double min) {
		this.min = min;
	}

	@Column(name = "MAX", precision = 15, scale = 4)
	public Double getMax() {
		return this.max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

}