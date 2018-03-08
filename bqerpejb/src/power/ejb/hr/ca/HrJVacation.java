package power.ejb.hr.ca;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJVacation entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_VACATION")
public class HrJVacation implements java.io.Serializable {

	// Fields
	/** serial id */
	private static final long serialVersionUID = 1L;
	private Long vacationid;
	private Long empId;
	private Long deptId;
	private Long vacationTypeId;
	private Date startTime;
	private Date endTime;
	private Double vacationDays;
	private Double vacationTime;
	private String reason;
	private String whither;
	private String memo;
	private String ifClear;
	private Date clearDate;
	private String insertby;
	private Date insertdate;
	private String signState;
	private String workFlowNo;
	private String lastModifiyBy;
	private Date lastModifiyDate;
	private String isUse;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public HrJVacation() {
	}

	/** minimal constructor */
	public HrJVacation(Long vacationid) {
		this.vacationid = vacationid;
	}

	/** full constructor */
	public HrJVacation(Long vacationid, Long empId, Long deptId,
			Long vacationTypeId, Date startTime, Date endTime,
			Double vacationDays, Double vacationTime, String reason,
			String whither, String memo, String ifClear, Date clearDate,
			String insertby, Date insertdate, String signState,
			String workFlowNo, String lastModifiyBy, Date lastModifiyDate,
			String isUse, String enterpriseCode) {
		this.vacationid = vacationid;
		this.empId = empId;
		this.deptId = deptId;
		this.vacationTypeId = vacationTypeId;
		this.startTime = startTime;
		this.endTime = endTime;
		this.vacationDays = vacationDays;
		this.vacationTime = vacationTime;
		this.reason = reason;
		this.whither = whither;
		this.memo = memo;
		this.ifClear = ifClear;
		this.clearDate = clearDate;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.signState = signState;
		this.workFlowNo = workFlowNo;
		this.lastModifiyBy = lastModifiyBy;
		this.lastModifiyDate = lastModifiyDate;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "VACATIONID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getVacationid() {
		return this.vacationid;
	}

	public void setVacationid(Long vacationid) {
		this.vacationid = vacationid;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
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

	@Column(name = "VACATION_TYPE_ID", precision = 10, scale = 0)
	public Long getVacationTypeId() {
		return this.vacationTypeId;
	}

	public void setVacationTypeId(Long vacationTypeId) {
		this.vacationTypeId = vacationTypeId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "START_TIME", length = 7)
	public Date getStartTime() {
		return this.startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "END_TIME", length = 7)
	public Date getEndTime() {
		return this.endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	@Column(name = "VACATION_DAYS", precision = 15, scale = 4)
	public Double getVacationDays() {
		return this.vacationDays;
	}

	public void setVacationDays(Double vacationDays) {
		this.vacationDays = vacationDays;
	}

	@Column(name = "VACATION_TIME", precision = 15, scale = 4)
	public Double getVacationTime() {
		return this.vacationTime;
	}

	public void setVacationTime(Double vacationTime) {
		this.vacationTime = vacationTime;
	}

	@Column(name = "REASON", length = 400)
	public String getReason() {
		return this.reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	@Column(name = "WHITHER", length = 100)
	public String getWhither() {
		return this.whither;
	}

	public void setWhither(String whither) {
		this.whither = whither;
	}

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "IF_CLEAR", length = 1)
	public String getIfClear() {
		return this.ifClear;
	}

	public void setIfClear(String ifClear) {
		this.ifClear = ifClear;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "CLEAR_DATE", length = 7)
	public Date getClearDate() {
		return this.clearDate;
	}

	public void setClearDate(Date clearDate) {
		this.clearDate = clearDate;
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

	@Column(name = "SIGN_STATE", length = 1)
	public String getSignState() {
		return this.signState;
	}

	public void setSignState(String signState) {
		this.signState = signState;
	}

	@Column(name = "WORK_FLOW_NO", length = 26)
	public String getWorkFlowNo() {
		return this.workFlowNo;
	}

	public void setWorkFlowNo(String workFlowNo) {
		this.workFlowNo = workFlowNo;
	}

	@Column(name = "LAST_MODIFIY_BY", length = 16)
	public String getLastModifiyBy() {
		return this.lastModifiyBy;
	}

	public void setLastModifiyBy(String lastModifiyBy) {
		this.lastModifiyBy = lastModifiyBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LAST_MODIFIY_DATE", length = 7)
	public Date getLastModifiyDate() {
		return this.lastModifiyDate;
	}

	public void setLastModifiyDate(Date lastModifiyDate) {
		this.lastModifiyDate = lastModifiyDate;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 10)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}