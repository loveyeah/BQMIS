package power.ejb.productiontec.dependabilityAnalysis.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJFeedpumpParameter entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_J_FEEDPUMP_PARAMETER")
public class PtJFeedpumpParameter implements java.io.Serializable {

	// Fields

	private Long feedpumpId;
	private Long auxiliaryId;
	private Double exportFlow;
	private Double exportPressure;
	private Double ratingSpeed;
	private Double ratingEfficiency;
	private Double minimumFlow;
	private Double tapFlow;
	private Double tapPressure;
	private Double cavitationFlow;
	private String adjustWay;
	private Double speedRangeFrom;
	private Double speedRangeTo;
	private String oldCode;
	private String memo;
	private String prePumpModel;
	private Double npsh;
	private Double importPressure;
	private Double importTemperature;
	private Double pumpRatingSpeed;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJFeedpumpParameter() {
	}

	/** minimal constructor */
	public PtJFeedpumpParameter(Long feedpumpId) {
		this.feedpumpId = feedpumpId;
	}

	/** full constructor */
	public PtJFeedpumpParameter(Long feedpumpId, Long auxiliaryId,
			Double exportFlow, Double exportPressure, Double ratingSpeed,
			Double ratingEfficiency, Double minimumFlow, Double tapFlow,
			Double tapPressure, Double cavitationFlow, String adjustWay,
			Double speedRangeFrom, Double speedRangeTo, String oldCode,
			String memo, String prePumpModel, Double npsh,
			Double importPressure, Double importTemperature,
			Double pumpRatingSpeed, String isUse, String enterpriseCode) {
		this.feedpumpId = feedpumpId;
		this.auxiliaryId = auxiliaryId;
		this.exportFlow = exportFlow;
		this.exportPressure = exportPressure;
		this.ratingSpeed = ratingSpeed;
		this.ratingEfficiency = ratingEfficiency;
		this.minimumFlow = minimumFlow;
		this.tapFlow = tapFlow;
		this.tapPressure = tapPressure;
		this.cavitationFlow = cavitationFlow;
		this.adjustWay = adjustWay;
		this.speedRangeFrom = speedRangeFrom;
		this.speedRangeTo = speedRangeTo;
		this.oldCode = oldCode;
		this.memo = memo;
		this.prePumpModel = prePumpModel;
		this.npsh = npsh;
		this.importPressure = importPressure;
		this.importTemperature = importTemperature;
		this.pumpRatingSpeed = pumpRatingSpeed;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "FEEDPUMP_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFeedpumpId() {
		return this.feedpumpId;
	}

	public void setFeedpumpId(Long feedpumpId) {
		this.feedpumpId = feedpumpId;
	}

	@Column(name = "AUXILIARY_ID", precision = 10, scale = 0)
	public Long getAuxiliaryId() {
		return this.auxiliaryId;
	}

	public void setAuxiliaryId(Long auxiliaryId) {
		this.auxiliaryId = auxiliaryId;
	}

	@Column(name = "EXPORT_FLOW", precision = 10)
	public Double getExportFlow() {
		return this.exportFlow;
	}

	public void setExportFlow(Double exportFlow) {
		this.exportFlow = exportFlow;
	}

	@Column(name = "EXPORT_PRESSURE", precision = 10)
	public Double getExportPressure() {
		return this.exportPressure;
	}

	public void setExportPressure(Double exportPressure) {
		this.exportPressure = exportPressure;
	}

	@Column(name = "RATING_SPEED", precision = 10)
	public Double getRatingSpeed() {
		return this.ratingSpeed;
	}

	public void setRatingSpeed(Double ratingSpeed) {
		this.ratingSpeed = ratingSpeed;
	}

	@Column(name = "RATING_EFFICIENCY", precision = 10)
	public Double getRatingEfficiency() {
		return this.ratingEfficiency;
	}

	public void setRatingEfficiency(Double ratingEfficiency) {
		this.ratingEfficiency = ratingEfficiency;
	}

	@Column(name = "MINIMUM_FLOW", precision = 10)
	public Double getMinimumFlow() {
		return this.minimumFlow;
	}

	public void setMinimumFlow(Double minimumFlow) {
		this.minimumFlow = minimumFlow;
	}

	@Column(name = "TAP_FLOW", precision = 10)
	public Double getTapFlow() {
		return this.tapFlow;
	}

	public void setTapFlow(Double tapFlow) {
		this.tapFlow = tapFlow;
	}

	@Column(name = "TAP_PRESSURE", precision = 10)
	public Double getTapPressure() {
		return this.tapPressure;
	}

	public void setTapPressure(Double tapPressure) {
		this.tapPressure = tapPressure;
	}

	@Column(name = "CAVITATION_FLOW", precision = 10)
	public Double getCavitationFlow() {
		return this.cavitationFlow;
	}

	public void setCavitationFlow(Double cavitationFlow) {
		this.cavitationFlow = cavitationFlow;
	}

	@Column(name = "ADJUST_WAY", length = 20)
	public String getAdjustWay() {
		return this.adjustWay;
	}

	public void setAdjustWay(String adjustWay) {
		this.adjustWay = adjustWay;
	}

	@Column(name = "SPEED_RANGE_FROM", precision = 10)
	public Double getSpeedRangeFrom() {
		return this.speedRangeFrom;
	}

	public void setSpeedRangeFrom(Double speedRangeFrom) {
		this.speedRangeFrom = speedRangeFrom;
	}

	@Column(name = "SPEED_RANGE_TO", precision = 10)
	public Double getSpeedRangeTo() {
		return this.speedRangeTo;
	}

	public void setSpeedRangeTo(Double speedRangeTo) {
		this.speedRangeTo = speedRangeTo;
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

	@Column(name = "PRE_PUMP_MODEL", length = 20)
	public String getPrePumpModel() {
		return this.prePumpModel;
	}

	public void setPrePumpModel(String prePumpModel) {
		this.prePumpModel = prePumpModel;
	}

	@Column(name = "NPSH", precision = 10)
	public Double getNpsh() {
		return this.npsh;
	}

	public void setNpsh(Double npsh) {
		this.npsh = npsh;
	}

	@Column(name = "IMPORT_PRESSURE", precision = 10)
	public Double getImportPressure() {
		return this.importPressure;
	}

	public void setImportPressure(Double importPressure) {
		this.importPressure = importPressure;
	}

	@Column(name = "IMPORT_TEMPERATURE", precision = 10)
	public Double getImportTemperature() {
		return this.importTemperature;
	}

	public void setImportTemperature(Double importTemperature) {
		this.importTemperature = importTemperature;
	}

	@Column(name = "PUMP_RATING_SPEED", precision = 10)
	public Double getPumpRatingSpeed() {
		return this.pumpRatingSpeed;
	}

	public void setPumpRatingSpeed(Double pumpRatingSpeed) {
		this.pumpRatingSpeed = pumpRatingSpeed;
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