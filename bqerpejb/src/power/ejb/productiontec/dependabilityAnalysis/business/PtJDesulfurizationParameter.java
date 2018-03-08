package power.ejb.productiontec.dependabilityAnalysis.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJDesulfurizationParameter entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_J_DESULFURIZATION_PARAMETER")
public class PtJDesulfurizationParameter implements java.io.Serializable {

	// Fields

	private Long desulfurizationId;
	private Long auxiliaryId;
	private String desulfurizeType;
	private Double so2Rate;
	private Double limestoneWeight;
	private Double electricityCapacity;
	private Double heatConsume;
	private Double sulfurAutuniteRate;
	private Double liquidGasRate;
	private Double importAirTpr;
	private Double exporttAirTpr;
	private Double importDustThickness;
	private Double exporttDustThickness;
	private Double electPower;
	private Double craftworkWater;
	private Double wasteWater;
	private String oldCode;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJDesulfurizationParameter() {
	}

	/** minimal constructor */
	public PtJDesulfurizationParameter(Long desulfurizationId) {
		this.desulfurizationId = desulfurizationId;
	}

	/** full constructor */
	public PtJDesulfurizationParameter(Long desulfurizationId,
			Long auxiliaryId, String desulfurizeType, Double so2Rate,
			Double limestoneWeight, Double electricityCapacity,
			Double heatConsume, Double sulfurAutuniteRate,
			Double liquidGasRate, Double importAirTpr, Double exporttAirTpr,
			Double importDustThickness, Double exporttDustThickness,
			Double electPower, Double craftworkWater, Double wasteWater,
			String oldCode, String memo, String isUse, String enterpriseCode) {
		this.desulfurizationId = desulfurizationId;
		this.auxiliaryId = auxiliaryId;
		this.desulfurizeType = desulfurizeType;
		this.so2Rate = so2Rate;
		this.limestoneWeight = limestoneWeight;
		this.electricityCapacity = electricityCapacity;
		this.heatConsume = heatConsume;
		this.sulfurAutuniteRate = sulfurAutuniteRate;
		this.liquidGasRate = liquidGasRate;
		this.importAirTpr = importAirTpr;
		this.exporttAirTpr = exporttAirTpr;
		this.importDustThickness = importDustThickness;
		this.exporttDustThickness = exporttDustThickness;
		this.electPower = electPower;
		this.craftworkWater = craftworkWater;
		this.wasteWater = wasteWater;
		this.oldCode = oldCode;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "DESULFURIZATION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDesulfurizationId() {
		return this.desulfurizationId;
	}

	public void setDesulfurizationId(Long desulfurizationId) {
		this.desulfurizationId = desulfurizationId;
	}

	@Column(name = "AUXILIARY_ID", precision = 10, scale = 0)
	public Long getAuxiliaryId() {
		return this.auxiliaryId;
	}

	public void setAuxiliaryId(Long auxiliaryId) {
		this.auxiliaryId = auxiliaryId;
	}

	@Column(name = "DESULFURIZE_TYPE", length = 20)
	public String getDesulfurizeType() {
		return this.desulfurizeType;
	}

	public void setDesulfurizeType(String desulfurizeType) {
		this.desulfurizeType = desulfurizeType;
	}

	@Column(name = "SO2_RATE", precision = 10)
	public Double getSo2Rate() {
		return this.so2Rate;
	}

	public void setSo2Rate(Double so2Rate) {
		this.so2Rate = so2Rate;
	}

	@Column(name = "LIMESTONE_WEIGHT", precision = 10)
	public Double getLimestoneWeight() {
		return this.limestoneWeight;
	}

	public void setLimestoneWeight(Double limestoneWeight) {
		this.limestoneWeight = limestoneWeight;
	}

	@Column(name = "ELECTRICITY_CAPACITY", precision = 10)
	public Double getElectricityCapacity() {
		return this.electricityCapacity;
	}

	public void setElectricityCapacity(Double electricityCapacity) {
		this.electricityCapacity = electricityCapacity;
	}

	@Column(name = "HEAT_CONSUME", precision = 10)
	public Double getHeatConsume() {
		return this.heatConsume;
	}

	public void setHeatConsume(Double heatConsume) {
		this.heatConsume = heatConsume;
	}

	@Column(name = "SULFUR_AUTUNITE_RATE", precision = 10)
	public Double getSulfurAutuniteRate() {
		return this.sulfurAutuniteRate;
	}

	public void setSulfurAutuniteRate(Double sulfurAutuniteRate) {
		this.sulfurAutuniteRate = sulfurAutuniteRate;
	}

	@Column(name = "LIQUID_GAS_RATE", precision = 10)
	public Double getLiquidGasRate() {
		return this.liquidGasRate;
	}

	public void setLiquidGasRate(Double liquidGasRate) {
		this.liquidGasRate = liquidGasRate;
	}

	@Column(name = "IMPORT_AIR_TPR", precision = 10)
	public Double getImportAirTpr() {
		return this.importAirTpr;
	}

	public void setImportAirTpr(Double importAirTpr) {
		this.importAirTpr = importAirTpr;
	}

	@Column(name = "EXPORTT_AIR_TPR", precision = 10)
	public Double getExporttAirTpr() {
		return this.exporttAirTpr;
	}

	public void setExporttAirTpr(Double exporttAirTpr) {
		this.exporttAirTpr = exporttAirTpr;
	}

	@Column(name = "IMPORT_DUST_THICKNESS", precision = 10)
	public Double getImportDustThickness() {
		return this.importDustThickness;
	}

	public void setImportDustThickness(Double importDustThickness) {
		this.importDustThickness = importDustThickness;
	}

	@Column(name = "EXPORTT_DUST_THICKNESS", precision = 10)
	public Double getExporttDustThickness() {
		return this.exporttDustThickness;
	}

	public void setExporttDustThickness(Double exporttDustThickness) {
		this.exporttDustThickness = exporttDustThickness;
	}

	@Column(name = "ELECT_POWER", precision = 10)
	public Double getElectPower() {
		return this.electPower;
	}

	public void setElectPower(Double electPower) {
		this.electPower = electPower;
	}

	@Column(name = "CRAFTWORK_WATER", precision = 10)
	public Double getCraftworkWater() {
		return this.craftworkWater;
	}

	public void setCraftworkWater(Double craftworkWater) {
		this.craftworkWater = craftworkWater;
	}

	@Column(name = "WASTE_WATER", precision = 10)
	public Double getWasteWater() {
		return this.wasteWater;
	}

	public void setWasteWater(Double wasteWater) {
		this.wasteWater = wasteWater;
	}

	@Column(name = "OLD_CODE", length = 20)
	public String getOldCode() {
		return this.oldCode;
	}

	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
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