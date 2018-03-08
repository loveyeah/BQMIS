package power.ejb.run.securityproduction;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SpJSafemeetingAttend entity.
 * 
 * @author MyEclipse Persistence Tools
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "SP_J_SAFEMEETING_ATTEND")
public class SpJSafemeetingAttend implements java.io.Serializable {

	// Fields

	private Long attendId;
	private Long meetingId;
	private String workerCode;
	private String depCode;
	private String enterpriseCode;

	// Constructors

	/** default constructor */
	public SpJSafemeetingAttend() {
	}

	/** minimal constructor */
	public SpJSafemeetingAttend(Long attendId) {
		this.attendId = attendId;
	}

	/** full constructor */
	public SpJSafemeetingAttend(Long attendId, Long meetingId,
			String workerCode, String depCode, String enterpriseCode) {
		this.attendId = attendId;
		this.meetingId = meetingId;
		this.workerCode = workerCode;
		this.depCode = depCode;
		this.enterpriseCode = enterpriseCode;
	}

	// Property accessors
	@Id
	@Column(name = "ATTEND_ID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getAttendId() {
		return this.attendId;
	}

	public void setAttendId(Long attendId) {
		this.attendId = attendId;
	}

	@Column(name = "MEETING_ID", precision = 10, scale = 0)
	public Long getMeetingId() {
		return this.meetingId;
	}

	public void setMeetingId(Long meetingId) {
		this.meetingId = meetingId;
	}

	@Column(name = "WORKER_CODE", length = 16)
	public String getWorkerCode() {
		return this.workerCode;
	}

	public void setWorkerCode(String workerCode) {
		this.workerCode = workerCode;
	}

	@Column(name = "DEP_CODE", length = 20)
	public String getDepCode() {
		return this.depCode;
	}

	public void setDepCode(String depCode) {
		this.depCode = depCode;
	}

	@Column(name = "ENTERPRISE_CODE", length = 20)
	public String getEnterpriseCode() {
		return this.enterpriseCode;
	}

	public void setEnterpriseCode(String enterpriseCode) {
		this.enterpriseCode = enterpriseCode;
	}

}