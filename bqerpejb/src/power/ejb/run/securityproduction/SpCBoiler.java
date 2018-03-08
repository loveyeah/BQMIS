package power.ejb.run.securityproduction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SpCBoiler entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SP_C_BOILER")
public class SpCBoiler implements java.io.Serializable {

	// Fields

	private Long boilerId;
	private Long FBoilerId;
	private String boilerName;
	private String boilerType;
	private String manufacturer;
	private String boilerNumber;
	private String annex;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpCBoiler() {
	}

	/** minimal constructor */
	public SpCBoiler(Long boilerId) {
		this.boilerId = boilerId;
	}

	/** full constructor */
	public SpCBoiler(Long boilerId, Long FBoilerId, String boilerName,
			String boilerType, String manufacturer, String boilerNumber,
			String annex, String isUse, String enterpriseCode) {
		this.boilerId = boilerId;
		this.FBoilerId = FBoilerId;
		this.boilerName = boilerName;
		this.boilerType = boilerType;
		this.manufacturer = manufacturer;
		this.boilerNumber = boilerNumber;
		this.annex = annex;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "BOILER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getBoilerId() {
		return this.boilerId;
	}

	public void setBoilerId(Long boilerId) {
		this.boilerId = boilerId;
	}

	@Column(name = "F_BOILER_ID", precision = 10, scale = 0)
	public Long getFBoilerId() {
		return this.FBoilerId;
	}

	public void setFBoilerId(Long FBoilerId) {
		this.FBoilerId = FBoilerId;
	}

	@Column(name = "BOILER_NAME", length = 200)
	public String getBoilerName() {
		return this.boilerName;
	}

	public void setBoilerName(String boilerName) {
		this.boilerName = boilerName;
	}

	@Column(name = "BOILER_TYPE", length = 200)
	public String getBoilerType() {
		return this.boilerType;
	}

	public void setBoilerType(String boilerType) {
		this.boilerType = boilerType;
	}

	@Column(name = "MANUFACTURER", length = 200)
	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@Column(name = "BOILER_NUMBER", length = 50)
	public String getBoilerNumber() {
		return this.boilerNumber;
	}

	public void setBoilerNumber(String boilerNumber) {
		this.boilerNumber = boilerNumber;
	}

	@Column(name = "ANNEX", length = 200)
	public String getAnnex() {
		return this.annex;
	}

	public void setAnnex(String annex) {
		this.annex = annex;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}