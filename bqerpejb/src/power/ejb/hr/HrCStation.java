package power.ejb.hr;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * HrCStation entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "HR_C_STATION", schema = "power")
public class HrCStation implements java.io.Serializable {

	// Fields

	private Long stationId;
	private Long stationTypeId;
	private String stationCode;
	private String stationName;
	private String stationDuty;
	private String isUse;
	private String memo;
	private String retrieveCode;
	private Long ordeyBy;

	//add by drdu 090929
	private String workKind;
	private Long stationLevelId;
	private String enterpriseCode;

	/** default constructor */
	public HrCStation() {
	}

	/** minimal constructor */
	public HrCStation(Long stationId) {
		this.stationId = stationId;
	}

	/** full constructor */
	public HrCStation(Long stationId, Long stationTypeId, String stationCode,
			String stationName, String stationDuty, String isUse, String memo,
			String retrieveCode, Long ordeyBy,String workKind,Long stationLevelId) {
		this.stationId = stationId;
		this.stationTypeId = stationTypeId;
		this.stationCode = stationCode;
		this.stationName = stationName;
		this.stationDuty = stationDuty;
		this.isUse = isUse;
		this.memo = memo;
		this.retrieveCode = retrieveCode;
		this.ordeyBy = ordeyBy;
		this.workKind = workKind;
		this.stationLevelId = stationLevelId;
	}


	// Property accessors
	@Id
	@Column(name = "STATION_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	@Column(name = "STATION_TYPE_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getStationTypeId() {
		return this.stationTypeId;
	}

	public void setStationTypeId(Long stationTypeId) {
		this.stationTypeId = stationTypeId;
	}

	@Column(name = "STATION_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getStationCode() {
		return this.stationCode;
	}

	public void setStationCode(String stationCode) {
		this.stationCode = stationCode;
	}

	@Column(name = "STATION_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 100)
	public String getStationName() {
		return this.stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Column(name = "STATION_DUTY", unique = false, nullable = true, insertable = true, updatable = true, length = 500)
	public String getStationDuty() {
		return this.stationDuty;
	}

	public void setStationDuty(String stationDuty) {
		this.stationDuty = stationDuty;
	}

	@Column(name = "IS_USE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "MEMO", unique = false, nullable = true, insertable = true, updatable = true, length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "RETRIEVE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 20)
	public String getRetrieveCode() {
		return this.retrieveCode;
	}

	public void setRetrieveCode(String retrieveCode) {
		this.retrieveCode = retrieveCode;
	}

	@Column(name = "ORDEY_BY", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getOrdeyBy() {
		return this.ordeyBy;
	}

	public void setOrdeyBy(Long ordeyBy) {
		this.ordeyBy = ordeyBy;
	}
	
	@Column(name = "WORK_KIND",length = 1)
	public String getWorkKind() {
		return workKind;
	}

	public void setWorkKind(String workKind) {
		this.workKind = workKind;
	}

	@Column(name = "STATION_LEVEL_ID",unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getStationLevelId() {
		return stationLevelId;
	}

	public void setStationLevelId(Long stationLevelId) {
		this.stationLevelId = stationLevelId;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}