package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJEmployeeborrowIn entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_EMPLOYEEBORROW_IN")
public class HrJEmployeeborrowIn implements java.io.Serializable {

	// Fields

	private Long hrJEmployeeborrowInId;
	private Long empId;
	private Date endDate;
	private Date startDate;
	private String ifBack;
	private String memo;
	private Long borrowDeptId;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String dcmStatus;

	// Constructors

	/** default constructor */
	public HrJEmployeeborrowIn() {
	}

	/** minimal constructor */
	public HrJEmployeeborrowIn(Long hrJEmployeeborrowInId, Long empId) {
		this.hrJEmployeeborrowInId = hrJEmployeeborrowInId;
		this.empId = empId;
	}

	/** full constructor */
	public HrJEmployeeborrowIn(Long hrJEmployeeborrowInId, Long empId,
			Date endDate, Date startDate, String ifBack, String memo,
			Long borrowDeptId, String enterpriseCode, String isUse,
			String lastModifiedBy, Date lastModifiedDate, String dcmStatus) {
		this.hrJEmployeeborrowInId = hrJEmployeeborrowInId;
		this.empId = empId;
		this.endDate = endDate;
		this.startDate = startDate;
		this.ifBack = ifBack;
		this.memo = memo;
		this.borrowDeptId = borrowDeptId;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.dcmStatus = dcmStatus;
	}

	// Property accessors
	@Id
	@Column(name = "HR_J_EMPLOYEEBORROW_IN_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getHrJEmployeeborrowInId() {
		return this.hrJEmployeeborrowInId;
	}

	public void setHrJEmployeeborrowInId(Long hrJEmployeeborrowInId) {
		this.hrJEmployeeborrowInId = hrJEmployeeborrowInId;
	}

	@Column(name = "EMP_ID", nullable = false, precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
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

	@Column(name = "MEMO", length = 256)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "BORROW_DEPT_ID", precision = 10, scale = 0)
	public Long getBorrowDeptId() {
		return this.borrowDeptId;
	}

	public void setBorrowDeptId(Long borrowDeptId) {
		this.borrowDeptId = borrowDeptId;
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

	@Column(name = "DCM_STATUS", length = 1)
	public String getDcmStatus() {
		return this.dcmStatus;
	}

	public void setDcmStatus(String dcmStatus) {
		this.dcmStatus = dcmStatus;
	}

}