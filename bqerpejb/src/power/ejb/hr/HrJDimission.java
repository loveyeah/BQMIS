package power.ejb.hr;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * HrJDimission entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "HR_J_DIMISSION")
public class HrJDimission implements java.io.Serializable {

	// Fields

	private Long dimissionid;
	private Long empId;
	private Long outTypeId;
	private Date dimissionDate;
	private String dimissionReason;
	private String ifSave;
	private String whither;
	private String memo;
	private String insertby;
	private Date insertdate;
	private String enterpriseCode;
	private String isUse;
	private String lastModifiedBy;
	private Date lastModifiedDate;
	private String advicenoteNo;
	private Date stopsalaryDate; 
    private Date registerDate; /** 登记时间 add by sychen 20100717*/

	// Constructors

	/** default constructor */
	public HrJDimission() {
	}

	/** minimal constructor */
	public HrJDimission(Long dimissionid) {
		this.dimissionid = dimissionid;
	}

	/** full constructor */
	public HrJDimission(Long dimissionid, Long empId, Long outTypeId,
			Date dimissionDate, String dimissionReason, String ifSave,
			String whither, String memo, String insertby, Date insertdate,
			String enterpriseCode, String isUse, String lastModifiedBy,
			Date lastModifiedDate, String advicenoteNo, Date stopsalaryDate) {
		this.dimissionid = dimissionid;
		this.empId = empId;
		this.outTypeId = outTypeId;
		this.dimissionDate = dimissionDate;
		this.dimissionReason = dimissionReason;
		this.ifSave = ifSave;
		this.whither = whither;
		this.memo = memo;
		this.insertby = insertby;
		this.insertdate = insertdate;
		this.enterpriseCode = enterpriseCode;
		this.isUse = isUse;
		this.lastModifiedBy = lastModifiedBy;
		this.lastModifiedDate = lastModifiedDate;
		this.advicenoteNo = advicenoteNo;
		this.stopsalaryDate = stopsalaryDate;
	}

	// Property accessors
	@Id
	@Column(name = "DIMISSIONID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getDimissionid() {
		return this.dimissionid;
	}

	public void setDimissionid(Long dimissionid) {
		this.dimissionid = dimissionid;
	}

	@Column(name = "EMP_ID", precision = 10, scale = 0)
	public Long getEmpId() {
		return this.empId;
	}

	public void setEmpId(Long empId) {
		this.empId = empId;
	}

	@Column(name = "OUT_TYPE_ID", precision = 10, scale = 0)
	public Long getOutTypeId() {
		return this.outTypeId;
	}

	public void setOutTypeId(Long outTypeId) {
		this.outTypeId = outTypeId;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DIMISSION_DATE", length = 7)
	public Date getDimissionDate() {
		return this.dimissionDate;
	}

	public void setDimissionDate(Date dimissionDate) {
		this.dimissionDate = dimissionDate;
	}

	@Column(name = "DIMISSION_REASON", length = 200)
	public String getDimissionReason() {
		return this.dimissionReason;
	}

	public void setDimissionReason(String dimissionReason) {
		this.dimissionReason = dimissionReason;
	}

	@Column(name = "IF_SAVE", length = 1)
	public String getIfSave() {
		return this.ifSave;
	}

	public void setIfSave(String ifSave) {
		this.ifSave = ifSave;
	}

	@Column(name = "WHITHER", length = 50)
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

	@Column(name = "ADVICENOTE_NO", length = 20)
	public String getAdvicenoteNo() {
		return this.advicenoteNo;
	}

	public void setAdvicenoteNo(String advicenoteNo) {
		this.advicenoteNo = advicenoteNo;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "STOPSALARY_DATE", length = 7)
	public Date getStopsalaryDate() {
		return this.stopsalaryDate;
	}

	public void setStopsalaryDate(Date stopsalaryDate) {
		this.stopsalaryDate = stopsalaryDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "REGISTER_DATE", length = 7)
	public Date getRegisterDate() {
		return registerDate;
	}

	public void setRegisterDate(Date registerDate) {
		this.registerDate = registerDate;
	}


}