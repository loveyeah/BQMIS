package power.ejb.manage.stat;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * BpCRunFormula entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_RUN_FORMULA")
public class BpCRunFormula implements java.io.Serializable {

	// Fields

	private BpCRunFormulaId id;
	private String operatorCode;
	private String deriveDataType;
	private String sdType;
	private String enterpriseCode;
	private String displayNo;

	// Constructors

	/** default constructor */
	public BpCRunFormula() {
	}

	/** minimal constructor */
	public BpCRunFormula(BpCRunFormulaId id) {
		this.id = id;
	}

	/** full constructor */
	public BpCRunFormula(BpCRunFormulaId id, String operatorCode,
			String deriveDataType, String sdType, String enterpriseCode,
			String displayNo) {
		this.id = id;
		this.operatorCode = operatorCode;
		this.deriveDataType = deriveDataType;
		this.sdType = sdType;
		this.enterpriseCode = enterpriseCode;
		this.displayNo = displayNo;
	}

	// Property accessors
	@EmbeddedId
	@AttributeOverrides( {
			@AttributeOverride(name = "itemCode", column = @Column(name = "ITEM_CODE", nullable = false, length = 40)),
			@AttributeOverride(name = "runDataCode", column = @Column(name = "RUN_DATA_CODE", nullable = false, length = 40)) })
	public BpCRunFormulaId getId() {
		return this.id;
	}

	public void setId(BpCRunFormulaId id) {
		this.id = id;
	}

	@Column(name = "OPERATOR_CODE", length = 10)
	public String getOperatorCode() {
		return this.operatorCode;
	}

	public void setOperatorCode(String operatorCode) {
		this.operatorCode = operatorCode;
	}

	@Column(name = "DERIVE_DATA_TYPE", length = 1)
	public String getDeriveDataType() {
		return this.deriveDataType;
	}

	public void setDeriveDataType(String deriveDataType) {
		this.deriveDataType = deriveDataType;
	}

	@Column(name = "SD_TYPE", length = 2)
	public String getSdType() {
		return this.sdType;
	}

	public void setSdType(String sdType) {
		this.sdType = sdType;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "DISPLAY_NO", length = 4)
	public String getDisplayNo() {
		return this.displayNo;
	}

	public void setDisplayNo(String displayNo) {
		this.displayNo = displayNo;
	}

}