package power.ejb.productiontec.dependabilityAnalysis.business;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * PtJPulverizerParameter entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_J_PULVERIZER_PARAMETER")
public class PtJPulverizerParameter implements java.io.Serializable {

	// Fields

	private Long pulverizerId;
	private Long auxiliaryId;
	private Double ratingCl;
	private Double ratingSpeed;
	private String speedType;
	private String systemType;
	private String oldCode;
	private String memo;
	private String electromotorNo;
	private Double ratingPower;
	private Double electRatingSpeed;
	private Double ratingVoltage;
	private Double ratingElectricity;
	private String shiftType;
	private String factoryCode;
	private String factoryName;
	private String isUse;
	private String enterpriseCode;
	private String connType;

	// Constructors

	

	/** default constructor */
	public PtJPulverizerParameter() {
	}

	/** minimal constructor */
	public PtJPulverizerParameter(Long pulverizerId) {
		this.pulverizerId = pulverizerId;
	}

	/** full constructor */
	public PtJPulverizerParameter(Long pulverizerId, Long auxiliaryId,
			Double ratingCl, Double ratingSpeed, String speedType,
			String systemType, String oldCode, String memo,
			String electromotorNo, Double ratingPower, Double electRatingSpeed,
			Double ratingVoltage, Double ratingElectricity, String shiftType,
			String factoryCode, String factoryName, String isUse,
			String enterpriseCode) {
		this.pulverizerId = pulverizerId;
		this.auxiliaryId = auxiliaryId;
		this.ratingCl = ratingCl;
		this.ratingSpeed = ratingSpeed;
		this.speedType = speedType;
		this.systemType = systemType;
		this.oldCode = oldCode;
		this.memo = memo;
		this.electromotorNo = electromotorNo;
		this.ratingPower = ratingPower;
		this.electRatingSpeed = electRatingSpeed;
		this.ratingVoltage = ratingVoltage;
		this.ratingElectricity = ratingElectricity;
		this.shiftType = shiftType;
		this.factoryCode = factoryCode;
		this.factoryName = factoryName;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "PULVERIZER_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getPulverizerId() {
		return this.pulverizerId;
	}

	public void setPulverizerId(Long pulverizerId) {
		this.pulverizerId = pulverizerId;
	}

	@Column(name = "AUXILIARY_ID", precision = 10, scale = 0)
	public Long getAuxiliaryId() {
		return this.auxiliaryId;
	}

	public void setAuxiliaryId(Long auxiliaryId) {
		this.auxiliaryId = auxiliaryId;
	}

	@Column(name = "RATING_CL", precision = 10)
	public Double getRatingCl() {
		return this.ratingCl;
	}

	public void setRatingCl(Double ratingCl) {
		this.ratingCl = ratingCl;
	}

	@Column(name = "RATING_SPEED", precision = 10)
	public Double getRatingSpeed() {
		return this.ratingSpeed;
	}

	public void setRatingSpeed(Double ratingSpeed) {
		this.ratingSpeed = ratingSpeed;
	}

	@Column(name = "SPEED_TYPE", length = 20)
	public String getSpeedType() {
		return this.speedType;
	}

	public void setSpeedType(String speedType) {
		this.speedType = speedType;
	}

	@Column(name = "SYSTEM_TYPE", length = 20)
	public String getSystemType() {
		return this.systemType;
	}

	public void setSystemType(String systemType) {
		this.systemType = systemType;
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

	@Column(name = "ELECTROMOTOR_NO", length = 20)
	public String getElectromotorNo() {
		return this.electromotorNo;
	}

	public void setElectromotorNo(String electromotorNo) {
		this.electromotorNo = electromotorNo;
	}

	@Column(name = "RATING_POWER", precision = 10)
	public Double getRatingPower() {
		return this.ratingPower;
	}

	public void setRatingPower(Double ratingPower) {
		this.ratingPower = ratingPower;
	}

	@Column(name = "ELECT_RATING_SPEED", precision = 10)
	public Double getElectRatingSpeed() {
		return this.electRatingSpeed;
	}

	public void setElectRatingSpeed(Double electRatingSpeed) {
		this.electRatingSpeed = electRatingSpeed;
	}

	@Column(name = "RATING_VOLTAGE", precision = 10)
	public Double getRatingVoltage() {
		return this.ratingVoltage;
	}

	public void setRatingVoltage(Double ratingVoltage) {
		this.ratingVoltage = ratingVoltage;
	}

	@Column(name = "RATING_ELECTRICITY", precision = 10)
	public Double getRatingElectricity() {
		return this.ratingElectricity;
	}

	public void setRatingElectricity(Double ratingElectricity) {
		this.ratingElectricity = ratingElectricity;
	}

	@Column(name = "SHIFT_TYPE", length = 20)
	public String getShiftType() {
		return this.shiftType;
	}

	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}

	@Column(name = "CONN_TYPE", length = 20)
	public String getConnType() {
		return connType;
	}

	public void setConnType(String connType) {
		this.connType = connType;
	}
	@Column(name = "FACTORY_CODE", length = 20)
	public String getFactoryCode() {
		return this.factoryCode;
	}

	public void setFactoryCode(String factoryCode) {
		this.factoryCode = factoryCode;
	}

	@Column(name = "FACTORY_NAME", length = 50)
	public String getFactoryName() {
		return this.factoryName;
	}

	public void setFactoryName(String factoryName) {
		this.factoryName = factoryName;
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