package power.ejb.equ.base;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * EquJBaseInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "EQU_J_BASE_INFO")
public class EquJBaseInfo implements java.io.Serializable {

	// Fields

	private Long equBaseId;
	private String attributeCode;
	private String manufacturer;
	private String model;
	private Date factoryDate;
	private Date installationDate;
	private String installationCode;
	private Double price;
	private String useYear;
	private String assetCode;
	private String technicalParameters;
	private String oneResponsible;
	private String twoResponsible;
	private String threeResponsible;
	private String lastModifyBy;
	private Date lastModifyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public EquJBaseInfo() {
	}

	/** minimal constructor */
	public EquJBaseInfo(Long equBaseId, Double price) {
		this.equBaseId = equBaseId;
		this.price = price;
	}

	/** full constructor */
	public EquJBaseInfo(Long equBaseId, String attributeCode,
			String manufacturer, String model, Date factoryDate,
			Date installationDate, String installationCode, Double price,
			String useYear, String assetCode, String technicalParameters,
			String oneResponsible, String twoResponsible,
			String threeResponsible, String lastModifyBy, Date lastModifyDate,
			String isUse, String enterpriseCode) {
		this.equBaseId = equBaseId;
		this.attributeCode = attributeCode;
		this.manufacturer = manufacturer;
		this.model = model;
		this.factoryDate = factoryDate;
		this.installationDate = installationDate;
		this.installationCode = installationCode;
		this.price = price;
		this.useYear = useYear;
		this.assetCode = assetCode;
		this.technicalParameters = technicalParameters;
		this.oneResponsible = oneResponsible;
		this.twoResponsible = twoResponsible;
		this.threeResponsible = threeResponsible;
		this.lastModifyBy = lastModifyBy;
		this.lastModifyDate = lastModifyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "EQU_BASE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEquBaseId() {
		return this.equBaseId;
	}

	public void setEquBaseId(Long equBaseId) {
		this.equBaseId = equBaseId;
	}

	@Column(name = "ATTRIBUTE_CODE", length = 30)
	public String getAttributeCode() {
		return this.attributeCode;
	}

	public void setAttributeCode(String attributeCode) {
		this.attributeCode = attributeCode;
	}

	@Column(name = "MANUFACTURER", length = 100)
	public String getManufacturer() {
		return this.manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	@Column(name = "MODEL", length = 50)
	public String getModel() {
		return this.model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FACTORY_DATE", length = 7)
	public Date getFactoryDate() {
		return this.factoryDate;
	}

	public void setFactoryDate(Date factoryDate) {
		this.factoryDate = factoryDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "INSTALLATION_DATE", length = 7)
	public Date getInstallationDate() {
		return this.installationDate;
	}

	public void setInstallationDate(Date installationDate) {
		this.installationDate = installationDate;
	}

	@Column(name = "INSTALLATION_CODE", length = 30)
	public String getInstallationCode() {
		return this.installationCode;
	}

	public void setInstallationCode(String installationCode) {
		this.installationCode = installationCode;
	}

	@Column(name = "PRICE", nullable = false, precision = 15, scale = 4)
	public Double getPrice() {
		return this.price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	@Column(name = "USE_YEAR", length = 5)
	public String getUseYear() {
		return this.useYear;
	}

	public void setUseYear(String useYear) {
		this.useYear = useYear;
	}

	@Column(name = "ASSET_CODE", length = 50)
	public String getAssetCode() {
		return this.assetCode;
	}

	public void setAssetCode(String assetCode) {
		this.assetCode = assetCode;
	}

	@Column(name = "TECHNICAL_PARAMETERS", length = 200)
	public String getTechnicalParameters() {
		return this.technicalParameters;
	}

	public void setTechnicalParameters(String technicalParameters) {
		this.technicalParameters = technicalParameters;
	}

	@Column(name = "ONE_RESPONSIBLE", length = 20)
	public String getOneResponsible() {
		return this.oneResponsible;
	}

	public void setOneResponsible(String oneResponsible) {
		this.oneResponsible = oneResponsible;
	}

	@Column(name = "TWO_RESPONSIBLE", length = 20)
	public String getTwoResponsible() {
		return this.twoResponsible;
	}

	public void setTwoResponsible(String twoResponsible) {
		this.twoResponsible = twoResponsible;
	}

	@Column(name = "THREE_RESPONSIBLE", length = 20)
	public String getThreeResponsible() {
		return this.threeResponsible;
	}

	public void setThreeResponsible(String threeResponsible) {
		this.threeResponsible = threeResponsible;
	}

	@Column(name = "LAST_MODIFY_BY", length = 20)
	public String getLastModifyBy() {
		return this.lastModifyBy;
	}

	public void setLastModifyBy(String lastModifyBy) {
		this.lastModifyBy = lastModifyBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_MODIFY_DATE", length = 7)
	public Date getLastModifyDate() {
		return this.lastModifyDate;
	}

	public void setLastModifyDate(Date lastModifyDate) {
		this.lastModifyDate = lastModifyDate;
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