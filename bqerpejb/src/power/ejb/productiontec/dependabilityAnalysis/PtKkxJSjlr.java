package power.ejb.productiontec.dependabilityAnalysis;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * PtKkxJSjlr entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "PT_KKX_J_SJLR")
public class PtKkxJSjlr implements java.io.Serializable {

	// Fields

	private Long sjlrId;
	private String blockCode;
	private Long jzztId;
	private Date startDate;
	private Date endDate;
	private Double keepTime;
	private Double reduceExert;
	private Long stopTimes;
	private Long successTimes;
	private Long failureTimes;
	private Double repairMandays;
	private Double repairCost;
	private String stopReason;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public PtKkxJSjlr() {
	}

	/** minimal constructor */
	public PtKkxJSjlr(Long sjlrId) {
		this.sjlrId = sjlrId;
	}

	/** full constructor */
	public PtKkxJSjlr(Long sjlrId, String blockCode, Long jzztId,
			Date startDate, Date endDate, Double keepTime, Double reduceExert,
			Long stopTimes, Long successTimes, Long failureTimes,
			Double repairMandays, Double repairCost, String stopReason,
			String enterpriseCode) {
		this.sjlrId = sjlrId;
		this.blockCode = blockCode;
		this.jzztId = jzztId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.keepTime = keepTime;
		this.reduceExert = reduceExert;
		this.stopTimes = stopTimes;
		this.successTimes = successTimes;
		this.failureTimes = failureTimes;
		this.repairMandays = repairMandays;
		this.repairCost = repairCost;
		this.stopReason = stopReason;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "SJLR_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getSjlrId() {
		return this.sjlrId;
	}

	public void setSjlrId(Long sjlrId) {
		this.sjlrId = sjlrId;
	}

	@Column(name = "BLOCK_CODE", length = 2)
	public String getBlockCode() {
		return this.blockCode;
	}

	public void setBlockCode(String blockCode) {
		this.blockCode = blockCode;
	}

	@Column(name = "JZZT_ID", precision = 10, scale = 0)
	public Long getJzztId() {
		return this.jzztId;
	}

	public void setJzztId(Long jzztId) {
		this.jzztId = jzztId;
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

	@Column(name = "KEEP_TIME", precision = 10)
	public Double getKeepTime() {
		return this.keepTime;
	}

	public void setKeepTime(Double keepTime) {
		this.keepTime = keepTime;
	}

	@Column(name = "REDUCE_EXERT", precision = 15)
	public Double getReduceExert() {
		return this.reduceExert;
	}

	public void setReduceExert(Double reduceExert) {
		this.reduceExert = reduceExert;
	}

	@Column(name = "STOP_TIMES", precision = 4, scale = 0)
	public Long getStopTimes() {
		return this.stopTimes;
	}

	public void setStopTimes(Long stopTimes) {
		this.stopTimes = stopTimes;
	}

	@Column(name = "SUCCESS_TIMES", precision = 4, scale = 0)
	public Long getSuccessTimes() {
		return this.successTimes;
	}

	public void setSuccessTimes(Long successTimes) {
		this.successTimes = successTimes;
	}

	@Column(name = "FAILURE_TIMES", precision = 4, scale = 0)
	public Long getFailureTimes() {
		return this.failureTimes;
	}

	public void setFailureTimes(Long failureTimes) {
		this.failureTimes = failureTimes;
	}

	@Column(name = "REPAIR_MANDAYS", precision = 10)
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

	@Column(name = "STOP_REASON", length = 500)
	public String getStopReason() {
		return this.stopReason;
	}

	public void setStopReason(String stopReason) {
		this.stopReason = stopReason;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}