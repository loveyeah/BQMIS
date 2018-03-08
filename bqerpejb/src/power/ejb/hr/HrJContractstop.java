package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJContractstop entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_CONTRACTSTOP")
public class HrJContractstop implements java.io.Serializable {

	// Fields

	private Long contractstopid;
	private Long workcontractid;
	private Date realEndTime;
	private String contractStopType;
	private String releaseReason;
	private Date startDate;
	private Date endDate;
	private String contractTermCode;
	private String dossierDirection;
	private String societyInsuranceDirection;
	private String memo;
	private String insertby;
	private Date insertdate;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private Long empId;
	private Long deptId;
	private Long stationId;
	private String contractTerminatedCode;

	// Constructors

	/** default constructor */
	public HrJContractstop() {
	}

	/** minimal constructor */
	public HrJContractstop(Long contractstopid, Long empId, Long stationId) {
		this.contractstopid = contractstopid;
		this.empId = empId;
		this.stationId = stationId;
	}

	/** full constructor */
	public HrJContractstop(Long contractstopid, Long workcontractid,
			Date realEndTime, String contractStopType, String releaseReason,
			Date startDate, Date endDate, String contractTermCode,
			String dossierDirection, String societyInsuranceDirection,
			String memo, String insertby, Date insertdate,
			String enterpriseCode, String isUse, String lastModifiedBy,
			Date lastModifiedDate, Long empId, Long deptId, Long stationId,
			String contractTerminatedCode) {
		this.contractstopid = contractstopid;
		this.workcontractid = workcontractid;
		this.realEndTime = realEndTime;
		this.contractStopType = contractStopType;
		this.releaseReason = releaseReason;
		this.startDate = startDate;
		this.endDate = endDate;
		this.contractTermCode = contractTermCode;
		this.dossierDirection = dossierDirection;
		this.societyInsuranceDirection = societyInsuranceDirection;
		this.memo = memo;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.empId = empId;
		this.deptId = deptId;
		this.stationId = stationId;
		this.contractTerminatedCode = contractTerminatedCode;
	}

	// Property accessors
	@Id
	@Column(name = "CONTRACTSTOPID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getContractstopid() {
		return this.contractstopid;
	}

	public void setContractstopid(Long contractstopid) {
		this.contractstopid = contractstopid;
	}

	@Column(name = "WORKCONTRACTID", precision = 10, scale = 0)
	public Long getWorkcontractid() {
		return this.workcontractid;
	}

	public void setWorkcontractid(Long workcontractid) {
		this.workcontractid = workcontractid;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "REAL_END_TIME", length = 7)
	public Date getRealEndTime() {
		return this.realEndTime;
	}

	public void setRealEndTime(Date realEndTime) {
		this.realEndTime = realEndTime;
	}

	@Column(name = "CONTRACT_STOP_TYPE", length = 1)
	public String getContractStopType() {
		return this.contractStopType;
	}

	public void setContractStopType(String contractStopType) {
		this.contractStopType = contractStopType;
	}

	@Column(name = "RELEASE_REASON", length = 30)
	public String getReleaseReason() {
		return this.releaseReason;
	}

	public void setReleaseReason(String releaseReason) {
		this.releaseReason = releaseReason;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "CONTRACT_TERM_CODE", length = 1)
	public String getContractTermCode() {
		return this.contractTermCode;
	}

	public void setContractTermCode(String contractTermCode) {
		this.contractTermCode = contractTermCode;
	}

	@Column(name = "DOSSIER_DIRECTION", length = 50)
	public String getDossierDirection() {
		return this.dossierDirection;
	}

	public void setDossierDirection(String dossierDirection) {
		this.dossierDirection = dossierDirection;
	}

	@Column(name = "SOCIETY_INSURANCE_DIRECTION", length = 50)
	public String getSocietyInsuranceDirection() {
		return this.societyInsuranceDirection;
	}

	public void setSocietyInsuranceDirection(String societyInsuranceDirection) {
		this.societyInsuranceDirection = societyInsuranceDirection;
	}

	@Column(name = "MEMO")
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "INSERTBY", length = 16)
	public String getInsertby() {
		return this.insertby;
	}

	public void setInsertby(String insertby) {
		this.insertby = insertby;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "INSERTDATE", length = 7)
	public Date getInsertdate() {
		return this.insertdate;
	}

	public void setInsertdate(Date insertdate) {
		this.insertdate = insertdate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "LAST_MODIFIED_BY", length = 16)
	public String getLastModifiedBy() {
		return this.lastModifiedBy;
	}

	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIED_DATE", length = 7)
	public Date getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	public void setLastModifiedDate(Date lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	@Column(name = "EMP_ID", nullable = false, precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "DEPT_ID", precision = 10, scale = 0)
	public Long getDeptId() {
		return this.deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	@Column(name = "STATION_ID", nullable = false, precision = 10, scale = 0)
	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	@Column(name = "CONTRACT_TERMINATED_CODE", length = 50)
	public String getContractTerminatedCode() {
		return this.contractTerminatedCode;
	}

	public void setContractTerminatedCode(String contractTerminatedCode) {
		this.contractTerminatedCode = contractTerminatedCode;
	}

}