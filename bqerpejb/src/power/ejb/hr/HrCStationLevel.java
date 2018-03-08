package power.ejb.hr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCStationLevel entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_STATION_LEVEL", schema = "POWER")
public class HrCStationLevel implements java.io.Serializable {

	// Fields

	private Long stationLevelId;
	private Long stationId;
	private String stationLevelName;
	private Long stationLevel;
	private Long gradation;
	private String isUse;
	private String retrieveCode;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrCStationLevel() {
	}

	/** minimal constructor */
	public HrCStationLevel(Long stationLevelId) {
		this.stationLevelId = stationLevelId;
	}

	/** full constructor */
	public HrCStationLevel(Long stationLevelId, Long stationId,
			String stationLevelName, Long stationLevel, Long gradation,
			String isUse, String retrieveCode, String enterpriseCode) {
		this.stationLevelId = stationLevelId;
		this.stationId = stationId;
		this.stationLevelName = stationLevelName;
		this.stationLevel = stationLevel;
		this.gradation = gradation;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "STATION_LEVEL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getStationLevelId() {
		return this.stationLevelId;
	}

	public void setStationLevelId(Long stationLevelId) {
		this.stationLevelId = stationLevelId;
	}

	@Column(name = "STATION_ID", precision = 10, scale = 0)
	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	@Column(name = "STATION_LEVEL_NAME", length = 100)
	public String getStationLevelName() {
		return this.stationLevelName;
	}

	public void setStationLevelName(String stationLevelName) {
		this.stationLevelName = stationLevelName;
	}

	@Column(name = "STATION_LEVEL", precision = 10, scale = 0)
	public Long getStationLevel() {
		return this.stationLevel;
	}

	public void setStationLevel(Long stationLevel) {
		this.stationLevel = stationLevel;
	}

	@Column(name = "GRADATION", precision = 10, scale = 0)
	public Long getGradation() {
		return this.gradation;
	}

	public void setGradation(Long gradation) {
		this.gradation = gradation;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "RETRIEVE_CODE", length = 20)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}