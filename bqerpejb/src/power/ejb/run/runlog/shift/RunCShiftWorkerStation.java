package power.ejb.run.runlog.shift;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * RunCShiftWorkerStation entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "RUN_C_SHIFT_WORKER_STATION", uniqueConstraints = {})
public class RunCShiftWorkerStation implements java.io.Serializable {

	// Fields

	private Long workerStationId;
	private Long shiftWorkerId;
	private Long stationId;
	private String stationName;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public RunCShiftWorkerStation() {
	}

	/** minimal constructor */
	public RunCShiftWorkerStation(Long workerStationId) {
		this.workerStationId = workerStationId;
	}

	/** full constructor */
	public RunCShiftWorkerStation(Long workerStationId, Long shiftWorkerId,
			Long stationId, String stationName, String isUse,
			String enterpriseCode) {
		this.workerStationId = workerStationId;
		this.shiftWorkerId = shiftWorkerId;
		this.stationId = stationId;
		this.stationName = stationName;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "WORKER_STATION_ID", unique = true, nullable = false, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getWorkerStationId() {
		return this.workerStationId;
	}

	public void setWorkerStationId(Long workerStationId) {
		this.workerStationId = workerStationId;
	}

	@Column(name = "SHIFT_WORKER_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getShiftWorkerId() {
		return this.shiftWorkerId;
	}

	public void setShiftWorkerId(Long shiftWorkerId) {
		this.shiftWorkerId = shiftWorkerId;
	}

	@Column(name = "STATION_ID", unique = false, nullable = true, insertable = true, updatable = true, precision = 10, scale = 0)
	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	@Column(name = "STATION_NAME", unique = false, nullable = true, insertable = true, updatable = true, length = 40)
	public String getStationName() {
		return this.stationName;
	}

	public void setStationName(String stationName) {
		this.stationName = stationName;
	}

	@Column(name = "IS_USE", unique = false, nullable = true, insertable = true, updatable = true, length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", unique = false, nullable = true, insertable = true, updatable = true, length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}