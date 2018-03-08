package power.ejb.manage.system;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * BpCMeasureUnit entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "BP_C_MEASURE_UNIT")
public class BpCMeasureUnit implements java.io.Serializable {

	// Fields

	private Long unitId;
	private String unitName;
	private String unitAlias;
	private String retrieveCode;
	private String enterpriseCode;
	private String isUsed;

	// Constructors

	/** default constructor */
	public BpCMeasureUnit() {
	}

	/** minimal constructor */
	public BpCMeasureUnit(Long unitId) {
		this.unitId = unitId;
	}

	/** full constructor */
	public BpCMeasureUnit(Long unitId, String unitName, String unitAlias,
			String retrieveCode, String enterpriseCode, String isUsed) {
		this.unitId = unitId;
		this.unitName = unitName;
		this.unitAlias = unitAlias;
		this.retrieveCode = retrieveCode;
		this.enterpriseCode = enterpriseCode;
		this.isUsed = isUsed;
	}

	// Property accessors
	@Id
	@Column(name = "UNIT_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getUnitId() {
		return this.unitId;
	}

	public void setUnitId(Long unitId) {
		this.unitId = unitId;
	}

	@Column(name = "UNIT_NAME", length = 50)
	public String getUnitName() {
		return this.unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	@Column(name = "UNIT_ALIAS", length = 50)
	public String getUnitAlias() {
		return this.unitAlias;
	}

	public void setUnitAlias(String unitAlias) {
		this.unitAlias = unitAlias;
	}

	@Column(name = "RETRIEVE_CODE", length = 8)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USED", length = 1)
	public String getIsUsed() {
		return this.isUsed;
	}

	public void setIsUsed(String isUsed) {
		this.isUsed = isUsed;
	}

}