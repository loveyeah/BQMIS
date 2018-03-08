package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJWorkcontract entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_WORKCONTRACT")
public class HrJWorkcontract implements java.io.Serializable {

	// Fields

	private Long workcontractid;
	private Long empId;
	private Long deptId;
	private Long stationId;
	private String wrokContractNo;
	private Long fristDepId;
	private String fristAddrest;
	private Long contractTermId;
	private Date workSignDate;
	private Date startDate;
	private Date endDate;
	private Date tryoutStartDate;
	private Date tryoutEndDate;
	private String ifExecute;
	private String contractContinueMark;
	private String memo;
	private String insertby;
	private Date insertdate;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String owner;
	private String signedInstitutions;
	private String contractPeriod;
	private String laborType;
	private String contractType;

	// Constructors

	/** default constructor */
	public HrJWorkcontract() {
	}

	/** minimal constructor */
	public HrJWorkcontract(Long workcontractid, Long empId) {
		this.workcontractid = workcontractid;
		this.empId = empId;
	}

	/** full constructor */
	public HrJWorkcontract(Long workcontractid, Long empId, Long deptId,
			Long stationId, String wrokContractNo, Long fristDepId,
			String fristAddrest, Long contractTermId, Date workSignDate,
			Date startDate, Date endDate, Date tryoutStartDate,
			Date tryoutEndDate, String ifExecute, String contractContinueMark,
			String memo, String insertby, Date insertdate,
			String enterpriseCode, String isUse, String lastModifiedBy,
			Date lastModifiedDate, String owner, String signedInstitutions,
			String contractPeriod, String laborType, String contractType) {
		this.workcontractid = workcontractid;
		this.empId = empId;
		this.deptId = deptId;
		this.stationId = stationId;
		this.wrokContractNo = wrokContractNo;
		this.fristDepId = fristDepId;
		this.fristAddrest = fristAddrest;
		this.contractTermId = contractTermId;
		this.workSignDate = workSignDate;
		this.startDate = startDate;
		this.endDate = endDate;
		this.tryoutStartDate = tryoutStartDate;
		this.tryoutEndDate = tryoutEndDate;
		this.ifExecute = ifExecute;
		this.contractContinueMark = contractContinueMark;
		this.memo = memo;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.owner = owner;
		this.signedInstitutions = signedInstitutions;
		this.contractPeriod = contractPeriod;
		this.laborType = laborType;
		this.contractType = contractType;
	}

	// Property accessors
	@Id
	@Column(name = "WORKCONTRACTID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getWorkcontractid() {
		return this.workcontractid;
	}

	public void setWorkcontractid(Long workcontractid) {
		this.workcontractid = workcontractid;
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

	@Column(name = "STATION_ID", precision = 10, scale = 0)
	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	@Column(name = "WROK_CONTRACT_NO", length = 20)
	public String getWrokContractNo() {
		return this.wrokContractNo;
	}

	public void setWrokContractNo(String wrokContractNo) {
		this.wrokContractNo = wrokContractNo;
	}

	@Column(name = "FRIST_DEP_ID", precision = 10, scale = 0)
	public Long getFristDepId() {
		return this.fristDepId;
	}

	public void setFristDepId(Long fristDepId) {
		this.fristDepId = fristDepId;
	}

	@Column(name = "FRIST_ADDREST", length = 30)
	public String getFristAddrest() {
		return this.fristAddrest;
	}

	public void setFristAddrest(String fristAddrest) {
		this.fristAddrest = fristAddrest;
	}

	@Column(name = "CONTRACT_TERM_ID", precision = 10, scale = 0)
	public Long getContractTermId() {
		return this.contractTermId;
	}

	public void setContractTermId(Long contractTermId) {
		this.contractTermId = contractTermId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "WORK_SIGN_DATE", length = 7)
	public Date getWorkSignDate() {
		return this.workSignDate;
	}

	public void setWorkSignDate(Date workSignDate) {
		this.workSignDate = workSignDate;
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

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TRYOUT_START_DATE", length = 7)
	public Date getTryoutStartDate() {
		return this.tryoutStartDate;
	}

	public void setTryoutStartDate(Date tryoutStartDate) {
		this.tryoutStartDate = tryoutStartDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "TRYOUT_END_DATE", length = 7)
	public Date getTryoutEndDate() {
		return this.tryoutEndDate;
	}

	public void setTryoutEndDate(Date tryoutEndDate) {
		this.tryoutEndDate = tryoutEndDate;
	}

	@Column(name = "IF_EXECUTE", length = 1)
	public String getIfExecute() {
		return this.ifExecute;
	}

	public void setIfExecute(String ifExecute) {
		this.ifExecute = ifExecute;
	}

	@Column(name = "CONTRACT_CONTINUE_MARK", length = 1)
	public String getContractContinueMark() {
		return this.contractContinueMark;
	}

	public void setContractContinueMark(String contractContinueMark) {
		this.contractContinueMark = contractContinueMark;
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

	@Column(name = "OWNER", length = 50)
	public String getOwner() {
		return this.owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	@Column(name = "SIGNED_INSTITUTIONS", length = 50)
	public String getSignedInstitutions() {
		return this.signedInstitutions;
	}

	public void setSignedInstitutions(String signedInstitutions) {
		this.signedInstitutions = signedInstitutions;
	}

	@Column(name = "CONTRACT_PERIOD", length = 50)
	public String getContractPeriod() {
		return this.contractPeriod;
	}

	public void setContractPeriod(String contractPeriod) {
		this.contractPeriod = contractPeriod;
	}

	@Column(name = "LABOR_TYPE", length = 1)
	public String getLaborType() {
		return this.laborType;
	}

	public void setLaborType(String laborType) {
		this.laborType = laborType;
	}

	@Column(name = "CONTRACT_TYPE", length = 1)
	public String getContractType() {
		return this.contractType;
	}

	public void setContractType(String contractType) {
		this.contractType = contractType;
	}

}