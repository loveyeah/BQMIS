package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtKkxBoilerInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_KKX_BOILER_INFO")
public class PtKkxBoilerInfo implements java.io.Serializable {

	// Fields

	private Long boilerId;
	private Long blockId;
	private String boilerModel;
	private Double evaporation;
	private String boilerType;
	private String furnaceStructure;
	private String fuelName;
	private String combustionMethod;
	private String slagMethod;
	private String flowMethod;
	private String airPreheaterType;
	private Double efficiency;
	private Date manufactureDate;
	private Double mainsteamTemperature;
	private Double mainsteamPressure;
	private Double resteamFlow;
	private Double resteamInTemperature;
	private Double resteamInPressure;
	private Double resteamOutTemperature;
	private Double resteamOutPressure;
	private Double waterTemperature;
	private Double smokeTemperature;
	private Double windTemperature;
	private String manufacturerCode;
	private String manufacturerName;
	private String designCoalQ;
	private String designCoalV;
	private String designCoalM;
	private String designCoalA;
	private String designCoalS;
	private String designCoalT1;
	private String designCoalT3;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtKkxBoilerInfo() {
	}

	/** minimal constructor */
	public PtKkxBoilerInfo(Long boilerId, Long blockId) {
		this.boilerId = boilerId;
		this.blockId = blockId;
	}

	/** full constructor */
	public PtKkxBoilerInfo(Long boilerId, Long blockId, String boilerModel,
			Double evaporation, String boilerType, String furnaceStructure,
			String fuelName, String combustionMethod, String slagMethod,
			String flowMethod, String airPreheaterType, Double efficiency,
			Date manufactureDate, Double mainsteamTemperature,
			Double mainsteamPressure, Double resteamFlow,
			Double resteamInTemperature, Double resteamInPressure,
			Double resteamOutTemperature, Double resteamOutPressure,
			Double waterTemperature, Double smokeTemperature,
			Double windTemperature, String manufacturerCode,
			String manufacturerName, String designCoalQ, String designCoalV,
			String designCoalM, String designCoalA, String designCoalS,
			String designCoalT1, String designCoalT3, String isUse,
			String enterpriseCode) {
		this.boilerId = boilerId;
		this.blockId = blockId;
		this.boilerModel = boilerModel;
		this.evaporation = evaporation;
		this.boilerType = boilerType;
		this.furnaceStructure = furnaceStructure;
		this.fuelName = fuelName;
		this.combustionMethod = combustionMethod;
		this.slagMethod = slagMethod;
		this.flowMethod = flowMethod;
		this.airPreheaterType = airPreheaterType;
		this.efficiency = efficiency;
		this.manufactureDate = manufactureDate;
		this.mainsteamTemperature = mainsteamTemperature;
		this.mainsteamPressure = mainsteamPressure;
		this.resteamFlow = resteamFlow;
		this.resteamInTemperature = resteamInTemperature;
		this.resteamInPressure = resteamInPressure;
		this.resteamOutTemperature = resteamOutTemperature;
		this.resteamOutPressure = resteamOutPressure;
		this.waterTemperature = waterTemperature;
		this.smokeTemperature = smokeTemperature;
		this.windTemperature = windTemperature;
		this.manufacturerCode = manufacturerCode;
		this.manufacturerName = manufacturerName;
		this.designCoalQ = designCoalQ;
		this.designCoalV = designCoalV;
		this.designCoalM = designCoalM;
		this.designCoalA = designCoalA;
		this.designCoalS = designCoalS;
		this.designCoalT1 = designCoalT1;
		this.designCoalT3 = designCoalT3;
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

	@Column(name = "BLOCK_ID", nullable = false, precision = 10, scale = 0)
	public Long getBlockId() {
		return this.blockId;
	}

	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	@Column(name = "BOILER_MODEL", length = 100)
	public String getBoilerModel() {
		return this.boilerModel;
	}

	public void setBoilerModel(String boilerModel) {
		this.boilerModel = boilerModel;
	}

	@Column(name = "EVAPORATION", precision = 15, scale = 4)
	public Double getEvaporation() {
		return this.evaporation;
	}

	public void setEvaporation(Double evaporation) {
		this.evaporation = evaporation;
	}

	@Column(name = "BOILER_TYPE", length = 50)
	public String getBoilerType() {
		return this.boilerType;
	}

	public void setBoilerType(String boilerType) {
		this.boilerType = boilerType;
	}

	@Column(name = "FURNACE_STRUCTURE", length = 50)
	public String getFurnaceStructure() {
		return this.furnaceStructure;
	}

	public void setFurnaceStructure(String furnaceStructure) {
		this.furnaceStructure = furnaceStructure;
	}

	@Column(name = "FUEL_NAME", length = 50)
	public String getFuelName() {
		return this.fuelName;
	}

	public void setFuelName(String fuelName) {
		this.fuelName = fuelName;
	}

	@Column(name = "COMBUSTION__METHOD", length = 50)
	public String getCombustionMethod() {
		return this.combustionMethod;
	}

	public void setCombustionMethod(String combustionMethod) {
		this.combustionMethod = combustionMethod;
	}

	@Column(name = "SLAG_METHOD", length = 50)
	public String getSlagMethod() {
		return this.slagMethod;
	}

	public void setSlagMethod(String slagMethod) {
		this.slagMethod = slagMethod;
	}

	@Column(name = "FLOW_METHOD", length = 50)
	public String getFlowMethod() {
		return this.flowMethod;
	}

	public void setFlowMethod(String flowMethod) {
		this.flowMethod = flowMethod;
	}

	@Column(name = "AIR_PREHEATER_TYPE", length = 50)
	public String getAirPreheaterType() {
		return this.airPreheaterType;
	}

	public void setAirPreheaterType(String airPreheaterType) {
		this.airPreheaterType = airPreheaterType;
	}

	@Column(name = "EFFICIENCY", precision = 15, scale = 4)
	public Double getEfficiency() {
		return this.efficiency;
	}

	public void setEfficiency(Double efficiency) {
		this.efficiency = efficiency;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "MANUFACTURE_DATE", length = 7)
	public Date getManufactureDate() {
		return this.manufactureDate;
	}

	public void setManufactureDate(Date manufactureDate) {
		this.manufactureDate = manufactureDate;
	}

	@Column(name = "MAINSTEAM_TEMPERATURE", precision = 15, scale = 4)
	public Double getMainsteamTemperature() {
		return this.mainsteamTemperature;
	}

	public void setMainsteamTemperature(Double mainsteamTemperature) {
		this.mainsteamTemperature = mainsteamTemperature;
	}

	@Column(name = "MAINSTEAM_PRESSURE", precision = 15, scale = 4)
	public Double getMainsteamPressure() {
		return this.mainsteamPressure;
	}

	public void setMainsteamPressure(Double mainsteamPressure) {
		this.mainsteamPressure = mainsteamPressure;
	}

	@Column(name = "RESTEAM_FLOW", precision = 15, scale = 4)
	public Double getResteamFlow() {
		return this.resteamFlow;
	}

	public void setResteamFlow(Double resteamFlow) {
		this.resteamFlow = resteamFlow;
	}

	@Column(name = "RESTEAM_IN_TEMPERATURE", precision = 15, scale = 4)
	public Double getResteamInTemperature() {
		return this.resteamInTemperature;
	}

	public void setResteamInTemperature(Double resteamInTemperature) {
		this.resteamInTemperature = resteamInTemperature;
	}

	@Column(name = "RESTEAM_IN_PRESSURE", precision = 15, scale = 4)
	public Double getResteamInPressure() {
		return this.resteamInPressure;
	}

	public void setResteamInPressure(Double resteamInPressure) {
		this.resteamInPressure = resteamInPressure;
	}

	@Column(name = "RESTEAM_OUT_TEMPERATURE", precision = 15, scale = 4)
	public Double getResteamOutTemperature() {
		return this.resteamOutTemperature;
	}

	public void setResteamOutTemperature(Double resteamOutTemperature) {
		this.resteamOutTemperature = resteamOutTemperature;
	}

	@Column(name = "RESTEAM_OUT_PRESSURE", precision = 15, scale = 4)
	public Double getResteamOutPressure() {
		return this.resteamOutPressure;
	}

	public void setResteamOutPressure(Double resteamOutPressure) {
		this.resteamOutPressure = resteamOutPressure;
	}

	@Column(name = "WATER_TEMPERATURE", precision = 15, scale = 4)
	public Double getWaterTemperature() {
		return this.waterTemperature;
	}

	public void setWaterTemperature(Double waterTemperature) {
		this.waterTemperature = waterTemperature;
	}

	@Column(name = "SMOKE_TEMPERATURE", precision = 15, scale = 4)
	public Double getSmokeTemperature() {
		return this.smokeTemperature;
	}

	public void setSmokeTemperature(Double smokeTemperature) {
		this.smokeTemperature = smokeTemperature;
	}

	@Column(name = "WIND_TEMPERATURE", precision = 15, scale = 4)
	public Double getWindTemperature() {
		return this.windTemperature;
	}

	public void setWindTemperature(Double windTemperature) {
		this.windTemperature = windTemperature;
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

	@Column(name = "DESIGN_COAL_Q", length = 50)
	public String getDesignCoalQ() {
		return this.designCoalQ;
	}

	public void setDesignCoalQ(String designCoalQ) {
		this.designCoalQ = designCoalQ;
	}

	@Column(name = "DESIGN_COAL_V", length = 50)
	public String getDesignCoalV() {
		return this.designCoalV;
	}

	public void setDesignCoalV(String designCoalV) {
		this.designCoalV = designCoalV;
	}

	@Column(name = "DESIGN_COAL_M", length = 50)
	public String getDesignCoalM() {
		return this.designCoalM;
	}

	public void setDesignCoalM(String designCoalM) {
		this.designCoalM = designCoalM;
	}

	@Column(name = "DESIGN_COAL_A", length = 50)
	public String getDesignCoalA() {
		return this.designCoalA;
	}

	public void setDesignCoalA(String designCoalA) {
		this.designCoalA = designCoalA;
	}

	@Column(name = "DESIGN_COAL_S", length = 50)
	public String getDesignCoalS() {
		return this.designCoalS;
	}

	public void setDesignCoalS(String designCoalS) {
		this.designCoalS = designCoalS;
	}

	@Column(name = "DESIGN_COAL_T1", length = 50)
	public String getDesignCoalT1() {
		return this.designCoalT1;
	}

	public void setDesignCoalT1(String designCoalT1) {
		this.designCoalT1 = designCoalT1;
	}

	@Column(name = "DESIGN_COAL_T3", length = 50)
	public String getDesignCoalT3() {
		return this.designCoalT3;
	}

	public void setDesignCoalT3(String designCoalT3) {
		this.designCoalT3 = designCoalT3;
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