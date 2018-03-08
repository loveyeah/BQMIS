package power.ejb.manage.client;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * ConJAppraiseRecord entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "CON_J_APPRAISE_RECORD")
public class ConJAppraiseRecord implements java.io.Serializable {

	// Fields

	private Long recordId;
	private Long eventId;
	private Long intervalId;
	private Long cliendId;
	private Double appraisePoint;
	private String memo;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public ConJAppraiseRecord() {
	}

	/** minimal constructor */
	public ConJAppraiseRecord(Long recordId) {
		this.recordId = recordId;
	}

	/** full constructor */
	public ConJAppraiseRecord(Long recordId, Long eventId, Long intervalId,
			Long cliendId, Double appraisePoint, String memo,
			String enterpriseCode) {
		this.recordId = recordId;
		this.eventId = eventId;
		this.intervalId = intervalId;
		this.cliendId = cliendId;
		this.appraisePoint = appraisePoint;
		this.memo = memo;
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

	@Column(name = "EVENT_ID", precision = 10, scale = 0)
	public Long getEventId() {
		return this.eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	@Column(name = "INTERVAL_ID", precision = 10, scale = 0)
	public Long getIntervalId() {
		return this.intervalId;
	}

	public void setIntervalId(Long intervalId) {
		this.intervalId = intervalId;
	}

	@Column(name = "CLIEND_ID", precision = 10, scale = 0)
	public Long getCliendId() {
		return this.cliendId;
	}

	public void setCliendId(Long cliendId) {
		this.cliendId = cliendId;
	}

	@Column(name = "APPRAISE_POINT", precision = 3, scale = 1)
	public Double getAppraisePoint() {
		return this.appraisePoint;
	}

	public void setAppraisePoint(Double appraisePoint) {
		this.appraisePoint = appraisePoint;
	}

	@Column(name = "MEMO", length = 100)
	public String getMemo() {
		return this.memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}