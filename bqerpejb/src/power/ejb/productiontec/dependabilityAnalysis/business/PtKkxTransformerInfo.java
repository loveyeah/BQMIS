package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtKkxTransformerInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_KKX_TRANSFORMER_INFO")
public class PtKkxTransformerInfo implements java.io.Serializable {

	// Fields

	private Long transformerId;
	private Long blockId;
	private String transformerModel;
	private Double ratedCapacity;
	private String ratedVoltage;
	private String coolingMethod;
	private Long coilNumber;
	private String connection;
	private Date manufactureDate;
	private String manufacturerCode;
	private String manufacturerName;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtKkxTransformerInfo() {
	}

	/** minimal constructor */
	public PtKkxTransformerInfo(Long transformerId, Long blockId) {
		this.transformerId = transformerId;
		this.blockId = blockId;
	}

	/** full constructor */
	public PtKkxTransformerInfo(Long transformerId, Long blockId,
			String transformerModel, Double ratedCapacity, String ratedVoltage,
			String coolingMethod, Long coilNumber, String connection,
			Date manufactureDate, String manufacturerCode,
			String manufacturerName, String isUse, String enterpriseCode) {
		this.transformerId = transformerId;
		this.blockId = blockId;
		this.transformerModel = transformerModel;
		this.ratedCapacity = ratedCapacity;
		this.ratedVoltage = ratedVoltage;
		this.coolingMethod = coolingMethod;
		this.coilNumber = coilNumber;
		this.connection = connection;
		this.manufactureDate = manufactureDate;
		this.manufacturerCode = manufacturerCode;
		this.manufacturerName = manufacturerName;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TRANSFORMER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTransformerId() {
		return this.transformerId;
	}

	public void setTransformerId(Long transformerId) {
		this.transformerId = transformerId;
	}

	@Column(name = "BLOCK_ID", nullable = false, precision = 10, scale = 0)
	public Long getBlockId() {
		return this.blockId;
	}

	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	@Column(name = "TRANSFORMER_MODEL", length = 100)
	public String getTransformerModel() {
		return this.transformerModel;
	}

	public void setTransformerModel(String transformerModel) {
		this.transformerModel = transformerModel;
	}

	@Column(name = "RATED_CAPACITY", precision = 15, scale = 4)
	public Double getRatedCapacity() {
		return this.ratedCapacity;
	}

	public void setRatedCapacity(Double ratedCapacity) {
		this.ratedCapacity = ratedCapacity;
	}

	@Column(name = "RATED_VOLTAGE", length = 50)
	public String getRatedVoltage() {
		return this.ratedVoltage;
	}

	public void setRatedVoltage(String ratedVoltage) {
		this.ratedVoltage = ratedVoltage;
	}

	@Column(name = "COOLING_METHOD", length = 50)
	public String getCoolingMethod() {
		return this.coolingMethod;
	}

	public void setCoolingMethod(String coolingMethod) {
		this.coolingMethod = coolingMethod;
	}

	@Column(name = "COIL_NUMBER", precision = 10, scale = 0)
	public Long getCoilNumber() {
		return this.coilNumber;
	}

	public void setCoilNumber(Long coilNumber) {
		this.coilNumber = coilNumber;
	}

	@Column(name = "CONNECTION", length = 50)
	public String getConnection() {
		return this.connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MANUFACTURE_DATE", length = 7)
	public Date getManufactureDate() {
		return this.manufactureDate;
	}

	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	@Column(name = "MANUFACTURER_CODE", length = 50)
	public String getManufacturerCode() {
		return this.manufacturerCode;
	}

	public void setManufacturerCode(String manufacturerCode) {
		this.manufacturerCode = manufacturerCode;
	}

	@Column(name = "MANUFACTURER_NAME", length = 100)
	public String getManufacturerName() {
		return this.manufacturerName;
	}

	public void setManufacturerName(String manufacturerName) {
		this.manufacturerName = manufacturerName;
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