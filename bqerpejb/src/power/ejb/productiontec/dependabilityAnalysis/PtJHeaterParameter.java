package power.ejb.productiontec.dependabilityAnalysis;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJHeaterParameter entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_J_HEATER_PARAMETER")
public class PtJHeaterParameter implements java.io.Serializable {

	// Fields

	private Long heaterId;
	private Long auxiliaryId;
	private Double heatArea;
	private Double importAirTpr;
	private Double importWaterTpr;
	private Double exportWaterTpr;
	private Double waterFlux;
	private Double airFlux;
	private Double workWaterPressure;
	private Double workAirPressure;
	private String bypassType;
	private String memo;
	private String oldCode;
	private String layType;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJHeaterParameter() {
	}

	/** minimal constructor */
	public PtJHeaterParameter(Long heaterId) {
		this.heaterId = heaterId;
	}

	/** full constructor */
	public PtJHeaterParameter(Long heaterId, Long auxiliaryId, Double heatArea,
			Double importAirTpr, Double importWaterTpr, Double exportWaterTpr,
			Double waterFlux, Double airFlux, Double workWaterPressure,
			Double workAirPressure, String bypassType, String memo,
			String oldCode, String layType, String isUse, String enterpriseCode) {
		this.heaterId = heaterId;
		this.auxiliaryId = auxiliaryId;
		this.heatArea = heatArea;
		this.importAirTpr = importAirTpr;
		this.importWaterTpr = importWaterTpr;
		this.exportWaterTpr = exportWaterTpr;
		this.waterFlux = waterFlux;
		this.airFlux = airFlux;
		this.workWaterPressure = workWaterPressure;
		this.workAirPressure = workAirPressure;
		this.bypassType = bypassType;
		this.memo = memo;
		this.oldCode = oldCode;
		this.layType = layType;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "HEATER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getHeaterId() {
		return this.heaterId;
	}

	public void setHeaterId(Long heaterId) {
		this.heaterId = heaterId;
	}

	@Column(name = "AUXILIARY_ID", precision = 10, scale = 0)
	public Long getAuxiliaryId() {
		return this.auxiliaryId;
	}

	public void setAuxiliaryId(Long auxiliaryId) {
		this.auxiliaryId = auxiliaryId;
	}

	@Column(name = "HEAT_AREA", precision = 10)
	public Double getHeatArea() {
		return this.heatArea;
	}

	public void setHeatArea(Double heatArea) {
		this.heatArea = heatArea;
	}

	@Column(name = "IMPORT_AIR_TPR", precision = 10)
	public Double getImportAirTpr() {
		return this.importAirTpr;
	}

	public void setImportAirTpr(Double importAirTpr) {
		this.importAirTpr = importAirTpr;
	}

	@Column(name = "IMPORT_WATER_TPR", precision = 10)
	public Double getImportWaterTpr() {
		return this.importWaterTpr;
	}

	public void setImportWaterTpr(Double importWaterTpr) {
		this.importWaterTpr = importWaterTpr;
	}

	@Column(name = "EXPORT_WATER_TPR", precision = 10)
	public Double getExportWaterTpr() {
		return this.exportWaterTpr;
	}

	public void setExportWaterTpr(Double exportWaterTpr) {
		this.exportWaterTpr = exportWaterTpr;
	}

	@Column(name = "WATER_FLUX", precision = 10)
	public Double getWaterFlux() {
		return this.waterFlux;
	}

	public void setWaterFlux(Double waterFlux) {
		this.waterFlux = waterFlux;
	}

	@Column(name = "AIR_FLUX", precision = 10)
	public Double getAirFlux() {
		return this.airFlux;
	}

	public void setAirFlux(Double airFlux) {
		this.airFlux = airFlux;
	}

	@Column(name = "WORK_WATER_PRESSURE", precision = 14, scale = 4)
	public Double getWorkWaterPressure() {
		return this.workWaterPressure;
	}

	public void setWorkWaterPressure(Double workWaterPressure) {
		this.workWaterPressure = workWaterPressure;
	}

	@Column(name = "WORK_AIR_PRESSURE", precision = 14, scale = 4)
	public Double getWorkAirPressure() {
		return this.workAirPressure;
	}

	public void setWorkAirPressure(Double workAirPressure) {
		this.workAirPressure = workAirPressure;
	}

	@Column(name = "BYPASS_TYPE", length = 10)
	public String getBypassType() {
		return this.bypassType;
	}

	public void setBypassType(String bypassType) {
		this.bypassType = bypassType;
	}

	@Column(name = "MEMO", length = 200)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "OLD_CODE", length = 20)
	public String getOldCode() {
		return this.oldCode;
	}

	public void setOldCode(String oldCode) {
		this.oldCode = oldCode;
	}

	@Column(name = "LAY_TYPE", length = 20)
	public String getLayType() {
		return this.layType;
	}

	public void setLayType(String layType) {
		this.layType = layType;
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