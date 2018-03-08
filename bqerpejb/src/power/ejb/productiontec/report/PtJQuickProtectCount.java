package power.ejb.productiontec.report;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJQuickProtectCount entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_J_QUICK_PROTECT_COUNT", schema = "POWER")
public class PtJQuickProtectCount implements java.io.Serializable {

	// Fields

	private Long chekupId;
	private String strMonth;
	private String protectEqu;
	private String protectDevice;
	private String protectType;
	private Date exitDate;
	private Date resumeDate;
	private String exitReason;
	private String checkBy;
	private String approveBy;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJQuickProtectCount() {
	}

	/** minimal constructor */
	public PtJQuickProtectCount(Long chekupId) {
		this.chekupId = chekupId;
	}

	/** full constructor */
	public PtJQuickProtectCount(Long chekupId, String strMonth,
			String protectEqu, String protectDevice, String protectType,
			Date exitDate, Date resumeDate, String exitReason, String checkBy,
			String approveBy, String entryBy, Date entryDate,
			String enterpriseCode) {
		this.chekupId = chekupId;
		this.strMonth = strMonth;
		this.protectEqu = protectEqu;
		this.protectDevice = protectDevice;
		this.protectType = protectType;
		this.exitDate = exitDate;
		this.resumeDate = resumeDate;
		this.exitReason = exitReason;
		this.checkBy = checkBy;
		this.approveBy = approveBy;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "CHEKUP_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getChekupId() {
		return this.chekupId;
	}

	public void setChekupId(Long chekupId) {
		this.chekupId = chekupId;
	}

	@Column(name = "STR_MONTH", length = 10)
	public String getStrMonth() {
		return this.strMonth;
	}

	public void setStrMonth(String strMonth) {
		this.strMonth = strMonth;
	}

	@Column(name = "PROTECT_EQU", length = 30)
	public String getProtectEqu() {
		return this.protectEqu;
	}

	public void setProtectEqu(String protectEqu) {
		this.protectEqu = protectEqu;
	}

	@Column(name = "PROTECT_DEVICE", length = 30)
	public String getProtectDevice() {
		return this.protectDevice;
	}

	public void setProtectDevice(String protectDevice) {
		this.protectDevice = protectDevice;
	}

	@Column(name = "PROTECT_TYPE", length = 20)
	public String getProtectType() {
		return this.protectType;
	}

	public void setProtectType(String protectType) {
		this.protectType = protectType;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "EXIT_DATE", length = 7)
	public Date getExitDate() {
		return this.exitDate;
	}

	public void setExitDate(Date exitDate) {
		this.exitDate = exitDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "RESUME_DATE", length = 7)
	public Date getResumeDate() {
		return this.resumeDate;
	}

	public void setResumeDate(Date resumeDate) {
		this.resumeDate = resumeDate;
	}

	@Column(name = "EXIT_REASON", length = 200)
	public String getExitReason() {
		return this.exitReason;
	}

	public void setExitReason(String exitReason) {
		this.exitReason = exitReason;
	}

	@Column(name = "CHECK_BY", length = 30)
	public String getCheckBy() {
		return this.checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
	}

	@Column(name = "APPROVE_BY", length = 30)
	public String getApproveBy() {
		return this.approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Column(name = "ENTRY_BY", length = 30)
	public String getEntryBy() {
		return this.entryBy;
	}

	public void setEntryBy(String entryBy) {
		this.entryBy = entryBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "ENTRY_DATE", length = 7)
	public Date getEntryDate() {
		return this.entryDate;
	}

	public void setEntryDate(Date entryDate) {
		this.entryDate = entryDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}