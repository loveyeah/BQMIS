package power.ejb.productiontec.dependabilityAnalysis.business;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtKkxTurbineInfo entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_KKX_TURBINE_INFO")
public class PtKkxTurbineInfo implements java.io.Serializable {

	// Fields

	private Long turbineId;
	private Long blockId;
	private String turbineModel;
	private String turbineType;
	private Double rated;
	private Double maximumContinuousPower;
	private Double ratedSpeed;
	private Double mainsteamPressure;
	private Double mainsteamTemperature;
	private Double resteamPressure;
	private Double resteamTemperature;
	private Double exhaustPressure;
	private Double waterInletTemperature;
	private Double heatConsumption;
	private Date manufactureDate;
	private String manufacturerCode;
	private String manufacturerName;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtKkxTurbineInfo() {
	}

	/** minimal constructor */
	public PtKkxTurbineInfo(Long turbineId, Long blockId) {
		this.turbineId = turbineId;
		this.blockId = blockId;
	}

	/** full constructor */
	public PtKkxTurbineInfo(Long turbineId, Long blockId, String turbineModel,
			String turbineType, Double rated, Double maximumContinuousPower,
			Double ratedSpeed, Double mainsteamPressure,
			Double mainsteamTemperature, Double resteamPressure,
			Double resteamTemperature, Double exhaustPressure,
			Double waterInletTemperature, Double heatConsumption,
			Date manufactureDate, String manufacturerCode,
			String manufacturerName, String isUse, String enterpriseCode) {
		this.turbineId = turbineId;
		this.blockId = blockId;
		this.turbineModel = turbineModel;
		this.turbineType = turbineType;
		this.rated = rated;
		this.maximumContinuousPower = maximumContinuousPower;
		this.ratedSpeed = ratedSpeed;
		this.mainsteamPressure = mainsteamPressure;
		this.mainsteamTemperature = mainsteamTemperature;
		this.resteamPressure = resteamPressure;
		this.resteamTemperature = resteamTemperature;
		this.exhaustPressure = exhaustPressure;
		this.waterInletTemperature = waterInletTemperature;
		this.heatConsumption = heatConsumption;
		this.manufactureDate = manufactureDate;
		this.manufacturerCode = manufacturerCode;
		this.manufacturerName = manufacturerName;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "TURBINE_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getTurbineId() {
		return this.turbineId;
	}

	public void setTurbineId(Long turbineId) {
		this.turbineId = turbineId;
	}

	@Column(name = "BLOCK_ID", nullable = false, precision = 10, scale = 0)
	public Long getBlockId() {
		return this.blockId;
	}

	public void setBlockId(Long blockId) {
		this.blockId = blockId;
	}

	@Column(name = "TURBINE_MODEL", length = 100)
	public String getTurbineModel() {
		return this.turbineModel;
	}

	public void setTurbineModel(String turbineModel) {
		this.turbineModel = turbineModel;
	}

	@Column(name = "TURBINE_TYPE", length = 50)
	public String getTurbineType() {
		return this.turbineType;
	}

	public void setTurbineType(String turbineType) {
		this.turbineType = turbineType;
	}

	@Column(name = "RATED", precision = 15, scale = 4)
	public Double getRated() {
		return this.rated;
	}

	public void setRated(Double rated) {
		this.rated = rated;
	}

	@Column(name = "MAXIMUM_CONTINUOUS_POWER", precision = 15, scale = 4)
	public Double getMaximumContinuousPower() {
		return this.maximumContinuousPower;
	}

	public void setMaximumContinuousPower(Double maximumContinuousPower) {
		this.maximumContinuousPower = maximumContinuousPower;
	}

	@Column(name = "RATED_SPEED", precision = 15, scale = 4)
	public Double getRatedSpeed() {
		return this.ratedSpeed;
	}

	public void setRatedSpeed(Double ratedSpeed) {
		this.ratedSpeed = ratedSpeed;
	}

	@Column(name = "MAINSTEAM_PRESSURE", precision = 15, scale = 4)
	public Double getMainsteamPressure() {
		return this.mainsteamPressure;
	}

	public void setMainsteamPressure(Double mainsteamPressure) {
		this.mainsteamPressure = mainsteamPressure;
	}

	@Column(name = "MAINSTEAM_TEMPERATURE", precision = 15, scale = 4)
	public Double getMainsteamTemperature() {
		return this.mainsteamTemperature;
	}

	public void setMainsteamTemperature(Double mainsteamTemperature) {
		this.mainsteamTemperature = mainsteamTemperature;
	}

	@Column(name = "RESTEAM_PRESSURE", precision = 15, scale = 4)
	public Double getResteamPressure() {
		return this.resteamPressure;
	}

	public void setResteamPressure(Double resteamPressure) {
		this.resteamPressure = resteamPressure;
	}

	@Column(name = "RESTEAM_TEMPERATURE", precision = 15, scale = 4)
	public Double getResteamTemperature() {
		return this.resteamTemperature;
	}

	public void setResteamTemperature(Double resteamTemperature) {
		this.resteamTemperature = resteamTemperature;
	}

	@Column(name = "EXHAUST_PRESSURE", precision = 15, scale = 4)
	public Double getExhaustPressure() {
		return this.exhaustPressure;
	}

	public void setExhaustPressure(Double exhaustPressure) {
		this.exhaustPressure = exhaustPressure;
	}

	@Column(name = "WATER_INLET_TEMPERATURE", precision = 15, scale = 4)
	public Double getWaterInletTemperature() {
		return this.waterInletTemperature;
	}

	public void setWaterInletTemperature(Double waterInletTemperature) {
		this.waterInletTemperature = waterInletTemperature;
	}

	@Column(name = "HEAT_CONSUMPTION", precision = 15, scale = 4)
	public Double getHeatConsumption() {
		return this.heatConsumption;
	}

	public void setHeatConsumption(Double heatConsumption) {
		this.heatConsumption = heatConsumption;
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