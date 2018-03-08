package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.junit.runner.Request;

/**
 * PtKkxGeneratorInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_KKX_GENERATOR_INFO")
public class PtKkxGeneratorInfo implements java.io.Serializable {

	// Fields

	private Long generatorId;
	private Long blockId;
	private String generatorType;
	private Double ratedCapacity;
	private Double maximumOutput;
	private Double ratedSpeed;
	private Double exportVoltage;
	private Double statorRatedCurrent;
	private Double ratedRotor;
	private Double ratedPowerFactor;
	private Double designEfficiency;
	private String coolingMethod;
	private Date manufactureDate;
	private String manufacturerCode;
	private Double ratedHydrogenPressure;
	private String manufacturerName;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtKkxGeneratorInfo() {
	}

	/** minimal constructor */
	public PtKkxGeneratorInfo(Long generatorId, Long blockId) {
		this.generatorId = generatorId;
		this.blockId = blockId;
	}

	/** full constructor */
	public PtKkxGeneratorInfo(Long generatorId, Long blockId,
			String generatorType, Double ratedCapacity, Double maximumOutput,
			Double ratedSpeed, Double exportVoltage, Double statorRatedCurrent,
			Double ratedRotor, Double ratedPowerFactor,
			Double designEfficiency, String coolingMethod,
			Date manufactureDate, String manufacturerCode,
			Double ratedHydrogenPressure, String manufacturerName,
			String isUse, String enterpriseCode) {
		this.generatorId = generatorId;
		this.blockId = blockId;
		this.generatorType = generatorType;
		this.ratedCapacity = ratedCapacity;
		this.maximumOutput = maximumOutput;
		this.ratedSpeed = ratedSpeed;
		this.exportVoltage = exportVoltage;
		this.statorRatedCurrent = statorRatedCurrent;
		this.ratedRotor = ratedRotor;
		this.ratedPowerFactor = ratedPowerFactor;
		this.designEfficiency = designEfficiency;
		this.coolingMethod = coolingMethod;
		this.manufactureDate = manufactureDate;
		this.manufacturerCode = manufacturerCode;
		this.ratedHydrogenPressure = ratedHydrogenPressure;
		this.manufacturerName = manufacturerName;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "GENERATOR_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getGeneratorId() {
		return this.generatorId;
	}

	public void setGeneratorId(Long generatorId) {
		this.generatorId = generatorId;
	}

	@Column(name = "BLOCK_ID", nullable = false, precision = 10, scale = 0)
	public Long getBlockId() {
		return this.blockId;
	}

	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	@Column(name = "GENERATOR_TYPE", length = 100)
	public String getGeneratorType() {
		return this.generatorType;
	}

	public void setGeneratorType(String generatorType) {
		this.generatorType = generatorType;
	}

	@Column(name = "RATED_CAPACITY", precision = 15, scale = 4)
	public Double getRatedCapacity() {
		return this.ratedCapacity;
	}

	public void setRatedCapacity(Double ratedCapacity) {
		this.ratedCapacity = ratedCapacity;
	}

	@Column(name = "MAXIMUM_OUTPUT", precision = 15, scale = 4)
	public Double getMaximumOutput() {
		return this.maximumOutput;
	}

	public void setMaximumOutput(Double maximumOutput) {
		this.maximumOutput = maximumOutput;
	}

	@Column(name = "RATED_SPEED", precision = 15, scale = 4)
	public Double getRatedSpeed() {
		return this.ratedSpeed;
	}

	public void setRatedSpeed(Double ratedSpeed) {
		this.ratedSpeed = ratedSpeed;
	}

	@Column(name = "EXPORT_VOLTAGE", precision = 15, scale = 4)
	public Double getExportVoltage() {
		return this.exportVoltage;
	}

	public void setExportVoltage(Double exportVoltage) {
		this.exportVoltage = exportVoltage;
	}

	@Column(name = "STATOR_RATED_CURRENT", precision = 15, scale = 4)
	public Double getStatorRatedCurrent() {
		return this.statorRatedCurrent;
	}

	public void setStatorRatedCurrent(Double statorRatedCurrent) {
		this.statorRatedCurrent = statorRatedCurrent;
	}

	@Column(name = "RATED_ROTOR", precision = 15, scale = 4)
	public Double getRatedRotor() {
		return this.ratedRotor;
	}

	public void setRatedRotor(Double ratedRotor) {
		this.ratedRotor = ratedRotor;
	}

	@Column(name = "RATED_POWER_FACTOR", precision = 15, scale = 4)
	public Double getRatedPowerFactor() {
		return this.ratedPowerFactor;
	}

	public void setRatedPowerFactor(Double ratedPowerFactor) {
		this.ratedPowerFactor = ratedPowerFactor;
	}

	@Column(name = "DESIGN_EFFICIENCY", precision = 15, scale = 4)
	public Double getDesignEfficiency() {
		return this.designEfficiency;
	}

	public void setDesignEfficiency(Double designEfficiency) {
		this.designEfficiency = designEfficiency;
	}

	@Column(name = "COOLING_METHOD", length = 50)
	public String getCoolingMethod() {
		return this.coolingMethod;
	}

	public void setCoolingMethod(String coolingMethod) {
		this.coolingMethod = coolingMethod;
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

	@Column(name = "RATED_HYDROGEN_PRESSURE", precision = 15, scale = 4)
	public Double getRatedHydrogenPressure() {
		return this.ratedHydrogenPressure;
	}

	public void setRatedHydrogenPressure(Double ratedHydrogenPressure) {
		this.ratedHydrogenPressure = ratedHydrogenPressure;
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