package power.ejb.manage.stat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCEnthalpyFormula entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_ENTHALPY_FORMULA")
public class BpCEnthalpyFormula implements java.io.Serializable {

	// Fields

	private String itemCode;
	private String enthalpyType;
	private String ylZbbm;
	private String wdZbbm;
	private String computeType;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public BpCEnthalpyFormula() {
	}

	/** minimal constructor */
	public BpCEnthalpyFormula(String itemCode) {
		this.itemCode = itemCode;
	}

	/** full constructor */
	public BpCEnthalpyFormula(String itemCode, String enthalpyType,
			String ylZbbm, String wdZbbm, String computeType,
			String enterpriseCode) {
		this.itemCode = itemCode;
		this.enthalpyType = enthalpyType;
		this.ylZbbm = ylZbbm;
		this.wdZbbm = wdZbbm;
		this.computeType = computeType;
		this.enterpriseCode = enterpriseCode;
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

	@Column(name = "ENTHALPY_TYPE", length = 2)
	public String getEnthalpyType() {
		return this.enthalpyType;
	}

	public void setEnthalpyType(String enthalpyType) {
		this.enthalpyType = enthalpyType;
	}

	@Column(name = "YL_ZBBM", length = 40)
	public String getYlZbbm() {
		return this.ylZbbm;
	}

	public void setYlZbbm(String ylZbbm) {
		this.ylZbbm = ylZbbm;
	}

	@Column(name = "WD_ZBBM", length = 40)
	public String getWdZbbm() {
		return this.wdZbbm;
	}

	public void setWdZbbm(String wdZbbm) {
		this.wdZbbm = wdZbbm;
	}

	@Column(name = "COMPUTE_TYPE", length = 1)
	public String getComputeType() {
		return this.computeType;
	}

	public void setComputeType(String computeType) {
		this.computeType = computeType;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}