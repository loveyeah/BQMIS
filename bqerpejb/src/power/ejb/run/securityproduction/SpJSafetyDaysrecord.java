package power.ejb.run.securityproduction;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SpJSafetyDaysrecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SP_J_SAFETY_DAYSRECORD", schema = "POWER")
public class SpJSafetyDaysrecord implements java.io.Serializable {

	// Fields

	private Long recordId;
	private Date startDate;
	private Date endDate;
	private String ifBreak;
	private Long safetyDays;
	private String memo;
	private String recordBy;
	private Date recordTime;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafetyDaysrecord() {
	}

	/** minimal constructor */
	public SpJSafetyDaysrecord(Long recordId) {
		this.recordId = recordId;
	}

	/** full constructor */
	public SpJSafetyDaysrecord(Long recordId, Date startDate, Date endDate,
			String ifBreak, Long safetyDays, String memo, String recordBy,
			Date recordTime, String enterpriseCode) {
		this.recordId = recordId;
		this.startDate = startDate;
		this.endDate = endDate;
		this.ifBreak = ifBreak;
		this.safetyDays = safetyDays;
		this.memo = memo;
		this.recordBy = recordBy;
		this.recordTime = recordTime;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "RECORD_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRecordId() {
		return this.recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "START_DATE", length = 7)
	public Date getStartDate() {
		return this.startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "IF_BREAK", length = 1)
	public String getIfBreak() {
		return this.ifBreak;
	}

	public void setIfBreak(String ifBreak) {
		this.ifBreak = ifBreak;
	}

	@Column(name = "SAFETY_DAYS", precision = 10, scale = 0)
	public Long getSafetyDays() {
		return this.safetyDays;
	}

	public void setSafetyDays(Long safetyDays) {
		this.safetyDays = safetyDays;
	}

	@Column(name = "MEMO", length = 500)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "RECORD_BY", length = 16)
	public String getRecordBy() {
		return this.recordBy;
	}

	public void setRecordBy(String recordBy) {
		this.recordBy = recordBy;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "RECORD_TIME", length = 7)
	public Date getRecordTime() {
		return this.recordTime;
	}

	public void setRecordTime(Date recordTime) {
		this.recordTime = recordTime;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}