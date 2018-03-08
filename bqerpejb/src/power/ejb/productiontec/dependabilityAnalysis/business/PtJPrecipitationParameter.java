package power.ejb.productiontec.dependabilityAnalysis.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJPrecipitationParameter entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "PT_J_PRECIPITATION_PARAMETER")
public class PtJPrecipitationParameter implements java.io.Serializable {

	// Fields

	private Long precipitationId;
	private Long auxiliaryId;
	private String precipitationType;
	private Double clearDustEffi;
	private Double dustArea;
	private Double importAirTpr;
	private Double importAirThickness;
	private Double windLeakRate;
	private Double dealAirQuantity;
	private Double maxNegativePressure;
	private Double resistanceLoss;
	private Long electricFieldNum;
	private Long roomNum;
	private Double sameSpace;
	private Double electricFieldWidth;
	private Double electricFieldHeight;
	private Double electricFieldLength;
	private String anodeType;
	private String cathodeType;
	private String oldCode;
	private String memo;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJPrecipitationParameter() {
	}

	/** minimal constructor */
	public PtJPrecipitationParameter(Long precipitationId) {
		this.precipitationId = precipitationId;
	}

	/** full constructor */
	public PtJPrecipitationParameter(Long precipitationId, Long auxiliaryId,
			String precipitationType, Double clearDustEffi, Double dustArea,
			Double importAirTpr, Double importAirThickness,
			Double windLeakRate, Double dealAirQuantity,
			Double maxNegativePressure, Double resistanceLoss,
			Long electricFieldNum, Long roomNum, Double sameSpace,
			Double electricFieldWidth, Double electricFieldHeight,
			Double electricFieldLength, String anodeType, String cathodeType,
			String oldCode, String memo, String isUse, String enterpriseCode) {
		this.precipitationId = precipitationId;
		this.auxiliaryId = auxiliaryId;
		this.precipitationType = precipitationType;
		this.clearDustEffi = clearDustEffi;
		this.dustArea = dustArea;
		this.importAirTpr = importAirTpr;
		this.importAirThickness = importAirThickness;
		this.windLeakRate = windLeakRate;
		this.dealAirQuantity = dealAirQuantity;
		this.maxNegativePressure = maxNegativePressure;
		this.resistanceLoss = resistanceLoss;
		this.electricFieldNum = electricFieldNum;
		this.roomNum = roomNum;
		this.sameSpace = sameSpace;
		this.electricFieldWidth = electricFieldWidth;
		this.electricFieldHeight = electricFieldHeight;
		this.electricFieldLength = electricFieldLength;
		this.anodeType = anodeType;
		this.cathodeType = cathodeType;
		this.oldCode = oldCode;
		this.memo = memo;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PRECIPITATION_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPrecipitationId() {
		return this.precipitationId;
	}

	public void setPrecipitationId(Long precipitationId) {
		this.precipitationId = precipitationId;
	}

	@Column(name = "AUXILIARY_ID", precision = 10, scale = 0)
	public Long getAuxiliaryId() {
		return this.auxiliaryId;
	}

	public void setAuxiliaryId(Long auxiliaryId) {
		this.auxiliaryId = auxiliaryId;
	}

	@Column(name = "PRECIPITATION_TYPE", length = 20)
	public String getPrecipitationType() {
		return this.precipitationType;
	}

	public void setPrecipitationType(String precipitationType) {
		this.precipitationType = precipitationType;
	}

	@Column(name = "CLEAR_DUST_EFFI", precision = 10)
	public Double getClearDustEffi() {
		return this.clearDustEffi;
	}

	public void setClearDustEffi(Double clearDustEffi) {
		this.clearDustEffi = clearDustEffi;
	}

	@Column(name = "DUST_AREA", precision = 10)
	public Double getDustArea() {
		return this.dustArea;
	}

	public void setDustArea(Double dustArea) {
		this.dustArea = dustArea;
	}

	@Column(name = "IMPORT_AIR_TPR", precision = 10)
	public Double getImportAirTpr() {
		return this.importAirTpr;
	}

	public void setImportAirTpr(Double importAirTpr) {
		this.importAirTpr = importAirTpr;
	}

	@Column(name = "IMPORT_AIR_THICKNESS", precision = 10)
	public Double getImportAirThickness() {
		return this.importAirThickness;
	}

	public void setImportAirThickness(Double importAirThickness) {
		this.importAirThickness = importAirThickness;
	}

	@Column(name = "WIND_LEAK_RATE", precision = 10)
	public Double getWindLeakRate() {
		return this.windLeakRate;
	}

	public void setWindLeakRate(Double windLeakRate) {
		this.windLeakRate = windLeakRate;
	}

	@Column(name = "DEAL_AIR_QUANTITY", precision = 10)
	public Double getDealAirQuantity() {
		return this.dealAirQuantity;
	}

	public void setDealAirQuantity(Double dealAirQuantity) {
		this.dealAirQuantity = dealAirQuantity;
	}

	@Column(name = "MAX_NEGATIVE_PRESSURE", precision = 10)
	public Double getMaxNegativePressure() {
		return this.maxNegativePressure;
	}

	public void setMaxNegativePressure(Double maxNegativePressure) {
		this.maxNegativePressure = maxNegativePressure;
	}

	@Column(name = "RESISTANCE_LOSS", precision = 10)
	public Double getResistanceLoss() {
		return this.resistanceLoss;
	}

	public void setResistanceLoss(Double resistanceLoss) {
		this.resistanceLoss = resistanceLoss;
	}

	@Column(name = "ELECTRIC_FIELD_NUM", precision = 10, scale = 0)
	public Long getElectricFieldNum() {
		return this.electricFieldNum;
	}

	public void setElectricFieldNum(Long electricFieldNum) {
		this.electricFieldNum = electricFieldNum;
	}

	@Column(name = "ROOM_NUM", precision = 10, scale = 0)
	public Long getRoomNum() {
		return this.roomNum;
	}

	public void setRoomNum(Long roomNum) {
		this.roomNum = roomNum;
	}

	@Column(name = "SAME_SPACE", precision = 10)
	public Double getSameSpace() {
		return this.sameSpace;
	}

	public void setSameSpace(Double sameSpace) {
		this.sameSpace = sameSpace;
	}

	@Column(name = "ELECTRIC_FIELD_WIDTH", precision = 10)
	public Double getElectricFieldWidth() {
		return this.electricFieldWidth;
	}

	public void setElectricFieldWidth(Double electricFieldWidth) {
		this.electricFieldWidth = electricFieldWidth;
	}

	@Column(name = "ELECTRIC_FIELD_HEIGHT", precision = 10)
	public Double getElectricFieldHeight() {
		return this.electricFieldHeight;
	}

	public void setElectricFieldHeight(Double electricFieldHeight) {
		this.electricFieldHeight = electricFieldHeight;
	}

	@Column(name = "ELECTRIC_FIELD_LENGTH", precision = 10)
	public Double getElectricFieldLength() {
		return this.electricFieldLength;
	}

	public void setElectricFieldLength(Double electricFieldLength) {
		this.electricFieldLength = electricFieldLength;
	}

	@Column(name = "ANODE_TYPE", length = 20)
	public String getAnodeType() {
		return this.anodeType;
	}

	public void setAnodeType(String anodeType) {
		this.anodeType = anodeType;
	}

	@Column(name = "CATHODE_TYPE", length = 20)
	public String getCathodeType() {
		return this.cathodeType;
	}

	public void setCathodeType(String cathodeType) {
		this.cathodeType = cathodeType;
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