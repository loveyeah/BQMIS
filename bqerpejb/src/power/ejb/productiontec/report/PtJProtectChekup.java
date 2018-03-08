package power.ejb.productiontec.report;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtJProtectChekup entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_J_PROTECT_CHEKUP", schema = "POWER")
public class PtJProtectChekup implements java.io.Serializable {

	// Fields

	private Long chekupId;
	private String strMonth;
	private String protectEqu;
	private String protectDevice;
	private Date lastCheckDate;
	private Date planFinishDate;
	private Date factFinishDate;
	private String finishThing;
	private String notFinishReason;
	private String hasProblem;
	private String approveBy;
	private String checkBy;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtJProtectChekup() {
	}

	/** minimal constructor */
	public PtJProtectChekup(Long chekupId) {
		this.chekupId = chekupId;
	}

	/** full constructor */
	public PtJProtectChekup(Long chekupId, String strMonth, String protectEqu,
			String protectDevice, Date lastCheckDate, Date planFinishDate,
			Date factFinishDate, String finishThing, String notFinishReason,
			String hasProblem, String approveBy, String checkBy,
			String entryBy, Date entryDate, String enterpriseCode) {
		this.chekupId = chekupId;
		this.strMonth = strMonth;
		this.protectEqu = protectEqu;
		this.protectDevice = protectDevice;
		this.lastCheckDate = lastCheckDate;
		this.planFinishDate = planFinishDate;
		this.factFinishDate = factFinishDate;
		this.finishThing = finishThing;
		this.notFinishReason = notFinishReason;
		this.hasProblem = hasProblem;
		this.approveBy = approveBy;
		this.checkBy = checkBy;
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

	@Temporal(TemporalType.DATE)
	@Column(name = "LAST_CHECK_DATE", length = 7)
	public Date getLastCheckDate() {
		return this.lastCheckDate;
	}

	public void setLastCheckDate(Date lastCheckDate) {
		this.lastCheckDate = lastCheckDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "PLAN_FINISH_DATE", length = 7)
	public Date getPlanFinishDate() {
		return this.planFinishDate;
	}

	public void setPlanFinishDate(Date planFinishDate) {
		this.planFinishDate = planFinishDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "FACT_FINISH_DATE", length = 7)
	public Date getFactFinishDate() {
		return this.factFinishDate;
	}

	public void setFactFinishDate(Date factFinishDate) {
		this.factFinishDate = factFinishDate;
	}

	@Column(name = "FINISH_THING", length = 50)
	public String getFinishThing() {
		return this.finishThing;
	}

	public void setFinishThing(String finishThing) {
		this.finishThing = finishThing;
	}

	@Column(name = "NOT_FINISH_REASON", length = 50)
	public String getNotFinishReason() {
		return this.notFinishReason;
	}

	public void setNotFinishReason(String notFinishReason) {
		this.notFinishReason = notFinishReason;
	}

	@Column(name = "HAS_PROBLEM", length = 50)
	public String getHasProblem() {
		return this.hasProblem;
	}

	public void setHasProblem(String hasProblem) {
		this.hasProblem = hasProblem;
	}

	@Column(name = "APPROVE_BY", length = 30)
	public String getApproveBy() {
		return this.approveBy;
	}

	public void setApproveBy(String approveBy) {
		this.approveBy = approveBy;
	}

	@Column(name = "CHECK_BY", length = 30)
	public String getCheckBy() {
		return this.checkBy;
	}

	public void setCheckBy(String checkBy) {
		this.checkBy = checkBy;
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