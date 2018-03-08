package power.ejb.manage.stat;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ZbJReportLoad entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "ZB_J_REPORT_LOAD")
public class ZbJReportLoad implements java.io.Serializable {

	// Fields

	private Long loadId;
	private String loadCode;
	private String loadName;
	private String reportCode;
	private String reportTime;
	private String annexAddress;
	private String loadBy;
	private Date loadDate;
	private String firstDeptCode;
	private String isUse;
	private String enterpriseCode;
	private String deleteBy;
	private Date deleteDate;

	// Constructors

	/** default constructor */
	public ZbJReportLoad() {
	}

	/** minimal constructor */
	public ZbJReportLoad(Long loadId) {
		this.loadId = loadId;
	}

	/** full constructor */
	public ZbJReportLoad(Long loadId, String loadCode, String loadName,
			String reportCode, String reportTime, String annexAddress,
			String loadBy, Date loadDate, String firstDeptCode, String isUse,
			String enterpriseCode, String deleteBy, Date deleteDate) {
		this.loadId = loadId;
		this.loadCode = loadCode;
		this.loadName = loadName;
		this.reportCode = reportCode;
		this.reportTime = reportTime;
		this.annexAddress = annexAddress;
		this.loadBy = loadBy;
		this.loadDate = loadDate;
		this.firstDeptCode = firstDeptCode;
		this.isUse = isUse;
		this.enterpriseCode = enterpriseCode;
		this.deleteBy = deleteBy;
		this.deleteDate = deleteDate;
	}

	// Property accessors
	@Id
	@Column(name = "LOAD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getLoadId() {
		return this.loadId;
	}

	public void setLoadId(Long loadId) {
		this.loadId = loadId;
	}

	@Column(name = "LOAD_CODE", length = 50)
	public String getLoadCode() {
		return this.loadCode;
	}

	public void setLoadCode(String loadCode) {
		this.loadCode = loadCode;
	}

	@Column(name = "LOAD_NAME", length = 100)
	public String getLoadName() {
		return this.loadName;
	}

	public void setLoadName(String loadName) {
		this.loadName = loadName;
	}

	@Column(name = "REPORT_CODE", length = 20)
	public String getReportCode() {
		return this.reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	@Column(name = "REPORT_TIME", length = 10)
	public String getReportTime() {
		return this.reportTime;
	}

	public void setReportTime(String reportTime) {
		this.reportTime = reportTime;
	}

	@Column(name = "ANNEX_ADDRESS", length = 100)
	public String getAnnexAddress() {
		return this.annexAddress;
	}

	public void setAnnexAddress(String annexAddress) {
		this.annexAddress = annexAddress;
	}

	@Column(name = "LOAD_BY", length = 20)
	public String getLoadBy() {
		return this.loadBy;
	}

	public void setLoadBy(String loadBy) {
		this.loadBy = loadBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "LOAD_DATE", length = 7)
	public Date getLoadDate() {
		return this.loadDate;
	}

	public void setLoadDate(Date loadDate) {
		this.loadDate = loadDate;
	}

	@Column(name = "FIRST_DEPT_CODE", length = 20)
	public String getFirstDeptCode() {
		return this.firstDeptCode;
	}

	public void setFirstDeptCode(String firstDeptCode) {
		this.firstDeptCode = firstDeptCode;
	}

	@Column(name = "IS_USE", length = 1)
	public String getIsUse() {
		return this.isUse;
	}

	public void setIsUse(String isUse) {
		this.isUse = isUse;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

	@Column(name = "DELETE_BY", length = 20)
	public String getDeleteBy() {
		return this.deleteBy;
	}

	public void setDeleteBy(String deleteBy) {
		this.deleteBy = deleteBy;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "DELETE_DATE", length = 7)
	public Date getDeleteDate() {
		return this.deleteDate;
	}

	public void setDeleteDate(Date deleteDate) {
		this.deleteDate = deleteDate;
	}

}