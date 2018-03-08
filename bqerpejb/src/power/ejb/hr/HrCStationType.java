package power.ejb.hr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCStationType entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_C_STATION_TYPE", schema = "LANWAN", uniqueConstraints = {})
public class HrCStationType implements java.io.Serializable {

	// Fields

	private Long stationTypeId;
	private String stationTypeName;
	private String isUse;
	private String retrieveCode;

	// Constructors

	/** default constructor */
	public HrCStationType() {
	}

	/** minimal constructor */
	public HrCStationType(Long stationTypeId) {
		this.stationTypeId = stationTypeId;
	}

	/** full constructor */
	public HrCStationType(Long stationTypeId, String stationTypeName,
			String isUse, String retrieveCode) {
		this.stationTypeId = stationTypeId;
		this.stationTypeName = stationTypeName;
		this.isUse = isUse;
		this.retrieveCode = retrieveCode;
	}

	// Property accessors
	@Id
	@Column(name = "STATION_TYPE_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getStationTypeId() {
		return this.stationTypeId;
	}

	public void setStationTypeId(Long stationTypeId) {
		this.stationTypeId = stationTypeId;
	}

	@Column(name = "STATION_TYPE_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getStationTypeName() {
		return this.stationTypeName;
	}

	public void setStationTypeName(String stationTypeName) {
		this.stationTypeName = stationTypeName;
	}

	@Column(name = "IS_USE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "RETRIEVE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

}