package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtKkxJFjregister entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_KKX_J_FJREGISTER")
public class PtKkxJFjregister implements java.io.Serializable {

	// Fields

	private Long fjId;
	private String strMonth;
	private String fjCode;
	private Date startDate;
	private Date endDate;
	private Long jzztId;
	private Double keepTime;
	private Long standbyNum;
	private Double repairMandays;
	private Double repairCost;
	private String eventCode;
	private String eventReason;
	private String eventOtherReason;
	private String entryBy;
	private Date entryDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtKkxJFjregister() {
	}

	/** minimal constructor */
	public PtKkxJFjregister(Long fjId) {
		this.fjId = fjId;
	}

	/** full constructor */
	public PtKkxJFjregister(Long fjId, String strMonth, String fjCode,
			Date startDate, Date endDate, Long jzztId, Double keepTime,
			Long standbyNum, Double repairMandays, Double repairCost,
			String eventCode, String eventReason, String eventOtherReason,
			String entryBy, Date entryDate, String enterpriseCode) {
		this.fjId = fjId;
		this.strMonth = strMonth;
		this.fjCode = fjCode;
		this.startDate = startDate;
		this.endDate = endDate;
		this.jzztId = jzztId;
		this.keepTime = keepTime;
		this.standbyNum = standbyNum;
		this.repairMandays = repairMandays;
		this.repairCost = repairCost;
		this.eventCode = eventCode;
		this.eventReason = eventReason;
		this.eventOtherReason = eventOtherReason;
		this.entryBy = entryBy;
		this.entryDate = entryDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "FJ_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getFjId() {
		return this.fjId;
	}

	public void setFjId(Long fjId) {
		this.fjId = fjId;
	}

	@Column(name = "STR_MONTH", length = 10)
	public String getStrMonth() {
		return this.strMonth;
	}

	public void setStrMonth(String strMonth) {
		this.strMonth = strMonth;
	}

	@Column(name = "FJ_CODE", length = 20)
	public String getFjCode() {
		return this.fjCode;
	}

	public void setFjCode(String fjCode) {
		this.fjCode = fjCode;
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

	@Column(name = "JZZT_ID", precision = 10, scale = 0)
	public Long getJzztId() {
		return this.jzztId;
	}

	public void setJzztId(Long jzztId) {
		this.jzztId = jzztId;
	}

	@Column(name = "KEEP_TIME", precision = 10)
	public Double getKeepTime() {
		return this.keepTime;
	}

	public void setKeepTime(Double keepTime) {
		this.keepTime = keepTime;
	}

	@Column(name = "STANDBY_NUM", precision = 10, scale = 0)
	public Long getStandbyNum() {
		return this.standbyNum;
	}

	public void setStandbyNum(Long standbyNum) {
		this.standbyNum = standbyNum;
	}

	@Column(name = "REPAIR_MANDAYS", precision = 10, scale = 1)
	public Double getRepairMandays() {
		return this.repairMandays;
	}

	public void setRepairMandays(Double repairMandays) {
		this.repairMandays = repairMandays;
	}

	@Column(name = "REPAIR_COST", precision = 10)
	public Double getRepairCost() {
		return this.repairCost;
	}

	public void setRepairCost(Double repairCost) {
		this.repairCost = repairCost;
	}

	@Column(name = "EVENT_CODE", length = 20)
	public String getEventCode() {
		return this.eventCode;
	}

	public void setEventCode(String eventCode) {
		this.eventCode = eventCode;
	}

	@Column(name = "EVENT_REASON", length = 500)
	public String getEventReason() {
		return this.eventReason;
	}

	public void setEventReason(String eventReason) {
		this.eventReason = eventReason;
	}

	@Column(name = "EVENT_OTHER_REASON", length = 500)
	public String getEventOtherReason() {
		return this.eventOtherReason;
	}

	public void setEventOtherReason(String eventOtherReason) {
		this.eventOtherReason = eventOtherReason;
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