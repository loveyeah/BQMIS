package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJEmployeeborrow entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_EMPLOYEEBORROW")
public class HrJEmployeeborrow implements java.io.Serializable {

	// Fields

	private Long employeeborrowid;
	private Long borrowcontractid;
	private Date startDate;
	private String ifBack;
	private Date endDate;
	private Date deductStartDate;
	private Date deductEndDate;
	private Date stopPayDate;
	private Date startPayDate;
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




	/** default constructor */
	public HrJEmployeeborrow() {
	}

	/** minimal constructor */
	public HrJEmployeeborrow(Long employeeborrowid, Long empId) {
		this.employeeborrowid = employeeborrowid;
		this.empId = empId;
	}

	/** full constructor */
	public HrJEmployeeborrow(Long employeeborrowid, Long borrowcontractid,
			Date startDate, String ifBack, Date endDate, Date deductStartDate,
			Date deductEndDate, Date stopPayDate, Date startPayDate,
			String memo, String insertby, Date insertdate,
			String enterpriseCode, String isUse, String lastModifiedBy,
			Date lastModifiedDate, Long empId, Long deptId, Long stationId) {
		this.employeeborrowid = employeeborrowid;
		this.borrowcontractid = borrowcontractid;
		this.startDate = startDate;
		this.ifBack = ifBack;
		this.endDate = endDate;
		this.deductStartDate = deductStartDate;
		this.deductEndDate = deductEndDate;
		this.stopPayDate = stopPayDate;
		this.startPayDate = startPayDate;
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
	}

	// Property accessors
	@Id
	@Column(name = "EMPLOYEEBORROWID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getEmployeeborrowid() {
		return this.employeeborrowid;
	}

	public void setEmployeeborrowid(Long employeeborrowid) {
		this.employeeborrowid = employeeborrowid;
	}

	@Column(name = "BORROWCONTRACTID", precision = 10, scale = 0)
	public Long getBorrowcontractid() {
		return this.borrowcontractid;
	}

	public void setBorrowcontractid(Long borrowcontractid) {
		this.borrowcontractid = borrowcontractid;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Column(name = "IF_BACK", length = 1)
	public String getIfBack() {
		return this.ifBack;
	}

	public void setIfBack(String ifBack) {
		this.ifBack = ifBack;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DEDUCT_START_DATE", length = 7)
	public Date getDeductStartDate() {
		return this.deductStartDate;
	}

	public void setDeductStartDate(Date deductStartDate) {
		this.deductStartDate = deductStartDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "DEDUCT_END_DATE", length = 7)
	public Date getDeductEndDate() {
		return this.deductEndDate;
	}

	public void setDeductEndDate(Date deductEndDate) {
		this.deductEndDate = deductEndDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "STOP_PAY_DATE", length = 7)
	public Date getStopPayDate() {
		return this.stopPayDate;
	}

	public void setStopPayDate(Date stopPayDate) {
		this.stopPayDate = stopPayDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_PAY_DATE", length = 7)
	public Date getStartPayDate() {
		return this.startPayDate;
	}

	public void setStartPayDate(Date startPayDate) {
		this.startPayDate = startPayDate;
	}

	@Column(name = "MEMO", length = 256)
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

	@Temporal(TemporalType.DATE)
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

	@Column(name = "STATION_ID", precision = 10, scale = 0)
	public Long getStationId() {
		return this.stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

}