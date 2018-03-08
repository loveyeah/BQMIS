package power.ejb.manage.client;

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * ConCInterval entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_C_INTERVAL")
public class ConCInterval implements java.io.Serializable {

	// Fields

	private Long intervalId;
	private Date beginDate;
	private Date endDate;
	private Long evaluationDays;
	private String memo;
	private String recordBy;
	private Date recordDate;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ConCInterval() {
	}

	/** minimal constructor */
	public ConCInterval(Long intervalId) {
		this.intervalId = intervalId;
	}

	/** full constructor */
	public ConCInterval(Long intervalId, Date beginDate, Date endDate,
			Long evaluationDays, String memo, String recordBy, Date recordDate,
			String enterpriseCode) {
		this.intervalId = intervalId;
		this.beginDate = beginDate;
		this.endDate = endDate;
		this.evaluationDays = evaluationDays;
		this.memo = memo;
		this.recordBy = recordBy;
		this.recordDate = recordDate;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "INTERVAL_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getIntervalId() {
		return this.intervalId;
	}

	public void setIntervalId(Long intervalId) {
		this.intervalId = intervalId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "BEGIN_DATE", length = 7)
	public Date getBeginDate() {
		return this.beginDate;
	}

	public void setBeginDate(Date beginDate) {
		this.beginDate = beginDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "END_DATE", length = 7)
	public Date getEndDate() {
		return this.endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@Column(name = "EVALUATION_DAYS", precision = 10, scale = 0)
	public Long getEvaluationDays() {
		return this.evaluationDays;
	}

	public void setEvaluationDays(Long evaluationDays) {
		this.evaluationDays = evaluationDays;
	}

	@Column(name = "MEMO", length = 200)
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
	@Column(name = "RECORD_DATE", length = 7)
	public Date getRecordDate() {
		return this.recordDate;
	}

	public void setRecordDate(Date recordDate) {
		this.recordDate = recordDate;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}